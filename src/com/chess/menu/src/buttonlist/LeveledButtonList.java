//package com.chess.menu.src.buttonlist;
//
//import com.chess.menu.src.button.ButtonAbstract;
//import com.chess.menu.src.component_list.ComponentListInterface;
//import com.chess.menu.src.component_list.LeveledComponentInterface;
//
//import javax.swing.*;
//import javax.swing.border.Border;
//import java.awt.*;
//
//@Deprecated
//public class LeveledButtonList extends JPanel implements ComponentListInterface<ButtonAbstract>, LeveledComponentInterface<ButtonAbstract> {
//    int levelSize;
//    GridBagConstraints gbc1=new GridBagConstraints();
//    GridBagConstraints gbc2=new GridBagConstraints();
//
//    @Override
//    public void addComponentToList(ButtonAbstract obj) {
//        addComponentLeveled(obj,levelSize);
//    }
//
//    @Override
//    public void removeComponentFromList(ButtonAbstract obj) {
//        for(int i=0;i<levelSize;i++){
//            Component[] a= ((JPanel)this.getComponent(i)).getComponents();
//            for(int j=0;j<a.length;j++){
//                if(a[j].equals(obj)){
//                    ((JPanel)this.getComponent(i)).remove(j);
//                    return;
//                }
//            }
//        }
//    }
//    @Override
//    public void removeComponentLeveled(ButtonAbstract obj,int level) {
//        ((JPanel)this.getComponent(level)).remove(obj);
//    }
//    public void removeComponentFromListAll(ButtonAbstract obj) {
//        for(int i=0;i<levelSize;i++){
//            Component[] a= ((JPanel)this.getComponent(i)).getComponents();
//            for(int j=0;j<a.length;j++){
//                if(a[j].equals(obj)){
//                    ((JPanel)this.getComponent(i)).remove(j);
//                }
//            }
//        }
//    }
//    @Override
//    public ButtonAbstract getBackFromList() {
//        Component[] a= ((JPanel)this.getComponent(levelSize-1)).getComponents();
//        return (ButtonAbstract)a[a.length-1];
//    }
//
//    @Override
//    public ButtonAbstract getFirstFromList() {
//        Component[] a= ((JPanel)this.getComponent(0)).getComponents();
//        return (ButtonAbstract)a[0];
//    }
//    public ButtonAbstract get(int level,int noInLevel){
//        Component[] a= ((JPanel)this.getComponent(level<levelSize?level:levelSize-1)).getComponents();
//        return (ButtonAbstract)a[noInLevel<a.length?noInLevel:a.length-1];
//    }
//    @Deprecated
//    @Override
//    public void clearList() {
//        this.removeAll();
//    }
//
//    @Override
//    public void addComponentLeveled(ButtonAbstract obj, int level) {
//        if(level>=levelSize){
//            addComponentLeveled(obj,levelSize-1);
//            return;
//        }
//        ((JPanel)this.getComponent(level)).add(obj,gbc2);
//    }
//    private void _init(){
//        Border _border = BorderFactory.createEmptyBorder(5, 10, 5, 10);
//        this.setLayout(new GridBagLayout());
//        for(int i=0;i<levelSize;i++){
//            JPanel temp=new JPanel(new GridBagLayout());
//            temp.setBorder(_border);
//            this.add(temp,gbc1);
//        }
//    }
//    public LeveledButtonList(int levelSize, GridBagConstraints gbc1, GridBagConstraints gbc2){
//        this.levelSize=levelSize;
//        this.gbc1=gbc1;
//        this.gbc2=gbc2;
//        _init();
//    }
//    public LeveledButtonList(int levelSize){
//        GridBagConstraints gbc=new GridBagConstraints();
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.weightx=0.8;
//        this.gbc1=gbc;
//        this.gbc2=gbc;
//        _init();
//    }
//}
