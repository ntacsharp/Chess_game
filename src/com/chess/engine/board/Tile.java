package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;
import com.chess.engine.pieces.Piece;

public abstract class Tile {
    protected final int row;
    protected final int col;

    private static final Map<Integer, EmptyTile> EMPTY_TILES = createEmpty8x8Tiles();

    private static Map<Integer, EmptyTile> createEmpty8x8Tiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int r = 0; r < BoardUtils.NUM_TILE_PER_ROW; r++) {
            for (int c = 0; c < BoardUtils.NUM_TILE_PER_COL; c++) {
                emptyTileMap.put(r * BoardUtils.NUM_TILE_PER_ROW + c, new EmptyTile(r, c));
            }
        }

        return emptyTileMap;
    }

    public static Tile createTile(final int row, final int col, final Piece piece) {
        if (piece != null) {
            return new OccupiedTile(row, col, piece);
        } else {
            return EMPTY_TILES.get(row * BoardUtils.NUM_TILE_PER_ROW + col);
        }
    }

    private Tile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    public static final class EmptyTile extends Tile {
        private EmptyTile(final int row, final int col) {
            super(row, col);
        }

        public boolean isOccupied() {
            return false;
        }

        public Piece getPiece() {
            return null;
        }
    }

    public static final class OccupiedTile extends Tile {
        private Piece pieceOnTile;

        private OccupiedTile(final int row, final int col, Piece piece) {
            super(row, col);
            this.pieceOnTile = piece;
        }

        public boolean isOccupied() {
            return true;
        }

        public Piece getPiece() {
            return pieceOnTile;
        }
    }
}
