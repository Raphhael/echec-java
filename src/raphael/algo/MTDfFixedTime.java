package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;

/**
 * Algorithme MTD-f avec limite de temps
 */
public class MTDfFixedTime extends MTDf {
	@Override
	public Noeud start(Noeud noeud, int profondeurMax) throws NoMoreChoicesException {
		return super.start(noeud, Constantes.PLUS_INFINI);
	}
	
	@Override
	public boolean mtdf_stop_cond(int inf, int sup) {
		return super.mtdf_stop_cond(inf, sup) && !timesUp();
	}
}
