import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import org.junit.Test;


public class ScannerGeneratorTest {

	@Test
	public void testTA() throws Exception {

		String specFile = "test/spec";
		String inputFile = "test/input";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/output"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	@Test
	public void testTA2() throws Exception {

		String specFile = "test/spec1";
		String inputFile = "test/input1";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/output1"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	@Test
	public void testTA3() throws Exception {

		String specFile = "test/spec2";
		String inputFile = "test/input2";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/output2"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	
	@Test (timeout = 100)
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
	
	@Test
	public void testCharClassError() throws Exception {

		String specFile = "test/Spec2.txt";
		String inputFile = "test/Input2.txt";
		
		try{
			ScannerGenerator.run(specFile, inputFile);
			fail("Should have gotten an exception");
		}
		catch(Exception e){
		}
	}

	@Test (timeout = 50)
	public void testEmptyStringAndConcatInParen() throws Exception {

		String specFile = "test/Spec3.txt";
		String inputFile = "test/Input3.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/Output3.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	

	@Test
	public void testDot() throws Exception {

		String specFile = "test/Spec4.txt";
		String inputFile = "test/Input4.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/Output4.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	

	@Test (timeout = 50)
	public void testFloat() throws Exception {

		String specFile = "test/Spec5.txt";
		String inputFile = "test/Input5.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/Output5.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}
	
	@Test (timeout = 150)
	public void testRandomThings() throws Exception {

		String specFile = "test/Spec6.txt";
		String inputFile = "test/Input6.txt";
		
		ScannerGenerator.run(specFile, inputFile);
		
		BufferedReader outputReader = new BufferedReader(new FileReader("output.txt"));
		BufferedReader correctOutputReader = new BufferedReader(new FileReader("test/Output6.txt"));
		
		String correctLine;
		while ((correctLine = correctOutputReader.readLine()) != null) {
			String currLine = outputReader.readLine(); 

			if(!currLine.equals(correctLine)){
				fail("Correct = "+correctLine+"\nFound = "+currLine);
			}
		}
	}

}
