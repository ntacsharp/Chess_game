package com.chess.menu.src.component_list;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class LeveledGenericComponentList<Comp extends  JComponent> extends JPanel implements ComponentListInterface<Comp>, LeveledComponentInterface<Comp> {
    int levelSize;
    Border border1;

    @Override
    public void addComponentToList(Comp obj) {
        addComponentLeveled(obj,levelSize);
    }

    @Override
    public void removeComponentFromList(Comp obj) {
        for(int i=0;i<levelSize;i++){
            Component[] a= ((JPanel)this.getComponent(i+1)).getComponents();
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
        ((JPanel)this.getComponent(level+1)).remove(obj);
    }
    public void removeComponentFromListAll(Comp obj) {
        for(int i=0;i<levelSize;i++){
            Component[] a= ((JPanel)this.getComponent(i+1)).getComponents();
            for(int j=0;j<a.length;j++){
                if(a[j].equals(obj)){
                    ((JPanel)this.getComponent(i)).remove(j);
                }
            }
        }
    }
    @Override
    public Comp getBackFromList() {
        Component[] a= ((JPanel)this.getComponent(levelSize-1+1)).getComponents();
        return (Comp)a[a.length-1];
    }

    @Override
    public Comp getFirstFromList() {
        Component[] a= ((JPanel)this.getComponent(0+1)).getComponents();
        return (Comp)a[0];
    }
    public Comp get(int level,int noInLevel){
        Component[] a= ((JPanel)this.getComponent(level<levelSize?level+1:levelSize-1+1)).getComponents();
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
        obj.setAlignmentX(CENTER_ALIGNMENT);
        ((JPanel)this.getComponent(level+1)).add(obj);
    }
    public LeveledGenericComponentList(int levelSize){
        this.levelSize=levelSize;
        border1 = BorderFactory.createEmptyBorder(5, 10, 5, 10);
        this.setAlignmentY(CENTER_ALIGNMENT);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        for(int i=0;i<levelSize;i++){
            JPanel temp=new JPanel();
            temp.setLayout(new BoxLayout(temp,BoxLayout.Y_AXIS));
            temp.setBorder(border1);
            this.add(temp);
        }
        this.add(Box.createVerticalGlue());
    }
}
