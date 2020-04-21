package model;

//System imports

import impresario.IView;
import utilities.Debug;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

//import javafx.scene.Scene;
//Project imports

public class ScoutCollection extends EntityBase implements IView {
    private static final String myTableName = "scout";
    private Vector<Scout> scoutList;

    public ScoutCollection() {
        this(new Vector<Scout>());
    }

    public ScoutCollection(Vector<Scout> scouts) {
        super(myTableName);
        scoutList = scouts;
    }

    public Vector<Scout> findScoutsWithFirstName(String firstName) {
        String query = "SELECT * FROM " + myTableName + " WHERE firstName LIKE '%" + firstName + "%' AND status = 'Active'";
        return findScouts(query);
    }

    public Vector<Scout> findScoutsWithLastName(String lastName) {
        String query = "SELECT * FROM " + myTableName + " WHERE lastName LIKE '%" + lastName + "%' AND status = 'Active'";
        return findScouts(query);
    }

    public Vector<Scout> findScoutsWithEmail(String email) {
        String query = "SELECT * FROM " + myTableName + " WHERE email LIKE '%" + email + "%' AND status = 'Active'";
        return findScouts(query);
    }

    public Vector<Scout> findAll() {
        String query = "SELECT * FROM " + myTableName + " WHERE status = 'Active'";
        return findScouts(query);
    }

    public boolean add(Scout s) {
        if (scoutList.contains(s)) {
            return false;
        } else {
            this.scoutList.add(s);
            return true;
        }
    }

    public boolean isEmpty() {
        if (scoutList.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean contains(Scout isThisInList) {
        for (Scout scouts : scoutList) {
            if (scouts.getState("id").equals(isThisInList.getState("id"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return scoutList.toString();
    }

    public int size() {
        return scoutList.size();
    }

    private Vector<Scout> findScouts(String query) {
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved == null) {
            Debug.logErr("No data retrieved");
            return null;
        } else {
            Debug.logMsg("Found scouts: " + Arrays.deepToString(allDataRetrieved.toArray()));
        }
        for (Properties properties : allDataRetrieved) {
            Scout scout = new Scout(properties);
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

    public Object getState(String key) {
        return switch (key) {
            case "Scouts" -> scoutList;
            case "ScoutList" -> this;
            default -> null;
        };
    }

    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    public Scout retrieve(String scoutId) {
        Scout retValue = null;
        for (int i = 0; i < scoutList.size(); i++) {
            Scout nextScout = scoutList.elementAt(i);
            String nextScoutNum = (String) nextScout.getState("scoutId");
            if (nextScoutNum.equals(scoutId)) {
                retValue = nextScout;
                return nextScout;
            }
        }
        return retValue;
    }

    protected Scout retrieve(int index) {
        return scoutList.get(index);
    }

    public void updateState(String key, Object value) {
        if (key.equals("Scouts")) {
            scoutList = (Vector<Scout>) value;
        }
        stateChangeRequest(key, value);
    }

    protected void createAndShowView() {
        //javaFX stuff
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) mySchema = getSchemaInfo((tableName));
    }
}

/**
 * public String toString(){
 * return "ID" + persistentSate.getProperty("scoutId") +
 * "scoutTitle"
 * "author"
 * }
 **/