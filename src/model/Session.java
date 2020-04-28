package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import utilities.Debug;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Session extends EntityBase implements IModel, IView {
    private static final String myTableName = "session";
    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // Our [B]onstructor
    public Session(String SessionId) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (id = " + SessionId + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple sessions matching id : " + SessionId + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedSessionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedSessionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedSessionData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no Session found for this id, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No session matching id : " + SessionId + " found.");
        }
    }

    public Session (int endingCash) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (endingCash = " + 0 + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple sessions matching ending cash : " + 0 + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedSessionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedSessionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedSessionData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no Session found for this id, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No session matching id : " + endingCash + " found.");
        }
    }

    //-- NEW CONSTRUCTOR EMPTY PROPERTIES
    public Session() {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
    }

    public boolean checkIfActiveSession() {
        String query = "SELECT  * FROM " + myTableName + " WHERE (endingCash = 0)";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved.size() == 0) {
            return false;
        } else {
            return true;
        }
    }



    // Can also be used to create a NEW Session (if the system it is part of
    // allows for a new Session to be set up)
    //----------------------------------------------------------
    public Session(Properties props) {
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
        return switch (key) {
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "schema" -> {
                if (mySchema == null) initializeSchema(myTableName);
                yield mySchema;
            }
            default -> persistentState.getProperty(key);
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("InsertSession")) {
            Properties data = (Properties) value;
            for (String s : new String[]{"startDate", "startTime", "endTime", "startingCash", "endingCash", "totalCheckTransactionsAmount", "notes"}) {
                persistentState.setProperty(s, data.getProperty(s));
            }
        } else if (key.equals("insert")) {
            this.updateStateInDatabase();
            persistentState.clear();
        } else if (value instanceof String val) {
            persistentState.setProperty(key, val);
            Debug.logMsg(String.format("Updating state \"%s\" to value \"%s  \"", key, val));
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
    public void update() {
        this.updateStateInDatabase();
    }

    //Updating Database State
    private void updateStateInDatabase() {
        Debug.logMsg("Updating Session ID " + persistentState.getProperty("id"));
        try {
            if (persistentState.getProperty("id") != null) {
                Debug.logMsg("Session id not null, updating state");

                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Session data for id number : " + persistentState.getProperty("id") + " updated successfully in database!";
            } else {
                Debug.logMsg("Session id null, generating...");

                int sessionId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("id", String.valueOf(sessionId));

                Debug.logMsg("Updating state");
                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Session data for new Session : " + persistentState.getProperty("id") + "installed successfully in database!";
            }

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Session data in database!";
            ex.printStackTrace();
        }
    }

    private void setDependencies() {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    // For table Views
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<>();

        v.addElement(persistentState.getProperty("startDate"));
        v.addElement(persistentState.getProperty("startTime"));
        v.addElement(persistentState.getProperty("endTime"));
        v.addElement(persistentState.getProperty("startingCash"));
        v.addElement(persistentState.getProperty("endingCash"));
        v.addElement(persistentState.getProperty("totalCheckTransactionsAmount"));
        v.addElement(persistentState.getProperty("notes"));

        return v;
    }
}
