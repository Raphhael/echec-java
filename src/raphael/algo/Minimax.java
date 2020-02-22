package raphael.algo;

import java.util.List;

public class Minimax implements Algorithme {
	private static Noeud bon;
	private static Joueur joueur;
	private static int profOri;
	private static int calc = 0;

	public Noeud start(Noeud noeud, int profondeur, Joueur joueur) {
		Minimax.joueur = joueur;
		profOri = profondeur;
		Noeud best = miniMax(noeud, profondeur, true);
		System.out.println("Meilleur score : " + best.evaluation(joueur));
		System.out.println("calc " + calc + " successeurs()");
		return bon;
	}
	private static Noeud miniMax(Noeud noeud, int profondeur, boolean estMax) {
		if(profondeur == 0 || noeud.estTerminal())
			return noeud;
		calc++;
		

		List<Noeud> successeurs = noeud.successeurs();
		if(successeurs.size() <= 1)
			return successeurs.get(0);
		
		if(estMax) {
			Noeud maximum = new Noeud.NoeudMin();
			for (Noeud fils : successeurs) {
				Noeud noeudRemonte = miniMax(fils, profondeur - 1, false);

				if(maximum.evaluation(joueur) < noeudRemonte.evaluation(joueur)) {
					maximum = noeudRemonte;
					if(profOri == profondeur)
						bon = fils;
				}
			}
			return maximum;
		}
		else {
			Noeud minimum = new Noeud.NoeudMax();
			for (Noeud fils : successeurs) {
				Noeud noeudRemonte = miniMax(fils, profondeur - 1, true);

				if(minimum.evaluation(joueur) > noeudRemonte.evaluation(joueur))
					minimum = noeudRemonte;
			}
			return minimum;
		}
	}
}
