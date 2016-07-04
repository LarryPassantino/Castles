package application.view;

import java.util.ArrayList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import application.CastlesMain;
import application.model.Card;
import application.model.Deck;
import application.model.Hand;

public class MainGameViewController {
	
	public CastlesMain main;
	public Hand aiHand = new Hand(null);
	public Hand playerHand = new Hand(null);
	public static Deck drawDeck = new Deck();
	public static Deck discardDeck = new Deck();
	
	@FXML
	public VBox playerScoreArea;
	@FXML
	public VBox aiScoreArea;
	@FXML
	public HBox aiCardArea = new HBox();
	@FXML
	public HBox deckMessageArea;
	@FXML
	public HBox deckDisplayArea = new HBox();
	@FXML
	public HBox playerCardArea = new HBox();
	@FXML
	public Button attackButton;
	@FXML
	public Button endTurnButton;
	@FXML
	public Button newGameButton;
	@FXML
	public Button nextRoundButton;
	@FXML
	public Button drawButton;
	@FXML
	public Label message = new Label("");
	@FXML
	public Rectangle castleP1 = new Rectangle(100,100);
	@FXML
	public Rectangle castleP2 = new Rectangle(100,100);
	@FXML
	public Rectangle castleP3 = new Rectangle(100,100);
	@FXML
	public Rectangle castleA1 = new Rectangle(100,100);
	@FXML
	public Rectangle castleA2 = new Rectangle(100,100);
	@FXML
	public Rectangle castleA3 = new Rectangle(100,100);
	@FXML
	public Polygon championP1 = new Polygon();
	@FXML
	public Polygon championP2 = new Polygon();
	@FXML
	public Polygon championA1 = new Polygon();
	@FXML
	public Polygon championA2 = new Polygon();
	@FXML
	public Rectangle drawDeckBlock = new Rectangle(80,100);
	@FXML
	public Rectangle discardDeckBlock = new Rectangle(80,100);
	@FXML
	public TextArea drawDeckText = new TextArea("");
	@FXML
	public TextArea discardDeckText = new TextArea("");
	
	private int numPlayerChampions, numAIChampions;
	private String attacker;
	private ArrayList<Card> playerHandList = new ArrayList<Card>();
	private ArrayList<Card> aiHandList = new ArrayList<Card>();
	private int attackerPower = 0;
	private int defenderPower = 0;
	private String roundWinner = "";
	
	private SimpleBooleanProperty isPlayerTurn = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty isGameOver = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty maxAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canDraw = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty needNextRound = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canEndTurn = new SimpleBooleanProperty(false);

	public MainGameViewController(){
		aiHand = new Hand(aiCardArea.getChildren());
        playerHand = new Hand(playerCardArea.getChildren());
	}
	
	public void setMainGame(CastlesMain main) {
		this.main = main;
	}
	
	public void bindButtons(){
		attackButton.disableProperty().bind(isGameOver);
		attackButton.disableProperty().bind(canAttack.not());
        endTurnButton.disableProperty().bind(canEndTurn.not());
        newGameButton.disableProperty().bind(isGameOver.not());
        drawButton.disableProperty().bind(canDraw.not());
        nextRoundButton.disableProperty().bind(needNextRound.not());
	}
	
	@FXML
	public void handleEndTurn(){
		canEndTurn.set(false);
		if(isPlayerTurn.get() == true){
			if(playerHand.handSizeProperty().get() > 7){
				handleAttack();
				maxAttack.set(true);
			}
			else{
				isPlayerTurn.set(false);
				canDraw.set(false);
				handleDraw();
				setDeckText();
				if(aiHand.numAttackersProperty().get() > 3){
					canAttack.set(true);
				}
				else{
					canAttack.set(false);
				}
			}
			if(!maxAttack.get()){
				message.setText("COMPUTER'S TURN");
				if(canAttack.get() && aiHand.numAttackersProperty().get() > 6 && aiHand.attackValueProperty().get() > 20){
					handleAttack();
				}
				else if(canAttack.get() && aiHand.numAttackersProperty().get() > 4 && aiHand.attackValueProperty().get() > 12){
					handleAttack();
				}
				else if(canAttack.get() && aiHand.numAttackersProperty().get() == 4 && aiHand.attackValueProperty().get() > 10){
					handleAttack();
				}
				else{
					handleEndTurn();
				}
			}
		}
		else{
			if(aiHand.handSizeProperty().get() > 7){
				handleAttack();
				maxAttack.set(true);
			}
			else{
				isPlayerTurn.set(true);
				canDraw.set(true);
				setDeckText();
				if(playerHand.numAttackersProperty().get() > 3){
					canAttack.set(true);
				}
				else{
					canAttack.set(false);
				}
			}
			if(!maxAttack.get()){
				message.setText("PLAYER'S TURN\nDRAW A CARD");
			}
		}
	}
	
	@FXML
	public void handleAttack(){
		canEndTurn.set(false);
		canAttack.set(false);
		playerHandList = playerHand.getHandList();
		aiHandList = aiHand.getHandList();
		discardHands(playerHandList);
		discardHands(aiHandList);
		if(isPlayerTurn.get()){
			attackerPower = playerHand.attackValueProperty().get() + numPlayerChampions*2;
			defenderPower = aiHand.defenseValueProperty().get() + numAIChampions*2;
			if(attackerPower > defenderPower){
				roundWinner = "player";
				removeCastle("ai");
				removeChampions("ai");
				if(!isGameOver.get()){
					message.setText("PLAYER WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\nCOMPUTER LOSES A CASTLE\n\nPLAYER'S TURN");
					needNextRound.set(true);
				}
				else{
					message.setText("Congratulations!\nYou are the winner!!!");
				}
			}
			else{
				roundWinner = "ai";
				addChampion("ai");
				removeChampions("player");
				message.setText("COMPUTER SUCESSFULLY DEFENDED\n" + Math.max(attackerPower, defenderPower) +
						"  -  " + Math.min(attackerPower, defenderPower) + "\nCOMPUTER EARNS A CHAMPION\n\nCOMPUTER'S TURN");
				needNextRound.set(true);
			}
		}
		else{
			attackerPower = aiHand.attackValueProperty().get() + numAIChampions*2;
			defenderPower = playerHand.defenseValueProperty().get() + numPlayerChampions*2;
			if(attackerPower > defenderPower){
				removeCastle("player");
				roundWinner = "ai";
				removeChampions("player");
				if(!isGameOver.get()){
					message.setText("COMPUTER WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\nPLAYER LOSES A CASTLE\n\nCOMPUTER'S TURN");
					needNextRound.set(true);
				}
				else{
					message.setText("Sorry, the computer won this game.\nTry again.");
				}
			}
			else{
				roundWinner = "player";
				addChampion("player");
				removeChampions("ai");
				message.setText("PLAYER SUCESSFULLY DEFENDED\n" + Math.max(attackerPower, defenderPower) +
						"  -  " + Math.min(attackerPower, defenderPower) + "\nPLAYER EARNS A CHAMPION\n\nPLAYER'S TURN");
				needNextRound.set(true);
			}
		}
	}
	
	@FXML
	public void handleNewGame(){
		isPlayerTurn.set(true);
		canDraw.set(true);
		maxAttack.set(false);
		needNextRound.set(false);
		roundWinner = "";
		discardDeck.clearDeck();
		drawDeck.refill();
		playerHand.reset();
		aiHand.reset();
		aiHand = new Hand(aiCardArea.getChildren());
        playerHand = new Hand(playerCardArea.getChildren());
		resetScoreArea();
		numAIChampions = 0;
		numPlayerChampions = 0;
		playerHand.takeCard(drawDeck.drawCard());
		playerHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		setDeckText();
		isGameOver.set(false);
		setDeckText();
		message.setText("PLAYER'S TURN\nDRAW A CARD");
	}
	
	public void handleDraw(){
		if(isPlayerTurn.get()){
			playerHand.takeCard(drawDeck.drawCard());
			canEndTurn.set(true);
			canDraw.set(false);
			if(playerHand.numAttackersProperty().get() > 3){
				canAttack.set(true);
			}
		}
		else{
			aiHand.takeCard(drawDeck.drawCard());
		}
	}
	
	public void handleNextRound(){
		needNextRound.set(false);
		maxAttack.set(false);
		playerHand.reset();
		aiHand.reset();
		playerHandList.clear();
		aiHandList.clear();
		aiHand = new Hand(aiCardArea.getChildren());
        playerHand = new Hand(playerCardArea.getChildren());
        playerHand.takeCard(drawDeck.drawCard());
		playerHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		attackerPower = 0;
		defenderPower = 0;
		
		if(roundWinner.equals("player")){
			isPlayerTurn.set(true);
			canDraw.set(true);
			setDeckText();
			roundWinner = "";
		}
		else{
			isPlayerTurn.set(false);
			handleDraw();
			setDeckText();
			handleEndTurn();
		}
	}
	
	public void resetScoreArea(){
		castleP1.setVisible(true);
		castleP2.setVisible(true);
		castleP3.setVisible(true);
		castleA1.setVisible(true);
		castleA2.setVisible(true);
		castleA3.setVisible(true);
		championP1.setVisible(false);
		championP2.setVisible(false);
		championA1.setVisible(false);
		championA2.setVisible(false);
	}
	
	public void newHands(String winner){
		maxAttack.set(false);
		playerHand.reset();
		aiHand.reset();
		aiHand = new Hand(aiCardArea.getChildren());
        playerHand = new Hand(playerCardArea.getChildren());
        playerHand.takeCard(drawDeck.drawCard());
		playerHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		aiHand.takeCard(drawDeck.drawCard());
		if(winner.equals("player")){
			isPlayerTurn.set(true);
			canDraw.set(true);
			setDeckText();
			message.setText("PLAYER WON THAT COMBAT.\n" + Math.max(attackerPower, defenderPower) +
					"  -  " + Math.min(attackerPower, defenderPower) + "\n\nPLAYER'S TURN");
			attackerPower = 0;
			defenderPower = 0;
		}
		else{
			isPlayerTurn.set(false);
			handleDraw();
			setDeckText();
			message.setText("AI WON THAT COMBAT.\n" + Math.max(attackerPower, defenderPower) +
					"  -  " + Math.min(attackerPower, defenderPower) + "\n\nAI'S TURN");
			attackerPower = 0;
			defenderPower = 0;
			handleEndTurn();
		}
	}
	
	public void removeCastle(String who){
		if(who.equals("player")){
			if(castleP3.isVisible()){
				castleP3.setVisible(false);
			}
			else if(!castleP3.isVisible() && castleP2.isVisible()){
				castleP2.setVisible(false);
			}
			else{
				castleP1.setVisible(false);
				isGameOver.set(true);
			}
		}
		else{
			if(castleA3.isVisible()){
				castleA3.setVisible(false);
			}
			else if(!castleA3.isVisible() && castleA2.isVisible()){
				castleA2.setVisible(false);
			}
			else{
				castleA1.setVisible(false);
				isGameOver.set(true);
			}
		}
	}
	
	public void addChampion(String who){
		if(who.equals("player")){
			if(championP1.isVisible()){
				championP2.setVisible(true);
				numPlayerChampions = 2;
			}
			else{
				championP1.setVisible(true);
				numPlayerChampions = 1;
			}
		}
		else{
			if(championA1.isVisible()){
				championA2.setVisible(true);
				numAIChampions = 2;
			}
			else{
				championA1.setVisible(true);
				numAIChampions = 1;
			}
		}
	}
	
	public void removeChampions(String who){
		if(who.equals("player")){
			championP1.setVisible(false);
			championP2.setVisible(false);
			numPlayerChampions = 0;
		}
		else{
			championA1.setVisible(false);
			championA2.setVisible(false);
			numAIChampions = 0;
		}
	}
	
	public void endGame(String winner){
		isGameOver.set(true);
		if(winner.equals("player")){
			message.setText("Congratulations!\nYou are the winner!!!");
		}
		else{
			message.setText("Sorry, the computer won this game.\nTry again.");
		}
	}
	
	public void discardHands(ArrayList<Card> handList){
		for (Card card : handList) {
			discardDeck.addDiscardedCards(card);
		}
	}

	public void setDeckText(){
		drawDeckText.setText("CARDS IN DECK:\n\n\n" + drawDeck.getDeckSize());
		discardDeckText.setText("CARDS IN DISCARD:\n\n\n" + discardDeck.getDeckSize());
	}
}
