package model;

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

import java.util.Hashtable;
import java.util.Properties;

public class Controller implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry; // What even is this

    private Hashtable<String, Scene> myViews;
    private Stage myStage;

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
            case "TreeTypeAdd":
            case "TreeTypeUpdate":
            case "ShiftOpen":
            case "TreeSell":
            case "ShiftClose":

                // Case when no other processing is needed
            case "Generic":
                createAndShowView(key + "View");
                break;
            case "ScoutRegisterSubmit":
                Scout newBoye = new Scout();
                Properties data = (Properties)value;
                newBoye.persistentState.setProperty("firstName", data.getProperty("firstName"));
                newBoye.persistentState.setProperty("lastName", data.getProperty("lastName"));
                newBoye.persistentState.setProperty("middleName", data.getProperty("middleName"));
                newBoye.persistentState.setProperty("dateOfBirth", data.getProperty("dateOfBirth"));
                newBoye.persistentState.setProperty("phoneNumber", data.getProperty("phoneNumber"));
                newBoye.persistentState.setProperty("email", data.getProperty("email"));
                newBoye.persistentState.setProperty("troopId", data.getProperty("troopId"));
                newBoye.persistentState.setProperty("status", data.getProperty("status"));
                newBoye.update();
                System.out.println("Scout Inserted");
                newBoye.persistentState.clear();
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
