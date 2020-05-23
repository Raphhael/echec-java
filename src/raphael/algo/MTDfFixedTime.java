package raphael.algo;

import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.structures.Noeud;
import raphael.algo.structures.TTElement;

/**
 * Algorithme MTD-f avec limite de temps
 */
public class MTDfFixedTime extends MTDf {
	@Override
	public Noeud start(Noeud noeud) throws NoMoreChoicesException {
		TTElement ttElement;
		int profondeur = 1;
		int score = Constantes.MOINS_INFINI;

		do {
			ttElement = getTable().retrieveBest(profondeur);
			score = MTDF(noeud, score, ++profondeur);
			if (score > Constantes.MAX_SCORE_SEUIL)
				return getTable().retrieveBest(profondeur).getMove();
		} while (!timesUp());

		if (ttElement == null || ttElement.getMove() == null)
			throw new NoMoreChoicesException();

		return ttElement.getMove();
	}

	@Override
	public boolean mtdf_stop_cond(int inf, int sup) {
		return super.mtdf_stop_cond(inf, sup) && !timesUp();
	}

	@Override
	public boolean stopCondition(Noeud noeud, int profondeur) {
		return super.stopCondition(noeud, profondeur) || timesUp();
	}
}
