import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class GrammarParser {
	public static void main(String[] args) throws IOException{
		String grammarFile = "test/grammar.txt";
		BufferedReader grammarReader = new BufferedReader(new FileReader(grammarFile));
		
		String currLine;
	
		while ((currLine = grammarReader.readLine()) != null){
			String[] line = currLine.split("::=");
			String[] right = line[1].split(" ");
			
		}
	}
}
