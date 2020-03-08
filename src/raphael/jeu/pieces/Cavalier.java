package raphael.jeu.pieces;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

public class Cavalier extends Piece {
	private static final int [] DEPLACEMENTS = { -12,-21,-19,-8,12,21,19,8 };
	
	public Cavalier(CouleurPiece couleur) {
		super(couleur);
	}

	public static ListeDeCoups listeCoups(Plateau plateau, int position, int piece) {
		ListeDeCoups liste = new ListeDeCoups();		
		
		for (int deplacement : DEPLACEMENTS) {
			int to = positionTAB120(deplacement, position);

			if((to != -1 && plateau.getCase(to) == 0) 
					|| (to != -1 && Piece.getCouleur(plateau.getCase(to)) != Piece.getCouleur(piece)))
				liste.add(new Coup(position, to));

		}
		return liste;
	}

	@Override
	protected int getValue() {
		return 320;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x0A;
	}
	
	@Override
	public int bitvalue() {
		return 16;
	}
}
