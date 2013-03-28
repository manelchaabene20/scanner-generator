import java.io.*;
import java.util.ArrayList;

/**
 * Scanner Generator reads in a spec files that 
 * @author Jasmine
 *
 */
public class ScannerGenerator {


	public static void main(String[] args) throws Exception {

		String specFile = "SampleSpec.txt";
		String inputFile = "SampleInput.txt";

		BufferedReader specReader = new BufferedReader(new FileReader(specFile));

		String currLine;

		ArrayList<NFA> nfas = new ArrayList<NFA>();
		
		/* Tokenize the spec line by line */
		while ((currLine = specReader.readLine()) != null) {
			System.out.println(currLine);

			
			/* Read in spec and generate primitive NFA for each character class */
			if(!currLine.equals("")){
				String var = currLine.substring(0, currLine.indexOf(" "));
				String regex = currLine.substring(currLine.indexOf(" ")+1);
				
				System.out.println(var+": "+regex);
				
				NFA nfa = new NFA(var, regex);
				nfas.add(nfa);
			}

			

			/* Combine all NFA's */


			/* Convert NFA's to DFATable */
			DFATable dfa = new DFATable();


			/* Create Table Walker */
			TableWalker tw = new TableWalker(dfa);
			tw.scan(inputFile);
		}

		specReader.close();

	}
}
