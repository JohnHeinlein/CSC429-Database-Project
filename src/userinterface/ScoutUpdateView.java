// specify the package
package userinterface;

// system imports
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


// project imports
import impresario.IModel;
import model.Scout;
import utilities.Debug;

public class ScoutUpdateView extends View {
    private Scout scout;

    public ScoutUpdateView(IModel myModel) {
        super(myModel, "ScoutRegisterView");
        scout = (Scout)myModel.getState("Scout");

        // Add a title for this panel
        setTitle("Insert Scout Data");

        // create our GUI components, add them to this Container
        addContent("First Name",
                makeField("First name"),
                makeField("Middle name"),
                makeField("Last name"));

        addContent("Date of Birth",
                makeDatePicker());

        addContent("Phone Number",
                makeField("Phone Number"));

        addContent("Email",
                makeField("Email"));

        addContent("Troop ID",
                makeField("Troop ID"));

        addContent("Status",
                makeComboBox("Active","Inactive"));

        submitButton();
        cancelButton();

        for(String field : controlList.keySet()){
            setValue(controlList.get(field), (String)scout.getState(field));
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