package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class Deck {
    private List<Card> totalDeck;
    private List<Card> currentDeck;
    private List<Card> hand;

    public Deck(final PlayerPiece playerPiece) {
        this.totalDeck = new ArrayList<Card>();
        this.currentDeck = new ArrayList<Card>();
        this.hand = new ArrayList<Card>();
        this.totalDeck.addAll(playerPiece.getDefaultDeck());
        refillCurrentDeck();
        fillHand(3);
    }

    public void refillCurrentDeck() {
        this.currentDeck.addAll(this.totalDeck);
    }

    public void updateCard(final int cardID, final int powerID, final boolean newVal) {
        Card card = this.totalDeck.get(cardID);
        card.setHasPower(powerID, newVal);
        this.totalDeck.set(cardID, card);
    }

    public void fillHand(final int cardCount) {
        for (int i = 0; i < cardCount; i++) {
            if (this.currentDeck.isEmpty())
                refillCurrentDeck();
            draw();
        }
    }

    // public void returnHand(){
    //     this.currentDeck.addAll(this.hand);
    //     this.hand.clear();
    // }

    public void draw() {
        Random rand = new Random();
        int tmp = rand.nextInt(this.currentDeck.size());
        this.hand.add(this.currentDeck.get(tmp));
        this.currentDeck.remove(tmp);
    }

    public List<Card> getHand() {
        return hand;
    }

    public Card getCardInHand(final int id) {
        if(id == -1) return null;
        return this.hand.get(id);
    }

    public List<Card> getCurrentDeck() {
        return currentDeck;
    }

    public List<Card> getTotalDeck() {
        return totalDeck;
    }

    public void handToString(){
        for (Card card : hand) {
            System.out.println(card.toString());
        }
    }

    public void generateShoppingRound(){
        Random rand = new Random();
        List<Card> shoppingList = new ArrayList<Card>();
        List<Integer> powerList = new ArrayList<Integer>();
        for(int i = 0; i < 5; i++){
            int tmp = rand.nextInt(totalDeck.size());
            Card card = totalDeck.get(tmp);
            while(shoppingList.contains(card) || card.isFullPower()){
                tmp = rand.nextInt(totalDeck.size());
                card = totalDeck.get(tmp);
            }
            shoppingList.add(card);
            tmp = rand.nextInt(4);
            while(card.getHasPower(tmp)){
                tmp = rand.nextInt(4);
            }
            powerList.add(tmp);
        }
    }
}
