package userinterface;

import impresario.IModel;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ControllerView extends GenericView{
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
                makeButt("Update/Delete","treeUpdate",null));

        addContent("Scout",
                makeButt("Update/Delete","scoutUpdate",null),
                makeButt("RegisteR","scoutRegister",null));
        // --------------------
        // Final configuration
        setTitle("Tree Sales System");

    }

    @Override
    public void updateState(String key, Object value) {

    }
}
