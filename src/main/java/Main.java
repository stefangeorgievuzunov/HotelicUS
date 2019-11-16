import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream file = classloader.getResourceAsStream("templates/Login.xml");

        VBox root = loader.load(file);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Hotelicus - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}