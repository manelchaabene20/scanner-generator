import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Scanner Generator reads in a spec files that
 * 
 * @author Jasmine
 * 
 */
public class ScannerGenerator {

	public static void main(String[] args) throws Exception {

		String specFile = "SampleSpec.txt";
		String inputFile = "SampleInput.txt";

		BufferedReader specReader = new BufferedReader(new FileReader(specFile));

		String currLine;

		HashMap<String, NFA> characterClassNfas = new HashMap<String, NFA>();
		HashMap<String, NFA> tokenNfas = new HashMap<String, NFA>();

		boolean token = false;
		/* Tokenize the spec line by line */
		while ((currLine = specReader.readLine()) != null) {

			if(currLine.equals("")){
				token = true;
				continue;
			}
			
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
					characterClassNfas.put(var, nfa);
				}
			} else {
				
				/* Tokens */
				String var = currLine.substring(0, currLine.indexOf(" "));
				String regex = currLine.substring(currLine.indexOf(" ") + 1);
				
				System.out.println(currLine);
				RecursiveDescentParser parser = new RecursiveDescentParser(characterClassNfas);
				tokenNfas.put(var, parser.next(regex));
			}
		}
		

		/* Combine all NFA's */
		

		/* Convert NFA's to DFATable */
		DFATable dfa = new DFATable();

		/* Create Table Walker */
		TableWalker tw = new TableWalker(dfa);
		tw.scan(inputFile);

		System.out.println("CHARACTER CLASSES");
		for (String name : characterClassNfas.keySet()) {
			System.out.println(name);
			NFA nfa = characterClassNfas.get(name);
			nfa.print();
		}
		System.out.println("-----------------------------------");
		System.out.println("TOKENS");
		for (String name : tokenNfas.keySet()) {
			System.out.println(name);
			NFA nfa = tokenNfas.get(name);
			nfa.print();
		}

	}
}
