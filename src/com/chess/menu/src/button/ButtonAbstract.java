package com.chess.menu.src.button;

import com.chess.menu.src.action.MenuActionInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class ButtonAbstract extends JPanel {
    JLabel title=new JLabel();
    MenuActionInterface onClick;

    public void setOnClick(MenuActionInterface onClick) {
        this.onClick = onClick;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.action((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource()));
            }
        });
    }
    public void setText(String text){
        this.title.setText(text);
    }
    public ButtonAbstract(){
        this("Blank button");
    }
    public ButtonAbstract(String title){
        this(title,(JFrame)->{});
    }
    public ButtonAbstract(String title,MenuActionInterface onClick){
        super();
        this.setText(title);
        this.setOnClick(onClick);
        this.setPreferredSize(new Dimension(50,120));
    }
}
