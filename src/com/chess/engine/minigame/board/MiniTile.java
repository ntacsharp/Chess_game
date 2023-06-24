package com.chess.engine.minigame.board;

import com.chess.engine.minigame.pieces.MiniPiece;

public abstract class MiniTile {
    protected final int row;
    protected final int col;
    protected final boolean blighted;

    public static MiniTile createTile(final int row, final int col, final MiniPiece piece, final boolean blighted) {
        if (piece != null) {
            return new OccupiedTile(row, col, piece, blighted);
        } else {
            return new EmptyTile(row, col, blighted);
        }
    }

    private MiniTile(int row, int col, final boolean blighted) {
        this.row = row;
        this.col = col;
        this.blighted = blighted;
    }

    public abstract boolean isOccupied();

    public abstract MiniPiece getPiece();

    public boolean isBlighted() {
        return blighted;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCor() {
        return row * MiniBoardUtils.NUM_TILE_PER_ROW + col;
    }

    private static class EmptyTile extends MiniTile {
        private EmptyTile(final int row, final int col, final boolean blighted) {
            super(row, col, blighted);
        }

        public boolean isOccupied() {
            return false;
        }

        @Override
        public MiniPiece getPiece() {
            return null;
        }
    }

    private static class OccupiedTile extends MiniTile {
        private MiniPiece pieceOnTile;

        private OccupiedTile(final int row, final int col, MiniPiece piece, final boolean blighted) {
            super(row, col, blighted);
            this.pieceOnTile = piece;
        }

        public boolean isOccupied() {
            return true;
        }

        public MiniPiece getPiece() {
            return pieceOnTile;
        }
    }
}
