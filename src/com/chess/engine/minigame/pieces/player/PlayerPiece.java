package com.chess.engine.minigame.pieces.player;

import java.util.Collection;

import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.MiniPiece;

public abstract class PlayerPiece extends MiniPiece{
    protected final PieceType pieceType;

    protected PlayerPiece(final int row, final int col, final PieceType pieceType) {
        super(row, col);
        this.pieceType = pieceType;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public abstract Collection<Card> getDefaultDeck();

    public enum PieceType {
        BABARIAN,
        TEMPLAR,
    }
}
