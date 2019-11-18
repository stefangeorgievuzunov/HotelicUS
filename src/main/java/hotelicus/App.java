package hotelicus;

import hotelicus.controllers.AdminController;
import hotelicus.entities.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


public class App extends Application {

    private static Stage stage;
    private Users loggedUser;

    private static App instance;

    public App() {
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    private static Stage getStage() {
        return stage;
    }

    public Users getLoggedUser() {
        return loggedUser;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            loginWindow();
            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loginWindow() {
        try {
            changeScene("login.xml", "Login", 320, 120);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dashboardWindow() {
        try {
            changeScene("dashboard.xml", "Dashboard", 500, 500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void adminWindow() {
        try {
            changeSceneByObject(new AdminController().getTableView());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void changeSceneByObject(TableView tableView){
        VBox vbox=new VBox(tableView);
        Scene scene=new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void changeScene(String fxml, String title, int width, int height) throws Exception {
        System.out.println(getClass().getClassLoader().getResource("templates/" + fxml));
        Parent page = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("templates/" + fxml)), null, new JavaFXBuilderFactory());
        Scene scene = getStage().getScene();

        if (scene == null) {
            scene = new Scene(page, width, height);
            stage.setScene(scene);
            stage.sizeToScene();
        } else {
            stage.setWidth(width);
            stage.setHeight(height);
            stage.getScene().setRoot(page);
        }

        stage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }

}