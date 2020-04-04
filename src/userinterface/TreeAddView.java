package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import model.TreeType;
import utilities.Debug;

public class TreeAddView extends View {

    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        TextFieldWrapper barcodeField = makeField("Barcode",20);
        TextFieldWrapper typeField = makeField("Tree Type",false);

        barcodeField.setListener(((observableValue, oldVal, newVal) -> {
            barcodeField.checkLength();

            if(newVal.length() == 2){
                // TODO: This certainly breaks spec. Fix it later.
                typeField.setText(TreeType.getType(newVal));
            }else if(newVal.length() < 2){
                typeField.setText("");
            }
        }));

        typeField.setListener(((observableValue,oldVal,newVal) ->{
            if(newVal.equals("Invalid barcode")){
                typeField.styleErr();
            }else if(newVal.equals("")){
                typeField.styleClear();
            }else{
                typeField.styleAccept();
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
            } catch (InvalidPrimaryKeyException IPKE) {
                Debug.logErr("Error Instantiating Tree Type With Barcode prefix: " + props.getProperty("barcode").substring(0, 2));
            }
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
