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
import com.chess.engine.minigame.pieces.MiniPiece;

public class Babarian extends PlayerPiece{
    
    private final Collection<Card> defaultCards;

    public Babarian(final int row, final int col){
        super(row, col, PieceType.BABARIAN);
        this.defaultCards = new ArrayList<Card>();
        defaultCards.add(new KingCard(0, false, false, false, false));
        defaultCards.add(new RookCard(1, false, false, false, false));
        defaultCards.add(new RookCard(2, false, false, false, false));
        defaultCards.add(new BishopCard(3, false, false, false, false));
        defaultCards.add(new BishopCard(4, false, false, false, false));
        defaultCards.add(new KnightCard(5, false, false, false, false));
        defaultCards.add(new KnightCard(6, false, false, false, false));
        defaultCards.add(new PawnCard(7, false, false, true, false));
        defaultCards.add(new PawnCard(8, false, true, true, false));
        defaultCards.add(new PawnCard(9, true, false, false, false));
        defaultCards.add(new PawnCard(10, false, false, false, true));
        defaultCards.add(new PawnCard(11, false, false, false, true));
    }

    @Override
    public Collection<Card> getDefaultDeck() {
        return this.defaultCards;
    }

    @Override
    public MiniPiece movePiece(MiniMove move){
        return new Babarian(move.getDestinationRow(), move.getDestinationCol());
    }
}
