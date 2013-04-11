import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.junit.Test;


public class ScannerGeneratorTest {

	@Test
	public void testSample() throws Exception {

		String specFile = "test/SampleSpec.txt";
		String inputFile = "test/SampleInput.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/SampleOutput.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	
	@Test
	public void testCharClass() throws Exception {

		String specFile = "test/Spec1.txt";
		String inputFile = "test/Input1.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/Output1.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	/* NOTE: left bracket [ fails test case */

}
