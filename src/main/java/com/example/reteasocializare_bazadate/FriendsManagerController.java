package com.example.reteasocializare_bazadate;

import com.example.reteasocializare_bazadate.repository.RepoException;
import com.example.reteasocializare_bazadate.service.Service;
import com.example.reteasocializare_bazadate.service.ServiceFriendShips;
import com.example.reteasocializare_bazadate.service.ServiceMessages;
import com.example.reteasocializare_bazadate.service.ServiceUsers;
import com.example.reteasocializare_bazadate.utils.events.GuiEntityChangeEvent;
import com.example.reteasocializare_bazadate.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FriendsManagerController implements Observer<GuiEntityChangeEvent> {

    private ServiceFriendShips serviceFriendShips;
    private ServiceUsers serviceUsers;
    private Service service;

    private ServiceMessages serviceMessages;


    @FXML
    private String usernameAccount;
    @FXML
    private Label greetUsername;

    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<String> requestsListView;
    @FXML
    private ListView<String> requestsSentListView;
    @FXML
    private Button acceptFriendBtn;
    @FXML
    private Button addFriendBtn;
    @FXML
    private Label friendStatusLb;
    @FXML
    private TextField searchFriendTf;


    public void initializer() {
        greetUsername.setText("Welcome back, " + usernameAccount);
        friendsListView.getItems().addAll(serviceFriendShips.getFriends(usernameAccount));
        requestsListView.getItems().addAll(serviceFriendShips.getFriendRequestsWithDate(usernameAccount));
        requestsSentListView.getItems().addAll(serviceFriendShips.getFriendRequestsSent(usernameAccount));

    }

    void initData(String username) {
        usernameAccount = username;
    }

    @FXML
    private void onRemoveFriend(ActionEvent actionEvent) {
        int index = friendsListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Don't make a mistake!");
            alert.setHeaderText("Are you sure you want to delete this friend and make him sad?");
            alert.setContentText("Do not regret it! IT WILL BE FOR REAL!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    String friendToRemove = friendsListView.getSelectionModel().getSelectedItem();
                    serviceFriendShips.deleteFriendShip(usernameAccount, friendToRemove);
                }
            });


        }
    }

    @FXML
    private void onAddFriend(ActionEvent actionEvent) {
        try {
            serviceFriendShips.addFriendShip(usernameAccount, searchFriendTf.getText());
            friendStatusLb.setVisible(true);
            friendStatusLb.setText("Request sent!");
            friendStatusLb.setStyle("-fx-text-fill: forestgreen;");
            show_button();


        } catch (RepoException e) {
            friendStatusLb.setVisible(true);
            friendStatusLb.setStyle("-fx-text-fill: #ec4d4d;");
            friendStatusLb.setText(searchFriendTf.getText() + e.getMessage());

            show_button();
        }

    }

    private void show_button() {

        TimerTask task = new TimerTask() {
            public void run() {
                friendStatusLb.setVisible(false);
            }
        };
        Timer timer = new Timer();
        long delay = 2500L;
        timer.schedule(task, delay);
    }

    @FXML
    private void onAcceptedFriend(ActionEvent actionEvent) {
        int index = requestsListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            String friendToAccept = requestsListView.getSelectionModel().getSelectedItem();
            String[] friend = friendToAccept.split(" ", 2);

            serviceFriendShips.updateFriendShip(friend[0], usernameAccount, "Accepted");


        }
    }

    @FXML
    private void onRemoveRequest(ActionEvent actionEvent) {
        int index = requestsSentListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            String FriendRequestToDelete = requestsSentListView.getSelectionModel().getSelectedItem();
            serviceFriendShips.deleteRequestFriendShip(usernameAccount, FriendRequestToDelete);

        }
    }


    public void setServices(Service service, ServiceUsers serviceUsers, ServiceFriendShips serviceFriendShips, ServiceMessages serviceMessages) {
        this.service = service;
        this.serviceUsers = serviceUsers;
        this.serviceFriendShips = serviceFriendShips;
        this.serviceMessages = serviceMessages;
        serviceFriendShips.addObserver(this);
    }

    @Override
    public void update(GuiEntityChangeEvent guiEntityChangeEvent) {
        friendsListView.getItems().clear();
        requestsListView.getItems().clear();
        requestsSentListView.getItems().clear();
        friendsListView.getItems().addAll(serviceFriendShips.getFriends(usernameAccount));
        requestsListView.getItems().addAll(serviceFriendShips.getFriendRequestsWithDate(usernameAccount));
        requestsSentListView.getItems().addAll(serviceFriendShips.getFriendRequestsSent(usernameAccount));
    }

    @FXML
    public void onMessageFriend(ActionEvent actionEvent) throws IOException {
        int index = friendsListView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            String userDestination = friendsListView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("chat-manager.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 300);
            Stage stage = new Stage();
            stage.setTitle("Chat-"+userDestination);
            stage.setScene(scene);
            ChatController chatController = fxmlLoader.getController();
            chatController.setServiceMessages(serviceMessages);
            chatController.initializer(usernameAccount, userDestination);
            stage.show();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Nu ai selectat niciun prieten.");
            alert.setContentText("Selecteaza un prieten caruia sa-i trimiti mesaj!");
            alert.show();

        }

    }
}
