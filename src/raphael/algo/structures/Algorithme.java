package raphael.algo.structures;

import raphael.algo.AlgorithmeException;
import raphael.algo.AlgorithmeException.NoMoreChoicesException;
import raphael.algo.Constantes;

/**
 * Interface décrivant les algos d'exploration utilisés
 */
public abstract class Algorithme {
	/**
	 * Heure de début d'algo lorsqu'on doit prendre en compte le temps.
	 */
	private long start;
	
	/**
	 * Joueur à maximiser
	 */
	private Joueur joueur;
	
	/**
	 * Profondeur max pour la recherche
	 */
	static private int profondeur_max = 1;
	
	/**
	 * Temps maximal en secondes
	 */
	static private int max_time = 10_000;
	
	/**
	 * Réalise quelques opérations et vérifications nécessaires à
	 * l'algo et le lance
	 */
	public Noeud initAndRun(Noeud noeud) throws AlgorithmeException {
		return initAndRun(noeud, profondeur_max, noeud.getTrait());
	}
	
	public Noeud initAndRun(Noeud noeud, int profondeurMax, Joueur joueur) throws AlgorithmeException {
		this.joueur = joueur;
		setProfondeurMax(profondeurMax);
		start = System.currentTimeMillis();
		if (stopCondition(noeud, profondeurMax))
			throw new NoMoreChoicesException();
		
		initRoutine();
		
		Noeud ret = start(noeud);
		
		endRoutine();
		
		return ret;
	}
	
	/**
	 * Fonction qui s'exécute après l'algo pour effectuer des
	 * éventuelles tâches de nettoyage ou autres.
	 */
	public void endRoutine() { }
	
	/**
	 * Fonction qui s'exécute après l'algo pour effectuer des
	 * éventuelles tâches de nettoyage ou autres.
	 */
	public void initRoutine() { }
	
	/**
	 * Démarrer la recherche
	 * 
	 * @param noeud		 Noeud racine (état courant du jeu)
	 * 
	 * @return Le meilleur état de jeu trouvé
	 * 
	 * @throws AlgorithmeException Si une erreur est survenue
	 */
	public abstract Noeud start(Noeud noeud) throws AlgorithmeException;
	
	/**
	 * Condition d'arrêt
	 * 
	 * @param noeud		 Le noeud sur lequel faire la vérification
	 * @param profondeur La pronfondeur restante
	 * 
	 * @return true si il faut arrêter, false sinon
	 */
	public boolean stopCondition(Noeud noeud, int profondeur) {
		return profondeur == 0 || noeud.estTerminal();
	}
	
	/**
	 * Vérifie si le temps de départ {@link Algorithme#start} plus le temps
	 * maximal autorisé {@link Constantes#TIMEOUT_MILLIS} est inférieu au 
	 * temps actuel.
	 * 
	 * @return true s'il ne reste plus de temps, false sinon
	 */
	public boolean timesUp() {
		return start + max_time < System.currentTimeMillis();
	}

	/**
	 * Joueur que dont on souhaite maximiser le score
	 * @return le joueur
	 */
	public Joueur getJoueur() {
		return joueur;
	}

	/**
	 * Profondeur maximale de l'arbre de recherche
	 * @return la profondeur maximale
	 */
	public int getProfondeurMax() {
		return profondeur_max;
	}

	/**
	 * Profondeur maximale de l'arbre de recherche
	 * @return la profondeur maximale
	 */
	public static int setProfondeurMax(int nouvelle_profondeur) {
		if(nouvelle_profondeur < Constantes.PROFONDEUR_MIN)
			profondeur_max = Constantes.PROFONDEUR_MIN;
		else if(nouvelle_profondeur > Constantes.PROFONDEUR_MAX)
			profondeur_max = Constantes.PROFONDEUR_MAX;
		else
			profondeur_max = nouvelle_profondeur;
		
		return profondeur_max;
	}

	/**
	 * Profondeur maximale de l'arbre de recherche
	 * @return la profondeur maximale
	 */
	public static int setExecutionTime(int nouveau_temps_max) {
		if(nouveau_temps_max < Constantes.PROFONDEUR_MIN)
			max_time = 1000 * Constantes.EXEC_TIME_MIN;
		else if(nouveau_temps_max > Constantes.PROFONDEUR_MAX)
			max_time = 1000 * Constantes.EXEC_TIME_MAX;
		else
			max_time = 1000 * nouveau_temps_max;
		
		return max_time / 1000;
	}
}
