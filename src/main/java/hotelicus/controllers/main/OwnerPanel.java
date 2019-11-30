package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.controllers.extended.Users.UserDbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Users;
import hotelicus.enums.HotelState;
import hotelicus.window.Confirmation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.HotelState.ACTIVE;
import static hotelicus.enums.HotelState.DISABLED;
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
    private TextField searchUserByName;

    private DbController<Hotels> hotels;
    private Users loggedUser=App.getLoggedUser();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.hotels = new DbController<Hotels>(Hotels.class);


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
//            try {
//                LoadExtendedWindow.loadUploadUserFormWindow(this.tableView, "Edit user", EDIT, user, OWNER);
//            } catch (IOException excep) {
//                System.out.println(excep.getMessage());
//            }
            return hotel;
        }));

        this.loadHotels(null);
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
        Confirmation logConfirmation = new Confirmation("Message", "Are you sure you want to log off ?");
        if (logConfirmation.getConfirmationResult() == true) {
            App.loginWindow();
        }
    }

    @FXML
    private void addHotel() {
//        try {
//            LoadExtendedWindow.loadUploadUserFormWindow(this.tableView, "Add new user", INSERT, null, OWNER);
//        } catch (IOException excep) {
//            excep.printStackTrace();
//        }
    }

    private void activateHotel(Hotels hotel) {
        Confirmation confirm = new Confirmation("Confirmation", "Do you want to activate this hotel?");
        if (confirm.getConfirmationResult() == true) {
            hotel.setRemovedOn(null);
            hotel.setHotelState(ACTIVE);
            this.hotels.update(hotel);
            this.tableView.refresh();
        }
    }

    private void disableHotel(Hotels hotel) {
        LocalDate deletedOn = LocalDate.now();
        Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
        if (confirm.getConfirmationResult() == true) {
            hotel.setRemovedOn(deletedOn);
            hotel.setHotelState(DISABLED);
            this.hotels.update(hotel);
            this.tableView.refresh();
        }
    }

    private void loadHotels(HotelState hotelState) {
        tableView.getItems().clear();

        List<Hotels> hotels;
        if (hotelState == ACTIVE) {
            hotels = UserDbController.selectOwnerHotels(this.loggedUser,ACTIVE);

        } else if (hotelState == DISABLED) {
            hotels = UserDbController.selectOwnerHotels(this.loggedUser,DISABLED);
        } else {
            hotels = this.hotels.selectEqualTo("owner",this.loggedUser,false);
        }

        for (Hotels hotel : hotels)
            this.tableView.getItems().add(hotel);
    }
}
