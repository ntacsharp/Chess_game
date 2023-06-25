package com.chess.engine.minigame.pieces;

import java.util.List;

import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;

public abstract class MiniPiece {
    protected int row;
    protected int col;

    public MiniPiece(final int row, final int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCorID() {
        return this.row * MiniBoardUtils.NUM_TILE_PER_ROW + this.col;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof MiniPiece))
            return false;
        MiniPiece objPiece = (MiniPiece) obj;
        if (this.getCorID() != objPiece.getCorID()) {
            return false;
        }
        return true;
    }

    @Override
    public abstract String toString();

    public abstract MiniPiece movePiece(MiniMove move);

    public abstract List<String> getInformation();
}
