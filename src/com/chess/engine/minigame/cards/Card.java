package com.chess.engine.minigame.cards;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public abstract class Card {
    protected final int[][] CROSS_COR = {
            { -1, 0 }, { 0, -1 }, { 0, 1 }, { 1, 0 }
    };

    protected final int[][] DIAGONAL_COR = {
            { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 }
    };

    protected final CardType cardType;
    private final List<Boolean> hasPower;

    protected Card(final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip, final CardType cardType) {
        this.hasPower = new ArrayList<Boolean>();
        this.hasPower.add(crossAttack);
        this.hasPower.add(diagonalAttack);
        this.hasPower.add(shield);
        this.hasPower.add(catnip);
        this.cardType = cardType;
    }

    public static Power getPowerByID(int id) {
        for (Power power : Power.values()) {
            if (power.getID() == id)
                return power;
        }
        return null;
    }

    public boolean getHasPower(int id) {
        return this.hasPower.get(id);
    }

    public boolean isFullPower() {
        for (int i = 0; i < 4; i++) {
            if (!this.hasPower.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void setHasPower(int id, boolean newVal) {
        this.hasPower.set(id, newVal);
    }

    public CardType getCardType() {
        return cardType;
    }

    public List<EnemyPiece> getAffectedPieces(final MiniBoard board, final int r, final int c) {
        List<EnemyPiece> res = new ArrayList<>();
        if (this.getHasPower(0)) {
            for (int[] cor : CROSS_COR) {
                int tmpR = r + cor[0];
                int tmpC = c + cor[1];
                if (MiniBoardUtils.isCorValid(tmpR, tmpC)) {
                    if (board.getTile(tmpR, tmpC).isOccupied()) {
                        if (board.getTile(tmpR, tmpC).getPiece() instanceof EnemyPiece) {
                            EnemyPiece enemyPiece = (EnemyPiece) board.getTile(tmpR, tmpC).getPiece();
                            if (!enemyPiece.isImmune())
                                res.add(enemyPiece);
                        }
                    }
                }
            }
        }
        if (this.getHasPower(1)) {
            for (int[] cor : CROSS_COR) {
                int tmpR = r + cor[0];
                int tmpC = c + cor[1];
                if (MiniBoardUtils.isCorValid(tmpR, tmpC)) {
                    if (board.getTile(tmpR, tmpC).isOccupied()) {
                        if (board.getTile(tmpR, tmpC).getPiece() instanceof EnemyPiece) {
                            EnemyPiece enemyPiece = (EnemyPiece) board.getTile(tmpR, tmpC).getPiece();
                            if (!enemyPiece.isImmune())
                                res.add(enemyPiece);
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        String res = this.cardType.toString();
        for (int i = 0; i < 4; i++) {
            if (this.getHasPower(i))
                res += ("_" + this.getPowerByID(i).toString());
        }
        return res;
    }

    public abstract List<MiniMove> legalMoves(final MiniBoard board, final PlayerPiece piece);

    public boolean canAttack(final int r, final int c, final MiniBoard board) {
        for (MiniMove move : this.legalMoves(board, board.getPlayerPiece())) {
            if (move.getDestinationRow() == r && move.getDestinationCol() == c)
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        // if (!(obj instanceof Card))
        //     return false;
        // Card objCard = (Card) obj;
        // if (this.cardType == objCard.getCardType() && this.getHasPower(0) == objCard.getHasPower(0)
        //         && this.getHasPower(1) == objCard.getHasPower(1) && this.getHasPower(2) == objCard.getHasPower(2)
        //         && this.getHasPower(3) == objCard.getHasPower(3)) {
        //             return true;
        // }
        return false;
    }

    public enum CardType {
        BISHOP("B", "Bishop"),
        KING("K", "King"),
        KNIGHT("N", "Knight"),
        PAWN("P", "Pawn"),
        QUEEN("Q", "Queen"),
        ROOK("R", "Rook");

        private String cardName, displayName;

        CardType(final String cardName, final String displayName) {
            this.cardName = cardName;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return this.cardName;
        }

        public String getName() {
            return this.displayName;
        }
    }

    public enum Power {
        CROSS(0, "+", "Perform a cross attack after moving."),
        DIAGONAL(1, "x", "Perform a diagonal attack after moving."),
        SHIELD(2, "s", "Gain a shield blocking 1 damage."),
        CATNIP(3, "c", "Gain a move and draw a card.");

        private int powerID;
        private String powerName;
        private String description;

        Power(int powerID, String powerName, String description) {
            this.powerID = powerID;
            this.powerName = powerName;
            this.description = description;
        }

        public int getID() {
            return this.powerID;
        }

        @Override
        public String toString() {
            return this.powerName;
        }

        public String getDescription() {
            return description;
        }
    }
}
