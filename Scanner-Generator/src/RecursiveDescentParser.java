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
		if(left_paren != -1){
			String part = s.substring(left_paren+1);
			int match = -1;
			while(part.indexOf('(') != -1 || part.indexOf(')') != -1){
				int left = part.indexOf("(");
				int right = part.indexOf(")");
				if((left != -1 && right == -1) || (left != -1 && left < right)){
					match--;
					//System.out.println("minus match: "+match);
					if(match == 0){
						right_paren = (right+s.indexOf(part));
						break;
					}
					part = part.substring(left+1);
				}
				else if((right != -1 && left == -1) || (right != -1 && right < left)){
					match++;
					//System.out.println("plus match: "+match);
					if(match == 0){
						right_paren = (right+s.indexOf(part));
						break;
					}
					part = part.substring(right+1);
				}
			}
		}
		int star = s.indexOf("*");
		int plus = s.indexOf("+");
		int or_bar = s.indexOf("|");
		
		//System.out.println("left paren == "+left_paren);
		//System.out.println("right paren == "+right_paren);
		//System.out.println("space = "+space);
		if ((space != -1 && left_paren == -1) || (space != -1 && (space < left_paren || space > right_paren) )) {
			
			/* Concatenation operator */
			String s1 = s.substring(0, space);
			String s2 = s.substring(space + 1);
			//System.out.println("CONCAT - A");
			//System.out.println(s1);
			//System.out.println(s2);
			return concat(s1, s2);
			
		} 
		else if(or_bar != -1 && (or_bar < left_paren || or_bar > right_paren)){
			String after_bar = s.substring(or_bar+1);			
			int or_space = after_bar.indexOf(" ");
			if(or_space == -1) or_space = s.length();
			String s1 = s.substring(0, or_bar);
			String s2 = s.substring(or_bar + 1,or_space);
			//System.out.println("UNION - A");
			//System.out.println(s1);
			//System.out.println(s2);
			return union(s1, s2);
		}
		else if (left_paren != -1 && right_paren != -1) {
			String group = s.substring(left_paren + 1, right_paren);

			//System.out.println("GROUP "+group);
			
			String after_group = "";
			if(right_paren+1 < s.length()){
				after_group = s.substring(right_paren + 1, right_paren + 2);
			}
			////System.out.println(after_group);
			if (after_group.equals("*")) {
				//System.out.println("STAR");
				return star(group);
			} else if (after_group.equals("+")) {
				////System.out.println("PLUS");
				return concat(group, "(" + group + ")*");
			} else {
				int bar = group.indexOf("|");
				int group_space = group.indexOf(" ");
				if (bar != -1) {
					String s1 = group.substring(0, bar);
					String s2 = group.substring(bar + 1);
					//System.out.println("UNION");
					//System.out.println(s1);
					//System.out.println(s2);
					return union(s1, s2);
				}
				else if (group_space != -1) {
					
					/* Concatenation operator */
					String s1 = group.substring(0, group_space);
					String s2 = group.substring(group_space + 1);
					//System.out.println("CONCAT");
					//System.out.println(s1);
					//System.out.println(s2);
					return concat(s1, s2);
				}
				else if(s.charAt(left_paren+1) == '('){
					//System.out.println("PAREN GROUP "+group);
					return next(group);
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
			//System.out.println("STAR - A "+s);
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
			//System.out.println("PLUS "+s);
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
			System.out.println("s: "+s);
			if(!s.equals("") && nfas.containsKey(s)){
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