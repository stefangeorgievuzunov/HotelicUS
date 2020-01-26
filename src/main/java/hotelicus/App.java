package hotelicus;

import hotelicus.controllers.extended.Users.UploadUserForm;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.controllers.main.DbController;
import hotelicus.core.HibernateUtil;
import hotelicus.entities.LoggedUsers;
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
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class App extends Application {

    private static Stage stage;
    private static Users loggedUser;
    private static Session session;
    private static App instance;
    private static Class type;
    private static ScheduledExecutorService loggedUserPinging;

    public App() {
        App.instance = this;
        App.session = HibernateUtil.getSessionFactory().openSession();
        App.type = getClass();
        App.loggedUserPinging= Executors.newSingleThreadScheduledExecutor();
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
            App.loggedUserPinging.scheduleAtFixedRate(() -> {
                if (App.loggedUser != null) {
                    DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
                    LoggedUsers result = setUserLoggedIn.selectUnique(Restrictions.eq("loggedUser", App.loggedUser));
                    result.setLastPinged(new Date());
                    setUserLoggedIn.update(result);
                }
            },0,60, TimeUnit.SECONDS);

            App.stage.setOnCloseRequest(e -> {
                Platform.exit();
                this.loggedUserPinging.shutdown();
            });

            App.loginWindow();
            primaryStage.show();
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (App.getLoggedUser() != null) {
            UserController.setUserLoggedOff(App.getLoggedUser());
        }
        App.session.close();
    }

    public static void loginWindow() {
        try {
            App.changeScene("login.fxml", "Login");
        } catch (IOException excep) {
            excep.printStackTrace();
        }

    }

    public static void ownerWindow() {
        try {
            App.changeScene("ownerpanel.fxml", "OWNER PANEL:");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void adminWindow() {
        try {
            App.changeScene("adminpanel.fxml", "ADMIN PANEL");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void changeScene(String fxml, String title) throws IOException {
        if (!fxml.isEmpty() && !title.isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/" + fxml));

            Parent page = (Parent) fxmlLoader.load();
            Scene scene = App.stage.getScene();

            if (scene == null) {
                scene = new Scene(page);
                App.stage.setScene(scene);
            } else {
                App.stage.getScene().setRoot(page);
            }
            App.stage.setTitle(title);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}