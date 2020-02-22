package raphael.jeu.pieces;

import java.util.ArrayList;
import java.util.List;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Coup;
import raphael.jeu.Piece;
import raphael.jeu.Plateau;

public class Pion extends Piece {
	private List<Coup> coups;

	public Pion(Pion pion) {
		super(pion);
	}
	
	public Pion(CouleurPiece couleur) {
		super(couleur);
	}
	
	public Pion(CouleurPiece couleur, int position) {
		super(couleur, position);
	}

	@Override
	public List<Coup> listeCoups(boolean allerProfond) {
		this.coups = new ArrayList<Coup>(5);
		if(getCouleur() == CouleurPiece.BLANC) 
			listeCoupsPionBlanc();
		else
			listeCoupsPionNoir();
		return coups;
	}
	
	private boolean checkerCase(int deplacement) {
		int to = positionTAB120(deplacement);
		if(to != -1 && getPlateau().getCase(to) == null) {
			coups.add(new Coup(getPosition(), to));
			return true;
		}
		return false;
	}
	
	private void mangerDiagonale(int deplacement) {
		int to = positionTAB120(deplacement);
		if(		to != -1 
				&& getPlateau().getCase(to) != null 
				&& getPlateau().getCase(to).getCouleur() != getCouleur())
			coups.add(new Coup(getPosition(), to));
	}

	private void listeCoupsPionBlanc() {

		if(checkerCase(-10) && Plateau.indexToRow(getPosition()) == 6)
			checkerCase(-20);

		mangerDiagonale(-11);
		mangerDiagonale(-9);
	}
	
	
	private void listeCoupsPionNoir() {
		if(checkerCase(10) && Plateau.indexToRow(getPosition()) == 1)
			checkerCase(20);

		mangerDiagonale(11);
		mangerDiagonale(9);
	}
	
	@Override
	protected int getValue() {
		return 100;
	}
	
	@Override
	protected Pion makeCopy() {
		return new Pion(this);
	}
}
