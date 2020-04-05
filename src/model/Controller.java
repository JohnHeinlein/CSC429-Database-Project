package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;
import utilities.Alerts;
import utilities.Debug;

import model.Scout;

import java.util.*;
import java.time.LocalDate;

public class Controller implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry; // What even is this

    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private ScoutCollection scoutCollection;
    private Scout scout;

    private Tree tree;
    private TreeCollection treeCollection;

    private TreeType treeType;
    private TreeTypeCollection treeTypeCollection;

    public Controller() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<>();
        myRegistry = new ModelRegistry("Controller");

        setDependencies();
        createAndShowView("ControllerView");
    }

    public void createAndShowView(String viewName) {
        //Attempt to retrieve scene from table to avoid recreating it.
//        Scene currentScene = myViews.get(viewName);
//
//        if (currentScene == null) {
//            View newView = ViewFactory.createView(viewName, this);
//
//            if (newView == null) {
//                Debug.logErr("ViewFactory returned null view");
//                return;
//            }
//            currentScene = new Scene(newView);
//            myViews.put(viewName, currentScene);
//        };
//        swapToView(currentScene);
        View newView = ViewFactory.createView(viewName,this);
        Scene newScene = new Scene(newView);

        myViews.put(viewName,newScene);

        swapToView(newScene);
    }

    private void swapToView(Scene newScene) {
        myStage.setScene(newScene);
        myStage.sizeToScene();

        //Necessary to apply the resize
        myStage.hide();
        myStage.show();

        WindowPosition.placeCenter(myStage);
    }

    public void setDependencies() {/*Do nothing I guess*/}

    @Override
    public void subscribe(String key, IView subscriber) {

    }

    @Override
    public void unSubscribe(String key, IView subscriber) {

    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        if(key.substring(key.length() - 3).equals("View")){
            return myViews.get(key);
        }

        return switch (key) {
            case "ScoutList" -> scoutCollection;
            case "Scout" -> scout;

            case "TreeList" -> treeCollection;
            case "Tree" -> tree;

            case "TreeTypeList" -> treeTypeCollection;
            case "TreeType" -> treeType;

            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        Properties props;

        switch (key) {
            case
            /*Scout*/   "ScoutRegister", "ScoutUpdateDelete",
            /*Tree*/    "TreeAdd",
            /*TreeType*/"TreeTypeAdd",
            /*Sales*/   "TreeSell",
            /*Shifts*/  "ShiftOpen", "ShiftClose"
                        -> createAndShowView(key + "View");

            //***************
            // Insertions
            //***************
            case "ScoutRegisterSubmit" -> {
                Debug.logMsg("Processing scout registration");
                props = (Properties) value;

                scout = new Scout();
                scout.persistentState = props;

                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString());
                scout.persistentState.clear(); //Not totally sure if this is kosher

                Alerts.infoMessage("Scout registered successfully!",this);
            }

            //***************
            // Scout
            //***************
            case "ScoutSearch" -> {
                Pair<String,String> pair = (Pair<String,String>) value;
                Vector<Scout> scouts;

                scoutCollection = new ScoutCollection();

                scouts = switch(pair.getKey()){
                    case "firstName" -> scoutCollection.findScoutsWithFirstName(pair.getValue());
                    case "lastName" -> scoutCollection.findScoutsWithLastName(pair.getValue());
                    case "email" -> scoutCollection.findScoutsWithEmail(pair.getValue());
                    default -> {
                        Debug.logErr("(" + key + ")" + "No valid field retrieved");
                        yield null;
                    }
                };
                scoutCollection.updateState("Scouts", scouts);

                Debug.logMsg("Created scout collection with scouts: " + Arrays.deepToString(scouts.toArray()));

                createAndShowView("ScoutCollectionView");
            }

            case "ScoutUpdate" -> {
                try {
                    scout = new Scout((String) value);
                } catch (InvalidPrimaryKeyException ex) {
                    Debug.logErr(String.format("(%s) Invalid scout ID", key));
                }
                createAndShowView("ScoutUpdateView");
            }

            case "ScoutUpdateSubmit" -> {
                Debug.logMsg("(" + key + ") Processing scout registration");
                props = (Properties) value;
                for (Object field : props.keySet()) {
                    scout.persistentState.setProperty(
                            (String) field,
                            (String) props.get(field));
                }
                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString()); //Internally calls update()

                Alerts.infoMessage("Scout updated!",this);
            }

            case "ScoutDelete" -> {
                try {
                    scout = new Scout((String) value);
                } catch (InvalidPrimaryKeyException ex) {
                    Debug.logErr(String.format("(%s) Invalid scout ID", key));
                }
                scout.updateState("status", "Inactive");
                scout.updateState("dateStatusUpdated", LocalDate.now().toString());

                Alerts.infoMessage("Scout deleted!",this);
            }
            //***************
            // Tree
            //***************
            case "TreeUpdateDelete" ->{
                createAndShowView("TreeUpdateDeleteView");
            }

            case "TreeUpdate" ->{
                tree = (Tree)value;
                createAndShowView("TreeUpdateView");
            }

            case "TreeUpdateSubmit" -> {
                Debug.logMsg("(" + key + ") Processing tree registration");
                props = (Properties) value;
                for (Map.Entry<Object, Object> entry : props.entrySet()) {
                    tree.persistentState.setProperty(
                            (String) entry.getKey(),
                            (String) entry.getValue());
                }
                tree.updateState("dateStatusUpdated", LocalDate.now().toString());
                tree.update("update");

                Alerts.infoMessage("Tree " + tree.getState("barcode") + " updated!", this);
            }

            case "TreeDelete" -> {
                tree = (Tree) value;

                if (tree == null) {
                    Debug.logErr("(%s) Invalid tree barcode", key);
                } else {
                    String barcode = (String) tree.getState("barcode");
                    tree.update("delete");

                    Alerts.infoMessage("Tree " + barcode + " deleted!", this);
                }
            }

            //***************
            // Add Tree Type
            //***************
            case "TreeTypeAddSubmit" -> {
                Debug.logMsg("Processing Tree Type Add");
                try {
                    props = (Properties) value;
                    treeType = new TreeType(props.getProperty("barcodePrefix"));
                    Debug.logMsg("Tree Type With barcode prefix: " + props.getProperty("barcodePrefix") + " Already Exists");
                } catch (InvalidPrimaryKeyException IPKE) {
                    treeType = new TreeType();
                    treeType.persistentState = (Properties) value;
                    treeType.update();
                    treeType.persistentState.clear();
                }

                Alerts.infoMessage("Tree type added!",this);
            }

            //***********************************
            //  Showing all Tree Types to Select
            //***********************************
            case "TreeTypeCollection" -> {
                treeTypeCollection = new TreeTypeCollection();
                treeTypeCollection.lookupAll();

                createAndShowView("TreeTypeCollectionView");
            }

            //*************************************
            //  Upon hitting submit in the table
            //*************************************
            case "TreeTypeCollectionSubmit" -> {
                try {
                    treeType = new TreeType((String) value);
                } catch (InvalidPrimaryKeyException ex) {
                    Debug.logErr(String.format("(%s) Invalid tree type ID", key));
                }
                createAndShowView("TreeTypeUpdateView");
            }

            case "TreeTypeUpdateSubmit" -> {
                Debug.logMsg("(" + key + ") Processing tree Type registration");
                props = (Properties) value;
                for (Object field : props.keySet()) {
                    treeType.persistentState.setProperty(
                            (String) field,
                            (String) props.get(field));
                }

                treeType.update();
                Alerts.infoMessage("Tree Type updated!",this);
            }

            //****************************
            //  Add A Tree
            //****************************
            case "TreeAddSubmit" -> {
                Debug.logMsg(String.format("(%s) Processing Tree Insertion", key));
                props = (Properties) value;
                try {
                    tree = new Tree(props.getProperty("barcode"));
                    Debug.logMsg("Tree With barcode: " + props.getProperty("barcode") + " Already Exists");
                } catch (InvalidPrimaryKeyException IPKE) {
                    tree = new Tree();
                    props.setProperty("dateStatusUpdated", java.time.LocalDate.now().toString());
                    tree.persistentState = props;
                    tree.update("Insert");
                    tree.persistentState.clear();
                }

                Alerts.infoMessage("Tree added!",this);
            }

            case "Cancel" -> createAndShowView("ControllerView");

            default -> Debug.logErr("Invalid key " + key);
        }
    }
}
