package userinterface;

import impresario.IModel;
import utilities.Debug;

public class ShiftOpenView extends View {
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
    public void submit() {
        if (scrapeFields()) {
            myModel.stateChangeRequest("SessionCreate", props);
        } else {
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
