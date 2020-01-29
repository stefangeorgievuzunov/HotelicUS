package hotelicus.panels.extended;

import hotelicus.core.LoggerUtil;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.entities.Hotels;
import hotelicus.entities.Rooms;
import hotelicus.enums.RoomCategories;
import hotelicus.enums.RoomStatus;
import hotelicus.enums.UploadAction;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;

import java.net.URL;
import java.util.ResourceBundle;

import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UploadAction.INSERT;

public class UploadRoomForm implements Initializable {
    @FXML
    private ChoiceBox<RoomCategories> categoryMenu;
    @FXML
    private TextField capacity;
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField price;
    @FXML
    private Button saveButton;

    private Hotels hotel;
    private Rooms room;
    private UploadAction uploadAction;
    private TableView parentTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (room == null) {
            this.room = new Rooms();
        }
    }

    @FXML
    public void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true && this.formValidation()) {
                DbController<Rooms> updateRoom = new DbController<Rooms>(Rooms.class);

                this.room.setCapacity(Integer.parseInt(this.capacity.getText()));
                this.room.setPrice(Double.parseDouble(this.price.getText()));
                this.room.setHotel(this.hotel);
                this.room.setRoomNumber(this.roomNumber.getText());
                this.room.setCategory(this.categoryMenu.getSelectionModel().getSelectedItem());

                if (this.uploadAction == EDIT) {
                    this.parentTable.refresh();
                    updateRoom.update(this.room);
                }

                if (this.uploadAction == INSERT) {
                    this.room.setStatus(RoomStatus.FREE);
                    updateRoom.insert(this.room);
                    this.parentTable.getItems().add(this.room);
                }
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NumberFormatException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
            new Error("Upload failed", "Price and Capacity must be numeric!");
        }
    }

    public void uploadInfo() {
        if (this.room.getCapacity() != null && this.room.getPrice() != null && this.room.getRoomNumber() != null && this.room.getCategory() != null) {
            this.capacity.setText(this.room.getCapacity().toString());
            this.price.setText(this.room.getPrice().toString());
            this.roomNumber.setText(this.room.getRoomNumber());
            this.categoryMenu.setValue(this.room.getCategory());
        }
    }

    private boolean formValidation() {
        try {
            if (!this.capacity.getText().isEmpty() && !this.price.getText().isEmpty() && !this.roomNumber.getText().isEmpty() && !this.categoryMenu.getSelectionModel().isEmpty()) {
                DbController<Rooms> uniqueRoom = new DbController<>(Rooms.class);
                Rooms testRoom = uniqueRoom.selectUnique(Restrictions.eq("roomNumber", this.roomNumber.getText()), Restrictions.eq("hotel", this.hotel));

                if (testRoom == null) {
                    return true;
                }
                if (this.room.getRoomNumber() != null) {
                    if (this.room.getRoomNumber().equals(testRoom.getRoomNumber())) {
                        return true;
                    } else {
                        throw new NonUniqueResultException(0);
                    }
                }
                if (this.roomNumber.getText().equals(testRoom.getRoomNumber())) {
                    throw new NonUniqueResultException(0);
                }
            } else {
                new Error("Upload failed", "There are empty fields!");
            }
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
            LoggerUtil.error(excep.getMessage());
        } catch (NonUniqueResultException excep) {
            new Error("Upload failed", "Room  number already exist !");
            LoggerUtil.error(excep.getMessage());
        }
        return false;
    }

    public Hotels getHotel() {
        return hotel;
    }

    public void setHotel(Hotels hotel) {
        this.hotel = hotel;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        if (room != null) {
            this.room = room;
        } else {
            throw new NullPointerException();
        }
    }

    public UploadAction getUploadAction() {
        return uploadAction;
    }

    public void setUploadAction(UploadAction uploadAction) {
        if (uploadAction != null) {
            this.uploadAction = uploadAction;
        } else {
            throw new NullPointerException();
        }
    }

    public TableView getParentTable() {
        return parentTable;
    }

    public void setParentTable(TableView parentTable) {
        if (parentTable != null) {
            this.parentTable = parentTable;
        } else {
            throw new NullPointerException();
        }
    }
}