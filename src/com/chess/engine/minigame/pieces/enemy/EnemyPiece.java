package com.chess.engine.minigame.pieces.enemy;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.NimbleMove;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public abstract class EnemyPiece extends MiniPiece {
    protected final int[][] MOVE_SET = {
            { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    protected boolean immune;
    private final PieceType pieceType;
    private final int turn;
    private final boolean nimble;
    private boolean isCurrentlyNimble;

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

    public MiniBoard triggerNimble(final MiniBoard board) {
        PlayerPiece playerPiece = board.getPlayerPiece();
        int rDif = this.getRow() - playerPiece.getRow();
        int cDif = this.getCol() - playerPiece.getCol();
        if (rDif != 0)
            rDif /= Math.abs(rDif);
        if (cDif != 0)
            cDif /= Math.abs(cDif);
        if (!board.getTile(this.getRow() + rDif, this.getCol() + cDif).isOccupied()) {
            MiniMove move = new NimbleMove(board, this, this.getRow() + rDif, this.getCol() + cDif);
            return move.execute();
        } else {
            for (int[] move : MOVE_SET) {
                int r = this.getRow() + move[0];
                int c = this.getRow() + move[1];
                int tmp = 0;
                if (!board.getTile(r, c).isOccupied()) {
                    if (Math.abs(r - playerPiece.getRow()) + Math.abs(c - playerPiece.getCol()) > tmp) {
                        rDif = r;
                        cDif = c;
                    }
                }
            }
            MiniMove move = new NimbleMove(board, this, rDif, cDif);
            return move.execute();
        }
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

    public abstract EnemyPiece nimbledPiece(MiniMove move);

    public abstract void triggerImmune(final MiniBoard board);

    public abstract int calculateDmg(final MiniBoard board);

    @Override
    public String toString() {
        return this.getPieceType().toString();
    }

    public enum PieceType {
        INFECTED("IF", 0),
        BEAST("BE", 1),
        ZOMBIE("ZM", 2),
        SWORDMAN("SW", 3),
        ARCHER("AR", 3),
        SHAMAN("SH", 5);

        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        public int getPieceValue() {
            return this.pieceValue;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
