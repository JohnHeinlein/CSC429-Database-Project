package userinterface;

import impresario.IModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashMap;

public class ControllerView extends GenericView{
    public ControllerView(IModel model) {
        super(model, "ControllerView");

        GridPane grid = content();

        /** UI Elements */
        String[] sections = {"Scout","Tree","TreeType","Shift","Sales"};
        HashMap<String,Label> labels = new HashMap<String,Label>();
        HashMap<String,HBox> buttons = new HashMap<String,HBox>();

        // --------------------
        // Shift section
        Button shiftOpen = new Button("Open");
        shiftOpen.setOnAction(e -> model.stateChangeRequest("shiftOpen",null));
        shiftOpen.setPrefWidth(100);

        Button shiftClose = new Button("Close");
        shiftClose.setOnAction(e -> model.stateChangeRequest("shiftClose",null));
        shiftClose.setPrefWidth(100);

        HBox shiftButtons = new HBox(shiftOpen,shiftClose);
        buttons.put("Shift",shiftButtons);

        // --------------------
        // Sale section
        Button treeSell = new Button("Sell");
        treeSell.setOnAction(e -> model.stateChangeRequest("treeSell",null));

        HBox sellButton = new HBox(treeSell);
        buttons.put("Sales",sellButton);

        // --------------------
        // Tree type section
        Button treeTypeUpdate = new Button("Update");
        treeTypeUpdate.setOnAction(e -> model.stateChangeRequest("treeTypeUpdate",null));

        Button treeTypeAdd = new Button("Add");
        treeTypeAdd.setOnAction(e -> model.stateChangeRequest("treeTypeAdd",null));

        HBox treeTypeButtons = new HBox(treeTypeUpdate,treeTypeAdd);
        buttons.put("TreeType",treeTypeButtons);

        // --------------------
        // Tree update section
        Button treeUpdate = new Button("Update/Delete");
        treeUpdate.setOnAction(e -> model.stateChangeRequest("treeUpdate",null));

        HBox treeUpdateButton = new HBox(treeUpdate);
        buttons.put("Tree",treeUpdateButton);

        // --------------------
        // Scout section
        Button scoutUpdate = new Button("Update/Delete");
        scoutUpdate.setOnAction(e -> model.stateChangeRequest("scoutUpdate",null));

        Button scoutRegister= new Button("Register");
        scoutRegister.setOnAction(e -> model.stateChangeRequest("scoutRegister",null));

        HBox scoutButtons = new HBox(scoutUpdate,scoutRegister);
        buttons.put("Scout",scoutButtons);

        /** Content configuration */
        //TODO: Port this pattern to GenericView
        for(int i = 0; i < sections.length;i++){
            // --------------------
            // Buttons
            HBox box = buttons.get(sections[i]);
            box.setAlignment(Pos.CENTER);
            box.setSpacing(5);
            //Fill buttons to width of HBox
            box.getChildren().forEach(b -> ((Button)b).setPrefWidth(300.0 / box.getChildren().size()));

            grid.add(box,1,i);

            // --------------------
            // Labels
            Label newLabel = new Label(sections[i]);
            newLabel.setAlignment(Pos.CENTER_RIGHT);
            newLabel.setFont(new Font("Arial",18));

            labels.put(sections[i],newLabel);
            grid.add(newLabel,0,i);
            grid.setHalignment(newLabel, HPos.RIGHT);
        }

        setTitle("Tree Sales System");

    }

    @Override
    public void updateState(String key, Object value) {

    }
}
