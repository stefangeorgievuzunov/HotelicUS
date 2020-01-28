package hotelicus.panels.extended;

import hotelicus.entities.*;
import hotelicus.enums.ReservationTypes;
import hotelicus.enums.RoomCategories;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class UploadReservationForm implements Initializable {
    @FXML
    private ChoiceBox<ReservationTypes> reservationType;
    @FXML
    private ChoiceBox<ReservationTypes> paymentType;
    @FXML
    private ChoiceBox<ReservationTypes> cancelingType;
    @FXML
    private ChoiceBox<RoomCategories> roomCategory;
    @FXML
    private ListView<Services> services;
    @FXML
    private TextField paidMoney;
    @FXML
    private TextField totalSum;
    @FXML
    private TextField searchClientById;
    @FXML
    private TableView<Clients> clientsTableView;
    @FXML
    private TableColumn<Clients, Integer> clientIdColumn;
    @FXML
    private TableColumn<Clients, String> firstNameColumn;
    @FXML
    private TableColumn<Clients, String> lastNameColumn;
    @FXML
    private TableColumn<Clients, String> phoneNumberColumn;
    @FXML
    private TableColumn<Clients, Integer> clientRateColumn;
    @FXML
    private TableColumn<Clients, Button> addClientColumn;
    @FXML
    private TableView<Rooms> roomsTableView;
    @FXML
    private TableColumn<Rooms, RoomCategories> roomCategoryColumn;
    @FXML
    private TableColumn<Rooms, Integer> roomCapacityColumn;
    @FXML
    private TableColumn<Rooms, String> roomNumberColumn;
    @FXML
    private TableColumn<Rooms, Double> roomPriceColumn;
    @FXML
    private TableColumn<Rooms, Button> takeRoomColumn;
    @FXML
    private DatePicker reserveFrom;
    @FXML
    private DatePicker reserveTo;
    @FXML
    private Button addNewClient;

    private TableView<Reservations> parentTableView;
    private Hotels hotel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.addNewClient.setVisible(false);

            clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            clientRateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
            addClientColumn.setCellValueFactory(new PropertyValueFactory<>("id")); //button

            roomCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
            roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            takeRoomColumn.setCellValueFactory(new PropertyValueFactory<>("rate")); //button



        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }


    }
}
