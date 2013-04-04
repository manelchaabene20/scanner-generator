import java.util.ArrayList;

public class NFA {
	private String name;
	private int[][] NFAtable;
	private ArrayList<Character> acceptedChars;
	private ArrayList<Node> starterNodes;
	NFA prev;
	Node startState;

	public NFA(String name) throws Exception{
		this.name = name;
		this.starterNodes = new ArrayList<Node>();
		
		
	}
	public NFA(String name, String regex) throws Exception {
		this.name = name;
		acceptedChars = new ArrayList<Character>();
		this.startState = parse(regex,0);
		
		
	}

	public NFA(String name, String regex, NFA prev) throws Exception {
		this.acceptedChars = new ArrayList<Character>();
		this.name = name;
		this.prev = prev;
		this.startState = parse(regex,1);
		
	
	}

	public Node parse(String regex, int method) throws Exception {

		/* Parsing genius code */
		int state = 0;
		int index = 0;
		char c;
		
		c = regex.charAt(index);

		/* Figure out accepted characters in a [] character class */
		if(method == 0){
			if (c == '[') {
				c = regex.charAt(++index);
				boolean accepted = true;
				if (c == '^') {
					accepted = false;
				}
				while (c != ']') {
					if (c == '-') {
						char start = acceptedChars.get(acceptedChars.size() - 1);
						char end = regex.charAt(index + 1);
	
						if (start > end) {
							throw new Exception("Range is invalid.");
						}
	
						for (int i = (int) start + 1; i < (int) end; i++) {
							acceptedChars.add((char) i);
						}
					} else if (c == '\\') { // NOTE: have to escape the \ char in
											// Java
						c = regex.charAt(++index);
						acceptedChars.add(c);
					} else { // All other characters
						acceptedChars.add(c);
					}
					if (c != ']')
						c = regex.charAt(++index);
				}
				if (accepted == false) { // We have a ^
					ArrayList<Character> actualAcceptedChars = new ArrayList<Character>();
					for (int i = 0; i < 256; i++) { // Loop through all possible
													// chars
						if (!acceptedChars.contains((char) i)) {
							actualAcceptedChars.add((char) i);
						}
					}
					acceptedChars = actualAcceptedChars;
				}
				index++; // Increment past ']'
			}
	
			/* Build NFA table */
			Node node = new Node();
			node.addSuccessor(new Node(acceptedChars,true));
			return node;
		}
		else if(method == 1){
			if (c == '[') {
				c = regex.charAt(++index);
				boolean accepted = true;
				if (c == '^') {
					accepted = true;
				}
				while (c != ']') {
					if (c == '-') {
						char start = acceptedChars.get(acceptedChars.size() - 1);
						char end = regex.charAt(index + 1);
	
						if (start > end) {
							throw new Exception("Range is invalid.");
						}
	
						for (int i = (int) start + 1; i < (int) end; i++) {
							acceptedChars.add((char) i);
						}
					} else if (c == '\\') { // NOTE: have to escape the \ char in
											// Java
						c = regex.charAt(++index);
						acceptedChars.add(c);
					} else { // All other characters
						acceptedChars.add(c);
					}
					if (c != ']')
						c = regex.charAt(++index);
				}
				if (accepted == false) { // We have a ^
					ArrayList<Character> actualAcceptedChars = new ArrayList<Character>();
					for (int i = 0; i < 256; i++) { // Loop through all possible
													// chars
						if (!acceptedChars.contains((char) i)) {
							actualAcceptedChars.add((char) i);
						}
					}
					acceptedChars = actualAcceptedChars;
				}
				index++; // Increment past ']'
			}
			ArrayList<Character> largerList = this.prev.getAcceptedChars();
			for(Character ch : acceptedChars){
				largerList.remove(ch);
			}
			Node node = new Node();
			node.addSuccessor(new Node(largerList,true));
			return node;
		}
		else
			return null;
	}

	public void print() {
		for(Node n : this.startState.getSuccessors())
		{
			System.out.println(n.transitionChars);
		}
	}
	public ArrayList<Character> getAcceptedChars(){
		return this.acceptedChars;
	}
	public static void main(String[] args) throws Exception {
		
		NFA nfa = new NFA("$CHAR", "[A-z]");

		NFA nfa2 = new NFA("$Upper", "[^a-z]", nfa);
		nfa2.print();
	}

}
