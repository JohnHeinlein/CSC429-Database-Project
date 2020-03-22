package model;

import java.util.Properties;
import impresario.IView;

public class Tree extends EntityBase implements IView {

    private static final String myTableName = "tree";
    private TreeType myType;

    public Tree(){
        super(myTableName);
        persistentState = new Properties();
    }

    /**
     *
     * @param barcode Primary key to table
     */
    public Tree(String barcode){
        super(myTableName);


    }

    // Returns the type of the tree from the barcode
    public void getType(){

    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        return persistentState.get(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }

    @Override
    protected void initializeSchema(String tableName) {

    }
}
