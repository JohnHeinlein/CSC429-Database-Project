package userinterface;

import impresario.IModel;
import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.Properties;

public class ScoutUpdateDeleteView extends View {

    public ScoutUpdateDeleteView(IModel model) {
        super(model, "ScoutUpdateDeleteView");
        setTitle("Update or Delete a Scout");

        addContent("First name",
                makeField("First Name"),
                makeButt("Search", e ->{
                    scrapeFieldsUnsafe();
                    myModel.stateChangeRequest(
                            "ScoutSearch",
                            new Pair<String,String>("firstName",(String)props.get("firstName")));
                }));

        addContent("Last name",
                makeField("Last Name"),
                makeButt("Search", e ->{
                    scrapeFieldsUnsafe();
                    myModel.stateChangeRequest(
                            "ScoutSearch",
                            new Pair<String,String>("lastName",(String)props.get("lastName")));
                }));

        addContent("Email",
                makeField("Email"),
                makeButt("Search", e ->{
                    scrapeFieldsUnsafe();
                    myModel.stateChangeRequest(
                            "ScoutSearch",
                            new Pair<String,String>("email",(String)props.get("email")));
                }));
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
