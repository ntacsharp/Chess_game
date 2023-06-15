package com.chess.menu.src.leveled;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LeveledGenericComponentList<Comp extends  Component> extends JPanel implements ComponentListInterface<Comp>, LeveledComponentInterface<Comp> {
    int levelSize;
    GridBagConstraints gbc1=new GridBagConstraints();
    GridBagConstraints gbc2=new GridBagConstraints();

    @Override
    public void addComponentToList(Comp obj) {
        addComponentLeveled(obj,levelSize);
    }

    @Override
    public void removeComponentFromList(Comp obj) {
        for(int i=0;i<levelSize;i++){
            Component[] a= ((JPanel)this.getComponent(i)).getComponents();
            for(int j=0;j<a.length;j++){
                if(a[j].equals(obj)){
                    ((JPanel)this.getComponent(i)).remove(j);
                    return;
                }
            }
        }
    }
    @Override
    public void removeComponentLeveled(Comp obj,int level) {
        ((JPanel)this.getComponent(level)).remove(obj);
    }
    public void removeComponentFromListAll(Comp obj) {
        for(int i=0;i<levelSize;i++){
            Component[] a= ((JPanel)this.getComponent(i)).getComponents();
            for(int j=0;j<a.length;j++){
                if(a[j].equals(obj)){
                    ((JPanel)this.getComponent(i)).remove(j);
                }
            }
        }
    }
    @Override
    public Comp getBackFromList() {
        Component[] a= ((JPanel)this.getComponent(levelSize-1)).getComponents();
        return (Comp)a[a.length-1];
    }

    @Override
    public Comp getFirstFromList() {
        Component[] a= ((JPanel)this.getComponent(0)).getComponents();
        return (Comp)a[0];
    }
    public Comp get(int level,int noInLevel){
        Component[] a= ((JPanel)this.getComponent(level<levelSize?level:levelSize-1)).getComponents();
        return (Comp)a[noInLevel<a.length?noInLevel:a.length-1];
    }
    @Deprecated
    @Override
    public void clearList() {
        this.removeAll();
    }

    @Override
    public void addComponentLeveled(Comp obj, int level) {
        if(level>=levelSize){
            addComponentLeveled(obj,levelSize-1);
            return;
        }
        ((JPanel)this.getComponent(level)).add(obj,gbc2);
    }
    private void _init(){
        Border _border = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        this.setLayout(new GridBagLayout());
        for(int i=0;i<levelSize;i++){
            JPanel temp=new JPanel(new GridBagLayout());
            temp.setBorder(_border);
            this.add(temp,gbc1);
        }
    }
    public LeveledGenericComponentList(int levelSize, GridBagConstraints gbc1, GridBagConstraints gbc2){
        this.levelSize=levelSize;
        this.gbc1=gbc1;
        this.gbc2=gbc2;
        _init();
    }
    public LeveledGenericComponentList(int levelSize){
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx=0.8;
        this.gbc1=gbc;
        this.gbc2=gbc;
        _init();
    }
}
