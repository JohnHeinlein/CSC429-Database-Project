// specify the package
package userinterface;

// system imports
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;


// project imports
import impresario.IModel;
import utilities.Debug;

public class ScoutRegisterView extends View {
    public ScoutRegisterView(IModel scout) {
        super(scout, "ScoutRegisterView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

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
        myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    public void submit(){
        if(scrapeFields()) {
            myModel.stateChangeRequest("ScoutRegisterSubmit", props);
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