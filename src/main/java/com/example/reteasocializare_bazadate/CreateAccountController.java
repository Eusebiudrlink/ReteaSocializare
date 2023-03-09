package com.example.reteasocializare_bazadate;

import com.example.reteasocializare_bazadate.model.validators.UserValidator;
import com.example.reteasocializare_bazadate.model.validators.ValidationException;
import com.example.reteasocializare_bazadate.repository.RepoException;
import com.example.reteasocializare_bazadate.service.ServiceUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController {
    @FXML
    private TextField usernameTf;
    @FXML
    private PasswordField passwordF;
    @FXML
    private Label warningLabel;
    private ServiceUsers serviceUsers;

    public void setServices(ServiceUsers serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    @FXML
    public void onSignUpBtn(ActionEvent actionEvent) {
        try {
            serviceUsers.addUser(usernameTf.getText(), passwordF.getText());
            warningLabel.setText("Succesfully created!");

        } catch (RepoException e) {
        warningLabel.setText(e.getMessage());
        }
        catch (ValidationException e){
            warningLabel.setText(e.getMessage());
        }
    }

}
