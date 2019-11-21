package hotelicus.controllers.main;

import hotelicus.App;
import hotelicus.controllers.extended.UsersTableController;
import hotelicus.entities.Users;
import hotelicus.enums.UploadAction;
import hotelicus.enums.UserPrivileges;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public final class AdminController {
private static VBox vbox;
private static MenuBar menu;
private static TableView tableView;


    public AdminController(){
        AdminController.initWindow();
    }

    private static void initWindow(){
        AdminController.loadMenuBar();
        AdminController.loadTableView();

        AdminController.vbox=new VBox();
        AdminController.vbox.getChildren().addAll(AdminController.menu,AdminController.tableView);

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

        Menu logout=new Menu("Logout");
        AdminController.menu.getMenus().addAll(add,filter,logout);

        addNewUser.setOnAction(e->{
           try{
               App.loadUploadUserFormWindow("ADD NEW RECORD", UploadAction.INSERT,new Users(), UserPrivileges.OWNER);
           }catch(IOException excep){
               excep.getMessage();
           }
        });
    }
    private static void loadTableView(){
        AdminController.tableView=new UsersTableController().getTableView();
    }
}
