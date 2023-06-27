package com.chess.engine.minigame.GUI.Shopping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chess.Game;
import com.chess.engine.minigame.GUI.GamePanel;

public class ShoppingNorthPanel extends JPanel {
    private LeftPanel leftPanel;
    private GoldPanel goldPanel;
    private final GamePanel gp;

    public ShoppingNorthPanel(final GamePanel gp) {
        super(new GridLayout(1, 2, 0, 0));
        this.gp = gp;
        setDoubleBuffered(true);
        this.setPreferredSize(
                new Dimension((int) Game.screenSize.getWidth(), (int) Game.screenSize.getHeight() / 12));
        this.leftPanel = new LeftPanel();
        this.goldPanel = new GoldPanel();
        this.add(leftPanel);
        this.add(goldPanel);
        validate();
    }

    public void draw(Graphics2D g2) {
        leftPanel.draw(g2);
        goldPanel.draw(g2);
    }

    private class LeftPanel extends JPanel {
        LeftPanel() {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setDoubleBuffered(true);
            this.setPreferredSize(new Dimension((int) Game.screenSize.getWidth() / 3,
                    (int) Game.screenSize.getHeight() / 12));
            JLabel settingLabel = new JLabel();
            settingLabel.setPreferredSize(
                    new Dimension((int) Game.screenSize.getHeight() / 12, (int) Game.screenSize.getHeight() / 12));
            settingLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("setting");
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            this.add(settingLabel);
            validate();
        }

        public void draw(Graphics2D g2) {
            g2.drawImage(Game.imageList.getGearImage(), 10, 10, (int) Game.screenSize.getHeight() / 12 - 20,
                    (int) Game.screenSize.getHeight() / 12 - 20, null);
        }
    }

    private class GoldPanel extends JPanel {
        GoldPanel() {
            super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            setDoubleBuffered(true);
            this.setPreferredSize(
                    new Dimension((int) Game.screenSize.getWidth() * 2 / 3, (int) Game.screenSize.getHeight() / 12));
            validate();
        }

        public void draw(Graphics2D g2) {
            g2.setFont(new Font("Arial", Font.BOLD, 28));
            g2.setColor(Color.WHITE);
            g2.drawString("Shopping round of floor " + gp.getGameState().getFloor(), (int) Game.screenSize.getWidth() / 3 + 55, 45);
            g2.drawImage(Game.imageList.getBagImage(), (int) Game.screenSize.getWidth() - 50, 10, 50,
                    50, null);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.setColor(Color.WHITE);
            g2.drawString(new DecimalFormat("00").format(gp.getGameState().getGold()),
                    (int) Game.screenSize.getWidth() - 39, 50);
        }
    }
}
