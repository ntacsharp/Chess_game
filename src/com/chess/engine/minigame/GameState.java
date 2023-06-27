package com.chess.engine.minigame;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class GameState {
    private static final int[][] CROSS = {
            { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }
    };
    private static final int[][] DIAGONAL = {
            { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 1 }
    };
    private MiniBoard chessBoard;
    private Deck deck;
    private int moveLeft;
    private int turn;
    private int maxHealth;
    private int currentHealth;
    private int shield;
    private int floor;
    private int gold;

    public GameState(PieceType playerPieceType) {
        this.chessBoard = MiniBoard.createStandardBoard(1, PieceType.BABARIAN);
        this.deck = new Deck(this.chessBoard.getPlayerPiece());
        this.floor = 1;
        this.moveLeft = 0;
        this.turn = 1;
        this.maxHealth = 4;
        this.currentHealth = 4;
        this.shield = 0;
        this.gold = 0;
    }

    public void createNewFloor() {
        this.deck.emptyCurrentDeck();
        this.floor++;
        this.moveLeft = 0;
        this.chessBoard = MiniBoard.createStandardBoard(this.floor, PieceType.BABARIAN);
        //this.deck.fillHand(3);
    }

    public void setTurn(final int turn) {
        this.turn = turn;
    }

    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getShield() {
        return shield;
    }

    public int getFloor() {
        return floor;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(final int x) {
        this.gold = x;
    }

    public boolean isCleared() {
        if (this.chessBoard.getEnemyPieces().isEmpty())
            return true;
        return false;
    }

    public void doDamage() {
        this.shield -= this.chessBoard.calculateDamage(chessBoard.getPlayerPiece().getRow(),
                chessBoard.getPlayerPiece().getCol());
        if (this.shield < 0) {
            this.currentHealth += this.shield;
        }

        this.shield = 0;
    }

    public void doMove(final EnemyPiece enemyPiece) {
        this.chessBoard = enemyPiece.move(this.chessBoard);
    }

    public void doMove(final Card card, final int r, final int c) {
        for (MiniMove move : card.legalMoves(chessBoard, chessBoard.getPlayerPiece())) {
            if (move.getDestinationRow() == r && move.getDestinationCol() == c) {
                this.chessBoard = move.execute();
                this.moveLeft--;
            }
        }
    }

    public void crossAttack() {
        for (int[] cor : CROSS) {
            int r = cor[0] + chessBoard.getPlayerPiece().getRow();
            int c = cor[1] + chessBoard.getPlayerPiece().getCol();
            if (MiniBoardUtils.isCorValid(r, c)) {
                MiniMove move = new MiniMove.PlayerMove(this.chessBoard, this.chessBoard.getPlayerPiece(),
                        this.chessBoard.getPlayerPiece().getRow(), this.chessBoard.getPlayerPiece().getCol(),
                        (EnemyPiece) this.chessBoard.getTile(r, c).getPiece());
                this.chessBoard = move.execute();
            }
        }
    }

    public void diagonalAttack() {
        for (int[] cor : DIAGONAL) {
            int r = cor[0] + chessBoard.getPlayerPiece().getRow();
            int c = cor[1] + chessBoard.getPlayerPiece().getCol();
            if (MiniBoardUtils.isCorValid(r, c)) {
                MiniMove move = new MiniMove.PlayerMove(this.chessBoard, this.chessBoard.getPlayerPiece(),
                        this.chessBoard.getPlayerPiece().getRow(), this.chessBoard.getPlayerPiece().getCol(),
                        (EnemyPiece) this.chessBoard.getTile(r, c).getPiece());
                this.chessBoard = move.execute();
            }
        }
    }

    public MiniBoard getChessBoard() {
        return chessBoard;
    }

    public int getTurn() {
        return turn;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getMoveLeft() {
        return moveLeft;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setChessBoard(MiniBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }
}
