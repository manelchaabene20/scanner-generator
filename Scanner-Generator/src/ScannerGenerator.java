import java.io.*;

/**
 * Scanner Generator reads in a spec files that 
 * @author Jasmine
 *
 */
public class ScannerGenerator {


	public static void main(String[] args) {
		
		//Names for input and spec files
		String specFile = args[0];
		String inputFile = args[1];

		BufferedReader specReader = null;

		try {

			String currLine;

			specReader = new BufferedReader(new FileReader(specFile));

			//tokenize the spec line by line
			while ((currLine = specReader.readLine()) != null) {
				System.out.println(currLine);
				//Read in spec and generate primitive NFA for each character class

				//combine all NFA's
				
				//Convert NFA's to DFATable
				DFATable dfa = new DFATable();
				
				
				//Create Table Walker
				TableWalker tw = new TableWalker(dfa);
				tw.scan(inputFile);
			}
		
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (specReader != null) {
					specReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
