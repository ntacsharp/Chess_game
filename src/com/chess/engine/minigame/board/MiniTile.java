package com.chess.engine.minigame.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.minigame.pieces.MiniPiece;

public abstract class MiniTile {
    protected final int row;
    protected final int col;

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmpty5x5Tiles();

    private static Map<Integer, EmptyTile> createEmpty5x5Tiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int r = 0; r < MiniBoardUtils.NUM_TILE_PER_ROW; r++) {
            for (int c = 0; c < MiniBoardUtils.NUM_TILE_PER_COL; c++) {
                emptyTileMap.put(r * MiniBoardUtils.NUM_TILE_PER_ROW + c, new EmptyTile(r, c));
            }
        }

        return emptyTileMap;
    }

    public static MiniTile createTile(final int row, final int col, final MiniPiece piece) {
        if (piece != null) {
            return new OccupiedTile(row, col, piece);
        } else {
            return EMPTY_TILES.get(row * MiniBoardUtils.NUM_TILE_PER_ROW + col);
        }
    }

    private MiniTile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isOccupied();

    public abstract MiniPiece getPiece();

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

        public MiniPiece getPiece() {
            return null;
        }
    }

    private static class OccupiedTile extends MiniTile {
        private MiniPiece pieceOnTile;

        private OccupiedTile(final int row, final int col, MiniPiece piece) {
            super(row, col);
            this.pieceOnTile = piece;
        }

        @Override
        public String toString() {
            return "";
            // MiniPiece piece = getPiece();       
            // if(piece.getPieceParty().isBlack()){
            //     return piece.toString().toLowerCase();
            // }             
            // else return piece.toString();
        }

        public boolean isOccupied() {
            return true;
        }

        public MiniPiece getPiece() {
            return pieceOnTile;
        }
    }
}
