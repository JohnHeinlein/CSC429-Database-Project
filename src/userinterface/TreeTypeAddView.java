package userinterface;

import impresario.IModel;

public class TreeTypeAddView extends View{

    public TreeTypeAddView(IModel model) {
        super(model, "TreeTypeAddView");

        setTitle("Add a Tree Type");

        addContent("Barcode Prefix",
                makeField("Barcode Prefix"));

        addContent("Type Description",
                makeField("Type Description"));

        addContent("Cost",
                makeField("Cost"));

        submitButton("TreeTypeAddSubmit");
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
