// specify the package
package userinterface;

// system imports

import impresario.IModel;
import javafx.scene.control.Control;
import model.Scout;
import utilities.Debug;

// project imports

public class ScoutUpdateView extends View {
    private Scout scout;

    public ScoutUpdateView(IModel myModel) {
        super(myModel, "ScoutRegisterView");
        scout = (Scout)myModel.getState("Scout");

        // Add a title for this panel
        setTitle("Insert Scout Data");

        // create our GUI components, add them to this Container
        addContent("First Name",
                makeField("First name",25),
                makeField("Middle name",25),
                makeField("Last name",25));

        addContent("Date of Birth",
                makeDatePicker());

        addContent("Phone Number",
                makeField("Phone Number",30));

        addContent("Email",
                makeField("Email",30));

        addContent("Troop ID",
                makeField("Troop ID",10));

        addContent("Status",
                makeComboBox("Active","Inactive"));

        submitButton();
        cancelButton();

        for(String field : controlList.keySet()){
            setValue((Control)controlList.get(field), (String)scout.getState(field));
        }
        //myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    public void submit(){
        if(scrapeFields()) {
            myModel.stateChangeRequest("ScoutUpdateSubmit", props);
        }else{
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }
    public void updateState(String key, Object value) {
        // STEP 6: Be sure to finish the end of the 'perturbation'
        // by indicating how the view state gets updated.
        if (key.equals("InsertScout")) {

        }

    }
}