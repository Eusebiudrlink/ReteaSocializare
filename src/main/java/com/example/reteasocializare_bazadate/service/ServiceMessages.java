package com.example.reteasocializare_bazadate.service;

import com.example.reteasocializare_bazadate.model.Message;
import com.example.reteasocializare_bazadate.repository.RepoMessagesDB;
import com.example.reteasocializare_bazadate.utils.events.ChangeEventType;
import com.example.reteasocializare_bazadate.utils.events.GuiEntityChangeEvent;
import com.example.reteasocializare_bazadate.utils.observer.Observable;
import com.example.reteasocializare_bazadate.utils.observer.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServiceMessages implements Observable<GuiEntityChangeEvent> {

    private RepoMessagesDB repoMessagesDB;
    private final List<Observer<GuiEntityChangeEvent>> observersMsg = new ArrayList<>();

    public ServiceMessages(RepoMessagesDB repoMessagesDB) {

        this.repoMessagesDB = repoMessagesDB;
    }

    public void addMessage(String message, String source, String destination) {
        //formatare datetime
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTimeNow.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);

        Message messagex = new Message(message, source, destination, dateTime);
        repoMessagesDB.addMessage(messagex);

        notifyObservers(new GuiEntityChangeEvent(ChangeEventType.ADD));

    }

    public List<Message> getMessages(String userSource, String userDestination) {
        List<Message> list = repoMessagesDB.getMessages();
        List<Message> conversation = new ArrayList<>();
        for (Message message : list) {
            if (Objects.equals(message.getSource(), userSource) && Objects.equals(message.getDestination(), userDestination))
                conversation.add(message);
            if (Objects.equals(message.getSource(), userDestination) && Objects.equals(message.getDestination(), userSource))
                conversation.add(message);
        }

        Collections.sort(conversation, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return conversation;
    }

    @Override
    public void addObserver(Observer<GuiEntityChangeEvent> e) {
        observersMsg.add(e);
    }

    @Override
    public void removeObserver(Observer<GuiEntityChangeEvent> e) {
    }

    @Override
    public void notifyObservers(GuiEntityChangeEvent t) {
        observersMsg.stream().forEach(x -> x.update(t));
    }
}
