package hotelicus.panels.extended;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionButtonTrigger<T> extends TableCell<T, Button> {

    private final Button actionButton;
    private  TableView table;

    public ActionButtonTrigger(String label, String buttonStyle, TableView table, Function<T, T> function) {
        this.actionButton = new Button(label);
        this.table=table;
        if(buttonStyle!=null){
            this.actionButton.setStyle(buttonStyle);
        }
        this.actionButton.setOnAction((ActionEvent e) -> {
            this.table.getSelectionModel().clearSelection();
            this.table.requestFocus();
            this.table.getFocusModel().focus(getTableRow().getIndex());
            function.apply(getCurrentItem());
        });
    }

    public T getCurrentItem() {
        return (T) this.table.getItems().get(getIndex());
    }

    public static <T> Callback<TableColumn<T, Button>, TableCell<T, Button>> forTableColumn(String label,String buttonStyle,TableView table, Function<T, T> function) {
        return param -> new ActionButtonTrigger<>(label,buttonStyle,table, function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            this.setAlignment(Pos.CENTER);
            setGraphic(this.actionButton);
        }
    }
}