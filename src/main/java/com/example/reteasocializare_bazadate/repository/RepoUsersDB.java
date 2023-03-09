package com.example.reteasocializare_bazadate.repository;


import com.example.reteasocializare_bazadate.model.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RepoUsersDB {

    private final JDBCUtils jdbcUtils = new JDBCUtils();
    private Set<User> users = new HashSet();

    public RepoUsersDB() {

        findAll();
    }

    /**
     * Functie ce citeste toti userii din baza de date
     */
    public Iterable<User> findAll() {
        users = new HashSet<>();
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String firstName = resultSet.getString("username");
                String lastName = resultSet.getString("password");
                User user = new User(firstName, lastName);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * functie ce adauga un nou user in set ul users
     *
     * @param user user de adaugat
     */
    public void addUser(User user) {
        if (users.contains(user))
            throw new RepoException("The user exist already!");
        users.add(user);

        save(user);
    }

    /**
     * functie ce salveaza in baza de date un user
     */

    public User save(User entity) {

        String sql = "insert into users (username, password ) values (?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * functie ce sterge user ul user din setul users
     *
     * @param user user de sters
     */
    public void delUser(User user) {
        int affectedRows = 0;
        String sql = "DELETE FROM users WHERE username=?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (affectedRows == 0)
            throw new RepoException("Userul nu exista in lista!");
        users.remove(user);

    }


    /**
     * functie ce verifica daca doi useri exista in lista de useri si arunca exceptie daca nu exista
     *
     * @param user  user de verificat
     * @param user1 user de verificat
     */
    public void verifUsers(User user, User user1) {
        if (!users.contains(user) || !users.contains(user))
            throw new RepoException("The users does not have an account!");
    }

    /**
     * @return lista de users
     */
    public Set<User> getUsers() {
        findAll();
        return users;
    }

    /**
     * functie ce cauta exact obiectul user in lista users
     *
     * @param user userul de cautat
     * @return obiectul daca exista in lista sau null
     */
    public User find(User user) {
        for (User user1 : users) {
            if (Objects.equals(user1, user))
                return user1;
        }
        return null;
    }

    public void update(User user, String passwd) {
        int affectedRows = 0;


        String sql = "UPDATE users\n" +
                "        SET password = ?" +
                "        WHERE username = ?;";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, passwd);
            ps.setString(2, user.getUsername());
            affectedRows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (affectedRows == 0)
            throw new RepoException("Userul nu exista in lista!");

        User user1 = find(user);
        user1.setPassword(passwd);
    }
}
