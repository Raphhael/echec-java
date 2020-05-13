package raphael.jeu;

import raphael.algo.structures.Joueur;

public enum CouleurPiece implements Joueur {
	NOIR,
	BLANC;
	
	public CouleurPiece oppose() {
		return this == NOIR ? BLANC : NOIR;
	}
}
