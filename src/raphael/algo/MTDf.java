package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;

/**
 * Algorithme MTD-f en utilisant l'alpha-beta memory
 * @author raphael
 */
public class MTDf extends AlphaBetaMemory {

	public Noeud start(Noeud noeud) throws NoMoreChoicesException {
		TTElement ttElement;
		int d = 1;
		for(int fenetre = Constantes.MOINS_INFINI; d <= getProfondeurMax(); d++)
			fenetre = MTDF(noeud, fenetre, d);
		
		ttElement = getTable().retrieveBest(d-1);
				
		if(ttElement.getMove() != null) {
			return ttElement.getMove();
		}
		
		throw new NoMoreChoicesException();
	}
	
	public int MTDF(Noeud noeud, int fenetre, int profondeur) {
		System.out.println("MTDF - niveau " + profondeur);
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
	
	/**
	 * Condition d'arrêt pour MTD-f
	 * 
	 * @param inf
	 * @param sup
	 * @return True si on doit continuer, false si il faut arrêter
	 */
	public boolean mtdf_stop_cond(int inf, int sup) {
		return inf < sup && inf < Constantes.MAX_SCORE_SEUIL;
	}
	
	@Override
	public void endRoutine() {
		getTable().clear();
	}
}

