package raphael.algo;

import raphael.algo.structures.Algorithme;
import raphael.algo.structures.ListeDeNoeuds;
import raphael.algo.structures.Noeud;

/**
 * Algo minimax basique
 */
public class Minimax extends Algorithme {

	public Noeud start(Noeud noeud) {
		Noeud meilleurNoeud = null;
		int meilleurScore = Constantes.MOINS_INFINI;

		for (Noeud fils : noeud.successeurs()) {
			int minimax = miniMax(fils, getProfondeurMax() - 1, false);
			if(meilleurNoeud == null || minimax > meilleurScore) {
				meilleurScore = minimax;
				meilleurNoeud = fils;
			}
		}

		return meilleurNoeud;
	}

	private int miniMax(Noeud noeud, int profondeur, boolean estMax) {
		if(stopCondition(noeud, profondeur))
			return noeud.evaluation(getJoueur());
		
		ListeDeNoeuds<?> successeurs = noeud.successeurs();
		int              currentValue = estMax ? Constantes.MOINS_INFINI : Constantes.PLUS_INFINI;
		
		if(estMax)
			for (Noeud fils : successeurs)
				currentValue = Math.max(currentValue,miniMax(fils, profondeur - 1, false));
		else
			for (Noeud fils : successeurs)
				currentValue = Math.min(currentValue, miniMax(fils, profondeur - 1, true));
		
		return currentValue;
	}
}
