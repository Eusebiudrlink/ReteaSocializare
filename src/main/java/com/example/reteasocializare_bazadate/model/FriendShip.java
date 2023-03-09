package com.example.reteasocializare_bazadate.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class FriendShip {
    private String username1;
    private String username2;
    private LocalDateTime data;
    private String status;

    public FriendShip(String username1, String username2, LocalDateTime data, String status) {
        this.username1 = username1;
        this.username2 = username2;
        this.data = data;
        this.status = status;
    }

    public String getUsername1() {
        return username1;
    }

    public String getUsername2() {
        return username2;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }


    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendShip that = (FriendShip) o;
        return username1.equals(that.username1) && username2.equals(that.username2) ||
                username2.equals(that.username1) && username1.equals(that.username2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username1, username2);
    }

}
