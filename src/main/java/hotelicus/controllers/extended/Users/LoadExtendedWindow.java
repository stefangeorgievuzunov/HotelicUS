package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.entities.Hotels;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadExtendedWindow {
    public static void loadUploadUserFormWindow(TableView tableView, String title, UploadAction uploadAction, Users user, UserPrivileges privileges) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/edit.fxml"));

        Parent root = (Parent) fxmlLoader.load();
        UploadUserForm controller = fxmlLoader.<UploadUserForm>getController();
        controller.init(tableView, user, privileges, uploadAction);
        LoadExtendedWindow.loadNewScene(root, title);
    }

    public static void loadUploadHotelFormWindow(TableView tableView,String title,Hotels hotel,UploadAction uploadAction) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/hotel.fxml"));

        Parent root = (Parent) fxmlLoader.load();
        UploadHotelForm controller = fxmlLoader.<UploadHotelForm>getController();
        controller.init(tableView,hotel, uploadAction);
        LoadExtendedWindow.loadNewScene(root, title);
    }

    private static void loadNewScene(Parent root, String title) {
        if (root != null) {
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        }
    }
}
