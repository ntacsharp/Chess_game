package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.board.MiniMove.PlayerMove;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class BishopCard extends Card {

    public static final int[][] CANDIDATE_MOVE_SET = {
            { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public BishopCard(final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip) {
        super(crossAttack, diagonalAttack, shield, catnip, CardType.BISHOP);
    }

    @Override
    public List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece playerPiece) {
        int r, c;
        List<MiniMove> legalMovesList = new ArrayList<>();

        for (final int[] moveSet : CANDIDATE_MOVE_SET) {
            r = playerPiece.getRow() + moveSet[0];
            c = playerPiece.getCol() + moveSet[1];
            while (MiniBoardUtils.isCorValid(r, c)) {
                final MiniTile destinationTile = board.getTile(r, c);
                List<EnemyPiece> attackedPieces = new ArrayList<>();
                if (destinationTile.isOccupied() && destinationTile.getPiece() instanceof PlayerPiece) {
                    throw new RuntimeException("Buged :(");
                } else {
                    if (destinationTile.isOccupied()) {
                        final EnemyPiece enemyPiece = (EnemyPiece) destinationTile.getPiece();
                        if (!enemyPiece.isImmune()) {
                            attackedPieces.add(enemyPiece);
                            attackedPieces.addAll(this.getAffectedPieces(board, r, c));
                            legalMovesList.add(new PlayerMove(board, playerPiece, r, c, attackedPieces));
                        }
                    } else {
                        attackedPieces.addAll(this.getAffectedPieces(board, r, c));
                        legalMovesList.add(new PlayerMove(board, playerPiece, r, c, attackedPieces));
                    }
                }
                r += moveSet[0];
                c += moveSet[1];
            }
        }

        return legalMovesList;
    }
}
