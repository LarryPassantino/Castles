package application.model;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Card extends GridPane{
	
	public final static String DISCARD_ONE = "Discard One Random Card";
	public final static String EACH_DISCARD_ONE = "Each Player Discards One Random Card";
	public final static String DRAW_TWO = "Draw Two Cards";
	public final static String NEW_CARD = "Trade Card Of Choosing For New Card";
	public final static String NEW_HAND = "Discard Hand And Draw New Hand Of Same Size";
	public final static String OPPONENT_DISCARD_ONE = "Opponent Discards One Random Card";
	public final static String TRADE_ONE = "Trade One Random Card With Opponent";
	
	public final static ArrayList<Card> allCards = new ArrayList<Card>();
	
	enum Unit{
		SPEARMEN(0,1,10), SHIELD_FOOTMEN(1,0,10), ARCHERS(3,3,20),
		LONGBOWS(0,6,6), GIANT(6,0,6), LADDERS(7,0,4), STRONG_WALLS(0,7,4),
		BATTERING_RAM(10,0,3), HEAVY_DOORS(0,10,3), ENCHANTER(0,13,3), WIZARD(13,0,3),
		BALLISTA(0,14,2), CATAPULT(14,0,2), GENERAL(16,16,4), HERO(20,20,1);
		
		final int attack, defense, quantity;
		final String type;
		private Unit(int attack, int defense, int quantity){
			this.attack = attack;
			this.defense = defense;
			this.quantity = quantity;
			this.type = "unit";
		}
	};
	
	enum Event{
		FIRE(DISCARD_ONE,"dis1"), STORM(DISCARD_ONE,"dis1"), SPY(DISCARD_ONE,"dis1"), TIRED_ARMY(DISCARD_ONE,"dis1"),
		EARTHQUAKE(EACH_DISCARD_ONE,"bothdis1"),
		LUCK(DRAW_TWO,"draw2"), GIFT(DRAW_TWO,"draw2"), BLESSING(DRAW_TWO,"draw2"), FORTUNE(DRAW_TWO,"draw2"),
		DARING(NEW_CARD,"newC"), FAVOR(NEW_CARD,"newC"), TACTICS(NEW_CARD,"newC"), OPPORTUNITY(NEW_CARD,"newC"),
		CLEAN_SLATE(NEW_HAND,"newH"),
		TREATY(TRADE_ONE,"trade1"),
		ILLUSIONIST(OPPONENT_DISCARD_ONE,"oppdis1"), TRICKSTER(OPPONENT_DISCARD_ONE,"oppdis1"),
		TRAITOR(OPPONENT_DISCARD_ONE,"oppdis1"), THIEF(OPPONENT_DISCARD_ONE,"oppdis1");
		
		final String result, resultCode, type;
		private Event(String result, String resultCode){
			this.result = result;
			this.resultCode = resultCode;
			this.type = "event";
		}
	};
	
	public final Unit unit;
	public final Event event;
	public final int attack, defense;
	public final String result;
	public final String resultCode;
	public final String type;
	public boolean selectable = false;
	public boolean isCardSelected = false;
	public boolean showBack = false;
	public Label cardLabel;
	public String blank = "";
	public String label;
	public String cardName, cardAttack, cardDefense, eventResult;
	public Label cardNameLabel = new Label("");
	public Label cardAttackLabel = new Label("");
	public Label cardDefenseLabel = new Label("");
	public String unitToString;
	public Label eventResultLabel = new Label("");
	public Text eventResultText = new Text("");
	public String eventToString;
	
	public Card() {
		this.unit = null;
		this.attack = 0;
		this.defense = 0;
		this.type = "";
		this.result = "";
		this.resultCode = "";
		this.event = null;
	}
	
	public Card(Unit unit) {
		this.unit = unit;
		this.attack = unit.attack;
		this.defense = unit.defense;
		this.type = unit.type;
		this.result = "";
		this.resultCode = "";
		this.event = null;
		unitToString = unit.toString();
		if(unitToString.contains("_")){
			unitToString = unitToString.replace('_', ' ');
		}
		this.cardName = unitToString;
		this.cardAttack = ""+attack;
		this.cardDefense = ""+defense;
		cardNameLabel.setText(cardName);
		cardAttackLabel.setText(cardAttack);
		cardDefenseLabel.setText(cardDefense);
		
		this.prefHeight(180);
		this.prefWidth(170);
		this.minHeight(180);
		this.minWidth(170);
		this.setHeight(180);
		this.setWidth(170);
		this.setPadding(new Insets(25,0,0,0));
		
		cardNameLabel.setFont(new Font("Eras Bold ITC",15.0));
		cardNameLabel.setTextAlignment(TextAlignment.CENTER);
		cardNameLabel.setAlignment(Pos.CENTER);
		cardAttackLabel.setFont(new Font("Eras Bold ITC",20.0));
		cardAttackLabel.setAlignment(Pos.CENTER);
		cardDefenseLabel.setFont(new Font("Eras Bold ITC",20.0));
		cardDefenseLabel.setAlignment(Pos.CENTER);
		this.setVgap(36);
		this.setHgap(60);
		this.setHalignment(cardNameLabel, HPos.CENTER);
		this.setHalignment(cardAttackLabel, HPos.RIGHT);
		this.setHalignment(cardDefenseLabel, HPos.RIGHT);
		this.setHgrow(cardNameLabel, Priority.ALWAYS);
		this.setHgrow(cardAttackLabel, Priority.ALWAYS);
		this.setHgrow(cardDefenseLabel, Priority.ALWAYS);
		this.add(cardNameLabel, 0, 0, 2, 1);
		this.add(cardAttackLabel, 0, 1, 1, 1);
		this.add(cardDefenseLabel, 0, 2, 1, 1);
		
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if(selectable){
		    		for (Card card : allCards) {
		    			if(card.selectable == true){
		    				card.highlightCardOnClick(false);
		    			}
					}
		    		highlightCardOnClick(selectable);
		    	}
		    }
		});
	}
	
	public Card(Event event) {
		this.unit = null;
		this.attack = 0;
		this.defense = 0;
		this.event = event;
		this.type = event.type;
		this.result = event.result;
		this.resultCode = event.resultCode;
		
		this.prefHeight(180);
		this.prefWidth(170);
		this.minHeight(180);
		this.minWidth(170);
		this.setHeight(180);
		this.setWidth(170);
		this.setPadding(new Insets(20,20,25,20));
		

		eventToString = event.toString();
		if(eventToString.contains("_")){
			eventToString = eventToString.replace('_', ' ');
		}
		this.cardName = eventToString;
		this.eventResult = result;
		cardNameLabel.setText(cardName);
		eventResultLabel.setText(eventResult);
		
		cardNameLabel.setFont(new Font("Eras Bold ITC",15.0));
		cardNameLabel.setTextAlignment(TextAlignment.CENTER);
		cardNameLabel.setAlignment(Pos.CENTER);
		eventResultLabel.setFont(new Font(12.0));
		eventResultLabel.setAlignment(Pos.CENTER);
		eventResultLabel.setWrapText(true);
		eventResultLabel.setPadding(new Insets(10,10,0,0));
		eventResultText.setFont(new Font("Eras Medium ITC",15.0));
		eventResultText.setText(eventResult);
		eventResultText.setTextAlignment(TextAlignment.CENTER);
		eventResultText.setWrappingWidth(120);
		this.setHalignment(cardNameLabel, HPos.CENTER);
		this.setHgrow(cardNameLabel, Priority.ALWAYS);
		this.setHgrow(eventResultLabel, Priority.ALWAYS);
		this.setVgap(50);
		this.setHgap(0);
		this.add(cardNameLabel, 0, 0, 1, 1);
		//this.add(eventResultLabel, 0, 1, 1, 1);
		this.add(eventResultText, 0, 1, 1, 1);
		
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if(selectable){
		    		for (Card card : allCards) {
		    			if(card.selectable == true){
		    				card.highlightCardOnClick(false);
		    			}
					}
		    		highlightCardOnClick(selectable);
		    	}
		    }
		});
	}
	
	public Pane getCardPane(){
		return this;
	}
	
	public void displayFront(){
		if(type.equals("event")){
			cardNameLabel.setVisible(true);
			eventResultLabel.setVisible(true);
			eventResultText.setVisible(true);
			setStyle("-fx-background-image: url(event_card_front.png);");
		}
		else{
			cardNameLabel.setVisible(true);
			cardAttackLabel.setVisible(true);
			cardDefenseLabel.setVisible(true);
			setStyle("-fx-background-image: url(unit_card_front_new.png);");
		}
	}
	
	public void displayBack(){
		if(type.equals("event")){
			cardNameLabel.setVisible(false);
			eventResultLabel.setVisible(false);
			eventResultText.setVisible(false);
			setStyle("-fx-background-image: url(wide_cardbacks.png);"+"-fx-border-color: #ffffff;"+"-fx-border-width: 2;");
		}
		else{
			cardNameLabel.setVisible(false);
			cardAttackLabel.setVisible(false);
			cardDefenseLabel.setVisible(false);
			setStyle("-fx-background-image: url(wide_cardbacks.png);"+"-fx-border-color: #ffffff;"+"-fx-border-width: 2;");
		}
	}
	
	public void highlightCard(boolean doHighlight){
		if(doHighlight){
			if(type.equals("unit")){
				cardNameLabel.setVisible(true);
				cardAttackLabel.setVisible(true);
				cardDefenseLabel.setVisible(true);
				setStyle("-fx-background-image: url(unit_card_front_new.png);"+"-fx-border-color: #8400ff;"+"-fx-border-width: 4;");
			}
			else{
				cardNameLabel.setVisible(true);
				eventResultLabel.setVisible(true);
				setStyle("-fx-background-image: url(event_card_front.png);"+"-fx-border-color: #8400ff;"+"-fx-border-width: 4;");
			}
		}
		else{
			if(type.equals("unit")){
				cardNameLabel.setVisible(true);
				cardAttackLabel.setVisible(true);
				cardDefenseLabel.setVisible(true);
				setStyle("-fx-background-image: url(unit_card_front_new.png);");
			}
			else{
				cardNameLabel.setVisible(true);
				eventResultLabel.setVisible(true);
				setStyle("-fx-background-image: url(event_card_front.png);");
			}
		}
	}
	
	public void highlightCardOnClick(boolean doHighlight){
		if(doHighlight && !isCardSelected){
			if(type.equals("unit")){
				setStyle("-fx-background-image: url(unit_card_front_new.png);"+"-fx-border-color: #99ff33;"+"-fx-border-width: 4;");
			}
			else{
				setStyle("-fx-background-image: url(event_card_front.png);"+"-fx-border-color: #99ff33;"+"-fx-border-width: 4;");
			}
			isCardSelected = true;
		}
		else{
			if(type.equals("unit")){
				setStyle("-fx-background-image: url(unit_card_front_new.png);");
			}
			else{
				setStyle("-fx-background-image: url(event_card_front.png);");
			}
			isCardSelected = false;
		}
	}
	
	public String toString(){
		if (this.type.equals("unit")) {
			String unitString = unit.toString();
			if (unitString.contains("_")) {
				unitString = unitString.replace('_', '\n');
			} else {
				unitString = unitString + "\n";
			}
			return unitString + "\n\n\nA= " + attack + "\nD= " + defense;
		}
		else{
			String eventString = event.toString();
			if (eventString.contains("_")) {
				eventString = eventString.replace('_', '\n');
			} else {
				eventString = eventString + "\n";
			}
			return eventString + "\n\n\n" + result;
		}
	}
	
	public int getEventPlayPriority(){
		if(resultCode.equals("dis1")){
			return 7;
		}
		else if(resultCode.equals("bothdis1")){
			return 5;
		}
		else if(resultCode.equals("draw2")){
			return 1;		
		}
		else if(resultCode.equals("newC")){
			return 3;
		}
		else if(resultCode.equals("newH")){
			return 4;
		}
		else if(resultCode.equals("oppdis1")){
			return 2;
		}
		else if(resultCode.equals("trade1")){
			return 6;
		}
		else{
			return 10;
		}
	}
}
