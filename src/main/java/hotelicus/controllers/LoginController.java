package hotelicus.controllers;

import hotelicus.window.Dashboard;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
//        user.setText(user.getText());
//        password.setText(password.getText());
        System.out.println(user.getText());
        System.out.println(password.getText());

        // Verify user input
        if (user.getText() != null && password.getText() != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("templates/Dashboard.xml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Dashboard");
                stage.setScene(new Scene(root));
                stage.show();
        } else {
            // return error
            System.out.println("Empty fields");
        }
    }
}
