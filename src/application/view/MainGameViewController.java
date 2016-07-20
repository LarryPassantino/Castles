package application.view;

import java.util.ArrayList;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
	public TilePane aiCardDisplay = new TilePane();
	@FXML
	public TilePane playerCardDisplay = new TilePane();
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
	public Button playEventButton;
	@FXML
	public Button discardButton;
	@FXML
	public Button helpButton = new Button();
	@FXML
	public Label message = new Label("");
	@FXML
	public ImageView castleP1 = new ImageView();
	@FXML
	public ImageView castleP2 = new ImageView();
	@FXML
	public ImageView castleP3 = new ImageView();
	@FXML
	public ImageView castleA1 = new ImageView();
	@FXML
	public ImageView castleA2 = new ImageView();
	@FXML
	public ImageView castleA3 = new ImageView();
	@FXML
	public ImageView championP1 = new ImageView();
	@FXML
	public ImageView championP2 = new ImageView();
	@FXML
	public ImageView championA1 = new ImageView();
	@FXML
	public ImageView championA2 = new ImageView();
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
	private boolean aiEventPlayed = false;
	private int messageLifeCounter = 0;
	private int discardCounter = 0;
	private boolean skipToEndTurn = false;
	
	private SimpleBooleanProperty isPlayerTurn = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty isGameOver = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty maxAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canDraw = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty needNextRound = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canEndTurn = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty eventCardPlayed = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty hasEventCard = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canDiscard = new SimpleBooleanProperty(false);

	public MainGameViewController(){
		
	}
	
	public void setComponents(){
		aiHand = new Hand(aiCardDisplay.getChildren());
        playerHand = new Hand(playerCardDisplay.getChildren());
        helpButton.setStyle("-fx-background-color: #dfc370;"+"-fx-border-radius: 10;"+"-fx-background-radius: 10;");
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
        BooleanBinding eventBinding = hasEventCard.not().or(eventCardPlayed);
        playEventButton.disableProperty().bind(eventBinding);
        discardButton.disableProperty().bind(canDiscard.not());
	}
	
	@FXML
	public void handleEndTurn(){
		messageLifeCounter++;
		if(messageLifeCounter == 3){
			messageLifeCounter=0;
			message.setText("");
		}
		canEndTurn.set(false);
		eventCardPlayed.set(false);
		hasEventCard.set(false);
		if(isPlayerTurn.get()){
			if(playerHand.handSizeProperty().get() > 7){
				message.setText("TOO MANY CARDS. DISCARD TWO OR ATTACK NOW.\nIF DISCARDING, SELECT YOUR FIRST CARD TO DISCARD.");
				canDiscard.set(true);
				canAttack.set(true);
				canEndTurn.set(false);
			}
			else{
				isPlayerTurn.set(false);
				canDraw.set(false);
				handleDraw();
				///////////////////////////////////////////////////do events for computer's turn
				if (checkForEvent() || !eventCardPlayed.get()) {
					hasEventCard.set(true);
					Card eventCard = getPreferredEventCard();
					eventCard.selectable = true;
					eventCard.isCardSelected = true;
					handlePlayEvent();
					if(eventCard.resultCode.equals("newC") && aiHand.handSizeProperty().get() > 0){
						Card tradeCard = selectCardToTradeIn();
						tradeCard.isCardSelected = true;
						handlePlayEvent();
					}
					aiEventPlayed = true;
				}
				////////////////////////////////////////////////////////////////////////////////
				setDeckText();
				if(aiHand.numAttackersProperty().get() > 3){
					canAttack.set(true);
				}
				else{
					canAttack.set(false);
				}
			}
			if(!isPlayerTurn.get()){
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
					skipToEndTurn = true;
				}
			}
			if(skipToEndTurn){
				skipToEndTurn = false;
				handleEndTurn();
			}
		}
		else{
			if(aiHand.handSizeProperty().get() > 7){
				if(aiHand.numAttackersProperty().get() > 6 && aiHand.attackValueProperty().get() > 20){
					handleAttack();
				}
				else if(aiHand.numAttackersProperty().get() > 4 && aiHand.attackValueProperty().get() > 12){
					handleAttack();
				}
				else if(aiHand.numAttackersProperty().get() == 4 && aiHand.attackValueProperty().get() > 10){
					handleAttack();
				}
				else{
					handleMaxDiscard();
					handleMaxDiscard();
					message.setText("YOUR OPPONENT DISCARDED TWO CARDS INSTEAD OF BEING FORCED TO ATTACK.");
					messageLifeCounter = 1;
					skipToEndTurn = true;
				}
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
			if(skipToEndTurn){
				skipToEndTurn = false;
				handleEndTurn();
			}
		}
	}
	
	@FXML
	public void handleAttack(){
		canEndTurn.set(false);
		canAttack.set(false);
		eventCardPlayed.set(false);
		hasEventCard.set(false);
		playerHandList = playerHand.getHandList();
		aiHandList = aiHand.getHandList();
		for (Card card : playerHandList) {
			card.selectable = false;
			card.isCardSelected = false;
		}
		discardHands(playerHandList);
		discardHands(aiHandList);
		if(isPlayerTurn.get()){
			playerHand.highlightAttackers();
			aiHand.highlightDefenders();
			attackerPower = playerHand.attackValueProperty().get() + numPlayerChampions*2;
			defenderPower = aiHand.defenseValueProperty().get() + numAIChampions*2;
			if(attackerPower > defenderPower){
				roundWinner = "player";
				removeCastle("ai");
				removeChampions("ai");
				if(!isGameOver.get()){
					message.setText("YOU WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\nYOUR OPPONENT LOSES A CASTLE\n\nYOUR TURN");
					needNextRound.set(true);
				}
				else{
					message.setText("YOU WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\n\nCongratulations!\nYou are the winner!!!");
				}
			}
			else{
				roundWinner = "ai";
				addChampion("ai");
				removeChampions("player");
				String champText = "";
				if(!championA2.isVisible()){
					champText = "\nYOUR OPPONENT EARNS A CHAMPION";
				}
				message.setText("YOUR OPPONENT SUCESSFULLY DEFENDED\n" + Math.max(attackerPower, defenderPower) +
						"  -  " + Math.min(attackerPower, defenderPower) + champText + "\n\nOPPONENT'S TURN");
				needNextRound.set(true);
			}
		}
		else{
			aiHand.highlightAttackers();
			playerHand.highlightDefenders();
			attackerPower = aiHand.attackValueProperty().get() + numAIChampions*2;
			defenderPower = playerHand.defenseValueProperty().get() + numPlayerChampions*2;
			if(attackerPower > defenderPower){
				removeCastle("player");
				roundWinner = "ai";
				removeChampions("player");
				if(!isGameOver.get()){
					message.setText("YOUR OPPONENT WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\nYOU LOSE A CASTLE\n\nOPPONENT'S TURN");
					needNextRound.set(true);
				}
				else{
					message.setText("YOUR OPPONENT WON THAT ATTACK\n" + Math.max(attackerPower, defenderPower) +
							"  -  " + Math.min(attackerPower, defenderPower) + "\n\nSorry, you lost this game.\nTry again.");
				}
			}
			else{
				roundWinner = "player";
				addChampion("player");
				removeChampions("ai");
				String champText = "";
				if(!championP2.isVisible()){
					champText = "\nYOU EARN A CHAMPION";
				}
				message.setText("YOU SUCESSFULLY DEFENDED\n" + Math.max(attackerPower, defenderPower) +
						"  -  " + Math.min(attackerPower, defenderPower) + champText + "\n\nYOUR TURN");
				needNextRound.set(true);
			}
		}
	}
	
	@FXML
	public void handleMaxDiscard(){
		discardCounter++;
		canAttack.set(false);
		if(discardCounter == 1){
			message.setText("NOW SELECT YOUR SECOND CARD TO DISCARD.");
		}
		else if(discardCounter > 1){
			canDiscard.set(false);
			discardCounter = 0;
			canEndTurn.set(true);
		}
		if(isPlayerTurn.get()){
			ArrayList<Card> tempHandList = playerHand.getHandList();
			for (Card card : tempHandList) {
				if(card.isCardSelected){
					discardCard(card);
					discardDeck.addDiscardedCards(card);
					setDeckText();
					//break;
				}
			}
		}
		else{
			Card card = selectCardToTradeIn();
			discardCard(card);
			discardDeck.addDiscardedCards(card);
		}
	}
	
	@FXML
	public void handleNewGame(){
		isPlayerTurn.set(true);
		canDraw.set(true);
		maxAttack.set(false);
		needNextRound.set(false);
		hasEventCard.set(false);
		canAttack.set(false);
		eventCardPlayed.set(false);
		roundWinner = "";
		discardDeck.clearDeck();
		drawDeck.refill();
		playerHand.reset();
		aiHand.reset();
		aiHand = new Hand(aiCardDisplay.getChildren());
        playerHand = new Hand(playerCardDisplay.getChildren());
		resetScoreArea();
		numAIChampions = 0;
		numPlayerChampions = 0;
		playerHand.takeCard(drawDeck.drawCard(true));
		playerHand.takeCard(drawDeck.drawCard(true));
		aiHand.takeCard(drawDeck.drawCard(false));
		aiHand.takeCard(drawDeck.drawCard(false));
		setDeckText();
		isGameOver.set(false);
		setDeckText();
		message.setText("YOU START THE GAME\nDRAW A CARD");
	}
	
	public void handleDraw(){
		if(isPlayerTurn.get()){
			playerHand.takeCard(drawDeck.drawCard(true));
			if (!checkForEvent() || eventCardPlayed.get()) {
				canEndTurn.set(true);
			}
			else{
				message.setText("You have at least one event and must play one this turn.");
				hasEventCard.set(true);
				canAttack.set(false);
			}
			canDraw.set(false);
			if(playerHand.numAttackersProperty().get() > 3){
				if (!checkForEvent() || eventCardPlayed.get()) {
					canAttack.set(true);
				}
				else{
					message.setText("You have at least one event and must play one this turn.");
				}
			}
		}
		else{
			aiHand.takeCard(drawDeck.drawCard(false));
		}
	}
	
	@FXML
	public void handlePlayEvent(){
		if (playEventButton.getText().equals("PLAY EVENT")) {
			ArrayList<Card> tempHandList = new ArrayList<Card>();
			if (isPlayerTurn.get()) {
				tempHandList = playerHand.getHandList();
			} else {
				tempHandList = aiHand.getHandList();
			}
			for (Card card : tempHandList) {
				if (card.isCardSelected && card.type.equals("event")) {
					if (!isPlayerTurn.get()) {
						card.highlightCardOnClick(true);
					}
					//discard 1
					if (card.resultCode.equals("dis1")) {
						discardCard(card);
						discardDeck.addDiscardedCards(card);
						Card tempCard;
						if(isPlayerTurn.get()){
							if(playerHand.handSizeProperty().get() > 0){
								tempCard = playerHand.getRandomCard();
								playerHand.removeCard(tempCard);
								discardDeck.addDiscardedCards(tempCard);
								message.setText("YOU DISCARDED A RANDOM CARD.");
							}
							else{
								message.setText("YOU HAVE NO CARDS TO DISCARD.");
							}
						}
						else{
							if(playerHand.handSizeProperty().get() > 0){
								tempCard = aiHand.getRandomCard();
								aiHand.removeCard(tempCard);
								discardDeck.addDiscardedCards(tempCard);
								message.setText("YOUR OPPONENT DISCARDED A RANDOM CARD.");
							}
							else{
								message.setText("YOUR OPPONENT HAD NO CARDS TO DISCARD.");
							}
						}
						setDeckText();
						messageLifeCounter=1;
					} 
					//each discard 1
					else if (card.resultCode.equals("bothdis1")) {
						discardCard(card);
						discardDeck.addDiscardedCards(card);
						if (playerHand.handSizeProperty().get() > 0) {
							Card plCard = playerHand.getRandomCard();
							playerHand.removeCard(plCard);
							discardDeck.addDiscardedCards(plCard);
							
						}
						if (aiHand.handSizeProperty().get() > 0) {
							Card aiCard = aiHand.getRandomCard();
							aiHand.removeCard(aiCard);
							discardDeck.addDiscardedCards(aiCard);
						}
						setDeckText();
						message.setText("EACH PLAYER DISCARDED A RANDOM CARD.");
						messageLifeCounter=1;
					}
					//draw 2
					else if (card.resultCode.equals("draw2")) {
						discardCard(card);
						if(isPlayerTurn.get()){
							playerHand.takeCard(drawDeck.drawCard(true));
							playerHand.takeCard(drawDeck.drawCard(true));
							message.setText("YOU DREW TWO NEW CARDS.");
						}
						else{
							aiHand.takeCard(drawDeck.drawCard(false));
							aiHand.takeCard(drawDeck.drawCard(false));
							message.setText("YOUR OPPONENT DREW TWO NEW CARDS.");
						}
						messageLifeCounter=1;
					} 
					//get new card
					else if (card.resultCode.equals("newC")) {
						discardCard(card);
						discardDeck.addDiscardedCards(card);
						if(isPlayerTurn.get() && playerHand.handSizeProperty().get() > 0){
							message.setText("SELECT A CARD TO TRADE IN FOR A NEW CARD.");
							playEventButton.setText("TRADE");
						}
						else if(!isPlayerTurn.get() && aiHand.handSizeProperty().get() > 0){
							message.setText("SELECT A CARD TO TRADE IN FOR A NEW CARD.");
							playEventButton.setText("TRADE");
						}
						else{
							message.setText("NO CARDS TO TADE IN.");
						}
					} 
					//get new hand
					else if (card.resultCode.equals("newH")) {
						discardCard(card);
						if(isPlayerTurn.get()){
							playerHandList = playerHand.getHandList();
							int cardsInHand = playerHand.handSizeProperty().get();
							for (Card handCard : playerHandList) {
								playerHand.removeCard(handCard);
							}
							playerHandList.clear();
							playerHand = new Hand(playerCardDisplay.getChildren());
							if(cardsInHand>0){
								for(int i=0; i<cardsInHand; i++){
						        	playerHand.takeCard(drawDeck.drawCard(true));
						        }
							}
							else{
								playerHand.takeCard(drawDeck.drawCard(true));
							}
							message.setText("YOUR HAND WAS DISCARDED AND NEW CARDS DRAWN.");
						}
						else{
							aiHandList = aiHand.getHandList();
							int cardsInHand = aiHand.handSizeProperty().get();
							for (Card handCard : aiHandList) {
								aiHand.removeCard(handCard);
							}
							aiHandList.clear();
							aiHand = new Hand(aiCardDisplay.getChildren());
							if(cardsInHand>0){
								for(int i=0; i<cardsInHand; i++){
						        	aiHand.takeCard(drawDeck.drawCard(false));
						        }
							}
							else{
								aiHand.takeCard(drawDeck.drawCard(false));
							}
							message.setText("YOUR OPPONENT'S HAND WAS DISCARDED AND NEW CARDS DRAWN.");
						}
						messageLifeCounter=1;
					}
					//trade 1 with opponent//////////////////////////////////////////////////////////////////////////////////////////
					else if (card.resultCode.equals("trade1")) {
						discardCard(card);
						Card playerCard = playerHand.getRandomCard();
						Card aiCard = aiHand.getRandomCard();
						playerCard.selectable = false;
						playerCard.displayBack();
						aiCard.selectable = true;
						aiCard.displayFront();
						playerHand.removeCard(playerCard);
						playerHand.takeCard(aiCard);
						aiHand.removeCard(aiCard);
						aiHand.takeCard(playerCard);
						message.setText("YOU AND YOUR OPPONENT TRADED A RANDOM CARD.");
						messageLifeCounter=1;
					} 
					//opponent discard 1
					else if (card.resultCode.equals("oppdis1")) {
						discardCard(card);
						discardDeck.addDiscardedCards(card);
						if(isPlayerTurn.get()){
							if (aiHand.handSizeProperty().get() > 0) {
								aiHand.removeCard(aiHand.getRandomCard());
								message.setText("YOUR OPPONENT DISCARDED A RANDOM CARD.");
							}
							else{
								message.setText("YOUR OPPONENT HAD NO CARDS TO DISCARD.");
							}
						}
						else{
							if (playerHand.handSizeProperty().get() > 0) {
								playerHand.removeCard(playerHand
										.getRandomCard());
								message.setText("YOUR OPPONENT MADE YOU DISCARD A RANDOM CARD.");
							}
							else{
								message.setText("YOUR OPPONENT TRIED TO MAKE YOU DISCARD, BUT YOU HAVE NO CARDS.");
							}
						}
						messageLifeCounter=1;
					}
					if(!playEventButton.getText().equals("TRADE")){
						eventCardPlayed.set(true);
						hasEventCard.set(false);
						canEndTurn.set(true);
						if((isPlayerTurn.get() && playerHand.numAttackersProperty().get() > 3) ||
								(!isPlayerTurn.get() && aiHand.numAttackersProperty().get() > 3)){
							canAttack.set(true);
						}
					}
				}
			}
		}
		else{
			ArrayList<Card> tempHandList = new ArrayList<Card>();
			if (isPlayerTurn.get()) {
				tempHandList = playerHand.getHandList();
			} else {
				tempHandList = aiHand.getHandList();
			}
			for (Card card : tempHandList) {
				if (card.isCardSelected) {
					if(isPlayerTurn.get()){
						card.highlightCardOnClick(true);
						playerHand.removeCard(card);
						playerHand.takeCard(drawDeck.drawCard(true));
						message.setText("YOU TRADED A CHOSEN CARD FOR A NEW CARD.");
					}
					else{
						aiHand.removeCard(card);
						aiHand.takeCard(drawDeck.drawCard(false));
						message.setText("YOUR OPPONENT TRADED A CHOSEN CARD FOR A NEW CARD.");
					}
					messageLifeCounter=1;
					eventCardPlayed.set(true);
					hasEventCard.set(false);
					canEndTurn.set(true);
					playEventButton.setText("PLAY EVENT");
					if((isPlayerTurn.get() && playerHand.numAttackersProperty().get() > 3) ||
							(!isPlayerTurn.get() && aiHand.numAttackersProperty().get() > 3)){
						canAttack.set(true);
					}
				}
			}
		}
	}
	
	
	public void handleNextRound(){
		messageLifeCounter=1;
		needNextRound.set(false);
		maxAttack.set(false);
		playerHand.reset();
		aiHand.reset();
		playerHandList.clear();
		aiHandList.clear();
		aiHand = new Hand(aiCardDisplay.getChildren());
        playerHand = new Hand(playerCardDisplay.getChildren());
        playerHand.takeCard(drawDeck.drawCard(true));
		playerHand.takeCard(drawDeck.drawCard(true));
		aiHand.takeCard(drawDeck.drawCard(false));
		aiHand.takeCard(drawDeck.drawCard(false));
		attackerPower = 0;
		defenderPower = 0;
		
		if(roundWinner.equals("player")){
			isPlayerTurn.set(true);
			canDraw.set(true);
			setDeckText();
			roundWinner = "";
			message.setText("PLAYER'S TURN\nDRAW A CARD");
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
		aiHand = new Hand(aiCardDisplay.getChildren());
        playerHand = new Hand(playerCardDisplay.getChildren());
        playerHand.takeCard(drawDeck.drawCard(true));
		playerHand.takeCard(drawDeck.drawCard(true));
		aiHand.takeCard(drawDeck.drawCard(false));
		aiHand.takeCard(drawDeck.drawCard(false));
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
	
	
	public boolean checkForEvent(){
		ArrayList<Card> tempHandList = new ArrayList<Card>();
		if(isPlayerTurn.get()){
			tempHandList = playerHand.getHandList();
		}
		else{
			tempHandList = aiHand.getHandList();
		}
		for (Card card : tempHandList) {
			if(card.type.equals("event")){
				return true;
			}
		}
		return false;
	}
	
	public Card getPreferredEventCard(){
		Card prefEventCard = new Card();
		int priority = 11;
		ArrayList<Card> tempHandList = aiHand.getHandList();
		for (Card card : tempHandList) {
			if(card.type.equals("event")){
				if(card.getEventPlayPriority() < priority){
					prefEventCard = card;
				}
			}
		}
		return prefEventCard;
	}
	
	public Card selectCardToTradeIn(){
		Card tradeCard = new Card();
		ArrayList<Card> tempHandList = aiHand.getHandList();

		//looking for bad events
		for (Card card : tempHandList) {
			boolean eventCardSelected = false;
			if(card.type.equals("event")){
				int priority = 4;
				if(card.getEventPlayPriority() > priority){
					tradeCard = card;
					priority = card.getEventPlayPriority();
					eventCardSelected = true;
				}
				else if(card.getEventPlayPriority() == 4 && priority == 4){
					tradeCard = card;
					eventCardSelected = true;
				}
			}
			if(eventCardSelected){
				return tradeCard;
			}
		}
		
		//this case is if defense is needed
		if((aiHand.numAttackersProperty().get() < aiHand.numDefendersProperty().get()) && 
				(aiHand.attackValueProperty().get() < aiHand.defenseValueProperty().get())){
			
			for (Card card : tempHandList) {
				boolean unitCardSelected = false;
				//if no bad events, looking for worst attacking unit
				if(card.type.equals("unit")){
					int lowestAttack = 11;
					if(card.attack < lowestAttack){
						tradeCard = card;
						unitCardSelected = true;
						lowestAttack = card.attack;
					}
				}
				if(unitCardSelected){
					return tradeCard;
				}
			}
			for (Card card : tempHandList) {
				boolean unitCardSelected = false;
				//if no bad attacking unit, looking for worst defense unit
				if(card.type.equals("unit")){
					int lowestDefense = 11;
					if(card.attack < lowestDefense){
						tradeCard = card;
						unitCardSelected = true;
						lowestDefense = card.defense;
					}
				}
				if(unitCardSelected){
					return tradeCard;
				}
			}
		}
		
		//this case is if attack is needed
		else{
			for (Card card : tempHandList) {
				boolean unitCardSelected = false;
				//if no bad event, looking for worst defense unit
				if(card.type.equals("unit")){
					int lowestDefense = 11;
					if(card.attack < lowestDefense){
						tradeCard = card;
						unitCardSelected = true;
						lowestDefense = card.defense;
					}
				}
				if(unitCardSelected){
					return tradeCard;
				}
			}
			for (Card card : tempHandList) {
				boolean unitCardSelected = false;
				//if no bad defense unit, looking for worst attack unit
				if(card.type.equals("unit")){
					int lowestAttack = 11;
					if(card.attack < lowestAttack){
						tradeCard = card;
						unitCardSelected = true;
						lowestAttack = card.attack;
					}
				}
				if(unitCardSelected){
					return tradeCard;
				}
			}
		}
		return aiHand.getRandomCard();
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
	
	public void discardCard(Card card){
		if(isPlayerTurn.get()){
			playerHand.removeCard(card);
		}
		else{
			aiHand.removeCard(card);
		}
		discardDeck.addDiscardedCards(card);
	}
	
	public void discardHands(ArrayList<Card> handList){
		for (Card card : handList) {
			discardDeck.addDiscardedCards(card);
		}
	}

	public void setDeckText(){
		drawDeckText.setFont(new Font(16.0));
		drawDeckText.setText("DECK:\n" + drawDeck.getDeckSize() + "  CARDS\n\n\nDISCARD:\n" + discardDeck.getDeckSize() + "  CARDS");
	}
	
	public void handleHelp(){
		main.getPopupStage().showAndWait();
		
	}
}
