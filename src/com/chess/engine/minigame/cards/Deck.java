package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void draw() {
        Collections.shuffle(this.currentDeck);
        this.hand.add(this.currentDeck.get(0));
        this.currentDeck.remove(0);
    }

    public Card getCardInHand(final int id) {
        return this.hand.get(id);
    }

    public Card getCardInTotalDeck(final int id) {
        return this.totalDeck.get(id);
    }

    public void handToString(){
        for (Card card : hand) {
            System.out.println(card.toString());
        }
    }
}