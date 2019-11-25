package hotelicus.window;

import javafx.scene.control.Alert;

import java.io.IOException;

public class Error {
    public Error(String errorTitle, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(errorTitle);
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
