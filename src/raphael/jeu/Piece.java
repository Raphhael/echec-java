package raphael.jeu;

import java.util.List;

/**
 * Classe abstraite pour toutes les pièces de l'échéquier
 * 
 */
public abstract class Piece implements Cloneable {
	private CouleurPiece	couleur;
	private int 			position;
	private Plateau			plateau;

	
	/***** Constructeurs *********/
	
	public Piece(Piece piece) {
		this(piece.couleur, piece.position);
		this.plateau = piece.plateau;
	}
	
	public Piece(CouleurPiece couleur) {
		this.couleur = couleur;
	}
	
	public Piece(CouleurPiece couleur, int position) {
		this.couleur = couleur;
		this.position = position;
	}
	
	/**
	 * Réalise une copie exacte de la pièce.
	 * Chaque classe fille utilise le constructeur Piece::Piece(Piece piece)
	 */
	protected abstract Piece makeCopy();
	
	/**
	 * Valeur utilisée dans la fonction d'évaluation.
	 * @return	La valeur stratégique de la piece
	 */
	protected abstract int getValue();
	protected abstract byte getZobriestValue();
	public byte zobriestValue() {
		return (byte) ((couleur == CouleurPiece.NOIR ? 0x01 : 0x00)
			           | getZobriestValue());
	}
	
	/**
	 * Calcule la liste des coups possibles de la piece sur le plateau.
	 * Le flag allerProfond est là pour les mêmes raisons que pour la 
	 * fonction Plateau::calculerCoups. Il permet d'éviter les boucles 
	 * infinies.
	 * 
	 * @param	allerProfond	Si true, execute les appels récursif (estAttaque).
	 * @return					La liste des coups possible pour cette pièce.
	 */
	public    abstract List<Coup> listeCoups(boolean allerProfond);

	
	/**
	 * Utilise la méthode de la boîte aux lettres pour voir
	 * si le coup est hors du jeu d'echec ou pas.
	 * 
	 * @param	deplacement Le nombre de cases
	 * @return				La position calculée (doit être différent de -1 si OK)
	 */
	public int positionTAB120(int deplacement) {
		return Constantes.TAB120[Constantes.TAB64[getPosition()] + deplacement];
	}
	

	/******** Getters & Setters ********/
	
	public CouleurPiece getCouleur() { return couleur; }

	public int  		getPosition() { return position; }
	public void 		setPosition(int position) { this.position = position; }
	
	public Plateau 		getPlateau() { return plateau; }
	public void    		setPlateau(Plateau plateau) { this.plateau = plateau; }
	
	public int  		getColonne() { return position - 8 * (int)(position / 8); }

	@Override
	public String toString() {
		return 	String.valueOf(getClass().getSimpleName()) + 
				String.valueOf(couleur.name().charAt(0)) 
				+ "@" + Utilitaire.positionFormat(position);
	}

	
}
