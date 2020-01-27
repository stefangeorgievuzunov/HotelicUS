package hotelicus.panels.main;

import hotelicus.App;
import hotelicus.entities.HotelReceptionists;
import hotelicus.entities.Hotels;
import hotelicus.entities.Reservations;
import hotelicus.entities.Rooms;
import hotelicus.enums.HotelState;
import hotelicus.enums.ReservationStatus;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.panels.extended.ActionButtonTrigger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.ReservationStatus.ACTIVE;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;

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
    private DatePicker reservedFrom;
    @FXML
    private DatePicker reservedTo;
    private Hotels hotel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DbController<HotelReceptionists> retrieveHotel = new DbController<>(HotelReceptionists.class);
        HotelReceptionists record = retrieveHotel.selectUnique(Restrictions.eq("receptionist", App.getLoggedUser()));
        this.hotel = record.getHotel();

        DbController<Reservations> retrieveReservations = new DbController<>(Reservations.class);

        reservedFrom.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.tableView.getItems().clear();
            if(!newValue.isAfter(this.reservedTo.getValue())){
                List<Reservations> allReservations = retrieveReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
                        Restrictions.or(
                                Restrictions.between("reservedFrom", newValue, this.reservedTo.getValue()),
                                Restrictions.between("reservedTo", newValue, this.reservedTo.getValue()),
                                Restrictions.and(Restrictions.ge("reservedTo",newValue),Restrictions.between("reservedFrom", newValue, this.reservedTo.getValue())),
                                Restrictions.and(Restrictions.le("reservedFrom",newValue),Restrictions.between("reservedTo", newValue, this.reservedTo.getValue())),
                                Restrictions.and(Restrictions.le("reservedFrom",newValue),Restrictions.ge("reservedTo",this.reservedTo.getValue()))
                        ));

                allReservations.forEach(r -> this.tableView.getItems().add(r));
            }
        });

        reservedTo.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.tableView.getItems().clear();
            if(!newValue.isBefore(this.reservedFrom.getValue())){
                List<Reservations> allReservations = retrieveReservations.select(Restrictions.eq("hotel", this.hotel), Restrictions.eq("reservationStatus", ACTIVE),
                        Restrictions.or(
                                Restrictions.between("reservedFrom", this.reservedFrom.getValue(), newValue),
                                Restrictions.between("reservedTo", this.reservedFrom.getValue(), newValue),
                                Restrictions.and(Restrictions.ge("reservedTo",newValue), Restrictions.between("reservedFrom", this.reservedFrom.getValue(), newValue)),
                                Restrictions.and(Restrictions.le("reservedFrom",this.reservedFrom.getValue()), Restrictions.between("reservedTo", this.reservedFrom.getValue(), newValue)),
                                Restrictions.and(Restrictions.le("reservedFrom",this.reservedFrom.getValue()),Restrictions.ge("reservedTo",newValue))
                        ));

                allReservations.forEach(r -> this.tableView.getItems().add(r));
            }
        });

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));
        reservedFromColumn.setCellValueFactory(new PropertyValueFactory<>("reservedFrom"));
        reservedToColumn.setCellValueFactory(new PropertyValueFactory<>("reservedTo"));
        abortColumn.setCellFactory(ActionButtonTrigger.<Reservations>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, this.tableView, (Reservations reservation) -> {
            abortReservation();
            return reservation;
        }));

        this.uploadAllReservations();
    }


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

    private void abortReservation() {

    }
}

