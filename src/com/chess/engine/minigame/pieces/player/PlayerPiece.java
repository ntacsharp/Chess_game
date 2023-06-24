package com.chess.engine.minigame.pieces.player;

import java.util.Collection;

import com.chess.engine.minigame.board.MiniMove;
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

    @Override
    public String toString(){
        return this.getPieceType().toString();
    }

    public abstract Collection<Card> getDefaultDeck();
    
    public abstract String getInformation();

    @Override
    public abstract PlayerPiece movePiece(MiniMove move);

    public enum PieceType {
        BABARIAN ("BA"),
        TEMPLAR ("TE");

        private String pieceName;

        PieceType(String pieceName){
            this.pieceName = pieceName;
        }

        public String toString() {
            return pieceName;
        }
    }
}
