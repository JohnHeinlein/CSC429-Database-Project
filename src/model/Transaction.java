package model;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import utilities.Debug;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class Transaction extends EntityBase implements IView, IModel {
    private static final String myTableName = "transaction";
    protected Properties dependencies;

    // GUI Components
    private String updateStatusMessage = "";

    // Our [B]onstructor
    public Transaction(String transId) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (id = " + transId + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple transactions matching id : " + transId + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedtransData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedtransData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedtransData.getProperty(nextKey);
                    // bookId = Integer.parseInt(retrievedBookData.getProperty("bookId"));

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no Scout found for this user name, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No transaction matching id : " + transId + " found.");
        }
    }

    //-- NEW CONSTRUCTOR EMPTY PROPERTIES
    public Transaction() {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
    }

    // Can also be used to create a NEW Scout (if the system it is part of
    // allows for a new Scout to be set up)
    //----------------------------------------------------------
    public Transaction(Properties props) {
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
        if (key.equals("InsertTransaction")) {
            Properties data = (Properties) value;
            for (String s : new String[]{"sessionId", "transactionType", "barcode", "transactionAmount", "paymentMethod", "customerName", "customerPhone", "customerEmail", "transactionDate", "transactionTime", "dateStatusUpdated"}) {
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

    //Updating Database State
    private void updateStateInDatabase() {
        Debug.logMsg("Updating Transaction ID " + persistentState.getProperty("id"));
        try {
            if (persistentState.getProperty("id") != null) {
                Debug.logMsg("Transaction id not null, updating state");

                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Transaction data for id number : " + persistentState.getProperty("id") + " updated successfully in database!";
            } else {
                Debug.logMsg("Transaction id null, generating...");

                int transId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("id", String.valueOf(transId));

                Debug.logMsg("Updating state");
                Properties whereClause = new Properties();
                whereClause.setProperty("id", persistentState.getProperty("id"));
                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Transaction data for new transaction : " + persistentState.getProperty("id") + "installed successfully in database!";
            }

        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing Transaction data in database!";
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

        v.addElement(persistentState.getProperty("id"));
        v.addElement(persistentState.getProperty("sessionId"));
        v.addElement(persistentState.getProperty("transactionType"));
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("transactionAmount"));
        v.addElement(persistentState.getProperty("paymentMethod"));
        v.addElement(persistentState.getProperty("customerName"));
        v.addElement(persistentState.getProperty("customerPhone"));
        v.addElement(persistentState.getProperty("customerEmail"));
        v.addElement(persistentState.getProperty("transactionDate"));
        v.addElement(persistentState.getProperty("transactionTime"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));

        return v;
    }
}
