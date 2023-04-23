package com.chess.engine.board;

public class BoardUtils {
    public static final int NUM_TILE_PER_ROW = 8;
    public static final int NUM_TILE_PER_COL = 8;

    private BoardUtils() {
        throw new RuntimeException("Cannot instantiate!");
    }

    public static boolean isCorValid(final int row, final int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8)
            return true;
        return false;
    }
}
