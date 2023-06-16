package com.chess.engine.minigame.board;

public class MiniBoardUtils {
    public static final int NUM_TILE_PER_ROW = 5;
    public static final int NUM_TILE_PER_COL = 5;

    public static boolean isCorValid(final int row, final int col) {
        if (row >= 0 && row < NUM_TILE_PER_ROW && col >= 0 && col < NUM_TILE_PER_COL)
            return true;
        return false;
    }
}
