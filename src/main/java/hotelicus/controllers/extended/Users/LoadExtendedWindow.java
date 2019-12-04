package hotelicus.controllers.extended.Users;

import hotelicus.App;
import hotelicus.controllers.extended.UploadHotelForm;
import hotelicus.controllers.extended.UploadRoomForm;
import hotelicus.entities.Hotels;
import hotelicus.entities.Rooms;
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
    public static void loadUploadUserFormWindow(TableView tableView, String title, Users user, UserPrivileges privileges, UploadAction uploadAction) throws IOException {
        if (tableView != null && title != null && privileges != null && uploadAction != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/edit.fxml"));

            Parent root = (Parent) fxmlLoader.load();
            UploadUserForm controller = fxmlLoader.<UploadUserForm>getController();
            controller.init(tableView, user, privileges, uploadAction);
            LoadExtendedWindow.loadNewScene(root, title);
        } else {
            throw new NullPointerException();
        }
    }

    public static void loadUploadRoomFormWindow(TableView tableView, String title, Hotels hotel, Rooms room, UploadAction uploadAction) throws IOException {
        if (tableView != null && title != null && hotel != null && uploadAction != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/room.fxml"));

            Parent root = (Parent) fxmlLoader.load();
            UploadRoomForm controller = fxmlLoader.<UploadRoomForm>getController();
            controller.init(tableView, hotel, room, uploadAction);
            LoadExtendedWindow.loadNewScene(root, title);
        } else {
            throw new NullPointerException();
        }
    }

    public static void loadUploadHotelFormWindow(TableView tableView, String title, Hotels hotel, UploadAction uploadAction) throws IOException {
        if (tableView != null && title != null && uploadAction != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/templates/hotel.fxml"));

            Parent root = (Parent) fxmlLoader.load();
            UploadHotelForm controller = fxmlLoader.<UploadHotelForm>getController();
            controller.init(tableView, hotel, uploadAction);
            LoadExtendedWindow.loadNewScene(root, title);
        } else {
            throw new NullPointerException();
        }
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
