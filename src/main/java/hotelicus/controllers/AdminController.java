package hotelicus.controllers;

import com.sun.javafx.scene.control.DoubleField;
import com.sun.javafx.scene.control.IntegerField;
import hotelicus.entities.Services;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

import static hotelicus.enums.UserPrivileges.OWNER;


public class AdminController {
    private TableView tab;

    public AdminController() {
        this.loadTableView();
    }

    public void loadTableView(){
        DbController<Users> users= new DbController<Users>(Users.class);

        List<Users> owners =users.findAll();

        tab=new TableView();
        TableColumn userId = new TableColumn("userId");
        userId.setCellValueFactory(new PropertyValueFactory<Users,Integer>("userId"));

        TableColumn username = new TableColumn("username");
        username.setCellValueFactory(new PropertyValueFactory<Users,String>("username"));

        TableColumn password = new TableColumn("password");
        password.setCellValueFactory(new PropertyValueFactory<Users,String>("password"));

        TableColumn privileges = new TableColumn("privileges");
        privileges.setCellValueFactory(new PropertyValueFactory<Users, UserPrivileges>("privileges"));

        TableColumn firstName = new TableColumn("firstName");
        firstName.setCellValueFactory(new PropertyValueFactory<Users,String>("firstName"));

        TableColumn lastName = new TableColumn("lastName");
        lastName.setCellValueFactory(new PropertyValueFactory<Users,String>("lastName"));

        TableColumn phoneNumber = new TableColumn("phoneNumber");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Users,Double>("phoneNumber"));

        TableColumn userState = new TableColumn("userState");
        userState.setCellValueFactory(new PropertyValueFactory<Users, UserState>("userState"));

        TableColumn createdOn = new TableColumn("createdOn");
        createdOn.setCellValueFactory(new PropertyValueFactory<Users,Date>("createdOn"));

        TableColumn startedOn = new TableColumn("startedOn");
        startedOn.setCellValueFactory(new PropertyValueFactory<Users,Date>("startedOn"));

        TableColumn endedOn = new TableColumn("endedOn");
        endedOn.setCellValueFactory(new PropertyValueFactory<Users, Date>("endedOn"));

        tab.getColumns().addAll(userId, username,password,privileges,firstName,lastName,phoneNumber,userState,createdOn,endedOn);


        for(Users owner : owners){
            if(owner.getPrivileges()==OWNER)
            tab.getItems().add(owner);
        }
    }
    public TableView getTableView(){
        return this.tab;
    }
}
