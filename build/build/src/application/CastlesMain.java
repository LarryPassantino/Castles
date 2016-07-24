package application;
	
import java.io.IOException;

import application.view.HowToWindowViewController;
import application.view.MainGameViewController;
import application.view.ResultsViewController;
import application.view.TitleScreenViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class CastlesMain extends Application {
	
	private Stage mainStage, popupStage, titleStage, resultStage;
	private BorderPane mainLayout, popupLayout;
	private AnchorPane titleLayout, resultLayout;
	MainGameViewController gameController;
	ResultsViewController resultsController;
	
	
	@Override
	public void start(Stage primaryStage) {
		this.mainStage = primaryStage;
		this.mainStage.setTitle("CASTLES");
		initMainLayout();
		//showGameView();
		initTitleScreen();
		initPopup();
		initResultScreen();
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
            ////////////////////////////////////////////////////////////////mainStage.setResizable(false);
            mainStage.sizeToScene();

            // Give the controller access to the main app.
            MainGameViewController controller = loader.getController();
            controller.setMainGame(this);
            gameController = controller;

            /*mainStage.show();
            controller.setComponents();
            controller.bindButtons();
            controller.handleNewGame();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void initTitleScreen() {
		try {
			titleStage = new Stage();
			titleStage.setTitle("CASTLES");
            // Load title layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CastlesMain.class
                    .getResource("view/TitleScreenView.fxml"));
            titleLayout = (AnchorPane) loader.load();
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(titleLayout);
            titleStage.setScene(scene);
            ////////////////////////////////////////////////////////////////titleStage.setResizable(false);
            titleStage.sizeToScene();

            // Give the controller access to the main app.
            TitleScreenViewController controller = loader.getController();
            controller.setMainGame(this);

            titleStage.show();
            controller.setComponents();
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
        ////////////////////////////////////////////////////////////////popupStage.setResizable(false);
        popupStage.sizeToScene();

        // Give the controller access to the main app.
        HowToWindowViewController controller = loader.getController();
        controller.setComponents();
	}
	
	private void initResultScreen() {
		resultStage = new Stage();
		//tell stage it is meant to pop-up (Modal)
		resultStage.initModality(Modality.APPLICATION_MODAL);
		resultStage.setTitle("GAME OVER");
		// Load popup layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CastlesMain.class
                .getResource("view/ResultsView.fxml"));
        try {
			resultLayout = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //resultLayout.setStyle("-fx-background-color: #B8E4FF");
        // Show the scene containing the root layout.
        Scene scene = new Scene(resultLayout);
        resultStage.setScene(scene);
        resultStage.setResizable(false);
        resultStage.sizeToScene();

        // Give the controller access to the main app.
        ResultsViewController controller = loader.getController();
        resultsController = controller;
        //controller.setComponents();
	}
	
	public void startGame(){
		mainStage.show();
        gameController.setComponents();
        gameController.bindButtons();
        gameController.handleNewGame();
	}
	
	public Stage getMainStage() {
        return mainStage;
    }

	public Stage getPopupStage() {
        return popupStage;
    }
	
	public Stage getTitleStage(){
		return titleStage;
	}
	
	public Stage getResultsStage(String result){
		resultsController.setComponents(result);
		return resultStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
