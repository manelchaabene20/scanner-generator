import java.util.ArrayList;
import java.util.HashMap;

public class NFA {
	private String name;
	private ArrayList<Character> acceptedChars;
	private ArrayList<Character> prev;
	private Node startState;
	private Node acceptState = null;
	private boolean isStar = false;

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
		this.acceptState = startState.getSuccessors().get(0);

	}
	public NFA(String name, String accepted, boolean something) throws Exception{
		this.name = name;
		ArrayList<Character> str = new ArrayList<Character>();
		int index = 0;
		Character c = accepted.charAt(index);
		while(index < accepted.length()){
			c = accepted.charAt(index);
			if(c == '\\'){
				index++;
			}
			else{
				str.add(c);
				index++;
			}
		}
		Node start = new Node();
		this.startState = start;
		Node temp = start;
		for(Character ch : str){
			temp.addSuccessor(new Node(ch));
			temp = temp.getSuccessors().get(0);
		}
		startState.start = true;
		temp.accept = true;
		this.acceptState = temp;
		
	}
	public NFA(String name, String regex, NFA prev) throws Exception {
		this.acceptedChars = new ArrayList<Character>();
		this.name = name;
		this.prev = (ArrayList<Character>) prev.getAcceptedChars().clone();
		this.startState = parseIN(regex);
		this.acceptState = startState.getSuccessors().get(0);

	}
	
	public Node getStartState(){
		return this.startState;
	}
	
	/**
	 * @param regex
	 * @return
	 * @throws Exception
	 */
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
				if (c != ']' || (c == ']' && regex.charAt(index-1) == '\\'))
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
		node.start = true;
		this.acceptedChars = largerList;
		node.addSuccessor(new Node(largerList, true));
		return node;
	}
	
	/**
	 * Reads in a string for the regex associated with a character class. Custom
	 * parse method reads in the regex to build out a list of accepted characters.
	 * @param regex
	 * @return
	 * @throws Exception
	 */
	public Node parse(String regex) throws Exception {

		/* Parsing genius code */
		int state = 0;
		int index = 0;
		char c;

		c = regex.charAt(index);

		/* Figure out accepted characters in a [] character class */
		if(c == '.'){
			for(int i = 0; i < 256; i++){
				acceptedChars.add((char)i);
			}
		}
		else{
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
					if (c != ']' || (c == ']' && regex.charAt(index-1) == '\\'))
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
		}

		/* Build NFA table */
		Node node = new Node();
		node.start = true;
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


	public static boolean accepted(String s,Node startNode){
		boolean accepted = false;
		if(s.length() == 0 && startNode.accept == true){
			return true;
		}
		else if(s.length() == 0 && startNode.accept != true){
			return false;
		}
		Character c = s.charAt(0);
		for(Node n : startNode.getSuccessors()){
			if(n.start == true){
				accepted =  accepted(s, n);
				if(accepted){
					return true;
				}
			}
		else{
			if(n.transitionChars.size() > 0){
				if(n.transitionChars.contains(c))
					accepted = accepted(s.substring(1), n);
					if(accepted){
						return true;
					}
				}
			}
		}
		return accepted;
	}
	
	@SuppressWarnings("unchecked")
	public static NFA union(NFA nfa1, NFA nfa2){
		Node start = new Node();
		start.start = true;
		start.accept = false;
		if(nfa1.isStar || nfa2.isStar){
			start.accept = true;
		}
		start.addSuccessor(nfa1.startState);
		start.addSuccessor(nfa2.startState);
		nfa1.acceptState = nfa2.acceptState;
		NFA out = new NFA(start);
		out.acceptState = nfa2.acceptState;
		return out;
	}
	
	public static NFA concat(NFA nfa1, NFA nfa2){
		nfa1.startState.accept = false;
		nfa1.acceptState.accept = false;
		nfa1.acceptState.addSuccessor(nfa2.startState);
		if(nfa2.isStar){
			nfa1.acceptState.accept = true;
		}
		else{
			nfa1.acceptState.accept = false;
		}	
		if(nfa1.isStar && nfa2.isStar){
			nfa1.startState.accept = true;
		}
		nfa1.isStar = false;
		nfa1.acceptState = nfa2.acceptState;
		nfa1.isStar = false;
		return nfa1;
	}
	
	public static NFA star(NFA nfa){
		nfa.acceptState.addSuccessor(nfa.startState);
		nfa.acceptState.end = true;
		nfa.isStar = true;
		nfa.startState.accept = true;
		return nfa;
	}
	

	public static NFA clone(NFA old){
		Node startState = new Node();
		startState.start = true;
		startState.addSuccessor(new Node(old.getAcceptedChars(), true));
		NFA out = new NFA(startState);
		out.acceptState = startState.getSuccessors().get(0);
		return out;
	}
	
	public static NFA union(HashMap<String,NFA> nfas){
		Node start = new Node();
		start.start = true;
		for(NFA n : nfas.values()){
			start.addSuccessor(n.startState);
		}
		
		return new NFA(start);
		
	}
	
	public static void main(String[] args) throws Exception {
		
		NFA nfa = new NFA("$DIGIT", "apple", true);
		nfa = star(nfa);
		System.out.println(accepted("apple",nfa.startState));
	}
}
