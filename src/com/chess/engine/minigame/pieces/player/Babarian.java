package com.chess.engine.minigame.pieces.player;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.board.MiniMove;

public class Babarian extends PlayerPiece{
    public Babarian(final int row, final int col){
        super(row, col, PieceType.BABARIAN);
    }
    @Override
    public List<String> getInformation(){
        List<String> res = new ArrayList<>();
        res.add("Promote whenever you move into top row,");
        res.add("or have 3 Pawn in hand.");
        return res;
    }

    @Override
    public Babarian movePiece(MiniMove move){
        this.row = move.getDestinationRow();
        this.col = move.getDestinationCol();
        return this;
        //return new Babarian(move.getDestinationRow(), move.getDestinationCol());
    }
}
