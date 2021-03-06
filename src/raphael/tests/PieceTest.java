package raphael.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import raphael.jeu.CouleurPiece;
import raphael.jeu.Piece;

class PieceTest {
	@Test
	void testGetZobriestValueInt() {
		assertEquals(0, Piece.getZobriestValue(0b0000000000001__0000__100001__0__0__0__000001));
		assertEquals(3, Piece.getZobriestValue(0b0000000000011__0011__100001__0__0__0__000001));
		assertEquals(9, Piece.getZobriestValue(0b0000000000111__1001__000001__0__0__0__000001));
	}

	@Test
	void testGetEvalValueInt() {
		assertEquals(7, Piece.getEvalValue(0b0000000000111__0001__000001__0__0__0__000001));
		assertEquals(7175, Piece.getEvalValue(0b1110000000111__0000__000001__0__0__0__000001));
	}

	@Test
	void testSetCouleur() {
		assertEquals(				  0b0000000000001__0001__000000__1__0__1__000001,
				 	 Piece.setCouleur(0b0000000000001__0001__000000__1__1__1__000001,
						  		  CouleurPiece.BLANC));
		assertEquals(				  0b0000000000001__0001__000000__0__1__0__000001,
				 	 Piece.setCouleur(0b0000000000001__0001__000000__0__0__0__000001,
						  		  CouleurPiece.NOIR));
	}

	@Test
	void testGetCouleurInt() {
		assertEquals(CouleurPiece.NOIR, Piece.getCouleur(0b0000000000001__0001__000000__0__1__0__000001));
		assertEquals(CouleurPiece.BLANC, Piece.getCouleur(0b0000000000001__0001__000000__1__0__1__000001));
	}

	@Test
	void testGetCharPiece() {
		assertEquals("R", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__000001));
		assertEquals("D", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__000010));
		assertEquals("T", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__000100));
		assertEquals("F", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__001000));
		assertEquals("C", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__010000));
		assertEquals("P", Piece.toSmallString(0b0000000000001__0001__000000__0__1__0__100000));
	}

	@Test
	void testGetABougeBoolean() {
		assertEquals(false, Piece.getABouge(0b0000000000001__0001__000001__0__1__0__000001));
		assertEquals(true, Piece.getABouge(0b0000000000001__0001__000000__1__0__1__000001));
	}

	@Test
	void testSystemOutPiece() {
		assertEquals("557185\n" + 
				"10001000000010000001\n" + 
				"Couleur : NOIR\n" + 
				"Valeur : 1\n" + 
				"Zobriest : 1\n" 
				, Piece.toString(0b0000000000001__0001__000000__0__1__0__000001));
	}

	@Test
	void testToFENStringPiece() {
		assertEquals("K", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__000001));
		assertEquals("Q", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__000010));
		assertEquals("R", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__000100));
		assertEquals("B", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__001000));
		assertEquals("N", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__010000));
		assertEquals("P", Piece.toFENString(0b0000000000001__0001__000000__0__0__0__100000));
		assertEquals("k", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__000001));
		assertEquals("q", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__000010));
		assertEquals("r", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__000100));
		assertEquals("b", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__001000));
		assertEquals("n", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__010000));
		assertEquals("p", Piece.toFENString(0b0000000000001__0001__000000__0__1__0__100000));
	}
}
