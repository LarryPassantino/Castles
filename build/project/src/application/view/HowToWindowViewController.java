package application.view;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

public class HowToWindowViewController {
	
	@FXML
	public Label gameOverviewTextArea = new Label();
	@FXML
	public Label basicTurnTextArea = new Label();
	@FXML
	public Label basicVictoryTextArea = new Label();
	@FXML
	public TabPane helpWindowLayout = new TabPane();
	@FXML
	public Label rulesLabel = new Label();
	
	
	public HowToWindowViewController(){
		setComponents();
	}
	
	public void setComponents(){
		String basicTurnText = "The game is played in turns and each turn has the following basic steps:\n" +
				"1. Draw a card.\n2. Play an EVENT if you have one.\n3. End your turn in some way:\n" +
				"\t\ta. Decide to take no action and end your turn.\n\t\tb. Attack your opponent, if possible.\n\t\t" +
				"c. Discard two cards if required and end your turn.";
		String overviewText = "Castles is a game about attacking and defending castles. " +
				"You draw cards that are better at attack, defense or both and use these in an effort to " +
				"take your opponent's castles. Be watchful though, your opponent is trying to do the same. " +
				"Choose the correct army for a successful seige and win the rights to the kingdom!";
		String victoryText = "The name of the game is taking castles and that is how to win. Be the first " +
				"player to eliminate all of your opponent's castles and you will be victorious!";
		
		gameOverviewTextArea.setText(overviewText);
		basicTurnTextArea.setText(basicTurnText);
		basicVictoryTextArea.setText(victoryText);
		
		gameOverviewTextArea.setStyle("-fx-border-color: #B06700;"+"-fx-border-width: 4;"+"-fx-border-radius: 10 10 10 10;"+
				"-fx-background-radius: 10 10 10 10;"+"-fx-font-alignment: center;"+"-fx-background-color: ffeeaa;");
		basicTurnTextArea.setStyle("-fx-border-color: #B06700;"+"-fx-border-width: 4;"+"-fx-border-radius: 10 10 10 10;"+
				"-fx-background-radius: 10 10 10 10;"+"-fx-font-alignment: center;"+"-fx-background-color: ffeeaa;");
		basicVictoryTextArea.setStyle("-fx-border-color: #B06700;"+"-fx-border-width: 4;"+"-fx-border-radius: 10 10 10 10;"+
				"-fx-background-radius: 10 10 10 10;"+"-fx-text-alignment: center;"+"-fx-background-color: ffeeaa;");
		
		rulesLabel.setStyle("-fx-border-color: #B06700;"+"-fx-border-width: 4;"+"-fx-border-radius: 10 10 10 10;"+
				"-fx-background-radius: 10 10 10 10;"+"-fx-background-color: ffeeaa;");
		rulesLabel.setText("The game is played in turns. Each turn goes as follows:\n\t1. Draw a card.\n\t" + 
				"2. If you have any EVENT cards in your hand, you MUST play one now. Only one\n\t    EVENT is played per turn.\n\t" + 
				"3. Decide how to complete your turn...\n\t\ta. If you have at least 4 attacking cards (attack > 0), you may choose to attack.\n\t\t" +
				"b. If you do not have enough attackers or choose not to attack, you just end\n\t\t    your turn.\n\t\t" +
				"c. If you have 8 or more cards at the end of your turn, you can choose to either\n\t\t    attack " +
				"(no matter how many attackers you have) or discard any 2 cards.\n\n" +
				"When an attack occurs, the attacker adds up the attack values of all of their UNITS, along\n with any CHAMPIONS " +
				"and the defender adds up the defense values of all of their UNITS,\n along with any CHAMPIONS.\n\t" +
				"If the attacker's total is higher, the attack succeeds and the defender loses one of\n\t thier CASTLES.\n\t" +
				"If the defender's total is the same of higher, they successfully defend and earn\n\t a CHAMPION.\n\t\t" +
				"A CHAMPION gives +2 to attack AND defense during all combats.\n\t\t" +
				"A player can have at most 2 CHAMPIONS but lose all CHAMPIONS upon losing\n\t\t a combat.\n\t" +
				"Whoever wins the combat gets to go first in the next round.\n\n" +
				"HOW TO WIN:\n\tWhen one player takes all 3 of their opponent's CASTLES, they win the game!");		
	}
	
}
