package hotelicus.controllers.extended;

import hotelicus.controllers.main.DbController;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UploadAction.INSERT;

public class UploadRoomForm {
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

    public void init(TableView parentTable, Hotels hotel, Rooms room, UploadAction uploadAction) {
        if (hotel != null && parentTable != null && uploadAction != null) {
            this.hotel = hotel;
            this.parentTable = parentTable;
            this.uploadAction = uploadAction;
        }else{
            throw new NullPointerException();
        }
        if (room != null) {
            this.room = room;
        } else {
            this.room = new Rooms();
        }
        this.uploadRoomInfo();
    }

    @FXML
    public void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true) {
                if (!this.capacity.getText().isEmpty() && !this.price.getText().isEmpty() && !this.roomNumber.getText().isEmpty() && !this.categoryMenu.getSelectionModel().isEmpty()) {

                    DbController<Rooms> updateRoom = new DbController<Rooms>(Rooms.class);

                    this.room.setCapacity(Integer.parseInt(this.capacity.getText()));
                    this.room.setHotel(this.hotel);
                    this.room.setPrice(Double.parseDouble(this.price.getText()));
                    this.room.setRoomNumber(this.roomNumber.getText());
                    this.room.setCategory(this.categoryMenu.getSelectionModel().getSelectedItem());
                    boolean successfulRecord = true;

                    if (this.uploadAction == EDIT) {
                        this.parentTable.refresh();
                        updateRoom.update(this.room);
                    }

                    if (this.uploadAction == INSERT) {
                        this.room.setStatus(RoomStatus.FREE);
                        updateRoom.insert(this.room);
                        this.parentTable.getItems().add(this.room);
                    }

                    if (successfulRecord) {
                        Stage stage = (Stage) saveButton.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    new Error("Upload failed", "There are empty fields!");
                }
            }
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        }catch(UpdateNullObjectException excep){
            excep.printStackTrace();
        }
    }

    private void uploadRoomInfo() {
        if (this.room.getCapacity() != null && this.room.getPrice() != null && this.room.getRoomNumber() != null && this.room.getCategory()!=null) {
            this.capacity.setText(this.room.getCapacity().toString());
            this.price.setText(this.room.getPrice().toString());
            this.roomNumber.setText(this.room.getRoomNumber());
            this.categoryMenu.setValue(this.room.getCategory());
        }
    }
}