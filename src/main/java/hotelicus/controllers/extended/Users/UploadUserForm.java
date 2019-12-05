package hotelicus.controllers.extended.Users;

import hotelicus.controllers.main.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.exceptions.*;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDate;

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
    private UserPrivileges privileges;
    private UploadAction uploadAction;
    private TableView tableView;
    private Hotels hotel;

    public UploadUserForm() {

    }

    public void init(TableView tableVliew, Users user, UserPrivileges priviliges, UploadAction uploadAction) {
        this.tableView = tableVliew;
        if (priviliges != null && uploadAction != null) {
            this.privileges = priviliges;
            this.uploadAction = uploadAction;
        } else {
            throw new NullPointerException();
        }
        if (user != null) {
            this.user = user;
        } else {
            this.user = new Users();
        }

        this.uploadUserInfo();
    }

    public void init(TableView tableVliew, Hotels hotel, Users user, UserPrivileges priviliges, UploadAction uploadAction) {
        this.tableView = tableVliew;
        if (priviliges != null && uploadAction != null && hotel != null) {
            this.hotel = hotel;
            this.privileges = priviliges;
            this.uploadAction = uploadAction;
        } else {
            throw new NullPointerException();
        }
        if (user != null) {
            this.user = user;
        } else {
            this.user = new Users();
        }

        this.uploadUserInfo();
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true) {
                if (this.formValidation()) {
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
                            if (this.tableView != null) {
                                this.tableView.getItems().add(this.user);
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
                        if (this.hotel != null) {
                            this.hotel.setManager(this.user);
                        }
                        if (this.tableView != null) {
                            this.tableView.refresh();
                        }
                        Stage stage = (Stage) saveButton.getScene().getWindow();
                        stage.close();
                    }
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

    private void uploadUserInfo() {
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
                Users testUser = uniqueUser.selectUnique(Restrictions.eq("username", username.getText()));
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
}
