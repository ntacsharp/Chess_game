package com.chess.engine.minigame.pieces.player;

import java.util.ArrayList;
import java.util.Collection;

import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.cards.BishopCard;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.KingCard;
import com.chess.engine.minigame.cards.KnightCard;
import com.chess.engine.minigame.cards.PawnCard;
import com.chess.engine.minigame.cards.RookCard;

public class Babarian extends PlayerPiece{
    
    private final Collection<Card> defaultCards;

    public Babarian(final int row, final int col){
        super(row, col, PieceType.BABARIAN);
        this.defaultCards = new ArrayList<Card>();
        defaultCards.add(new KingCard(false, false, false, false));
        defaultCards.add(new RookCard(false, false, false, false));
        defaultCards.add(new RookCard(false, false, false, false));
        defaultCards.add(new BishopCard(false, false, false, false));
        defaultCards.add(new BishopCard(false, false, false, false));
        defaultCards.add(new KnightCard(false, false, false, false));
        defaultCards.add(new KnightCard(false, false, false, false));
        defaultCards.add(new PawnCard(false, false, true, false));
        defaultCards.add(new PawnCard(false, true, true, false));
        defaultCards.add(new PawnCard(true, false, false, false));
        defaultCards.add(new PawnCard(false, false, false, true));
        defaultCards.add(new PawnCard(false, false, false, true));
    }

    @Override
    public Collection<Card> getDefaultDeck() {
        return this.defaultCards;
    }

    @Override
    public Babarian movePiece(MiniMove move){
        return new Babarian(move.getDestinationRow(), move.getDestinationCol());
    }
}
