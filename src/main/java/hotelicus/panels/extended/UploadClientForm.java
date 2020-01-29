package hotelicus.panels.extended;

import hotelicus.App;
import hotelicus.entities.Clients;
import hotelicus.exceptions.InsertNullObjectException;
import hotelicus.panels.controllers.DbController;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.exception.ConstraintViolationException;
import java.time.LocalDate;

public class UploadClientForm {
    @FXML
    private TextField clientId;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private Button saveButton;

    private TableView<Clients> parentTableView;

    @FXML
    private void uploadRouter() {
        try {
            Confirmation confirm = new Confirmation("Confirmation", "Are you sure you want to save?");
            if (confirm.getConfirmationResult() == true && !this.clientId.getText().isEmpty() &&
                    !this.phoneNumber.getText().isEmpty() && !this.firstName.getText().isEmpty() && !this.lastName.getText().isEmpty()) {
                Clients client = new Clients();
                DbController<Clients> uploadClients = new DbController<>(Clients.class);

                client.setId(Integer.parseInt(this.clientId.getText()));
                client.setCreatedOn(LocalDate.now());
                client.setFirstName(this.firstName.getText());
                client.setLastName(this.lastName.getText());
                client.setPhoneNumber(this.phoneNumber.getText());
                client.setCreatedBy(App.getLoggedUser());

                uploadClients.insert(client);

                if (this.parentTableView != null) {
                    this.getParentTableView().getItems().add(client);
                    this.parentTableView.refresh();
                }
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }else{
                new Error("Upload failed", "There are empty fields!");
            }
        } catch (ConstraintViolationException excep) {
            new Error("Upload failed", "Client already exist");
            excep.printStackTrace();
        } catch (NumberFormatException excep) {
            excep.printStackTrace();
            new Error("Upload failed", "ID must be numeric!");
        } catch (InsertNullObjectException excep) {
            excep.printStackTrace();
        }
    }

    public TableView<Clients> getParentTableView() {
        return parentTableView;
    }

    public void setParentTableView(TableView<Clients> parentTableView) {
        try {
            if (parentTableView != null) {
                this.parentTableView = parentTableView;
            }
        } catch (NullPointerException excep) {
            excep.printStackTrace();
        }
    }
}
