package com.chess.engine.board;

import com.chess.engine.Party;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
    protected final Board board;
    protected final Piece movePiece;
    protected final int destinationCol;
    protected final int destinationRow;
    protected boolean isPromoteMove;

    Move(Board board, Piece movePiece, int destinationRow, int destinationCol) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationRow = destinationRow;
        this.destinationCol = destinationCol;
        this.isPromoteMove = false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Move))
            return false;
        Move objMove = (Move) obj;
        if (this.destinationCol != objMove.getDestinationCol()
                || this.destinationRow != objMove.getDestinationRow()
                || this.movePiece.equals(objMove.getMovePiece()))
            return false;
        return true;
    }

    public int getHashCode() {
        return this.hashCode();
    }

    public Piece getMovePiece() {
        return movePiece;
    }

    public int getDestinationCol() {
        return destinationCol;
    }

    public int getDestinationRow() {
        return destinationRow;
    }

    public int getDestinationCor() {
        return this.destinationRow * BoardUtils.NUM_TILE_PER_ROW + this.destinationCol;
    }

    public boolean isAttackingMove() {
        return false;
    }

    public boolean isCastle() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public int getCurrentCor() {
        return this.movePiece.getCorID();
    }

    public Board execute() {

        final Builder builder = new Builder();

        for (Piece piece : this.board.getCurrentPlayer().activePiecesList()) {
            if (!this.movePiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }

        for (Piece piece : this.board.getCurrentPlayer().getOpponent().activePiecesList()) {
            builder.setPiece(piece);
        }

        if (this.movePiece.isPawn() && this.movePiece.getPieceParty().isPromotionRow(this.destinationRow)) {
            Pawn promotePawn = (Pawn) this.movePiece;
            builder.setPiece(promotePawn.promote(this));
            this.isPromoteMove = true;
        } else
            builder.setPiece(this.movePiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getParty());
        return builder.build();
    }

    public static class NeutralMove extends Move {

        public NeutralMove(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol) {
            super(board, movePiece, destinationRow, destinationCol);
        }

        @Override
        public String toString() {
            String moveString = this.movePiece.toString() + BoardUtils.getStringByCor(this.getCurrentCor()) + "-"
                    + BoardUtils.getStringByCor(this.getDestinationCor());
            if (this.isPromoteMove) {
                Pawn movePawn = (Pawn) this.movePiece;
                moveString += "=" + movePawn.getPromoteToPiece().toString();
            }
            return moveString;
        }
    }

    public static class AttackingMove extends Move {
        protected final Piece attackedPiece;

        public AttackingMove(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol, final Piece attackedPiece) {
            super(board, movePiece, destinationRow, destinationCol);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttackingMove() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof AttackingMove))
                return false;
            Move objMove = (AttackingMove) obj;
            if (!super.equals(objMove)
                    || this.attackedPiece != objMove.getAttackedPiece())
                return false;
            return true;
        }

        @Override
        public String toString() {
            String moveString = this.movePiece.toString() + BoardUtils.getStringByCor(this.getCurrentCor()) + "x"
                    + BoardUtils.getStringByCor(this.getDestinationCor());
            if (this.isPromoteMove) {
                Pawn movePawn = (Pawn) this.movePiece;
                moveString += "=" + movePawn.getPromoteToPiece().toString();
            }
            return moveString;
        }
    }

    public static class EnPassant extends AttackingMove {
        public EnPassant(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol, final Piece attackedPiece) {
            super(board, movePiece, destinationRow, destinationCol, attackedPiece);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().activePiecesList()) {
                if (!this.movePiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().activePiecesList()) {
                if (!this.attackedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movePiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getParty());
            return builder.build();
        }

        @Override
        public String toString() {
            return super.toString() + "e.p";
        }
    }

    public static final class PawnJump extends NeutralMove {
        public PawnJump(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol) {
            super(board, movePiece, destinationRow, destinationCol);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().activePiecesList()) {
                if (!this.movePiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().activePiecesList()) {
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn) this.movePiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getParty());
            return builder.build();
        }
    }

    public static abstract class Castle extends NeutralMove {
        protected Piece moveRook;

        public Castle(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol, final Piece moveRook) {
            super(board, movePiece, destinationRow, destinationCol);
            this.moveRook = moveRook;
        }

        @Override
        public boolean isCastle() {
            return true;
        }
    }

    public static final class QueenSideCastle extends Castle {
        public QueenSideCastle(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol, final Piece moveRook) {
            super(board, movePiece, destinationRow, destinationCol, moveRook);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().activePiecesList()) {
                if (!this.movePiece.equals(piece) && !this.moveRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().activePiecesList()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movePiece.movePiece(this));
            if (this.movePiece.getPieceParty().isBlack())
                builder.setPiece(new Rook(0, 3, Party.BLACK, false));
            else
                builder.setPiece(new Rook(7, 3, Party.WHITE, false));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getParty());
            return builder.build();
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }

    public static class KingSideCastle extends Castle {
        public KingSideCastle(final Board board, final Piece movePiece, final int destinationRow,
                final int destinationCol, final Piece moveRook) {
            super(board, movePiece, destinationRow, destinationCol, moveRook);
        }

        @Override
        public Board execute() {

            final Builder builder = new Builder();

            for (Piece piece : this.board.getCurrentPlayer().activePiecesList()) {
                if (!this.movePiece.equals(piece) && !this.moveRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            for (Piece piece : this.board.getCurrentPlayer().getOpponent().activePiecesList()) {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movePiece.movePiece(this));
            if (this.movePiece.getPieceParty().isBlack())
                builder.setPiece(new Rook(0, 5, Party.BLACK, false));
            else
                builder.setPiece(new Rook(7, 5, Party.WHITE, false));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getParty());
            return builder.build();
        }

        @Override
        public String toString() {
            return "O-O";
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
        }

        public static Move findMove(final Board board,
                final int currentCor,
                final int destinationCor) {
            for (Move move : board.getCurrentPlayer().getLegalMoves()) {
                System.out.println(move.toString());
                if (move.getCurrentCor() == currentCor && move.getDestinationCor() == destinationCor)
                    return move;
                // System.out.println(move.getMovePiece() + " " + move.getCurrentCor() + " " +
                // move.getDestinationRow() + " " + move.getDestinationCol());
            }
            return null;
        }
    }

}
