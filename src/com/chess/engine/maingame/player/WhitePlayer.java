package com.chess.engine.maingame.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.maingame.Party;
import com.chess.engine.maingame.board.Board;
import com.chess.engine.maingame.board.Move;
import com.chess.engine.maingame.pieces.Piece;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        super(board, playerMoves, opponentMoves);
    }

    @Override
    public Collection<Piece> activePiecesList() {
        return this.board.getWhitePieces();
    }

    @Override
    public Party getParty() {
        return Party.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public Collection<Move> legalCastleMoves(Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        final List<Move> castleMoves = new ArrayList<>();

        if (this.getKing().isFirstMove() && !this.isChecked()) {
            // King_side_castle
            if (this.board.getTile(7, 7).isOccupied()) {
                final Piece rookPiece = this.board.getTile(7, 7).getPiece();
                if (rookPiece.isRook() && rookPiece.isFirstMove()) {
                    if (!this.board.getTile(7, 5).isOccupied() && !this.board.getTile(7, 6).isOccupied()) {
                        if (Player.checkAttackOnTile(61, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(62, opponentMoves).isEmpty()) {
                            castleMoves.add(new Move.KingSideCastle(board, this.king, 7, 6, rookPiece));
                        }
                    }
                }
            }

            // Queen_side_castle
            if (this.board.getTile(7, 0).isOccupied()) {
                final Piece rookPiece = this.board.getTile(7, 0).getPiece();
                if (rookPiece.isRook() && rookPiece.isFirstMove()) {
                    if (!this.board.getTile(7, 1).isOccupied()
                            && !this.board.getTile(7, 2).isOccupied()
                            && !this.board.getTile(7, 3).isOccupied()) {
                        if (Player.checkAttackOnTile(57, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(58, opponentMoves).isEmpty()
                                && Player.checkAttackOnTile(59, opponentMoves).isEmpty()) {
                            castleMoves.add(new Move.QueenSideCastle(board, this.king, 7, 2, rookPiece));
                        }
                    }
                }
            }
        }

        return castleMoves;
    }

}
