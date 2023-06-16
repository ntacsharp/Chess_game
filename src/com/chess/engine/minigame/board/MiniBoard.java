package com.chess.engine.minigame.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.chess.engine.maingame.board.BoardUtils;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
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

    private static List<MiniTile> createGameBoard(final Builder builder) {
        List<MiniTile> tiles = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                tiles.add(MiniTile.createTile(r, c, builder.boardConfig.get(r * MiniBoardUtils.NUM_TILE_PER_ROW + c)));
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

        return null;
    }

    private static void generateFloor(final int floor, final Builder builder) {
        int difficulty = 4, r, c, tmp;
        Random rand = new Random();
        List<EnemyPiece> generatedEnemies = new ArrayList<EnemyPiece>();
        r = rand.nextInt(4);
        c = rand.nextInt(5);
        generatedEnemies.add(new EnemyPiece(r, c, rand.nextInt(2), false, false, false, false, false, false, false));
        while (existedCor(generatedEnemies, r, c)) {
            r = rand.nextInt(4);
            c = rand.nextInt(5);
        }
        generatedEnemies.add(new EnemyPiece(r, c, rand.nextInt(2), false, false, false, false, false, false, false));
        while (existedCor(generatedEnemies, r, c)) {
            r = rand.nextInt(4);
            c = rand.nextInt(5);
        }
        tmp = rand.nextInt(2);
        generatedEnemies
                .add(new EnemyPiece(r, c, rand.nextInt(2), tmp == 0, tmp == 1, false, false, false, false, false));
        while (difficulty < 2 + floor * 2) {
            tmp = rand.nextInt(3);
            while ((tmp == 0 && generatedEnemies.size() == 20) || (tmp == 1 && allFullPower(generatedEnemies))
                    || (tmp == 2 && allFullRange(generatedEnemies))) {
                if ((tmp == 0 && generatedEnemies.size() == 20) && (tmp == 1 && allFullPower(generatedEnemies))
                        && (tmp == 2 && allFullRange(generatedEnemies))) {
                    throw new RuntimeException("Bugged :(");
                }
                tmp = rand.nextInt(3);
            }
            if (tmp == 0) {
                r = rand.nextInt(4);
                c = rand.nextInt(5);
                generatedEnemies
                        .add(new EnemyPiece(r, c, rand.nextInt(2), false, false, false, false, false, false, false));
            } else if (tmp == 1) {
                tmp = rand.nextInt(generatedEnemies.size());
                EnemyPiece enemyPiece = generatedEnemies.get(tmp);
                while (enemyPiece.isFullPower())
                    tmp = rand.nextInt(generatedEnemies.size());
                enemyPiece.grantRandomPower();
                generatedEnemies.set(tmp, enemyPiece);
            } else {
                tmp = rand.nextInt(generatedEnemies.size());
                EnemyPiece enemyPiece = generatedEnemies.get(tmp);
                while (enemyPiece.isFullRange())
                    tmp = rand.nextInt(generatedEnemies.size());
                enemyPiece.grantMoreRange();
                generatedEnemies.set(tmp, enemyPiece);
            }
        }
    }

    private static boolean existedCor(final List<EnemyPiece> list, final int r, final int c) {
        for (EnemyPiece enemyPiece : list) {
            if (r == enemyPiece.getRow() && c == enemyPiece.getCol())
                return true;
        }
        return false;
    }

    private static boolean allFullPower(final List<EnemyPiece> list) {
        for (EnemyPiece enemyPiece : list) {
            if (!enemyPiece.isFullPower())
                return false;
        }
        return true;
    }

    private static boolean allFullRange(final List<EnemyPiece> list) {
        for (EnemyPiece enemyPiece : list) {
            if (!enemyPiece.isFullRange())
                return false;
        }
        return true;
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

    public static class Builder {
        Map<Integer, MiniPiece> boardConfig;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final MiniPiece piece) {
            this.boardConfig.put(0, piece);
            return this;
        }

        public MiniBoard build() {
            return new MiniBoard(this);
        }
    }
}
