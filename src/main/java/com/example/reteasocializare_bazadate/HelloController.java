package com.example.reteasocializare_bazadate;


import com.example.reteasocializare_bazadate.model.User;
import com.example.reteasocializare_bazadate.model.validators.FriendShipValidator;
import com.example.reteasocializare_bazadate.model.validators.UserValidator;
import com.example.reteasocializare_bazadate.repository.RepoException;
import com.example.reteasocializare_bazadate.repository.RepoFriendShipsDB;
import com.example.reteasocializare_bazadate.repository.RepoMessagesDB;
import com.example.reteasocializare_bazadate.repository.RepoUsersDB;
import com.example.reteasocializare_bazadate.service.Service;
import com.example.reteasocializare_bazadate.service.ServiceFriendShips;
import com.example.reteasocializare_bazadate.service.ServiceMessages;
import com.example.reteasocializare_bazadate.service.ServiceUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloController {

    private RepoUsersDB repoUsersDB;
    private RepoFriendShipsDB repoFriendShipsDB;
    private ServiceFriendShips serviceFriendShips;
    private ServiceUsers serviceUsers;
    private Service service;
    private ServiceMessages serviceMessages;

    public void initialize() {
        repoUsersDB = new RepoUsersDB();
        repoFriendShipsDB = new RepoFriendShipsDB();
        serviceFriendShips = new ServiceFriendShips(repoFriendShipsDB, new FriendShipValidator());
        serviceUsers = new ServiceUsers(repoUsersDB, new UserValidator());
        service = new Service(repoUsersDB, repoFriendShipsDB);
        serviceMessages = new ServiceMessages(new RepoMessagesDB());
    }

    @FXML
    private TextField usernameTf;
    @FXML
    private TextField passwordTf;
    @FXML
    private Label wrongCredidentials;

    @FXML
    private void onLoginClick(ActionEvent actionEvent) {
        User credidentialUser = new User(
                usernameTf.getText(),
                passwordTf.getText());
        try {
            serviceUsers.loginTest(credidentialUser);

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("friends-manager.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 635, 453);
            Stage stage = new Stage();
            stage.setTitle("Social Network");
            stage.setScene(scene);

            FriendsManagerController friendsManagerController = fxmlLoader.getController();
            friendsManagerController.setServices(service, serviceUsers, serviceFriendShips, serviceMessages);
            friendsManagerController.initData(usernameTf.getText());
            friendsManagerController.initializer();
            stage.show();

        } catch (RepoException e) {
            wrongCredidentials.setText(e.getMessage());
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

    }
    @FXML
    private void onCreateAccount(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("create-account.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 310, 260);
            Stage stage = new Stage();
            stage.setTitle("New account");
            stage.setScene(scene);
            CreateAccountController createAccountController = fxmlLoader.getController();
            createAccountController.setServices(serviceUsers);
            stage.show();
        }
        catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
}