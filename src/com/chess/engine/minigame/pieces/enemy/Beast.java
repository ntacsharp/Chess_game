package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.EnemyMove;

public class Beast extends EnemyPiece {
    private final int[][] RANGE = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public Beast(final int row, final int col, final int turn) {
        super(row, col, PieceType.BEAST, false, false, turn);
    }

    @Override
    public MiniBoard move(MiniBoard board) {
        Random rand = new Random();
        int r, c ,pR = board.getPlayerPiece().getRow(), pC = board.getPlayerPiece().getCol();
        List<int[]> possibleMove = new ArrayList<int[]>();
        for (int[] move : this.MOVE_SET) {
            r = this.row + move[0];
            c = this.col + move[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                if (!board.getTile(r, c).isOccupied()) {
                    int r1, c1;
                    for (int[] des : this.RANGE) {
                        r1 = r + des[0];
                        c1 = c + des[1];
                        if (r1 == board.getPlayerPiece().getRow() && c1 == board.getPlayerPiece().getCol()) {
                            possibleMove.add(move);
                        }
                    }
                }
            }
        }
        double tmpMax = 1000000000;
        if (possibleMove.isEmpty()) {
            for (int[] move : this.MOVE_SET) {
                r = this.row + move[0];
                c = this.col + move[1];
                if (MiniBoardUtils.isCorValid(r, c)) {
                    if (!board.getTile(r, c).isOccupied()) {
                        double dist = Math.sqrt((r - pR) * (r - pR) + (c - pC) * (c - pC));
                        if(dist < tmpMax){
                            possibleMove.clear();
                            possibleMove.add(move);
                            tmpMax = dist;
                        } else if(dist == tmpMax){
                            possibleMove.add(move);
                        }
                    }
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
    public void triggerImmune(final MiniBoard board) {
        this.immune = false;
    }

    @Override
    public int calculateDmg(final MiniBoard board, final int row, final int col) {
        for (int[] range : this.RANGE) {
            int r = this.row + range[0];
            int c = this.col + range[1];
            if (row == r && col == c)
                return 1;
        }
        return 0;
    }

    @Override
    public Beast movePiece(MiniMove move) {
        this.row = move.getDestinationRow();
        this.col = move.getDestinationCol();
        this.isCurrentlyNimble = this.isNimble();
        this.turn++;
        return this;
        // return new Beast(move.getDestinationRow(), move.getDestinationCol(), this.getTurn() + 1);
    }

    @Override
    public Beast nimbledPiece(final int row, final int col) {
        return null;
    }

    @Override
    public boolean canAttactk(final int r, final int c){
        if(this.getRange().contains(r * 5 + c)) return true;
        return false;
    }

    @Override
    public List<String> getInformation(){
        List<String> res = new ArrayList<>();
        res.add("Close-ranged unit with no power.");
        return res;
    }

    @Override
    public List<Integer> getRange(){
        List<Integer> res = new ArrayList<>();
        for (int[] range : this.RANGE) {
            int r = this.row + range[0];
            int c = this.col + range[1];
            if(MiniBoardUtils.isCorValid(r, c)){
                res.add(r * 5 + c);
            }
        }
        return res;
    }
}
