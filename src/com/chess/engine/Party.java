package com.chess.engine;

public enum Party {
    WHITE{
        @Override
        public int getDirecion() {
            return -1;
        }
        
    },
    BLACK{
        @Override
        public int getDirecion() {
            return 1;
        }
    };

    public abstract int getDirecion();
}
