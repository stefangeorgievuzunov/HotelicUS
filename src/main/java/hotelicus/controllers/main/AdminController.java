package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.Users.UsersTableController;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;

import hotelicus.window.Confirmation;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.styles.Styles.*;

public final class AdminController {
private static VBox vbox;
private static MenuBar menu;
private static UsersTableController tableView;
private static Button refresh;
private static Button logOff;


    public AdminController(){
        AdminController.initWindow();
    }

    private static void initWindow(){
        AdminController.loadMenuBar();
        AdminController.loadTableView();

       try{
           AdminController.loadRefreshButton();
           AdminController.loadLogOffButton();
       }catch(Exception excp){
           System.out.println(excp.getMessage());
       }

        HBox buttonsGroup=new HBox(AdminController.refresh,AdminController.logOff);
        buttonsGroup.setPadding(new Insets(10,10,10,10));
        buttonsGroup.setSpacing(5);

        AdminController.vbox=new VBox();
        AdminController.vbox.getChildren().addAll(AdminController.menu,AdminController.tableView.getTableView(),buttonsGroup);

        Scene scene=new Scene(vbox);
        App.getAppStage().setScene(scene);
        App.getAppStage().setTitle("ADMIN PANEL");
        App.getAppStage().show();
    }
    private static void loadMenuBar(){
        AdminController.menu=new MenuBar();

        Menu add=new Menu("Add");
        MenuItem addNewUser=new MenuItem("New User");
        add.getItems().add(addNewUser);

        Menu filter=new Menu("Filter");
        MenuItem active= new MenuItem("Active");
        MenuItem disabled = new MenuItem("Disabled");
        filter.getItems().addAll(active,disabled);

        active.setOnAction(e->{
            AdminController.tableView.getTableView().getItems().clear();
            AdminController.tableView.loadActiveUsers(OWNER);
        });

        disabled.setOnAction(e->{
            AdminController.tableView.getTableView().getItems().clear();
            AdminController.tableView.loadDisabledUsers(OWNER);
        });

        AdminController.menu.getMenus().addAll(add,filter);

        addNewUser.setOnAction(e->{
           try{
               App.loadUploadUserFormWindow("ADD NEW RECORD", UploadAction.INSERT,new Users(), OWNER);
           }catch(IOException excep){
               excep.getMessage();
           }
        });
    }

    private static void loadTableView(){
        AdminController.tableView=new UsersTableController(OWNER);
    }

    private static void loadRefreshButton()throws FileNotFoundException {
        FileInputStream input = new FileInputStream(REFRESH_BUTTON_IMG_PATH);
        Image refreshButtonImg=new Image(input);

        AdminController.refresh=new Button();
        AdminController.refresh.setStyle(REFRESH_BUTTON_STYLE);
        AdminController.refresh.setGraphic(new ImageView(refreshButtonImg));

        AdminController.refresh.setOnAction(e->{
            AdminController.tableView.getTableView().getItems().clear();
            AdminController.tableView.loadAllUsers(OWNER);
    });
    }

    private static void loadLogOffButton()throws FileNotFoundException,IOException {
        FileInputStream input = new FileInputStream(LOG_OFF_BUTTON_IMG_PATH);
        Image refreshButtonImg=new Image(input);

        AdminController.logOff=new Button();
        AdminController.logOff.setStyle(LOG_OFF_BUTTON_STYLE);
        AdminController.logOff.setGraphic(new ImageView(refreshButtonImg));

        AdminController.logOff.setOnAction(e->{
            Confirmation logConfirmation = new Confirmation("Message","Are you sure you want to log off ?");
            if(logConfirmation.getConfirmationResult()==true){
                App.loginWindow();
            }
        });
    }


    private static TableView getTableView(){
        return AdminController.tableView.getTableView();
    }
}
