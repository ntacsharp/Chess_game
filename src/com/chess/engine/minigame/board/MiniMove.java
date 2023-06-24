package com.chess.engine.minigame.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard.Builder;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.Beast;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.enemy.Infected;
import com.chess.engine.minigame.pieces.enemy.Zombie;

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

    public int getCurrentCor() {
        return this.movePiece.getCorID();
    }

    public boolean isPlayerMove() {
        return false;
    }

    public abstract MiniBoard execute();

    public static class EnemyMove extends MiniMove {
        public EnemyMove(MiniBoard board, MiniPiece movePiece, int destinationRow, int destinationCol) {
            super(board, movePiece, destinationRow, destinationCol);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof EnemyMove && super.equals(obj);
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
            builder.setPiece(this.movePiece.movePiece(this));
            return builder.build();
        }
    }

    public static class PlayerMove extends MiniMove {
        protected final List<EnemyPiece> enemyPieces = new ArrayList<EnemyPiece>();

        public PlayerMove(MiniBoard board, MiniPiece movePiece, final int destinationRow,
                final int destinationCol, final List<EnemyPiece> enemyPieces) {
            super(board, movePiece, destinationRow, destinationCol);
            this.enemyPieces.addAll(enemyPieces);
        }

        @Override
        public boolean equals(final Object obj) {
            return this == obj || obj instanceof PlayerMove && super.equals(obj);
        }

        @Override
        public boolean isPlayerMove() {
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
            builder.setPiece(this.movePiece.movePiece(this));
            for (EnemyPiece enemyPiece : this.board.getEnemyPieces()) {
                if (!enemyPieces.contains(enemyPiece)) {
                    builder.setPiece(enemyPiece);
                }
            }
            for (EnemyPiece enemyPiece : enemyPieces) {
                if (enemyPiece.isCurrentlyNimble()) {
                    builder.setPiece(enemyPiece.nimbledPiece(this));
                } else if (enemyPiece instanceof Beast) {
                    Random rand = new Random();
                    int r = rand.nextInt(5);
                    int c = rand.nextInt(5);
                    while ((r == this.destinationRow && c == this.destinationCol)
                            || (this.board.getTile(r, c).isOccupied())) {
                        r = rand.nextInt(5);
                        c = rand.nextInt(5);
                    }
                    builder.setPiece(new Infected(r, c, enemyPiece.getTurn()));
                }else if (enemyPiece instanceof Zombie) {
                    Zombie zombie = (Zombie) enemyPiece;
                    zombie.triggerEffect(builder);
                }else if (enemyPiece instanceof Infected) {
                    Infected infected = (Infected) enemyPiece;
                    infected.triggerEffect(builder);
                }
            }
            return builder.build();
        }
    }

    public static class NimbleMove extends EnemyMove {
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
    // final Card chosenCard,
    // final int currentCor,
    // final int destinationCor) {
    // for (MiniMove move : chosenCard.legalMoves(board, board.getPlayerPiece())) {
    // if (move.getCurrentCor() == currentCor && move.getDestinationCor() ==
    // destinationCor)
    // return move;
    // }
    // return null;
    // }
    // }
}
