package userinterface;

import impresario.IModel;
import javafx.scene.control.ComboBox;
import model.TreeTypeCollection;

import java.util.Vector;

public class TreeAddView extends View {
    public TreeAddView(IModel model) {
        super(model, "TreeAddView");
        setTitle("Add a Tree");

        // Barcode input field
        TextFieldWrapper barcodeField = makeField("Barcode", 6, 6, "numeric");
        barcodeField.setListener(((observableValue, oldVal, newVal) -> {
            if(newVal.length() < 2) barcodeField.setText(oldVal);
        }));

        // Get a collection of all tree types
        TreeTypeCollection typeCollection = new TreeTypeCollection();
        typeCollection.updateState("TreeTypes",typeCollection.lookupAll());

        ComboBox<String> typeSelector = new ComboBox<>();
        // Populate combobox with description of every tree type
        typeSelector.getItems().addAll((Vector<String>)typeCollection.getState("typeDescription"));
        typeSelector.getSelectionModel().selectFirst();

        // Update barcode field with prefix of selected type
        typeSelector.getSelectionModel().selectedItemProperty().addListener((options, oldVal, newVal) -> {
                barcodeField.setText(typeCollection.getPrefixFromDescription(newVal) + barcodeField.getText().substring(2));
                barcodeField.getField().setEditable(true);
        });

        barcodeField.setText(typeCollection.getPrefixFromDescription(typeSelector.getSelectionModel().getSelectedItem()));

        addContent("Barcode",
                barcodeField);

        addContent("Tree type",
                typeSelector);

        addContent("Notes",
                makeNotesField("Notes", 200));

        submitButton();
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
