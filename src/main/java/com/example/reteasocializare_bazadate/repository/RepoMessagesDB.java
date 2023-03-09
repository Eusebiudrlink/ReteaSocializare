package com.example.reteasocializare_bazadate.repository;

import com.example.reteasocializare_bazadate.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepoMessagesDB {
    private final JDBCUtils jdbcUtils = new JDBCUtils();
    private List<Message> messages = new ArrayList<>();

    public RepoMessagesDB() {
        findAll();
    }

    public Message save(Message entity) {

        String sql = "insert into messages(message, source, destination,date) values (?, ?, ?,?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getMessage());
            ps.setString(2, entity.getSource());
            ps.setString(3, entity.getDestination());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = entity.getDate().format(formatter);
            ps.setString(4, formattedDateTime);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<Message> findAll() {
        messages = new ArrayList<>();
        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String message = resultSet.getString("message");
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                String date = resultSet.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

                Message messageX = new Message(message, source, destination, dateTime);
                messages.add(messageX);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;

    }

    public void addMessage(Message messagex) {
        messages.add(messagex);
        save(messagex);
    }

    public List<Message> getMessages() {
        findAll();
        return messages;
    }

}
