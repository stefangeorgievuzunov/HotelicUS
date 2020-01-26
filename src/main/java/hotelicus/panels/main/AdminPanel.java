package hotelicus.panels.main;

import hotelicus.panels.controllers.DbController;
import hotelicus.panels.controllers.SceneController;
import hotelicus.panels.extended.ActionButtonTrigger;
import hotelicus.panels.extended.UploadUserForm;
import hotelicus.panels.controllers.UserController;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import hotelicus.window.Confirmation;

import javafx.animation.PauseTransition;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UploadAction.INSERT;
import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.enums.UserState.ACTIVE;
import static hotelicus.enums.UserState.DISABLED;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;
import static hotelicus.styles.Styles.EDIT_BUTTON_STYLE;

public class AdminPanel implements Initializable {
    @FXML
    private TableView<Users> tableView;
    @FXML
    private TableColumn<Users, String> usernameColumn;
    @FXML
    private TableColumn<Users, String> passwordColumn;
    @FXML
    private TableColumn<Users, UserPrivileges> privilegesColumn;
    @FXML
    private TableColumn<Users, String> firstNameColumn;
    @FXML
    private TableColumn<Users, String> lastNameColumn;
    @FXML
    private TableColumn<Users, UserState> userStateColumn;
    @FXML
    private TableColumn<Users, LocalDate> startedOnColumn;
    @FXML
    private TableColumn<Users, LocalDate> endedOnColumn;
    @FXML
    private TableColumn<Users, Button> statusColumn;
    @FXML
    private TableColumn<Users, Button> editColumn;
    @FXML
    private TextField searchUserByName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DbController<Users> retrieveUsers = new DbController<Users>(Users.class);

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            this.searchUserByName.textProperty().addListener(e -> {
                pause.setOnFinished(event -> {
                    List<Users> result = retrieveUsers.select(Restrictions.like("firstName", searchUserByName.getText(), MatchMode.START));
                    if (!result.isEmpty()) {
                        this.tableView.getItems().clear();
                        result.forEach(user -> this.tableView.getItems().add(user));
                    } else {
                        this.tableView.getItems().clear();
                    }
                });
                pause.playFromStart();
            });

            usernameColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("password"));
            privilegesColumn.setCellValueFactory(new PropertyValueFactory<Users, UserPrivileges>("privileges"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<Users, String>("lastName"));
            userStateColumn.setCellValueFactory(new PropertyValueFactory<Users, UserState>("userState"));
            startedOnColumn.setCellValueFactory(new PropertyValueFactory<Users, LocalDate>("startedOn"));
            endedOnColumn.setCellValueFactory(new PropertyValueFactory<Users, LocalDate>("endedOn"));

            statusColumn.setCellFactory(ActionButtonTrigger.<Users>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, tableView, (Users user) -> {
                if (user.getUserState() == ACTIVE) {
                    disableUser(user);
                } else {
                    activateUser(user);
                }
                return user;
            }));

            editColumn.setCellFactory(ActionButtonTrigger.<Users>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Users user) -> {
                try {
                    SceneController.openNewScene(UploadUserForm.class, "Edit user", () -> {
                        UploadUserForm uploadUserForm = SceneController.getStageAccessTo(UploadUserForm.class);
                        uploadUserForm.setParentTableView(tableView);
                        uploadUserForm.setPrivileges(OWNER);
                        uploadUserForm.setUploadAction(EDIT);
                        uploadUserForm.setUser(user);
                        uploadUserForm.uploadInfo();
                    });

                } catch (IOException excep) {
                    System.out.println(excep.getMessage());
                }
                return user;
            }));

            this.loadUsers(null);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void filterActivate() {
        this.loadUsers(ACTIVE);
    }

    @FXML
    private void filterDisable() {
        this.loadUsers(DISABLED);
    }

    @FXML
    private void logOut() {
        UserController.logOut();
    }

    @FXML
    private void addUser() {
        try {
            SceneController.openNewScene(UploadUserForm.class, "Add new user", () -> {
                UploadUserForm uploadUserForm = SceneController.getStageAccessTo(UploadUserForm.class);
                uploadUserForm.setParentTableView(this.tableView);
                uploadUserForm.setPrivileges(OWNER);
                uploadUserForm.setUploadAction(INSERT);
            });
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    private void activateUser(Users user) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to activate this user?");
            if (confirm.getConfirmationResult() == true && user != null) {
                DbController<Users> activateUser = new DbController<Users>(Users.class);
                List<Users> users = activateUser.select(Restrictions.eq("privileges", OWNER));

                user.setEndedOn(null);
                user.setUserState(ACTIVE);
                activateUser.update(user);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void disableUser(Users user) {
        try {

            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this user?");
            if (confirm.getConfirmationResult() == true && user != null) {
                DbController<Users> disableUser = new DbController<Users>(Users.class);
                List<Users> users = disableUser.select(Restrictions.eq("privileges", OWNER));

                LocalDate deletedOn = LocalDate.now();

                user.setEndedOn(deletedOn);
                user.setUserState(DISABLED);
                disableUser.update(user);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void loadUsers(UserState userState) {
        try {
            tableView.getItems().clear();

            DbController<Users> retrieveUsers = new DbController<Users>(Users.class);
            List<Users> users = retrieveUsers.select(Restrictions.eq("privileges", OWNER));

            for (Users user : users) {
                if (userState == ACTIVE) {
                    if (user.getPrivileges() == OWNER && user.getUserState() == ACTIVE) {
                        this.tableView.getItems().add(user);
                    }
                } else if (userState == DISABLED) {
                    if (user.getPrivileges() == OWNER && user.getUserState() == DISABLED) {
                        this.tableView.getItems().add(user);
                    }
                } else {
                    if (user.getPrivileges() == OWNER) {
                        this.tableView.getItems().add(user);
                    }
                }
            }
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }
}
