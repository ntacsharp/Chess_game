package com.chess.menu.src.menuframe;

import com.chess.menu.src.action.MenuActionInterface;
import com.chess.menu.src.button.ButtonAbstract;
import com.chess.menu.src.buttonpanel.ButtonMultilevelPanelAbstract;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

public abstract class MultilevelMenuFrameAbstract extends JFrame implements MenuFrameInterface,FrameWithMultilevelMenu {
    int moveOnButtonLevel,aboutButtonLevel,exitButtonLevel;
    ButtonMultilevelPanelAbstract buttonPanel;
    Integer actionID=0;
    Hashtable<Integer, MenuActionInterface> moveOnOptions=new Hashtable<Integer, MenuActionInterface>();
    public void addToButtonPanel(ButtonAbstract button,int level){
        buttonPanel.addButton(button,level);
    }
    public int addMoveOn(MenuActionInterface action, ButtonAbstract button){
        actionID++;
        moveOnOptions.put(actionID,action);
        button.setOnClick(action);
        this.addToButtonPanel(button,moveOnButtonLevel);
        return (int) actionID;
    };
    @Deprecated
    public MenuActionInterface modifyMoveOn(int key,MenuActionInterface action) throws IllegalArgumentException{
        if(!moveOnOptions.containsKey(key)){
            throw new IllegalArgumentException("Provided moveOn key is not valid!");
        }
        return moveOnOptions.put(key,action);
    };
    public MenuActionInterface deleteMoveOn(int key) throws IllegalArgumentException{
        if(!moveOnOptions.containsKey(key)){
            throw new IllegalArgumentException("Provided moveOn key is not valid!");
        }
        return moveOnOptions.remove(key);
    };
    public void moveOn(int key) throws IllegalArgumentException{
        if(!moveOnOptions.containsKey(key)){
            throw new IllegalArgumentException("Provided moveOn key is not valid!");
        }
        moveOnOptions.get(key).action(this);
    }
    MenuActionInterface about;
    public void setAbout(MenuActionInterface action,ButtonAbstract button){
        this.about=action;
        button.setOnClick(action);
        this.addToButtonPanel(button,aboutButtonLevel);
    };
    public void about(){
        about.action(this);
    }
    MenuActionInterface exit=(JFrame frame)->{
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    };
    public void setExit(MenuActionInterface action,ButtonAbstract button){
        this.exit=action;
        button.setOnClick(action);
        this.addToButtonPanel(button,exitButtonLevel);
    };
    public void exit(){
        exit.action(this);
    }
}
