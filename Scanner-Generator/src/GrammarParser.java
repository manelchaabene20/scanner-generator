import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GrammarParser {
	public static void main(String[] args) throws IOException{
		String grammarFile = "test/grammar.txt";
		BufferedReader grammarReader = new BufferedReader(new FileReader(grammarFile));
		
		String currLine;
		
		HashMap<String, ArrayList<String>> rules = new HashMap<String, ArrayList<String>>();
	
		while ((currLine = grammarReader.readLine()) != null){
			String[] line = currLine.split(" ::= ");
			String left = line[0];
			String[] right = line[1].split(" \\| ");
			
			ArrayList<String> r = new ArrayList<String>();
			for(String rule: right){
				r.add(rule);
			}
			if(rules.get(left) != null){
				for(String alreadyRule : rules.get(left)){
					r.add(alreadyRule);
				}
			}
			rules.put(left, r);
			
		}
	}
	
	public HashMap<String, ArrayList<String>> FirstSet(HashMap<String, ArrayList<String>> rules){
		return null;
	}
	
	public HashMap<String, ArrayList<String>> FollowSet(HashMap<String, ArrayList<String>> rules){
		return null;
	}
	
	
}
