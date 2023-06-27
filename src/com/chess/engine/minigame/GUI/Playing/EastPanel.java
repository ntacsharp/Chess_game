package com.chess.engine.minigame.GUI.Playing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class EastPanel extends JPanel {
    private final GamePanel gp;
    private Object objEntered = null;

    public EastPanel(final GamePanel gp) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.gp = gp;
        setDoubleBuffered(true);
        this.setPreferredSize(
                new Dimension(new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight())));
        validate();
    }

    public void setObjEntered(Object objEntered) {
        this.objEntered = objEntered;
    }

    public void draw(Graphics2D g2) {
        if (objEntered != null) {
            g2.setColor(Color.WHITE);
            if (objEntered instanceof MiniTile) {
                MiniTile tile = (MiniTile) objEntered;
                if (tile.isOccupied()) {
                    MiniPiece piece = tile.getPiece();
                    if (piece instanceof PlayerPiece) {
                        g2.setFont(new Font("Arial", Font.BOLD, 24));
                        g2.drawString("Babarian", (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                (int) Game.screenSize.getHeight() / 12 + 20);
                        g2.setFont(new Font("Arial", Font.ITALIC, 18));
                        for (int i = 0; i < gp.getGameState().getChessBoard().getPlayerPiece().getInformation()
                                .size(); i++) {
                            g2.drawString(gp.getGameState().getChessBoard().getPlayerPiece().getInformation().get(i),
                                    (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                    (int) Game.screenSize.getHeight() / 12 + 60 + i * 30);
                        }

                    } else {
                        EnemyPiece enemyPiece = (EnemyPiece) piece;
                        g2.setFont(new Font("Arial", Font.BOLD, 24));
                        g2.drawString(enemyPiece.getPieceType().getName(),
                                (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                (int) Game.screenSize.getHeight() / 12 + 20);
                        g2.setFont(new Font("Arial", Font.ITALIC, 18));
                        for (int i = 0; i < enemyPiece.getInformation().size(); i++) {
                            g2.drawString(enemyPiece.getInformation().get(i),
                                    (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                    (int) Game.screenSize.getHeight() / 12 + 60 + i * 30);
                        }
                        g2.setFont(new Font("Arial", Font.PLAIN, 18));
                        if (enemyPiece.isCurrentlyNimble()) {
                            g2.drawImage(Game.imageList.getNimbleImage(), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                    (int) Game.screenSize.getHeight() / 12 + 105, 20, 20, null);
                            g2.drawString("This piece can dodge first attack",
                                    (int) Game.screenSize.getWidth() * 2 / 3 + 50,
                                    (int) Game.screenSize.getHeight() / 12 + 120);
                        }
                    }
                }
                if (tile.isBlighted()) {
                    g2.drawImage(Game.imageList.getBlightImage(), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                            (int) Game.screenSize.getHeight() / 12 + 130, 20, 20, null);
                    g2.setFont(new Font("Arial", Font.PLAIN, 18));
                    g2.drawString("This tile can deal 1 damage to player stand on it.",
                            (int) Game.screenSize.getWidth() * 2 / 3 + 50,
                            (int) Game.screenSize.getHeight() / 12 + 150);
                }
            } else if (objEntered instanceof Card) {
                Card card = (Card) objEntered;
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.drawString(card.getCardType().getName(), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                        (int) Game.screenSize.getHeight() / 12 + 20);
                int cnt = 0;
                g2.setFont(new Font("Arial", Font.PLAIN, 18));
                for (int i = 0; i < 4; i++) {
                    if (card.getHasPower(i)) {
                        g2.drawImage(Game.imageList.getPowerImage(i), (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                                (int) Game.screenSize.getHeight() / 12 + 33 + cnt * 30, 40, 40, null);
                        g2.drawString(Card.getPowerByID(i).getDescription(), (int) Game.screenSize.getWidth() * 2 / 3 + 60,
                                (int) Game.screenSize.getHeight() / 12 + 60 + cnt * 30);
                        cnt++;
                    }
                }
            } else if (objEntered instanceof String) {
                g2.setFont(new Font("Arial", Font.BOLD, 24));
                g2.drawString("Skip", (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                        (int) Game.screenSize.getHeight() / 12 + 20);
                g2.setFont(new Font("Arial", Font.ITALIC, 18));
                g2.drawString("Remove your remaining moves",
                        (int) Game.screenSize.getWidth() * 2 / 3 + 20,
                        (int) Game.screenSize.getHeight() / 12 + 60);
            }
        }
    }
}