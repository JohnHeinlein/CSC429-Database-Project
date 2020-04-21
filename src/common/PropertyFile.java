// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/ATM/implementation/common/PropertyFile.java,v $
//
//	Reason: The PropertyFile class, where config data can be stored to/
//		  read from.
//
//	Revision History: See end of file.
//
//*************************************************************

/**
 * @author $Author: smitra $  @version	$Revision: 1.1 $
 * @version $Revision: 1.1 $
 */
/** @version $Revision: 1.1 $ */
package common;

import event.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

// project imports

/** Layer a file onto a Properties object */
public class PropertyFile extends Properties {

    private String myFilename;
    private static boolean allowWrites = true;

    /** Constructs and fills the object with properties from the file */
    public PropertyFile(String filename) {
        // save the filename for later
        myFilename = filename.replace('\\', File.separatorChar);
        try (FileInputStream propFile = new FileInputStream(myFilename)) {
            load(propFile);
        } catch (FileNotFoundException exc) {
            new Event(Event.getLeafLevelClassName(this), "PropertyFile", "Could not open PropertyFile: " + myFilename, Event.WARNING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Copy constructor */
    public PropertyFile(PropertyFile props) {
        // setup our properties as copies of the original
        putAll(props);
    }

    protected PropertyFile() {
        // create an empty propertyfile
    }

    public void disallowStores() {
        allowWrites = false;
    }

    public void allowStores() {
        allowWrites = true;
    }

    /** Overload the store method to take a filename */
    public void store(String filename, String comment) {
        if (allowWrites) {
            filename = filename.replace('\\', File.separatorChar);

            try (FileOutputStream propFile = new FileOutputStream(filename)) {
                store(propFile, comment);
            } catch (Exception e) {
                new Event(Event.getLeafLevelClassName(this), "store(filename, comment)", "Could not store PropertyFile: " + filename, Event.WARNING);
                e.printStackTrace();
            }
        }
    }

    /** Provide the name of the loaded file */
    public String getFilename() {
        return myFilename;
    }

    /** Overload the store method to use the original filename */
    public void store() {
        if (allowWrites) {
            if (myFilename != null) {
                myFilename = myFilename.replace('\\', File.separatorChar);

                try (FileOutputStream propFile = new FileOutputStream(myFilename)) {
                    store(propFile, "");
                } catch (Exception e) {
                    new Event(Event.getLeafLevelClassName(this), "store", "Could not store PropertyFile: " + myFilename, Event.WARNING);
                    e.printStackTrace();
                }
            } else {
                new Event(Event.getLeafLevelClassName(this), "store", "Invalid filename to store!", Event.WARNING);
            }
        }
    }
}
