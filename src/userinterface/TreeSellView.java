package userinterface;

import database.JDBCBroker;
import database.Persistable;
import impresario.IModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Tree;
import model.TreeType;

import java.sql.Connection;

public class treeAddView extends View{
    private final int TREE_NOTES_LEN = 100;

    public treeAddView(IModel model) {
        super(model, "treeAddView");

        // Tree is created for obvious reasons.
        // TreeType is created in order to check the provided Barcode against a type.
        Tree tree = new Tree();
        //TreeType treeType = new TreeType();

        setTitle("Add a tree");

        // Listener handles creating
        VBox barcodeEntryBox = makeField("Enter barcode");
        TextField barcodeField = (TextField)barcodeEntryBox.getChildren().get(0);
        barcodeField.textProperty().addListener(e -> {
            switch(barcodeField.getText().substring(0,1)){
                default:break;
            };
        });
        addContent("Barcode",
                barcodeEntryBox);

        VBox treeTypeBox = makeField("(Tree Type Auto selected by barcode)");
        TextField treeTypeField = (TextField)treeTypeBox.getChildren().get(0);
        treeTypeField.setEditable(false);
        addContent("Tree Type",
                treeTypeBox);

        addContent("Notes",
                makeNotesField("Notes",TREE_NOTES_LEN));

        addContent("Status",
                makeComboBox("Available","Unavailable"));
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
