import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class GrammarParser {
	public static void main(String[] args) throws IOException{
		String grammarFile = "test/testGrammar.txt";
		BufferedReader grammarReader = new BufferedReader(new FileReader(grammarFile));
		
		String currLine;
		
		HashMap<String, ArrayList<String>> rules = new HashMap<String, ArrayList<String>>();
		String first_rule = "";
		
		while ((currLine = grammarReader.readLine()) != null){
			String[] line = currLine.split(" ::= ");
			String left = line[0];
			if(first_rule.equals("")){
				first_rule = left;
			}
			
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
		HashMap<String, ArrayList<String>> something = FirstSet(rules);
		
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


		return firstSet;
	}
	

	
	public static HashMap<String, ArrayList<String>> FollowSet(HashMap<String, ArrayList<String>> rules, HashMap<String, ArrayList<String>> first, String first_rule){
		HashMap<String, ArrayList<String>> follow = new HashMap<String, ArrayList<String>>();


		/* Initialize follow sets */
		for(String left: rules.keySet()){
			if(left.equals(first_rule)){
				ArrayList<String> startsymbol = new ArrayList<String>();
				startsymbol.add("$");
				follow.put(first_rule, startsymbol);
			}
			else{
				follow.put(left, new ArrayList<String>());
			}
		}
		
		
		/* While there are changes to any Follow(A) */
		boolean changes = true;
		while(changes){
			changes = false;
			/* For each production rule X-->X1,X2,...Xn */
			for (String left: rules.keySet()){
				for(String right: rules.get(left)){
					
					String after = new String(right);
					/* For each Xi that is a nonterminal */
					ArrayList<String> nonterminals = getNonterminals(right);
					for(String nonterminal: nonterminals){
						
						ArrayList<String> symbols = follow.get(nonterminal);
						
						/* Find what comes after nonterminal Xi */
						after = after.substring(after.indexOf(nonterminal));
						ArrayList<String> tokens = getTokens(after);
						String immediatelyFollowing = tokens.get(0);
						
						/* NOTE: should do Xi+1....Xn or just Xi+1??? */
						
						for(String firstSymbol: first.get(immediatelyFollowing)){
							
							/* If epsilon is in First(Xi+1...Xn) then add the Follow(X) */
							if(firstSymbol.equals("<epsilon>")){
								
								for(String followSymbol: follow.get(left)){
									if(!symbols.contains(followSymbol)){
										symbols.add(followSymbol);
										changes = true;
									}
								}
							}
							else{
								/* Add the First(Xi+1....Xn) to the Follow(Xi) */
								if(!symbols.contains(firstSymbol)){
									symbols.add(firstSymbol);
									changes = true;
								}
							}
						}
						follow.put(nonterminal, symbols);
					}
					
				}
			}
		}
		/* Calculate follow set for a nonterminal */
		ArrayList<String> symbols = new ArrayList<String>();
			
			
		return follow;
	}

	public static ArrayList<String> getNonterminals(String s){
		ArrayList<String> nonterminals = new ArrayList<String>();
		while(true){
			int leftarrow = s.indexOf("<");
			if(leftarrow == -1) break;
			int rightarrow = s.indexOf(">");
			String nonterminal = s.substring(leftarrow+1, rightarrow).trim();
			nonterminals.add(nonterminal);
			s = s.substring(rightarrow+1);
		}
		return nonterminals;
	}
	
	public static ArrayList<String> getTokens(String s){
		ArrayList<String> tokens = new ArrayList<String>();
		while(true){
			int space = s.indexOf(" ");
			int leftarrow = s.indexOf("<");
			if(leftarrow == -1 && space == -1) break;
			int rightarrow = s.indexOf(">");
			
			if(leftarrow < space && leftarrow != -1){
				String nonterminal = s.substring(leftarrow+1, rightarrow).trim();
				tokens.add(nonterminal);
				s = s.substring(rightarrow+1);
			}
			else{
				int space2 = s.indexOf(" ");
				int leftarrow2 = s.indexOf("<");
				/* If the end of the string has been reached */
				if(leftarrow == -1 && space == -1){
					String terminal = s.substring(space+1).trim();
					tokens.add(terminal);
					break;
				}
				/* If the < symbol comes first */
				if(leftarrow2 == -1 || space2 < leftarrow2){
					String terminal = s.substring(space+1,space2);
					tokens.add(terminal);
				}
				/* If the space symbol comes first */
				/* space2 == -1 || leftarrow2 < space2 */
				else{
					String terminal = s.substring(space+1,leftarrow2);
					tokens.add(terminal);
				}
			}
			
		}
		return tokens;
	}
	
	
}
