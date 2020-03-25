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

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;
import utilities.Debug;

public class ScoutCollection extends EntityBase implements IView {
    private static final String myTableName = "scout";
    private Vector<Scout> scoutList;

    public ScoutCollection(){
        this(new Vector<Scout>());
    }

    public ScoutCollection(Vector<Scout> scouts){
        super(myTableName);
        scoutList = scouts;
    }

    public Vector<Scout> findScoutsWithFirstName(String firstName){
        String query = "SELECT * FROM " + myTableName + " WHERE firstName = '" + firstName + "'";
        return findScouts(query);
    }

    public Vector<Scout> findScoutsWithLastName(String lastName){
        String query = "SELECT * FROM " + myTableName + " WHERE lastName = '" + lastName + "'";
        return findScouts(query);
    }

    public Vector<Scout> findScoutsWithEmail(String email){
        String query = "SELECT * FROM " + myTableName + " WHERE email = '" + email + "'";
        return findScouts(query);
    }

    private Vector<Scout> findScouts(String query) {
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if(allDataRetrieved == null){
            Debug.logErr("No data retrieved");
            return null;
        }else {
            Debug.logMsg("Found scouts: " + Arrays.deepToString(allDataRetrieved.toArray()));
        }
        for (int x = 0; x < allDataRetrieved.size(); x++) {
            Scout scout = new Scout(allDataRetrieved.get(x));
            //scoutList.insertElementAt(scout, findIndexToAdd(scout));
            scoutList.add(scout);
        }
        return scoutList;
    }

    public Integer insertScout(Properties properties) throws SQLException {
        return insertAutoIncrementalPersistentState(mySchema, properties);
    }

//    private int findIndexToAdd(Scout b) {
//        //users.add(u);
//        int low=0;
//        int high = scoutList.size()-1;
//        int middle;
//
//        while (low <=high) {
//            middle = (low+high)/2;
//
//            Scout midSession = scoutList.elementAt(middle);
//
//            int result = Scout.compare(b,midSession);
//
//            if (result ==0) {
//                return middle;
//            }
//            else if (result<0) {
//                high=middle-1;
//            }
//            else {
//                low=middle+1;
//            }
//
//
//        }
//        return low;
//    }

    public Object getState(String key){
        switch(key){
            case "Scouts":   return scoutList;
            case "ScoutList":return this;
            default:        return null;
        }
    }

    public void stateChangeRequest(String key, Object value){
        myRegistry.updateSubscribers(key, this);
    }

    public Scout retrieve(String scoutId){
        Scout retValue = null;
        for(int i = 0; i < scoutList.size(); i++){
            Scout nextScout = scoutList.elementAt(i);
            String nextScoutNum = (String)nextScout.getState("scoutId");
            if(nextScoutNum.equals(scoutId)){
                retValue = nextScout;
                return nextScout;
            }
        }
        return retValue;
    }

    public void updateState(String key, Object value){ stateChangeRequest(key,value); }

    protected void createAndShowView(){
        //javaFX stuff
    }

    protected  void initializeSchema(String tableName){
        if(mySchema == null) mySchema = getSchemaInfo((tableName));
    }
}

/**public String toString(){
 * return "ID" + persistentSate.getProperty("scoutId") +
 *                                          "scoutTitle"
 *                                          "author"
 }
 **/