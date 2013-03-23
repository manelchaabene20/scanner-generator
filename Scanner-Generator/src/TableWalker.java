import java.io.File;

public class TableWalker {

	private String file;
	private File inputFile;
	private File outputFile;
	private DFATable dfaTable;
	
	public TableWalker(DFATable table) {
		dfaTable =table;
	}
	
	
	
	public File scan(String input){
		inputFile = new File(input);
			
		
		
		return outputFile;
		
	}
}
