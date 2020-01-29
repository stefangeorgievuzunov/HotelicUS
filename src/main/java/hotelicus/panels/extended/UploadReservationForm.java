package hotelicus.panels.extended;

import hotelicus.entities.*;
import hotelicus.enums.ReservationTypes;
import hotelicus.enums.RoomCategories;
import hotelicus.enums.RoomStatus;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import org.hibernate.criterion.Restrictions;


import javax.xml.transform.Result;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.ReservationStatus.ACTIVE;
import static hotelicus.enums.RoomStatus.FREE;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;

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
    private ListView<String> services;
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
    private List<Rooms> pickedRooms;
    private Clients pickedClient;
    private Reservations newReservation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            services.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            DbController<Clients> retrieveAllClients = new DbController<>(Clients.class);
            DbController<Reservations> retriveAllReservations = new DbController<>(Reservations.class);
            DbController<ReservationRooms> retrieveAllBusyRooms = new DbController<>(ReservationRooms.class);
            DbController<Rooms> retrieveAllRooms = new DbController<>(Rooms.class);


            this.newReservation = new Reservations();
            this.pickedClient = new Clients();


            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            this.searchClientById.textProperty().addListener((observable, oldValue, newValue) -> {
                pause.setOnFinished(e -> {
                    try {
                        if (Integer.parseInt(newValue) > 0) {
                            List<Clients> result = retrieveAllClients.select(Restrictions.eq("id", Integer.parseInt(newValue))); //TODO add restrictions for current hotel.
                            if (!result.isEmpty()) {
                                this.clientsTableView.getItems().clear();
                                result.forEach(client -> {
                                    this.clientsTableView.getItems().add(client);
                                });
                                this.addNewClient.setVisible(false);
                            } else {
                                this.clientsTableView.getItems().clear();
                                this.addNewClient.setVisible(true);
                            }
                        }
                    } catch (NumberFormatException excep) {
                        this.clientsTableView.getItems().clear();
                    }
                });
                pause.playFromStart();
            });


            clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            clientRateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
            addClientColumn.setCellFactory(ActionButtonTrigger.<Clients>forTableColumn("Select", CHANGE_STATUS_BUTTON_STYLE, this.clientsTableView, (Clients client) -> {
                if (this.pickedClient == client) {
                    this.pickedClient = null;
                } else {
                    this.pickedClient = client;
                }
                return client;
            }));


            roomCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("roomId"));
            roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            takeRoomColumn.setCellFactory(ActionButtonTrigger.<Rooms>forTableColumn("Select", CHANGE_STATUS_BUTTON_STYLE, this.clientsTableView, (Rooms room) -> {
                pickRoom(room);
                return room;
            }));


            this.roomCategory.setOnAction(e -> {
                this.roomsTableView.getItems().clear();

                try {
                    if (this.reserveFrom.getValue().isBefore(this.reserveTo.getValue()) && this.reserveTo.getValue().isAfter(this.reserveFrom.getValue())) {

                        List<Reservations> allReservations = retriveAllReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
                                Restrictions.or(
                                        Restrictions.between("reservedFrom", this.reserveFrom.getValue(), this.reserveTo.getValue()),
                                        Restrictions.between("reservedTo", this.reserveFrom.getValue(), this.reserveTo.getValue()),
                                        Restrictions.and(Restrictions.ge("reservedTo", this.reserveTo.getValue()), Restrictions.between("reservedFrom", this.reserveFrom.getValue(), this.reserveTo.getValue())),
                                        Restrictions.and(Restrictions.le("reservedFrom", this.reserveFrom.getValue()), Restrictions.between("reservedTo", this.reserveFrom.getValue(), this.reserveTo.getValue())),
                                        Restrictions.and(Restrictions.le("reservedFrom", this.reserveFrom.getValue()), Restrictions.ge("reservedTo", this.reserveTo.getValue()))
                                ));
                        List<Rooms> freeRooms = retrieveAllRooms.select(Restrictions.eq("category", this.roomCategory.getSelectionModel().getSelectedItem()),
                                Restrictions.eq("status", FREE), Restrictions.eq("hotel", this.hotel));

                        List<Rooms> busyRooms = new ArrayList<>();

                        for (Reservations reservation : allReservations) {
                            List<ReservationRooms> busyRoomz = retrieveAllBusyRooms.select(Restrictions.eq("reservation", reservation));
                            for (ReservationRooms record : busyRoomz) {
                                if (record.getRoom().getCategory() == this.roomCategory.getSelectionModel().getSelectedItem() && record.getRoom().getStatus() == FREE) {
                                    busyRooms.add(record.getRoom());
                                }
                            }
                        }

                        freeRooms.removeAll(busyRooms);
                        freeRooms.forEach(r -> this.roomsTableView.getItems().add(r));
                        this.roomCategory.getSelectionModel().clearSelection();
                    }
                } catch (SelectNullObjectException excep) {
                    excep.printStackTrace();
                } catch (NullPointerException excep) {
                    new Error("Search failed", "Please choose period and category !");
                    excep.printStackTrace();
                }
            });

            this.uploadInfo();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    private void pickRoom(Rooms room) {
        try {
            if (room != null) {

            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    private void uploadInfo() {
        DbController<Services> retrieveAllServices = new DbController<>(Services.class);
        List<Services> allServices = retrieveAllServices.findAll();
        allServices.forEach(s -> this.services.getItems().add("Name: " + s.getName() + " Price: " + s.getPrice()));

        DbController<Clients> retrieveAllClients = new DbController<>(Clients.class);
        List<Clients> allClients = retrieveAllClients.findAll();
        allClients.forEach(c -> this.clientsTableView.getItems().add(c));
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
            if (confirm.getConfirmationResult() == true && this.newReservation != null && this.formValidation()) {

            }
        } catch (NumberFormatException excep) {
            excep.printStackTrace();
            new Error("Upload failed", "Price must be numeric!");
        }
    }

    private boolean formValidation() {
        try {
            if (!this.reservationType.getSelectionModel().isEmpty() && !this.paymentType.getSelectionModel().isEmpty() && !this.cancelingType.getSelectionModel().isEmpty() && pickedClient != null && !pickedRooms.isEmpty()) {
                if (Double.parseDouble(this.paidMoney.getText()) < this.newReservation.getTotalSum()) {
                    return true;
                } else {
                    new Error("Upload failed", "Paid money should be less or equal to Total sum !");
                }
            } else {
                new Error("Upload failed", "There are empty fields!");
            }
        } catch (NumberFormatException excep) {
            excep.printStackTrace();
            new Error("Upload failed", "Paid money must be numeric!");
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
        return false;
    }

    public TableView<Reservations> getParentTableView() {
        return parentTableView;
    }

    public void setParentTableView(TableView<Reservations> parentTableView) {
        try {
            if (parentTableView != null) {
                this.parentTableView = parentTableView;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        try {
            if (hotel != null) {
                this.hotel = hotel;
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

}
