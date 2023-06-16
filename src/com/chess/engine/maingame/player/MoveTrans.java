package com.chess.engine.maingame.player;

import com.chess.engine.maingame.board.Board;

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
