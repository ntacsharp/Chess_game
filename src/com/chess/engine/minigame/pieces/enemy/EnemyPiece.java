package com.chess.engine.minigame.pieces.enemy;

import java.awt.image.BufferedImage;
import java.util.List;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public abstract class EnemyPiece extends MiniPiece {
    public final int[][] MOVE_SET = {
            { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    protected boolean immune;
    private final PieceType pieceType;
    protected int turn;
    protected final boolean nimble;
    protected boolean isCurrentlyNimble;

    public EnemyPiece(final int row, final int col, final PieceType pieceType, final boolean nimble,
            final boolean isCurrentlyNimble, final int turn) {
        super(row, col);
        this.pieceType = pieceType;
        this.immune = false;
        this.turn = turn;
        this.nimble = nimble;
        this.isCurrentlyNimble = isCurrentlyNimble;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public boolean isCurrentlyNimble() {
        return this.isCurrentlyNimble;
    }

    public boolean isImmune() {
        return this.immune;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isNimble() {
        return nimble;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof EnemyPiece))
            return false;
        EnemyPiece objPiece = (EnemyPiece) obj;
        if (!super.equals(obj)
                || this.turn != objPiece.getTurn()
                || this.isNimble() != objPiece.isNimble())
            return false;
        return true;
    }

    public abstract boolean canAttactk(final int r, final int c);

    public abstract MiniBoard move(final MiniBoard board);

    @Override
    public abstract EnemyPiece movePiece(MiniMove move);

    public abstract EnemyPiece nimbledPiece(final int row, final int col);

    public abstract void triggerImmune(final MiniBoard board);

    public abstract int calculateDmg(final MiniBoard board);

    public abstract List<Integer> getRange();

    public abstract List<String> getInformation();

    @Override
    public String toString() {
        return this.getPieceType().toString();
    }

    public enum PieceType {
        INFECTED("IF", 0, "Infected"),
        BEAST("BE", 1, "Beast"),
        ZOMBIE("ZM", 2, "Zombie"),
        SWORDMAN("SW", 3, "Viking"),
        ARCHER("AR", 3, "Archer"),
        SHAMAN("SH", 5, "Demon");

        private String pieceName;
        private int pieceValue;
        private String displayName;

        PieceType(final String pieceName, final int pieceValue, final String displayName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
            this.displayName = displayName;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public String getName(){
            return this.displayName;
        }
    }
}
