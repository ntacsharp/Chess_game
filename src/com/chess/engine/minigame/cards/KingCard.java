package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.board.MiniMove.PlayerMove;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class KingCard extends Card {

    public static final int[][] CANDIDATE_MOVE_SET = {
            { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public KingCard(final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip) {
        super(crossAttack, diagonalAttack, shield, catnip, CardType.KING);
    }

    @Override
    public List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece playerPiece) {
        int r, c;
        List<MiniMove> legalMovesList = new ArrayList<>();
        for (final int[] moveSet : CANDIDATE_MOVE_SET) {
            r = playerPiece.getRow() + moveSet[0];
            c = playerPiece.getCol() + moveSet[1];

            if (MiniBoardUtils.isCorValid(r, c)) {
                final MiniTile destinationTile = board.getTile(r, c);
                if (destinationTile.isOccupied()) {
                    MiniPiece piece = board.getTile(r, c).getPiece();
                    if (piece instanceof PlayerPiece)
                        throw new RuntimeException("Buged :(");
                    EnemyPiece attackedPiece = (EnemyPiece) piece;
                    if (!attackedPiece.isImmune())
                        legalMovesList.add(new PlayerMove(board, playerPiece, r, c, attackedPiece));
                } else {
                    legalMovesList.add(new PlayerMove(board, playerPiece, r, c, null));
                }
            }
        }
        return legalMovesList;
    }
}
