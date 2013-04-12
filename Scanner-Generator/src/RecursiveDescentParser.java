import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RecursiveDescentParser {

	public HashMap<String, NFA> nfas;
	
	public RecursiveDescentParser(HashMap<String, NFA> nfas) {
		this.nfas = nfas;
	}
	
	/**
	 * Finds the next operation that should be called
	 * 
	 * @param s
	 *            The string to recursive
	 * @throws Exception
	 */
	public NFA next(String s) throws Exception {
		//System.out.println(s);
		int space = s.indexOf(" ");
		int left_paren = s.indexOf("(");
		int right_paren = s.indexOf(")");
		int star = s.indexOf("*");
		int plus = s.indexOf("+");
		if ((space != -1 && left_paren == -1) || (space != -1 && (space < left_paren || space > right_paren) )) {
			
			/* Concatenation operator */
			String s1 = s.substring(0, space);
			String s2 = s.substring(space + 1);
			System.out.println("CONCAT");
			System.out.println(s1);
			System.out.println(s2);
			return concat(s1, s2);
			
		} else if (left_paren != -1 && right_paren != -1) {
			String group = s.substring(left_paren + 1, right_paren);

			System.out.println("GROUP "+group);
			
			String after_group = "";
			if(right_paren+1 < s.length()){
				after_group = s.substring(right_paren + 1, right_paren + 2);
			}
			//System.out.println(after_group);
			if (after_group.equals("*")) {
				System.out.println("STAR");
				return star("("+group+")");
			} else if (after_group.equals("+")) {
				//System.out.println("PLUS");
				return concat(group, "(" + group + ")*");
			} else {
				int or_bar = group.indexOf("|");
				int group_space = group.indexOf(" ");
				if (or_bar != -1) {
					String s1 = group.substring(0, or_bar);
					String s2 = group.substring(or_bar + 1);
					//System.out.println("UNION");
					//System.out.println(s1);
					//System.out.println(s2);
					return union(s1, s2);
				}
				else if (group_space != -1) {
					
					/* Concatenation operator */
					String s1 = group.substring(0, group_space);
					String s2 = group.substring(group_space + 1);
					System.out.println("CONCAT");
					System.out.println(s1);
					System.out.println(s2);
					return concat(s1, s2);
				}
				else{
					if(nfas.containsKey(group)){
						//System.out.println("NFA "+group);
						
						return NFA.clone(nfas.get(group));
						
						
					}
					else return new NFA(group, group, true);
				}	
			}
		}
		else if(star != -1 && s.substring(star-1,star).matches("\\\\*")==false){
			System.out.println("STAR "+s);
			if(star != s.length()-1){
				throw new Exception("Inproper input! Star in middle of string!");
			}
			String toStar = s.substring(star-1,star);
			if(star==1){
				return star(toStar);
			}
			else{
				return concat(s.substring(0,star-1),toStar+"*");
			}
		}
		else if(plus != -1 && s.substring(plus-1,plus).matches("\\\\*")==false){
			System.out.println("PLUS "+s);
			if(plus != s.length()-1){
				throw new Exception("Inproper input! Star in middle of string!");
			}
			String toPlus = s.substring(plus-1,plus);
			if(plus==1){
				return concat(toPlus,toPlus+"*");
			}
			else{
				return concat(s.substring(0,plus-1)+toPlus,toPlus+"*");
			}
		}
		else{
			if(nfas.containsKey(s)){
				//System.out.println("NFA "+s);
				return NFA.clone(nfas.get(s));
			}
			
			else return new NFA(s,s, true);
		}
	}
	
	public NFA concat(String s1, String s2) throws Exception {
		return NFA.concat(next(s1), next(s2));
	}

	public NFA union(String s1, String s2) throws Exception {
		return NFA.union(next(s1), next(s2));
	}

	public NFA star(String s1) throws Exception {
		return NFA.star(next(s1));
	}
}