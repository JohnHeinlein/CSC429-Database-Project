package userinterface;

import impresario.IModel;

public class scoutRegisterView extends GenericView{
    public scoutRegisterView(IModel model) {
        super(model, "scoutRegisterView");

        setTitle("Register a scout");
        addContent("Fuck you",
                makeButt("Big penis", e -> System.out.println("OH LAWD")));

        addContent("Notes",
                makeNotesField("Notes", 100));

        addContent("Text Field",
                makeField("boobies!"));

        addContent("Combo example",
                makeComboBox("foo", "bar", "foobar"));

        cancelButton();
    }
    @Override
    public void updateState(String key, Object value) {

    }
}
