package com.chess.engine.minigame;

import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class GameState {
    private MiniBoard chessBoard;
    private Deck deck;
    private int moveLeft;
    private int turn;
    private int maxHealth;
    private int currentHealth;

    public GameState(PieceType playerPieceType){
        this.chessBoard = MiniBoard.createStandardBoard(1, PieceType.BABARIAN);
        this.deck = new Deck(this.chessBoard.getPlayerPiece());
        this.moveLeft = 0;
        this.turn = 1;
        this.maxHealth = 3;
        this.currentHealth = 3;
        checkTurn();
    }

    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    private void checkTurn(){
        if(this.moveLeft == 0){
            enemyTurn();
        }
        else playerTurn();
    }

    private void enemyTurn(){
        if(this.turn > 1) this.currentHealth -= this.chessBoard.calculateDamage();
        while(this.chessBoard.notMoved(this.turn) != null){
            EnemyPiece enemyPiece = this.chessBoard.notMoved(this.turn);
            this.chessBoard = enemyPiece.move(this.chessBoard);
        }
        this.setMoveLeft(3);
        this.checkTurn();
    }

    private void playerTurn(){
        deck.fillHand(3);
        while(this.moveLeft > 0){

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
