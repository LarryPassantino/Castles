package application.view;

import application.CastlesMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;



public class TitleScreenViewController {
	
	@FXML
	Button startGameButton = new Button();
	@FXML
	Button startGameWithHelpButton = new Button();
	public CastlesMain main;
	
	public TitleScreenViewController(){
		setComponents();
	}
	
	public void setComponents() {
		startGameButton.setStyle("-fx-background-color: #ffeeaa;"+"-fx-border-radius: 10;"+"-fx-background-radius: 10;");
		startGameWithHelpButton.setStyle("-fx-background-color: #ffeeaa;"+"-fx-border-radius: 10;"+"-fx-background-radius: 10;");
		
	}

	public void setMainGame(CastlesMain main) {
		this.main = main;
	}
	
	public void handleStartGame(){
		main.getTitleStage().close();
		main.getMainStage().show();
		main.startGame();
	}
	
	public void handleStartGameWithHelp(){
		main.getTitleStage().close();
		main.getMainStage().show();
		main.startGame();
		main.getPopupStage().showAndWait();
	}

}
