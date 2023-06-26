package com.chess.engine.minigame.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.Archer;
import com.chess.engine.minigame.pieces.enemy.Beast;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.enemy.Infected;
import com.chess.engine.minigame.pieces.enemy.Shaman;
import com.chess.engine.minigame.pieces.enemy.Zombie;
import com.chess.engine.minigame.pieces.enemy.Swordman;
import com.chess.engine.minigame.pieces.player.Babarian;
import com.chess.engine.minigame.pieces.player.PlayerPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class MiniBoard {
    private final List<MiniTile> gameBoard;
    private final Collection<EnemyPiece> enemyPieces;
    private final PlayerPiece playerPiece;

    private MiniBoard(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        enemyPieces = findEnemyPiece(gameBoard);
        playerPiece = findPlayerPiece(gameBoard);
        for (EnemyPiece enemyPiece : enemyPieces) {
            enemyPiece.triggerImmune(this);
        }
    }

    public MiniTile getTile(final int row, final int col) {
        return gameBoard.get(row * MiniBoardUtils.NUM_TILE_PER_ROW + col);
    }

    public List<MiniTile> getGameBoard() {
        return gameBoard;
    }

    public Collection<EnemyPiece> getEnemyPieces() {
        return enemyPieces;
    }

    public PlayerPiece getPlayerPiece() {
        return playerPiece;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                final String tileString = this.gameBoard.get(r * 5 + c).toString();
                builder.append(String.format("%3s", tileString));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static List<MiniTile> createGameBoard(final Builder builder) {
        List<MiniTile> tiles = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                tiles.add(MiniTile.createTile(r, c, builder.boardConfig.get(r * MiniBoardUtils.NUM_TILE_PER_ROW + c),
                        (builder.blightMap.get(r * MiniBoardUtils.NUM_TILE_PER_ROW + c) != null)));
            }
        }
        return tiles;
    }

    public static MiniBoard createStandardBoard(final int floor, final PieceType pieceType) {
        final Builder builder = new Builder();

        if (pieceType == PieceType.BABARIAN) {
            builder.setPiece(new Babarian(4, 2));
        } else if (pieceType == PieceType.TEMPLAR) {
            // Do later
        }

        generateFloor(floor, builder);

        return builder.build();
    }

    private static void generateFloor(final int floor, final Builder builder) {
        int shCount = 0;
        int difficulty = 2 + 2 * floor;
        Random rand = new Random();
        while (difficulty > 0) {
            int x = rand.nextInt(1, Math.min(7, difficulty + 1));
            int r = rand.nextInt(3);
            int c = rand.nextInt(5);
            while (builder.boardConfig.get(r * 5 + c) != null) {
                r = rand.nextInt(3);
                c = rand.nextInt(5);
            }
            if (difficulty - x >= 0) {
                if (x == 1) {
                    builder.setPiece(new Beast(r, c, 0));
                    difficulty -= 1;
                } else if (x == 2) {
                    builder.setPiece(new Zombie(r, c, 0));
                    difficulty -= 2;
                } else if (x == 3) {
                    int tmp = rand.nextInt(2);
                    if (tmp == 0) {
                        builder.setPiece(new Swordman(r, c, false, false, 0));
                        difficulty -= 3;
                    } else if (tmp == 1) {
                        builder.setPiece(new Archer(r, c, false, false, 0));
                        difficulty -= 3;
                    }
                } else if (x == 4) {
                    int tmp = rand.nextInt(2);
                    if (tmp == 0) {
                        builder.setPiece(new Swordman(r, c, true, true, 0));
                        difficulty -= 4;
                    } else if (tmp == 1) {
                        builder.setPiece(new Archer(r, c, true, true, 0));
                        difficulty -= 4;
                    }
                } else if (x == 5) {
                    if (shCount == 0) {
                        builder.setPiece(new Shaman(r, c, false, false, 0));
                        difficulty -= 5;
                        shCount++;
                    }
                } else if (x == 6) {
                    if (shCount == 0) {
                        builder.setPiece(new Shaman(r, c, true, true, 0));
                        difficulty -= 6;
                        shCount++;
                    }
                }
            }
        }
    }

    public EnemyPiece getNotMovedPiece(final int turn) {
        for (EnemyPiece enemyPiece : this.enemyPieces) {
            if (enemyPiece.getTurn() < turn)
                return enemyPiece;
        }
        return null;
    }

    private static Collection<EnemyPiece> findEnemyPiece(final List<MiniTile> gameBoard) {
        final List<EnemyPiece> activePieces = new ArrayList<>();
        for (MiniTile tile : gameBoard) {
            if (tile.isOccupied()) {
                MiniPiece piece = tile.getPiece();
                if (piece instanceof EnemyPiece) {
                    activePieces.add((EnemyPiece) piece);
                }

            }
        }
        return activePieces;
    }

    private static PlayerPiece findPlayerPiece(final List<MiniTile> gameBoard) {
        for (MiniTile tile : gameBoard) {
            if (tile.isOccupied()) {
                MiniPiece piece = tile.getPiece();
                if (piece instanceof PlayerPiece) {
                    return (PlayerPiece) piece;
                }

            }
        }
        return null;
    }

    public int calculateDamage(final int row, final int col) {
        int res = 0;
        for (EnemyPiece enemyPiece : enemyPieces) {
            res += enemyPiece.calculateDmg(this, row, col);
        }
        if (this.getTile(row, col).isBlighted())
            res++;
        return res;
    }

    public static class Builder {
        Map<Integer, MiniPiece> boardConfig;
        Map<Integer, Boolean> blightMap;

        public Builder() {
            this.boardConfig = new HashMap<>();
            this.blightMap = new HashMap<>();
        }

        public Builder setBlight(final int cor) {
            this.blightMap.put(cor, true);
            return this;
        }

        public Builder setPiece(final MiniPiece piece) {
            this.boardConfig.put(piece.getCorID(), piece);
            return this;
        }

        public MiniBoard build() {
            return new MiniBoard(this);
        }
    }
}
