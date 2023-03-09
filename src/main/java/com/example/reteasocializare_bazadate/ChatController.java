package com.example.reteasocializare_bazadate;

import com.example.reteasocializare_bazadate.model.Message;
import com.example.reteasocializare_bazadate.service.ServiceMessages;
import com.example.reteasocializare_bazadate.utils.events.GuiEntityChangeEvent;
import com.example.reteasocializare_bazadate.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ChatController implements Observer<GuiEntityChangeEvent> {

    private ServiceMessages serviceMessages;
    private String userSource;
    private String userDestination;

    @FXML
    private TextArea MessagesTextArea;
    @FXML
    private Button btnSend;
    @FXML
    private TextField contentTf;

    @FXML
    private ScrollPane scrollPaneConversation;

    @FXML
    public void initializer(String userSource, String userDestination) {
        this.userSource = userSource;
        this.userDestination = userDestination;
        reloadPane();
        serviceMessages.addObserver(this);

    }

    private void reloadPane() {
        Iterable<Message> conversation = serviceMessages.getMessages(userSource, userDestination);
        VBox vBox = new VBox();
        vBox.setPrefWidth(470);
        vBox.setPrefHeight(200);
        for (Message msg : conversation) {
            if (msg.getSource().equals(userSource)) {

                Label labelMessage = new Label();
                Label labelLastName = new Label();
                Label labelSpace = new Label();
                Pane paneMessage=new Pane();
                labelSpace.setText("");
                labelMessage.setText(msg.getMessage());
                //paneMessage.getChildren().add(labelMessage);
                //BackgroundFill background_fill = new BackgroundFill(Color.AQUA, CornerRadii.EMPTY, Insets.EMPTY);
                //Background background=new Background(background_fill);
               // paneMessage.setBackground(background);
                labelLastName.setText(userSource + ":");
                VBox vBoxMessage = new VBox();
                vBoxMessage.getChildren().add(labelLastName);
                vBoxMessage.getChildren().add(labelMessage);
                vBoxMessage.getChildren().add(labelSpace);
                vBoxMessage.setAlignment(Pos.BASELINE_RIGHT);
                vBox.getChildren().add(vBoxMessage);
            } else {

                Label labelMessage = new Label();
                Label labelLastName = new Label();
                Label labelSpace = new Label();
                labelSpace.setText("");
                labelMessage.setText(msg.getMessage());
                labelLastName.setText(userDestination + ":");
                VBox vBoxMessage = new VBox();
                vBoxMessage.getChildren().add(labelLastName);
                vBoxMessage.getChildren().add(labelMessage);
                vBoxMessage.getChildren().add(labelSpace);
                vBox.getChildren().add(vBoxMessage);
            }
            scrollPaneConversation.setContent(vBox);
            scrollPaneConversation.setVvalue(1D);
        }
    }

    @FXML
    public void onSendButton(ActionEvent actionEvent) {
        String content = contentTf.getText();
        serviceMessages.addMessage(content, userSource, userDestination);
        reloadPane();
    }

    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    @Override
    public void update(GuiEntityChangeEvent guiEntityChangeEvent) {
        reloadPane();
    }
}
