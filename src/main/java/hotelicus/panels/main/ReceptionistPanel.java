package hotelicus.panels.main;

import hotelicus.App;
import hotelicus.entities.*;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.panels.controllers.UserController;
import hotelicus.panels.extended.ActionButtonTrigger;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.util.Duration;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;

import java.net.URL;
import java.time.LocalDate;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static hotelicus.enums.ReservationCanceling.EMERGENCY;
import static hotelicus.enums.ReservationCanceling.ON_TIME;
import static hotelicus.enums.ReservationStatus.*;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;
import static hotelicus.styles.Styles.EDIT_BUTTON_STYLE;

public class ReceptionistPanel implements Initializable {
    @FXML
    private TableView<Reservations> tableView;
    @FXML
    private TableColumn<Reservations, Integer> reservationIdColumn;
    @FXML
    private TableColumn<Reservations, LocalDate> createdOnColumn;
    @FXML
    private TableColumn<Reservations, LocalDate> reservedFromColumn;
    @FXML
    private TableColumn<Reservations, LocalDate> reservedToColumn;
    @FXML
    private TableColumn<Reservations, Button> abortColumn;
    @FXML
    private TableColumn<Reservations, Button> registerColumn;
    @FXML
    private Button notifications;
    @FXML
    private DatePicker reservedFrom;
    @FXML
    private DatePicker reservedTo;
    private Hotels hotel;
    private Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> this.notifications.setVisible(false)),
            new KeyFrame(Duration.seconds(1.5), e -> this.notifications.setVisible(true)));

    private ScheduledExecutorService expiringReservationsSchedule = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.timeline.setCycleCount(Animation.INDEFINITE);

            DbController<HotelReceptionists> retrieveHotel = new DbController<>(HotelReceptionists.class);
            HotelReceptionists record = retrieveHotel.selectUnique(Restrictions.eq("receptionist", App.getLoggedUser()));
            this.hotel = record.getHotel();

            DbController<Reservations> retrieveReservations = new DbController<>(Reservations.class);

            reservedFrom.valueProperty().addListener((ov, oldValue, newValue) -> {
                this.tableView.getItems().clear();
                if (!newValue.isAfter(this.reservedTo.getValue())) {
                    List<Reservations> allReservations = retrieveReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
                            Restrictions.or(
                                    Restrictions.between("reservedFrom", newValue, this.reservedTo.getValue()),
                                    Restrictions.between("reservedTo", newValue, this.reservedTo.getValue()),
                                    Restrictions.and(Restrictions.ge("reservedTo", newValue), Restrictions.between("reservedFrom", newValue, this.reservedTo.getValue())),
                                    Restrictions.and(Restrictions.le("reservedFrom", newValue), Restrictions.between("reservedTo", newValue, this.reservedTo.getValue())),
                                    Restrictions.and(Restrictions.le("reservedFrom", newValue), Restrictions.ge("reservedTo", this.reservedTo.getValue()))
                            ));

                    allReservations.forEach(r -> this.tableView.getItems().add(r));
                }
            });

            reservedTo.valueProperty().addListener((ov, oldValue, newValue) -> {
                this.tableView.getItems().clear();
                if (!newValue.isBefore(this.reservedFrom.getValue())) {
                    List<Reservations> allReservations = retrieveReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
                            Restrictions.or(
                                    Restrictions.between("reservedFrom", this.reservedFrom.getValue(), newValue),
                                    Restrictions.between("reservedTo", this.reservedFrom.getValue(), newValue),
                                    Restrictions.and(Restrictions.ge("reservedTo", newValue), Restrictions.between("reservedFrom", this.reservedFrom.getValue(), newValue)),
                                    Restrictions.and(Restrictions.le("reservedFrom", this.reservedFrom.getValue()), Restrictions.between("reservedTo", this.reservedFrom.getValue(), newValue)),
                                    Restrictions.and(Restrictions.le("reservedFrom", this.reservedFrom.getValue()), Restrictions.ge("reservedTo", newValue))
                            ));

                    allReservations.forEach(r -> this.tableView.getItems().add(r));
                }
            });

            reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
            createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
            reservedFromColumn.setCellValueFactory(new PropertyValueFactory<>("reservedFrom"));
            reservedToColumn.setCellValueFactory(new PropertyValueFactory<>("reservedTo"));
            abortColumn.setCellFactory(ActionButtonTrigger.<Reservations>forTableColumn("Abort", CHANGE_STATUS_BUTTON_STYLE, this.tableView, (Reservations reservation) -> {
                abortReservation(reservation);
                return reservation;
            }));
            registerColumn.setCellFactory(ActionButtonTrigger.<Reservations>forTableColumn("Register", EDIT_BUTTON_STYLE, this.tableView, (Reservations reservation) -> {
                registerReservation(reservation);
                return reservation;
            }));

            this.uploadAllReservations();

            this.expiringReservationsSchedule.scheduleAtFixedRate(() -> {
                List<Reservations> expiringReservations = retrieveReservations.select(
                        Restrictions.eq("reservationStatus", ACTIVE),
                        Restrictions.le("reservedFrom", LocalDate.now()),
                        Restrictions.ge("reservedTo", LocalDate.now()),
                        Restrictions.eq("paidMoney", 0.0));

                if (!expiringReservations.isEmpty()) {
                    DbController<Clients> updateClientsRate = new DbController<>(Clients.class);
                    for (Reservations r : expiringReservations) {
                        Clients badClient = r.getClient();
                        badClient.setRate(badClient.getRate() - 0.5);
                        updateClientsRate.update(badClient);
                    }

                    this.timeline.play();
                }
            }, 0, 60, TimeUnit.SECONDS); //have to be changed to 12hours or 24 hours.
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void uploadAllReservations() {
        try {
            this.tableView.getItems().clear();

            DbController<Reservations> retrieveReservations = new DbController<>(Reservations.class);
            List<Reservations> allReservations = retrieveReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE));

            allReservations.forEach(r -> this.tableView.getItems().add(r));
        } catch (NonUniqueResultException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void loadExpiringReservations() {
        try {
            this.timeline.stop();
            this.tableView.getItems().clear();
            DbController<Reservations> retrieveExpiringReservations = new DbController<>(Reservations.class);
            List<Reservations> expiringReservations = retrieveExpiringReservations.select(
                    Restrictions.eq("reservationStatus", ACTIVE),
                    Restrictions.le("reservedFrom", LocalDate.now()),
                    Restrictions.ge("reservedTo", LocalDate.now()),
                    Restrictions.eq("paidMoney", 0.0));

            if (!expiringReservations.isEmpty()) {
                expiringReservations.forEach(r -> this.tableView.getItems().add(r));
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void abortReservation(Reservations reservation) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
            if (confirm.getConfirmationResult() == true && reservation != null) {
                DbController<Reservations> updateReservation = new DbController<>(Reservations.class);
                DbController<Register> updateRegister = new DbController<>(Register.class);
                DbController<Clients> updateClientsRate = new DbController<>(Clients.class);

                LocalDate today = LocalDate.now();

                if (reservation.getPaidMoney() > 0) {
                    Register register = new Register();
                    register.setCreatedOn(today);
                    register.setReservation(reservation);
                    register.setStatus(ABORTED);
                    updateRegister.insert(register);
                }

                Clients badClient = reservation.getClient();
                badClient.setRate(badClient.getRate() - 1);
                updateClientsRate.update(badClient);

                reservation.setReservationStatus(ABORTED);
                reservation.setCancelingType(EMERGENCY);
                updateReservation.update(reservation);

                this.tableView.getItems().remove(reservation);
                this.tableView.refresh();
            }
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }

    }

    private void registerReservation(Reservations reservation) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to disable this hotel?");
            if (confirm.getConfirmationResult() == true && reservation != null) {
                if (reservation.getPaidMoney() > 0) {
                    DbController<Reservations> updateReservation = new DbController<>(Reservations.class);
                    DbController<Register> updateRegister = new DbController<>(Register.class);

                    LocalDate today = LocalDate.now();

                    Register register = new Register();
                    register.setCreatedOn(today);
                    register.setReservation(reservation);

                    if (today.isBefore(reservation.getReservedFrom()) || (today.isAfter(reservation.getReservedFrom()) && today.isBefore(reservation.getReservedTo()))) {
                        register.setStatus(ABORTED);
                        reservation.setReservationStatus(ABORTED);
                        reservation.setCancelingType(EMERGENCY);
                        if (reservation.getPaidMoney() < reservation.getTotalSum()) {
                            DbController<Clients> badClientRate = new DbController<>(Clients.class);
                            Clients badClient = reservation.getClient();
                            badClient.setRate(badClient.getRate() - 1);
                            badClientRate.update(badClient);
                        }
                    } else {
                        if (reservation.getPaidMoney() == reservation.getTotalSum()) {
                            register.setStatus(EXPIRED);
                            reservation.setReservationStatus(EXPIRED);
                            reservation.setCancelingType(ON_TIME);
                        } else {
                            new Error("Upload failed", "Paid money must be full price !");
                            throw new UpdateNullObjectException();
                        }
                    }

                    updateReservation.update(reservation);
                    updateRegister.insert(register);
                    this.tableView.getItems().remove(reservation);
                } else {
                    new Error("Upload failed", "Paid money too low.");
                }
                this.tableView.refresh();
            }
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void logOut() {
        UserController.logOut();
    }
}

