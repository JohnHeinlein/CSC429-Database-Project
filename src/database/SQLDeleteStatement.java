// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /export/cvs1/repo1/GamePlayersUnlimited/database/SQLDeleteStatement.java,v $
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

import java.util.Enumeration;
import java.util.Properties;

public class SQLDeleteStatement extends SQLStatement {
    /**
     *
     * This handles only equality in the WHERE clause. This also
     * expects that for numeric types in the WHERE clause, a separate
     * Properties object containing the column name and numeric type
     * indicator will be provided. For text types, no entry in this
     * Properties object is necessary.
     */
    public SQLDeleteStatement(Properties schema, Properties whereValues) {
        theSQLStatement = "DELETE FROM " + schema.getProperty("TableName");

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

                if (theColumnValue.equals("NULL")) {
                    theWhereString.append(theColumnName).append(" IS NULL");
                } else {
                    String actualType = "Text";
                    String whereTypeValue = schema.getProperty(theColumnName);

                    if (whereTypeValue != null) {
                        actualType = whereTypeValue;
                    }

                    actualType = actualType.toLowerCase();

                    if (actualType.equals("numeric")) {
                        theWhereString.append(theColumnName).append(" = ").append(theColumnValue);
                    } else {
                        theWhereString.append(theColumnName).append(" = '").append(theColumnValue).append("'");
                    }
                }
            }
        }

        theSQLStatement += theWhereString.append(";");
    }
}

//---------------------------------------------------------------
//	Revision History:
//
//	$Log: SQLDeleteStatement.java,v $
//	Revision 1.1.1.2  2008/04/16 22:17:34  pwri0503
//	no message
//	
//	Revision 1.1.1.1  2008/04/08 19:21:02  tswa0407
//	no message
//	
//	Revision 1.2  2003/10/04 03:57:17  smitra
//	Changed to use the schema concept
//
//	Revision 1.1  2003/10/01 01:21:37  tomb
//	Initial checkin, reflects behavior extracted from EasyVideo DatabaseMutator and Accessor.
//
