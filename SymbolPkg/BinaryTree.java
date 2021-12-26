package SymbolPkg;

import java.util.ArrayList;

// class to insert, search and traverse nodes in a binary search tree
public class BinaryTree{
    private Node rootNode;

    // insert a new node in the tree
    private void insert(Node parentNode, Node newNode){
        // If the parentNode is null, this is the first node in the tree
        if(parentNode == null){
            rootNode = newNode;
            return;
        }

        // if the parentNode isn't null
        if(newNode.getKey().compareTo(parentNode.getKey()) < 0){
            // go to left child
            if(parentNode.leftNode != null){
                    insert(parentNode.leftNode, newNode);
            } else {
                parentNode.leftNode = newNode;
            }
        }
        else if(newNode.getKey().compareTo(parentNode.getKey()) > 0){
            // go to right child
            if(parentNode.rightNode != null){
                    insert(parentNode.rightNode, newNode);
            } else {
                parentNode.rightNode = newNode;
            }
        }

    //    Not necessay, because duplication check is done before calling insert method
    //    else if(newNode.getKey().compareTo(parentNode.getKey()) == 0){
    //        // Duplicate key - set mflag to false
    //    }

    }

    // public method to expose the insert method
    public void insert(Node newNode){
        // insert starts with comparing with rootNode
        insert(this.rootNode, newNode);
    }

    // search for a node in the tree
    private Node search(Node parentNode, Node node){
        if(parentNode == null){
            return null;
        }
        else if(node.getKey().equals(parentNode.getKey())){
            return parentNode;
        }
        else if(node.getKey().compareTo(parentNode.getKey()) < 0){
            return search(parentNode.leftNode, node);
        }
        else if(node.getKey().compareTo(parentNode.getKey()) > 0){
            return search(parentNode.rightNode, node);
        }

        // placeholder, code execution shouldn't come to this point.
        return null;
    }

    // public method to expose the search method
    public Node search(Node node){
        // search starts with comparing with rootNode
        return search(this.rootNode, node);
    }

    // inorder traversal of the tree
    private void view(Node node){
        if(node.leftNode != null){
            view(node.leftNode);
        }

        System.out.println(node);

        if(node.rightNode != null){
            view(node.rightNode);
        }
    }

    // public method to expose the view method
    public void view(){
        if(rootNode != null){
            view(rootNode);
        }
    }

    // find duplicate nodes in an array
    private void dupNodes (Node node, ArrayList<String> dupNodeList) {
        if(node.leftNode != null){
            dupNodes(node.leftNode, dupNodeList);
        }

        if (node.getMflag()) {
            dupNodeList.add(node.getKey());
        }

        if(node.rightNode != null){
            dupNodes(node.rightNode, dupNodeList);
        }
    }

    // public method to expose the dupNodes method
    public ArrayList<String> dupNodes(){
        ArrayList<String> dupNodeList = new ArrayList<String>();
        if(rootNode != null)
            dupNodes(rootNode, dupNodeList);
        return dupNodeList;
    }
    
    // store the nodes in an array
    public ArrayList<Node> getAll(){

        ArrayList<Node> nodeList = new ArrayList<>();

        if(rootNode != null){
            getAll(rootNode, nodeList);
        }

        return nodeList;
    }

    // utility function to store the nodes in an array
    private void getAll(Node node, ArrayList<Node> nodeList){
        if(node.leftNode != null){
            getAll(node.leftNode, nodeList);
        }

        nodeList.add(node);

        if(node.rightNode != null){
            getAll(node.rightNode, nodeList);
        }
    }

}


