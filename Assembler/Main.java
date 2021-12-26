package Assembler;

import OperandPkg.Literal;
import SymbolPkg.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

// Input : A SICXE .asm file
// Output : Object Code.
// During Pass 1 : One intermediate file ending in .int with location counter value added to every line.
// During Pass 2 : Listing file prepared by updating intermediate file ending in .txt extension with object code added to the right of every instruction.
// And one object file ending in .o extension.
 
public class Main {
    public static void main(String[] args) throws IOException{
        SymbolTable symbolTable = new SymbolTable();
        LinkedList<Literal> literalTable = new LinkedList<>();
        ArrayList<String> errorsCaught = new ArrayList<>();

        // set input files
        String inputFile;
        if(args.length < 1){
            System.out.println("Missing command like argument.");

            // request the intput SIC/XE Assembly file
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter SIC Assembly Source file : ");
            inputFile = reader.readLine();
        } else {
            inputFile = args[0];
        }

        System.out.println("Source File : " + inputFile);

        // Generate OPTAB
        OpcodeUtility.OPTabInit();

        // ********************** PASS 1 **********************
        System.out.println("\n*********** PASS 1 ***********");
        
        // create an intermediate file in .int extension, and populate symbol and literal table
        Pass1Utility.generateIntermediate(inputFile, symbolTable, literalTable, errorsCaught);

        // print the .int file
        String intFileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".int");
        System.out.println("\n> Intermediate File : " + intFileName);
        Utility.printFile(intFileName);

        // display errors, if any
        if (errorsCaught.size() > 0) {
            for (int i = 0; i < errorsCaught.size(); ++i)
                System.out.println(errorsCaught.get(i));
            System.out.println("Terminating...");
            return;
        }

        // print the symbol table
        System.out.println("\n> Symbol Table\nSymbol\t\tValue");
        int pass1symbolTableSize = symbolTable.size();
        if(pass1symbolTableSize != 0)
            symbolTable.view();
        else
            System.out.println("(Symbol Table is Empty)");

        // print the literal table
        if(literalTable.size() != 0) {
            System.out.println("\n> Literal Table\nLiteral\t\t\tValue\t\tlength\taddress");
            for (Literal literal : literalTable)
                System.out.println(literal);
        }

        // ********************** PASS 2 **********************
        System.out.println("\n\n*********** PASS 2 ***********");
        
        // create an updated intermediate file in .txt extension, and object file in .o extension
        Pass2Utility.generateObj(inputFile, symbolTable, literalTable);

        // print the updated intermediate code
        String txtfileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".txt");
        System.out.println("\n> Listing File: " + txtfileName);
        Utility.printFile(txtfileName);

        // print the updated symbol table
        if(symbolTable.size() != pass1symbolTableSize) {
            System.out.println("\n> Updated Symbol Table\nSymbol\t\tValue");
            symbolTable.view();
        }

        // print the generated object code
        String objFileName = inputFile.substring(0, inputFile.indexOf('.')).concat(".o");
        System.out.println("\n> Object Code : " + objFileName);
        Utility.printFile(objFileName);
    }
}
