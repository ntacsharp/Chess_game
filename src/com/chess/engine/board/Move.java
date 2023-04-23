package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {
    final Board board;
    final Piece movePiece;
    final int destinationCol;
    final int destinationRow;

    Move(Board board, Piece movePiece, int destinationCol, int destinationRow) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationCol = destinationCol;
        this.destinationRow = destinationRow;
    }

    public static final class NeutralMove extends Move {

        public NeutralMove(final Board board, final Piece movePiece, final int destinationCol, final int destinationRow) {
            super(board, movePiece, destinationCol, destinationRow);

        }

    }

    public static final class AttackingMove extends Move {
        final Piece attackedPiece;

        public AttackingMove(final Board board, final Piece movePiece, final int destinationCol, final int destinationRow, final Piece attackedPiece) {
            super(board, movePiece, destinationCol, destinationRow);
            this.attackedPiece = attackedPiece;

        }
    }

}
