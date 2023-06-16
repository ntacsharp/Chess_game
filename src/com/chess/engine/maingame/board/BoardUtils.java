package com.chess.engine.maingame.board;

import com.chess.engine.maingame.player.MoveStatus;
import com.chess.engine.maingame.player.MoveTrans;

public class BoardUtils {
    public static final int NUM_TILE_PER_ROW = 8;
    public static final int NUM_TILE_PER_COL = 8;

    private static final String[] corToString = {
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
    };

    private BoardUtils() {
        throw new RuntimeException("Bugged :(");
    }

    public static boolean isCorValid(final int row, final int col) {
        if (row >= 0 && row < NUM_TILE_PER_ROW && col >= 0 && col < NUM_TILE_PER_COL)
            return true;
        return false;
    }

    public static String getStringByCor(final int cor) {
        return corToString[cor];
    }

    public static boolean isEndGame(final Board board) {
        if (board.getCurrentPlayer().isCheckmated() || board.getCurrentPlayer().isStalemated())
            return true;
        else
            return false;
    }

    public static boolean isThreatKingMove(Move move, Board board) {
        final MoveTrans moveTrans = board.getCurrentPlayer().moveTrans(move, false);
        if (moveTrans.getMoveStatus() == MoveStatus.PLAYER_STILL_CHECKED)
            return true;
        else
            return false;
    }
}
