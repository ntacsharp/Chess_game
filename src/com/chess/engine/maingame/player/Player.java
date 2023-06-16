package com.chess.engine.maingame.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chess.engine.maingame.Party;
import com.chess.engine.maingame.board.Board;
import com.chess.engine.maingame.board.Move;
import com.chess.engine.maingame.pieces.King;
import com.chess.engine.maingame.pieces.Piece;

public abstract class Player {
    protected final Board board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    protected final Collection<Move> castleMoves;

    private final boolean isInCheck;

    Player(Board board, Collection<Move> playerMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.king = findKing();
        this.isInCheck = !Player.checkAttackOnTile(this.king.getCorID(), opponentMoves).isEmpty();
        this.castleMoves = this.legalCastleMoves(playerMoves, opponentMoves);
        this.legalMoves = Stream
                .concat(playerMoves.stream(), this.castleMoves.stream())
                .collect(Collectors.toList());
    }

    public King getKing() {
        return king;
    }

    public static Collection<Move> checkAttackOnTile(int cor, Collection<Move> opponentMoves) {
        final List<Move> attackOnTileList = new ArrayList<>();
        for (final Move move : opponentMoves) {
            if (cor == move.getDestinationCor()) {
                attackOnTileList.add(move);
            }
        }
        return attackOnTileList;
    }

    private King findKing() {
        for (final Piece piece : activePiecesList()) {
            if (piece.isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("Bug happened!");
    }

    protected boolean haveEscapeMoves() {
        for (Move move : this.legalMoves) {
            final MoveTrans transition = moveTrans(move, false);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public Collection<Move> getCastleMoves() {
        return this.castleMoves;
    }

    public boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isChecked() {
        return this.isInCheck;
    }

    public boolean isCheckmated() {
        return (this.isInCheck && !haveEscapeMoves());
    }

    public boolean isStalemated() {
        return (!this.isInCheck && !haveEscapeMoves());
    }

    public MoveTrans moveTrans(Move move, boolean isRealMove) {
        if (!this.isMoveLegal(move)) {
            return new MoveTrans(this.board, MoveStatus.ILLEGAL);
        }
        final Board transBoard = move.execute(isRealMove);

        final Collection<Move> attackOnTileList = Player.checkAttackOnTile(
                transBoard.getCurrentPlayer().getOpponent().getKing().getCorID(),
                transBoard.getCurrentPlayer().getLegalMoves());

        if (!attackOnTileList.isEmpty()) {
            return new MoveTrans(this.board, MoveStatus.PLAYER_STILL_CHECKED);
        }

        return new MoveTrans(transBoard, MoveStatus.DONE);
    }

    public abstract Collection<Piece> activePiecesList();

    public abstract Collection<Move> legalCastleMoves(Collection<Move> playerMoves, Collection<Move> opponentMoves);

    public abstract Party getParty();

    public abstract Player getOpponent();
}
