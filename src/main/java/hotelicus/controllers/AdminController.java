package hotelicus.controllers;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.styles.Styles;
import hotelicus.window.Confirmation;
import hotelicus.window.Error;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.enums.UserState.ACTIVE;
import static hotelicus.enums.UserState.DISABLED;


public class AdminController {
    private TableView tab;
    private DbController<Users> users;
    public AdminController() {
        this.users= new DbController<Users>(Users.class);
        this.loadTableView();
    }

    private void loadTableView(){
        List<Users> users =this.users.findAll();

        this.tab=new TableView();
        TableColumn userId = new TableColumn("User id");
        userId.setCellValueFactory(new PropertyValueFactory<Users,Integer>("userId"));

        TableColumn username = new TableColumn("Username");
        username.setCellValueFactory(new PropertyValueFactory<Users,String>("username"));

        TableColumn password = new TableColumn("Password");
        password.setCellValueFactory(new PropertyValueFactory<Users,String>("password"));

        TableColumn privileges = new TableColumn("Privileges");
        privileges.setCellValueFactory(new PropertyValueFactory<Users, UserPrivileges>("privileges"));

        TableColumn firstName = new TableColumn("First name");
        firstName.setCellValueFactory(new PropertyValueFactory<Users,String>("firstName"));

        TableColumn lastName = new TableColumn("Last name");
        lastName.setCellValueFactory(new PropertyValueFactory<Users,String>("lastName"));

        TableColumn phoneNumber = new TableColumn("Phone number");
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Users,Double>("phoneNumber"));

        TableColumn userState = new TableColumn("User state");
        userState.setCellValueFactory(new PropertyValueFactory<Users, UserState>("userState"));

        TableColumn createdOn = new TableColumn("Created on");
        createdOn.setCellValueFactory(new PropertyValueFactory<Users,Date>("createdOn"));

        TableColumn startedOn = new TableColumn("Started on");
        startedOn.setCellValueFactory(new PropertyValueFactory<Users,Date>("startedOn"));

        TableColumn endedOn = new TableColumn("Ended on");
        endedOn.setCellValueFactory(new PropertyValueFactory<Users, Date>("endedOn"));

        TableColumn removeButton = new TableColumn("Delete Record");
        removeButton.setCellValueFactory(new PropertyValueFactory<Users,String>("removeButton"));

        TableColumn activateButton = new TableColumn("Activate Record");
        activateButton.setCellValueFactory(new PropertyValueFactory<Users,String>("activateButton"));


        removeButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("disable", Styles.DISABLE_BUTTON_STYLE, (Users user) -> {
            try{
                disableUser(user);
            }catch(IOException excep){
                excep.getMessage();
            }
            return user;
        }));

        activateButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("activate",Styles.ACTIVATE_BUTTON_STYLE, (Users user) -> {
            try{
                activateUser(user);
            }catch(IOException excep){
                excep.getMessage();
            }
            return user;
        }));

        tab.getColumns().addAll(userId, username,password,privileges,firstName,lastName,phoneNumber,userState,createdOn,endedOn,removeButton,activateButton);


        for(Users user : users){
            if(user.getPrivileges()==OWNER) {
                this.tab.getItems().add(user);
            }
        }
    }

    private void activateUser(Users user)throws IOException{
        Confirmation confirm=new Confirmation("Confirmation","Do you want to activate this user?");
        if(confirm.getConfirmationResult()==true){
            if(user.getUserState()==DISABLED){
                user.setEndedOn(null);
                user.setUserState(ACTIVE);
                this.users.update(user);
                this.tab.refresh();
            }else{
               new Error("Message","ALREADY ACTIVE !");
            }
        }
    }

    private void disableUser(Users user)throws IOException{
        Date deletedOn=new Date();
        Confirmation confirm=new Confirmation("Confirmation","Do you want to disable this user?");
        if(confirm.getConfirmationResult()==true){
          if(user.getUserState()==ACTIVE){
              user.setEndedOn(deletedOn);
              user.setUserState(DISABLED);
              this.users.update(user);
              this.tab.refresh();
          }
          else{
              new Error("Message","ALREADY DISABLED !");
          }
        }
    }

    public TableView getTableView(){
        return this.tab;
    }
}

