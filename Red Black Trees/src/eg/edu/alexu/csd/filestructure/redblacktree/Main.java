package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ITreeMap<Integer, String> tree = new TreeMap<>();
		
		tree.put(0, "a");
		
		tree.put(7, "a");
		tree.put(12, "h");
		tree.put(8, "a");
		tree.put(4, "y");
		
		
		
		Set<Map.Entry<Integer, String>> set = new LinkedHashSet<>();
		set = tree.entrySet();
		
		ArrayList<Map.Entry<Integer, String>> s = tree.headMap(12);
				
		System.out.println(s);
		
		
		
		
		
	}

}
