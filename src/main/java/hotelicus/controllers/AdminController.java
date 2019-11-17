package hotelicus.controllers;

import hotelicus.entities.Services;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class AdminController {
    @FXML
    private TableView<Services> hotelsTable = new TableView<>();

    @FXML
    private TableColumn<Services, String> serviceId;

    @FXML
    private TableColumn<Services, String> name;

    @FXML
    private TableColumn<Services, String> price;

    public AdminController() {
        System.out.println(hotelsTable.getItems().toString());
        ObservableList<Services> data = hotelsTable.getItems();
        Services h = new Services();
        h.setServiceId(1);
        h.setName("Hotelicus");
        h.setPrice(100.0);
        data.add(h);
        System.out.println(hotelsTable.getItems().toString());
    }
}
