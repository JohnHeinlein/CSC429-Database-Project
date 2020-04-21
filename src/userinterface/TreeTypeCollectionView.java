package userinterface;

import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.TreeType;
import model.TreeTypeCollection;
import model.TreeTypeTableModel;
import utilities.Alerts;
import utilities.Utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class TreeTypeCollectionView extends View {

    protected final int COL_WIDTH = 200;

    protected TableView<TreeTypeTableModel> tableOfTypes;
    protected TreeTypeTableModel selection; //Selected type

    public TreeTypeCollectionView(IModel model) {
        super(model, "TreeTypeCollectionView");
        setTitle("Search results");

        tableOfTypes = new TableView<>();
        tableOfTypes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        List<TableColumn<TreeTypeTableModel, String>> tableColumns = Arrays.asList(
                //new TableColumn<>("Id"),
                new TableColumn<>("Type Description"),
                new TableColumn<>("Cost"),
                new TableColumn<>("Barcode Prefix")
        );

        tableColumns.forEach(column -> {
            column.setMinWidth(COL_WIDTH);
            column.setCellValueFactory(new PropertyValueFactory<>(Utilities.toCamelCase(column.getText())));
        });

        tableOfTypes.getColumns().addAll(tableColumns);
        tableOfTypes.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown() && e.getClickCount() >= 2) {
                selection = tableOfTypes.getSelectionModel().getSelectedItem();
            }
        });

        addContent(tableOfTypes);

        footButt(makeButt("Update", e -> {
            if (selection == null) {
                Alerts.errorMessage("Must double click a Tree Type!");
            } else {
                myModel.stateChangeRequest("TreeTypeCollectionSubmit", selection.getBarcodePrefix());
            }
        }));

        cancelButton();

        getEntryTableModelValues();

    }

    protected void getEntryTableModelValues() {
        ObservableList<TreeTypeTableModel> tableData = FXCollections.observableArrayList();

        TreeTypeCollection collection = (TreeTypeCollection) myModel.getState("TreeTypeList");
        Vector<TreeType> entryList = (Vector<TreeType>) collection.getState("TreeTypes");

        for (TreeType type : entryList) {
            tableData.add(new TreeTypeTableModel(type.getEntryListView()));
        }

        tableOfTypes.setItems(tableData);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
