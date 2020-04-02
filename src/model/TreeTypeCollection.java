package model;

//System imports
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;
//import javafx.scene.Scene;

//Project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IModel;
import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;
import utilities.Debug;

public class TreeTypeCollection extends EntityBase implements IModel, IView {

    private static final String myTableName = "treeType";
    private Vector<TreeType> treeTypeList;

    public TreeTypeCollection(){
        this(new Vector<TreeType>());
    }

    // -- Our Constructor
    ///////////////////////////////////////////////////////
    public TreeTypeCollection(Vector<TreeType> types){
        super(myTableName);
        treeTypeList = types;
    }

    //  -- Get All Tree Types Query
    ////////////////////////////////////////////////////////
    public Vector<TreeType> lookupAll (){
        String query = "SELECT * FROM " + myTableName + " ORDER BY barcodePrefix ASC";
        return findTypes(query);
    }

    //  -- Get all tree Type Objects
    //////////////////////////////////////////////////////////////////////////
    private Vector<TreeType> findTypes(String query) {
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if(allDataRetrieved == null){
            Debug.logErr("No data retrieved");
            return null;
        }else {
            Debug.logMsg("Found TreeTypes: " + Arrays.deepToString(allDataRetrieved.toArray()));
        }
        for (int x = 0; x < allDataRetrieved.size(); x++) {
            TreeType type = new TreeType(allDataRetrieved.get(x));
            treeTypeList.add(type);
        }
        return treeTypeList;
    }

    //  -- Get a certain one from the collection
    ////////////////////////////////////////////////////////////////////////////
    public TreeType retrieve(String id){
        TreeType retValue = null;
        for(int i = 0; i < treeTypeList.size(); i++){
            TreeType nextType = treeTypeList.elementAt(i);
            String nextTypeNum = (String)nextType.getState("getId");
            if(nextTypeNum.equals(id)){
                retValue = nextType;
                return nextType;
            }
        }
        return retValue;
    }

    //  -- Iview, Imodel Methods
    ///////////////////////////////////////////////////////////////////////////
    public void updateState(String key, Object value){
        if(key.equals("TreeTypes")){
            treeTypeList = (Vector<TreeType>)value;
        }
        stateChangeRequest(key,value);
    }


    public Object getState(String key){
            return switch (key) {
                case "TreeTypes" -> treeTypeList;
                case "TreeTypeList" -> this;
                default -> null;
            };
        }

    public void stateChangeRequest(String key, Object value){
        myRegistry.updateSubscribers(key, this);
    }

    protected  void initializeSchema(String tableName){
        if(mySchema == null) mySchema = getSchemaInfo((tableName));
    }

}
