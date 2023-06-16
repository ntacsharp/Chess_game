package com.chess.engine.maingame.player;

public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    PLAYER_STILL_CHECKED {
        @Override
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();
}
