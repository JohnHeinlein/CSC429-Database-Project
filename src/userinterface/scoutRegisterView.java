package userinterface;

import impresario.IModel;

public class scoutRegisterView extends View{
    public scoutRegisterView(IModel model) {
        super(model, "scoutRegisterView");

        setTitle("Register a scout");

        addContent("Name",
                makeField("First Name"),
                makeField("Last Name"));

        addContent("Address",
                makeField("Address"));
        addContent("",
                makeField("City"),
                makeField("State"),
                makeField("Zip"));

        addContent("Date of Birth",
                makeDatePicker());

        addContent("Phone Number",
                makeField("Phone Number"));

        addContent("Email",
                makeField("Email Address"));

        addContent("Troop ID",
                makeField("Troop ID"));

        addContent("Status",
                makeComboBox("Active", "Inactive"));

        footButt(makeButt("Submit", "scoutRegisterSubmit", null));
        cancelButton();
    }
    @Override
    public void updateState(String key, Object value) {

    }
}
