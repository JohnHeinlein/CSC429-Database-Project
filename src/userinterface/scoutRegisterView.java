package userinterface;

import impresario.IModel;

public class scoutRegisterView extends View{
    public scoutRegisterView(IModel model) {
        super(model, "scoutRegisterView");

        setTitle("Register a scout");
    }
    @Override
    public void updateState(String key, Object value) {

    }
}
