package com.example.reteasocializare_bazadate.utils.observer;


import com.example.reteasocializare_bazadate.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}