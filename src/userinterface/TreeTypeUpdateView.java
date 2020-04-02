package userinterface;

// system imports
import exception.InvalidPrimaryKeyException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


// project imports
import impresario.IModel;
import model.Scout;
import model.TreeType;
import utilities.Debug;

import java.util.Properties;

public class TreeTypeUpdateView extends View {
    private TreeType treeType;

    public TreeTypeUpdateView (IModel myModel) {
        super(myModel, "TreeTypeAddView");
        treeType = (TreeType)myModel.getState("TreeType");

        setTitle("Update a Tree Type");

        addContent("Barcode Prefix",
                makeField("Barcode Prefix"));

        addContent("Type Description",
                makeField("Type Description"));

        addContent("Cost",
                makeField("Cost"));

        submitButton();
        cancelButton();

        for(String field : controlList.keySet()){
            setValue(controlList.get(field), (String)treeType.getState(field));
        }
    }

    @Override
    public void submit(){
        if(scrapeFields()) {
            try {
                if (((String)treeType.getState("barcodePrefix")).equals(props.getProperty("barcodePrefix")) == false) {
                    TreeType tType = new TreeType(props.getProperty("barcodePrefix"));
                    Debug.logMsg("Tree Type With barcode prefix: " + props.getProperty("barcodePrefix") + " Already Exists");
                }
                else {
                    myModel.stateChangeRequest("TreeTypeUpdateSubmit", props);
                }
            } catch (InvalidPrimaryKeyException IPKE) {
                myModel.stateChangeRequest("TreeTypeUpdateSubmit", props);
            }
        }else{
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    public void updateState(String key, Object value) {

    }
}
