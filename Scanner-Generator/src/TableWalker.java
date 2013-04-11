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
				for(NFA nfa: nfas.values()){
					if (NFA.accepted(s, nfa.getStartState())){
						System.out.println("ACCEPTED "+s);
						accepted = true;
					}
				}
				if (accepted == false){
					System.out.println("NOT ACCEPTED "+s);
				}
			}
		}
			
			
		
		
		return outputFile;
		
	}
}
