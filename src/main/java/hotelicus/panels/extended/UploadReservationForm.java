package hotelicus.panels.extended;

import hotelicus.App;
import hotelicus.core.LoggerUtil;
import hotelicus.entities.*;
import hotelicus.enums.*;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.panels.controllers.SceneController;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static hotelicus.enums.ReservationStatus.ACTIVE;
import static hotelicus.enums.RoomStatus.FREE;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;

public class UploadReservationForm implements Initializable {
    @FXML
    private ChoiceBox<ReservationTypes> reservationType;
    @FXML
    private ChoiceBox<PaymentTypes> paymentType;
    @FXML
    private ChoiceBox<ReservationCanceling> cancelingType;
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
    private Button saveButton;

    private TableView<Reservations> parentTableView;
    private Hotels hotel;
    private List<Rooms> pickedRooms = new ArrayList<>();
    private Clients pickedClient = new Clients();
    private Reservations newReservation = new Reservations();
    private List<Services> pickedServices = new ArrayList<>();
    private Map<String, Services> loadedServices = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DbController<Clients> retrieveAllClients = new DbController<>(Clients.class);
            DbController<Reservations> retriveAllReservations = new DbController<>(Reservations.class);
            DbController<ReservationRooms> retrieveAllBusyRooms = new DbController<>(ReservationRooms.class);
            DbController<Rooms> retrieveAllRooms = new DbController<>(Rooms.class);
            DbController<Services> retrieveAllServices = new DbController<>(Services.class);

            this.services.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.services.getSelectionModel().selectedItemProperty().addListener(e -> {
                if (this.pickedServices.contains(this.loadedServices.get(e))) {
                    this.pickedServices.remove(this.loadedServices.get(e));
                } else {
                    this.pickedServices.add(this.loadedServices.get(e));
                }
            });

            List<Services> allServices = retrieveAllServices.findAll();
            for (Services service : allServices) {
                this.loadedServices.put("Name: " + service.getName() + " Price: " + service.getPrice(), service);
                this.services.getItems().add("Name: " + service.getName() + " Price: " + service.getPrice());
            }

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
                            } else {
                                this.clientsTableView.getItems().clear();
                            }
                        }
                    } catch (NumberFormatException excep) {
                        this.clientsTableView.getItems().clear();
                        LoggerUtil.error(excep.getMessage());
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
                    if (client.getRate() < 5) {
                        new Error("WARNING", "THIS CLIENT MAY BE INDECOROUS");
                    }
                    this.pickedClient = client;
                }
                return client;
            }));

            roomCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            roomCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            takeRoomColumn.setCellFactory(ActionButtonTrigger.<Rooms>forTableColumn("Select", CHANGE_STATUS_BUTTON_STYLE, this.roomsTableView, (Rooms room) -> {
                if (this.pickedRooms.contains(room)) {
                    this.pickedRooms.remove(room);
                } else {
                    if (this.reserveFrom.getValue().isBefore(this.reserveTo.getValue()) && this.reserveTo.getValue().isAfter(this.reserveFrom.getValue())) {
                        this.pickedRooms.add(room);
                        this.newReservation.setReservedFrom(this.reserveFrom.getValue());
                        this.newReservation.setReservedTo(this.reserveTo.getValue());
                    } else {
                        new Error("Selection failed !", "Please select valid date period !");
                    }
                }
                return room;
            }));


            this.roomCategory.getSelectionModel().selectedItemProperty().addListener(e -> {
                try {
                    this.roomsTableView.getItems().clear();
                    if (this.reserveFrom.getValue() != null && this.reserveTo != null && this.reserveFrom.getValue().isBefore(this.reserveTo.getValue()) && this.reserveTo.getValue().isAfter(this.reserveFrom.getValue())) {

                        List<Reservations> allActiveReservations = retriveAllReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
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

                        for (Reservations reservation : allActiveReservations) {
                            List<ReservationRooms> busyRoomz = retrieveAllBusyRooms.select(Restrictions.eq("reservation", reservation));
                            for (ReservationRooms record : busyRoomz) {
                                busyRooms.add(record.getRoom());
                            }
                        }

                        if (!busyRooms.isEmpty()) {
                            freeRooms.removeAll(busyRooms);
                        }
                        for (Rooms room : freeRooms) {
                            if (room.getCategory() == this.roomCategory.getSelectionModel().getSelectedItem()) {
                                this.roomsTableView.getItems().add(room);
                            }
                        }
                    }
                } catch (SelectNullObjectException excep) {
                    excep.printStackTrace();
                    LoggerUtil.error(excep.getMessage());
                } catch (NullPointerException excep) {
                    new Error("Search failed", "Please choose period and category !");
                    excep.printStackTrace();
                    LoggerUtil.error(excep.getMessage());
                }
            });
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    @FXML
    private void uploadClientForm() {
        try {
            SceneController.openNewScene(UploadClientForm.class, "Upload new Client", () -> {
                UploadClientForm accessClientForm = SceneController.getStageAccessTo(UploadClientForm.class);
                accessClientForm.setParentTableView(this.clientsTableView);
            });
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (IOException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to save this reservation?");
            if (confirm.getConfirmationResult() == true && this.formValidation()) {
                DbController<Reservations> uploadNewReservation = new DbController<>(Reservations.class);
                DbController<ReservationRooms> uploadReservationRooms = new DbController<>(ReservationRooms.class);

                Double totalSum = 0.0;
                long days = ChronoUnit.DAYS.between(this.reserveFrom.getValue(), this.reserveTo.getValue());
                System.out.println("TEST DAYS" + days);

                for (Rooms room : this.pickedRooms) {
                    totalSum += room.getPrice() * days;
                    System.out.println("TEST total sum: " + totalSum);
                }

                for (Services service : this.pickedServices) {
                    totalSum += service.getPrice();
                }

                this.totalSum.setText(totalSum.toString());
                this.newReservation.setTotalSum(totalSum);
                this.newReservation.setReservationStatus(ACTIVE);
                this.newReservation.setCancelingType(this.cancelingType.getSelectionModel().getSelectedItem());
                this.newReservation.setClient(this.pickedClient);
                this.newReservation.setCreatedOn(LocalDate.now());
                this.newReservation.setHotel(this.hotel);
                this.newReservation.setPaidMoney(Double.parseDouble(this.paidMoney.getText()));
                this.newReservation.setPaymentType(this.paymentType.getSelectionModel().getSelectedItem());
                this.newReservation.setReservationType(this.reservationType.getSelectionModel().getSelectedItem());
                this.newReservation.setUser(App.getLoggedUser());

                uploadNewReservation.insert(this.newReservation);

                for (Rooms room : this.pickedRooms) {
                    ReservationRooms newReservationRooms = new ReservationRooms();

                    newReservationRooms.setReservation(this.newReservation);
                    newReservationRooms.setRoom(room);

                    uploadReservationRooms.insert(newReservationRooms);
                }
                Stage stage = (Stage) this.saveButton.getScene().getWindow();
                stage.close();
            }
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NumberFormatException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
            new Error("Upload failed", "Price must be numeric!");
        }
    }

    private boolean formValidation() {
        try {
            if (!this.reservationType.getSelectionModel().isEmpty() && !this.paymentType.getSelectionModel().isEmpty() && !this.cancelingType.getSelectionModel().isEmpty() && this.pickedClient.getId() != null && !this.pickedRooms.isEmpty()) {
                return true;
            } else {
                new Error("Upload failed", "There are empty fields!");
            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
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
            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
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
            LoggerUtil.error(excep.getMessage());
        }
    }

}
