package com.chess.menu.src.menuframe;

import com.chess.menu.src.action.MenuActionInterface;
import com.chess.menu.src.button.ButtonAbstract;

import javax.swing.*;

public interface MenuFrameInterface {
    public void setMenuUpperText(String text);
    public void setLogo(JLabel img);
    public int addMoveOn(MenuActionInterface action, ButtonAbstract button);
    public MenuActionInterface modifyMoveOn(int moveOnActionIndex,MenuActionInterface action);
    public MenuActionInterface deleteMoveOn(int moveOnActionIndex);
    public void moveOn(int moveOnActionIndex);
    public void setExit(MenuActionInterface action,ButtonAbstract button);
    public void exit();

    public void setAbout(MenuActionInterface action,ButtonAbstract button);
    public void about();

}
