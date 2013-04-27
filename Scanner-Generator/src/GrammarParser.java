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


		HashMap<String, ArrayList<String>> firstSet =  FirstSet(rules);
		System.out.println("---------------------------------------------------");
		System.out.println("First Set");
		System.out.println("---------------------------------------------------");
		for(String left: firstSet.keySet()){
			System.out.println("RULE: "+left);
			System.out.print("{");
			for(int i=0; i<firstSet.get(left).size(); i++){
				String symbol = firstSet.get(left).get(i);
				if(i == firstSet.get(left).size()-1)
					System.out.print(symbol);
				else
					System.out.print(symbol+", ");
			}
			System.out.print("}");
			System.out.println();
			System.out.println();
		}
		System.out.println();
		HashMap<String, ArrayList<String>> followSet = FollowSet(rules, firstSet, first_rule);
		System.out.println("---------------------------------------------------");
		System.out.println("Follow Set");
		System.out.println("---------------------------------------------------");
		for(String left: followSet.keySet()){
			System.out.println("RULE: "+left);
			System.out.print("{");
			for(int i=0; i<followSet.get(left).size(); i++){
				String symbol = followSet.get(left).get(i);
				if(i == followSet.get(left).size()-1)
					System.out.print(symbol);
				else
					System.out.print(symbol+", ");
			}
			System.out.print("}");
			System.out.println();

		}

		
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
					//System.out.println();
					//System.out.println("RULE: "+left+" ::== "+right);
					
					String after = new String(right);
					/* For each Xi that is a nonterminal */
					ArrayList<String> nonterminals = getNonterminals(right);
										
					for(String nonterminal: nonterminals){
						//System.out.println("Nonterminal: "+nonterminal);
						
						ArrayList<String> symbols = follow.get(nonterminal);
						
						/* Find what comes after nonterminal Xi */
						after = after.substring(after.indexOf(nonterminal));
						ArrayList<String> tokens = getTokens(after);
						
						/*System.out.println("TOKENS");
						for(String t: tokens){
							System.out.print(t+"   ");
						}
						System.out.println();*/
						
						/* Nothing follows the nonterminal -- do Follow(X) */
						if(tokens.size()<2){
							for(String followSymbol: follow.get(left)){
								if(!symbols.contains(followSymbol)){
									symbols.add(followSymbol);
									changes = true;
								}
							}
						}
						else{
							String immediatelyFollowing = tokens.get(1);
							
							/* If a terminal follows the Xi */
							if(!nonterminals.contains(immediatelyFollowing)){
								if(!symbols.contains(immediatelyFollowing)){
									symbols.add(immediatelyFollowing);
								}
							}
							else{
								/* Add the First(Xi+1) to Follow(Xi) */
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
			int rightarrow = s.substring(leftarrow).indexOf(">")+s.substring(0,leftarrow).length();
			String nonterminal = s.substring(leftarrow, rightarrow+1).trim();
			if(!nonterminal.equals("<epsilon>")){
				nonterminals.add(nonterminal);			
			}	
			s = s.substring(rightarrow+1);
		}
		return nonterminals;
	}
	
	public static ArrayList<String> getTokens(String s){
		s = s.trim();
		ArrayList<String> tokens = new ArrayList<String>();
		while(true){
			int space = s.indexOf(" ");
			int leftarrow = s.indexOf("<");			
			int rightarrow = s.indexOf(">");
			int leftarrow2 = s.substring(rightarrow+1).indexOf("<");
			
			/* No more tokens */
			if((leftarrow == -1 || leftarrow == 0) && leftarrow2 ==-1 && space == -1){
				tokens.add(s.trim());
				break;
			}
			
			/* The < symbol appears first */
			if(leftarrow < space && leftarrow != -1){
				String nonterminal = s.substring(leftarrow, rightarrow+1).trim();
				tokens.add(nonterminal);
				s = s.substring(rightarrow+1).trim();
			}
			/* The space symbol appears first */
			else{
				String nonterminal = s.substring(0, space).trim();
				tokens.add(nonterminal);
				s = s.substring(space+1).trim();
			}
			
		}
		return tokens;
	}
	
	
}
