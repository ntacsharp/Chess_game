package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import com.chess.engine.Party;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackingMove;
import com.chess.engine.board.Move.EnPassant;
import com.chess.engine.board.Move.NeutralMove;
import com.chess.engine.board.Move.PawnJump;

public class Pawn extends Piece {

    private Piece promoteToPiece = null;

    public Pawn(final int row, final int col, final Party pieceParty, final boolean isFirstMove) {
        super(row, col, pieceParty, PieceType.PAWN, isFirstMove);
    }

    @Override
    public Collection<Move> legalMoves(Board board) {
        final int[][] ATTACKING_MOVE_SET = {
                { 1, -1 }, { 1, 1 }
        };
        final int[] JUMP_MOVE = { 2, 0 };
        final int[] NORMAL_MOVE = { 1, 0 };
        int r1, c, r2;
        List<Move> legalMovesList = new ArrayList<>();

        r1 = this.row + this.getPieceParty().getDirecion() * NORMAL_MOVE[0];
        r2 = this.row + this.getPieceParty().getDirecion() * JUMP_MOVE[0];
        c = this.col;
        final Tile normalTile = board.getTile(r1, c);
        final Tile startingTile = board.getTile(r1, c);

        if (BoardUtils.isCorValid(r1, c)) {
            if (!normalTile.isOccupied()) {
                legalMovesList.add(new NeutralMove(board, this, r1, c));
            }
        }

        if (this.isFirstMove() && BoardUtils.isCorValid(r2, c)) {
            if (!normalTile.isOccupied() && !startingTile.isOccupied()) {
                legalMovesList.add(new PawnJump(board, this, r2, c));
            }
        }

        for (int[] moveSet : ATTACKING_MOVE_SET) {
            c = moveSet[1] + this.col;

            if (BoardUtils.isCorValid(r1, c)) {
                final Tile destinationTile = board.getTile(r1, c);

                if (destinationTile.isOccupied()) {
                    final Piece pieceAtDestination = destinationTile.getPiece();
                    final Party destinationParty = pieceAtDestination.getPieceParty();

                    if (this.pieceParty != destinationParty) {
                        legalMovesList.add(new AttackingMove(board, this, r1, c, pieceAtDestination));
                    }
                } else {
                    final Tile nextTile = board.getTile(this.row, c);
                    if (nextTile.isOccupied()) {
                        final Piece nextPiece = nextTile.getPiece();
                        if (nextPiece.equals(board.getEnPassantPawn())) {
                            legalMovesList.add(new EnPassant(board, this, r1, c, nextPiece));
                        }
                    }
                }
            }
        }

        return legalMovesList;
    }

    public Piece getPromoteToPiece() {
        return promoteToPiece;
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationRow(), move.getDestinationCol(), move.getMovePiece().getPieceParty(), false);
    }

    public Piece promote(Move move) {
        // NEED_ANOTHER_WAY_TO_CHOOSE_PIECE_HERE
        final String[] options = { "Knight", "Bishop", "Rook", "Queen" };
        var promotionPiece = JOptionPane.showOptionDialog(null, "Choose a piece!", "Promote to:", 0, 3, null, options,
                options[3]);
        switch (promotionPiece) {
            case 0:
                promoteToPiece = new Knight(move.getDestinationRow(), move.getDestinationCol(),
                        move.getMovePiece().getPieceParty(), false);
                break;
            case 1:
                promoteToPiece = new Bishop(move.getDestinationRow(), move.getDestinationCol(),
                        move.getMovePiece().getPieceParty(), false);
                break;
            case 2:
                promoteToPiece = new Rook(move.getDestinationRow(), move.getDestinationCol(),
                        move.getMovePiece().getPieceParty(), false);
                break;
            default:
                promoteToPiece = new Queen(move.getDestinationRow(), move.getDestinationCol(),
                        move.getMovePiece().getPieceParty(), false);
                break;
        }
        return promoteToPiece;
    }
}
