package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.cards.Card.CardType;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class Deck {
    private List<Card> totalDeck;
    private List<Card> currentDeck;
    private List<Card> hand;
    private List<Card> usedCard;
    private List<Card> shoppingList;
    private List<Integer> powerIds;

    public Deck() {
        this.totalDeck = new ArrayList<Card>();
        this.currentDeck = new ArrayList<Card>();
        this.hand = new ArrayList<Card>();
        this.usedCard = new ArrayList<Card>();
    }

    public void fillDefaultTotalDeck(final PieceType pieceType) {
        if (pieceType == PieceType.BABARIAN) {
            totalDeck.add(new KingCard(false, false, false, false));
            totalDeck.add(new RookCard(false, false, false, false));
            totalDeck.add(new RookCard(false, false, false, false));
            totalDeck.add(new BishopCard(false, false, false, false));
            totalDeck.add(new BishopCard(false, false, false, false));
            totalDeck.add(new KnightCard(false, false, false, false));
            totalDeck.add(new KnightCard(false, false, false, false));
            totalDeck.add(new PawnCard(false, false, true, false));
            totalDeck.add(new PawnCard(false, true, true, false));
            totalDeck.add(new PawnCard(true, false, false, false));
            totalDeck.add(new PawnCard(false, false, false, true));
            totalDeck.add(new PawnCard(false, false, false, true));
        }
    }

    public void addToTotalDeck(final Card card){
        totalDeck.add(card);
    }

    public void refillCurrentDeck() {
        this.currentDeck.clear();
        this.currentDeck.addAll(this.totalDeck);
    }

    // public void updateCard(final Card card, final int powerID, final boolean
    // newVal) {
    // int ind = totalDeck.indexOf(card);
    // Card newCard = this.totalDeck.get(ind);
    // card.setHasPower(powerID, newVal);
    // this.totalDeck.set(cardID, card);
    // }

    public void fillHand(final int cardCount) {
        while (hand.size() < cardCount)
            draw();
    }

    public void promote() {
        boolean flag = false;
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card.getCardType() == CardType.PAWN) {
                flag = true;
                // hand.remove(i);
                hand.set(i, new QueenCard(card.getHasPower(0), card.getHasPower(1), card.getHasPower(2),
                        card.getHasPower(3)));
                break;
            }
        }
        if (!flag) {
            for (int i = 0; i < currentDeck.size(); i++) {
                Card card = currentDeck.get(i);
                if (card.getCardType() == CardType.PAWN) {
                    flag = true;
                    currentDeck.set(i, new QueenCard(card.getHasPower(0), card.getHasPower(1), card.getHasPower(2),
                            card.getHasPower(3)));
                    break;
                }
            }
        }
    }

    public void emptyCurrentDeck() {
        this.currentDeck.clear();
        this.usedCard.clear();
    }

    public void emptyTotalDeck() {
        this.totalDeck.clear();
        this.currentDeck.clear();
        this.usedCard.clear();
    }

    public void draw() {
        if (this.currentDeck.isEmpty()) {
            refillCurrentDeck();
            usedCard.clear();
        }
        Random rand = new Random();
        int tmp = rand.nextInt(this.currentDeck.size());
        this.hand.add(this.currentDeck.get(tmp));
        this.currentDeck.remove(tmp);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void emptyHand() {
        this.usedCard.addAll(hand);
        this.hand.clear();
    }

    public List<Card> getCurrentDeck() {
        return currentDeck;
    }

    public List<Card> getTotalDeck() {
        return totalDeck;
    }

    public void handToString() {
        for (Card card : hand) {
            System.out.println(card.toString());
        }
    }

    public void generateShoppingRound() {
        Random rand = new Random();
        shoppingList = new ArrayList<Card>();
        powerIds = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            int tmp = rand.nextInt(totalDeck.size());
            Card card = totalDeck.get(tmp);
            while (shoppingList.contains(card) || card.isFullPower()) {
                tmp = rand.nextInt(totalDeck.size());
                card = totalDeck.get(tmp);
            }
            shoppingList.add(card);
            tmp = rand.nextInt(4);
            while (card.getHasPower(tmp)) {
                tmp = rand.nextInt(4);
            }
            powerIds.add(tmp);
        }
    }

    public List<Card> getUsedCard() {
        return usedCard;
    }

    public List<Card> getShoppingList() {
        return shoppingList;
    }

    public List<Integer> getPowerIds() {
        return powerIds;
    }

    public void removeChoice(Card card, int powerID) {
        for (int i = 0; i < shoppingList.size(); i++) {
            if (shoppingList.get(i).equals(card) && powerIds.get(i).equals(powerID)) {
                shoppingList.remove(i);
                powerIds.remove(i);
                break;
            }
        }
    }
}
