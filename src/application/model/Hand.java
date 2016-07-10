package application.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Hand {
	
	private ObservableList<Node> hand = FXCollections.observableArrayList();
	private SimpleIntegerProperty attackValue = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty defenseValue = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty numAttackers = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty handSize = new SimpleIntegerProperty(0);
	
	
	public Hand(ObservableList<Node> hand){
		this.hand = hand;
	}
	
	public void takeCard(Card card){
		hand.add(card);
		attackValue.set(attackValue.get() + card.attack);
		defenseValue.set(defenseValue.get() + card.defense);
		handSize.set(handSize.get() + 1);
		
		if(card.attack > 0){
			numAttackers.set(numAttackers.get() + 1);
		}
	}
	
	public void removeCard(Card card){
		hand.remove(card);
		attackValue.set(attackValue.get() - card.attack);
		defenseValue.set(defenseValue.get() - card.defense);
		handSize.set(handSize.get()-1);
		
		if(card.attack > 0){
			numAttackers.set(numAttackers.get() - 1);
		}
	}
	
	public Card getRandomCard(){
		Card card;
		int index = (int)(Math.random()*hand.size());
		card = (Card)hand.get(index);
		return card;
	}
	
	public ArrayList getHandList(){
		ArrayList<Card> handList = new ArrayList<Card>();
		for (Node node : hand) {
			handList.add((Card)node);
		}
		return handList;
	}
	
	public void highlightAttackers(){
		for (Node card : hand) {
			Card thisCard = (Card) card;
			if(thisCard.attack > 0){
				thisCard.highlightCard(true);;
			}
		}
	}
	
	public void highlightDefenders(){
		for (Node card : hand) {
			Card thisCard = (Card) card;
			if(thisCard.defense > 0){
				thisCard.highlightCard(true);;
			}
		}
	}
	
	public void reset(){
		hand.clear();
		attackValue.set(0);
		defenseValue.set(0);
		numAttackers.set(0);
		handSize.set(0);
	}
	
	public SimpleIntegerProperty numAttackersProperty(){
		return numAttackers;
	}
	
	public SimpleIntegerProperty attackValueProperty(){
		return attackValue;
	}
	
	public SimpleIntegerProperty defenseValueProperty(){
		return defenseValue;
	}
	
	public SimpleIntegerProperty handSizeProperty(){
		return handSize;
	}
}
