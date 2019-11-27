package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UserDbController;
import hotelicus.entities.Users;
import hotelicus.window.Error;
import javafx.fxml.FXML;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.NonUniqueResultException;


public class LoginController {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public LoginController() {
    }

    @FXML
    private void loginRouter() {
        if (loginValidation(username.getText(), password.getText())) {
            UserDbController loginControl = new UserDbController();
            try{
                Users loggedUser=loginControl.selectUniqueUser(username.getText());
                if(loggedUser!=null){
                    App.setLoggedUser(loggedUser);
                }else{
                    new Error("FATAL ERROR", "User doesn't exist.");
                }
            }
            catch(NonUniqueResultException excep){
                excep.printStackTrace();
            }
            System.out.println(App.getLoggedUser().getUsername());
            App.dashboardWindow();
        }
    }

    private boolean loginValidation(String username, String password) {
        if (!username.equals("") && !password.equals("")) {
            UserDbController loginControl = new UserDbController();

            if (loginControl.usernamePasswordValidator(username, password))
                return true;
            else
                return false;
        } else {
            new Error("Failed to login", "There are empty fields!");
            return false;
        }
    }
}
