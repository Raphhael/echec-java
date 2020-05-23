package raphael.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import raphael.algo.AlgorithmeException;
import raphael.algo.structures.Algorithme;
import raphael.jeu.Etat;

abstract class AbstractAlgorithmeTest {
	private Class<? extends Algorithme> algorithme;

	public AbstractAlgorithmeTest(Class<? extends Algorithme> algorithme) {
		this.algorithme = algorithme;
	}

	@ParameterizedTest
	@CsvFileSource(resources = "1_coup_mat.csv")
	void testEchecEtMatDirect(String fen_start, String fen_end) throws AlgorithmeException {
		Etat etat = new Etat(fen_start);
		Etat etatFinal = (Etat) makeClass().initAndRun(etat, 1, etat.getTrait());
		assertEquals(fen_end, etatFinal.getFEN());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "3_coups_mat.csv")
	void testEchecEtMatProf3(String fen_start) throws AlgorithmeException {
		Etat etat = new Etat(fen_start);
		for (int coup = 0; coup < 3; coup++)
			etat = (Etat) makeClass().initAndRun(etat, 3, etat.getTrait());
		assertTrue(etat.echecEtMat());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "5_coups_mat.csv")
	void testEchecEtMatProf5(String fen_start) throws AlgorithmeException {
		Etat etat = new Etat(fen_start);
		for (int coup = 0; coup < 5; coup++)
			etat = (Etat) makeClass().initAndRun(etat, 5, etat.getTrait());
		assertTrue(etat.echecEtMat());
	}

	private Algorithme makeClass() {
		try {
			return algorithme.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			System.err.println("AbstractAlgorithmeTest : " + " Erreur lors de l'instanciation de la classe "
					+ algorithme.getSimpleName() + " (" + e.getMessage() + ")");
		}
		return null;
	}
}
