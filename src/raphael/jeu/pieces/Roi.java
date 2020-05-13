package raphael.jeu.pieces;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

public class Roi extends Piece {

	private static final int [] DEPLACEMENTS = { -11,-9, 11, 9, -10, 10, -1, 1 };
		
	public Roi(CouleurPiece couleur) {
		super(couleur);
	}

	public static ListeDeCoups listeCoups(Plateau plateau, int position,  int piece, boolean allerProfond) {
		ListeDeCoups listeCoups = new ListeDeCoups();
		CouleurPiece couleur = Piece.getCouleur(piece);
		
		for (int i = 0; i < DEPLACEMENTS.length; i++) {
			int to = Piece.positionTAB120(DEPLACEMENTS[i], position);
			
			if((to != -1 && plateau.getCase(to) == 0)
					|| (to != -1 && Piece.getCouleur(plateau.getCase(to)) != couleur) ) {
				listeCoups.add(new Coup(position, to, TypeCoup.ROI_BOUGE));
			}
		}
		
		if(allerProfond) {
			if(couleur == CouleurPiece.BLANC) {
				/********** GRAND ROQUE BLANC ****************/
				if(plateau.getEtat().isGrandRoqueBlanc()
						&& plateau.getCase(57) == 0
						&& plateau.getCase(58) == 0
						&& plateau.getCase(59) == 0
						&& !plateau.estAttaquee(57, CouleurPiece.NOIR)
						&& !plateau.estAttaquee(58, CouleurPiece.NOIR)
						&& !plateau.estAttaquee(59, CouleurPiece.NOIR)
						&& !plateau.estAttaquee(60, CouleurPiece.NOIR))
					listeCoups.add(new Coup(60, 58, Coup.TypeCoup.ROQUE_GRAND_BLANC));
				/********** PETIT ROQUE BLANC ****************/
				if(plateau.getEtat().isPetitRoqueBlanc()
						&& plateau.getCase(61) == 0
						&& plateau.getCase(62) == 0
						&& !plateau.estAttaquee(60, CouleurPiece.NOIR)
						&& !plateau.estAttaquee(61, CouleurPiece.NOIR)
						&& !plateau.estAttaquee(62, CouleurPiece.NOIR)
					)
					listeCoups.add(new Coup(60, 62, Coup.TypeCoup.ROQUE_PETIT_BLANC));
				
			}
			else if(couleur == CouleurPiece.NOIR) {
				/********** GRAND ROQUE NOIR ****************/
				if(plateau.getEtat().isGrandRoqueNoir()
						&& plateau.getCase(1) == 0
						&& plateau.getCase(2) == 0
						&& plateau.getCase(3) == 0
						&& !plateau.estAttaquee(1, CouleurPiece.BLANC)
						&& !plateau.estAttaquee(2, CouleurPiece.BLANC)
						&& !plateau.estAttaquee(3, CouleurPiece.BLANC)
						&& !plateau.estAttaquee(4, CouleurPiece.BLANC))
					listeCoups.add(new Coup(4, 2, Coup.TypeCoup.ROQUE_GRAND_NOIR));
				/********** PETIT ROQUE NOIR ****************/
				if(plateau.getEtat().isPetitRoqueNoir()
						&& plateau.getCase(5) == 0
						&& plateau.getCase(6) == 0
						&& !plateau.estAttaquee(6, CouleurPiece.BLANC)
						&& !plateau.estAttaquee(5, CouleurPiece.BLANC)
						&& !plateau.estAttaquee(4, CouleurPiece.BLANC))
					listeCoups.add(new Coup(4, 6, Coup.TypeCoup.ROQUE_PETIT_NOIR));
			}
		}
		
		return listeCoups;
	}

	@Override
	protected int getValue() {
		return 0;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x02;
	}
	
	@Override
	public int bitvalue() {
		return 1;
	}
}
