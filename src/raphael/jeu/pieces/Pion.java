package raphael.jeu.pieces;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;
import raphael.jeu.Utilitaire;

public class Pion extends Piece {

	public Pion(CouleurPiece couleur) {
		super(couleur);
	}

	public static ListeDeCoups listeCoups(Plateau plateau, int position, int piece) {
		ListeDeCoups liste = new ListeDeCoups();
		CouleurPiece couleur = Piece.getCouleur(piece);
		
		if(couleur == CouleurPiece.BLANC) {
			if(checkerCase(-10, position, plateau, couleur, liste) && Utilitaire.indexToRow(position) == 6)
				checkerCase(-20, position, plateau, couleur, liste);
			mangerDiagonale(-11, position, plateau, couleur, liste);
			mangerDiagonale(-9, position, plateau, couleur, liste);
		}
		else {
			if(checkerCase(10, position, plateau, couleur, liste) && Utilitaire.indexToRow(position) == 1)
				checkerCase(20, position, plateau, couleur, liste);

			mangerDiagonale(11, position, plateau, couleur, liste);
			mangerDiagonale(9, position, plateau, couleur, liste);
		}
		return liste;
	}
	
	private static boolean checkerCase(int deplacement, int position, Plateau plateau, CouleurPiece couleur, ListeDeCoups coups) {
		int to = positionTAB120(deplacement, position);
		if(to != -1 && plateau.getCase(to) == 0) {
			if(
				(Utilitaire.indexToRow(position) == 1 && couleur == CouleurPiece.BLANC)
				|| (Utilitaire.indexToRow(position) == 6 && couleur == CouleurPiece.NOIR)
			)
				coups.add(new Coup(position, to, TypeCoup.PION_DAME));
			else
				coups.add(new Coup(position, to));
			return true;
		}
		return false;
	}
	
	private static void mangerDiagonale(int deplacement, int position, Plateau plateau, CouleurPiece couleur, ListeDeCoups coups) {
		int to = positionTAB120(deplacement, position);
		if(		to != -1 
				&& plateau.getCase(to) != 0 
				&& Piece.getCouleur(plateau.getCase(to)) != couleur) {
			if(
				(Utilitaire.indexToRow(position) == 1 && couleur == CouleurPiece.BLANC)
				|| (Utilitaire.indexToRow(position) == 6 && couleur == CouleurPiece.NOIR)
			)
				coups.add(new Coup(position, to, TypeCoup.PION_DAME));
			else
				coups.add(new Coup(position, to));
		}
	}
	
	@Override
	protected int getValue() {
		return 100;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x04;
	}
	
	@Override
	public int bitvalue() {
		return 32;
	}
}
