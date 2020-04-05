package userinterface;

// system imports

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import model.TreeType;
import utilities.Debug;

// project imports

public class TreeTypeUpdateView extends View {
    private final TreeType treeType;

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
                if (!treeType.getState("barcodePrefix").equals(props.getProperty("barcodePrefix"))) {
                    TreeType tType = new TreeType(props.getProperty("barcodePrefix"));
                    Debug.logMsg("Tree Type With barcode prefix: " + props.getProperty("barcodePrefix") + " Already Exists");
                }
                else {
                    myModel.stateChangeRequest("TreeTypeUpdateSubmit", props);
                }
            } catch (InvalidPrimaryKeyException IPKE) {
                Debug.logErr("Invalid primary key exception caught");
                myModel.stateChangeRequest("TreeTypeUpdateSubmit", props);
            }
        }else{
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    public void updateState(String key, Object value) {

    }
}
