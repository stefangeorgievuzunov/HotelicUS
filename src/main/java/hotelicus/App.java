package hotelicus;

import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.controllers.extended.Users.UserController;
import hotelicus.controllers.main.DbController;
import hotelicus.controllers.main.LoginController;
import hotelicus.controllers.main.Monitor;
import hotelicus.controllers.main.OwnerPanel;
import hotelicus.core.HibernateUtil;
import hotelicus.entities.LoggedUsers;
import hotelicus.entities.Users;
import hotelicus.exceptions.DbControllerNullConstructorException;
import hotelicus.exceptions.SelectNullObjectException;
import hotelicus.exceptions.UpdateNullObjectException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class App extends Application {

    private static Stage stage;
    private static Users loggedUser;
    private static Session session;
    private static ScheduledExecutorService loggedUserPinging;

    public App() {
        App.session = HibernateUtil.getSessionFactory().openSession();
        App.loggedUserPinging = Executors.newSingleThreadScheduledExecutor();
    }

    public static Session getSession() {
        return App.session;
    }

    public static Stage getStage() {
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

            App.loggedUserPinging.scheduleAtFixedRate(() -> {
                if (App.loggedUser != null) {
                    DbController<LoggedUsers> setUserLoggedIn = new DbController<LoggedUsers>(LoggedUsers.class);
                    LoggedUsers result = setUserLoggedIn.selectUnique(Restrictions.eq("loggedUser", App.loggedUser));
                    result.setLastPinged(new Date());
                    setUserLoggedIn.update(result);
                }
            }, 0, 60, TimeUnit.SECONDS);

            App.stage.setOnCloseRequest(e -> {
                Platform.exit();
                this.loggedUserPinging.shutdown();
            });

            Monitor.changePrimaryScene(LoginController.class, "Login Panel");
        } catch (IOException excep) {
            excep.printStackTrace();
        } catch (SelectNullObjectException excep) {
            excep.printStackTrace();
        } catch (DbControllerNullConstructorException excep) {
            excep.printStackTrace();
        } catch (UpdateNullObjectException excep) {
            excep.printStackTrace();
        } catch (NonUniqueResultException excep) {
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

    public static void main(String[] args) {
        launch(args);
    }
}