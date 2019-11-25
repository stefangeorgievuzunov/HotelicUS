package hotelicus.controllers.extended.Users;
import hotelicus.App;
import hotelicus.controllers.extended.ActionButtonTableCell;
import hotelicus.controllers.main.DbController;
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

import static hotelicus.enums.UploadAction.EDIT;
import static hotelicus.enums.UserPrivileges.OWNER;
import static hotelicus.enums.UserState.ACTIVE;
import static hotelicus.enums.UserState.DISABLED;
import static hotelicus.styles.Styles.EDIT_BUTTON_STYLE;


public class UsersTableController {
    private  TableView tab;
    private  DbController<Users> users;
    public UsersTableController(UserPrivileges toBeLoad) {
        this.users= new DbController<Users>(Users.class);
        this.loadDefaultTableView();
        this.loadAllUsers(toBeLoad);
    }

    private void loadDefaultTableView(){
        this.tab=new TableView();
        this.tab.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        TableColumn userState = new TableColumn("User state");
        userState.setCellValueFactory(new PropertyValueFactory<Users, UserState>("userState"));

        TableColumn startedOn = new TableColumn("Started on");
        startedOn.setCellValueFactory(new PropertyValueFactory<Users,Date>("startedOn"));

        TableColumn endedOn = new TableColumn("Ended on");
        endedOn.setCellValueFactory(new PropertyValueFactory<Users, Date>("endedOn"));

        TableColumn removeButton = new TableColumn("Delete Record");
        removeButton.setCellValueFactory(new PropertyValueFactory<Users,String>("removeButton"));

        TableColumn activateButton = new TableColumn("Activate Record");
        activateButton.setCellValueFactory(new PropertyValueFactory<Users,String>("activateButton"));

        TableColumn editButton = new TableColumn("Edit Record");
        editButton.setCellValueFactory(new PropertyValueFactory<Users,String>("editButton"));


        removeButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("disable", Styles.DISABLE_BUTTON_STYLE,this.tab, (Users user) -> {
            disableUser(user);
            return user;
        }));

        activateButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("activate",Styles.ACTIVATE_BUTTON_STYLE,this.tab, (Users user) -> {
            activateUser(user);
            return user;
        }));

        editButton.setCellFactory(ActionButtonTableCell.<Users>forTableColumn("Edit",EDIT_BUTTON_STYLE,this.tab, (Users user) -> {
            try{
                editUser(user);
            }catch(IOException excep){
                System.out.println(excep.getMessage());
            }
            return user;
        }));
        this.tab.getColumns().addAll(userId, username,password,privileges,firstName,lastName,userState,startedOn,endedOn,removeButton,activateButton,editButton);
    }

    public void loadAllUsers(UserPrivileges toBeLoad){
        List<Users> users = this.users.selectEqualTo("privileges",toBeLoad,false);
        for(Users user : users){
            if(user.getPrivileges()==toBeLoad) {
                this.tab.getItems().add(user);
            }
        }
    }
    public void loadActiveUsers(UserPrivileges toBeLoad){
        List<Users> users = this.users.selectEqualTo("privileges",toBeLoad,false);
        for(Users user : users){
            if(user.getPrivileges()==toBeLoad && user.getUserState()==ACTIVE) {
                this.tab.getItems().add(user);
            }
        }
    }
    public void loadDisabledUsers(UserPrivileges toBeLoad){
        List<Users> users = this.users.selectEqualTo("privileges",toBeLoad,false);
        for(Users user : users){
            if(user.getPrivileges()==toBeLoad&& user.getUserState()==DISABLED) {
                this.tab.getItems().add(user);
            }
        }
    }

    private void activateUser(Users user) {
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

    private void disableUser(Users user){
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

    private void editUser(Users user)throws  IOException{
        App.loadUploadUserFormWindow( "EDIT RECORD", EDIT,user,user.getPrivileges());
    }

    public  TableView getTableView(){
        return this.tab;
    }
}
