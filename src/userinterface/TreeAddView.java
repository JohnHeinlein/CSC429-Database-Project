package userinterface;

import impresario.IModel;

public class TreeAddView extends View {
    public TreeAddView(IModel model) {
        super (model, "TreeAddView");

        setTitle("Add a Tree");

        addContent("Barcode",
                makeField("Barcode"));

        addContent("Status",
                makeComboBox("Active", "Inactive"));

        submitButton("TreeAddSubmit", null);
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
