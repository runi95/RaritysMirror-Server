package javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.view.Editor;
import javafx.view.ServerView;

public class Main extends Application {
	public static void main(String[] args) {
		Main.launch(args);
	}

	private static int width, height;
	private Scene scene;
	private static StackPane root = new StackPane();

	public static StackPane getRoot() {
		return root;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		startValues(primaryStage);
		setStartScene(primaryStage);
	}

	private void startValues(Stage stage) {
		width = 1280;
		height = 768;

		stage.setTitle("Rarity's Mirror - Server");
		// stage.getIcons().addAll(new
		// Image("/server/ui/resources/images/ui/icon.png"), new
		// Image("/server/ui/resources/images/ui/icon.icns"));
	}

	private void setStartScene(Stage stage) {
		root.getChildren().add(new Editor());

		scene = new Scene(root, width, height);
		// scene.getStylesheets().add("javafx/view/fxml/css/style.css");
		stage.setScene(scene);
		stage.show();
	}
}