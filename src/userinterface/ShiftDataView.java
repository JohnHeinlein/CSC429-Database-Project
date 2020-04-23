package userinterface;

import impresario.IModel;

public class ShiftDataView extends View {
    public ShiftDataView(IModel model) {
        super(model, "ShiftDataView");

        setTitle("Open a Shift");

        addContent("Companion",
                makeField("Companion Name",50,"alphabetic"),
                makeField("Companion Hours","numeric"));

        addContent("Ending Time",
                makeField("Ending Time",12));
    }

    @Override
    public void updateState(String key, Object value) {

    }
}