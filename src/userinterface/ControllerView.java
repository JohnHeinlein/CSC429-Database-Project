package userinterface;

import impresario.IModel;

public class ControllerView extends View{
    public ControllerView(IModel model) {
        super(model, "ControllerView");

        addContent("Shift",
                makeButt("Open","shiftOpen",null),
                makeButt("Close","shiftClose",null));

        addContent("Sales",
                makeButt("Sell","treeSell",null));

        addContent("Tree Type",
                makeButt("Update", "treeTypeUpdate",null),
                makeButt("Add","treeTypeAdd",null));

        addContent("Tree",
                makeButt("Update/Delete","treeUpdate",null),
                makeButt( "Add", "treeAdd", null));

        addContent("Scout",
                makeButt("Update/Delete","scoutUpdate",null),
                makeButt("Register","scoutRegister",null));


        // --------------------
        // Final configuration
        setTitle("Tree Sales System");

    }

    @Override
    public void updateState(String key, Object value) {

    }
}
