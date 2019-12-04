package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.controllers.extended.Users.LoadExtendedWindow;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.entities.Hotels;
import hotelicus.enums.HotelState;
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
import org.hibernate.sql.Update;

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

    private DbController<Hotels> hotels;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.hotels = new DbController<Hotels>(Hotels.class);

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            this.searchHotelByName.textProperty().addListener(e -> {
                pause.setOnFinished(event -> {
                    List<Hotels> result = this.hotels.select(Restrictions.like("name", searchHotelByName.getText(), MatchMode.START), Restrictions.eq("owner", App.getLoggedUser()));
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

            statusColumn.setCellFactory(ActionButtonTableCell.<Hotels>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, tableView, (Hotels hotel) -> {
                if (hotel.getHotelState() == ACTIVE) {
                    disableHotel(hotel);
                } else {
                    activateHotel(hotel);
                }
                return hotel;
            }));

            editColumn.setCellFactory(ActionButtonTableCell.<Hotels>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Hotels hotel) -> {
                try {
                    LoadExtendedWindow.loadUploadHotelFormWindow(this.tableView, "Edit hotel", hotel, EDIT);
                } catch (IOException excep) {
                    excep.printStackTrace();
                }
                return hotel;
            }));

            viewColumn.setCellFactory(ActionButtonTableCell.<Hotels>forTableColumn("View", EDIT_BUTTON_STYLE, tableView, (Hotels hotel) -> {
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
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
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
            LoadExtendedWindow.loadUploadHotelFormWindow(this.tableView, "Add new hotel", null, INSERT);
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    private void activateHotel(Hotels hotel) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to activate this hotel?");
            if (confirm.getConfirmationResult() == true && hotel != null) {
                hotel.setRemovedOn(null);
                hotel.setHotelState(ACTIVE);
                this.hotels.update(hotel);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void disableHotel(Hotels hotel) {
        try {
            LocalDate deletedOn = LocalDate.now();
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
            if (confirm.getConfirmationResult() == true && hotel != null) {
                hotel.setRemovedOn(deletedOn);
                hotel.setHotelState(DISABLED);
                this.hotels.update(hotel);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void loadHotels(HotelState hotelState) {
        try {
            tableView.getItems().clear();

            List<Hotels> hotels;
            if (hotelState == ACTIVE) {
                hotels = this.hotels.select(Restrictions.eq("owner", App.getLoggedUser()), Restrictions.eq("hotelState", ACTIVE));

            } else if (hotelState == DISABLED) {
                hotels = this.hotels.select(Restrictions.eq("owner", App.getLoggedUser()), Restrictions.eq("hotelState", DISABLED));
            } else {
                hotels = this.hotels.select(Restrictions.eq("owner", App.getLoggedUser()));
            }
            for (Hotels hotel : hotels) {
                this.tableView.getItems().add(hotel);
            }
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }
}
