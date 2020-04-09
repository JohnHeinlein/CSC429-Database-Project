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

        addContent("Name",
                makeField("First name", 25,"alphabetic"),
                makeField("Middle name", 25,"alphabetic"),
                makeField("Last name", 25,"alphabetic"));

        addContent("Date of Birth",
                makeDatePicker());

        addContent("Phone Number",
                makeField("Phone Number", "phone"));

        addContent("Email",
                makeField("Email",30,"email"));

        addContent("Troop ID",
                makeField("Troop ID",9,9,"numeric"));

        submitButton();
        cancelButton();
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