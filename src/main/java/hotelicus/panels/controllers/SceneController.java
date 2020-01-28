package hotelicus.panels.controllers;

import hotelicus.App;
import hotelicus.panels.extended.UploadHotelForm;
import hotelicus.panels.extended.UploadReservationForm;
import hotelicus.panels.extended.UploadRoomForm;
import hotelicus.panels.extended.UploadUserForm;
import hotelicus.panels.main.AdminPanel;
import hotelicus.panels.main.OwnerPanel;
import hotelicus.panels.main.ReceptionistPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneController {
    private static final String XML_DIRECTORY = "/templates/";
    private static final String UPLOAD_ROOM_FORM_XML = "room.fxml";
    private static final String UPLOAD_USER_FORM_XML = "edit.fxml";
    private static final String UPLOAD_HOTEL_FORM_XML = "hotel.fxml";
    private static final String UPLOAD_RESERVATION_FORM_XML="reservation.fxml";
    private static final String OWNER_PANEL_XML = "ownerpanel.fxml";
    private static final String LOGIN_PANEL_XML = "login.fxml";
    private static final String ADMIN_PANEL_XML = "adminpanel.fxml";
    private static final String RECEPTIONIST_PANEL_XML="receptionistpanel.fxml";

    private static final Map<Class<?>, String> XML_RELATIONS = new HashMap<Class<?>, String>() {
        {
            put(OwnerPanel.class, OWNER_PANEL_XML);
            put(AdminPanel.class, ADMIN_PANEL_XML);
            put(ReceptionistPanel.class,RECEPTIONIST_PANEL_XML);
            put(LoginController.class, LOGIN_PANEL_XML);
            put(UploadHotelForm.class, UPLOAD_HOTEL_FORM_XML);
            put(UploadRoomForm.class, UPLOAD_ROOM_FORM_XML);
            put(UploadUserForm.class, UPLOAD_USER_FORM_XML);
            put(UploadReservationForm.class,UPLOAD_RESERVATION_FORM_XML);
        }
    };

    private static Map<Class<?>, FXMLLoader> stagesAccess = new HashMap<>();

    public static <T> void changePrimaryScene(final Class<T> clazz, final String title) throws IOException {
        if (clazz != null && XML_RELATIONS.containsKey(clazz) && title != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(XML_DIRECTORY + XML_RELATIONS.get(clazz)));
            Parent page = fxmlLoader.load();

            Scene scene = new Scene(page);
            App.getStage().setScene(scene);
            App.getStage().setTitle(title);
            App.getStage().setResizable(false);
            App.getStage().show();

            stagesAccess.put(clazz, fxmlLoader);
        }
    }

    public static <T> void openNewScene(final Class<T> clazz, final String title, Runnable block) throws IOException {
        if (clazz != null && XML_RELATIONS.containsKey(clazz) && title != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(XML_DIRECTORY + XML_RELATIONS.get(clazz)));
            Parent page = fxmlLoader.load();

            stagesAccess.put(clazz, fxmlLoader);

            if (block != null) {
                block.run();
            } else {
                throw new NullPointerException();
            }

            Scene scene = new Scene(page);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();

            stage.setOnCloseRequest(e -> {
                        if (stagesAccess.containsKey(clazz)) {
                            stagesAccess.remove(clazz);
                        }
                    }
            );
        }
    }

    public static <T> T getStageAccessTo(final Class<T> clazz) {
        if (clazz != null && stagesAccess.containsKey(clazz)) {
            T controller = stagesAccess.get(clazz).getController();
            return controller;
        }
        return null;
    }

    public static void wipeStages() {
        stagesAccess.clear();
    }
}
