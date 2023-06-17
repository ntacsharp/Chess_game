package com.chess.engine.maingame.pieces;

import java.util.Collection;

import com.chess.engine.maingame.Party;
import com.chess.engine.maingame.board.Board;
import com.chess.engine.maingame.board.BoardUtils;
import com.chess.engine.maingame.board.Move;

public abstract class Piece {

    protected final int row;
    protected final int col;
    private final boolean isFirstMove;
    private final PieceType pieceType;
    protected final Party pieceParty;

    protected Piece(final int row, final int col, final Party pieceParty, final PieceType pieceType,
            final boolean isFirstMove) {
        this.row = row;
        this.col = col;
        this.pieceParty = pieceParty;
        this.isFirstMove = isFirstMove;
        this.pieceType = pieceType;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof Piece))
            return false;
        Piece objPiece = (Piece) obj;
        if (this.getCorID() != objPiece.getCorID()
                || this.isFirstMove != objPiece.isFirstMove()
                || this.pieceType != objPiece.getPieceType()
                || this.pieceParty != objPiece.getPieceParty())
            return false;
        return true;
    }

    public int getHashCode() {
        return this.hashCode();
    }

    public int getCorID() {
        return this.row * BoardUtils.NUM_TILE_PER_ROW + this.col;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public Party getPieceParty() {
        return this.pieceParty;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public boolean isKing() {
        return this.pieceType == PieceType.KING ? true : false;
    }

    public boolean isRook() {
        return this.pieceType == PieceType.ROOK ? true : false;
    }

    public boolean isPawn() {
        return this.pieceType == PieceType.PAWN ? true : false;
    }

    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> legalMoves(final Board board);

    public enum PieceType {
        BISHOP("B", 300),
        KING("K", 10000),
        KNIGHT("N", 299),
        PAWN("P", 100),
        QUEEN("Q", 900),
        ROOK("R", 500);

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        // public abstract int getPieceValue();

    }
}
