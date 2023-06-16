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

public class RookCard extends Card {

    public static final int[][] CANDIDATE_MOVE_SET = {
            { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 }
    };

    public RookCard(final int cardID, final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip) {
        super(cardID, crossAttack, diagonalAttack, shield, catnip, CardType.ROOK);
    }

    @Override
    public List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece playerPiece){
        int r, c;
        List<MiniMove> legalMovesList = new ArrayList<>();
        for (final int[] moveSet : CANDIDATE_MOVE_SET) {
            r = playerPiece.getRow() + moveSet[0];
            c = playerPiece.getCol() + moveSet[1];
            while (MiniBoardUtils.isCorValid(r, c)) {
                final MiniTile destinationTile = board.getTile(r, c);
                if (!destinationTile.isOccupied()) {
                    legalMovesList.add(new NeutralMove(board, playerPiece, r, c));
                } else {
                    final MiniPiece pieceAtDestination = destinationTile.getPiece();
                    if (pieceAtDestination instanceof PlayerPiece) {
                        throw new RuntimeException("Buged :(");
                    } else {
                        final EnemyPiece enemyPiece = (EnemyPiece) pieceAtDestination;
                        if (!enemyPiece.isImmune())
                            legalMovesList.add(new AttackingMove(board, playerPiece, r, c, enemyPiece));
                    }
                    break;
                }
                r += moveSet[0];
                c += moveSet[1];
            }
        }
        return legalMovesList;
    }
}
