package hotelicus.controllers.main;

import hotelicus.App;
import javafx.fxml.FXML;


public class DashboardController {
    @FXML
    private void openAdmin() {
        App.adminWindow();
    }
}
