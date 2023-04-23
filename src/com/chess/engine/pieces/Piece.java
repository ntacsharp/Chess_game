package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Party;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

public abstract class Piece {
    
    protected final int row;
    protected final int col;
    protected final boolean isFirstMove;

    protected final Party pieceParty;

    public Piece(final int row, final int col, final Party pieceParty){
        this.row = row;
        this.col = col;
        this.pieceParty = pieceParty;
        this.isFirstMove = true;
    }

    public int getCorID(){
        return this.row * BoardUtils.NUM_TILE_PER_ROW + this.col;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public Party getPieceParty() {
        return this.pieceParty;
    }

    public abstract Collection<Move> legalMoves(final Board board);
}
