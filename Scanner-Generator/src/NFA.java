import java.util.ArrayList;

public class NFA {
	private String name;
	private int[][] NFAtable;
	private ArrayList<Character> acceptedChars;

	public NFA(String name, String regex) throws Exception {
		this.name = name;
		acceptedChars = new ArrayList<Character>();
		NFAtable = parse(regex);
	}

	public NFA(String name, String regex, NFA prev) {
		this.name = name;
		// table = parse(regex);
	}

	public int[][] parse(String regex) throws Exception {

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

		/* Build NFA table */

		int[][] table = new int[2 + acceptedChars.size()][acceptedChars.size() + 1];

		/* Labels for the columns of the table */
		for (int x = 0; x < acceptedChars.size(); x++) {
			char ac = acceptedChars.get(x);
			table[0][x] = ac;
		}

		/* Put state transitions in table (only first row will have them) */
		for (int y = 0; y < table[1].length; y++) {
			table[1][y] = y + 1;
		}

		/* Is state accepting(1) or non-accepting(0)? */
		for (int x = 1; x < table.length; x++) {
			table[x][table[0].length - 1] = 0;
		}
		table[1][table[0].length - 1] = 1;
		return table;

	}

	public void print() {
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < NFAtable[0].length; c++) {
				if (r == 0) {
					System.out.print(((char) NFAtable[r][c]) + "  ");
				} else {
					System.out.print(NFAtable[r][c] + "  ");
				}

			}
			System.out.println();
		}
	}
	/*
	public static void main(String[] args) throws Exception {
		NFA nfa = new NFA("$DIGIT", "[A-Z\\+\\[\\ \\\\]");
		nfa.print();
	}*/

}
