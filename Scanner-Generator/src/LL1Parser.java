import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class LL1Parser {
	/**
	 * Parses through the given input file using the parsing table
	 * Throws an error if input is invalid; does nothing if the input is valid
	 * 
	 * @param table The generated Parsing Table
	 * @throws Exception 
	 */
	public static void parse(HashMap<String, HashMap<String, String>> table, String tokenFile) throws Exception{
		ArrayList<String> tokens = getTokens(tokenFile);
		ArrayList<String> tokenNames = getTokenNames(tokenFile);
		Stack<String> stack = new Stack<String>();
		stack.push("$");
		
		/* TEMPORARY: hard code start rule */
		String left = "<MiniRE-program>";
		stack.push(left);
		
		while(true){
			left = stack.pop();
			System.out.println("left: "+left);
			
			if(left.equals("$")){
				if(tokens.size() > 0){
					throw new Exception("Error! -- stack empty (just $) but input still has "+tokens.size()+" tokens!");
				}
				
				System.out.println("PARSING SUCCESS!!!!");
				break;
				
			}
			
			String token = tokens.get(0);
			String tokenName = tokenNames.get(0);
			
			if(!table.containsKey(left)){
				/*(if(!ParserGenerator.getAllTerminals(rules).contains(left)){
					throw new Exception("Invalid token!! Not a nonterminal or a terminal: "+left);
				}*/
				
				/*Otherwise, left is a terminal -- try to match */
				if(token.equals(left) || tokenName.equals(left)){
					tokens.remove(0);
					tokenNames.remove(0);
				}
				else if(left.equals("<epsilon>")){
					//DO NOTHING
				}
				else{
					throw new Exception("Parse error! Expected: "+left+"    Input had: "+tokenName+" "+token);
				}
			}
			else{
				HashMap<String,String> row = table.get(left);
				
				/* Error check */
				if(!row.containsKey(token) && !row.containsKey(tokenName)){
					throw new Exception("ERROR -- Input token invalid: "+tokenName+" "+token);
				}
				
				/* Replace nonterminal with rule */
				String right = "";
				if(row.containsKey(token))
					right = row.get(token);
				else
					right = row.get(tokenName);
				ArrayList<String> rightTokens = ParserGenerator.getTokens(right);
				for(int i=rightTokens.size()-1; i>=0; i--){
					/* Push the right hand side in reverse (so it gets popped off in order) */
					String rt = rightTokens.get(i);
					stack.push(rt);
					System.out.println("push: "+rt);
				}
			}
			
		}
			
		
	}
	
	public static ArrayList<String> getTokens(String tokenFile) throws Exception{
		ArrayList<String> tokens = new ArrayList<String>();
		 
		BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile));
			
		String currLine;
		
		while ((currLine = tokenReader.readLine()) != null){
			int space = currLine.indexOf(" ");
			String name = currLine.substring(0,space);
			String token = currLine.substring(space+1);
			tokens.add(token);
		}
		 
		return tokens;
	}
	
	public static ArrayList<String> getTokenNames(String tokenFile) throws Exception{
		ArrayList<String> tokenNames = new ArrayList<String>();
		 
		BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile));
			
		String currLine;
		
		while ((currLine = tokenReader.readLine()) != null){
			int space = currLine.indexOf(" ");
			String name = currLine.substring(0,space);
			String token = currLine.substring(space+1);
			tokenNames.add(name);
		}
		 
		return tokenNames;
	}
	
	public static void main(String[] args) throws Exception{
		HashMap<String, HashMap<String, String>> table = ParserGenerator.generateParsingTable("test/grammar.txt");
		parse(table, "test/phase2output.txt");
	}
	
}
