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

			String sCurrentLine;

			specReader = new BufferedReader(new FileReader(specFile));

			//tokenize the spec line by line
			while ((sCurrentLine = specReader.readLine()) != null) {
				System.out.println(sCurrentLine);
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
