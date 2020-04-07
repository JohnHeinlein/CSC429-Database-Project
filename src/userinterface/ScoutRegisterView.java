package userinterface;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;


public class ScoutRegisterView extends View {
    public ScoutRegisterView(IModel scout){
        super(scout, "ScoutRegisterView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        setTitle("Insert Scout Data");

        // create our GUI components, add them to this Container
        addContent("Name",
                makeField("First name",  25),
                makeField("Middle name", 25),
                makeField("Last name",   25));

        addContent("Date of Birth",
                makeDatePicker());

        addContent("Phone Number",
                makeField("Phone Number",30));

        addContent("Email",
                makeField("Email",30));

        addContent("Troop ID",
                makeField("Troop ID",10));

//        addContent("Status",
//                makeComboBox("Active","Inactive"));

        submitButton();
        cancelButton();
        //myModel.subscribe("UpdateStatusMessage", this);
    }

//    @Override
//    public void submit(){
//        if(scrapeFields()) {
//            myModel.stateChangeRequest("ScoutRegisterSubmit", props);
//        }else{
//            Debug.logErr("Failed submission: scrapeFields failed");
//        }
//    }

    public void updateState(String key, Object value) {
        if (key.equals("InsertScout")) {

        }
    }
}