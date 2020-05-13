package raphael.algo.structures;

public class TTElement {
	private int profondeur;
	private int evaluation;
	private Noeud bestMove;
	
	public static enum TypeNoeud { EXACT, ALPHA, BETA }
	private TypeNoeud type;
	
	public TTElement(int profondeur, int evaluation, TypeNoeud type, Noeud bestMove) {
		this.profondeur = profondeur;
		this.evaluation = evaluation;
		this.type = type;
		this.bestMove = bestMove;
	}

	public void decreaseProfondeur() { --profondeur; }
	public int getProfondeur() { return profondeur; }
	public int getEvaluation() { return evaluation; }
	public TypeNoeud getType() { return type; }
	public Noeud getMove() { return bestMove; }
}
