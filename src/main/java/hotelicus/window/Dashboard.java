package hotelicus.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard {

    public Dashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("templates/Dashboard.xml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
