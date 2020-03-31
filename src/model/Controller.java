package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;
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
    private TreeType treeType;

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
        switch (key) {
            case "ScoutList":
                return scoutCollection;
            case "Scout":
                return scout;
            default: return null;
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        Properties props;

        switch (key) {
            case "ScoutRegister":
            case "ScoutUpdateDelete":

            case "TreeAdd":
            case "TreeUpdateDelete":
            case "TreeUpdate":
            case "TreeDelete":

            case "TreeTypeAdd":
            case "TreeTypeUpdate":

            case "TreeSell":

            case "ShiftOpen":
            case "ShiftClose":

            // Case when no other processing is needed
            case "Generic":
                createAndShowView(key + "View");
                break;

            //***************
            // Insertions
            //***************
            case "ScoutRegisterSubmit":
                Debug.logMsg("Processing scout registration");
                props = (Properties)value;
                scout = new Scout();
                scout.persistentState = props;
                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString());
                scout.persistentState.clear(); //Not totally sure if this is kosher
                break;

            //***************
            // Updates
            //***************
            case "ScoutSearch":
                //TODO: Allow user to specify which field they are going to search for
                props = (Properties)value;
                Vector<Scout> scouts = null;

                String firstName = (String)props.get("firstName");
                String lastName = (String)props.get("lastName");
                String email = (String)props.get("email");

                scoutCollection = new ScoutCollection();

                if(firstName != null){
                    scouts = scoutCollection.findScoutsWithFirstName(firstName);
                }else if(lastName != null){
                    scouts = scoutCollection.findScoutsWithLastName(lastName);
                }else if(email != null){
                    scouts = scoutCollection.findScoutsWithEmail(email);
                }else{
                    Debug.logErr("(" + key + ")" + "No valid field retrieved");
                }
                scoutCollection.updateState("Scouts",scouts);

                Debug.logMsg("Created scout collection with scouts: " + Arrays.deepToString(scouts.toArray()));

                createAndShowView("ScoutCollectionView");
                break;

            case "ScoutUpdate":
                try{
                    scout = new Scout((String)value);
                }catch(InvalidPrimaryKeyException ex){
                    Debug.logErr(String.format("(%s) Invalid scout ID",key));
                }
                createAndShowView("ScoutUpdateView");
                break;

            case "ScoutUpdateSubmit":
                Debug.logMsg("(" + key + ") Processing scout registration");
                props = (Properties) value;
                for(Object field : props.keySet()){
                    scout.persistentState.setProperty(
                            (String)field,
                            (String)props.get(field));
                }
                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString()); //Internally calls update()
                break;

            case "ScoutDelete":
                try{
                    scout = new Scout((String)value);
                }catch(InvalidPrimaryKeyException ex){
                    Debug.logErr(String.format("(%s) Invalid scout ID",key));
                }
                scout.updateState("status","Inactive");
                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Scout status set to Inactive");
                Optional<ButtonType> confirm = alert.showAndWait();
                if(confirm.get() == ButtonType.OK){
                    this.stateChangeRequest("Cancel",null);
                }
                break;

            //***************
            // Add Tree Type
            //***************
            case "TreeTypeAddSubmit":
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
                break;

            case "TreeAddSubmit":
                Debug.logMsg(String.format("(%s) Processing Tree Insertion",key));
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
                break;

            case "Cancel":
                createAndShowView("ControllerView");
                break;
            default:
                Debug.logErr("Invalid key " + key);
                break;
        }
    }
}
