package hotelicus.controllers;

import hotelicus.window.Dashboard;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    public LoginController()
    {
    }

    @FXML
    private void initialize()
    {
    }

    @FXML
    private void printOutput() throws IOException {
        System.out.println(user.getText());
        System.out.println(password.getText());

        // Verify user input
        if (!user.getText().equals("") && !password.getText().equals("")) {
            new Dashboard();
        } else {
            // return error
            new Error("Failed to login", "There are empty fields!");
        }
    }
}
