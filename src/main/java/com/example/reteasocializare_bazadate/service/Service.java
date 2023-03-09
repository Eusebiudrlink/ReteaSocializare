package com.example.reteasocializare_bazadate.service;


import com.example.reteasocializare_bazadate.repository.RepoUsersDB;

import com.example.reteasocializare_bazadate.model.FriendShip;
import com.example.reteasocializare_bazadate.model.User;
import com.example.reteasocializare_bazadate.repository.RepoFriendShipsDB;

import java.util.*;

public class Service {
    private RepoUsersDB repoUsers;
    private RepoFriendShipsDB repoFriendShips;

    public Service(RepoUsersDB repoUsers, RepoFriendShipsDB repoFriendShips) {
        this.repoUsers = repoUsers;
        this.repoFriendShips = repoFriendShips;
    }

    /**
     * @param user pentru stringul user vom cauta toate prieteniile acestuia
     * @return toti prietenii user ului
     */
    public Set<String> getFriends(String user) {

        Set<String> friends = new HashSet<>();
        for (FriendShip friendship : repoFriendShips.getFriendships()) {
            if (Objects.equals(friendship.getUsername1(), user)) {
                friends.add(friendship.getUsername2());
            }
            if (Objects.equals(friendship.getUsername2(), user)) {
                friends.add(friendship.getUsername1());
            }
        }
        return friends;
    }

    /**
     * @param user    reprezinta userul pentru care vom cauta toate prieteniile acestuia
     * @param visitet un set in care marcam crearea componentelor conex
     * @param network un set de useri ce reprezinta componenta conexa
     * @param lentgh  este lungimea componentei conexe
     * @return lungimea componentei conexe
     */
    private int visit(User user, Set<User> visitet, Set<User> network, int lentgh) {
        int len = lentgh;
        network.add(user);
        visitet.add(user);
        for (String friend : getFriends(user.getUsername())) {
            User friendU = repoUsers.find(new User(friend));
            if (!visitet.contains(friendU)) {
                len++;
                len += visit(friendU, visitet, network, len);
            }
        }
        return len;
    }

    /**
     * determină toate rețelele din aplicație
     *
     * @return o listă de mulțimi de useri
     */
    public List<Set<User>> networks() {
        Set<User> visited = new HashSet<>();
        List<Set<User>> networksSet = new ArrayList<>();
        for (User user : repoUsers.getUsers()) {
            if (!visited.contains(user)) {
                Set<User> newSet = new HashSet<>();
                networksSet.add(newSet);
                int length = visit(user, visited, newSet, 0);
            }
        }
        return networksSet;
    }


}
