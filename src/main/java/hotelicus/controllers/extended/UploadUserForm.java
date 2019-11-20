package hotelicus.controllers.extended;

import hotelicus.controllers.main.DbController;
import hotelicus.entities.Users;
import hotelicus.enums.UserState;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UploadUserForm {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    private Users user;
    private UserState state;

    public UploadUserForm(Users user, UserState state){
        this.uploadUserInfo();
        this.user=user;
        this.state=state;
    }
    public UploadUserForm(UserState state){
        this.uploadUserInfo();
        this.user=new Users();
        this.state=state;
    }

    @FXML
    private void uploadRouter()throws  IOException{
        if(this.formValidation(this.username.getText(),this.password.getText(),this.firstName.getText(),this.lastName.getText())){
           DbController<Users> updateUser=new DbController<Users>(Users.class);
            this.user.setUsername(this.username.getText());
            this.user.setPassword(this.password.getText());
            this.user.setFirstName(this.firstName.getText());
            this.user.setLastName(this.lastName.getText());
            this.user.setUserState(state);

            updateUser.update(this.user);
        }
    }

    private  void uploadUserInfo(){
        this.username.setText(this.user.getUsername());
        this.password.setText(this.user.getUsername());
        this.firstName.setText(this.user.getUsername());
        this.lastName.setText(this.user.getUsername());
    }

    private boolean formValidation(String username, String password, String firstName, String lastName)throws IOException {
        UserDbController usernameValidation=new UserDbController();

        if (!username.equals("") && !password.equals("") && !firstName.equals("") && !lastName.equals("")){
            Users testUser=usernameValidation.selectUniqueUser(username);
            if(testUser==null){
                return true;
            }
            else if(this.user.getUsername().equals(testUser.getUsername())){
                return true;
            }else{
                new Error("Upload failed", "Username is busy");
                return false;
            }
        }else{
            new Error("Upload failed", "There are empty fields!");
            return false;
        }
    }
}
