package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Party;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

public abstract class Piece {

    protected final int row;
    protected final int col;
    protected final boolean isFirstMove;
    protected final PieceType pieceType;
    protected final Party pieceParty;

    public Piece(final int row, final int col, final Party pieceParty, final PieceType pieceType) {
        this.row = row;
        this.col = col;
        this.pieceParty = pieceParty;
        this.isFirstMove = true;
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

    public boolean isKing() {
        return this.pieceType == PieceType.KING ? true : false;
    }

    public boolean isRook() {
        return this.pieceType == PieceType.ROOK ? true : false;
    }

    public boolean isPawn() {
        return this.pieceType == PieceType.PAWN ? true : false;
    }

    public abstract Piece movedPiece(Move move);

    public abstract Collection<Move> legalMoves(final Board board);

    public enum PieceType {
        BISHOP("B"),
        KING("K"),
        KNIGHT("N"),
        PAWN("P"),
        QUEEN("Q"),
        ROOK("R");

        private String pieceName;

        PieceType(final String pieceName) {
            this.pieceName = pieceName;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

    }
}
