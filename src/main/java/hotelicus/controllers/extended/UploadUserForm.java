package hotelicus.controllers.extended;
import hotelicus.App;
import hotelicus.controllers.main.AdminController;
import hotelicus.controllers.main.DbController;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.util.Date;

import static hotelicus.enums.UploadAction.*;

public class UploadUserForm {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private Button saveButton;

    private Users user;
    private UserPrivileges priviliges;
    private UploadAction uploadAction;

    public UploadUserForm(){

    }

    public void init(Users user, UserPrivileges priviliges, UploadAction uploadAction){
        this.user=user;
        this.priviliges=priviliges;
        this.uploadAction=uploadAction;
        this.uploadUserInfo();
    }

    @FXML
    private void uploadRouter()throws  IOException{
        if(this.formValidation(this.username.getText(),this.password.getText(),this.firstName.getText(),this.lastName.getText())){
           DbController<Users> updateUser=new DbController<Users>(Users.class);
            this.user.setUsername(this.username.getText());
            this.user.setPassword(this.password.getText());
            this.user.setFirstName(this.firstName.getText());
            this.user.setLastName(this.lastName.getText());
            this.user.setUserState(UserState.ACTIVE);
            this.user.setPrivileges(this.priviliges);
            if(this.user.getStartedOn()==null){
                Date startedOn=new Date();
                this.user.setStartedOn(startedOn);
            }
            boolean successfulRecord=true;

            if(this.uploadAction==EDIT){
                try{
                    updateUser.update(this.user);
                }
                catch(ConstraintViolationException excp){
                    successfulRecord=false;
                    new Error("Upload failed", "Username is busy");
                }
            }
            if(this.uploadAction==INSERT){
                try{
                    updateUser.insert(this.user);
                }
                catch(ConstraintViolationException excp){
                    successfulRecord=false;
                    new Error("Upload failed", "Username is busy");
                }
            }

            if(successfulRecord){
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        }
    }

    private  void uploadUserInfo(){
        if( this.user.getUsername()!=null && this.user.getPassword()!=null && this.user.getFirstName()!=null && this.user.getLastName()!=null){
            this.username.setText(user.getUsername());
            this.password.setText(user.getPassword());
            this.firstName.setText(user.getFirstName());
            this.lastName.setText(user.getLastName());
        }
    }

    private boolean formValidation(String username, String password, String firstName, String lastName)throws IOException {

        if (!username.equals("") && !password.equals("") && !firstName.equals("") && !lastName.equals("")){
            try{
                Users testUser=UserDbController.selectUniqueUser(username);
                if(testUser==null){
                    return true;
                }
                if(this.user.getUsername()!=null){
                    if(this.user.getUsername().equals(testUser.getUsername())){
                        return true;
                    }
                    else{
                        throw new NonUniqueResultException(0);
                    }
                }
                if(username.equals(testUser.getUsername())){
                    throw new NonUniqueResultException(0);
                }
            }
            catch(NonUniqueResultException uniqueExcep){
                new Error("Upload failed", "Username is busy");
            }
        }else{
            new Error("Upload failed", "There are empty fields!");
        }
        return false;
    }
}
