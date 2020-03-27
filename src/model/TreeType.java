package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import utilities.Debug;
import impresario.IModel;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class TreeType extends EntityBase implements IModel, IView {

    private static final String myTableName = "treeType";
    private static Vector<Properties> table;
    protected Properties dependencies;
    private String updateStatusMessage = "";

    public TreeType() {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        // Populate table with the SQL table
        updateTable();
        Debug.logMsg("table = \n\t" + table.toString().replaceAll("},", "}\n\t"));
    }

    // Our [B]onstructor
    public TreeType(String BarcodePrefix) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();

        String query = "SELECT  * FROM " + myTableName + " WHERE (barcodePrefix = " + BarcodePrefix + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple accounts matching Prefix : " + BarcodePrefix + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeTypeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeTypeData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String) allKeys.nextElement();
                    String nextValue = retrievedTreeTypeData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        }
        // If no treetype found for this user name, throw an exception
        else {
            throw new InvalidPrimaryKeyException("No Tree Type matching id : " + BarcodePrefix + " found.");
        }
    }

    //-Because we Deserve nice things
    public void update() {
        updateStateInDatabase();
    }

    //Updating Database State
    private void updateStateInDatabase() {
            try
            {
                if (persistentState.getProperty("id") != null)
                {
                    Properties whereClause = new Properties();
                    whereClause.setProperty("id",
                            persistentState.getProperty("id"));
                    updatePersistentState(mySchema, persistentState, whereClause);
                    updateStatusMessage = "Tree type data for id number : " + persistentState.getProperty("id") + " updated successfully in database!";
                }
                else
                {
                    Integer TreeTypeId =
                            insertAutoIncrementalPersistentState(mySchema, persistentState);
                    persistentState.setProperty("id", "" + TreeTypeId.intValue());
                    updateStatusMessage = "Account data for new account : " +  persistentState.getProperty("id")
                            + "installed successfully in database!";
                }
            }
            catch (SQLException ex)
            {
                updateStatusMessage = "Error in installing Treetype data in database!";
            }
            //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
        }

    /**
     * Update the singleton table with values from the database
     */
    public void updateTable() {
        Debug.logMsg("Generating table...");
        table = getPersistentState(getSchemaInfo(myTableName), null);
        Debug.logMsg("Done");
    }

    /**
     * Gets the type of tree (description from database) for given barcode prefix
     *
     * @param barcodeId
     * @return
     */ //TODO: Turn me into a getState request?
    public static String getType(String barcodeId) {
        if (table == null) {
            Debug.logErr("No table found, generating...");
            new TreeType().updateTable();
        }

        Debug.logMsg("Getting type for barcodeId: " + barcodeId);

        for (Properties prop : table) {
            if (((String) prop.get("barcodePrefix")).startsWith(barcodeId)) {
                return (String) prop.get("typeDescription");
            }
        }
        return "Invalid barcode";
    }

    @Override
    public Object getState(String key) {
        if (key.equals("getId")) {
            return persistentState.getProperty("id");
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

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
}