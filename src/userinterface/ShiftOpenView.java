package userinterface;

import impresario.IModel;

public class ShiftOpenView extends View{
    public ShiftOpenView(IModel model) {
        super(model, "ShiftOpenView");

        setTitle("Open a Shift");

        addContent("Times",
                makeField("Start Time"),
                makeField("End Time"));

        addContent("Starting Cash",
                makeField("Starting Cash"));

        submitButton();
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
