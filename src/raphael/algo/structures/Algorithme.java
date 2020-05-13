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
	 * Réalise quelques opérations et vérifications nécessaires à
	 * l'algo et le lance
	 */
	public Noeud initAndRun(Noeud noeud, int profondeurMax, Joueur joueur) throws AlgorithmeException {
		this.joueur = joueur;
		start = System.currentTimeMillis();
		if (stopCondition(noeud, profondeurMax))
			throw new NoMoreChoicesException();
		
		Noeud ret = start(noeud, profondeurMax);
		
		endRoutine();
		
		return ret;
	}
	
	/**
	 * Fonction qui s'exécute après l'algo pour effectuer des
	 * éventuelles tâches de nettoyage ou autres.
	 */
	public void endRoutine() { }
	
	/**
	 * Démarrer la recherche
	 * 
	 * @param noeud		 Noeud racine (état courant du jeu)
	 * @param profondeur Profondeur maximale où aller si nécessaire
	 * @param joueur	 Joueur à maximiser (l'IA)
	 * 
	 * @return Le meilleur état de jeu trouvé
	 * 
	 * @throws AlgorithmeException Si une erreur est survenue
	 */
	public abstract Noeud start(Noeud noeud, int profondeur) throws AlgorithmeException;
	
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
		return start + Constantes.TIMEOUT_MILLIS < System.currentTimeMillis();
	}

	/**
	 * 
	 * @return
	 */
	public Joueur getJoueur() {
		return joueur;
	}
}
