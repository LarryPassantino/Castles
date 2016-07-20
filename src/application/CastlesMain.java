package application;
	
import java.io.IOException;

import application.view.HowToWindowViewController;
import application.view.MainGameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class CastlesMain extends Application {
	
	private Stage mainStage, popupStage;
	private BorderPane mainLayout, popupLayout;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		this.mainStage.setTitle("CASTLE SIEGE");
		initMainLayout();
		//showGameView();
		initPopup();
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
            controller.setComponents();
            controller.bindButtons();
            controller.handleNewGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void initPopup() {
		popupStage = new Stage();
		//tell stage it is meant to pop-up (Modal)
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("HOW TO PLAY");
		// Load popup layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CastlesMain.class
                .getResource("view/HowToWindowView.fxml"));
        try {
			popupLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        popupLayout.setStyle("-fx-background-color: #B8E4FF");
        // Show the scene containing the root layout.
        Scene scene = new Scene(popupLayout);
        popupStage.setScene(scene);

        // Give the controller access to the main app.
        HowToWindowViewController controller = loader.getController();
        controller.setComponents();
	}
	
	public Stage getMainStage() {
        return mainStage;
    }

	public Stage getPopupStage() {
        return popupStage;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
