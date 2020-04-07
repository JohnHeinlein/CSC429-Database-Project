package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import model.TreeType;

public class TreeAddView extends View {
    TreeType type = (TreeType)myModel.getState("TreeType");

    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        TextFieldWrapper barcodeField = makeField("Barcode",20, 3,"numeric");
        TextFieldWrapper typeField = makeField("Tree Type","barcode");
        typeField.setText("Enter a barcode"); //initial value
        typeField.getField().setEditable(false);


        barcodeField.setListener(((observableValue, oldVal, newVal) -> {
            if(newVal.length() == 2){
                try {
                    type = new TreeType(newVal);
                    typeField.setText((String)type.getState("typeDescription"));
                } catch (InvalidPrimaryKeyException e) {
                    typeField.setText("Invalid barcode");
                }
            }else if(newVal.length() < 2){
                typeField.setText("Enter a barcode");
            }
        }));

//        typeField.setListener(((observableValue,oldVal,newVal) ->{
//            if(newVal.equals("Invalid barcode")){
//                typeField.styleErr();
//            }else if(newVal.equals("")){
//                typeField.styleClear();
//            }else{
//                typeField.styleAccept();
//            }
//        }));

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

//    @Override
//    public void submit(){
//        if(scrapeFields()) {
//            try {
//                TreeType check = new TreeType(props.getProperty("barcode").substring(0, 2));
//                props.setProperty("treeType", (String) check.getState("getId"));
//                myModel.stateChangeRequest("TreeAddSubmit", props.getProperty("barcode"));
//            } catch (InvalidPrimaryKeyException IPKE) {
//                Debug.logErr("Error Instantiating Tree Type With Barcode prefix: " + props.getProperty("barcode").substring(0, 2));
//            }
//        }
//    }
    @Override
    public void submit(){
        if (scrapeFields()) {
            props.setProperty("treeType",(String)type.getState("id"));
            myModel.stateChangeRequest("TreeAddSubmit", props);
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
