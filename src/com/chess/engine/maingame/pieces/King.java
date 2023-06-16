package com.chess.engine.maingame.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.maingame.Party;
import com.chess.engine.maingame.board.Board;
import com.chess.engine.maingame.board.BoardUtils;
import com.chess.engine.maingame.board.Move;
import com.chess.engine.maingame.board.Tile;
import com.chess.engine.maingame.board.Move.AttackingMove;
import com.chess.engine.maingame.board.Move.NeutralMove;

public class King extends Piece {

    public King(final int row, final int col, final Party pieceParty, final boolean isFirstMove) {
        super(row, col, pieceParty, PieceType.KING, isFirstMove);
    }

    @Override
    public Collection<Move> legalMoves(final Board board) {
        final int[][] CANDIDATE_MOVE_SET = {
                { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };
        int r, c;
        List<Move> legalMovesList = new ArrayList<>();

        for (final int[] moveSet : CANDIDATE_MOVE_SET) {
            r = this.row + moveSet[0];
            c = this.col + moveSet[1];

            if (BoardUtils.isCorValid(r, c)) {
                final Tile destinationTile = board.getTile(r, c);
                if (!destinationTile.isOccupied()) {
                    legalMovesList.add(new NeutralMove(board, this, r, c));
                } else {
                    final Piece pieceAtDestination = destinationTile.getPiece();
                    final Party destinationParty = pieceAtDestination.getPieceParty();

                    if (this.pieceParty != destinationParty) {
                        legalMovesList.add(new AttackingMove(board, this, r, c, pieceAtDestination));
                    }
                }
            }
        }

        return legalMovesList;
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationRow(), move.getDestinationCol(), move.getMovePiece().getPieceParty(), false);
    }
}