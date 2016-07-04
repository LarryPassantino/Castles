package application.model;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Card extends Parent{
	
	public final static String DISCARD_ONE = "discard one random card";
	public final static String EACH_DISCARD_ONE = "each player discards one random card";
	public final static String DRAW_TWO = "draw two cards";
	public final static String NEW_CARD = "trade card of choosing for new card";
	public final static String NEW_HAND = "discard hand and draw new hand of same size(not including this card)";
	public final static String OPPONENT_DISCARD_ONE = "one opponent discards one random card";
	public final static String TRADE_ONE = "trade one random card with opponent";

	enum Unit{
		SPEARMEN(0,1,10), SHIELD_FOOTMEN(1,0,10), ARCHERS(3,3,20),
		LONGBOWS(0,6,6), GIANT(6,0,6), LADDERS(7,0,4), STRONG_WALLS(0,7,4),
		BATTERING_RAM(10,0,3), HEAVY_DOORS(0,10,3), ENCHANTER(0,13,3), WIZARD(13,0,3),
		BALLISTA(0,14,2), CATAPULT(14,0,2), GENERAL(16,16,4), HERO(20,20,1);
		
		final int attack, defense, quantity;
		private Unit(int attack, int defense, int quantity){
			this.attack = attack;
			this.defense = defense;
			this.quantity = quantity;
		}
	};
	
	enum Event{
		FIRE(DISCARD_ONE), STORM(DISCARD_ONE), SPY(DISCARD_ONE), TIRED_ARMY(DISCARD_ONE),
		EARTHQUAKE(EACH_DISCARD_ONE),
		LUCK(DRAW_TWO), GIFT(DRAW_TWO), BLESSING(DRAW_TWO), FORTUNE(DRAW_TWO),
		DARING(NEW_CARD), FAVOR(NEW_CARD), TACTICS(NEW_CARD), OPPORTUNITY(NEW_CARD),
		CLEAN_SLATE(NEW_HAND),
		TREATY(TRADE_ONE),
		ILLUSIONIST(OPPONENT_DISCARD_ONE), TRICKSTER(OPPONENT_DISCARD_ONE),
		TRAITOR(OPPONENT_DISCARD_ONE), THIEF(OPPONENT_DISCARD_ONE);
		
		final String result;
		private Event(String result){
			this.result = result;
		}
	};
	
	public final Unit unit;
	//public final Event event;//when events added
	public final int attack, defense;
	//public final String result;//for events
	
	public Card(Unit unit){
		this.unit = unit;
		this.attack = unit.attack;
		this.defense = unit.defense;
		
		Rectangle bg = new Rectangle(80,100);
		bg.setArcWidth(20);
		bg.setArcHeight(20);
		bg.setFill(Color.WHITE);
		
		Text text = new Text(toString());
		text.setFont(new Font(10.0));
		text.setWrappingWidth(70);
		
		getChildren().add(new StackPane(bg, text));
	}
	
	public String toString(){
		String unitString = unit.toString();
		if(unitString.contains("_")){
			unitString = unitString.replace('_', ' ');
		}
		else{
			unitString = unitString + "\n";
		}
		return unitString + "\n\n\nA= " + attack + "\nD= " + defense;
	}
	
}
