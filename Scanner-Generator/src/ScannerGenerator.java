import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;

/**
 * Scanner Generator reads in a spec files that
 * 
 * @author Jasmine
 * 
 */
public class ScannerGenerator {

	public static void main(String[] args) throws Exception {

		// prompt the user to enter the spec name
		String cont = "";

		Scanner scanner = new Scanner(System.in);
		do {
			// enter spec name
			System.out
					.print("Enter the name of the spec file including the extension: ");
			String spec = scanner.nextLine();

			// enter input name
			System.out
					.print("Enter the name of the input file including the extension: ");
			String input = scanner.nextLine();

			// run the scanner generator
			run(spec, input);
			//System.out.println("See results in output.txt");

			System.out
					.println("To run a new test type 'again'. To quit enter 'quit'.");
			cont = scanner.nextLine();

		} while (!cont.equals("quit"));

		/*
		 * String specFile = "test/SampleSpec.txt"; String inputFile =
		 * "test/SampleInput.txt"; run(specFile, inputFile);
		 */
	}

	public static void run(String specFile, String inputFile) throws Exception {

		BufferedReader specReader = new BufferedReader(new FileReader(specFile));

		String currLine;

		HashMap<String, NFA> characterClassNfas = new HashMap<String, NFA>();
		HashMap<String, NFA> tokenNfas = new HashMap<String, NFA>();

		boolean token = false;
		/* Tokenize the spec line by line */
		while ((currLine = specReader.readLine()) != null) {

			if (currLine.equals("")) {
				token = true;
				continue;
			}
			System.out.println(currLine);
			/* Read in spec and generate primitive NFA for each character class */
			if (token == false) {

				String var = currLine.substring(0, currLine.indexOf(" "));
				String regex = currLine.substring(currLine.indexOf(" ") + 1);

				/* Contains IN */
				if (currLine.indexOf(" IN ") != -1) {
					regex = regex.substring(0, regex.indexOf(" IN "));
					String name = currLine
							.substring(currLine.indexOf(" IN ") + 4);

					NFA prev = characterClassNfas.get(name);
					NFA nfa = new NFA(var, regex, prev);
					characterClassNfas.put(var, nfa);
				} else {
					/* Simple primitive NFA */
					NFA nfa = new NFA(var, regex);
					//nfa.print();
					characterClassNfas.put(var, nfa);
				}
			} else {

				/* Tokens */
				String var = currLine.substring(0, currLine.indexOf(" "));
				String regex = currLine.substring(currLine.indexOf(" ") + 1);

				// //System.out.println(currLine);
				RecursiveDescentParser parser = new RecursiveDescentParser(
						characterClassNfas);
				
				NFA newNfa = parser.next(regex);
				tokenNfas.put(var, newNfa);
			}
		}

		/* Combine all NFA's */
		//NFA combined = NFA.union(tokenNfas);		

		/* Create Table Walker */
		TableWalker tw = new TableWalker(tokenNfas);
		tw.scan(inputFile);

		// //System.out.println("CHARACTER CLASSES");
		for (String name : characterClassNfas.keySet()) {
			// //System.out.println(name);
			NFA nfa = characterClassNfas.get(name);
			// nfa.print();
		}
		// //System.out.println("-----------------------------------");
		// //System.out.println("TOKENS");
		for (String name : tokenNfas.keySet()) {
			// //System.out.println(name);
			NFA nfa = tokenNfas.get(name);
			// nfa.print();
		}
	}
}
