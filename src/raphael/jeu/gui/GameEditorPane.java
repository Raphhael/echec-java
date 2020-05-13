package raphael.jeu.gui;

import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import raphael.jeu.Etat;

public class GameEditorPane extends FlowPane {

	public GameEditorPane(Board board) {
		super(Orientation.HORIZONTAL);
		getStyleClass().addAll("bordered");

		Button precedent = new Button("Précédent");
		precedent.setOnAction((ActionEvent event) -> {
			board.precedent();
		});

		TextArea fenInput = new TextArea();
		fenInput.setPrefRowCount(3);

		Button apply = new Button("Appliquer FEN");
		apply.setOnAction((ActionEvent event) -> {
			try {
				Etat e = new Etat(fenInput.getText());
				board.setEtat(e);
			} catch (IllegalArgumentException e) {
				fenInput.setText(e.getMessage());
			}
		});

		Button seeFen = new Button("Voir FEN");
		seeFen.setOnAction((ActionEvent event) -> {
			fenInput.setText(board.getEtat().getFEN());
		});

		getChildren().addAll(precedent, apply, seeFen, fenInput);

	}

}
