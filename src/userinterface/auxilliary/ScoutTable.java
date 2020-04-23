package userinterface.auxilliary;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ScoutTableModel;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoutTable extends TableView<ScoutTableModel> {
    private final int COL_WIDTH = 100;
    private final String[] colTitles = {
            "Id", "Last Name", "First Name", "Middle Name", "Date Of Birth",
            "Phone Number", "Email", "Troop Id", "Status", "Date Status Updated"
    };

    public ScoutTable(){
        this("");
    }

    public ScoutTable(String... excluded_columns){
        ArrayList<String> ex = new ArrayList<String>(Arrays.asList(excluded_columns));
        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ArrayList<TableColumn<ScoutTableModel, String>> tableColumns = new ArrayList<>();
        for(String col : colTitles){
            if(ex.contains(col)) continue;
            tableColumns.add(new TableColumn<>(col));
        }

        tableColumns.forEach(column -> {
            column.setMinWidth(COL_WIDTH);
            column.setCellValueFactory(new PropertyValueFactory<>(Utilities.toCamelCase(column.getText())));
        });

        this.getColumns().addAll(tableColumns);
    }
}
