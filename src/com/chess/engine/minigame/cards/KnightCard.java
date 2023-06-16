package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.board.MiniMove.AttackingMove;
import com.chess.engine.minigame.board.MiniMove.NeutralMove;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class KnightCard extends Card {

    public static final int[][] CANDIDATE_MOVE_SET = {
            { 2, 1 }, { 2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { -2, 1 }, { -2, -1 }
    };

    public KnightCard(final int cardID, final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip) {
        super(cardID, crossAttack, diagonalAttack, shield, catnip, CardType.KNIGHT);
    }

    @Override
    public List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece playerPiece) {
        int r, c;
        List<MiniMove> legalMovesList = new ArrayList<>();
        for (int[] moveSet : CANDIDATE_MOVE_SET) {
            r = moveSet[0] + playerPiece.getRow();
            c = moveSet[1] + playerPiece.getCol();

            if (MiniBoardUtils.isCorValid(r, c)) {
                final MiniTile destinationTile = board.getTile(r, c);

                if (!destinationTile.isOccupied()) {
                    legalMovesList.add(new NeutralMove(board, playerPiece, r, c));
                } else {
                    final MiniPiece pieceAtDestination = destinationTile.getPiece();

                    if (pieceAtDestination instanceof EnemyPiece) {
                        EnemyPiece enemyPiece = (EnemyPiece) pieceAtDestination;
                        if (!enemyPiece.isImmune())
                            legalMovesList.add(new AttackingMove(board, playerPiece, r, c, enemyPiece));
                    }
                }
            }
        }
        return legalMovesList;
    }
}