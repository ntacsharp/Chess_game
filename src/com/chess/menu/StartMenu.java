package com.chess.menu;

import com.chess.GUI.Table;
import com.chess.menu.src.button.SimpleButton;
import com.chess.menu.src.menuframe.SimpleMultilevelMenuFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class StartMenu{

    public static void main(String[] args) {
        SimpleMultilevelMenuFrame startMenu=new SimpleMultilevelMenuFrame("Start Menu");
        startMenu.setMenuUpperText("A chess game");
        startMenu.addMoveOn((JFrame parent)->{
            new Table();
            parent.dispose();
        },new SimpleButton("Start Game"));
        startMenu.setLogo(new JLabel(new ImageIcon(StartMenu.class.getResource("res/logo.jpg"))));
        startMenu.setAbout((JFrame parent)->{
            JOptionPane.showMessageDialog(parent,
                    "A game made by An, T.Anh and Ly");
        },new SimpleButton("About"));
        startMenu.setExit((JFrame parent)->{
            parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
        },new SimpleButton("Exit"));
        startMenu.setVisible(true);
    }
}
