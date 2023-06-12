package com.chess.menu.src.button;

import com.chess.menu.src.action.MenuActionInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ButtonAbstract extends JButton {
    MenuActionInterface onClick;

    public void setOnClick(MenuActionInterface onClick) {
        this.onClick = onClick;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClick.action((JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource()));
            }
        });
    }
    public ButtonAbstract(){
        super();
    }
    public ButtonAbstract(String title){
        super(title);
        setOnClick(onClick);
    }
    public ButtonAbstract(String title,MenuActionInterface onClick){
        super(title);
        setOnClick(onClick);
    }
}
