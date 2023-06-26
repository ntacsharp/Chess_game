package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.EnemyMove;

public class Swordman extends EnemyPiece {
    private final int[][] RANGE = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public Swordman(final int row, final int col, final boolean nimble, final boolean isCurrentlyNimble,
            final int turn) {
        super(row, col, PieceType.SWORDMAN, nimble, isCurrentlyNimble, turn);
    }

    @Override
    public MiniBoard move(MiniBoard board) {
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
        }
        tmp = rand.nextInt(possibleMove.size());
        r = this.row + possibleMove.get(tmp)[0];
        c = this.col + possibleMove.get(tmp)[1];
        MiniMove move = new EnemyMove(board, this, r, c);
        return move.execute();
    }

    @Override
    public void triggerImmune(final MiniBoard board) {
        int r = board.getPlayerPiece().getRow();
        int c = board.getPlayerPiece().getCol();
        if (Math.abs(r - this.row) + Math.abs(c - this.col) == 1)
            this.immune = true;
        else
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
    public Swordman movePiece(MiniMove move) {
        this.row = move.getDestinationRow();
        this.col = move.getDestinationCol();
        this.isCurrentlyNimble = this.isNimble();
        this.turn++;
        return this;
        // return new Swordman(move.getDestinationRow(), move.getDestinationCol(),
        // this.isNimble(), this.isNimble(),
        // this.getTurn() + 1);
    }

    @Override
    public Swordman nimbledPiece(final int row, final int col) {
        this.row = row;
        this.col = col;
        this.isCurrentlyNimble = false;
        return this;
        // return new Swordman(row, col, this.isNimble(), false, this.getTurn());
    }

    @Override
    public boolean canAttactk(final int r, final int c) {
        if (this.getRange().contains(r * 5 + c))
            return true;
        return false;
    }

    @Override
    public List<String> getInformation() {
        List<String> res = new ArrayList<>();
        res.add("Close-ranged,");
        res.add("invulnerable if player is on cardinally adjacent tile!");
        return res;
    }

    @Override
    public List<Integer> getRange() {
        List<Integer> res = new ArrayList<>();
        for (int[] range : this.RANGE) {
            int r = this.row + range[0];
            int c = this.col + range[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                res.add(r * 5 + c);
            }
        }
        return res;
    }
}
