package model;

import Utilities.Utilities;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Hashtable;
import java.util.Properties;

public class Controller implements IView, IModel {
    private Properties dependencies;
    private ModelRegistry myRegistry; // What even is this

    //Collections

    //

    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    public Controller() {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();
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
                Utilities.logErr("ViewFactory returned null view");
                return;
            }
            currentScene = new Scene(newView);
            myViews.put(viewName, currentScene);
        }
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
            case "Cancel":
                createAndShowView("ControllerView");
            default: break;
        }
    }
}
