package hotelicus;

import hotelicus.controllers.extended.Users.UploadUserForm;
import hotelicus.controllers.main.AdminController;
import hotelicus.core.HibernateUtil;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        App.type=getClass();
    }
    public static Session getSession(){
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

    @Override
    public void start(Stage primaryStage) {
        try {
            App.stage = primaryStage;
            App.loginWindow();
            primaryStage.show();
        } catch (Exception excep) {
            excep.printStackTrace();
        }
    }

    public static void loginWindow() {
            try{
                changeScene("login.xml", "Login", 320, 120);
            }
            catch(IOException excep){
                excep.printStackTrace();
            }

    }

    public static void dashboardWindow() {
        try {
            changeScene("dashboard.xml", "Dashboard", 500, 500);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void adminWindow() {
        try {
            new AdminController();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void changeScene(String fxml, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.type.getClassLoader().getResource("templates/" + fxml));

        Parent page = (Parent)fxmlLoader.load();
        Scene scene = getStage().getScene();

        if (scene == null) {
            scene = new Scene(page, width, height);
            App.stage.setScene(scene);
            App.stage.sizeToScene();
        } else {
            App.stage.setWidth(width);
            App.stage.setHeight(height);
            App.stage.getScene().setRoot(page);
        }

        App.stage.setTitle(title);
    }

    public  static void loadUploadUserFormWindow(String title,UploadAction uploadAction, Users user, UserPrivileges privileges)throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(App.type.getClassLoader().getResource("templates/UploadForm.xml"));

        Parent root = (Parent)fxmlLoader.load();
        UploadUserForm controller = fxmlLoader.<UploadUserForm>getController();
        controller.init(user,privileges,uploadAction);
        Stage stage=new Stage();
        stage.setTitle(title);
        Scene scene=new Scene(root, 450, 450);
        stage.setScene(scene);
        stage.show();
    }
    public static Stage getAppStage(){
        return App.stage;
    }
    public static void main(String[] args) {
        launch(args);
    }

}