package raphael.jeu.gui;

import javafx.scene.layout.BorderPane;

public class ContainerPane extends BorderPane {
	public static final double ZOOM = 1.0; 	// taille des cases

	public ContainerPane(Board board) {
		super(board);
		setBottom(new IAPanel(board));
	}
}
