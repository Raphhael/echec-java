package raphael.jeu.pieces;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Coup.TypeCoup;
import raphael.jeu.ListeDeCoups;
import raphael.jeu.Piece;

public class Roi extends Piece {

	private final int [] DEPLACEMENTS = { -11,-9, 11, 9, -10, 10, -1, 1 };
	
	ListeDeCoups listeCoups;

	public Roi(Roi roi) {
		super(roi);
	}
	
	public Roi(CouleurPiece couleur) {
		super(couleur);
	}
	
	public Roi(CouleurPiece couleur, int position) {
		super(couleur, position);
	}

	@Override
	public ListeDeCoups listeCoups(boolean allerProfond) {
		listeCoups = new ListeDeCoups();
		
		for (int deplacement : DEPLACEMENTS) {
			int to = positionTAB120(deplacement);
			
			if((to != -1 && getPlateau().getCase(to) == null)
					|| (to != -1 && getPlateau().getCase(to).getCouleur() != getCouleur()) ) {
				listeCoups.add(new Coup(getPosition(), to, TypeCoup.ROI_BOUGE));
			}
		}
		
		if(allerProfond) {
			if(getCouleur() == CouleurPiece.BLANC) {
				grandRoqueBlanc();
				petitRoqueBlanc();
			}
			else if(getCouleur() == CouleurPiece.NOIR) {
				grandRoqueNoir();
				petitRoqueNoir();
			}
		}
		
		return listeCoups;
	}


	private void petitRoqueBlanc() {
		if(getPlateau().getEtat().isPetitRoqueBlanc()
				&& getPlateau().getCase(61) == null
				&& getPlateau().getCase(62) == null
				&& !getPlateau().estAttaquee(60, CouleurPiece.NOIR)
				&& !getPlateau().estAttaquee(61, CouleurPiece.NOIR)
				&& !getPlateau().estAttaquee(62, CouleurPiece.NOIR)
			)
			listeCoups.add(new Coup(60, 62, Coup.TypeCoup.ROQUE_PETIT_BLANC));
	}
	private void petitRoqueNoir() {
		if(getPlateau().getEtat().isPetitRoqueNoir()
				&& getPlateau().getCase(5) == null
				&& getPlateau().getCase(6) == null
				&& !getPlateau().estAttaquee(6, CouleurPiece.BLANC)
				&& !getPlateau().estAttaquee(5, CouleurPiece.BLANC)
				&& !getPlateau().estAttaquee(4, CouleurPiece.BLANC))
			listeCoups.add(new Coup(4, 6, Coup.TypeCoup.ROQUE_PETIT_NOIR));
	}
	private void grandRoqueBlanc() {
		if(getPlateau().getEtat().isGrandRoqueBlanc()
				&& getPlateau().getCase(57) == null
				&& getPlateau().getCase(58) == null
				&& getPlateau().getCase(59) == null
				&& !getPlateau().estAttaquee(57, CouleurPiece.NOIR)
				&& !getPlateau().estAttaquee(58, CouleurPiece.NOIR)
				&& !getPlateau().estAttaquee(59, CouleurPiece.NOIR)
				&& !getPlateau().estAttaquee(60, CouleurPiece.NOIR))
			listeCoups.add(new Coup(60, 58, Coup.TypeCoup.ROQUE_GRAND_BLANC));
	}
	private void grandRoqueNoir() {
		if(getPlateau().getEtat().isGrandRoqueNoir()
				&& getPlateau().getCase(1) == null
				&& getPlateau().getCase(2) == null
				&& getPlateau().getCase(3) == null
				&& !getPlateau().estAttaquee(1, CouleurPiece.BLANC)
				&& !getPlateau().estAttaquee(2, CouleurPiece.BLANC)
				&& !getPlateau().estAttaquee(3, CouleurPiece.BLANC)
				&& !getPlateau().estAttaquee(4, CouleurPiece.BLANC))
			listeCoups.add(new Coup(4, 2, Coup.TypeCoup.ROQUE_GRAND_NOIR));
	}
	@Override
	protected int getValue() {
		return 20000;
	}

	@Override
	protected byte getZobriestValue() {
		return 0x02;
	}
	
	@Override
	protected Roi makeCopy() {
		return new Roi(this);
	}
}
