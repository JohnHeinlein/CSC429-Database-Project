package userinterface;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;

public class GenericView extends View{
    private BorderPane container;
    private VBox header;
    private HBox footer;
    private GridPane content;

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
        content.setHgap(10);

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
        titleLabel.setFont(new Font("Arial",18));

        header.getChildren().add(new Separator());
        header.getChildren().add(titleLabel);

    }

    /**
     * Given Adds a control and its name to the content GridPane.
     * Names will be right-aligned, and controls will be sized appropriately
     *  based on their type.
     * @param name  Name of section to appear alongside Nodes
     * @param control Node to apear in section. Can be
     */
    public void addContent(String name, Node control){
        //TODO: Populate with expected datatypes
        //TODO: Classes can be evaluated to their simple names and used in switch
        if(control instanceof Button){
            Button butt = (Button)control;

        }
    }
    /**
     * Populates the GridPane with whatever content has been generated added via addContent
     */
    public void populateContent(){

    }

    public HBox footer(){ return footer; }
    public VBox header(){ return header; }
    public GridPane content(){return content;}


    @Override
    public void updateState(String key, Object value) {

    }
}
