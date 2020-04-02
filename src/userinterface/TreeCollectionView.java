package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import utilities.Alerts;
import utilities.Debug;
import utilities.Utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class TreeCollectionView extends View{
    protected final int COL_WIDTH = 100;

    private TableView<TreeTableModel> tableOfTrees;
    private TreeTableModel selection;

    public TreeCollectionView(IModel model) {
        super(model, "TreeCollectionView");
        setTitle("Search Results");

        tableOfTrees = new TableView<>();
        tableOfTrees.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        List<TableColumn<TreeTableModel,String>> tableColumns = Arrays.asList(
                new TableColumn<>("Barcode"),
                new TableColumn<>("Tree Type"),
                new TableColumn<>("Notes"),
                new TableColumn<>("Status"),
                new TableColumn<>("Date Status Updated")
        );

        tableColumns.forEach(column ->{
            column.setMinWidth(COL_WIDTH);
            column.setCellValueFactory(new PropertyValueFactory<>(Utilities.toCamelCase(column.getText())));
            Debug.logMsg("Factory  created for %s",Utilities.toCamelCase(column.getText()));
        });

        tableOfTrees.getColumns().addAll(tableColumns);
        tableOfTrees.setOnMousePressed(e ->{
            if (e.isPrimaryButtonDown() && e.getClickCount() >= 2){
                selection = tableOfTrees.getSelectionModel().getSelectedItem();
            }
        });

        addContent(tableOfTrees);

        footButt(makeButt("Update",e ->{
            if(selection == null){
                Alerts.errorMessage("Must select a tree!");
            }else {
                myModel.stateChangeRequest("TreeUpdate", selection.getBarcode());
            }
        }));

        footButt(makeButt("Delete",e-> {
            if(selection == null) {
                Alerts.errorMessage("Must select a tree!");
            }else {
                Optional<ButtonType> confirmation = Alerts.confirmMessage("Are you sure you want to delete Tree " +  selection.getBarcode() + "?");
                if (confirmation.get() == ButtonType.OK){
                    myModel.stateChangeRequest("TreeDelete",selection.getBarcode());
                }
            }
        }));

        cancelButton();

        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues(){
        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();

        TreeCollection collection = (TreeCollection) myModel.getState("TreeList");
        Vector<Tree> entryList = (Vector<Tree>) collection.getState("Trees");

        for(Tree tree : entryList){
            tableData.add(new TreeTableModel(tree.getEntryListView()));
        }

        tableOfTrees.setItems(tableData);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
