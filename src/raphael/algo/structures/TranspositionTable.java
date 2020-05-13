package raphael.algo.structures;

import java.util.HashMap;
import java.util.Iterator;

public class TranspositionTable extends HashMap<Long, TTElement> {
	private static final long serialVersionUID = 2576969364178610113L;
	public TranspositionTable() {
		super(100000);
	}
	
	public void maj() {
		Iterator<Long> it = keySet().iterator();
		while (it.hasNext()) {
			Long key = it.next();
			TTElement elem = get(key);
			if(elem.getProfondeur() <= 1)
				it.remove();
			else
				elem.decreaseProfondeur();
		}
	}
	
	public TTElement retrieveBest(int profondeur) {
		TTElement best = null;
		for (TTElement elem : values()) 
			if(elem.getProfondeur() == profondeur
				&& (best == null
					|| elem.getEvaluation() > best.getEvaluation())) {
//				System.out.println("ELEM : CHOISI eval=" + elem.getEvaluation() + ", prof="  + elem.getProfondeur());
				best = elem;
			}

		return best;
	}
}
