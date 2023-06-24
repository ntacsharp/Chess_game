package com.chess.engine.minigame;

import javax.sound.sampled.Clip;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;
import com.chess.sound.*;
public class GameState {
    private MiniBoard chessBoard;
    private Deck deck;
    private int moveLeft;
    private int turn;
    private int maxHealth;
    private int currentHealth;
    private int shield;
    private int floor;
    private int gold;
    private BackgroundMusic backgroundMusic;

    public GameState(PieceType playerPieceType) {
        this.chessBoard = MiniBoard.createStandardBoard(1, PieceType.BABARIAN);
        this.deck = new Deck(this.chessBoard.getPlayerPiece());
        this.floor = 1;
        this.moveLeft = 0;
        this.turn = 1;
        this.maxHealth = 3;
        this.currentHealth = 3;
        this.shield = 0;
        this.gold = 0;
        this.backgroundMusic = new BackgroundMusic("Chess_game\\src\\com\\chess\\sound\\background.wav");
        checkTurn();
    }

    public void start() {
        backgroundMusic.play();
    }
    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getShield() {
        return shield;
    }

    public int getFloor() {
        return floor;
    }

    private boolean isCleared() {
        if (this.chessBoard.getEnemyPieces().isEmpty())
            return true;
        return false;
    }

    private void checkTurn() {
        if (this.isCleared()) {
            this.chessBoard = MiniBoard.createStandardBoard(++this.floor, PieceType.BABARIAN);
            this.deck = new Deck(this.chessBoard.getPlayerPiece());
        }
        if (this.moveLeft == 0) {
            enemyTurn();
        } else
            playerTurn();
    }

    private void enemyTurn() {
        if (this.turn > 1) {
            this.shield -= this.chessBoard.calculateDamage();
            if (this.shield < 0)
                this.currentHealth += this.shield;
            this.shield = 0;
            if (this.currentHealth <= 0)
                return;
        }
        while (this.chessBoard.notMoved(this.turn) != null) {
            // try {
            //     Thread.sleep(1000);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
            EnemyPiece enemyPiece = this.chessBoard.notMoved(this.turn);
            this.chessBoard = enemyPiece.move(this.chessBoard);
        }
        this.setMoveLeft(2);
        this.checkTurn();
    }

    private void playerTurn() {
        // deck.fillHand(3);
        // while(this.moveLeft > 0){

        // }
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
