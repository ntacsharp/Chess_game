package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniBoard.Builder;
import com.chess.engine.minigame.board.MiniMove.EnemyMove;

public class Zombie extends EnemyPiece {
    private final int[][] RANGE = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
    };

    public Zombie(final int row, final int col, final int turn) {
        super(row, col, PieceType.ZOMBIE, false, false, turn);
    }

    public void triggerEffect(final Builder builder){
        builder.setBlight(this.row * MiniBoardUtils.NUM_TILE_PER_ROW + this.col);
        for (int[] move : this.MOVE_SET) {
            int r = this.row + move[0];
            int c = this.col + move[1];
            if (MiniBoardUtils.isCorValid(r, c)){
                builder.setBlight(r * MiniBoardUtils.NUM_TILE_PER_ROW + c);
            }
        }
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
        int tmp = Integer.MAX_VALUE;
        if (possibleMove.isEmpty()) {
            for (int[] move : this.MOVE_SET) {
                r = this.row + move[0];
                c = this.col + move[1];
                if (MiniBoardUtils.isCorValid(r, c)) {
                    if (!board.getTile(r, c).isOccupied()) {
                        if(board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c < tmp){
                            possibleMove.clear();
                            possibleMove.add(move);
                            tmp = board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c;
                        }
                        else if(board.getPlayerPiece().getRow() - r + board.getPlayerPiece().getCol() - c == tmp){
                            possibleMove.add(move);
                        }
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
    public void triggerImmune(final MiniBoard board){
        this.immune = false;
    }

    @Override
    public int calculateDmg(final MiniBoard board){
        for (int[] range : this.RANGE) {
            int r = this.row + range[0];
            int c = this.col + range[1];
            if(board.getPlayerPiece().getRow() == r && board.getPlayerPiece().getCol() == c) return 1;
        }
        return 0;
    }

    @Override
    public Zombie movePiece(final MiniMove move) {
        return new Zombie(move.getDestinationRow(), move.getDestinationCol(), this.getTurn() + 1);
    }

    @Override
    public Zombie nimbledPiece(final MiniMove move) {
        return null;
    }

    @Override
    public boolean canAttactk(final int r, final int c){
        for (int[] range : this.RANGE) {
            if(this.row + range[0] == r && this.col + range[1] == c) return true;
        }
        return false;
    }
}