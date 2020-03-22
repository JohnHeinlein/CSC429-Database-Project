// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLInsertStatement.java,v $
//
//	Reason: Represents a SQL Statement that can be applied to
//			a database.
//
//	Revision History: See end of file.
//
//*************************************************************

/**
 * @author $Author: pwri0503 $  @version	$Revision: 1.1.1.2 $
 * @version $Revision: 1.1.1.2 $
 */
/** @version $Revision: 1.1.1.2 $ */


// specify the package
package database;

// system imports
import java.util.Enumeration;
import java.util.Properties;

// Beginning of DatabaseManipulator class
//---------------------------------------------------------------------------------------------------------
public class SQLInsertStatement extends SQLStatement {
    /**
     *
     * This handles only equality in the WHERE clause. This also
     * expects that for numeric types in the WHERE clause, a separate
     * Properties object containing the column name and numeric type
     * indicator will be provided. For text types, no entry in this
     * Properties object is necessary.
     */
    //------------------------------------------------------------
    public SQLInsertStatement(Properties schema,        // the name of the table to insert into
                              Properties insertValues)    // the values to insert
    {
        // Begin construction of the actual SQL statement
        theSQLStatement = "INSERT INTO " + schema.getProperty("TableName");

        // Construct the column name list and values part of the SQL statement
        StringBuilder theColumnNamesList = new StringBuilder();
        StringBuilder theValuesString = new StringBuilder();

        // Now, traverse the Properties object. In this case, this loop
        // must go at least one or we will get an error back from the db
        Enumeration theValuesColumns = insertValues.propertyNames();

        while (theValuesColumns.hasMoreElements()) {
            if ((theValuesString.toString().equals("")) && (theColumnNamesList.toString().equals(""))) {
                theValuesString.append(" VALUES ( ");
                theColumnNamesList.append(" ( ");
            } else {
                theValuesString.append(" , ");
                theColumnNamesList.append(" , ");
            }

            String theColumnName = (String) theValuesColumns.nextElement();
            String theColumnValue = insertEscapes(insertValues.getProperty(theColumnName));
            theColumnNamesList.append(theColumnName);
            String insertType = schema.getProperty(theColumnName);

            if (insertType.equals("numeric")) {
                theValuesString.append(theColumnValue);
            } else {
                theValuesString.append("'").append(theColumnValue).append("'");
            }
        }

        // this must be the case for an insert statement
        if (!theValuesString.toString().equals("") && !theColumnNamesList.toString().equals("")) {
            theValuesString.append(" ) ");
            theColumnNamesList.append(" ) ");
        }

        theSQLStatement += theColumnNamesList.append(theValuesString).append(";");
    }
}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLInsertStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.3  2003/10/08 17:17:25  tomb
//	Reformatted slightly, cleaned up type detection.
//
//	Revision 1.2  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//
