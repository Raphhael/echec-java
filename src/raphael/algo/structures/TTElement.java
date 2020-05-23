package raphael.algo.structures;

public class TTElement {
	/**
	 * Profondeur où à été calculé ce noeud
	 */
	private int profondeur;
	
	/**
	 * Valeur du noeud (valeur remontée des profondeurs, pas l'évaluation
	 * du noeud)
	 */
	private int score;
	
	/**
	 * Meilleur noeud suivant
	 */
	private Noeud bestMove;
	
	/**
	 * Type du noeud : si il a été calculé sans coupe, si c'est une
	 * limite inférieure ou supérieure.
	 */
	public static enum TypeNoeud { EXACT, ALPHA, BETA }
	
	/**
	 * Type du noeud
	 */
	private TypeNoeud type;
	
	
	public TTElement(int profondeur, int score, TypeNoeud type, Noeud bestMove) {
		this.profondeur = profondeur;
		this.score = score;
		this.type = type;
		this.bestMove = bestMove;
	}

	public void decreaseProfondeur() { --profondeur; }
	public int getProfondeur() { return profondeur; }
	public int getScore() { return score; }
	public TypeNoeud getType() { return type; }
	public Noeud getMove() { return bestMove; }
}
