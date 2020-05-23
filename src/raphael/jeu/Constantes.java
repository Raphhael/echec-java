package raphael.jeu;

import java.util.Random;

import raphael.jeu.pieces.Cavalier;
import raphael.jeu.pieces.Dame;
import raphael.jeu.pieces.Fou;
import raphael.jeu.pieces.Pion;
import raphael.jeu.pieces.Roi;
import raphael.jeu.pieces.Tour;

public interface Constantes {

	
	/***************** Poids pour la fonctions d'évaluation ************/

	// Plus il est élevé plus les pieces vont vers l'avant
	float	COEF_AVANCEMENT_PIECES = 0.01f;

	// Plus il est élevé plus on va maitriser le centre
	int		COEF_CONTROLE_CENTRE = 35;
	
	// Valeur du roque
	int		COEF_ROQUER = 80;
	float	COEF_EMPECHER_ROQUE_ENNEMI = 40; // pourcentage de _ROQUER

	// Forcer la dame à ne pas bouger en début de partie
	int		COEF_FORCER_DAME = 90;
	int		COEF_FORCER_DAME_ENNEMIE_A_BOUGER = 30;
	

	public static final Random GENERATOR = new Random();
	
	int [] TAB120 = {
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1,  0,  1,  2,  3,  4,  5,  6,  7, -1,
	    -1,  8,  9, 10, 11, 12, 13, 14, 15, -1,
	    -1, 16, 17, 18, 19, 20, 21, 22, 23, -1,
	    -1, 24, 25, 26, 27, 28, 29, 30, 31, -1,
	    -1, 32, 33, 34, 35, 36, 37, 38, 39, -1,
	    -1, 40, 41, 42, 43, 44, 45, 46, 47, -1,
	    -1, 48, 49, 50, 51, 52, 53, 54, 55, -1,
	    -1, 56, 57, 58, 59, 60, 61, 62, 63, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

	public final static int [] TAB64 = {
	    21, 22, 23, 24, 25, 26, 27, 28,
	    31, 32, 33, 34, 35, 36, 37, 38,
	    41, 42, 43, 44, 45, 46, 47, 48,
	    51, 52, 53, 54, 55, 56, 57, 58,
	    61, 62, 63, 64, 65, 66, 67, 68,
	    71, 72, 73, 74, 75, 76, 77, 78,
	    81, 82, 83, 84, 85, 86, 87, 88,
	    91, 92, 93, 94, 95, 96, 97, 98
	};
	
	String[] LETTRE = {"A", "B", "C", "D", "E", "F", "G", "H"};
	String[] CHIFFRE = {"8", "7", "6", "5", "4", "3", "2", "1"};

	
	
	/***** QUELQUES DISPOSITIONS POUR DES TESTS ******/
	
	/* La constante PLATEAU_DEFAUT sera chargée au lancement et ce sera au tour 
	 * COULEUR_DEFAUT de jouer.							   ********************/
	
	CouleurPiece COULEUR_DEFAUT = CouleurPiece.BLANC;
	
	Piece[] PLATEAU_DEFAUT_1 = new Piece []	{
																				new Tour(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Dame(CouleurPiece.NOIR), new Roi(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Tour(CouleurPiece.NOIR),
																				new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), null, new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR),
																				null, null, null, null, null, null, null, null, 
																				null, null, null, null, new Cavalier(CouleurPiece.BLANC), null, null, null, 
																				null, null, null, null, null, null, null, null, 
																				null, new Dame(CouleurPiece.BLANC), null, null, null, null, null, null, 
																				new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC),
																				new Tour(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), null, new Roi(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), null, new Tour(CouleurPiece.BLANC)
											};
	
	

	public static final Piece[] PLATEAU_DEFAUT_MANGE = new Piece []	{
																				new Tour(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Dame(CouleurPiece.NOIR), new Roi(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Tour(CouleurPiece.NOIR),
																				null, null, new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR),
																				null, null, null, null, null, null, null, null, 
																				null, new Pion(CouleurPiece.NOIR), null, null, null, null, null, null, 
																				null, null, new Pion(CouleurPiece.NOIR), null, null, null, null, null, 
																				null, null, new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), null, null, null, null, 
																				new Pion(CouleurPiece.BLANC), null, null, new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC),
																				new Tour(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), new Dame(CouleurPiece.BLANC), new Roi(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Tour(CouleurPiece.BLANC)
																};
	public static final Piece[] PLATEAU_DEFAUT = new Piece [] {
																				new Tour(CouleurPiece.NOIR), null, new Fou(CouleurPiece.NOIR), new Dame(CouleurPiece.NOIR), new Roi(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Tour(CouleurPiece.NOIR),
																				new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), null, new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR),
																				null, null, new Cavalier(CouleurPiece.NOIR), null, null, null, null, null, 
																				null, null, null, new Pion(CouleurPiece.NOIR), null, null, null, null, 
																				null, null, null, null, new Pion(CouleurPiece.BLANC), null, null, null, 
																				null, null, new Cavalier(CouleurPiece.BLANC), null, null, new Cavalier(CouleurPiece.BLANC), null, null, 
																				new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), null, new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC),
																				new Tour(CouleurPiece.BLANC), null, new Fou(CouleurPiece.BLANC), new Dame(CouleurPiece.BLANC), new Roi(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), null, new Tour(CouleurPiece.BLANC)
															};
	public static final Piece[] PLATEAU_DEFAUT_DEFAULT = new Piece [] {
																				new Tour(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Dame(CouleurPiece.NOIR), new Roi(CouleurPiece.NOIR), new Fou(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Tour(CouleurPiece.NOIR),
																				new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR),
																				null, null, null, null, null, null, null, null, 
																				null, null, null, null, null, null, null, null, 
																				null, null, null, null, null, null, null, null, 
																				null, null, null, null, null, null, null, null, 
																				new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC),
																				new Tour(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), new Dame(CouleurPiece.BLANC), new Roi(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Tour(CouleurPiece.BLANC)
														};
	public static final Piece[] PLATEAU_OTHER = new Piece [] {
																				new Tour(CouleurPiece.NOIR), null, null, new Roi(CouleurPiece.NOIR), null, new Fou(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), new Tour(CouleurPiece.NOIR),
																				new Pion(CouleurPiece.NOIR), null, new Pion(CouleurPiece.NOIR), new Dame(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR),
																				new Fou(CouleurPiece.NOIR), new Pion(CouleurPiece.NOIR), new Cavalier(CouleurPiece.NOIR), null, null, null, null, null, 
																				null, null, null, new Dame(CouleurPiece.BLANC), null, null, null, null, 
																				null, null, null, null, new Pion(CouleurPiece.BLANC), null, null, null, 
																				null, null, null, null, null, new Cavalier(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), 
																				new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), null, new Pion(CouleurPiece.BLANC), new Pion(CouleurPiece.BLANC), null, new Fou(CouleurPiece.BLANC),
																				new Tour(CouleurPiece.BLANC), new Cavalier(CouleurPiece.BLANC), new Fou(CouleurPiece.BLANC), null, new Roi(CouleurPiece.BLANC), null, null, new Tour(CouleurPiece.BLANC)
															};
	
	public static String START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	public static String C50 = "r1bqk1nr/ppppbppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 5 4";
}
