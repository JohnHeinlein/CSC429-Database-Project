package userinterface;

import impresario.IModel;

public class ShiftCloseView extends View{
    public ShiftCloseView(IModel model) {
        super(model, "ShiftCloseView");

        setTitle("Close a Shift");

        addContent("",
                makeScrollPane("", ""));

        submitButton("ShiftCloseSubmit");
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
