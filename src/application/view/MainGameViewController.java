package application.view;

import java.util.ArrayList;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
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
	
	private SimpleBooleanProperty isPlayerTurn = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty isGameOver = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty maxAttack = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canDraw = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty needNextRound = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty canEndTurn = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty eventCardPlayed = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty hasEventCard = new SimpleBooleanProperty(false);

	public MainGameViewController(){
		aiHand = new Hand(aiCardDisplay.getChildren());
        playerHand = new Hand(playerCardDisplay.getChildren());
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
	}
	
	@FXML
	public void handleEndTurn(){
		canEndTurn.set(false);
		eventCardPlayed.set(false);
		hasEventCard.set(false);
		if(isPlayerTurn.get() == true){
			if(playerHand.handSizeProperty().get() > 7){
				handleAttack();
				maxAttack.set(true);
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
					if(eventCard.resultCode.equals("newC")){
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
				/*if(aiEventPlayed){
					message.setText(message.getText() + "\n\nPLAYER'S TURN\nDRAW A CARD");
					aiEventPlayed = false;
				}
				else{
					message.setText("PLAYER'S TURN\nDRAW A CARD");
				}*/
				message.setText("PLAYER'S TURN\nDRAW A CARD");
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
			aiHand.highlightAttackers();
			playerHand.highlightDefenders();
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
		message.setText("PLAYER'S TURN\nDRAW A CARD");
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
							tempCard = playerHand.getRandomCard();
							playerHand.removeCard(tempCard);
							discardDeck.addDiscardedCards(tempCard);
							message.setText("YOU DISCARDED A RANDOM CARD.");
						}
						else{
							tempCard = aiHand.getRandomCard();
							aiHand.removeCard(tempCard);
							discardDeck.addDiscardedCards(tempCard);
							message.setText("YOUR OPPONENT DISCARDED A RANDOM CARD.");
						}
						setDeckText();
					} 
					//each discard 1
					else if (card.resultCode.equals("bothdis1")) {
						discardCard(card);
						discardDeck.addDiscardedCards(card);
						Card plCard = playerHand.getRandomCard();
						playerHand.removeCard(plCard);
						Card aiCard = aiHand.getRandomCard();
						aiHand.removeCard(aiCard);
						discardDeck.addDiscardedCards(plCard);
						discardDeck.addDiscardedCards(aiCard);
						setDeckText();
						message.setText("EACH PLAYER DISCARDED A RANDOM CARD.");
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
					} 
					//get new card
					else if (card.resultCode.equals("newC")) {
						discardCard(card);
						message.setText("Select a card to discard then a new card will be drawn for you.");
						playEventButton.setText("TRADE");
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
							aiHand = new Hand(playerCardDisplay.getChildren());
							if(cardsInHand>0){
								for(int i=0; i<cardsInHand; i++){
						        	aiHand.takeCard(drawDeck.drawCard(true));
						        }
							}
							else{
								aiHand.takeCard(drawDeck.drawCard(true));
							}
							message.setText("YOUR OPPONENT'S HAND WAS DISCARDED AND NEW CARDS DRAWN.");
						}
					}
					//trade 1 with opponent//////////////////////////////////////////////////////////////////////////////////////////
					else if (card.resultCode.equals("trade1")) {
						message.setText("YOU AND OPPONENT TRADED A RANDOM CARD.");
					} 
					//opponent discard 1
					else if (card.resultCode.equals("oppdis1")) {
						discardCard(card);
						if(isPlayerTurn.get()){
							aiHand.removeCard(aiHand.getRandomCard());
							message.setText("YOUR OPPONENT DISCARDED A RANDOM CARD.");
						}
						else{
							playerHand.removeCard(playerHand.getRandomCard());
							message.setText("YOUR OPPONENT MADE YOU DISCARD A RANDOM CARD.");
						}
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
					}
					else{
						aiHand.removeCard(card);
						aiHand.takeCard(drawDeck.drawCard(false));
					}
				}
			}
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
	
	
	public void handleNextRound(){
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
		discardDeckText.setFont(new Font(16.0));
		drawDeckText.setText("DECK:\n\n\n" + drawDeck.getDeckSize() + "  CARDS");
		discardDeckText.setText("DISCARD:\n\n\n" + discardDeck.getDeckSize() + "  CARDS");
	}
}
