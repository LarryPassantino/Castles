package application.model;

import java.util.ArrayList;
import javafx.scene.Parent;
import application.model.Card.Event;
import application.model.Card.Unit;
import application.view.MainGameViewController;


public class Deck extends Parent{
	
public ArrayList<Card> cards = new ArrayList<Card>();
	
	public Deck(){
	}

	public final void refill() {
		cards.clear();
		for(Unit unit : Unit.values()){
			for(int i=0; i<unit.quantity; i++){
				Card card = new Card(unit);
				cards.add(card);
				Card.allCards.add(card);
			}
		}
		for(Event event : Event.values()){
			Card card = new Card(event);
			cards.add(card);
			Card.allCards.add(card);
		}
	}
	
	public final void shuffle() {
		for (Card card : MainGameViewController.discardDeck.cards) {
			card.selectable = false;
			card.isCardSelected = false;
			card.displayFront();
			MainGameViewController.drawDeck.cards.add(card);
		}
		MainGameViewController.discardDeck.cards.clear();
	}
	
	public Card drawCard(boolean selectable){
		if(getDeckSize() == 0){
			shuffle();
		}
		Card card = null;
		while(card == null){
			int index = (int)(Math.random()*cards.size());
			card = cards.get(index);
			cards.remove(index);
		}
		if(selectable){
			card.selectable = true;
			card.displayFront();
		}
		else{
			card.displayBack();
		}
		return card;
	}
	
	public void addDiscardedCards(Card card){
		cards.add(card);
	}
	
	public ArrayList<Card> getAllCards(){
		return cards;
	}
	
	public void clearDeck(){
		cards.clear();
	}
	
	public int getDeckSize(){
		return cards.size();
	}
	
	public String toString(){
		return "# of Cards Left:\n\n" + cards.size();
	}

}
