import java.util.ArrayList;


public class Node {
	char nextChar;
	ArrayList<Node> successors;
	ArrayList<Character> transitionChars;
	boolean accept;
	boolean start;
	public Node(){
		this.start = true;
		successors = new ArrayList<Node>();
	}
	public Node(char next){
		this.start = false;
		transitionChars = new ArrayList<Character>();
		successors = new ArrayList<Node>();
	}
	public Node(char next, boolean accept){
		this.start = false;
		this.accept = accept;
		transitionChars = new ArrayList<Character>();
		successors = new ArrayList<Node>();
	}
	public Node(ArrayList<Character> trans, boolean accept){
		this.start = false;
		this.accept = accept;
		transitionChars = trans;
		successors = new ArrayList<Node>();
	}
	public void addSuccessor(char c){
		this.successors.add(new Node(c));
	}
	
	public void addSuccessor(Node node){
		this.successors.add(node);
	}
}
