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
            String barcode = newVal;

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
