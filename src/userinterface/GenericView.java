package userinterface;

import impresario.IModel;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class GenericView extends View{
    private BorderPane container;
    private VBox header;
    private HBox footer;
    private GridPane content;
    private final Font LABEL_FONT = new Font("Arial",18);
    private final double FIELD_WIDTH = 300.0;

    public GenericView(IModel model, String classname){
        super(model, classname);

        container = new BorderPane();
        header = new VBox();
        footer = new HBox();
        content = new GridPane();

        //Header
        header.setAlignment(Pos.CENTER);
        header.setSpacing(25);

        //Footer
        footer.setAlignment(Pos.CENTER);
        footer.setSpacing(25);

        //GridPane
        content.setPadding(new Insets(15,15,15,15));
        content.setVgap(10);
        content.setHgap(5);

        // This can be implemented and tidied with CSS to get a better separator appearance
        // content.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        container.setTop(header);
        container.setCenter(content);
        container.setBottom(footer);
        getChildren().add(container);
    }

    /**
     * Adds a cancel button to return to ControllerView
     */
    public void cancelButton(){
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Cancel",null));
        footer.getChildren().add(
                footer.getChildren().size(), //Add to rightmost side
                cancelButton);
    }

    /**
     * Adds button to leftmost of footer
     **/
    public void addButton(Button butt){
        footer.getChildren().add(0,butt);
    }

    /**
     * Adds a title label and separator to the header
     * @param title Text for title of window
     */
    public void setTitle(String title){
        Label titleLabel = new Label(title);
        titleLabel.setFont(LABEL_FONT);

        header.getChildren().add(new Separator());
        header.getChildren().add(titleLabel);

    }

    /**
     * Given Adds a control and its name to the content GridPane.
     * Names will be right-aligned, and controls will be sized appropriately
     *  based on their type.
     * @param name  Name of section to appear alongside Nodes
     * @param controls Node(s) appearing in section
     */
    public void addContent(String name, Control... controls){
        // Label
        Label label = new Label(name);
        label.setFont(LABEL_FONT);

        // Controls/Fields
        HBox controlBox = new HBox();
        for(Control control : controls){
            control.setPrefWidth(FIELD_WIDTH/controls.length); //Scale width to fill space
            controlBox.getChildren().add(control);
        }

        content.addColumn(0,label);
        content.setHalignment(label, HPos.RIGHT);
        content.addColumn(1,controlBox);
    }

    public Button makeButt(String text, String state, Object prop){
        Button butt = new Button(text);
        butt.setOnAction(e -> myModel.stateChangeRequest(state,prop));
        return butt;
    }
    public HBox footer(){ return footer; }
    public VBox header(){ return header; }
    public GridPane content(){return content;}


    @Override
    public void updateState(String key, Object value) {

    }
}
