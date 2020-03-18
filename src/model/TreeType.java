package model;

import Utilities.Debug;
import impresario.IModel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class TreeType extends EntityBase implements IModel {
    private static final String myTableName = "treeType";
    private static Vector<Properties> table;

    public TreeType(){
        super(myTableName);

        // Populate table with the SQL table
        updateTable();
        Debug.logMsg("table = \n\t" + table.toString().replaceAll("},","}\n\t"));
    }

    /**
     * Update the singleton table with values from the database
     */
    public void updateTable(){
        Debug.logMsg("Generating table...");
        table = getPersistentState(getSchemaInfo(myTableName), null);
        Debug.logMsg("Done");
    }

    /**
     * Gets the type of tree (description from database) for given barcode prefix
     * @param barcodeId
     * @return
     */ //TODO: Turn me into a getState request?
    public static String getType(String barcodeId){
        if(table == null){
            Debug.logErr("No table found, generating...");
            new TreeType().updateTable();
        }

        Debug.logMsg("Getting type for barcodeId: " + barcodeId);
        for(Properties prop: table){
            if( ((String)prop.get("barcodePrefix")).startsWith(barcodeId) ){
                return (String)prop.get("description");
            }
        }
        return "Invalid barcode";
    }

    @Override
    public Object getState(String key) {

        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }

    @Override
    protected void initializeSchema(String tableName) {

    }
}