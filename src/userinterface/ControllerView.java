package userinterface;

import impresario.IModel;

public class ControllerView extends View{
    public ControllerView(IModel model) {
        super(model, "ControllerView");

        addContent("Shift",
                makeButt("Open","ShiftOpen",null),
                makeButt("Close","ShiftClose",null));

        addContent("Sales",
                makeButt("Sell","TreeSell",null));

        addContent("Tree Type",
                makeButt("Update", "TreeTypeUpdate",null),
                makeButt("Add","TreeTypeAdd",null));

        addContent("Tree",
                makeButt("Update/Delete","TreeUpdate",null),
                makeButt( "Add", "TreeAdd", null));

        addContent("Scout",
                makeButt("Update/Delete","ScoutUpdate",null),
                makeButt("Register","ScoutRegister",null));


        // --------------------
        // Final configuration

        setTitle("Tree Sales System");

    }

    @Override
    public void updateState(String key, Object value) {

    }
}
