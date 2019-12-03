package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.controllers.main.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Rooms;
import hotelicus.enums.HotelState;
import hotelicus.enums.RoomCategories;
import hotelicus.enums.RoomStatus;
import hotelicus.enums.UploadAction;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UploadAction.INSERT;

public class UploadHotelForm implements Initializable {
    @FXML
    private TableView<Rooms> tableView;
    @FXML
    private TableColumn<Rooms, RoomCategories> categoryColumn;
    @FXML
    private TableColumn<Rooms, Integer> capacityColumn;
    @FXML
    private TableColumn<Rooms, String> roomNumberColumn;
    @FXML
    private TableColumn<Rooms, Double> priceColumn;
    @FXML
    private TableColumn<Rooms, RoomStatus> roomStateColumn;
    @FXML
    private TableColumn<Rooms, Button> statusColumn;
    @FXML
    private TableColumn<Rooms, Button> editColumn;
    @FXML
    private TextField hotelNameField;
    @FXML
    private Button saveButton;

    private TableView hotelsTableView;
    private UploadAction uploadAction;
    private Hotels hotel;

    public void init(TableView hotelsTable, Hotels hotel, UploadAction uploadAction) {
        this.hotelsTableView = hotelsTable;
        this.uploadAction = uploadAction;
        if (hotel != null) {
            this.hotel = hotel;
        }
        this.uploadHotelInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // this.rooms=new DbController<Rooms>(Rooms.class);
//
//        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
//        this.searchRoomByNumber.textProperty().addListener(e -> {
//            pause.setOnFinished(event -> {
//                List<Rooms> result = this.rooms.select(Restrictions.like("roomNumber", searchRoomByNumber.getText(), MatchMode.START), Restrictions.eq("hotel", this.hotel));
//                if (!result.isEmpty()) {
//                    this.tableView.getItems().clear();
//                    result.forEach(room -> {
//                        this.tableView.getItems().add(room);
//                    });
//                } else {
//                    this.tableView.getItems().clear();
//                }
//            });
//            pause.playFromStart();
//        });

        categoryColumn.setCellValueFactory(new PropertyValueFactory<Rooms, RoomCategories>("category"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<Rooms, Integer>("capacity"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<Rooms, String>("roomNumber"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Rooms, Double>("price"));
        roomStateColumn.setCellValueFactory(new PropertyValueFactory<Rooms, RoomStatus>("status"));
    }

    @FXML
    private void addNewRoom() {

    }

    @FXML
    private void manageManager() {

    }

    @FXML
    private void saveHotel() {
        Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
        if (!this.hotelNameField.getText().isEmpty()&&confirm.getConfirmationResult() == true) {
            DbController<Hotels> updateHotel = new DbController<Hotels>(Hotels.class);
            this.hotel = new Hotels();
            this.hotel.setHotelState(HotelState.ACTIVE);
            this.hotel.setCreatedOn(LocalDate.now());
            this.hotel.setOwner(App.getLoggedUser());
            this.hotel.setName(this.hotelNameField.getText());

            boolean successfulRecord = true;

            if (this.uploadAction == EDIT) {
                updateHotel.update(this.hotel);
            }

            if (this.uploadAction == INSERT) {
                updateHotel.insert(this.hotel);
                this.hotelsTableView.getItems().add(this.hotel);
            }

            if (successfulRecord) {
                this.hotelsTableView.refresh();
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        } else {
            new Error("Upload failed", "Hotel's name field cannot be empty !");
        }
    }

    private void uploadHotelInfo() {
        if (this.hotel != null) {
            if (this.hotel.getName() != null) {
                this.hotelNameField.setText(this.hotel.getName());
            }
            DbController<Rooms> loadRooms=new DbController<Rooms>(Rooms.class);
            List<Rooms> result=loadRooms.select(Restrictions.eq("hotel",this.hotel));
            if(!result.isEmpty()){
                result.forEach(room->this.tableView.getItems().add(room));
            }
        }
    }

}
