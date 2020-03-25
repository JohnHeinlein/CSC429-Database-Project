package userinterface;

import impresario.IModel;

public class ScoutUpdateDeleteView extends View {

    public ScoutUpdateDeleteView(IModel model) {
        super(model, "ScoutUpdateDeleteView");

        setTitle("Update or Delete a Scout");

        addContent("Name",
                makeField("First Name"),
                makeField("Last Name"));

        addContent("Email",
                makeField("Email"));

        //miscButton("Update", "ScoutUpdateSubmit", null);
        footButt(makeButt("Update", e->{
            scrapeFields();
            myModel.stateChangeRequest("ScoutUpdateSubmit",props);
        }));

        footButt(makeButt("Delete",e->{
            scrapeFields();
            myModel.stateChangeRequest("ScoutDeleteSubmit",props);
        }));
        cancelButton();
    }



    @Override
    public void updateState(String key, Object value) {

    }
}
