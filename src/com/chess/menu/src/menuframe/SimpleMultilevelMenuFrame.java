package com.chess.menu.src.menuframe;

import com.chess.menu.src.buttonpanel.ButtonMutilevelGridBagPanel;
import com.chess.menu.src.buttonpanel.ButtonMultilevelPanelAbstract;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleMultilevelMenuFrame extends MultilevelMenuFrameAbstract {
    JPanel menuLabelPanel,menuIconPanel;

    Label menuLabel;
    JPanel menuPanel;

    JLabel imgLabel;

    public void setLogo(JLabel img) throws IllegalArgumentException {
        if (img.getIcon() == null) {
            throw new IllegalArgumentException("Label must contains ICON");
        }
        menuIconPanel.add(img);
    }
    public void setMenuUpperText(String text){
        menuLabel.setText(text);
    }
    public void setButtonPanel(ButtonMultilevelPanelAbstract p){
        if(buttonPanel!=null){
            menuPanel.remove(buttonPanel);
        }
        menuPanel.add(p);
        buttonPanel =p;
    }
    public SimpleMultilevelMenuFrame(String title) {
        super();
        super.moveOnButtonLevel=0;
        super.aboutButtonLevel=1;
        super.exitButtonLevel=2;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        this.setTitle(title);
        this.setSize(400,600);
        menuPanel =new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.PAGE_AXIS));
        menuPanel.setBorder(new EmptyBorder(10,10,30,10));

        menuIconPanel=new JPanel(new BorderLayout());
        menuPanel.add(menuIconPanel);

        menuLabelPanel=new JPanel(new BorderLayout());
        menuPanel.add(menuLabelPanel);

        this.setButtonPanel(new ButtonMutilevelGridBagPanel(3));


        this.add(menuPanel);
    }
}
