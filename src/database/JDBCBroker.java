// tabs=4
//************************************************************
//	COPYRIGHT 2007 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$URL: svn://archsynergy.net/Volumes/Storage/SEEMTool/Implementation/database/JDBCBroker.java $
//
//	$Date: 2007-01-08 19:49:24 -0500 (Mon, 08 Jan 2007) $
//
//	Reason: Manage the connection to the database. This is a singleton!
//
//*************************************************************

/**
 * @author $Author: tomb $  @version	$Revision: 168 $  @version	$Revision: timmullins,2008-02-20 $
 * @version $Revision: 168 $  @version $Revision: timmullins,2008-02-20 $
 * @version $Revision: 168 $  @version $Revision: timmullins,2008-02-20 $
 * @version $Revision: 168 $  @version $Revision: timmullins,2008-02-20 $
 */
/** @version $Revision: 168 $ */
/** @version $Revision: timmullins,2008-02-20 $ */
package database;

import common.PropertyFile;
import event.Event;
import utilities.Debug;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;

public class JDBCBroker {

    public static Driver theDriver = null;
    // Single broker to be shared by all other Servlets
    private static JDBCBroker myInstance = null;
    private static Connection theDBConnection = null;

    // DB Access data
    private String dbName;
    private String username;
    private String password;
    private String server;

    protected JDBCBroker() {
        PropertyFile props = new PropertyFile("dbConfig.ini");
        dbName = props.getProperty("dbName");
        username = props.getProperty("username");
        password = props.getProperty("password");
        server = props.getProperty("server");
        String driverClassName = "com.mysql.jdbc.Driver";

        try {
            // load and register the JDBC driver classes
            theDriver = (Driver) Class.forName(driverClassName).newInstance();
        } catch (ClassNotFoundException exc) {
            Debug.logErr("Could not load driver class: ClassNotFoundException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        } catch (InstantiationException exc) {
            Debug.logErr("Could not load driver class: InstantiationException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        } catch (IllegalAccessException exc) {
            Debug.logErr("Could not load driver class: IllegalAccessException");
            new Event(Event.getLeafLevelClassName(this), "JDBCBroker", "Could not load driver class[" + driverClassName + "]", Event.ERROR);
        }
    }

    static public JDBCBroker getInstance() {
        if (myInstance == null) {
            myInstance = new JDBCBroker();
        }

        return myInstance;
    }

    /** Create a connection to the database */
    public Connection getConnection() {
        if (myInstance != null && theDBConnection == null && dbName != null && username != null && password != null) {
            try {
                Debug.logMsg("Connecting to database at " + server);
                theDBConnection =
                        theDriver.connect(String.format("jdbc:mysql://%s/%s?user=%s&password=%s",
                                server, dbName, username, password), null);
                if (theDBConnection == null)
                    Debug.logErr("Could not connect to database!");
            } catch (SQLException exc) {
                Debug.logErr("Could not connect to database! (SQL Exception)" + "\n" + exc.getMessage());
            }
        }
        return theDBConnection;
    }

    /** Release a previously allocated connection */
    public void releaseConnection(Connection connection) {
        // don't release the connection, hang on till we're destructed
    }

    protected void finalize() {
        if (theDBConnection != null) {
            try {
                theDBConnection.close();
                theDBConnection = null;
            } catch (SQLException exc) {
                new Event(Event.getLeafLevelClassName(this), "releaseConnection", "Could not release connection", Event.WARNING);
            }
        }
    }
}
