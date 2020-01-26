package hotelicus.panels.extended;

import hotelicus.panels.controllers.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.exceptions.*;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static hotelicus.enums.UploadAction.*;

public class UploadUserForm implements Initializable {
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
    private UserPrivileges privileges;
    private UploadAction uploadAction;
    private TableView parentTableView;
    private Hotels hotel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.user == null) {
            this.user = new Users();
        }
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true && this.formValidation()) {
                DbController<Users> updateUser = new DbController<Users>(Users.class);

                this.user.setUsername(this.username.getText());
                this.user.setPassword(this.password.getText());
                this.user.setFirstName(this.firstName.getText());
                this.user.setLastName(this.lastName.getText());
                this.user.setUserState(UserState.ACTIVE);
                this.user.setPrivileges(this.privileges);

                if (this.user.getStartedOn() == null) {
                    LocalDate startedOn = LocalDate.now();
                    this.user.setStartedOn(startedOn);
                }
                boolean successfulRecord = true;

                if (this.uploadAction == EDIT) {
                    updateUser.update(this.user);
                }

                if (this.uploadAction == INSERT) {
                    try {
                        updateUser.insert(this.user);
                        if (this.parentTableView != null) {
                            this.parentTableView.getItems().add(this.user);
                        }
                    } catch (ConstraintViolationException excep) {
                        successfulRecord = false;
                        new Error("Upload failed", "Username is busy");
                        excep.printStackTrace();
                    }
                }
                if (this.uploadAction == INSERT_OR_EDIT) {
                    try {
                        updateUser.insertOrUpdate(this.user);
                    } catch (ConstraintViolationException excep) {
                        successfulRecord = false;
                        new Error("Upload failed", "Username is busy");
                        excep.printStackTrace();
                    }
                }
                if (successfulRecord) {
                    if (this.hotel != null && this.user != null) {
                        this.hotel.setManager(this.user);
                    }
                    if (this.parentTableView != null) {
                        this.parentTableView.refresh();
                    }
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                }
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        } catch (InsertOrUpdateNullObjectException excep) {
            excep.printStackTrace();
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    public void uploadInfo() {
        if (this.user.getUsername() != null && this.user.getPassword() != null && this.user.getFirstName() != null && this.user.getLastName() != null) {
            this.username.setText(user.getUsername());
            this.password.setText(user.getPassword());
            this.firstName.setText(user.getFirstName());
            this.lastName.setText(user.getLastName());
        }
    }

    private boolean formValidation() {
        try {
            if (!this.username.getText().isEmpty() && !this.password.getText().isEmpty() && !this.firstName.getText().isEmpty() && !this.lastName.getText().isEmpty()) {

                DbController<Users> uniqueUser = new DbController<Users>(Users.class);
                Users testUser = uniqueUser.selectUnique(Restrictions.eq("username", this.username.getText()));
                if (testUser == null) {
                    return true;
                }
                if (this.user.getUsername() != null) {
                    if (this.user.getUsername().equals(testUser.getUsername())) {
                        return true;
                    } else {
                        throw new NonUniqueResultException(0);
                    }
                }
                if (this.username.getText().equals(testUser.getUsername())) {
                    throw new NonUniqueResultException(0);
                }
            } else {
                new Error("Upload failed", "There are empty fields!");
            }
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (NonUniqueResultException excep) {
            excep.printStackTrace();
            new Error("Upload failed", "Username is busy");
        }
        return false;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        if (user != null) {
            this.user = user;
        } else {
            throw new NullPointerException();
        }
    }

    public UserPrivileges getPrivileges() {
        return privileges;
    }

    public void setPrivileges(UserPrivileges privileges) {
        if (privileges != null) {
            this.privileges = privileges;
        } else {
            throw new NullPointerException();
        }
    }

    public UploadAction getUploadAction() {
        return uploadAction;
    }

    public void setUploadAction(UploadAction uploadAction) {
        if (uploadAction != null) {
            this.uploadAction = uploadAction;
        } else {
            throw new NullPointerException();
        }
    }

    public TableView getParentTableView() {
        return parentTableView;
    }

    public void setParentTableView(TableView parentTableView) {
        if (parentTableView != null) {
            this.parentTableView = parentTableView;
        } else {
            throw new NullPointerException();
        }
    }

    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        if (hotel != null) {
            this.hotel = hotel;
        } else {
            throw new NullPointerException();
        }
    }
}
