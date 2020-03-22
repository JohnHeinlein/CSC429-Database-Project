package userinterface;

import impresario.IModel;
import javafx.scene.layout.VBox;
import model.TreeType;

public class TreeAddView extends View {

    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        TextFieldWrapper barcodeField = makeField("Barcode");
        TextFieldWrapper typeField = makeField("(Type determined by barcode)",false);

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

        addContent("Type",
                typeField);

        addContent("Notes",
                makeNotesField("Notes", 100)); //TODO: Get max length from schema

        addContent("Status",
                makeComboBox("Active", "Inactive"));

        submitButton("TreeAddSubmit");
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
