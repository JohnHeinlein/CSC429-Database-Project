// tabs=4
//************************************************************
//	COPYRIGHT 2003 ArchSynergy, Ltd. - ALL RIGHTS RESERVED
//
// This file is the product of ArchSynergy, Ltd. and cannot be
// reproduced, copied, or used in any shape or form without
// the express written consent of ArchSynergy, Ltd.
//************************************************************
//
//	$Source: /cvsroot/EZVideo/impresario/ModelRegistry.java,v $
//
//	Reason: The registry mechanism for the Model object in
//			an MVC relationship.
//
//	Revision History: See end of file.
//
//*************************************************************

// JavaDoc information
/**
 * @author $Author: smitra $  @version	$Revision: 1.6 $
 * @version $Revision: 1.6 $
 * @version $Revision: 1.6 $
 * @version $Revision: 1.6 $
 * @version $Revision: 1.6 $
 */
/** @version $Revision: 1.6 $ */

// specify the package
package impresario;

import common.PropertyFile;
import common.StringList;
import event.Event;

import java.util.Properties;
import java.util.Vector;

/**
 * This class is used to instantiate the object that is encapsulated
 * by every EasyObserver client in order to keep track of which control
 * subscribes to which key and which keys depend on which other keys.
 *
 * After the client updates its state on the basis of a posted state change,
 * this class' methods are used to update the GUI controls that subscribe to
 * the keys that depend on the key on which the state change is posted.
 */
public class ModelRegistry extends Registry {

    /** A list of keys that are dependant on other keys */
    private Properties myDependencies;

    /**
     * @param classname the name of the class that contains this Registry, debug only
     * @param dependencies the dependency information for keys
     */
    public ModelRegistry(String classname, Properties dependencies) {
        super(classname);    // build our base class

        // save our dependencies
        myDependencies = dependencies;
    }

    /**
     * @param classname the name of the class that contains this Registry, debug only
     * @param dependencyFile filename that contains the dependency information for keys
     */
    public ModelRegistry(String classname, String dependencyFile) {
        super(classname);    // build our base class

        // save our dependencies
        myDependencies = new PropertyFile(dependencyFile);
    }

    /**
     * @param classname filename that contains the dependency information for keys
     */
    public ModelRegistry(String classname) {
        super(classname);
        myDependencies = new Properties();  // may be replaced later
    }

    /**
     * @param dependencies filename that contains the dependency information for keys
     */
    public void setDependencies(Properties dependencies) {
        myDependencies = dependencies;
    }


    /**
     * This is the method that actually checks the dependency file for the key on which the
     * state change was posted, finds out which keys depend on this key, and then
     * invokes the update methods on all the Views that subscribe to each dependant key.
     * The value sent to the View comes from this object.
     * Note: This version of updateSubscribers is only called by a Model.
     *
     * @param    key        Value of key on which the state change was posted and whose
     *					dependencies must be determined
     */
    public void updateSubscribers(String key, IModel client) {
        // now update all the subscribers to the changed key
        StringList propertyList = new StringList(key + "," + myDependencies.getProperty(key));

        while (propertyList.hasMoreElements()) {
            // Pick out each dependant property from the list
            String dependProperty = propertyList.nextElement();

            // Get all subscribers to this dependant property
            Object tempObj = mySubscribers.get(dependProperty);

            // make sure we have subscribers
            if (tempObj == null) {
                continue;
            }

            // see if we have multiple subscribers
            if (tempObj instanceof Vector vector) {
                // get the list of elements
                vector.forEach(subscriber -> {
                    // update via a key-value pair
                    if (subscriber instanceof IView view) {
                        view.updateState(dependProperty, client.getState(dependProperty));
                    } else {
                        new Event(Event.getLeafLevelClassName(this), "UpdateSubscribers", "EVT_InvalidSubscriber", "Vector Invalid Subscriber: " + subscriber.getClass(), Event.WARNING);
                    }
                });
            } else {    // we must have a single subscriber
                // If not, use the standard update via a key-value pair
                if (tempObj instanceof IView view) {
                    view.updateState(dependProperty, client.getState(dependProperty));
                } else {
                    new Event(Event.getLeafLevelClassName(this), "UpdateSubscribers", "EVT_InvalidSubscriber", "Invalid Subscriber: " + tempObj.getClass(), Event.WARNING);
                }
            }
        }
    }
}

//**************************************************************
//	Revision History:
//
//	$Log: ModelRegistry.java,v $
//	Revision 1.6  2004/01/18 05:52:06  smitra
//	Removed a lot of debug o/p stmts
//
//	Revision 1.5  2003/10/24 17:45:32  tomb
//	Fixed base class name from Registry1 to Registry.
//
//	Revision 1.4  2003/10/24 17:31:38  tomb
//	Added default initialization of dependency properties.
//
//	Revision 1.3  2003/10/24 12:31:43  tomb
//	Updated to use Registry1 as a base class.
//
//	Revision 1.2  2003/10/24 05:45:19  tomb
//	Changed register to subscribe.
//
//	Revision 1.1  2003/10/24 04:12:07  tomb
//	The original Registry class has been split into ModelRegistry and Controlregistry to better reflect their behavrio relative to the new MVC Impresario interfaces.
//
