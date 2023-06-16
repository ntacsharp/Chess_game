package com.chess.menu.src.component_list;

public interface ComponentListInterface<T> {
    void addComponentToList(T obj);
    void removeComponentFromList(T obj);
    T getBackFromList();
    T getFirstFromList();
    void clearList();
}
