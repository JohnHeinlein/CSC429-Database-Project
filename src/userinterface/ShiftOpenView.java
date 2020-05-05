package userinterface;

import impresario.IModel;
import utilities.Alerts;
import utilities.Debug;

public class ShiftOpenView extends View {
    public ShiftOpenView(IModel model) {
        super(model, "ShiftOpenView");

        setTitle("Open a Shift");

        addContent("Times",
                makeField("Start Time", 12),
                makeField("End Time", 12));

        addContent("Starting Cash",
                makeField("Starting Cash", 15));

        addContent("Notes",
                makeNotesField("Notes", 500));

        submitButton();
        cancelButton();
    }

    @Override
    public void submit() {
        if (scrapeFields()) {
            if (Double.parseDouble(props.getProperty("startingCash")) <= 0) {
                Alerts.errorMessage("Starting cash must be > 0");
            } else {
                myModel.stateChangeRequest("SessionCreate", props);
            }
        } else {
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
