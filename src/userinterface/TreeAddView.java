package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.scene.layout.VBox;
import model.TreeType;
import utilities.Debug;

public class TreeAddView extends View {

    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        TextFieldWrapper barcodeField = makeField("Barcode");
        TextFieldWrapper typeField = makeField("Tree Type",false);

        barcodeField.setListener(((observableValue, oldVal, newVal) -> {
            if(newVal.length() == 2){
                // TODO: This certainly breaks spec. Fix it later.
                typeField.getField().setText(TreeType.getType(newVal));
            }else if(newVal.length() < 2){
                typeField.getField().setText("");
            }
        }));

        typeField.setListener(((observableValue,oldVal,newVal) ->{
            if(newVal.equals("Invalid barcode")){
                typeField.getField().setStyle("-fx-text-box-border: #ff4040 ; -fx-focus-color: #ff4040 ;");
            }else if(newVal.equals("")){
                typeField.getField().setStyle(null);
            }else{
                typeField.getField().setStyle("-fx-text-box-border: #30ff30 ; -fx-focus-color: #30ff30 ;");
            }
        }));

        addContent("Barcode",
                barcodeField);

        addContent("Tree Type",
                typeField);

        addContent("Notes",
                makeNotesField("Notes", 200));

        addContent("Status",
                makeComboBox("Available", "Sold", "Damaged"));

        submitButton();
        cancelButton();
    }

    @Override
    public void submit(){
        if(scrapeFields()) {
            try {
                TreeType check = new TreeType(props.getProperty("barcode").substring(0, 2));
                props.setProperty("treeType", (String) check.getState("getId"));
                myModel.stateChangeRequest("TreeAddSubmit", props);
            }
            catch (InvalidPrimaryKeyException IPKE) {Debug.logMsg("Error Instantiating Tree Type With Barcode prefix: " + props.getProperty("barcode").substring(0, 2)); }
        }else{
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
