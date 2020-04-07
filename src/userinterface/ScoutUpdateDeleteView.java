package userinterface;

import impresario.IModel;
import javafx.util.Pair;

public class ScoutUpdateDeleteView extends View {

    public ScoutUpdateDeleteView(IModel model) {
        super(model, "ScoutUpdateDeleteView");
        setTitle("Update or Delete a Scout");

        addContent("First name",
                makeField("First Name",25,"alphabetic"),
                makeButt("Search", e ->{
                    scrapeFieldsUnsafe();
                    myModel.stateChangeRequest(
                            "ScoutSearch",
                            new Pair<String,String>("firstName",(String)props.get("firstName")));
                }));

        addContent("Last name",
                makeField("Last Name",25,"alphabetic"),
                makeButt("Search", e ->{
                    scrapeFieldsUnsafe();
                    myModel.stateChangeRequest(
                            "ScoutSearch",
                            new Pair<String,String>("lastName",(String)props.get("lastName")));
                }));

        addContent("Email",
                makeField("Email",25,"email"),
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
