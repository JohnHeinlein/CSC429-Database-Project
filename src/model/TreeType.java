package model;

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
        table = getPersistentState(getSchemaInfo(myTableName), null);

    }

    public void updateTable(){
        table = getPersistentState(getSchemaInfo(myTableName), null);
    }

    public static String getType(String barcodeId){
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
