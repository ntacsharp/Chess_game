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
        protected final EnemyPiece attackedPiece;

        public PlayerMove(MiniBoard board, MiniPiece movePiece, final int destinationRow,
                final int destinationCol, final EnemyPiece attackedPiece) {
            super(board, movePiece, destinationRow, destinationCol);
            this.attackedPiece = attackedPiece;
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
            if (attackedPiece != null) {
                for (EnemyPiece enemyPiece : this.board.getEnemyPieces()) {
                    if (!enemyPiece.equals(attackedPiece)) {
                        builder.setPiece(enemyPiece);
                    }
                }
                if (attackedPiece.isCurrentlyNimble()) {
                    if (this.movePiece.getRow() == this.attackedPiece.getRow()) {
                        if (this.movePiece.getCol() == this.attackedPiece.getCol()) {
                            throw new RuntimeException("Bugged!");
                        } else if (this.movePiece.getCol() > this.attackedPiece.getCol()) {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow(), attackedPiece.getCol() - 1)
                                    && !this.board.getTile(attackedPiece.getRow(), attackedPiece.getCol() - 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow(), attackedPiece.getCol() - 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        } else {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow(), attackedPiece.getCol() + 1)
                                    && !this.board.getTile(attackedPiece.getRow(), attackedPiece.getCol() + 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow(), attackedPiece.getCol() + 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        }
                    } else if (this.movePiece.getRow() > this.attackedPiece.getRow()) {
                        if (this.movePiece.getCol() == this.attackedPiece.getCol()) {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() - 1, attackedPiece.getCol() )
                                    && !this.board.getTile(attackedPiece.getRow() - 1, attackedPiece.getCol())
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() - 1, attackedPiece.getCol()));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        } else if (this.movePiece.getCol() > this.attackedPiece.getCol()) {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() - 1, attackedPiece.getCol() - 1)
                                    && !this.board.getTile(attackedPiece.getRow() - 1, attackedPiece.getCol() - 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() - 1, attackedPiece.getCol() - 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        } else {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() - 1, attackedPiece.getCol() + 1)
                                    && !this.board.getTile(attackedPiece.getRow() - 1, attackedPiece.getCol() + 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() - 1, attackedPiece.getCol() + 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        }
                    } else {
                        if (this.movePiece.getCol() == this.attackedPiece.getCol()) {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() + 1, attackedPiece.getCol() )
                                    && !this.board.getTile(attackedPiece.getRow() + 1, attackedPiece.getCol())
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() + 1, attackedPiece.getCol()));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        } else if (this.movePiece.getCol() > this.attackedPiece.getCol()) {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() + 1, attackedPiece.getCol() - 1)
                                    && !this.board.getTile(attackedPiece.getRow() + 1, attackedPiece.getCol() - 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() + 1, attackedPiece.getCol() - 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        } else {
                            if (MiniBoardUtils.isCorValid(attackedPiece.getRow() + 1, attackedPiece.getCol() + 1)
                                    && !this.board.getTile(attackedPiece.getRow() + 1, attackedPiece.getCol() + 1)
                                            .isOccupied()) {
                                builder.setPiece(
                                        attackedPiece.nimbledPiece(attackedPiece.getRow() + 1, attackedPiece.getCol() + 1));
                            } else {
                                List<Integer> newR = new ArrayList<>();
                                List<Integer> newC = new ArrayList<>();
                                for (int[] move : attackedPiece.MOVE_SET) {
                                    int r = attackedPiece.getRow() + move[0];
                                    int c = attackedPiece.getCol() + move[1];
                                    if (MiniBoardUtils.isCorValid(r, c) && !this.board.getTile(r, c).isOccupied()) {
                                        newR.add(r);
                                        newC.add(c);
                                    }
                                }
                                if (!newR.isEmpty()) {
                                    Random rand = new Random();
                                    int tmp = rand.nextInt(newR.size());
                                    builder.setPiece(attackedPiece.nimbledPiece(newR.get(tmp), newC.get(tmp)));
                                }
                            }
                        }
                    }
                } else if (attackedPiece instanceof Beast) {
                    Random rand = new Random();
                    int r = rand.nextInt(5);
                    int c = rand.nextInt(5);
                    while (this.board.getTile(r, c).isOccupied()) {
                        r = rand.nextInt(5);
                        c = rand.nextInt(5);
                    }
                    builder.setPiece(new Infected(r, c, attackedPiece.getTurn()));
                }
                builder.setPiece(this.movePiece.movePiece(this));
                return builder.build();
            } else {
                for (EnemyPiece enemyPiece : this.board.getEnemyPieces()) {
                    builder.setPiece(enemyPiece);
                }
                builder.setPiece(this.movePiece.movePiece(this));
                return builder.build();
            }
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
