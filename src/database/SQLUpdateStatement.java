// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be 
// reproduced, copied, or used in any shape or form without 
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLUpdateStatement.java,v $
//
//	Reason: Represents a SQL Statement that can be applied to
//			a database.
//
//	Revision History: See end of file.
//
//*************************************************************

/**
 * @author $Author: pwri0503 $  @version	$Revision: 1.1.1.2 $
 */
/** @version $Revision: 1.1.1.2 $ */


// specify the package
package database;

// system imports
import java.util.Enumeration;
import java.util.Properties;

// project imports

// Beginning of DatabaseManipulator class
//---------------------------------------------------------------------------------------------------------
public class SQLUpdateStatement extends SQLStatement {
    /**
     *
     * This handles only equality in the WHERE clause. This also 
     * expects that for numeric types in the WHERE clause, a separate
     * Properties object containing the column name and numeric type
     * indicator will be provided. For text types, no entry in this
     * Properties object is necessary.
     */
    //------------------------------------------------------------
    public SQLUpdateStatement(Properties schema,        // the table's schema
                              Properties updateValues,    // the values to update
                              Properties whereValues)    // condition update values
    {
        super();    // implicit, doesn't do anything, but what the hell

        // Begin construction of the actual SQL statement
        theSQLStatement = "UPDATE " + schema.getProperty("TableName");

        // Construct the SET part of the SQL statement
        StringBuilder theSetString = new StringBuilder();

        // Now, traverse the update Properties object (used for creating
        // the SET part of this statement)
        Enumeration theSetColumns = updateValues.propertyNames();
        while (theSetColumns.hasMoreElements()) {
            if (theSetString.toString().equals("")) {
                theSetString.append(" SET ");
            } else {
                theSetString.append(" , ");
            }

            String theColumnName = (String) theSetColumns.nextElement();
            String theColumnValue = insertEscapes(updateValues.getProperty(theColumnName));

            String updateType = schema.getProperty(theColumnName);

            // if the type is numeric, do NOT include quotes
            if (updateType.equals("numeric")) {
                theSetString.append(theColumnName).append(" = ").append(theColumnValue);
            } else {
                // must the a text type, include the quotes
                theSetString.append(theColumnName).append(" = '").append(theColumnValue).append("'");
            }
        }

        theSQLStatement += theSetString;

        // Now, construct the WHERE part of the SQL statement
        StringBuilder theWhereString = new StringBuilder();

        // Now, traverse the WHERE clause Properties object
        if (whereValues != null) {
            Enumeration theWhereColumns = whereValues.propertyNames();
            while (theWhereColumns.hasMoreElements()) {
                if (theWhereString.toString().equals("")) {
                    theWhereString.append(" WHERE ");
                } else {
                    theWhereString.append(" AND ");
                }

                String theColumnName = (String) theWhereColumns.nextElement();
                String theColumnValue = insertEscapes(whereValues.getProperty(theColumnName));
                String whereType = schema.getProperty(theColumnName);

                // if the type is numeric, do NOT include quotes
                if (whereType.equals("numeric")) {
                    theWhereString.append(theColumnName).append(" = ").append(theColumnValue);
                } else {
                    // must the a text type, include the quotes
                    theWhereString.append(theColumnName).append(" = '").append(theColumnValue).append("'");
                }
            }
        }

        theSQLStatement += theWhereString + ";";
    }
}


//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLUpdateStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.4  2004/01/21 16:02:54  smitra
//	No changes - turned debug on and off
//	
//	Revision 1.3  2003/10/07 20:35:50  tomb
//	Reformatted, tweeked code for type detection.
//	
//	Revision 1.2  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//	
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//	
