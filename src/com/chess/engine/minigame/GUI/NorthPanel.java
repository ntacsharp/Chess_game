package com.chess.engine.minigame.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NorthPanel extends JPanel {
    private static final String OTHER_ICON_PATH = "art\\other\\";

    private final MiniTable miniTable;
    private LeftPanel leftPanel;
    private TurnPanel turnPanel;
    private GoldPanel goldPanel;

    public NorthPanel(final MiniTable miniTable) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.miniTable = miniTable;
        this.setBackground(MiniTable.darkTileColor);
        this.setPreferredSize(
                new Dimension((int) MiniTable.screenSize.getWidth(), (int) MiniTable.screenSize.getHeight() / 13));
        this.turnPanel = new TurnPanel();
        this.leftPanel = new LeftPanel();
        this.goldPanel = new GoldPanel();
        this.add(leftPanel);
        this.add(turnPanel);
        this.add(goldPanel);
        validate();
    }

    private class LeftPanel extends JPanel {
        LeftPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(MiniTable.darkTileColor);
            this.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                    (int) MiniTable.screenSize.getHeight() / 13));
            try {
                final ImageIcon gearIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "gear.png")));
                final JLabel setting = new JLabel(
                        new ImageIcon(gearIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 32,
                                (int) MiniTable.screenSize.getHeight() / 18, Image.SCALE_SMOOTH)));
                setting.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        miniTable.setting();
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
                add(setting);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class TurnPanel extends JPanel {
        TurnPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(MiniTable.darkTileColor);
            this.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                    (int) MiniTable.screenSize.getHeight() / 13));
            try {
                final ImageIcon coinIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "coin.png")));
                final ImageIcon crystalIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "crystal.png")));
                if (miniTable.getGameState().getTurn() <= 12) {
                    this.add(new JLabel(new ImageIcon(
                            crystalIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 36,
                                    (int) MiniTable.screenSize.getHeight() / 19, Image.SCALE_SMOOTH))));
                }
                for (int i = 1; i < 13 - miniTable.getGameState().getTurn(); i++) {
                    this.add(new JLabel(new ImageIcon(
                            coinIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 42,
                                    (int) MiniTable.screenSize.getHeight() / 23, Image.SCALE_SMOOTH))));
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GoldPanel extends JPanel {
        GoldPanel() {
            super(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            this.setBackground(MiniTable.darkTileColor);
            this.setPreferredSize(new Dimension((int) MiniTable.screenSize.getWidth() / 3,
                    (int) MiniTable.screenSize.getHeight() / 13));
            try {
                final ImageIcon coinIcon = new ImageIcon(ImageIO.read(new File(OTHER_ICON_PATH + "coin.png")));
                JLabel label = new JLabel(Integer.toString(miniTable.getGameState().getGold()));
                label.setForeground(MiniTable.lightBorderColor);
                label.setHorizontalAlignment(JLabel.CENTER);
                this.add(label);
                this.add(new JLabel(
                        new ImageIcon(coinIcon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 32,
                                (int) MiniTable.screenSize.getHeight() / 18, Image.SCALE_SMOOTH))));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
