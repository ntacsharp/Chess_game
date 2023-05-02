package com.chess.engine.player;

import com.chess.engine.board.Board;

public class MoveTrans {
    private final Board board;
    private final MoveStatus moveStatus;

    public MoveTrans(Board board, MoveStatus moveStatus) {
        this.board = board;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return moveStatus;
    }

    public Board getBoard() {
        return board;
    }
}
