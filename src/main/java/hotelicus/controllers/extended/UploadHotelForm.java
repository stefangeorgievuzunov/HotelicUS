package hotelicus.controllers.extended;

import hotelicus.App;
import hotelicus.controllers.extended.Users.LoadExtendedWindow;
import hotelicus.controllers.main.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Rooms;
import hotelicus.enums.*;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
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
import javafx.util.Duration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static hotelicus.enums.RoomStatus.BUSY;
import static hotelicus.enums.RoomStatus.FREE;
import static hotelicus.enums.UploadAction.*;
import static hotelicus.enums.UserPrivileges.MANAGER;
import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.styles.Styles.CHANGE_STATUS_BUTTON_STYLE;
import static hotelicus.styles.Styles.EDIT_BUTTON_STYLE;

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
    private TextField searchRoomByNumber;
    @FXML
    private Button saveButton;
    @FXML
    private Button addNewRoomButton;
    @FXML
    private Button manageManagerButton;

    private TableView parentTableView;
    private UploadAction uploadAction;
    private Hotels hotel;

    public void init(TableView parentTableView, Hotels hotel, UploadAction uploadAction) {
        if (parentTableView != null && uploadAction != null) {
            this.parentTableView = parentTableView;
            this.uploadAction = uploadAction;
            if (this.uploadAction == INSERT) {
                this.addNewRoomButton.setVisible(false);
                this.manageManagerButton.setVisible(false);
                this.searchRoomByNumber.setVisible(false);
            }
        } else {
            throw new NullPointerException();
        }
        if (hotel != null) {
            this.hotel = hotel;
        }
        this.uploadHotelInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            DbController<Rooms> rooms = new DbController<Rooms>(Rooms.class);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            this.searchRoomByNumber.textProperty().addListener(e -> {
                pause.setOnFinished(event -> {
                    if (this.hotel != null) {
                        List<Rooms> result = rooms.select(Restrictions.like("roomNumber", searchRoomByNumber.getText(), MatchMode.START), Restrictions.eq("hotel", this.hotel));
                        if (!result.isEmpty()) {
                            this.tableView.getItems().clear();
                            result.forEach(room -> {
                                this.tableView.getItems().add(room);
                            });
                        } else {
                            this.tableView.getItems().clear();
                        }
                    }
                });
                pause.playFromStart();
            });


            this.categoryColumn.setCellValueFactory(new PropertyValueFactory<Rooms, RoomCategories>("category"));
            this.capacityColumn.setCellValueFactory(new PropertyValueFactory<Rooms, Integer>("capacity"));
            this.roomNumberColumn.setCellValueFactory(new PropertyValueFactory<Rooms, String>("roomNumber"));
            this.priceColumn.setCellValueFactory(new PropertyValueFactory<Rooms, Double>("price"));
            this.roomStateColumn.setCellValueFactory(new PropertyValueFactory<Rooms, RoomStatus>("status"));

            statusColumn.setCellFactory(ActionButtonTableCell.<Rooms>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, this.tableView, (Rooms room) -> {
                if (room != null) {
                    if (room.getStatus() == FREE) {
                        takeRoom(room);
                    } else {
                        freeRoom(room);
                    }
                }
                return room;
            }));

            editColumn.setCellFactory(ActionButtonTableCell.<Rooms>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Rooms room) -> {
                try {
                    LoadExtendedWindow.loadUploadRoomFormWindow(this.tableView, "Edit user", this.hotel, room, EDIT);
                } catch (IOException excep) {
                    System.out.println(excep.getMessage());
                } catch (NullPointerException excep) {
                    excep.printStackTrace();
                }
                return room;
            }));

        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true) {
                if (!this.hotelNameField.getText().isEmpty()) {
                    DbController<Hotels> updateHotel = new DbController<Hotels>(Hotels.class);
                    if (this.hotel == null) {
                        this.hotel = new Hotels();
                        this.hotel.setHotelState(HotelState.ACTIVE);
                        this.hotel.setCreatedOn(LocalDate.now());
                        this.hotel.setOwner(App.getLoggedUser());
                    }

                    this.hotel.setName(this.hotelNameField.getText());

                    boolean successfulRecord = true;

                    if (this.uploadAction == EDIT) {
                        updateHotel.update(this.hotel);
                    }

                    if (this.uploadAction == INSERT) {
                        updateHotel.insert(this.hotel);
                        this.parentTableView.getItems().add(this.hotel);
                    }

                    if (successfulRecord) {
                        this.parentTableView.refresh();
                        this.searchRoomByNumber.setVisible(true);
                        this.addNewRoomButton.setVisible(true);
                        this.manageManagerButton.setVisible(true);
                    }
                } else {
                    new Error("Upload failed", "Hotel's name field cannot be empty !");
                }
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        } catch (ConstraintViolationException excep) {
            excep.printStackTrace();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void addNewRoom() {
        try {
            LoadExtendedWindow.loadUploadRoomFormWindow(this.tableView, "Room", this.hotel, null, INSERT);
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    @FXML
    private void manageManager() {
        try {
            LoadExtendedWindow.loadUploadUserFormWindow(null, this.hotel,"Manager", this.hotel.getManager(), MANAGER, INSERT_OR_EDIT);
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }

    private void uploadHotelInfo() {
        try {
            this.tableView.getItems().clear();
            if (this.hotel != null) {
                if (this.hotel.getName() != null) {
                    this.hotelNameField.setText(this.hotel.getName());
                }
                DbController<Rooms> loadRooms = new DbController<Rooms>(Rooms.class);
                List<Rooms> result = loadRooms.select(Restrictions.eq("hotel", this.hotel));
                if (!result.isEmpty()) {
                    result.forEach(room -> this.tableView.getItems().add(room));
                }
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void takeRoom(Rooms room) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to set busy this room?");
            if (confirm.getConfirmationResult() == true && room != null) {
                DbController<Rooms> updateRoom = new DbController<Rooms>(Rooms.class);
                room.setStatus(BUSY);
                updateRoom.update(room);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    private void freeRoom(Rooms room) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to set free this room?");
            if (confirm.getConfirmationResult() == true && room != null) {
                DbController<Rooms> updateRoom = new DbController<Rooms>(Rooms.class);
                room.setStatus(FREE);
                updateRoom.update(room);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        }
    }
}
