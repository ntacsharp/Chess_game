package com.chess.menu.src.buttonpanel;

import com.chess.menu.src.button.ButtonAbstract;

import java.awt.*;
public class ButtonGridBagPanel extends ButtonPanelAbstract {
    GridBagConstraints gbc;
    public void addButton(ButtonAbstract b){
        this.add(b,gbc);
    }

    public ButtonGridBagPanel(){
        this.setLayout(new GridBagLayout());

        gbc=new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }
}
