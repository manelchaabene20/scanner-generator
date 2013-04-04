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

		HashMap<String, NFA> nfas = new HashMap<String, NFA>();
		

		/* Tokenize the spec line by line */
		while ((currLine = specReader.readLine()) != null) {

			/* Read in spec and generate primitive NFA for each character class */
			if (!currLine.equals("")) {
				
				String var = currLine.substring(0, currLine.indexOf(" "));
				String regex = currLine.substring(currLine.indexOf(" ") + 1);


	
				
				NFA nfa;
				if (currLine.indexOf(" IN ") != -1) {
					String name = currLine.substring(currLine.indexOf(" IN ") + 4);
					NFA prev = nfas.get(name);
					nfa = new NFA(var, regex, prev);
				} else {
					nfa = new NFA(var, regex);
				}
			
				nfas.put(nfa.getName(), nfa);
			}

			/* Combine all NFA's */

			/* Convert NFA's to DFATable */
			DFATable dfa = new DFATable();

			/* Create Table Walker */
			TableWalker tw = new TableWalker(dfa);
			tw.scan(inputFile);
		}

		for (String name : nfas.keySet()) {
			System.out.println(name);
			NFA nfa = nfas.get(name);
			nfa.print();
		}

		specReader.close();

	}
}
