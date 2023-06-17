package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.NeutralMove;

public class Swordman extends EnemyPiece {
    private final int[][] RANGE = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
    };

    public Swordman(int row, int col, boolean nimble, boolean isCurrentlyNimble, int turn) {
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
        if (possibleMove.isEmpty()) {
            for (int[] move : this.MOVE_SET) {
                r = this.row + move[0];
                c = this.col + move[1];
                if (MiniBoardUtils.isCorValid(r, c)) {
                    if (!board.getTile(r, c).isOccupied()) {
                        possibleMove.add(move);
                    }
                }
            }
        }
        int tmp = rand.nextInt(possibleMove.size());
        r = this.row + possibleMove.get(tmp)[0];
        c = this.col + possibleMove.get(tmp)[1];
        MiniMove move = new NeutralMove(board, this, r, c);
        return move.execute();
    }

    @Override
    public Swordman movePiece(MiniMove move) {
        return new Swordman(move.getDestinationRow(), move.getDestinationCol(), this.isNimble(), this.isNimble(),
                this.getTurn() + 1);
    }

    @Override
    public Swordman nimbledPiece(MiniMove move) {
        return new Swordman(move.getDestinationRow(), move.getDestinationCol(), this.isNimble(), false,
                this.getTurn());
    }

}
