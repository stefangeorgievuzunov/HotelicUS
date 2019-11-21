package hotelicus.controllers.extended;


import hotelicus.styles.Styles;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ActionButtonTableCell(String label,String buttonStyle, Function<S, S> function) {
        this.actionButton = new Button(label);
        if(buttonStyle!=null){
            this.actionButton.setStyle(buttonStyle);
        }
        this.actionButton.setOnAction((ActionEvent e) -> {
            getTableView().getSelectionModel().clearSelection();
            getTableView().requestFocus();
            getTableView().getFocusModel().focus(getTableRow().getIndex());
            System.out.println(getTableRow().getIndex());
            function.apply(getCurrentItem());
        });
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label,String buttonStyle, Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label,buttonStyle, function);
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