package Assembler;

import OperandPkg.Literal;
import OperandPkg.OperandUtility;
import SymbolPkg.Node;
import SymbolPkg.SymbolTable;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Pass1Utility {
    public static int startAddress = 0;
    public static int locationCounter = startAddress;   // both startAddress and locationCounter will change if START directive is found
    public static int programLength = 0;
    private static int endlocationCounter = 0;
    private static String endSymbol = null;
    public static String controlSectionName = null;

    public static void generateIntermediate (String assemblyFileName, SymbolTable symbolTable, LinkedList<Literal> literalTable, ArrayList<String> errorsCaught) throws IOException {

        // set the output file name, and set a output writer to write on that file
        String intFile = assemblyFileName.substring(0, assemblyFileName.indexOf('.')).concat(".int");
        PrintWriter intWriter = new PrintWriter(intFile, "UTF-8");

        // Take input and process SIC Instruction
        BufferedReader asmReader = new BufferedReader(new FileReader(assemblyFileName));
        String instruction = asmReader.readLine();

        String label, opcode, operand;
        int format = 0;

        while(instruction != null){
            // data flow issue without null initialization
            label = null;
            opcode = null;
            operand = null;
            format = 0;

            // Remove comments from instruction
            instruction = Utility.removeComment(instruction);

            // After comment removal if there is no token, read the next line
            StringTokenizer tokenizer = new StringTokenizer(instruction);
            if(tokenizer.countTokens() == 0){
                instruction = asmReader.readLine();
                continue;
            }

            // Make everything upper case in input instruction

// ************************************************************

            // read label
            Node symbol = null;
            // if first character is a white space then there is no label
            if(!(instruction.charAt(0) <= 32)){
                label = tokenizer.nextToken();

                // populate the symbol table using the label
                symbol = new Node(label);
                symbolTable.add(symbol);
                symbol.value = locationCounter;
            }

            // read opcode
            if(tokenizer.hasMoreTokens())
                opcode = tokenizer.nextToken();
            if(opcode != null){
                // update the rflag of the symbol if opcode is EQU
                if(opcode.equals("EQU") && symbol != null)
                    symbol.rflag = false;

                // format 3 or format 4 opcode
                format = OpcodeUtility.getFormat(opcode);
                if (format == -1) { // invalid opcode
                    // System.out.println("Invalid Opcode found : " + opcode);
                    errorsCaught.add("Error at Location " + Utility.padAddress(locationCounter, 5) + ": Invalid Opcode Found : " + opcode);
                }
                locationCounter += format;
            }

            // read operand
            if(tokenizer.hasMoreTokens())
                operand = tokenizer.nextToken();
            if(operand != null){
                // if instruction has START assembler directive
                if(opcode.equals("START") && Utility.isInteger(operand)) {
                    startAddress = Integer.parseInt(operand);
                    locationCounter = startAddress;
                    controlSectionName = label;
                }

                //
                if(operand.equals("*") && symbol != null) {
                    // update the rflag and value of the symbol if opcode is EQU and operand is *
                    symbol.value = locationCounter;
                    symbol.rflag = true;
                } else {
                    // Evaluate the operand
                    OperandUtility.evaluateOperand(symbolTable, literalTable, operand);
                }
            }

            // We need the line counter of END directive later
            if(opcode.equals("END") && operand != null){
               endlocationCounter = locationCounter;
               endSymbol = operand;
            }

// ************************************************************

            // Handles BYTE operand
            if(opcode.equals("BYTE")){
                String temp = operand.substring(operand.indexOf('\'')+1, operand.lastIndexOf('\''));
                if(operand.contains("C"))
                    format = temp.length();
                else
                    format = temp.length() / 2;

                locationCounter += format;
            }

            // Handles WORD operand
            if(opcode.equals("WORD")){
                format = 3;
                locationCounter += format;
            }

            // Handles RESW operand
            if(opcode.equals("RESW")){
                format = 3 * Integer.parseInt(operand);
                locationCounter += format;
            }

            // Handles RESB operand
            if(opcode.equals("RESB")){
                format = Integer.parseInt(operand);
                locationCounter += format;
            }

            // generate the intermediate instruction
            // 0-7 8-19 20-34 35-49
            String intermediateInstruction = String.format("%-8s%-12s%-20s%-20s",
                    Utility.padAddress(locationCounter-format, 5),
                    ((label == null) ? " " : label ),
                    ((opcode == null) ? " " : opcode ),
                    ((operand == null) ? " " : operand));

            // print the intermediate instruction to terminal and file
            intWriter.println(intermediateInstruction);

            // print the intermediate instruction to terminal
            instruction = asmReader.readLine();
        }

        // close the asm file
        asmReader.close();

        // add the literal lists at the end of the program and update the address of the literals
        for(Literal literal : literalTable){
            String intermediateInstruction = String.format("%-8s*           %-20s", Utility.padAddress(locationCounter-format, 5), literal.name);

            intWriter.println(intermediateInstruction);

            literal.address = locationCounter-format;
            locationCounter += literal.length;
        }

        // Print the program langth; locationCounter is always updated
//        System.out.println("Program Length : " + Integer.toHexString(locationCounter).toUpperCase());
        int sizeOfLiteralTable = locationCounter - endlocationCounter;
        if(endSymbol == null)
            programLength = locationCounter;
        else
            programLength = (endlocationCounter - symbolTable.search(endSymbol).getValue()) + sizeOfLiteralTable;

        // close the intermediate file
        intWriter.close();

        checkDuplicacy(symbolTable, errorsCaught);
    }

    // Returns true if there are duplicate symbols
    public static void checkDuplicacy (SymbolTable symbolTable, ArrayList<String> errorsCaught) throws IOException {
        ArrayList<String> duplicateSymbols = symbolTable.getDuplicateSymbols();
        
        // add duplicate symbols to errors if found any
        if (duplicateSymbols.size() > 0) {
            for (String duplicateSymbol: duplicateSymbols) {
                errorsCaught.add("Duplicate Symbol Found : " + duplicateSymbol);
            }
        }
    }
}
