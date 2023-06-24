package com.chess.engine.minigame.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class EastPanel extends JPanel {
    private static final String POWER_ICON_PATH = "art\\other\\power\\";
    private static final String OTHER_ICON_PATH = "art\\other\\";

    public EastPanel() {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBackground(MiniTable.darkTileColor);
        this.setPreferredSize(
                new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                        (int) MiniTable.screenSize.getHeight()));
        validate();
    }

    public void redraw(Object o) {
        removeAll();
        if (o instanceof MiniTile) {
            MiniTile tile = (MiniTile) o;
            if (tile.isOccupied()) {
                if (tile.getPiece() instanceof PlayerPiece) {
                    JLabel character = new JLabel("Babarian");
                    character.setFont(new Font("Arial", Font.BOLD, 24));
                    character.setForeground(Color.WHITE);
                    character.setBorder(new EmptyBorder(0, 10, 0, 0));
                    character.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 20));
                    JLabel information = new JLabel(tile.getPiece().getInformation());
                    information.setFont(new Font("Arial", Font.PLAIN, 18));
                    information.setForeground(Color.WHITE);
                    information.setBorder(new EmptyBorder(0, 10, 0, 0));
                    information.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 18));
                    this.add(character);
                    this.add(information);
                } else {
                    EnemyPiece enemyPiece = (EnemyPiece) tile.getPiece();
                    JLabel character = new JLabel(enemyPiece.getPieceType().getName());
                    character.setFont(new Font("Arial", Font.BOLD, 24));
                    character.setForeground(Color.WHITE);
                    character.setBorder(new EmptyBorder(0, 10, 0, 0));
                    character.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 20));
                    this.add(character);
                    JLabel information = new JLabel(enemyPiece.getInformation());
                    information.setFont(new Font("Arial", Font.PLAIN, 18));
                    information.setForeground(Color.WHITE);
                    information.setBorder(new EmptyBorder(0, 10, 0, 0));
                    information.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 18));
                    this.add(information);
                    if (enemyPiece.isCurrentlyNimble()) {
                        try {
                            ImageIcon nimbleIcon = new ImageIcon(ImageIO.read(
                                    new File(OTHER_ICON_PATH + "nimble.png")));
                            JLabel nimbleLabel = new JLabel(
                                    "This enemy can dodge first attack each turn",
                                    new ImageIcon(nimbleIcon.getImage()
                                            .getScaledInstance(
                                                    (int) MiniTable.screenSize.getWidth() / 41,
                                                    (int) MiniTable.screenSize.getHeight() / 27,
                                                    Image.SCALE_SMOOTH)),
                                    JLabel.LEFT);
                            nimbleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                            nimbleLabel.setForeground(Color.WHITE);
                            nimbleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                            nimbleLabel.setPreferredSize(
                                    new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                            (int) MiniTable.screenSize.getHeight() / 25));
                            this.add(nimbleLabel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (tile.isBlighted()) {
                JLabel blightLabel = new JLabel(
                        "<html><b>Blighted tile:</b> Deal 1 damage if player stay in this tile in the end of turn!</html>");
                blightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                blightLabel.setForeground(Color.WHITE);
                blightLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                blightLabel.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                        (int) MiniTable.screenSize.getHeight() / 15));
                blightLabel.setVerticalAlignment(JLabel.BOTTOM);
                this.add(blightLabel);
            }
        }
        if (o instanceof Card) {
            Card card = (Card) o;
            JLabel character = new JLabel(card.getCardType().getName());
                    character.setFont(new Font("Arial", Font.BOLD, 24));
                    character.setForeground(Color.WHITE);
                    character.setBorder(new EmptyBorder(0, 10, 0, 0));
                    character.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 20));
            this.add(character);
            try {
                if (card.getHasPower(0)) {
                    ImageIcon crossIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "+.png")));
                    JLabel crossLabel = new JLabel("Perform a cross attack after moving.",
                            new ImageIcon(crossIcon.getImage().getScaledInstance(
                                    (int) MiniTable.screenSize.getWidth() / 43,
                                    (int) MiniTable.screenSize.getHeight() / 27,
                                    Image.SCALE_SMOOTH)),
                            JLabel.LEFT);
                    crossLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    crossLabel.setForeground(Color.WHITE);
                    crossLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                    crossLabel.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 25));
                    this.add(crossLabel);
                }
                if (card.getHasPower(1)) {
                    ImageIcon diagonalIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "x.png")));
                    JLabel diagonalLabel = new JLabel("Perform a diagonal attack after moving.",
                            new ImageIcon(diagonalIcon.getImage().getScaledInstance(
                                    (int) MiniTable.screenSize.getWidth() / 43,
                                    (int) MiniTable.screenSize.getHeight() / 27,
                                    Image.SCALE_SMOOTH)),
                            JLabel.LEFT);
                    diagonalLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    diagonalLabel.setForeground(Color.WHITE);
                    diagonalLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                    diagonalLabel.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 25));
                    this.add(diagonalLabel);
                }
                if (card.getHasPower(2)) {
                    ImageIcon shieldIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "s.png")));
                    JLabel shieldLabel = new JLabel("Gain a shield absorbing 1 damage this turn.",
                            new ImageIcon(shieldIcon.getImage().getScaledInstance(
                                    (int) MiniTable.screenSize.getWidth() / 43,
                                    (int) MiniTable.screenSize.getHeight() / 27,
                                    Image.SCALE_SMOOTH)),
                            JLabel.LEFT);
                    shieldLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    shieldLabel.setForeground(Color.WHITE);
                    shieldLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                    shieldLabel.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 25));
                    this.add(shieldLabel);
                }
                if (card.getHasPower(3)) {
                    ImageIcon catnipIcon = new ImageIcon(ImageIO.read(new File(POWER_ICON_PATH + "c.png")));
                    JLabel catnipLabel = new JLabel("Gain a move and draw a card",
                            new ImageIcon(catnipIcon.getImage().getScaledInstance(
                                    (int) MiniTable.screenSize.getWidth() / 43,
                                    (int) MiniTable.screenSize.getHeight() / 27,
                                    Image.SCALE_SMOOTH)),
                            JLabel.LEFT);
                    catnipLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                    catnipLabel.setForeground(Color.WHITE);
                    catnipLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
                    catnipLabel.setPreferredSize(
                            new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                                    (int) MiniTable.screenSize.getHeight() / 25));
                    this.add(catnipLabel);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        validate();
        repaint();
    }
}
