package hotelicus.controllers;

import com.sun.javafx.scene.control.DoubleField;
import com.sun.javafx.scene.control.IntegerField;
import hotelicus.entities.Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class AdminController {
    private TableView tab;

    public AdminController() {

        DbController<Services> services= new DbController<Services>(Services.class);

        List<Services> ourServices=services.findAll();

         tab = new TableView();
//        Services service = new Services(1,"test12",10.2);
        TableColumn serviceId = new TableColumn("serviceId");
        serviceId.setCellValueFactory(new PropertyValueFactory<Services,Integer>("serviceId"));

        TableColumn name = new TableColumn("name");
        name.setCellValueFactory(new PropertyValueFactory<Services,String>("name"));

        TableColumn price = new TableColumn("price");
        price.setCellValueFactory(new PropertyValueFactory<Services,Double>("price"));

        tab.getColumns().addAll(serviceId, name,price);


       for(Services service : ourServices){
            tab.getItems().add(service);
        }
    }

    public TableView getTableView(){
        return this.tab;
    }
}
