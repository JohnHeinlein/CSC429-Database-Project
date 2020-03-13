package userinterface;

import impresario.IModel;
import javafx.scene.layout.VBox;
import model.TreeType;

public class TreeAddView extends View {
    public TreeAddView(IModel model) {
        super (model, "TreeAddView");
        TreeType types = new TreeType();

        setTitle("Add a Tree");

        TextFieldWrapper barcodeField = makeField("Barcode");
        TextFieldWrapper typeField = makeField("(Type determined by barcode)",false);

        barcodeField.setListener(((observableValue, oldVal, newVal) -> {
            if(newVal.length() == 2){
                // I'm not sure how to get the table other than through an instance variable.
                // TODO: This certainly breaks spec. Fix it later.
                typeField.getField().setText(types.getType(newVal));
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

        addContent("Status",
                makeComboBox("Active", "Inactive"));

        submitButton("TreeAddSubmit", null);
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
