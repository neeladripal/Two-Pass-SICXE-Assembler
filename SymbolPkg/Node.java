package SymbolPkg;

import Assembler.Utility;

// Node of a binary tree
public class Node {
    // reference to left and right node
    public Node leftNode, rightNode;

    // this node
    String key;
    public int value;
    public boolean rflag = true, iflag = true, mflag; // default is false

    // Default Constructor
    public Node(){}

    // Parameterized constructor 1
    public Node(String key) {
        this.key = key.toUpperCase();
    }

    // Parameterized constructor 2
    public Node(String key, int value, boolean rflag, boolean iflag, boolean mflag){
        this.key = key.toUpperCase();
        this.value = value;
        this.rflag = rflag;
        this.iflag = iflag;
        this.mflag = mflag;
    }

    @Override
    public String toString(){
        String output = String.format("%1$-15s %2$-9s",
                key,
                Utility.padAddress(value, 5));
        return output;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key) {
        this.key = key.toUpperCase();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean getRflag() {
        return rflag;
    }

    public void setRflag(boolean rflag) {
        this.rflag = rflag;
    }

    public boolean getIflag() {
        return iflag;
    }

    public void setIflag(boolean iflag) {
        this.iflag = iflag;
    }

    public boolean getMflag() {
        return mflag;
    }

    public void setMflag(boolean mflag) {
        this.mflag = mflag;
    }
}