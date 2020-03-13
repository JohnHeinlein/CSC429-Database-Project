package userinterface;

import impresario.IModel;

public class TreeTypeUpdateView extends View{
    public TreeTypeUpdateView(IModel model) {
        super(model, "TreeTypeUpdateView");

        setTitle("Update a Tree Type");

        submitButton("TreeTypeUpdateSubmit", null);
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
