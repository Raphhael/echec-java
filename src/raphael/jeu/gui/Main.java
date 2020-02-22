package raphael.jeu.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = new ContainerPane(new Board());
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setTitle("Échec");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
