package raphael.jeu;

import raphael.jeu.pieces.Cavalier;
import raphael.jeu.pieces.Dame;
import raphael.jeu.pieces.Fou;
import raphael.jeu.pieces.Pion;
import raphael.jeu.pieces.Roi;
import raphael.jeu.pieces.Tour;

/**
 * Si la case égale 0, il n'y a aucune piece dessus.
 * Sinon le format est un entier (4 octets) tel que :
 * 
 * 0                13         17          23        24        25       31
 * +--------+-------+----------+-----------+---------+---------+--------+
 * |   EVAL VALUE   | ZOBRIEST | POSITION  | A BOUGÉ | COULEUR | PCFTDR |
 * +--------+-------+----------+-----------+---------+---------+--------+
 * 
 * avec :
 * 	- EVAL VALUE: Valeur de la pièce dans la fonction d'évaluation
 *  - ZOBRIEST: Valeur de la pièce pour l'algo de hashage
 *  - POSITION: sur l'échéquier
 *  - A BOUGÉ: Flag 
 *  - COULEUR: 0 = blanc, 1 = noir
 *  - PCFTDR: Pion, Cavalier, Fou, Tour, Dame, Roi
 */
public abstract class Piece implements Cloneable {
	private CouleurPiece	couleur;
	
	/***** Constructeurs *********/
	public Piece(CouleurPiece couleur) {
		this.couleur = couleur;
	}
	
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
	 * La pièce est identifée par ses 6 derniers bits.
	 * 
	 * @return les 6 derniers bits pour identifiant le type de pièce
	 */
	public abstract int bitvalue();
	
	/**
	 * Calcule la liste des coups possibles d'une piece sur le plateau.
	 * Le flag allerProfond est là pour les mêmes raisons que pour la 
	 * fonction Plateau::calculerCoups. Il permet d'éviter les boucles 
	 * infinies.
	 * 
	 * @param	plateau			Liens vers le plateau
	 * @param	piece			Piece à vérifier
	 * @param	allerProfond	Si true, execute les appels récursif (estAttaque).
	 * @return					La liste des coups possible pour cette pièce.
	 */
	public	static	ListeDeCoups listeCoups(Plateau plateau, int piece, boolean allerProfond) {
		
		switch(piece & 63) {
			case 1: return Roi.listeCoups(plateau, piece, allerProfond);
			case 2: return Dame.listeCoups(plateau, piece);
			case 4: return Tour.listeCoups(plateau, piece);
			case 8: return Fou.listeCoups(plateau, piece);
			case 16: return Cavalier.listeCoups(plateau, piece);
			case 32: return Pion.listeCoups(plateau, piece);
			default: return null;
		}
	}

	
	/**
	 * Utilise la méthode de la boîte aux lettres pour voir
	 * si le coup est hors du jeu d'echec ou pas.
	 * 
	 * @param	deplacement Le nombre de cases
	 * @return				La position calculée (doit être différent de -1 si OK)
	 */
	public static int positionTAB120(int deplacement, int position) {
		return Constantes.TAB120[Constantes.TAB64[position] + deplacement];
	}
	
	
	/**
	 * Converti une pièce au format entier
	 * @param	piece	l'objet pièce
	 * @return			la pièce en entier
	 */
	public static int makePiece(Piece piece) {
		int nouv = 0;
		
		nouv |= piece.bitvalue();
		nouv |= ((piece.getValue() < 0x1FFF) ? piece.getValue() : 0x1FFF) << 19;
		nouv |= piece.zobriestValue() << 15;
		
		nouv = setCouleur(nouv, piece.getCouleur());
		
		return nouv;
	}

	public static int setPosition(int piece, int pos) {
		return piece | pos << 9;
	}

	public static int getPosition(int piece) {
		return 63 & piece >> 9 ;
	}

	public static int getZobriestValue(int piece) {
		return  0xF & piece >>> 15;
	}
	
	/**
	 * Récupère la valeur d'une pièce (valeur au sens 
	 * de la fonction d'évaluation)
	 * 
	 * @param	piece
	 * @return	
	 */
	public static int getEvalValue(int piece) {
		return  piece >>> 19;
	}
	
	/**
	 * Change la couleur d'une pièce
	 * 
	 * @param	piece	La pièce
	 * @param	couleur	La nouvelle couleur
	 * @return			la nouvelle pièce
	 */
	public static int setCouleur(int piece, CouleurPiece couleur) {
		return (couleur == CouleurPiece.BLANC) ?
				(piece & 0xFFFFFF7F) : (piece | 1 << 7);
	}
	public static CouleurPiece getCouleur(int piece) {
		return  (1 & piece >> 7) == 0 ? CouleurPiece.BLANC : CouleurPiece.NOIR;
	}
	
	/**
	 * Est ce que la piece a bougé (utile surtout pour
	 * la tour).
	 * 
	 * @param	La piece
	 * @return	Vrai si le flag "a bougé" est a 1
	 */
	public static boolean getABouge(int piece) {
		return  (1 & piece >> 8) == 0 ? false : true;
	}
	
	/**
	 * @param	piece La piece
	 * @return	La nouvelle piece
	 */
	public static int setABouge(int piece) {
		return  piece & 0xFFFFFEFF;
	}
	
	
	
	
	
	public static String toString(int piece) {
		StringBuffer sb = new StringBuffer();
		sb.append(piece).append(System.lineSeparator())
		  .append(Integer.toBinaryString(piece)).append(System.lineSeparator())
		  .append("Couleur : " + getCouleur(piece).name()).append(System.lineSeparator())
		  .append("Valeur : " + getEvalValue(piece)).append(System.lineSeparator())
		  .append("Zobriest : " + getZobriestValue(piece)).append(System.lineSeparator());
		
		return sb.toString();
	}
	
	public static String toSmallString(int piece) {
		switch(piece & 63) {
			case 1: return "R";
			case 2: return "D";
			case 4: return "T";
			case 8: return "F";
			case 16: return "C";
			case 32: return "P";
			default: return " ";
		}
	}

	public CouleurPiece getCouleur() { return couleur; }
}
