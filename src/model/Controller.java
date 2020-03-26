package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;
import utilities.Debug;

import model.Scout;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class Controller implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry; // What even is this

    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private ScoutCollection scoutCollection;

    public Controller() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<>();
        myRegistry = new ModelRegistry("Controller");

        setDependencies();
        createAndShowView("ControllerView");
    }

    public void createAndShowView(String viewName) {
        //Attempt to retrieve scene from table to avoid recreating it.
        Scene currentScene = myViews.get(viewName);

        if (currentScene == null) {
            View newView = ViewFactory.createView(viewName, this);

            if (newView == null) {
                Debug.logErr("ViewFactory returned null view");
                return;
            }
            currentScene = new Scene(newView);
            myViews.put(viewName, currentScene);
        };
        swapToView(currentScene);
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
            default: return null;
        }
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
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
                Scout newBoye = new Scout();
                newBoye.persistentState = (Properties)value;
                newBoye.update();
                newBoye.persistentState.clear();
                break;

            //***************
            // Updates
            //***************
            case "ScoutUpdateSubmit":
                Properties props = (Properties)value;
                Vector<Scout> scouts = null;

                scoutCollection = new ScoutCollection();

                String firstName = (String)props.get("firstName");
                String lastName = (String)props.get("lastName");
                String email = (String)props.get("email");

                if(firstName != null){
                    scouts = scoutCollection.findScoutsWithFirstName(firstName);
                }else if(lastName != null){
                    scouts = scoutCollection.findScoutsWithLastName(lastName);
                }else if(email != null){
                    scouts = scoutCollection.findScoutsWithEmail(email);
                }else{
                    Debug.logErr("(" + key + ")" + "No valid field retrieved");
                }
                scoutCollection = new ScoutCollection(scouts);
                Debug.logMsg("Created scout collection with scouts: " + Arrays.deepToString(scouts.toArray()));

                createAndShowView("ScoutCollectionView");
                break;

            //***************
            // Deletions
            //***************
            case "ScoutDeleteSubmit":

                break;
            //***************
            // Add Tree Type
            //***************
            case "TreeTypeAddSubmit":
                Debug.logMsg("Processing Tree Type Add");
                try {
                    Properties data = (Properties) value;
                    TreeType newTreeBoye = new TreeType(data.getProperty("barcodePrefix"));
                    Debug.logMsg("Tree Type With barcode prefix: " + data.getProperty("barcodePrefix") + " Already Exists");
                } catch (InvalidPrimaryKeyException IPKE) {
                    Properties data = (Properties) value;
                    TreeType newTreeBoye = new TreeType();
                    newTreeBoye.persistentState = (Properties) value;
                    newTreeBoye.update();
                    newTreeBoye.persistentState.clear();
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
