package com.chess.menu.src.button;

import com.chess.menu.src.action.MenuActionInterface;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
public class SimpleButton extends ButtonAbstract {

    public SimpleButton(){
        super();
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setAlignmentY(Component.CENTER_ALIGNMENT);
        this.setBorder(new LineBorder(Color.BLACK));
//        this.setMargin(new Insets(20,0,20,0));
    }
    public SimpleButton(String text){
        this();
        this.setText(text);
    }
    public SimpleButton(String text, MenuActionInterface action){
        this();
        this.setText(text);
        this.add(title);
        setOnClick(action);
    }

}
