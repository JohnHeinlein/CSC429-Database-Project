package userinterface;

import impresario.IModel;

public class TreeUpdateDeleteView extends View {

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));

        miscButton("Update", "TreeUpdateSubmit", null);
        miscButton("Delete", "TreeDeleteSubmit", null);
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
