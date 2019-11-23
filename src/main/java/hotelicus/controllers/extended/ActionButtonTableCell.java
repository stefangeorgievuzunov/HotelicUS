package hotelicus.controllers.extended;


import hotelicus.styles.Styles;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;
    private  TableView table;

    public ActionButtonTableCell(String label, String buttonStyle,TableView table, Function<S, S> function) {
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

    public S getCurrentItem() {
        return (S) this.table.getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label,String buttonStyle,TableView table, Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label,buttonStyle,table, function);
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