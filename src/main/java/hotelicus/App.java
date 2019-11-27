package hotelicus;

import hotelicus.controllers.extended.Users.UploadUserForm;
import hotelicus.core.HibernateUtil;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.io.IOException;


public final class App extends Application {

    private static Stage stage;
    private static Users loggedUser;
    private static Session session;
    private static App instance;
    private static Class type;

    public App() {
        App.instance = this;
        App.session = HibernateUtil.getSessionFactory().openSession();
        App.type = getClass();
    }

    public static Session getSession() {
        return App.session;
    }

    public static App getInstance() {
        return App.instance;
    }

    private static Stage getStage() {
        return App.stage;
    }

    public static Users getLoggedUser() {
        return App.loggedUser;
    }

    public static void setLoggedUser(Users user) {
        App.loggedUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            App.stage = primaryStage;
            App.stage.setResizable(false);
            App.stage.setOnCloseRequest(e -> Platform.exit());

            App.loginWindow();
            primaryStage.show();
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }

    public static void loginWindow() {
        try {
            changeScene("login.fxml", "Login");
        } catch (IOException excep) {
            excep.printStackTrace();
        }

    }

    public static void dashboardWindow() {
        try {
            changeScene("dashboard.xml", "Dashboard");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void adminWindow() {
        try {
            App.changeScene("adminpanel.fxml", "ADMIN PANEL");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void changeScene(String fxml, String title) throws IOException {
        if (!fxml.isEmpty() && !title.isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/" + fxml));

            Parent page = (Parent) fxmlLoader.load();
            Scene scene = getStage().getScene();

            if (scene == null) {
                scene = new Scene(page);
                App.stage.setScene(scene);

            } else {
                App.stage.getScene().setRoot(page);
            }
            App.stage.setTitle(title);
        }
    }

    public static Stage getAppStage() {
        return App.stage;
    }


    public static void main(String[] args) {
        launch(args);
    }

}