package com.example.reteasocializare_bazadate.service;

import com.example.reteasocializare_bazadate.model.FriendShip;
import com.example.reteasocializare_bazadate.model.validators.FriendShipValidator;
import com.example.reteasocializare_bazadate.repository.RepoFriendShipsDB;
import com.example.reteasocializare_bazadate.utils.events.ChangeEventType;
import com.example.reteasocializare_bazadate.utils.events.GuiEntityChangeEvent;
import com.example.reteasocializare_bazadate.utils.observer.Observable;
import com.example.reteasocializare_bazadate.utils.observer.Observer;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServiceFriendShips implements Observable<GuiEntityChangeEvent> {
    private RepoFriendShipsDB repoFriendShips;
    private FriendShipValidator friendShipValidator;
    private final List<Observer<GuiEntityChangeEvent>> observers = new ArrayList<>();

    public ServiceFriendShips(RepoFriendShipsDB repoFriendShips, FriendShipValidator friendShipValidator) {
        this.repoFriendShips = repoFriendShips;
        this.friendShipValidator = friendShipValidator;
    }

    /**
     * creeaza,valideaza si adauga o prietenie din repo
     *
     * @param username1 partea 1 a legaturii
     * @param username2 partea 2 a legaturii
     */
    public void addFriendShip(String username1, String username2) {
        //formatare datetime
        LocalDateTime dateTimeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTimeNow.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(formattedDateTime, formatter);
        String status = "Waiting";
        FriendShip friendShip = new FriendShip(username1, username2, dateTime, status);
        friendShipValidator.validate(friendShip);

        repoFriendShips.addFriendShip(friendShip);
        notifyObservers(new GuiEntityChangeEvent(ChangeEventType.ADD));

    }

    /**
     * valideaza si sterge obiectul de sters din repo
     *
     * @param username1 partea 1 a legaturii
     * @param username2 partea 2 a legaturii
     */
    public void deleteFriendShip(String username1, String username2) {
        FriendShip friendShip = new FriendShip(username1, username2, LocalDateTime.now(), "Acceptat");
        friendShipValidator.validate(friendShip);
        repoFriendShips.deleteFriendShip(friendShip);
        notifyObservers(new GuiEntityChangeEvent(ChangeEventType.DELETE));
    }


    /**
     * functie ce sterge toate prieteniile unui user
     *
     * @param username numele pentru care vom sterge prieteniile
     */
    public void delFriendships(String username) {
        repoFriendShips.delFriendships(username);
    }


    public void updateFriendShip(String usernameAccount, String friendToAccept, String status) {

        repoFriendShips.update(usernameAccount, friendToAccept, status);

        notifyObservers(new GuiEntityChangeEvent(ChangeEventType.UPDATE));
    }

    public Set<String> getFriends(String username) {
        Set<String> friends = new HashSet<>();
        for (FriendShip friendship : repoFriendShips.getFriendships()) {
            if (Objects.equals(friendship.getUsername1(), username) && Objects.equals(friendship.getStatus(), "Accepted")) {
                friends.add(friendship.getUsername2());
            }
            if (Objects.equals(friendship.getUsername2(), username) && Objects.equals(friendship.getStatus(), "Accepted")) {
                friends.add(friendship.getUsername1());
            }
        }
        return friends;
    }

    public List<String> getFriendRequests(String user) {
        List<String> friends = new ArrayList<String>();
        for (FriendShip friendship : repoFriendShips.getFriendships()) {
            if (Objects.equals(friendship.getUsername2(), user) && Objects.equals(friendship.getStatus(), "Waiting")) {
                friends.add(friendship.getUsername1());
            }
        }
        return friends;
    }

    public List<String> getFriendRequestsWithDate(String user) {
        List<String> friends = new ArrayList<String>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (FriendShip friendship : repoFriendShips.getFriendships()) {
            if (Objects.equals(friendship.getUsername2(), user) && Objects.equals(friendship.getStatus(), "Waiting")) {
                String data = friendship.getData().format(formatter);
                friends.add(friendship.getUsername1() + " " + data);
            }
        }
        return friends;
    }

    public List<String> getFriendRequestsSent(String usernameAccount) {
        List<String> friends = new ArrayList<String>();
        for (FriendShip friendship : repoFriendShips.getFriendships())
            if (Objects.equals(friendship.getUsername1(), usernameAccount) && Objects.equals(friendship.getStatus(), "Waiting")) {
                friends.add(friendship.getUsername2());
            }
        return friends;
    }

    public void deleteRequestFriendShip(String usernameAccount, String friendRequestToDelete) {
        FriendShip friendShip = new FriendShip(usernameAccount, friendRequestToDelete, LocalDateTime.now(), "Waiting");
        friendShipValidator.validate(friendShip);
        repoFriendShips.deleteFriendShip(friendShip);
        notifyObservers(new GuiEntityChangeEvent(ChangeEventType.DELETE));
    }

    @Override
    public void addObserver(Observer<GuiEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<GuiEntityChangeEvent> e) {

    }

    @Override
    public void notifyObservers(GuiEntityChangeEvent t) {
        observers.stream().forEach(x -> x.update(t));
    }
}
