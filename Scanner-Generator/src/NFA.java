import java.util.ArrayList;

public class NFA {
	private String name;
	private ArrayList<Character> acceptedChars;
	private ArrayList<Character> prev;
	private Node startState;

	public NFA(Node node){
		this.startState = node;
	}
	public NFA(String name) throws Exception {
		this.name = name;
	}

	public NFA(String name, String regex) throws Exception {
		this.name = name;
		acceptedChars = new ArrayList<Character>();
		this.startState = parse(regex);

	}

	public NFA(String name, String regex, NFA prev) throws Exception {
		this.acceptedChars = new ArrayList<Character>();
		this.name = name;
		this.prev = (ArrayList<Character>) prev.getAcceptedChars().clone();
		this.startState = parseIN(regex);

	}

	public Node parseIN(String regex) throws Exception {
		int state = 0;
		int index = 0;
		char c;

		c = regex.charAt(index);
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
		ArrayList<Character> largerList = this.prev;
		for (Character ch : acceptedChars) {
			largerList.remove(ch);
		}
		Node node = new Node();
		node.addSuccessor(new Node(largerList, true));
		return node;
	}

	public Node parse(String regex) throws Exception {

		/* Parsing genius code */
		int state = 0;
		int index = 0;
		char c;

		c = regex.charAt(index);

		/* Figure out accepted characters in a [] character class */

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
		else{
			
			while(index < regex.length()){
				c = regex.charAt(index);
				if(c == '\\'){
					index++;
				}
				else{
					acceptedChars.add(c);
					index++;
				}
				
				
				
			}
			
		}

		/* Build NFA table */
		Node node = new Node();
		node.addSuccessor(new Node(acceptedChars, true));
		return node;

	}

	public void print() {
		Node node = this.startState;
		while(node.accept == false){
			for (Node n : node.getSuccessors()) {
				System.out.println(n.transitionChars);
			}
			node = node.getSuccessors().get(0);
		}
	}

	public ArrayList<Character> getAcceptedChars() {
		return this.acceptedChars;
	}


	public String getName() {
		return name;
	}

	public static void main(String[] args) throws Exception {
		
		NFA nfa = new NFA("$CHAR", "[a-zA-Z]");
		NFA nfa2 = new NFA("$DIGIT","[0-9]");
		ArrayList<NFA> al = new ArrayList<NFA>();
		al.add(nfa);
		al.add(nfa2);
		NFA nfa3 = concat(nfa,nfa2);
		nfa3.print();

	
	}
	
	@SuppressWarnings("unchecked")
	public static NFA union(NFA nfa1, NFA nfa2){
		ArrayList<Character> arr = new ArrayList<Character>();
		Node start = new Node();
		for(Node n : nfa1.startState.getSuccessors()){
			for(Character c : n.transitionChars){
				arr.add(c);
			}
		}
		for(Node n : nfa2.startState.getSuccessors()){
			for(Character c : n.transitionChars){
				arr.add(c);
			}
		}
		Node end = new Node(arr,true);
		start.addSuccessor(end);
		return new NFA(start);
	}
	
	public static NFA concat(NFA nfa1, NFA nfa2){
		
		Node start = new Node();
		Node node = start;
		node.addSuccessor(nfa1.startState.getSuccessors().get(0).clone());
		node = node.getSuccessors().get(0);
		while(node.accept != true){
			node = node.getSuccessors().get(0);
		}
		node.accept = false;
		node.addSuccessor(nfa2.startState.getSuccessors().get(0).clone());
		
		
		return new NFA(start);
	}
	
	public static NFA star(NFA nfa){
		Node start = new Node();
		start.addSuccessor(nfa.startState.getSuccessors().get(0).clone());
		Node node = start;
		while(node.accept != true){
			node = node.getSuccessors().get(0);
		}
		node.addSuccessor(node);
		
		return new NFA(node);
	}

}
