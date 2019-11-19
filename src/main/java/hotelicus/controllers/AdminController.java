package hotelicus.controllers;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.entities.Users;
import hotelicus.enums.UserPrivileges;
import hotelicus.enums.UserState;
import hotelicus.window.Confirmation;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.enums.UserState.DISABLED;


public class AdminController {
    private TableView tab;
    private DbController<Users> users;
    public AdminController() {
        this.loadTableView();
    }

    private void loadTableView(){
        this.users= new DbController<Users>(Users.class);
        List<Users> owners =this.users.findAll();

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

        TableColumn removeButton = new TableColumn("Delete Record");
        removeButton.setCellValueFactory(new PropertyValueFactory<Users,String>("removeButton"));

        TableColumn activateButton = new TableColumn("Activate Record");
        activateButton.setCellValueFactory(new PropertyValueFactory<Users,String>("activateButton"));

        //activateButton.setCellFactory(cellFactory);
        removeButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("Remove", (Users u) -> {
            try{
                deleteUser(u);
            }catch(IOException excep){
                excep.getMessage();
            }

            return u;
        }));
        tab.getColumns().addAll(userId, username,password,privileges,firstName,lastName,phoneNumber,userState,createdOn,endedOn,removeButton,activateButton);


        for(Users owner : owners){
            if(owner.getPrivileges()==OWNER) {
                tab.getItems().add(owner);
            }
        }
    }

    private void deleteUser(Users user)throws IOException{
        Date deletedOn=new Date();

        Confirmation confirm=new Confirmation("Confirmation","Do you want to delete this row?");
        if(confirm.getConfirmationResult()==true){
            Users updatedUser=new Users();
            updatedUser=user;
            updatedUser.setEndedOn(deletedOn);
            updatedUser.setUserState(DISABLED);
            this.users.update(updatedUser);
            System.out.println(user.getUserState());
            this.tab.refresh();
        }
    }

    public TableView getTableView(){
        return this.tab;
    }
}

