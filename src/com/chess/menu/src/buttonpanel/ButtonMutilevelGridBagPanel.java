package com.chess.menu.src.buttonpanel;

import com.chess.menu.src.button.ButtonAbstract;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ButtonMutilevelGridBagPanel extends ButtonMultilevelPanelAbstract {
    GridBagConstraints gbc=new GridBagConstraints();
    public void addButton(ButtonAbstract b, int level){
        ((JPanel)this.getComponent(level)).add(b,gbc);
    }
    /**
     *  This method add the button to the highest level of the panel
     */
    public ButtonMutilevelGridBagPanel(int levels){
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx=0.8;
        Border _border = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        this.setLayout(new GridBagLayout());
        for(int i=0;i<levels;i++){
            JPanel temp=new JPanel(new GridBagLayout());
            temp.setBorder(_border);
            this.add(temp,gbc);
        }

    }
}
