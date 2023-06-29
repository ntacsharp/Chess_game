package com.chess.menu;

import com.chess.Game;
import com.chess.menu.src.button.ButtonAbstract;
import com.chess.menu.src.button.SimpleButton;
import com.chess.menu.src.component_list.LeveledGenericComponentList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartMenu{
    private static ImageIcon logo = new ImageIcon(StartMenu.class.getResource("res/logo.jpg"));
    public static void main(String[] args) {
//        SimpleMultilevelMenuFrame startMenu=new SimpleMultilevelMenuFrame("Start Menu");
//        startMenu.setMenuUpperText("A chess game");
//        startMenu.addMoveOn((JFrame parent)->{
//            new Table();
//            parent.dispose();
//        },new SimpleButton("Start Game"));
//        startMenu.setLogo(new JLabel(new ImageIcon(StartMenu.class.getResource("res/logo.jpg"))));
//        startMenu.setAbout((JFrame parent)->{
//            JOptionPane.showMessageDialog(parent,
//                    "A game made by An, T.Anh and Ly");
//        },new SimpleButton("About"));
//        startMenu.setExit((JFrame parent)->{
//            parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
//        },new SimpleButton("Exit"));
//        startMenu.setVisible(true)
        JFrame startMenu=new JFrame("A chess game: Start Menu");
        startMenu.setIconImage(logo.getImage());
        startMenu.setSize(Game.screenSize);
        startMenu.setUndecorated(true);
        startMenu.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        LeveledGenericComponentList<JComponent> startPanel= new LeveledGenericComponentList<JComponent>(2,true);
        startMenu.add(startPanel);

        LeveledGenericComponentList<JComponent> upperPanel= new LeveledGenericComponentList<JComponent>(2);
        upperPanel.addComponentLeveled(new JLabel(logo),0);
        upperPanel.addComponentLeveled(new JLabel("A chess game"),1);
        startPanel.add(upperPanel,0);

        LeveledGenericComponentList<ButtonAbstract> buttonPanel= new LeveledGenericComponentList<ButtonAbstract>(3);
        ButtonAbstract startButton=new SimpleButton("Start Game",
                (JFrame parent)->{
                    //new MiniTable();
                    Game.play();
                    parent.dispose();
                });
        buttonPanel.addComponentLeveled(startButton,0);
        buttonPanel.addComponentLeveled(new SimpleButton("About",
                (JFrame parent)->{
                    JOptionPane.showMessageDialog(parent,
                            "A game made by An, T.Anh and Ly");
                }
                ),1);
        buttonPanel.addComponentLeveled(new SimpleButton("Exit",(JFrame parent)->{
            parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
        }),2);
        startPanel.add(buttonPanel,1);

        startMenu.setVisible(true);
    }
}
