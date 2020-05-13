package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;

/**
 * Algorithme MTD-f en utilisant l'alpha-beta memory
 * @author raphael
 */
public class MTDf extends AlphaBetaMemory {
		
	public Noeud start(Noeud noeud, int profondeurMax) throws NoMoreChoicesException {
		TTElement ttElement;
		
		for(int d = 1, fenetre = 0; d <= profondeurMax; d++)
			fenetre = MTDF(noeud, fenetre, d);
		
		ttElement = getTable().retrieveBest(profondeurMax);
		
		getTable().clear();
		
		if(ttElement.getMove() != null)
			return ttElement.getMove();
		
		throw new NoMoreChoicesException();
	}
	
	public int MTDF(Noeud noeud, int fenetre, int profondeur) {
		int value = fenetre;
		int inf = Constantes.MOINS_INFINI;
		int sup = Constantes.PLUS_INFINI;
		
		while (mtdf_stop_cond(inf, sup)) {
			int beta = (value == inf) ? value + 1 : value;
			value = alphaBeta(noeud, profondeur, true, beta - 1, beta);
			
			if(value < beta) sup = value;
			else 		 inf = value;
		}
		return value;
	}
	
	public boolean mtdf_stop_cond(int inf, int sup) {
		return inf < sup;
	}
}

