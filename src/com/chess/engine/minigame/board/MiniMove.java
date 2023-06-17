package com.chess.engine.minigame.board;

import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard.Builder;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.Beast;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.enemy.Infected;
import com.chess.engine.minigame.pieces.enemy.Spider;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public abstract class MiniMove {
    protected final MiniBoard board;
    protected final MiniPiece movePiece;
    protected final int destinationCol;
    protected final int destinationRow;
    protected boolean isPromoteMove;

    protected MiniMove(MiniBoard board, MiniPiece movePiece, int destinationRow, int destinationCol) {
        this.board = board;
        this.movePiece = movePiece;
        this.destinationRow = destinationRow;
        this.destinationCol = destinationCol;
        this.isPromoteMove = false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof MiniMove))
            return false;
        MiniMove objMove = (MiniMove) obj;
        if (this.destinationCol != objMove.getDestinationCol()
                || this.destinationRow != objMove.getDestinationRow())
            return false;
        return true;
    }

    public int getDestinationCol() {
        return destinationCol;
    }

    public int getDestinationRow() {
        return destinationRow;
    }

    public int getDestinationCor() {
        return this.destinationRow * MiniBoardUtils.NUM_TILE_PER_ROW + this.destinationCol;
    }

    public boolean isAttackingMove() {
        return false;
    }

    public MiniBoard execute() {

        final Builder builder = new Builder();
        for (int i = 0; i < MiniBoardUtils.NUM_TILE_PER_COL; i++) {
            for (int j = 0; j < MiniBoardUtils.NUM_TILE_PER_ROW; j++) {
                if (this.board.getTile(i, j).isBlighted())
                    builder.setBlight(i * MiniBoardUtils.NUM_TILE_PER_ROW + j);
            }
        }

        if (this.movePiece instanceof PlayerPiece) {
            for (MiniPiece piece : this.board.getEnemyPieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movePiece.movePiece(this));
            // trigger power
        } else if (this.movePiece instanceof EnemyPiece) {
            builder.setPiece(this.board.getPlayerPiece());
            for (MiniPiece piece : this.board.getEnemyPieces()) {
                if (!this.movePiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movePiece.movePiece(this));
        }

        return builder.build();
    }

    public static class NeutralMove extends MiniMove {
        public NeutralMove(MiniBoard board, MiniPiece movePiece, int destinationRow, int destinationCol) {
            super(board, movePiece, destinationRow, destinationCol);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof NeutralMove && super.equals(obj);
        }
    }

    public static class AttackingMove extends MiniMove {
        protected final EnemyPiece attackedPiece;

        public AttackingMove(MiniBoard board, MiniPiece movePiece, final int destinationRow,
                final int destinationCol, final EnemyPiece attackedPiece) {
            super(board, movePiece, destinationRow, destinationCol);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof AttackingMove && super.equals(obj);
        }

        @Override
        public boolean isAttackingMove() {
            return true;
        }

        @Override
        public MiniBoard execute() {
            final Builder builder = new Builder();
            for (int i = 0; i < MiniBoardUtils.NUM_TILE_PER_COL; i++) {
                for (int j = 0; j < MiniBoardUtils.NUM_TILE_PER_ROW; j++) {
                    if (this.board.getTile(i, j).isBlighted())
                        builder.setBlight(i * MiniBoardUtils.NUM_TILE_PER_ROW + j);
                }
            }
            for (int i = 0; i < MiniBoardUtils.NUM_TILE_PER_COL; i++) {
                for (int j = 0; j < MiniBoardUtils.NUM_TILE_PER_ROW; j++) {
                    if (this.board.getTile(i, j).isBlighted())
                        builder.setBlight(i * MiniBoardUtils.NUM_TILE_PER_ROW + j);
                }
            }

            if (attackedPiece.isCurrentlyNimble()) {
                MiniBoard newBoard = attackedPiece.triggerNimble(this.board);
                for (MiniPiece piece : newBoard.getEnemyPieces()) {
                    builder.setPiece(piece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
            } else if (attackedPiece instanceof Beast) {
                Random rand = new Random();
                int r = rand.nextInt(5);
                int c = rand.nextInt(5);
                while ((r == this.destinationRow && c == this.destinationCol)
                        || (this.board.getTile(r, c).isOccupied())) {
                    r = rand.nextInt(5);
                    c = rand.nextInt(5);
                }
                builder.setPiece(new Infected(r, c, attackedPiece.getTurn()));
                for (MiniPiece piece : this.board.getEnemyPieces()) {
                    if (!attackedPiece.equals(piece))
                        builder.setPiece(piece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
            } else if (attackedPiece instanceof Spider) {
                Spider spider = (Spider) attackedPiece;
                spider.triggerEffect(builder);
                for (MiniPiece piece : this.board.getEnemyPieces()) {
                    if (!attackedPiece.equals(piece))
                        builder.setPiece(piece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
            } else if (attackedPiece instanceof Infected) {
                Infected infected = (Infected) attackedPiece;
                infected.triggerEffect(builder);
                for (MiniPiece piece : this.board.getEnemyPieces()) {
                    if (!attackedPiece.equals(piece))
                        builder.setPiece(piece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
            } else {
                for (MiniPiece piece : this.board.getEnemyPieces()) {
                    if (!attackedPiece.equals(piece))
                        builder.setPiece(piece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
            }

            return builder.build();
        }
    }

    public static class NimbleMove extends NeutralMove {
        public NimbleMove(MiniBoard board, MiniPiece movePiece, int destinationRow, int destinationCol) {
            super(board, movePiece, destinationRow, destinationCol);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof NimbleMove && super.equals(obj);
        }

        @Override
        public MiniBoard execute() {

            final Builder builder = new Builder();
            for (int i = 0; i < MiniBoardUtils.NUM_TILE_PER_COL; i++) {
                for (int j = 0; j < MiniBoardUtils.NUM_TILE_PER_ROW; j++) {
                    if (this.board.getTile(i, j).isBlighted())
                        builder.setBlight(i * MiniBoardUtils.NUM_TILE_PER_ROW + j);
                }
            }
            builder.setPiece(this.board.getPlayerPiece());
            for (MiniPiece piece : this.board.getEnemyPieces()) {
                if (!this.movePiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            EnemyPiece piece = (EnemyPiece) this.movePiece;
            builder.setPiece(piece.nimbledPiece(this));

            return builder.build();
        }
    }

    // public static class MoveFactory {
    // private MoveFactory() {
    // }

    // public static MiniMove findMove(final MiniBoard board,
    // final int currentCor,
    // final int destinationCor) {
    // for (MiniMove move : board.getCurrentPlayer().getLegalMoves()) {
    // if (move.getCurrentCor() == currentCor && move.getDestinationCor() ==
    // destinationCor)
    // return move;
    // }
    // return null;
    // }
    // }
}
