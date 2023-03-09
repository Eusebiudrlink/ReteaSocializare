package com.example.reteasocializare_bazadate.utils.events;




public class GuiEntityChangeEvent implements Event {

    private final ChangeEventType type;
    public GuiEntityChangeEvent(ChangeEventType type) {
        this.type = type;
    }
    public ChangeEventType getType() {
        return type;
    }

}