package com.chess.engine.minigame.GUI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chess.Game;

public class SettingPanel extends JPanel {
    public SettingPanel() {
        super(new GridLayout(7, 1, 0, 0));
        setBackground(ColorList.transparentBg);
        setSize(Game.screenSize);
        setDoubleBuffered(true);
        JPanel masterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        masterPanel.setBackground(ColorList.transparentBg);
        //add(masterPanel);
        JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        musicPanel.setBackground(ColorList.transparentBg);
        //add(musicPanel);
        JPanel soundPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        soundPanel.setBackground(ColorList.transparentBg);
        //add(soundPanel);
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        keyPanel.setBackground(ColorList.transparentBg);
        //add(keyPanel);
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        mainPanel.setBackground(ColorList.transparentBg);

        JLabel mainLabel = new JLabel("Exit to Mainmenu");
        mainLabel.setHorizontalAlignment(JLabel.CENTER);
        mainLabel.setFont(new Font("Arial", Font.BOLD, 36));
        mainLabel.setForeground(ColorList.chosenBackground);
        mainLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Game.exitToMenu();
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
        mainPanel.add(mainLabel);
        
        JPanel abandonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        abandonPanel.setBackground(ColorList.transparentBg);
        JLabel abandonLabel = new JLabel("Abandon run");
        abandonLabel.setHorizontalAlignment(JLabel.CENTER);
        abandonLabel.setFont(new Font("Arial", Font.BOLD, 36));
        abandonLabel.setForeground(ColorList.chosenBackground);
        abandonLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Abandon");
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
        abandonPanel.add(abandonLabel);
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
        backPanel.setBackground(ColorList.transparentBg);
        JLabel backLabel = new JLabel("Back");
        backLabel.setHorizontalAlignment(JLabel.CENTER);
        backLabel.setFont(new Font("Arial", Font.BOLD, 36));
        backLabel.setForeground(ColorList.chosenBackground);
        backLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Game.setting();
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
        backPanel.add(backLabel);
        
        add(mainPanel);
        add(backPanel);
        add(abandonPanel);
        validate();
    }
}
