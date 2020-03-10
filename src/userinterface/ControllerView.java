package userinterface;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static java.lang.Integer.MAX_VALUE;

public class ControllerView extends View{
    public ControllerView(IModel model) {
        super(model, "ControllerView");

        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(15,30,15,30));
//        grid.setPadding(new Insets(15,55,15,55));
//        grid.setAlignment(Pos.CENTER);
//        grid.setVgap(10);
//        grid.setHgap(10);

        Label label = new Label("TREE SALES SYSTEM");
        HBox labelBox = new HBox(label);
        labelBox.setAlignment(Pos.CENTER);

        /** Buttons */
        //Shift buttons
        Button shiftOpen = new Button("Open a shift");
        shiftOpen.setOnAction(e -> model.stateChangeRequest("shiftOpen",null));

        Button shiftClose = new Button("Close a shift");
        shiftClose.setOnAction(e -> model.stateChangeRequest("shiftClose",null));

        HBox shiftButtons = new HBox(shiftOpen,shiftClose);

        //Sale button
        Button treeSell = new Button("Sell a tree");
        treeSell.setOnAction(e -> model.stateChangeRequest("treeSell",null));
        treeSell.setPrefWidth(300);

        HBox sellButton = new HBox(treeSell);

        //Tree type buttons
        Button treeTypeUpdate = new Button("Update a tree type");
        treeTypeUpdate.setOnAction(e -> model.stateChangeRequest("treeTypeUpdate",null));

        Button treeTypeAdd = new Button("Add a tree type");
        treeTypeAdd.setOnAction(e -> model.stateChangeRequest("treeTypeAdd",null));
        HBox treeTypeButtons = new HBox(treeTypeUpdate,treeTypeAdd);

        //Tree update button
        Button treeUpdate = new Button("Update/Delete a Tree");
        treeUpdate.setOnAction(e -> model.stateChangeRequest("treeUpdate",null));
        HBox treeUpdateButton = new HBox(treeUpdate);

        //Scout buttons
        Button scoutUpdate = new Button("Update/Delete a scout");
        scoutUpdate.setOnAction(e -> model.stateChangeRequest("scoutUpdate",null));

        Button scoutRegister= new Button("Register a scout");
        scoutRegister.setOnAction(e -> model.stateChangeRequest("scoutRegister",null));

        HBox scoutButtons = new HBox(scoutUpdate,scoutRegister);

        //Final setup
        vbox.getChildren().add(labelBox);
        for(HBox box : new HBox[] {sellButton,treeTypeButtons,treeUpdateButton,scoutButtons}){
            box.setAlignment(Pos.CENTER);
            box.setSpacing(5);
            vbox.getChildren().addAll(new Separator(Orientation.HORIZONTAL), box);
        }

        getChildren().add(vbox);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
