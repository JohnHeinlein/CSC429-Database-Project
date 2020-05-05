package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.scene.control.ButtonType;
import model.ScoutTableModel;
import model.Tree;
import utilities.Alerts;
import utilities.Debug;

import java.util.Optional;

public class TreeUpdateDeleteView extends View {

    protected ScoutTableModel selection; //Selected scout

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));

        footButt(makeButt("Update", e -> {
            Tree tree = getTree();

            if (tree != null) {
                myModel.stateChangeRequest("TreeUpdate", tree);
            }
        }));

        // TODO: Currently says it deletes the tree, but doesn't actually.
        // TODO: THIS IS A LIE IM 95% POSITIVE
        footButt(makeButt("Delete", e -> {
            Tree tree = getTree();

            if (tree != null) {
                Optional<ButtonType> confirmation = Alerts.confirmMessage("Confirm delete of tree " + tree.getState("barcode") + "?");

                if (confirmation.get() == ButtonType.OK) {
                    myModel.stateChangeRequest("TreeDelete", tree);
                }
            }
        }));

        cancelButton();
    }

    public Tree getTree() {
        scrapeFields();

        String barcode = (String) props.get("barcode");

        if (barcode == null) {
            Debug.logErr("No barcode retrieved");
            return null;
        }
        try {
            return new Tree(barcode);
        } catch (InvalidPrimaryKeyException ex) {
            Debug.logErr(String.format("(%s) Invalid barcode", barcode));
            Alerts.errorMessage("No barcode matching " + barcode + " found.");
        }
        return null;
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
