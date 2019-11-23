package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserDbController;
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
    private void loginRouter() throws IOException {
        System.out.println(user.getText());
        System.out.println(password.getText());

        if(loginValidation(user.getText(),password.getText())){
            App.dashboardWindow();
            return;
        }
       }

    private boolean loginValidation(String username, String password){
        if (!username.equals("") && !password.equals("")) {
            UserDbController loginControl=new UserDbController();

            if(loginControl.usernamePasswordValidator(username,password))
                return true;
            else
                return false;
        }else{
            new Error("Failed to login", "There are empty fields!");
            return false;
        }
    }
}
