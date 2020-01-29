package hotelicus.panels.extended;

import hotelicus.App;
import hotelicus.core.LoggerUtil;
import hotelicus.panels.controllers.DbController;
import hotelicus.panels.controllers.SceneController;
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
    private Button addNewRoomButton;
    @FXML
    private Button manageManagerButton;

    private TableView parentTableView;
    private UploadAction uploadAction;
    private Hotels hotel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (this.hotel == null) {
                this.hotel = new Hotels();
                this.hotel.setHotelState(HotelState.ACTIVE);
                this.hotel.setCreatedOn(LocalDate.now());
                this.hotel.setOwner(App.getLoggedUser());
            }

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

            this.statusColumn.setCellFactory(ActionButtonTrigger.<Rooms>forTableColumn("Switch", CHANGE_STATUS_BUTTON_STYLE, this.tableView, (Rooms room) -> {
                if (room != null) {
                    if (room.getStatus() == FREE) {
                        changeRoomStatus(room, BUSY);
                    } else {
                        changeRoomStatus(room, FREE);
                    }
                }
                return room;
            }));

            this.editColumn.setCellFactory(ActionButtonTrigger.<Rooms>forTableColumn("Edit", EDIT_BUTTON_STYLE, tableView, (Rooms room) -> {
                try {
                    SceneController.openNewScene(UploadRoomForm.class, "Edit user", () -> {
                        UploadRoomForm uploadRoomForm = SceneController.getStageAccessTo(UploadRoomForm.class);
                        uploadRoomForm.setHotel(this.hotel);
                        uploadRoomForm.setUploadAction(EDIT);
                        uploadRoomForm.setParentTable(tableView);
                        uploadRoomForm.setRoom(room);
                        uploadRoomForm.uploadInfo();
                    });
                } catch (IOException excep) {
                    System.out.println(excep.getMessage());
                } catch (NullPointerException excep) {
                    excep.printStackTrace();
                    LoggerUtil.error(excep.getMessage());
                }
                return room;
            }));
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true) {
                if (!this.hotelNameField.getText().isEmpty()) {
                    DbController<Hotels> updateHotel = new DbController<Hotels>(Hotels.class);

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
            LoggerUtil.error(excep.getMessage());
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (ConstraintViolationException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    @FXML
    private void addNewRoom() {
        try {
            SceneController.openNewScene(UploadRoomForm.class, "Room", () -> {
                UploadRoomForm uploadRoomForm = SceneController.getStageAccessTo(UploadRoomForm.class);
                uploadRoomForm.setHotel(this.hotel);
                uploadRoomForm.setParentTable(this.tableView);
                uploadRoomForm.setUploadAction(INSERT);
            });

        } catch (IOException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    @FXML
    private void manageManager() {
        try {
            SceneController.openNewScene(UploadUserForm.class, "Manager", () -> {
                UploadUserForm uploadUserForm = SceneController.getStageAccessTo(UploadUserForm.class);
                uploadUserForm.setHotel(this.hotel);
                if(this.hotel.getManager()!=null){
                    uploadUserForm.setUser(this.hotel.getManager());
                }
                uploadUserForm.setPrivileges(MANAGER);
                uploadUserForm.setUploadAction(INSERT_OR_EDIT);
                uploadUserForm.uploadInfo();
            });

        } catch (IOException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NullPointerException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    public void uploadInfo() {
        try {
            this.tableView.getItems().clear();
            if (this.uploadAction == INSERT) {
                this.addNewRoomButton.setVisible(false);
                this.manageManagerButton.setVisible(false);
                this.searchRoomByNumber.setVisible(false);
            }

            if (this.hotel != null && this.uploadAction == EDIT) {
                this.hotelNameField.setText(this.hotel.getName());

                DbController<Rooms> loadRooms = new DbController<Rooms>(Rooms.class);
                List<Rooms> result = loadRooms.select(Restrictions.eq("hotel", this.hotel));

                if (!result.isEmpty()) {
                    result.forEach(room -> this.tableView.getItems().add(room));
                }
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    private void changeRoomStatus(Rooms room, RoomStatus status) {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Do you want to set busy this room?");
            if (confirm.getConfirmationResult() == true && room != null && status != null) {
                DbController<Rooms> updateRoom = new DbController<Rooms>(Rooms.class);
                room.setStatus(status);
                updateRoom.update(room);
                this.tableView.refresh();
            }
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        }
    }

    public Hotels getHotel() {
        return hotel;
    }

    public TableView getParentTableView() {
        return parentTableView;
    }

    public UploadAction getUploadAction() {
        return uploadAction;
    }

    public void setParentTableView(TableView parentTableView) {
        if (parentTableView != null) {
            this.parentTableView = parentTableView;
        } else {
            throw new NullPointerException();
        }
    }

    public void setUploadAction(UploadAction uploadAction) {
        if (uploadAction != null) {
            this.uploadAction = uploadAction;
        } else {
            throw new NullPointerException();
        }
    }

    public void setHotel(Hotels hotel) {
        if (hotel != null) {
            this.hotel = hotel;
        } else {
            throw new NullPointerException();
        }
    }
}
