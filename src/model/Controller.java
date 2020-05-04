package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;
import javafx.util.Pair;
import userinterface.*;
import utilities.Alerts;
import utilities.Debug;

import java.time.LocalDate;
import java.util.*;


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

    private Transaction transaction;
    private TransactionCollection transactionCollection;

    private Session session;
    private Shift shift;

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
        switch (key) {
            case "ScoutList" -> scoutCollection = (ScoutCollection) value;
            case "Scout" -> scout = (Scout) value;

            case "TreeList" -> treeCollection = (TreeCollection) value;
            case "Tree" -> tree = (Tree) value;

            case "TreeTypeList" -> treeTypeCollection = (TreeTypeCollection) value;
            case "TreeType" -> {
                if (value instanceof String) { //Provided barcode prefix
                    String pref = ((String) value).substring(0, 1);
                    try {
                        treeType = new TreeType("pref");
                    } catch (InvalidPrimaryKeyException ex) {
                        treeType = null; //Invalid key
                    }
                } else if (value instanceof TreeType) {
                    treeType = (TreeType) value;
                }
            }
        }
    }

    @Override
    public Object getState(String key) {
        if (key.substring(key.length() - 3).equals("View")) {
            return myViews.get(key);
        }

        return switch (key) {
            case "ScoutList" -> scoutCollection;
            case "Scout" -> scout;

            case "TreeList" -> treeCollection;
            case "Tree" -> tree;

            case "TreeTypeList" -> {
                if(treeTypeCollection == null){
                    treeTypeCollection = new TreeTypeCollection();
                    treeTypeCollection.updateState("TreeTypes",treeTypeCollection.lookupAll());
                }
                yield treeTypeCollection;
            }
            case "TreeType" -> {
                if (treeType == null) treeType = new TreeType();
                yield treeType;
            }

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
                    /*Shifts*/  "ShiftOpen" -> createAndShowView(key + "View");

            //************************************************
            // Creation of a new session upon opening a shift
            //************************************************
            case "SessionCreate" -> {
                Debug.logMsg("Processing session creation");
                props = (Properties) value;
                session = new Session();
                //Only one session can be open at a time, Sessions are checked if the Endingcash is 0 (because the session hasnt ended yet)
                //Creation of a session is not allowed if one is already active
                if (!session.checkIfActiveSession()) {
                    //setting defaults
                    props.setProperty("totalCheckTransactionsAmount", "0");
                    props.setProperty("endingCash", "0");
                    props.setProperty("startDate", java.time.LocalDate.now().toString());

                    session.persistentState = props;
                    session.update(); //Updated state in database

                    //Getting List of all active scouts
                    Debug.logMsg("Showing all scouts...");
                    scoutCollection = new ScoutCollection();
                    scoutCollection.findAll();
                    createAndShowView("AllScoutsView");
                } else {
                    Alerts.infoMessage("Session already in progress, please click 'Close shift' and close the open shift", this);
                }

            }

            //*************************************************
            //  Shifts
            //*************************************************

            case "OpenShifts" -> {
                scoutCollection = (ScoutCollection) value;
                Debug.logMsg("Requesting to open shifts for " + scoutCollection.size() + " scouts");

                int completed = 0; // How many scouts shift records we have completed
                boolean cancel = false;
                while (completed < scoutCollection.size()) {
                    Dialog<Properties> dialog = new Dialog<>();

                    View view = new ShiftDataView(this);
                    dialog.getDialogPane().setContent(view);

                    ButtonType nextButtonType = new ButtonType("Open Shift", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                    dialog.getDialogPane().getButtonTypes().addAll(nextButtonType,cancelButtonType);

                    //What is done after you click OK on the form
                    dialog.setResultConverter(
                            dialogButton -> {
                                if(dialogButton == nextButtonType){
                                    return view.getProps();
                                }else if(dialogButton == cancelButtonType){
                                    dialog.close();
                                }
                                return null;
                            }
                    );

                    // Creation of the shift record
                    Optional<Properties> result = dialog.showAndWait();
                    if(result.isEmpty()){  cancel = true; break; }

                    System.out.println(result.get().toString());
                    shift = new Shift();
                    shift.persistentState.setProperty("sessionId", (String) session.getState("id"));
                    shift.persistentState.setProperty("scoutId", (String) scoutCollection.retrieve(completed).getState("id"));
                    shift.persistentState.setProperty("startTime", (String) session.getState("startTime"));
                    shift.persistentState.setProperty("companionName", result.get().getProperty("companionName"));
                    shift.persistentState.setProperty("companionHours", result.get().getProperty("companionHours"));
                    shift.persistentState.setProperty("endTime", result.get().getProperty("endingTime"));
                    shift.update();
                    completed++; // We just finished our shift creation, onto the next.
                }
                if(cancel == true) {
                    cancel = false;
                } else {
                    Alerts.infoMessage("All Shifts Opened Successfully!", this);
                }
            }

            case"ShiftClose" -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Test");
                alert.setHeaderText("Close a Shift");
                alert.setResizable(false);
                alert.setContentText("Are you sure you want to close the current session?");

                Optional<ButtonType> result = alert.showAndWait();
                ButtonType button = result.orElse(ButtonType.CANCEL);

                if (button == ButtonType.OK) {
                    transactionCollection = new TransactionCollection();
                    try {
                        session = new Session(0); //GETTING CURRENT SESSION ID
                        Vector<Transaction> v = transactionCollection.findTransactionsWithSessionId((String)session.getState("id"));
                        double totalCheck = 0;
                        double totalCash = Double.parseDouble((String)session.getState("startingCash"));
                        for(Transaction trans : v) {
                            if(trans.persistentState.getProperty("paymentMethod").equals("Cash")) {
                                totalCash += Double.parseDouble(trans.persistentState.getProperty("transactionAmount"));
                            }
                            else if (trans.persistentState.getProperty("paymentMethod").equals("Check")) {
                                totalCheck += Double.parseDouble(trans.persistentState.getProperty("transactionAmount"));
                            }
                        }
                        session.persistentState.setProperty("endingCash", (String)("" + totalCash));
                        session.persistentState.setProperty("totalCheckTransactionsAmount", (String)("" + totalCheck));
                        session.update();
                        Alerts.infoMessage("Shift Closed.", this);
                    } catch (InvalidPrimaryKeyException IPKE) {
                        Alerts.infoMessage("ERROR: No active Session to close", this);
                        return;
                    }
                } else {
                    Debug.logMsg("Close Shift Canceled");
                    return;
                }
            }

            //***************
            // Scout
            //***************

            case "ScoutRegisterSubmit" -> {
                Debug.logMsg("Processing scout registration");
                props = (Properties) value;
                props.setProperty("status", "Active");

                scout = new Scout();
                scout.persistentState = props;

                scout.updateState("dateStatusUpdated", java.time.LocalDate.now().toString());
                scout.updateState("insert", null);
                //scout.persistentState.clear(); //Not totally sure if this is kosher

                Alerts.infoMessage("Scout registered successfully!", this);
            }

            case "ScoutSearch" -> {
                Pair<String, String> pair = (Pair<String, String>) value;
                Vector<Scout> scouts;

                scoutCollection = new ScoutCollection();

                scouts = switch (pair.getKey()) {
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
                scout.updateState("insert", null);

                Alerts.infoMessage("Scout updated!", this);
            }

            case "ScoutDelete" -> {
                try {
                    scout = new Scout((String) value);
                } catch (InvalidPrimaryKeyException ex) {
                    Debug.logErr(String.format("(%s) Invalid scout ID", key));
                }
                scout.updateState("status", "Inactive");
                scout.updateState("dateStatusUpdated", LocalDate.now().toString());
                scout.updateState("insert", null);

                Alerts.infoMessage("Scout deleted!", this);
            }

            //***************
            // TreeType
            //***************
            case "TreeTypeAddSubmit" -> {
                Debug.logMsg("Processing Tree Type Add");
                try {
                    props = (Properties) value;
                    treeType = new TreeType(props.getProperty("barcodePrefix"));
                    Debug.logMsg("Tree Type With barcode prefix: " + props.getProperty("barcodePrefix") + " Already Exists");
                    Alerts.infoMessage("Tree Type With barcode prefix: " + props.getProperty("barcodePrefix") + " Already Exists", this);
                } catch (InvalidPrimaryKeyException IPKE) {
                    treeType = new TreeType();
                    treeType.persistentState = (Properties) value;
                    treeType.update();
                    treeType.persistentState.clear();
                    Alerts.infoMessage("Tree type added!", this);
                }

            }

            case "TreeTypeCollection" -> {
                treeTypeCollection = new TreeTypeCollection();
                treeTypeCollection.lookupAll();

                createAndShowView("TreeTypeCollectionView");
            }

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
                Alerts.infoMessage("Tree Type updated!", this);
            }

            //***************
            // Tree
            //***************
            case "TreeUpdateDelete" -> {
                createAndShowView("TreeUpdateDeleteView");
            }

            case "TreeUpdate" -> {
                tree = (Tree) value;
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

            case "TreeAddSubmit" -> {
                Debug.logMsg(String.format("(%s) Processing Tree Insertion", key));
                props = (Properties) value;
                try {
                    tree = new Tree(props.getProperty("barcode"));
                    Debug.logMsg("Tree With barcode: " + props.getProperty("barcode") + " Already Exists");
                } catch (InvalidPrimaryKeyException IPKE) {
                    String barcode = props.getProperty("barcode");
                    try {
                        treeType = new TreeType(props.getProperty("barcode").substring(0, 2));
                    }catch(InvalidPrimaryKeyException ex){
                        Debug.logErr("Invalid barcode prefix %s", barcode.substring(0,2));
                    }

                    Properties treeProps = new Properties();
                    treeProps.setProperty("barcode", props.getProperty("barcode"));
                    treeProps.setProperty("treeType",(String) treeType.getState("id"));
                    treeProps.setProperty("notes", props.getProperty("notes"));
                    treeProps.setProperty("status","available");
                    treeProps.setProperty("dateStatusUpdated", java.time.LocalDate.now().toString());
                    tree = new Tree();
                    tree.persistentState = treeProps;
                    tree.update("Insert");
                    tree.persistentState.clear();
                }

                Alerts.infoMessage("Tree added!", this);
            }

            //****************************
            //  Sell a tree
            //****************************
            case "TreeSellSubmit" -> {
                try {
                    String barcode = ((Properties)value).getProperty("barcode");
                    String prefix = barcode.substring(0,2);

                    tree = new Tree(barcode);
                    treeType = new TreeType(prefix);
                    transaction = new Transaction();

                    transaction.persistentState.setProperty("barcode", barcode);
                    transaction.persistentState.setProperty("transactionAmount", (String)treeType.getState("cost"));
                    String status = (String) tree.getState("status");
                    if(!( status.equals("Available")) || status.equals("Damaged")){
                        Alerts.errorMessage("Tree unavailable!");
                        return;
                    }

                    String cost = (String) treeType.getState("cost");
                    String type = (String) treeType.getState("typeDescription");

                    Optional<ButtonType> confirm = Alerts.confirmMessage(String.format("Confirm %s with cost %s", type, cost));
                    if(confirm.get() == ButtonType.OK){
                        createAndShowView("TreeSellInfoView");
                    }
                }catch(InvalidPrimaryKeyException IPKE){
                    Alerts.errorMessage("Invalid barcode!");
                }
            }

            case "TreeSellInfoViewSubmit" -> {
                props = (Properties) value;
                try {
                    session = new Session(0); // THIS GETS THE CURRENT ACTIVE SESSION DATA
                    if (session.checkIfActiveSession()) {
                        transaction.persistentState.setProperty("sessionId", (String) session.getState("id"));
                        transaction.persistentState.setProperty("transactionType", "Tree Sale");
                        transaction.persistentState.setProperty("paymentMethod", props.getProperty("paymentType"));
                        transaction.persistentState.setProperty("customerName", (props.getProperty("firstName") + " " + props.getProperty("lastName")));
                        transaction.persistentState.setProperty("customerPhone", props.getProperty("customerPhone"));
                        transaction.persistentState.setProperty("customerEmail", props.getProperty("customerEmail"));
                        transaction.persistentState.setProperty("transactionDate", java.time.LocalDate.now().toString());
                        transaction.persistentState.setProperty("transactionTime", java.time.LocalTime.now().toString().substring(0,8));
                        transaction.persistentState.setProperty("dateStatusUpdated", java.time.LocalDate.now().toString());

                        transaction.update();

                        tree = new Tree(transaction.persistentState.getProperty("barcode"));
                        tree.persistentState.setProperty("status", "Sold");
                        tree.update("update");

                        Alerts.infoMessage("Tree Sold", this);
                    } else {
                        Alerts.infoMessage("Error: Must sell tree under an active session, please Open a shift and try again.", this);
                        return;
                    }
                } catch (InvalidPrimaryKeyException IPKE) {
                    Alerts.infoMessage("Error: Must sell tree under an active session, please open a shift and try again.", this);
                    return;
                }
            }

            case "Cancel" -> createAndShowView("ControllerView");

            default -> Debug.logErr("Invalid key " + key);
        }
    }
}
