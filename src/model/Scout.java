package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import utilities.Debug;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Scout extends EntityBase implements IView, IModel {

    private static final String myTableName = "scout";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    // Our [B]onstructor
    public Scout(String ScoutId) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (id = " + ScoutId + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple accounts matching id : " + ScoutId + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedScoutData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedScoutData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedScoutData.getProperty(nextKey);
                    // bookId = Integer.parseInt(retrievedBookData.getProperty("bookId"));

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no Scout found for this user name, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No account matching id : " + ScoutId + " found.");
        }
    }

    //-- NEW CONSTRUCTOR EMPTY PROPERTIES
    public Scout() {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
    }

    // Can also be used to create a NEW Scout (if the system it is part of
    // allows for a new Scout to be set up)
    //----------------------------------------------------------
    public Scout (Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////
    //
    //  Methods compiler will scream if we don't have
    //

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("InsertScout")) {
            Properties data = (Properties) value;
            persistentState.setProperty("firstName", data.getProperty("firstName"));
            persistentState.setProperty("lastName", data.getProperty("lastName"));
            persistentState.setProperty("middleName", data.getProperty("middleName"));
            persistentState.setProperty("dateOfBirth", data.getProperty("dateOfBirth"));
            persistentState.setProperty("phoneNumber", data.getProperty("phoneNumber"));
            persistentState.setProperty("email", data.getProperty("email"));
            persistentState.setProperty("troopId", data.getProperty("troopId"));
            persistentState.setProperty("status", data.getProperty("status"));
            persistentState.setProperty("dateStatusUpdated", data.getProperty("dateStatusUpdated"));

            this.update();

            persistentState.clear();

        }else if(value instanceof String){
            String val = (String) value;
            persistentState.setProperty(key, val);
            Debug.logMsg(String.format("Updating state \"%s\" to value \"%s\"",
                    key,val));

            this.update();
        }
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
            Debug.logMsg("Schema initialized");
        }
    }
    //
    // End Of Methods Compiler Screams about
    //
    //////////////////////////////////////////////////////////////////////////////////

    //Updating Database State
    private void updateStateInDatabase() {
        Debug.logMsg("Updating scout ID " + persistentState.getProperty("id"));
        try {
            if (persistentState.getProperty("id") != null) {
                Debug.logMsg("Scout id not null, updating state");

                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Scout data for id number : " + persistentState.getProperty("id") + " updated successfully in database!";
            } else {
                Debug.logMsg("Scout id null, generating...");

                Integer scoutId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("id", "" + scoutId.intValue());

                Debug.logMsg("Updating state");
                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Account data for new account : " + persistentState.getProperty("id") + "installed successfully in database!";
            }

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Scout data in database!";
        }
    }

    //-Because we Deserve nice things
    public void update() {
        updateStateInDatabase();
    }

    private void setDependencies() {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    // For table Views
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<>();

        v.addElement(persistentState.getProperty("id"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("middleName"));
        v.addElement(persistentState.getProperty("dateOfBirth"));
        v.addElement(persistentState.getProperty("phoneNumber"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("troopId"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));

        return v;
    }
}
