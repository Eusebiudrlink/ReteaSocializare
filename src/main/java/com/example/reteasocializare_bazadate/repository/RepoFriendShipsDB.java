package com.example.reteasocializare_bazadate.repository;


import com.example.reteasocializare_bazadate.model.FriendShip;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RepoFriendShipsDB {

    private final JDBCUtils jdbcUtils = new JDBCUtils();
    private Set<FriendShip> friendShips = new HashSet();

    public RepoFriendShipsDB() {
        findAll();
    }

    /**
     * Functie ce adauga o prietenie in baza de date
     *
     * @param entity
     * @return
     */
    public FriendShip save(FriendShip entity) {

        String sql = "insert into friendships(username1, username2, datetime,status) values (?, ?, ?,?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUsername1());
            ps.setString(2, entity.getUsername2());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = entity.getData().format(formatter);
            ps.setString(3, formattedDateTime);
            ps.setString(4, entity.getStatus());


            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Functie ce citeste toate prieteniile din baza de date
     */
    public Iterable<FriendShip> findAll() {
        friendShips = new HashSet<>();
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String username1 = resultSet.getString("username1");
                String username2 = resultSet.getString("username2");
                String dateTimeString = resultSet.getString("datetime");
                String status = resultSet.getString("status");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

                FriendShip friendShip = new FriendShip(username1, username2, dateTime, status);
                friendShips.add(friendShip);
            }
            return friendShips;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendShips;
    }

    /**
     * functie ce adauga in set-ul friendShips noua prietenie friendShip
     *
     * @param friendShip prietenie de adaugat
     */
    public void addFriendShip(FriendShip friendShip) {


        FriendShip fr = find(friendShip);

        if (fr != null && Objects.equals(fr.getStatus(), "Accepted"))
            throw new RepoException(" is already your friend");
        if (fr != null && Objects.equals(fr.getStatus(), "Waiting") && friendShip.getUsername1().equals(fr.getUsername1()))
            throw new RepoException(" has already a request from you");
        if (fr != null && Objects.equals(fr.getStatus(), "Waiting") && friendShip.getUsername1().equals(fr.getUsername2())) {
            update(friendShip.getUsername2(), friendShip.getUsername1(), "Accepted");
            throw new RepoException(" is now your friend.");
        }
        friendShips.add(friendShip);
        save(friendShip);


    }

    /**
     * functie ce sterge din set-ul frindShips prietenia friendShip
     *
     * @param friendShip prietenie de sters
     */
    public void deleteFriendShip(FriendShip friendShip) {


        int affectedRows = 0;
        int affectedRows2 = 0;

        String sql = "DELETE FROM friendships WHERE username1=? AND username2=?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, friendShip.getUsername1());
            ps.setString(2, friendShip.getUsername2());
            affectedRows = ps.executeUpdate();
            ps.setString(2, friendShip.getUsername1());
            ps.setString(1, friendShip.getUsername2());
            affectedRows2 = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (affectedRows == 0 && affectedRows2 == 0) {
            throw new RepoException("ERR:Prietenia nu exista in lista!");
        }
        friendShips.remove(friendShip);
    }

    /**
     * functie ce sterge toti prietenii user-ului cu numele username
     *
     * @param username username pentru care vom sterge toti prietenii
     */
    public void delFriendships(String username) {

        List<FriendShip> friendshipsToBeDeleted = new ArrayList<>();
        for (FriendShip friendShip : friendShips) {
            if (Objects.equals(friendShip.getUsername2(), username) || Objects.equals(friendShip.getUsername1(), username)) {
                friendshipsToBeDeleted.add(friendShip);
            }
        }

        for (FriendShip friendShip : friendshipsToBeDeleted)
            deleteFriendShip(friendShip);
    }

    /**
     * @return toate prieteniile din set ul friendShips
     */
    public Set<FriendShip> getFriendships() {
        findAll();
        return friendShips;
    }

    public FriendShip find(FriendShip friendShip) {
        for (FriendShip friendShip1 : friendShips) {
            if (Objects.equals(friendShip, friendShip1))
                return friendShip1;
        }
        return null;
    }

    public void update(String usernameAccount, String usernameFriend, String status) {

        int affectedRows = 0;

        String sql = "UPDATE friendships\n" +
                "        SET status = ?" +
                "        WHERE username1 = ? and username2 = ?;";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, usernameAccount);
            ps.setString(3, usernameFriend);
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FriendShip friendShip = new FriendShip(usernameAccount, usernameFriend, LocalDateTime.now(), status);
        friendShips.remove(friendShip);
        friendShips.add(friendShip);

    }

}
