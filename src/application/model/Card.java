package application.model;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Card extends Pane{
	
	public final static String DISCARD_ONE = "discard one \nrandom card";
	public final static String EACH_DISCARD_ONE = "each player \ndiscards one \nrandom card";
	public final static String DRAW_TWO = "draw two \ncards";
	public final static String NEW_CARD = "trade card of \nchoosing for \nnew card";
	public final static String NEW_HAND = "discard hand \nand draw new \nhand of same \nsize(not \nincluding this \ncard)";
	public final static String OPPONENT_DISCARD_ONE = "one opponent \ndiscards one \nrandom card";
	public final static String TRADE_ONE = "trade one \nrandom card \nwith opponent";

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
		/*FIRE(DISCARD_ONE,"dis1"), STORM(DISCARD_ONE,"dis1"), SPY(DISCARD_ONE,"dis1"), TIRED_ARMY(DISCARD_ONE,"dis1"),
		EARTHQUAKE(EACH_DISCARD_ONE,"bothdis1"),*/
		LUCK(DRAW_TWO,"draw2"), GIFT(DRAW_TWO,"draw2"), BLESSING(DRAW_TWO,"draw2"), FORTUNE(DRAW_TWO,"draw2")/*,
		DARING(NEW_CARD,"newC"), FAVOR(NEW_CARD,"newC"), TACTICS(NEW_CARD,"newC"), OPPORTUNITY(NEW_CARD,"newC"),
		CLEAN_SLATE(NEW_HAND,"newH"),
		TREATY(TRADE_ONE,"trade1"),
		ILLUSIONIST(OPPONENT_DISCARD_ONE,"oppdis1"), TRICKSTER(OPPONENT_DISCARD_ONE,"oppdis1"),
		TRAITOR(OPPONENT_DISCARD_ONE,"oppdis1"), THIEF(OPPONENT_DISCARD_ONE,"oppdis1")*/;
		
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
	
	public Card(Unit unit) {
		this.unit = unit;
		this.attack = unit.attack;
		this.defense = unit.defense;
		this.type = unit.type;
		this.result = "";
		this.resultCode = "";
		this.event = null;
		
		this.prefHeight(200);
		this.prefWidth(160);
		this.minHeight(200);
		this.minWidth(160);
		this.setHeight(200);
		this.setWidth(160);
		//this.setPadding(new Insets(20,10,20,10));
		this.setStyle("-fx-background-color: #ffffff");
		
		Text text = new Text(toString());
		text.setFont(new Font(13.0));
		text.setWrappingWidth(75);
		
		Label cardLabel = new Label(toString());
		cardLabel.setFont(new Font(12.0));
		cardLabel.setPadding(new Insets(10,10,10,10));
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	highlightCardOnClick(selectable);
		    }
		});
		
		this.getChildren().add(cardLabel);
	}
	
	public Card(Event event) {
		this.unit = null;
		this.attack = 0;
		this.defense = 0;
		this.event = event;
		this.type = event.type;
		this.result = event.result;
		this.resultCode = event.resultCode;
		
		this.prefHeight(200);
		this.prefWidth(160);
		this.minHeight(200);
		this.minWidth(160);
		this.setHeight(200);
		this.setWidth(160);
		//this.setPadding(new Insets(20,10,20,10));
		this.setStyle("-fx-background-color: #ffffff");
		
		Text text = new Text(toString());
		text.setFont(new Font(13.0));
		text.setWrappingWidth(75);
		
		Label cardLabel = new Label();
		cardLabel.setFont(new Font(13.0));
		cardLabel.setPadding(new Insets(10,10,10,10));
		cardLabel.setText(toString());
		cardLabel.setWrapText(true);
		cardLabel.prefWidth(75);
		
		
		this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	highlightCardOnClick(selectable);
		    }
		});
		
		this.getChildren().add(cardLabel);
	}
	
	public Pane getCardPane(){
		return this;
	}
	
	public void highlightCard(boolean doHighlight){
		if(doHighlight){
			this.setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: #ffc266;"+"-fx-border-width: 5;");
		}
		else{
			this.setStyle("-fx-background-color: #ffffff");
		}
	}
	
	public void highlightCardOnClick(boolean doHighlight){
		if(doHighlight && !isCardSelected){
			this.setStyle("-fx-background-color: #ffffff;"+"-fx-border-color: #99ff33;"+"-fx-border-width: 5;");
			isCardSelected = true;
		}
		else{
			this.setStyle("-fx-background-color: #ffffff");
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
			return eventString + "\n\n\n" + result;
		}
	}
	
}
