package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import model.Scout;
import model.ScoutCollection;
import model.ScoutTableModel;
import userinterface.auxilliary.ScoutTable;
import utilities.Alerts;
import utilities.Debug;

import java.util.Optional;
import java.util.Vector;

public class AllScoutsView extends View {
    protected final int COL_WIDTH = 100;

    protected ScoutTable tableOfScouts, tableOfSelected;
    protected ScoutTableModel selection;
    protected Vector<ScoutTableModel> Bois = new Vector<ScoutTableModel>();

    public AllScoutsView(IModel model) {
        super(model, "AllScoutsView");
        setTitle("Search results");

        tableOfScouts = new ScoutTable("Id", "Status", "Date Status Updated");
        tableOfSelected = new ScoutTable("Id", "Status", "Date Status Updated");

        tableOfScouts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selection = tableOfScouts.getSelectionModel().getSelectedItem();
            //tableOfSelected.getSelectionModel().clearSelection();
            try{
                Debug.logMsg("Selected %s", selection.getFirstName());
            }catch(NullPointerException ex){
                Debug.logMsg("No scout selected");
            }
        });

        tableOfSelected.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selection = tableOfSelected.getSelectionModel().getSelectedItem();
            //tableOfScouts.getSelectionModel().clearSelection();
            try{
                Debug.logMsg("Selected %s", selection.getFirstName());
            }catch(NullPointerException ex){
                Debug.logMsg("No scout selected");
            }
        });

        Button add = makeButt("->", e -> {
            if (selection != null) {
                //Scout isWorking = new Scout(selection.getId());

                // Add scout if they're not working
                if (!Bois.contains(selection)) {
                    Bois.add(selection);

                    tableOfSelected.getItems().add(selection);
                    tableOfScouts.getItems().remove(selection);
                    tableOfScouts.getSelectionModel().clearSelection();

                    selection = null;
                } else {
                    Alerts.errorMessage(String.format("Error, Scout %s %s already selected to work shift!", selection.getFirstName(), selection.getLastName()));
                }
            } else {
                Alerts.errorMessage("Must select a scout!");
            }
        });

        Button remove = makeButt("<-", e -> {
            if (selection != null) {
                // Add scout if they're not working
                if (Bois.contains(selection)) {
                    Bois.remove(selection);

                    tableOfScouts.getItems().add(selection);
                    tableOfSelected.getItems().remove(selection);
                    tableOfSelected.getSelectionModel().clearSelection();

                    selection = null;
                } else {
                    Alerts.errorMessage(String.format("Error, Scout %s %s already selected to work shift!", selection.getFirstName(), selection.getLastName()));
                }
            } else {
                Alerts.errorMessage("Must select a scout!");
            }
        });

        container.setLeft(tableOfScouts);
        container.setCenter(new VBox(add, remove));
        container.setRight(tableOfSelected);

//        footButt(makeButt("Add to Shift", e -> {
//            if (selection == null) {
//                Alerts.errorMessage("Must select a scout!");
//            } else {
//                //If you can create a scout with the scout's selected ID
//                try {
//                    Scout isWorking = new Scout(selection.getId());
//                    //If theres no scouts, add it to the collection
//                    if (Bois.isEmpty()) {
//                        Bois.add(isWorking);
//                        Debug.logMsg(selection.getFirstName() + " " + selection.getLastName() + " added to shift ");
//                    } else if (Bois.contains(isWorking) == false) {
//                        //Checking if a scout with the ID selected is already in your collection of scouts, if they are not in the collection, then add them
//                        Bois.add(isWorking);
//                        Debug.logMsg(selection.getFirstName() + " " + selection.getLastName() + " added to shift ");
//                    } else {
//                        //You already selected this scout to work this session, are you qualified for this position?
//                        Alerts.errorMessage("Error, Scout " + selection.getFirstName() + " " + selection.getLastName() + " already selected to work shift!");
//                    }
//                } catch (InvalidPrimaryKeyException IPKE) {
//                    Alerts.errorMessage("Scout with Id " + selection.getId() + " does not exist!");
//                }
//            }
//        }));

        footButt(makeButt("Done", e -> {
            if (Bois.isEmpty()) {
                //If no scouts are selected upon completion
                Alerts.errorMessage("Must select scouts to work shift!");
            } else {
                //User confirmation feedback
                Optional<ButtonType> confirmation = Alerts.confirmMessage("Are you sure you are finished selecting scouts?");
                if (confirmation.get() == ButtonType.OK) {
                    System.out.println(Bois.toString());

                    Vector<Scout> bois = new Vector<>();
                    for(ScoutTableModel foo : Bois){
                        try {
                            bois.add(new Scout(foo.getId()));
                        }catch(InvalidPrimaryKeyException ex){
                            // Shouldn't execute since we gathered these scouts from the database to begin with
                            Alerts.errorMessage("Invalid scout id: " + foo.getId());
                        }
                    }
                    myModel.stateChangeRequest("OpenShifts", new ScoutCollection(bois));
                }
            }
        }));

        cancelButton();

        getEntryTableModelValues();
    }

    protected void getEntryTableModelValues() {
        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();

        ScoutCollection collection = (ScoutCollection) myModel.getState("ScoutList");
        Vector<Scout> entryList = (Vector<Scout>) collection.getState("Scouts");

        for (Scout scout : entryList) {
            tableData.add(new ScoutTableModel(scout.getEntryListView()));
        }

        tableOfScouts.setItems(tableData);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
