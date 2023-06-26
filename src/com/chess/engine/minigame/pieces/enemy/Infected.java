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

    public void triggerEffect(final Builder builder) {
        builder.setBlight(this.row * MiniBoardUtils.NUM_TILE_PER_ROW + this.col);
    }

    @Override
    public MiniBoard move(final MiniBoard board) {
        Random rand = new Random();
        int r, c;
        List<int[]> possibleMove = new ArrayList<int[]>();
        int tmp = Integer.MAX_VALUE;
        for (int[] move : this.MOVE_SET) {
            r = this.row + move[0];
            c = this.col + move[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                if (!board.getTile(r, c).isOccupied()) {
                    if (board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c < tmp) {
                        possibleMove.clear();
                        possibleMove.add(move);
                        tmp = board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c;
                    } else if (board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c == tmp) {
                        possibleMove.add(move);
                    }
                }
            }
        }
        tmp = rand.nextInt(possibleMove.size());
        r = this.row + possibleMove.get(tmp)[0];
        c = this.col + possibleMove.get(tmp)[1];
        MiniMove move = new EnemyMove(board, this, r, c);
        return move.execute();
    }

    @Override
    public void triggerImmune(final MiniBoard board) {
        this.immune = false;
    }

    @Override
    public int calculateDmg(final MiniBoard board, final int row, final int col) {
        return 0;
    }

    @Override
    public Infected movePiece(final MiniMove move) {
        this.row = move.getDestinationRow();
        this.col = move.getDestinationCol();
        this.isCurrentlyNimble = this.isNimble();
        this.turn++;
        return this;
        // return new Infected(move.getDestinationRow(), move.getDestinationCol(), this.getTurn() + 1);
    }

    @Override
    public Infected nimbledPiece(final int row, final int col) {
        return null;
    }

    @Override
    public boolean canAttactk(final int r, final int c) {
        return false;
    }

    @Override
    public List<String> getInformation(){
        List<String> res = new ArrayList<>();
        res.add("Weak, blight the tile on death.");
        return res;
    }

    @Override
    public List<Integer> getRange(){
        List<Integer> res = new ArrayList<>();
        return res;
    }
}
