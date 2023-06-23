package com.chess.menu.src.component_list;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class LeveledGenericComponentList<Comp extends  JComponent> extends JPanel implements ComponentListInterface<Comp>, LeveledComponentInterface<Comp> {
    int levelSize;
    int isGlued;
    Border border1;

    @Override
    public void addComponentToList(Comp obj) {
        addComponentLeveled(obj,levelSize);
    }

    @Override
    public void removeComponentFromList(Comp obj) {
        for(int i=0;i<levelSize;i++){
            //modified for glue
            Component[] a= ((JPanel)this.getComponent(i+isGlued)).getComponents();
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
            //modified for glue
            Component[] a= ((JPanel)this.getComponent(i+isGlued)).getComponents();
            for(int j=0;j<a.length;j++){
                if(a[j].equals(obj)){
                    ((JPanel)this.getComponent(i)).remove(j);
                }
            }
        }
    }
    @Override
    public Comp getBackFromList() {
        //modified for glue
        Component[] a= ((JPanel)this.getComponent(levelSize-1+isGlued)).getComponents();
        return (Comp)a[a.length-1];
    }

    @Override
    public Comp getFirstFromList() {
        //modified for glue
        Component[] a= ((JPanel)this.getComponent(0+isGlued)).getComponents();
        return (Comp)a[0];
    }
    public Comp get(int level,int noInLevel){
        //modified for glue
        Component[] a= ((JPanel)this.getComponent(level<levelSize?level+1:levelSize-1+isGlued)).getComponents();
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
        ((JPanel)this.getComponent(level+isGlued)).add(obj);
    }
    public LeveledGenericComponentList(int levelSize,boolean isGlued){
        this.isGlued=0;
        this.levelSize=levelSize;
        border1 = BorderFactory.createEmptyBorder(5, 0, 5, 0);
//        this.setAlignmentY(CENTER_ALIGNMENT);
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        if(isGlued){
            this.setBorder(new EmptyBorder((int)d.getHeight()/4,(int)d.getWidth()/4,(int)d.getHeight()/4,(int)d.getWidth()/4));
        }

        for(int i=0;i<levelSize;i++){
            JPanel temp=new JPanel();
            temp.setAlignmentX(CENTER_ALIGNMENT);
            temp.setLayout(new BoxLayout(temp,BoxLayout.Y_AXIS));
            temp.setBorder(border1);
            this.add(temp,BorderLayout.CENTER);
        }
    }
    public LeveledGenericComponentList(int levelSize){
        this(levelSize,false);
    }
}
