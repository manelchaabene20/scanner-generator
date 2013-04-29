import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class LL1Parser {
	
	/**
	 * Parses through the given input file using the parsing table
	 * Throws an error if input is invalid; does nothing if the input is valid
	 * 
	 * @param table The generated Parsing Table
	 * @throws Exception 
	 */
	public static void parse(HashMap<String, HashMap<String, String>> table, String tokenFile, String first_rule) throws Exception{
		
		PrintWriter out = new PrintWriter(new FileWriter("stackOutput.txt"));
		String output = "";
		
		ArrayList<String> tokens = getTokens(tokenFile);
		ArrayList<String> tokenNames = getTokenNames(tokenFile);
		Stack<String> stack = new Stack<String>();
		stack.push("$");
		
		/* TEMPORARY: hard code start rule */
		String left = first_rule;
		stack.push(left);
		
		while(true){
			/* Print contents of input */
			output += "Input: ";
			for(String s: tokens){
				output += s+" ";
			}
			output += "$\n";
			
			/* Print contents of stack */
			output += "Stack: ";
			Stack<String> printStack = new Stack<String>();
			while(!stack.isEmpty()){
				String s = stack.pop();
				output += s+" ";
				printStack.push(s);
			}
			while(!printStack.isEmpty()){
				stack.push(printStack.pop());
			}
			output += "\n\n";
			
			left = stack.pop();
			
			if(left.equals("$")){
				if(tokens.size() > 0){
					throw new Exception("Error! -- stack empty (just $) but input still has "+tokens.size()+" tokens!");
				}
				
				output += "PARSING SUCCESS!!!!\n";
				break;
				
			}
			
			String token = "";
			String tokenName = "";
			if(tokens.size() > 0){
				token = tokens.get(0);
				tokenName = tokenNames.get(0);
			}
			
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
				if(!row.containsKey(token) && !row.containsKey(tokenName) && !token.equals("")){
					throw new Exception("ERROR -- Input token invalid: "+tokenName+" "+token);
				}
				if(token.equals("")){
					token = "$";
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
				}
			}
			
		}
		
		out.println(output);
		out.close();
		
	}
	
	public static ArrayList<String> getTokens(String tokenFile) throws Exception{
		ArrayList<String> tokens = new ArrayList<String>();
		 
		BufferedReader tokenReader = new BufferedReader(new FileReader(tokenFile));
			
		String currLine;
		
		while ((currLine = tokenReader.readLine()) != null){
			if(currLine.equals("")) break;
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
			if(currLine.equals("")) break;
			int space = currLine.indexOf(" ");
			String name = currLine.substring(0,space);
			String token = currLine.substring(space+1);
			tokenNames.add(name);
		}
		 
		return tokenNames;
	}
	
	/** Main for testing purposes **/
	public static void main(String[] args) throws Exception{
		HashMap<String, HashMap<String, String>> table = ParserGenerator.generateParsingTable("test/testGrammar.txt");
		Scanner scan = new Scanner(new FileReader("test/testGrammar.txt"));
		String line = scan.nextLine();
		String first_rule = line.substring(0,line.indexOf(" ::= "));
		
		parse(table, "test/simpleInput.txt", first_rule);
	}
	
}
