package raphael.algo;

import raphael.algo.structures.Algorithme;
import raphael.algo.structures.ListeDeNoeuds;
import raphael.algo.structures.Noeud;

/**
 * Algo minimax basique
 */
public class Minimax extends Algorithme {

	public Noeud start(Noeud noeud, int profondeur) {
		Noeud meilleurNoeud = null;

		for (Noeud fils : noeud.successeurs())
			if(meilleurNoeud == null || miniMax(fils, profondeur - 1, false) > meilleurNoeud.evaluation(getJoueur()))
				meilleurNoeud = fils;

		return meilleurNoeud;
	}

	private int miniMax(Noeud noeud, int profondeur, boolean estMax) {
		if(stopCondition(noeud, profondeur))
			return noeud.evaluation(getJoueur());
		
		ListeDeNoeuds<?> successeurs = noeud.successeurs();
		int              currentValue = estMax ? Constantes.MOINS_INFINI : Constantes.PLUS_INFINI;
		
		if(estMax)
			for (Noeud fils : successeurs)
				currentValue = Math.max(currentValue,
										miniMax(fils, profondeur - 1, false));
		else
			for (Noeud fils : successeurs)
				currentValue = Math.min(currentValue,
										miniMax(fils, profondeur - 1, true));
		
		return currentValue;
	}
}
