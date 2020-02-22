package raphael.jeu;

public class Utilitaire {
	public static String positionFormat(int i) {
		return 	  Constantes.LETTRE[i - 8 * (int)(i / 8)]
				+ Constantes.CHIFFRE[(int)(i / 8)];
	}
}
