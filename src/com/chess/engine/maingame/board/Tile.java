package com.chess.engine.maingame.board;

import com.chess.engine.maingame.pieces.Piece;

public abstract class Tile {
    protected final int row;
    protected final int col;

    public static Tile createTile(final int row, final int col, final Piece piece) {
        if (piece != null) {
            return new OccupiedTile(row, col, piece);
        } else {
            return new EmptyTile(row, col);
        }
    }

    private Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCor() {
        return row * BoardUtils.NUM_TILE_PER_ROW + col;
    }

    private static class EmptyTile extends Tile {
        private EmptyTile(final int row, final int col) {
            super(row, col);
        }

        @Override
        public String toString() {
            return ".";
        }

        public boolean isOccupied() {
            return false;
        }

        @Override
        public Piece getPiece() {
            return null;
        }

    }

    private static class OccupiedTile extends Tile {
        private Piece pieceOnTile;

        private OccupiedTile(final int row, final int col, Piece piece) {
            super(row, col);
            this.pieceOnTile = piece;
        }

        @Override
        public String toString() {
            Piece piece = getPiece();       
            if(piece.getPieceParty().isBlack()){
                return piece.toString().toLowerCase();
            }             
            else return piece.toString();
        }

        public boolean isOccupied() {
            return true;
        }

        public Piece getPiece() {
            return pieceOnTile;
        }
    }
}
