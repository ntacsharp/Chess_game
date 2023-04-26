package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTrans {
    private final Board board;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTrans(Board board, Move move, MoveStatus moveStatus) {
        this.board = board;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
