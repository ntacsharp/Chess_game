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

public class Knight extends Piece {

    public Knight(final int row, final int col, final Party pieceParty, final boolean isFirstMove) {
        super(row, col, pieceParty, PieceType.KNIGHT, isFirstMove);
    }

    public Collection<Move> legalMoves(final Board board) {
        final int[][] CANDIDATE_MOVE_SET = {
                { 2, 1 }, { 2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }, { -2, 1 }, { -2, -1 }
        };
        int r, c;
        List<Move> legalMovesList = new ArrayList<>();

        for (int[] moveSet : CANDIDATE_MOVE_SET) {
            r = moveSet[0] + this.row;
            c = moveSet[1] + this.col;

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
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getDestinationRow(), move.getDestinationCol(), move.getMovePiece().getPieceParty(), false);
    }
}
