package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Party;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        super(board, playerMoves, opponentMoves);
    }

    @Override
    public Collection<Piece> activePiecesList() {
        return this.board.getBlackPieces();
    }

    @Override
    public Party getParty() {
        return Party.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    public Collection<Move> legalCastleMoves(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        final List<Move> castleMoves = new ArrayList<>();

        if (this.getKing().isFirstMove() && !this.isChecked()) {
            // King_side_castle
            if (this.board.getTile(0, 0).isOccupied()) {
                final Piece rookPiece = this.board.getTile(0, 0).getPiece();
                if (rookPiece.isRook() && rookPiece.isFirstMove()) {
                    if (!this.board.getTile(0, 1).isOccupied()
                            && !this.board.getTile(0, 2).isOccupied()
                            && !this.board.getTile(0, 3).isOccupied()) {
                        if (Player.checkAttackOnTile(1, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(2, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(3, opponentMoves).isEmpty()) {
                            castleMoves.add(new Move.KingSideCastle(board, this.king, 0, 6, rookPiece));
                        }
                    }
                }
            }

            // Queen_side_castle
            if (this.board.getTile(0, 7).isOccupied()) {
                final Piece rookPiece = this.board.getTile(0, 7).getPiece();
                if (rookPiece.isRook() && rookPiece.isFirstMove()) {
                    if (!this.board.getTile(0, 5).isOccupied() && !this.board.getTile(0, 6).isOccupied()) {
                        if (Player.checkAttackOnTile(5, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(6, opponentMoves).isEmpty()) {
                            castleMoves.add(new Move.QueenSideCastle(board, this.king, 0, 2, rookPiece));
                        }
                    }
                }
            }
        }
        return castleMoves;
    }
}
