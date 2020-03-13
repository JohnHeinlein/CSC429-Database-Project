package userinterface;

import impresario.IModel;
import javafx.scene.layout.VBox;

public class TreeAddView extends View {
    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        addContent("Barcode",
                makeField("Barcode"));


        TextFieldWrapper typeField = makeField("(Type determined by barcode");

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
