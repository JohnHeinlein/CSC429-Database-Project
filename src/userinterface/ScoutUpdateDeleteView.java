package userinterface;

import impresario.IModel;

public class ScoutUpdateDeleteView extends View {

    public ScoutUpdateDeleteView(IModel model) {
        super(model, "ScoutUpdateDeleteView");

        setTitle("Update or Delete a Scout");

        addContent("Name",
                makeField("First Name"),
                makeField("Last Name"));

        addContent("Email",
                makeField("Email"));

        miscButton("Update", "ScoutUpdateSubmit", null);
        miscButton("Delete", "ScoutDeleteSubmit", null);
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
