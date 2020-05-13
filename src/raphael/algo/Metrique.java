package raphael.algo;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Utilisée pour compter certains points des algorithmes
 */
public class Metrique {
	private static HashMap<String, Integer> dict = new HashMap<>();
	
	public static void update(String key) {
		Integer k = dict.get(key);
		if (k == null)
			dict.put(key, 1);
		else
			dict.put(key, k + 1);
	}

	public static void reset() {
		dict.clear();
	}
	public static void reset(String key) {
		dict.remove(key);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (Entry<String, Integer> i : dict.entrySet())
			sb.append(i.getKey())   .append('\t')
			  .append(i.getValue()) .append(System.lineSeparator());
		
		return sb.toString();
	}
}
