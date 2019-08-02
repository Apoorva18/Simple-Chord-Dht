package edu.buffalo.cse.cse486586.simpledht;

public class Node implements Comparable<Node> {
    String portNo;
    String hash;
    String successor;
    String predecessor;

    public Node(String portNo, String hash, String successor, String pred) {
        this.portNo = portNo;
        this.hash = hash;
        this.successor = successor;
        this.predecessor = pred;

    }

    public String geth() {
        return hash;
    }

    public String getPortNo() {
        return portNo;
    }

    public String getSuccessor() {
        return successor;
    }

    public String getPredecessor() {
        return predecessor;
    }

    @Override
    public int compareTo(Node o) {
        if ((this.geth().compareTo(o.geth()) > 0))
            return 1;
        else if ((this.geth().compareTo(o.geth()) < 0))
            return -1;


        else {
            return 0;
        }
    }
}