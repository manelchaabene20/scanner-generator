import java.util.ArrayList;

public class NFA {
	private String name;
	private ArrayList<Character> acceptedChars;
	private ArrayList<Character> prev;
	private Node startState;
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
		node.start = true;
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
		Node end = new Node();
		end.accept = true;
		Node temp = nfa1.startState;
		if(!nfa1.isStar){
			while(temp.accept == false && temp.getSuccessors().size() > 0){
				temp = temp.getSuccessors().get(0);
			}
		}
		else{
			while(temp.accept == false && temp.end != false){
				temp = temp.getSuccessors().get(0);
			}
		}
		temp.addSuccessor(end);
		temp = nfa2.startState;
		if(!nfa2.isStar){
			while(temp.accept == false && temp.getSuccessors().size() > 0){
				temp = temp.getSuccessors().get(0);
			}
		}
		else{
			while(temp.accept == false && temp.end != false){
				temp = temp.getSuccessors().get(0);
			}
		}
		temp.addSuccessor(end);
		return new NFA(start);
	}
	
	public static NFA concat(NFA nfa1, NFA nfa2){
		
		Node node = new Node();
		if(nfa1.isStar){
			nfa1.startState.accept = false;
			nfa1.startState.addSuccessor(nfa2.startState);
			node = nfa1.startState;
			while(node.accept == false && node.end == false){
				node = node.getSuccessors().get(0);
			}
			for(Node n : nfa2.startState.getSuccessors()){
				node.addSuccessor(n);
				nfa1.startState.addSuccessor(n);
			}
		}
		else{
			node = nfa1.startState;
			while(node.accept == false && node.getSuccessors().size() > 0){
				node = node.getSuccessors().get(0);
			}
			
			for(Node n : nfa2.startState.getSuccessors()){
				node.addSuccessor(n);
				
			}
		}
		node.accept = false;
		if(!nfa2.isStar){
			nfa2.startState.start = false;
		}
		else{
			node.accept = true;
		}
		if(nfa1.isStar && nfa2.isStar){
			nfa1.startState.accept = true;
			
		}
		
		return nfa1;
	}
	
	public static NFA star(NFA nfa){
		
		Node temp = nfa.startState;
		while(temp.accept == false && temp.getSuccessors().size() > 0){
			temp = temp.getSuccessors().get(0);
		}
		temp.addSuccessor(nfa.startState);
		temp.end = true;
		nfa.isStar = true;
		nfa.startState.accept = true;
		return nfa;
	}
	
	public static void main(String[] args) throws Exception {
		
		NFA nfa = new NFA("$CHAR", "[a-zA-Z]");
		NFA nfa2 = new NFA("$DIGIT", "[0-9]");
		nfa = star(nfa);
		nfa2 = star(nfa2);
		NFA nfa3 = union(nfa,nfa2);
		System.out.println(accepted("",nfa3.startState));
		

	
	}
}
