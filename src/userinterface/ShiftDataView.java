package userinterface;

import impresario.IModel;
import model.Session;
import model.Shift;
import userinterface.View;
import utilities.Debug;

public class ShiftDataView extends View {
    public ShiftDataView(IModel model) {
        super(model, "ShiftDataView");

        setTitle("Open a Shift");

        addContent("Companion",
                makeField("Companion Name"),
                makeField("Companion Hours"));

        addContent("Ending Time",
                makeField("Ending Time"));

        submitButton();
        cancelButton();
    }
    @Override
    public void submit () {
        if(scrapeFields()) {
            myModel.stateChangeRequest("abc", props);
        }else{
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }
    @Override
    public void updateState(String key, Object value) {

    }
}