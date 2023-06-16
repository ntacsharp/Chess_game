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

public class PawnCard extends Card {

    public static final int[][] ATTACKING_MOVE_SET = {
            { 1, -1 }, { 1, 1 }
    };
    public static final int[] NORMAL_MOVE = { 1, 0 };

    public PawnCard(final int cardID, final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip) {
        super(cardID, crossAttack, diagonalAttack, shield, catnip, CardType.PAWN);
    }

    @Override
    public List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece playerPiece) {
        int r, c;
        List<MiniMove> legalMovesList = new ArrayList<>();

        r = playerPiece.getRow() + NORMAL_MOVE[0];
        c = playerPiece.getCol() + NORMAL_MOVE[1];
        if (MiniBoardUtils.isCorValid(r, c)) {
            MiniTile destinationTile = board.getTile(r, c);
            if (!destinationTile.isOccupied())
                legalMovesList.add(new NeutralMove(board, playerPiece, r, c));
        }

        for (final int[] moveSet : ATTACKING_MOVE_SET) {
            r = playerPiece.getRow() + moveSet[0];
            c = playerPiece.getCol() + moveSet[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                MiniTile destinationTile = board.getTile(r, c);
                if (destinationTile.isOccupied()) {
                    final MiniPiece pieceAtDestination = destinationTile.getPiece();
                    if (pieceAtDestination instanceof EnemyPiece) {
                        final EnemyPiece enemyPiece = (EnemyPiece) pieceAtDestination;
                        if (!enemyPiece.isImmune())
                            legalMovesList.add(new AttackingMove(board, playerPiece, r, c, enemyPiece));
                    }
                }
            }
        }
        return legalMovesList;
    }
}