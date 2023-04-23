package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Party;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackingMove;
import com.chess.engine.board.Move.NeutralMove;

public class Rook extends Piece {

    private static final int[][] CANDIDATE_MOVE_SET = {
            { -1, 0 }, { 0, -1 }, { -1, 0 }, { 1, 0 }
    };

    public Rook(int row, int col, Party pieceParty) {
        super(row, col, pieceParty);
    }

    @Override
    public Collection<Move> legalMoves(final Board board) {
        int r, c;
        List<Move> legalMovesList = new ArrayList<>();

        for (final int[] moveSet : CANDIDATE_MOVE_SET) {
            r = this.row + moveSet[0];
            c = this.col + moveSet[1];
            while (BoardUtils.isCorValid(r, c)) {
                final Tile destinationTile = board.getTile(r, c);

                if (!destinationTile.isOccupied()) {
                    legalMovesList.add(new NeutralMove(board, this, r, c));
                } else {
                    final Piece pieceAtDestination = destinationTile.getPiece();
                    final Party destinationParty = pieceAtDestination.getPieceParty();

                    if (this.pieceParty != destinationParty) {
                        legalMovesList.add(new AttackingMove(board, this, r, c, pieceAtDestination));
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
