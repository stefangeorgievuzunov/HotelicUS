package hotelicus.controllers;

import hotelicus.entities.Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class AdminController {
    @FXML
    private TableView<Services> hotelsTable = new TableView<>();

    @FXML
    private TableColumn<Services, Integer> serviceId = new TableColumn<Services,Integer>("serviceId");

    @FXML
    private TableColumn<Services, String> name = new TableColumn<Services,String>("name");

    @FXML
    private TableColumn<Services, Double> price = new TableColumn<Services,Double>("price");

    public AdminController() {
        serviceId.setCellValueFactory(new PropertyValueFactory<Services, Integer>("serviceId"));
        name.setCellValueFactory(new PropertyValueFactory<Services, String>("name"));
        price.setCellValueFactory(new PropertyValueFactory<Services, Double>("price"));
        final ObservableList<Services> das = FXCollections.observableArrayList(
                new Services(1, "Hotelicus", 100.0)
        );
        hotelsTable.setItems(das);
        System.out.println(das.isEmpty());
    }
}
