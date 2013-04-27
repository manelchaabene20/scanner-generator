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
		HashMap<String, ArrayList<String>> temp =  FirstSet(rules);
		
	}
	public static ArrayList<String> first(String nonTerminal, HashMap<String, ArrayList<String>> rules){
		ArrayList<String> out = new ArrayList<String>();
		if(nonTerminal.equals("<epsilon>")){
			out.add("<epsilon>");
		}
		else if(nonTerminal.length() > 0 && nonTerminal.charAt(0) == '<'){
			for(String key : rules.keySet()){
				if(key.equals(nonTerminal)){
					for(String rule : rules.get(key)){
						String[] arr = rule.split(" ");
						ArrayList<String> temp = first(arr[0].trim(), rules);
						for(String s : temp){
							out.add(s);
						}
					}
				}
			}
		}
		else{
			out.add(nonTerminal);
		}
		return out;
	}
	public static HashMap<String, ArrayList<String>> FirstSet(HashMap<String, ArrayList<String>> rules){
		HashMap<String, ArrayList<String>> firstSet = new HashMap<String, ArrayList<String>>();
		for(String s : rules.keySet()){
			firstSet.put(s, new ArrayList<String>());
			ArrayList<String> arr = first(s,rules);
			for(String str : arr){
				firstSet.get(s).add(str);
			}
		}
		for(String s : rules.keySet()){
			System.out.println("Key : " + s);
			for(String str : firstSet.get(s)){
				System.out.print(str + " ");
			}
			System.out.println(" ");
			
		}
		/*iterate 1000 times!
		for(String key : rules.keySet()){
			for(String rule : rules.get(key)){
				int k = 1;
				String[] splitRule = rule.split(" ");
				if(firstSet.get(key).size() > 0){
					
				}
				else{
					ArrayList<String> arr = first(key,rules);
					firstSet.get(key).add
				}
			}
		}
		*/
		return null;
	}
	
	public HashMap<String, ArrayList<String>> FollowSet(HashMap<String, ArrayList<String>> rules){
		return null;
	}
	
	
}
