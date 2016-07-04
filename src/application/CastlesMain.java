package application;
	
import java.io.IOException;

import application.view.MainGameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class CastlesMain extends Application {
	
	private Stage mainStage;
	private BorderPane mainLayout;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		this.mainStage.setTitle("CASTLES");
		initMainLayout();
		//showGameView();
	}
	
	private void showGameView() {
		// TODO Auto-generated method stub
		
	}

	private void initMainLayout() {
		try {
            // Load main layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CastlesMain.class
                    .getResource("view/MainGameView.fxml"));
            mainLayout = (BorderPane) loader.load();

            mainLayout.setStyle("-fx-background-color: #222222");
            // Show the scene containing the root layout.
            Scene scene = new Scene(mainLayout);
            mainStage.setScene(scene);

            // Give the controller access to the main app.
            MainGameViewController controller = loader.getController();
            controller.setMainGame(this);

            mainStage.show();
            controller.bindButtons();
            controller.handleNewGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public Stage getMainStage() {
        return mainStage;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
