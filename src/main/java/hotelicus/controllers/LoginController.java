package hotelicus.controllers;

import hotelicus.App;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    public LoginController()
    {
    }

    @FXML
    private void printOutput() throws IOException {
        System.out.println(user.getText());
        System.out.println(password.getText());

        // Verify user input
        if (!user.getText().equals("") && !password.getText().equals("")) {
            new App().dashboardWindow();

            return;
        }
        // return error
        new Error("Failed to login", "There are empty fields!");
    }
}
