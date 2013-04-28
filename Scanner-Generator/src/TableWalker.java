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
			String line = currLine;
			String toTry = line;
			boolean accepted = false;
			while(line.length() > 0){
				while(toTry.length() >= 0) {
					for (String name : nfas.keySet()) {
						NFA nfa = nfas.get(name);
						if (NFA.accepted(toTry, nfa.getStartState())) {
							output += name.substring(1) + " " + toTry + "\n";
							accepted = true;
							break;
						}
					}
					if(toTry.length() == 0){
						throw new Exception();
					}
					if(accepted){
						break;
					}
					else{
						toTry = toTry.substring(0, toTry.length() - 1);
					}
				}
				if(accepted){
					
				}
				accepted = false;
				if(toTry.length() == line.length()){
					break;
				}
				System.out.println("----------");
				System.out.println(toTry);
				System.out.println(line);
				toTry = line.substring(toTry.length()).trim();
				line = toTry;
				System.out.println(toTry);
				System.out.println(line);
				
			}
		}

		out.println(output);
		out.close();

		return outputFile;

	}
}
