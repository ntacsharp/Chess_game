package com.chess.engine.player;

public enum MoveStatus {
    DONE {
        @Override
        boolean isDone() {
            return true;
        }
    },
    ILLEGAL {
        @Override
        boolean isDone() {
            return false;
        }
    },
    PLAYER_STILL_CHECKED {
        @Override
        boolean isDone() {
            return false;
        }
    };

    abstract boolean isDone();
}
