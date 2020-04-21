package model;

import impresario.IView;
import utilities.Debug;

import java.util.Arrays;
import java.util.Properties;
import java.util.Vector;

public class TreeCollection extends EntityBase implements IView {
    private static final String myTableName = "tree";
    private Vector<Tree> treeList;

    public TreeCollection() {
        this(new Vector<Tree>());
    }

    public TreeCollection(Vector<Tree> trees) {
        super(myTableName);
        treeList = trees;
    }

    public Vector<Tree> findTrees(String barcode) {
        String query = "SELECT * FROM " + myTableName + " WHERE barcode LIKE '%" + barcode + "%'";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved == null) {
            Debug.logErr("No data retrieved");
            return null;
        } else {
            Debug.logMsg("Found trees: " + Arrays.deepToString(allDataRetrieved.toArray()));
        }

        for (Properties properties : allDataRetrieved) {
            Tree tree = new Tree(properties);
            treeList.add(tree);
        }
        return treeList;
    }

    @Override
    public void updateState(String key, Object value) {
        if (key.equals("Trees")) {
            treeList = (Vector<Tree>) value;
        }
    }

    @Override
    public Object getState(String key) {
        if (key.equals("Trees")) {
            return treeList;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }

    @Override
    protected void initializeSchema(String tableName) {

    }
}
