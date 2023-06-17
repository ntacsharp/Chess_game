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

    public GameState(PieceType playerPieceType){
        this.chessBoard = MiniBoard.createStandardBoard(1, PieceType.BABARIAN);
        this.deck = new Deck(this.chessBoard.getPlayerPiece());
        this.moveLeft = 0;
        this.turn = 1;
        checkTurn();
    }

    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    private void checkTurn(){
        if(this.moveLeft == 0){
            enemyTurn();
        }
    }

    private void enemyTurn(){
        while(this.chessBoard.notMoved(this.turn) != null){
            EnemyPiece enemyPiece = this.chessBoard.notMoved(this.turn);
            this.chessBoard = enemyPiece.move(this.chessBoard);
        }
        this.setMoveLeft(3);
    }
}
