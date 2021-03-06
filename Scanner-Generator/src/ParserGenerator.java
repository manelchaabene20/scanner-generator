import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class ParserGenerator {
	
	/**
	 * Generates a parsing table for the given grammar file
	 * 
	 * @param grammarFile
	 * @return parsing table
	 * @throws Exception
	 */
	public static HashMap<String, HashMap<String, String>> generateParsingTable(String grammarFile) throws Exception{
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
		HashMap<String, ArrayList<String>> followSet = FollowSet(rules, firstSet, first_rule);
		HashMap<String, HashMap<String, String>> table = ParsingTable(rules, followSet);

		return table;
	}
	
	/** 
	 * Constructs a parsing table for the given grammar rules and follow set
	 * 
	 * @param rules
	 * @param follow
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, HashMap<String, String>> ParsingTable(HashMap<String,ArrayList<String>> rules, HashMap<String, ArrayList<String>> follow) throws Exception{
		HashMap<String, HashMap<String, String>> table = new HashMap<String, HashMap<String, String>>();
		
		ArrayList<String> nonterminals = getAllNonterminals(rules);
		ArrayList<String> terminals = getAllTerminals(rules);
		
		/* Initialize */
		for(String nonterminal: nonterminals){			
			HashMap<String, String> row = new HashMap<String, String>();
			for(String terminal: terminals){		
				row.put(terminal, "");				
			}
			row.put("$", "");
			table.put(nonterminal, row);
		}
		
		/* Run algorithm */
		for (String left: rules.keySet()){
			for(String right: rules.get(left)){
				ArrayList<String> firstSet = first(right, rules);
				for(String symbol: firstSet){
					
					/* Check for ERROR */
					if(!table.get(left).get(symbol).equals("")){
						throw new Exception("ERROR -- grammar is not LL(1) -- duplicate rules in table entry: \n"+table.get(left).get(symbol)+"\n"+right);
					}
					/* Check for epsilon in First set */
					else if(symbol.equals("<epsilon>")){
						ArrayList<String> followSet = follow.get(left);
						for(String f: followSet){
							/* Check for ERROR */
							if(!table.get(left).get(f).equals("")){
								throw new Exception("ERROR -- grammar is not LL(1) -- duplicate rules in table entry: \n"+table.get(left).get(f)+"\n"+symbol);
							}
							else{
								table.get(left).put(f, symbol);
							}
						}
					}
					else{
						table.get(left).put(symbol, right);
					}
				}
				
			}
		}
		
		return table;
	}
	
	/**
	 * Gets all nonterminals from the given grammar rules
	 * 
	 * @param rules
	 * @return list of nonterminals
	 */
	public static ArrayList<String> getAllNonterminals(HashMap<String,ArrayList<String>> rules){
		ArrayList<String> nonterminals = new ArrayList<String>();
		for(String left: rules.keySet()){
			if(!nonterminals.contains(left))
				nonterminals.add(left);
		}
		return nonterminals;
	}
	
	/**
	 * Gets all terminals from the given grammar rules
	 * 
	 * @param rules
	 * @return list of terminals
	 */
	public static ArrayList<String> getAllTerminals(HashMap<String,ArrayList<String>> rules){
		ArrayList<String> terminals = new ArrayList<String>();
		for(String left: rules.keySet()){
			for(String right: rules.get(left)){
				ArrayList<String> nonterminals = getNonterminals(right);
				ArrayList<String> tokens = getTokens(right);
				
				for(String token: tokens){
					if(!nonterminals.contains(token) && !terminals.contains(token)){
						terminals.add(token);
					}
				}
			}
		}
		return terminals;
	}
	
	/**
	 * Calculates the first set of a single grammar rule
	 * 
	 * @param nonTerminal
	 * @param rules
	 * @return first set
	 */
	public static ArrayList<String> first(String nonTerminal, HashMap<String, ArrayList<String>> rules){
		ArrayList<String> out = new ArrayList<String>();
		ArrayList<String> tokens = getTokens(nonTerminal);
		
		if(tokens.get(0).equals("<epsilon>")){
			out.add("<epsilon>");
		}
		else if(tokens.get(0).length() > 0 && tokens.get(0).charAt(0) == '<'){
			for(String key : rules.keySet()){
				if(key.equals(tokens.get(0))){
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
			out.add(tokens.get(0));
		}
		return out;
	}
	
	/**
	 * Returns the First Set for the entire grammar
	 * 
	 * @param rules
	 * @return entire first set
	 */
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
	

	/**
	 * Generates the entire follow set for grammar rules
	 * 
	 * @param rules
	 * @param first
	 * @param first_rule
	 * @return follow set
	 */
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
									if(firstSymbol.equals("<epsilon>")){
										for(String followSymbol: follow.get(left)){
											if(!symbols.contains(followSymbol)){
												symbols.add(followSymbol);
												changes = true;
											}
										}
									}
									
									/* Add the First(Xi+1....Xn) to the Follow(Xi) */
									else{
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

	/**
	 * Gets the nonterminals for the right hand side of a single rule 
	 * 
	 * @param s the right hand side of a rule
	 * @return list of nonterminals
	 */
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
	
	/**
	 * Gets all of the tokens for a given string (all terminals and nonterminals
	 * 
	 * @param s 
	 * @return list of tokens
	 */
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
			if(leftarrow != -1 && ((leftarrow < space && space !=-1) || (leftarrow > space && space == -1))){
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
	

	/** Main for testing purposes **/
	public static void main(String[] args) throws Exception{
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
			System.out.println();

		}
		
		HashMap<String, HashMap<String, String>> table = ParsingTable(rules, followSet);
		System.out.println("---------------------------------------------------");
		System.out.println("PARSING TABLE");
		System.out.println("---------------------------------------------------");
		System.out.println();
		for(String nonterminal: table.keySet()){
			System.out.println("ROW "+nonterminal);
			for(String terminal: table.get(nonterminal).keySet()){
				if(!table.get(nonterminal).get(terminal).equals("")){
					System.out.println(terminal+": "+table.get(nonterminal).get(terminal));	
				}
			}
			System.out.println();
			System.out.println();
		}
		
	}
	
	
}
