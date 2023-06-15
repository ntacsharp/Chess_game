package com.chess.menu.src.leveled;

public interface LeveledComponentInterface<T> {
    void addComponentLeveled(T obj, int level);
    void removeComponentLeveled(T obj, int level);
}
