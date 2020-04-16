package model;

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

public class TransactionCollection extends EntityBase implements IView {
    private static final String myTableName = "transaction";
    private Vector<Transaction> transactionList;

    public TransactionCollection(){ this(new Vector<Transaction>()); }
    public TransactionCollection(Vector<Transaction> transactions){
        super(myTableName);
        transactionList = transactions;
    }

    public Vector<Transaction> findTransactionsWithSessionId(String sessionId){
        String query = "SELECT * FROM " + myTableName + " WHERE sessiondId = '" + sessionId +"'";
        return findTransactions(query);
    }

    private Vector<Transaction> findTransactions(String query) {
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if(allDataRetrieved == null){
            Debug.logErr("No data retrieved");
            return null;
        }else {
            Debug.logMsg("Found transactions: " + Arrays.deepToString(allDataRetrieved.toArray()));
        }
        for (Properties properties : allDataRetrieved) {
            Transaction trans = new Transaction(properties);
            transactionList.add(trans);
        }
        return transactionList;
    }

    public Integer insertTransaction(Properties properties) throws SQLException {
        return insertAutoIncrementalPersistentState(mySchema, properties);
    }

    public Object getState(String key){
      return null;
    }

    public void stateChangeRequest(String key, Object value){
        myRegistry.updateSubscribers(key, this);
    }

    public Transaction retrieve(String transId){
        Transaction retValue = null;
        for(int i = 0; i < transactionList.size(); i++){
            Transaction nextTrans = transactionList.elementAt(i);
            String nextTransNum = (String)nextTrans.getState("transId");
            if(nextTransNum.equals(transId)){
                retValue = nextTrans;
                return nextTrans;
            }
        }
        return retValue;
    }

    public void updateState(String key, Object value){
        if(key.equals("Transactions")){
            transactionList = (Vector<Transaction>)value;
        }
        stateChangeRequest(key,value);
    }

    protected void createAndShowView(){
        //javaFX stuff
    }

    protected  void initializeSchema(String tableName){
        if(mySchema == null) mySchema = getSchemaInfo((tableName));
    }
}
