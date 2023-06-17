package com.chess.engine.minigame.cards;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public abstract class Card {
    protected final int cardID;
    protected final CardType cardType;
    private final List<Boolean> hasPower;

    protected Card(final int cardID, final boolean crossAttack, final boolean diagonalAttack, final boolean shield,
            final boolean catnip, final CardType cardType) {
        this.cardID = cardID;
        this.hasPower = new ArrayList<Boolean>();
        this.hasPower.add(crossAttack);
        this.hasPower.add(diagonalAttack);
        this.hasPower.add(shield);
        this.hasPower.add(catnip);
        this.cardType = cardType;
    }

    public int getCardID() {
        return cardID;
    }

    public Power getPowerByID(int id) {
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
        for(int i = 0; i < 4; i++){
            if(!this.hasPower.get(i)){
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

    public enum CardType {
        BISHOP("B"),
        KING("K"),
        KNIGHT("N"),
        PAWN("P"),
        QUEEN("Q"),
        ROOK("R");

        private String cardName;

        CardType(final String cardName) {
            this.cardName = cardName;
        }

        @Override
        public String toString() {
            return this.cardName;
        }
    }

    public enum Power {
        CROSS(0, "+"),
        DIAGONAL(1, "x"),
        SHIELD(2, "s"),
        CATNIP(3, "c");

        private int powerID;
        private String powerName;

        Power(int powerID, String powerName) {
            this.powerID = powerID;
            this.powerName = powerName;
        }

        public int getID() {
            return this.powerID;
        }

        @Override
        public String toString() {
            return this.powerName;
        }
    }
}
