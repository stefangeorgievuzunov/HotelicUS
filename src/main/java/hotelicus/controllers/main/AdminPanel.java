package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.controllers.extended.Users.LoadExtendedWindow;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
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

    private DbController<Users> users;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.users = new DbController<Users>(Users.class);

        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        this.searchUserByName.textProperty().addListener(e -> {
            pause.setOnFinished(event -> {
                List<Users> result = users.select(Restrictions.like("firstName", searchUserByName.getText(), MatchMode.START));
                if (!result.isEmpty()) {
                    tableView.getItems().clear();
                    result.forEach(user -> tableView.getItems().add(user));
                } else {
                    tableView.getItems().clear();
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

        statusColumn.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, tableView, (Users user) -> {
            if (user.getUserState() == ACTIVE) {
                disableUser(user);
            } else {
                activateUser(user);
            }
            return user;
        }));

        editColumn.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Users user) -> {
            try {
                LoadExtendedWindow.loadUploadUserFormWindow(this.tableView, "Edit user", EDIT, user, OWNER);
            } catch (IOException excep) {
                System.out.println(excep.getMessage());
            }
            return user;
        }));

        this.loadUsers(null);
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
        Confirmation logConfirmation = new Confirmation("Message", "Are you sure you want to log off ?");
        if (logConfirmation.getConfirmationResult() == true) {
            UserController.setUserLoggedOff(App.getLoggedUser());
            App.loginWindow();
        }
    }

    @FXML
    private void addUser() {
        try {
            LoadExtendedWindow.loadUploadUserFormWindow(this.tableView, "Add new user", INSERT, null, OWNER);
        } catch (IOException excep) {
            excep.printStackTrace();
        }
    }

    private void activateUser(Users user) {
        Confirmation confirm = new Confirmation("Confirmation", "Do you want to activate this user?");
        if (confirm.getConfirmationResult() == true) {
            user.setEndedOn(null);
            user.setUserState(ACTIVE);
            this.users.update(user);
            this.tableView.refresh();
        }
    }

    private void disableUser(Users user) {
        LocalDate deletedOn = LocalDate.now();
        Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this user?");
        if (confirm.getConfirmationResult() == true) {
            user.setEndedOn(deletedOn);
            user.setUserState(DISABLED);
            this.users.update(user);
            this.tableView.refresh();
        }
    }

    private void loadUsers(UserState userState) {
        tableView.getItems().clear();
        List<Users> users = this.users.select(Restrictions.eq("privileges", OWNER));
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
    }
}
