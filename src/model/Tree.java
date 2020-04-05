package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import utilities.Debug;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Tree extends EntityBase implements IModel, IView {

    private static final String myTableName = "tree";
    protected Properties dependencies;
    private TreeType myType;
    private String updateStatusMessage = "";

    public Tree() {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
    }

    // Our [B]onstructor
    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (barcode = " + barcode + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple Trees matching barcode : " + barcode + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no treetype found for this user name, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No Tree Type matching barcode : " + barcode + " found.");
        }
    }

    public Tree(Properties props) {
        super(myTableName);
        setDependencies();

        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //-Because we Deserve nice things
    public void update(String key) {
        updateStateInDatabase(key);
    }

    //Updating Database State
    private void updateStateInDatabase(String key) {
        try {
            if (key.equalsIgnoreCase("update")) {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode", persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree data for barcode number : " + persistentState.getProperty("barcode") + " updated successfully in database!";
            } else if (key.equalsIgnoreCase("insert")) {
                insertPersistentState(mySchema, persistentState);
                updateStatusMessage = "Tree data for new account : " + persistentState.getProperty("barcode")
                        + "installed successfully in database!";
            } else if (key.equalsIgnoreCase("delete")) {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode", persistentState.getProperty("barcode"));

                deletePersistentState(mySchema, whereClause);
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Tree data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    @Override
    public Object getState(String key) {
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("InsertTree")) {
            Properties data = (Properties) value;
            for (String s : new String[]{"barcode", "treeType", "notes", "status", "dateStatusUpdated"}) {
                persistentState.setProperty(s, data.getProperty(s));
            }
        } else if (value instanceof String val) {
            persistentState.setProperty(key, val);
            Debug.logMsg(String.format("Updating state \"%s\" to value \"%s\"", key, val));
        }
        updateStateInDatabase(key);
//        persistentState.clear();
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
            Debug.logMsg("Schema initialized");
        }
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    private void setDependencies() {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<>();

        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("treeType"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));

        return v;
    }
}
