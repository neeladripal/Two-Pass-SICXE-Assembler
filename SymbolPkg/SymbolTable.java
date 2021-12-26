package SymbolPkg;
import java.util.ArrayList;


// SymbolTable uses a BinaryTree to store symbols and the location values.
// provides error checking before data entry.
public class SymbolTable {
    BinaryTree binaryTree = new BinaryTree();

    // add a node to the table
    public void add(Node symbol){
        Node tempNode = binaryTree.search(symbol);
        if(tempNode != null){
            // System.out.println("Duplicate Symbol : " + tempNode.getKey());
            tempNode.setMflag(true);
        }
        else {
            binaryTree.insert(symbol);
        }
    }

    // Return duplicate symbols
    public ArrayList<String> getDuplicateSymbols() {
        return binaryTree.dupNodes();
    }
    
    // // Read an entire line, validate every token. Upon successful validation, enter the symbol into the symbol table.
    // // This function will use StringTokenizer to parse individual attributes.
    // // If any invalid entry, the function returns without entering the symbol into the symbol table.
    // public void addLine(String line){
    //     StringTokenizer tokenizer = new StringTokenizer(line);
    //     Node node = new Node();
    //     while(tokenizer.hasMoreTokens()){
    //         String tempToken = tokenizer.nextToken();

    //         // check for valid symbol
    //         SymbolStatus symbolStatus = SymbolTableUtility.validateSymbol(tempToken);
    //         if(symbolStatus == SymbolStatus.Valid){
    //             // Check for duplication
    //             Node tempNode = binaryTree.search(new Node(tempToken)); // returns null is Node isn't found
    //             if(tempNode != null){
    //                 // duplicate found, so set the mflag to true
    //                 tempNode.setMflag(true);
    //                 System.out.println("Duplicate Symbol : " + tempToken);
    //                 break;
    //             } else {
    //                 node.setKey(tempToken);
    //             }
    //         // invalid symbol
    //         } else {
    //             System.out.println("Invalid Symbol : " + tempToken + " -> " + symbolStatus.getDetails());
    //             break;  // Once an error found the entire line is discarded
    //         }
    //         tempToken = tokenizer.nextToken(); // Valid symbol is found, now try to get the value of the symbol

    //         // check for valid value
    //         if(Utility.isInteger(tempToken)){
    //             node.setValue(Integer.parseInt(tempToken));
    //         } else {
    //             System.out.println("Invalid Value : " + tempToken);
    //             break;
    //         }
    //         tempToken = tokenizer.nextToken();  // Valid value of the symbol is found, now try to get the rflag of the symbol

    //         // check for valid rflag
    //         RFlagStatus rFlagStatus = SymbolTableUtility.validateRFlag(tempToken);
    //         if(!(rFlagStatus == RFlagStatus.Invalid)){
    //             if(rFlagStatus == RFlagStatus.False)
    //                     node.setRflag(false);
    //                 else
    //                     node.setRflag(true);
    //             } else {
    //                 System.out.println("Invalid Rflag : " + tempToken);
    //                 break;
    //         }

    //         // set the remaining iflag and mflag to true and false by default
    //         node.setIflag(true);
    //         node.setMflag(false);
    //         binaryTree.insert(node);
    //     }
    // }

    // Print the binary tree.
    public void view(){
        binaryTree.view();
    }

    // Search for a node in the Binary tree
    public Node search(String symbol){
        return binaryTree.search(new Node(symbol));
    }

    // Call getAll() method of the BinaryTree.
    public ArrayList<Node> getAll(){
        return binaryTree.getAll();
    }

    // Get all external symbols
    public ArrayList<Node> getAllExternal(){
        ArrayList<Node> allExternal = new ArrayList<>();

        for(Node symbol : getAll()){
            if(!symbol.iflag){
                allExternal.add(symbol);
            }
        }

        return allExternal;
    }

    public ArrayList<Node> getAllInternal(){
        ArrayList<Node> allInternal = new ArrayList<>();

        for(Node symbol : getAll()){
            if(symbol.iflag){
                allInternal.add(symbol);
            }
        }

        return allInternal;
    }

    public int size(){
        return getAll().size();
    }
}
