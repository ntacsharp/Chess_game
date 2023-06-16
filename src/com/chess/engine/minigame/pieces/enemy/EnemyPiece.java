package com.chess.engine.minigame.pieces.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniMove.NeutralMove;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class EnemyPiece extends MiniPiece {
    private static final int[][][] RANGE_SET = {
            {
                    { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
            },
            {
                    { -2, -2 }, { -2, -1 }, { -2, 0 }, { -2, 1 }, { -2, 2 }, { -1, -2 }, { -1, 2 }, { 0, -2 }, { 0, 2 },
                    { 1, -2 }, { 1, 2 }, { 2, -2 }, { 2, -1 }, { 2, 0 }, { 2, 1 }, { 2, 2 }
            },
            {
                    { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { -2, -2 },
                    { -2, -1 }, { -2, 0 }, { -2, 1 }, { -2, 2 }, { -1, -2 }, { -1, 2 }, { 0, -2 }, { 0, 2 },
                    { 1, -2 }, { 1, 2 }, { 2, -2 }, { 2, -1 }, { 2, 0 }, { 2, 1 }, { 2, 2 }
            },
    };
    private static final int[][] MOVE_SET = {
            { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }
    };

    private int type;
    private List<Boolean> hasPower;
    private boolean immune;

    public EnemyPiece(final int row, final int col, final int type, final boolean brawler,
            final boolean vigilant, final boolean nimble, final boolean webWeaver, final boolean blightWake,
            final boolean blightBlast, final boolean blightHost) {
        super(row, col);
        this.type = type;
        this.hasPower = new ArrayList<Boolean>();
        this.hasPower.add(brawler);
        this.hasPower.add(vigilant);
        this.hasPower.add(nimble);
        this.hasPower.add(webWeaver);
        this.hasPower.add(blightWake);
        this.hasPower.add(blightBlast);
        this.hasPower.add(blightHost);
        this.immune = false;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Boolean> getHasPower() {
        return this.hasPower;
    }

    public Boolean getHasPowerByID(final int id) {
        return this.hasPower.get(id);
    }

    public void setHasPowerByID(final int id, final boolean newVal) {
        this.hasPower.set(id, newVal);
    }

    public boolean isImmune() {
        return this.immune;
    }

    public boolean isFullPower() {
        for (int i = 2; i < 7; i++) {
            boolean power = this.hasPower.get(i);
            if (!power)
                return false;
        }
        if (!this.hasPower.get(0) && !this.hasPower.get(1))
            return false;
        return true;
    }

    public boolean isFullRange() {
        if (this.type < 2)
            return false;
        return true;
    }

    public void grantRandomPower(){
        Random rand = new Random();
        int tmp = rand.nextInt(this.hasPower.size());
        while((tmp == 0 && this.hasPower.get(1)) || (tmp == 1 && this.hasPower.get(0)) || this.hasPower.get(tmp)){
            tmp = rand.nextInt(this.hasPower.size());
        }
        this.setHasPowerByID(tmp, true);
    }

    public void grantMoreRange(){
        this.setType(2);
    }

    public void triggerTrait(final MiniBoard board) {
        // if (this.brawler) {
        // for (int[] pos : RANGE_SET[0]) {
        // if (board.getTile(pos[0], pos[1]).isOccupied()) {
        // MiniPiece piece = board.getTile(pos[0], pos[1]).getPiece();
        // if (piece instanceof PlayerPiece) {
        // this.immune = true;
        // break;
        // }
        // }
        // }
        // }

        // if (this.vigilant) {
        // boolean flag = false;
        // for (int[] pos : RANGE_SET[0]) {
        // if (board.getTile(pos[0], pos[1]).isOccupied()) {
        // MiniPiece piece = board.getTile(pos[0], pos[1]).getPiece();
        // if (piece instanceof PlayerPiece) {
        // flag = true;
        // break;
        // }
        // }
        // }
        // if (!flag)
        // this.immune = true;
        // }
    }

    public void triggerNimble() {

    }

    public MiniMove move(final MiniBoard board) {
        Random rand = new Random();
        int r, c;
        List<int[]> possibleMove = new ArrayList<int[]>();
        for (int[] move : MOVE_SET) {
            r = this.row + move[0];
            c = this.col + move[1];
            if (MiniBoardUtils.isCorValid(r, c)) {
                if (!board.getTile(r, c).isOccupied()) {
                    int r1, c1;
                    for (int[] des : RANGE_SET[this.type]) {
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
            for (int[] move : MOVE_SET) {
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
        return new NeutralMove(board, this, r, c);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof EnemyPiece))
            return false;
        EnemyPiece objPiece = (EnemyPiece) obj;
        if (!super.equals(obj)
                || this.type != objPiece.getType()
                || this.hasPower != objPiece.getHasPower())
            return false;
        return true;
    }

    @Override
    public MiniPiece movePiece(MiniMove move) {
        return new EnemyPiece(move.getDestinationRow(), move.getDestinationRow(), this.getType(),
                this.getHasPowerByID(0), this.getHasPowerByID(1), this.getHasPowerByID(2), this.getHasPowerByID(3),
                this.getHasPowerByID(4), this.getHasPowerByID(5), this.getHasPowerByID(6));
    }
}
