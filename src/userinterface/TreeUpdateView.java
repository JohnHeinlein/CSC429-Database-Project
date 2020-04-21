package userinterface;

import impresario.IModel;
import javafx.scene.control.ComboBox;
import model.Tree;
import utilities.Debug;

import java.util.Map;

public class TreeUpdateView extends View {

    private Tree tree;

    public TreeUpdateView(IModel model) {
        super(model, "TreeUpdateView");

        tree = (Tree) myModel.getState("Tree");

        // Add a title for this panel
        setTitle("Update Tree Data");

        // create our GUI components, add them to this Container
        TextFieldWrapper treeType = makeField("Tree Type", 11);
        treeType.setText((String) tree.getState("treeType"));
        addContent("Tree Type",
                treeType);

        NotesFieldWrapper notes = makeNotesField("Notes", 200);
        treeType.setText((String) tree.getState("notes"));
        addContent("Notes",
                notes);

        ComboBox<String> status = makeComboBox("Available", "Sold", "Damaged");
        status.getSelectionModel().select((String) tree.getState("status"));
        addContent("Status",
                status);

        submitButton();
        cancelButton();

        for (Map.Entry<String, Object> entry : controlList.entrySet()) {
            setValue(entry.getValue(), (String) tree.getState(entry.getKey()));
        }
        //myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    public void submit() {
        if (scrapeFields()) {
            myModel.stateChangeRequest("TreeUpdateSubmit", props);
        } else {
            Debug.logErr("Failed submission: scrapeFields failed");
        }
    }

    public void updateState(String key, Object value) {
        // STEP 6: Be sure to finish the end of the 'perturbation'
        // by indicating how the view state gets updated.
        if (key.equals("InsertTree")) {

        }
    }
}
