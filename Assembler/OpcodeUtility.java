package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class OpcodeUtility {
    private static HashMap<String, ArrayList<Object>> OPTab;    // optable : key -> opcode, value -> {length, hexcode}
    // Initialize OP Table
    public static void OPTabInit() {
        OPTab = new HashMap<String, ArrayList<Object>>();
        try(BufferedReader inputReader = new BufferedReader(new FileReader("opcodes.txt"))){
            String instruction = inputReader.readLine();
            StringTokenizer tokenizer;
            while(instruction != null){
                tokenizer = new StringTokenizer(instruction, " ");
                String op = tokenizer.nextToken();
                Integer length = Integer.parseInt(tokenizer.nextToken());
                String hexcode = tokenizer.nextToken();
                ArrayList<Object> arr = new ArrayList<Object>(); arr.add(length); arr.add(hexcode);
                OPTab.put(op, arr);
                instruction = inputReader.readLine();
            }
        } catch (IOException e){
            System.out.println("Problem opening opcodes.txt file.");
        }
    }
    // Get the format code for a given opcode. For example, LDA returns 3
    public static int getFormat(String opcode){
        boolean format4 = false;
        if(opcode.contains("+")) {
            format4 = true;
            opcode = opcode.substring(1);
        }

        if (OpcodeUtility.OPTab.containsKey(opcode)) {
            ArrayList<Object> val = OpcodeUtility.OPTab.get(opcode);
            // if opcode found fetch the second column or return 4 if format4 is true
            if(format4)
                return 4;
            else
                return ((int)(val.get(0)));
        }

        return -1;
    }

    // Get the Hex code for a given opcode. For example, LDA returns 00
    public static String getHexCode(String opcode){
        if(opcode.contains("+"))
            opcode = opcode.substring(1);

        if (OpcodeUtility.OPTab.containsKey(opcode)) {
            ArrayList<Object> val = OpcodeUtility.OPTab.get(opcode);
            return val.get(1).toString();
        }

        return null;
    }
}
