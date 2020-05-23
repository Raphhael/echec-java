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
	
	/**
	 * Retrouver le meilleur élément à une certaine profondeur.
	 * 
	 * @param profondeur
	 * @return le meilleur élément de la table
	 */
	public TTElement retrieveBest(int profondeur) {
		TTElement best = null;
		for (TTElement elem : values()) {
			if(elem.getProfondeur() == profondeur
				&& (best == null
					|| elem.getScore() > best.getScore())) {
				best = elem;
			}
		}

		return best;
	}
}
