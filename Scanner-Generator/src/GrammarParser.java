import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class GrammarParser {
	public static void main(String[] args) throws IOException{
		String specFile = "test/grammar.txt";
		BufferedReader specReader = new BufferedReader(new FileReader(specFile));
		
		String currLine;
	
		HashMap<String, String[]> characterClassNfas = new HashMap<String, String[]>();
		HashMap<String, NFA> tokenNfas = new HashMap<String, NFA>();
	
		while ((currLine = specReader.readLine()) != null){
			String[] line = currLine.split("::=");
			String[] right = line[1].split(" ");
			
		}
	}
}
