package application.view;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResultsViewController {

	@FXML
	ImageView resultImage = new ImageView();
	Image winImage = new Image("WinScreen.png");
	Image loseImage = new Image("LoseScreen.png");
	

	public void setComponents(String result){
		if(result.equals("win")){
			resultImage.setImage(winImage);
		}
		else{
			resultImage.setImage(loseImage);
		}
	}
	
}
