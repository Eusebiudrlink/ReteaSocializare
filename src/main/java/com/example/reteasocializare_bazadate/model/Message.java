package com.example.reteasocializare_bazadate.model;

import java.time.LocalDateTime;

public class Message {
    private String message;
    private String source;
    private String destination;
    private LocalDateTime date;

    public Message(String message, String source, String destination, LocalDateTime date) {
        this.message = message;
        this.source = source;
        this.destination = destination;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
