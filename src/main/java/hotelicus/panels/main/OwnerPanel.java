package hotelicus.panels.main;

import hotelicus.App;
import hotelicus.core.LoggerUtil;
import hotelicus.panels.controllers.DbController;
import hotelicus.panels.controllers.SceneController;
import hotelicus.panels.extended.ActionButtonTrigger;
import hotelicus.panels.extended.UploadHotelForm;
import hotelicus.panels.controllers.UserController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Users;
import hotelicus.enums.HotelState;
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

import static hotelicus.enums.HotelState.ACTIVE;
import static hotelicus.enums.HotelState.DISABLED;
import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UploadAction.INSERT;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;
import static hotelicus.styles.Styles.EDIT_BUTTON_STYLE;

public class OwnerPanel implements Initializable {
    @FXML
    private TableView<Hotels> tableView;
    @FXML
    private TableColumn<Hotels, String> hotelNameColumn;
    @FXML
    private TableColumn<Hotels, HotelState> hotelStateColumn;
    @FXML
    private TableColumn<Hotels, LocalDate> createdOnColumn;
    @FXML
    private TableColumn<Hotels, LocalDate> removedOnColumn;
    @FXML
    private TableColumn<Hotels, Button> statusColumn;
    @FXML
    private TableColumn<Hotels, Button> editColumn;
    @FXML
    private TableColumn<Hotels, Button> viewColumn;
    @FXML
    private TextField searchHotelByName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DbController<Hotels> hotels = new DbController<Hotels>(Hotels.class);

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            this.searchHotelByName.textProperty().addListener(e -> {
                pause.setOnFinished(event -> {
                    List<Hotels> result = hotels.select(Restrictions.like("name", searchHotelByName.getText(), MatchMode.START), Restrictions.eq("owner", App.getLoggedUser()));
                    if (!result.isEmpty()) {
                        this.tableView.getItems().clear();
                        result.forEach(hotel -> {
                            this.tableView.getItems().add(hotel);
                        });
                    } else {
                        this.tableView.getItems().clear();
                    }
                });
                pause.playFromStart();
            });

            hotelNameColumn.setCellValueFactory(new PropertyValueFactory<Hotels, String>("name"));
            hotelStateColumn.setCellValueFactory(new PropertyValueFactory<Hotels, HotelState>("hotelState"));
            createdOnColumn.setCellValueFactory(new PropertyValueFactory<Hotels, LocalDate>("createdOn"));
            removedOnColumn.setCellValueFactory(new PropertyValueFactory<Hotels, LocalDate>("removedOn"));

            statusColumn.setCellFactory(ActionButtonTrigger.<Hotels>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, tableView, (Hotels hotel) -> {
                if (hotel.getHotelState() == ACTIVE) {
                    changeHotelStatus(hotel, DISABLED);
                } else {
                    changeHotelStatus(hotel, ACTIVE);
                }
                return hotel;
            }));

            editColumn.setCellFactory(ActionButtonTrigger.<Hotels>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Hotels hotel) -> {
                try {
                    SceneController.openNewScene(UploadHotelForm.class, "Edit hotel", () -> {
                        UploadHotelForm uploadHotelForm = SceneController.getStageAccessTo(UploadHotelForm.class);
                        uploadHotelForm.setUploadAction(EDIT);
                        uploadHotelForm.setParentTableView(this.tableView);
                        uploadHotelForm.setHotel(hotel);
                        uploadHotelForm.uploadInfo();
                    });

                } catch (IOException excep) {
                    excep.printStackTrace();
                    LoggerUtil.error(excep.getMessage());
                } catch (NullPointerException excep) {
                    excep.printStackTrace();
                    LoggerUtil.error(excep.getMessage());
                }
                return hotel;
            }));

            viewColumn.setCellFactory(ActionButtonTrigger.<Hotels>forTableColumn("View", EDIT_BUTTON_STYLE, tableView, (Hotels hotel) -> {
//            try {
//                LoadExtendedWindow.loadUploadUserFormWindow(this.tableView, "Edit user", EDIT, user, OWNER);
//            } catch (IOException excep) {
//                System.out.println(excep.getMessage());
//            }
                return hotel;
            }));

            this.loadHotels(null);
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }


    @FXML
    private void filterActivate() {
        this.loadHotels(ACTIVE);
    }

    @FXML
    private void filterDisable() {
        this.loadHotels(DISABLED);
    }

    @FXML
    private void logOut() {
        UserController.logOut();
    }

    @FXML
    private void addHotel() {
        try {
            SceneController.openNewScene(UploadHotelForm.class, "Add new Hotel", () -> {
                UploadHotelForm uploadHotelForm = SceneController.getStageAccessTo(UploadHotelForm.class);
                uploadHotelForm.setUploadAction(INSERT);
                uploadHotelForm.setParentTableView(this.tableView);
                uploadHotelForm.uploadInfo();
            });
        } catch (IOException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    private void changeHotelStatus(Hotels hotel, HotelState state) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
            if (confirm.getConfirmationResult() == true && hotel != null && state != null) {
                LocalDate deletedOn = LocalDate.now();
                if (hotel.getManager() != null) {
                    DbController<Users> updateManager = new DbController<>(Users.class);
                    if (state == DISABLED) {
                        hotel.getManager().setUserState(UserState.DISABLED);
                        hotel.getManager().setEndedOn(deletedOn);
                    } else {
                        hotel.getManager().setUserState(UserState.ACTIVE);
                        hotel.getManager().setEndedOn(null);
                    }
                    updateManager.update(hotel.getManager());
                }
                if (state == DISABLED) {
                    hotel.setRemovedOn(deletedOn);
                } else {
                    hotel.setRemovedOn(null);
                }
                hotel.setHotelState(state);
                DbController<Hotels> hotels = new DbController<Hotels>(Hotels.class);
                hotels.update(hotel);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    private void loadHotels(HotelState hotelState) {
        try {
            tableView.getItems().clear();
            DbController<Hotels> retrieveHotel = new DbController<Hotels>(Hotels.class);
            List<Hotels> hotels;
            if (hotelState == ACTIVE) {
                hotels = retrieveHotel.select(Restrictions.eq("owner", App.getLoggedUser()), Restrictions.eq("hotelState", ACTIVE));

            } else if (hotelState == DISABLED) {
                hotels = retrieveHotel.select(Restrictions.eq("owner", App.getLoggedUser()), Restrictions.eq("hotelState", DISABLED));
            } else {
                hotels = retrieveHotel.select(Restrictions.eq("owner", App.getLoggedUser()));
            }
            for (Hotels hotel : hotels) {
                this.tableView.getItems().add(hotel);
            }
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    public TableView<Hotels> getTableView() {
        return tableView;
    }
}
