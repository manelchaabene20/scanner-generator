import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class TableWalker {

	private String file;
	private File outputFile;
	private HashMap<String, NFA> nfas;
	private PrintWriter out;

	public TableWalker(HashMap<String, NFA> nfas) {
		this.nfas = nfas;
	}

	public File scan(String input) throws Exception {
		out = new PrintWriter(new FileWriter("output.txt"));

		BufferedReader inputReader = new BufferedReader(new FileReader(
				new File(input)));

		String currLine;

		String output = "";
		/* Read input file line by line */
		while ((currLine = inputReader.readLine()) != null) {
			String[] tokens = currLine.split(" ");
			for (String s : tokens) {
				//System.out.println(s);
				boolean accepted = false;
				String temp = s;
				int length = s.length();
				int index = temp.length() - 1;
				while(s.length() > 0 || !accepted){
					while(accepted == false || temp.length() > 0){
						for (String name : nfas.keySet()) {
							NFA nfa = nfas.get(name);
							System.out.println("temp "+temp);
							System.out.println("acc?"+NFA.accepted(temp, nfa.getStartState()));
							if (NFA.accepted(temp, nfa.getStartState())) {
								// Put this println into a file
								//System.out.println(name.substring(1) + " " + temp);
								output += name.substring(1) + " " + temp + "\n";
	
								accepted = true;
								
							}
						}
						if(!accepted){
							index--;	
							
							if(index <= -1){
								throw new Exception("Error in input! Invalid character sequence! "+s);
							}
							
							temp = s.substring(0, index);
						}
						else{
							break;
						}
					}
					if(accepted = true && !s.equals(temp)){
						s = s.substring(temp.length());
						index = s.length();
						temp = s;
						accepted = false;
					}
					else
						break;
					
					
				}
			}

		}

		out.println(output);
		out.close();

		return outputFile;

	}
}
