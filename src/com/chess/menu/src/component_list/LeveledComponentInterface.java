package com.chess.menu.src.component_list;

public interface LeveledComponentInterface<T> {
    void addComponentLeveled(T obj, int level);
    void removeComponentLeveled(T obj, int level);
}
