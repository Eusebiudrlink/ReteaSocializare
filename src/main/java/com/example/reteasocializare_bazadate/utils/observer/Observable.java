package com.example.reteasocializare_bazadate.utils.observer;


import com.example.reteasocializare_bazadate.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);

    void removeObserver(Observer<E> e);

    void notifyObservers(E t);
}
