package userinterface;

import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * Handles the construction of Views
 * Allows easy access to methods adhering to the pattern present in the State Diagram Project Artifact
 */
public class GenericView extends View{
    private BorderPane container;
    private VBox header;
    private HBox footer;
    private GridPane content;

    private final String DEFAULT_FONT = "Comic Sans";
    private final Font LABEL_FONT = new Font(DEFAULT_FONT,18);
    private final double FIELD_WIDTH = 300.0;

    public GenericView(IModel model, String classname){
        super(model, classname);

        container = new BorderPane();
        header = new VBox();
        footer = new HBox();
        content = new GridPane();

        //Header
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(5,5,5,5));

        //Footer
        footer.setAlignment(Pos.CENTER);
        footer.setSpacing(25);

        //GridPane
        content.setPadding(new Insets(5,15,15,15));
        content.setVgap(10);
        content.setHgap(5);
        System.out.println(content.styleProperty());

        container.setTop(header);
        container.setCenter(content);
        container.setBottom(footer);
        getChildren().add(container);
    }

    // ***************
    // Header methods
    /**
     * Adds a title label to the header
     * @param title Text for title of window
     */
    public void setTitle(String title){
        Label titleLabel = new Label(title);
        titleLabel.setFont(LABEL_FONT);

        header.getChildren().add(titleLabel);
        Separator sep = new Separator();
        sep.setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: gray");
        header.getChildren().add(sep);
    }

    // ***************
    // Content methods
    /**
     * Given Adds a control and its name to the content GridPane.
     * Names will be right-aligned, and controls will be sized appropriately
     *  based on their type.
     * @param name  Name of section to appear alongside Nodes
     * @param controls Node(s) appearing in section
     */
    public void addContent(String name, Region... controls){
        // Label
        Label label = new Label(name);
        label.setFont(LABEL_FONT);

        // Controls/Fields
        HBox controlBox = new HBox();
        for(Region control : controls){
            control.setPrefWidth(FIELD_WIDTH/controls.length); //Scale width to fill space
            controlBox.getChildren().add(control);
        }

        content.addColumn(0,label);
        GridPane.setHalignment(label, HPos.RIGHT);
        content.addColumn(1,controlBox);
    }

    // ***************
    // Footer methods
    /** Adds a cancel button to rightmost of footer to return to ControllerView */
    public void cancelButton(){
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> myModel.stateChangeRequest("Cancel",null));
        footer.getChildren().add(
                footer.getChildren().size(), //Add to rightmost side
                cancelButton);
    }

    /** Adds button to leftmost of footer */
    public void footButt(Button butt){ footer.getChildren().add(0,butt); }

    // ***************
    // Control creation
    public Button makeButt(String text, String state, Object prop){
        return makeButt(text, e -> myModel.stateChangeRequest(state, prop));
    }
    public Button makeButt(String text, EventHandler<ActionEvent> event){
        Button butt = new Button(text);
        butt.setOnAction(event);
        return butt;
    }

    public TextField makeField(String prompt){
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }
    public VBox makeNotesField(String prompt, int maxLength){
        VBox box = new VBox();
        Label count = new Label("0/"+maxLength);

        TextArea field = new TextArea();
        field.setPromptText(prompt);
        field.setPrefColumnCount(80);
        field.setPrefRowCount((int)Math.ceil(maxLength / 80.0));
        field.setWrapText(true);
        field.setFont(new Font(DEFAULT_FONT,12));

        field.textProperty().addListener((observableValue, s, t1) -> {
            if(field.getText().length() >= maxLength)
                field.setText(field.getText().substring(0,maxLength));
            count.setText(field.getText().length() + "/" + maxLength);
        });

        box.getChildren().addAll(field,count);
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    // Give any number of string options. The first will be the default.
    public ComboBox<String> makeComboBox(String... choices){
      ComboBox<String> combo = new ComboBox<>();
      combo.getItems().addAll(choices);
      combo.getSelectionModel().selectFirst();

      return combo;
    }

    // ***************
    // Public-facing region getters
    public HBox footer(){ return footer; }
    public VBox header(){ return header; }
    public GridPane content(){return content;}

    @Override
    public void updateState(String key, Object value) { }
}