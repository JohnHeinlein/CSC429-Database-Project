package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Scout;
import model.ScoutCollection;
import utilities.Utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ScoutCollectionView extends View{
    protected TableView<ScoutTableModel> tableOfScouts;
    public ScoutCollectionView(IModel model) {
        super(model, "ScoutCollectionView");
        setTitle("Search results");

        tableOfScouts = new TableView<>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        List<TableColumn<ScoutTableModel, String>> tableColumns = Arrays.asList(
                new TableColumn<>("Id"),
                new TableColumn<>("Last Name"),
                new TableColumn<>("First Name"),
                new TableColumn<>("Middle Name"),
                new TableColumn<>("Date Of Birth"),
                new TableColumn<>("Phone Number"),
                new TableColumn<>("Email"),
                new TableColumn<>("Troop Id"),
                new TableColumn<>("Status"),
                new TableColumn<>("Date Status Updated")
        );
        tableColumns.forEach(column ->{
           column.setMinWidth(100.0);
           column.setCellValueFactory(new PropertyValueFactory<>(Utilities.toCamelCase(column.getText())));
        });

        tableOfScouts.getColumns().addAll(tableColumns);
        tableOfScouts.setOnMousePressed(e ->{
            if (e.isPrimaryButtonDown() && e.getClickCount() >= 2){
                processScoutSelected();
            }
        });

        //addContent("christ",tableOfScouts);
        addContent(tableOfScouts);
        cancelButton();

        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues(){
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();

        ScoutCollection collection = (ScoutCollection) myModel.getState("ScoutList");
        Vector<Scout> entryList = (Vector<Scout>) collection.getState("Scouts");

        for(Scout scout : entryList){
            tableData.add(new ScoutTableModel(scout.getEntryListView()));
        }

        tableOfScouts.setItems(tableData);
    }

    public void processScoutSelected(){

    }

    @Override
    public void updateState(String key, Object value) {

    }
}