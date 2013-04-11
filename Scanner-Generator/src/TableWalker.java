import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class TableWalker {

	private String file;
	private File outputFile;
	private HashMap<String, NFA> nfas;
	
	public TableWalker(HashMap<String, NFA> nfas) {
		this.nfas = nfas;
	}
	
	
	
	public File scan(String input) throws Exception{
		
		BufferedReader inputReader = new BufferedReader(new FileReader(new File(input)));

		String currLine;

		/* Read input file line by line */
		while ((currLine = inputReader.readLine()) != null) {
			String[] tokens = currLine.split(" ");
			for(String s: tokens){
				//System.out.println(s);
				boolean accepted = false;
				for(String name: nfas.keySet()){
					NFA nfa = nfas.get(name);
					if (NFA.accepted(s, nfa.getStartState())){
						// Put this println into a file
						System.out.println(name.substring(1)+" "+s);
						accepted = true;
					}
				}
				if (accepted == false){
					// And put this println into a file
					System.out.println("NOT ACCEPTED "+s);
				}
			}
		}
			
			
		
		
		return outputFile;
		
	}
}
