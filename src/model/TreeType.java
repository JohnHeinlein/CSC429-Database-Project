package model;

import impresario.IModel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

public class TreeType extends EntityBase implements IModel {
    private static final String myTableName = "treeType";

    public TreeType(){
        super(myTableName);
        Vector<Object> idk = getPersistentState(
                getSchemaInfo(myTableName),
                null);

        for(Object obj : idk){
            System.out.println(obj);
        }
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
