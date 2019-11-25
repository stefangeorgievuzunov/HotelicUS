package hotelicus.window;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Confirmation {
    private
    Optional<ButtonType> result;
    ButtonType confirmationButton;
    ButtonType cancelButton;

    public Confirmation(String title, String message) {
        this.confirmationButton = new ButtonType("Yes");
        this.cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("Choose your option.");

        alert.getButtonTypes().setAll(confirmationButton, cancelButton);
        this.result = alert.showAndWait();
    }

    public boolean getConfirmationResult() {
        if (this.result.get() == this.confirmationButton) {
            return true;
        } else {
            return false;
        }
    }
}
