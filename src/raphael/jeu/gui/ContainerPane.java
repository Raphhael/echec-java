package raphael.jeu.gui;

import javafx.geometry.Orientation;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class ContainerPane extends BorderPane {
	public static final double ZOOM = 1.0;
	private Board board;
	
	public ContainerPane(Board board) {
		this.board = board;
		setCenter(board);
		setRight(new RightPane());
	}
	
	public class RightPane extends FlowPane {
		public RightPane() {
			super(Orientation.HORIZONTAL);
			getChildren().addAll(
				new IAPanel(board),
				new GameEditorPane(board)
			);
		}
	}
}
