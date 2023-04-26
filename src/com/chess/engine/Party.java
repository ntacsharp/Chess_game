package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Party {
    WHITE {
        @Override
        public int getDirecion() {
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;            
        }

        @Override
        public boolean isPromotionRow(int row) {
            return (row == 0);
        }
    },
    BLACK {
        @Override
        public int getDirecion() {
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public boolean isPromotionRow(int row) {
            return (row == 7);
        }
        
    };

    public abstract int getDirecion();

    public abstract boolean isBlack();

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);

    public abstract boolean isPromotionRow(int row);
}
