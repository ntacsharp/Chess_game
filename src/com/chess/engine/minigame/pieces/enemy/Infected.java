package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoard.Builder;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.EnemyMove;

public class Infected extends EnemyPiece {
    public Infected(final int row, final int col, final int turn) {
        super(row, col, PieceType.INFECTED, false, false, turn);
    }

    public void triggerEffect(final Builder builder){
        builder.setBlight(this.row * MiniBoardUtils.NUM_TILE_PER_ROW + this.col);
    }

    @Override
    public MiniBoard move(final MiniBoard board) {
        Random rand = new Random();
        int r, c;
        List<int[]> possibleMove = new ArrayList<int[]>();
        for (int[] move : this.MOVE_SET) {
            r = this.row + move[0];
            c = this.col + move[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                if (!board.getTile(r, c).isOccupied()) {
                    possibleMove.add(move);
                }
            }
        }
        int tmp = rand.nextInt(possibleMove.size());
        r = this.row + possibleMove.get(tmp)[0];
        c = this.col + possibleMove.get(tmp)[1];
        MiniMove move = new EnemyMove(board, this, r, c);
        return move.execute();
    }

    @Override
    public void triggerImmune(final MiniBoard board){
        this.immune = false;
    }

    @Override
    public int calculateDmg(final MiniBoard board){
        return 0;
    }

    @Override
    public Infected movePiece(final MiniMove move) {
        return new Infected(move.getDestinationRow(), move.getDestinationCol(), this.getTurn() + 1);
    }

    @Override
    public Infected nimbledPiece(final MiniMove move) {
        return null;
    }
}
