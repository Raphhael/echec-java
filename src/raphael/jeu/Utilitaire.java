package raphael.jeu;

public class Utilitaire {
	public static String positionFormat(int i) {
		return 	  Constantes.LETTRE[i - 8 * (int)(i / 8)]
				+ Constantes.CHIFFRE[(int)(i / 8)];
	}

	/**
	 * Ligne d'une pièce dont on connait la position dans un tableau
	 * à une seule dimension.
	 * 
	 * @param 	i la position dans un tableau unidimentionnel.
	 * @return	la ligne
	 */
	public static int indexToRow(int i) {
		return (int)(i / 8);
	}
	
	/**
	 * Ligne d'une pièce dont on connait la position dans un tableau
	 * à une seule dimension.
	 * 
	 * @param 	i la position dans un tableau unidimentionnel.
	 * @return	la ligne
	 */
	public static int indexToColumn(int i) {
		return i - 8 * (int)(i / 8);
	}
}
