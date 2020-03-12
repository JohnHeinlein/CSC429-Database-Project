// tabs=4
//************************************************************
//	COPYRIGHT 2009/2015 Sandeep Mitra and Michael Steves, The
//    College at Brockport, State University of New York. - 
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot 
// be reproduced, copied, or used in any shape or form without 
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package userinterface;

import impresario.ControlRegistry;
import impresario.IControl;
import impresario.IModel;
import impresario.IView;
import Utilities.Utilities;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//==============================================================
public abstract class View
        extends Group
        implements IView, IControl {
    protected IModel myModel;
    protected ControlRegistry myRegistry;

    private BorderPane container;
    private VBox header;
    private HBox footer;
    private GridPane content;

    private final String DEFAULT_FONT = "Comic Sans";
    private final Font LABEL_FONT = new Font(DEFAULT_FONT, 18);
    private final double FIELD_WIDTH = 300.0;

    // Class constructor
    //----------------------------------------------------------
    public View(IModel model, String classname) {
        myModel = model;
        myRegistry = new ControlRegistry(classname);

        container = new BorderPane();
        header = new VBox();
        footer = new HBox();
        content = new GridPane();

        //Header
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(5, 5, 5, 5));

        //Footer
        footer.setAlignment(Pos.CENTER);
        footer.setSpacing(25);

        //GridPane
        content.setPadding(new Insets(5, 15, 15, 15));
        content.setVgap(10);
        content.setHgap(5);

        container.setTop(header);
        container.setCenter(content);
        container.setBottom(footer);
        getChildren().add(container);
    }

    // ***************
    // Header methods

    /**
     * Adds a title label to the header
     *
     * @param title Text for title of window
     */
    public void setTitle(String title) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(LABEL_FONT);

        header.getChildren().add(titleLabel);
        Separator sep = new Separator();
        sep.setStyle("-fx-border-width: 2 0 0 0; -fx-border-color: gray");
        header.getChildren().add(sep);
    }

    // ***************
    // Content methods
    // ***************

    /**
     * Given Adds a control and its name to the content GridPane.
     * Names will be right-aligned, and controls will be sized to fit width.
     *
     * @param name     Name of section to appear alongside Nodes
     * @param controls Node(s) appearing in section. Region is source of setPrefWidth
     */
    public void addContent(String name, Region ... controls) {
        // Label
        Label label = new Label(name);
        label.setFont(LABEL_FONT);

        // Controls/Fields
        HBox controlBox = new HBox();
        for (Region control : controls) {
            control.setPrefWidth(FIELD_WIDTH / controls.length); //Scale width to fill space
            controlBox.getChildren().add(control);
        }

        content.addColumn(0, label);
        GridPane.setHalignment(label, HPos.RIGHT);
        content.addColumn(1, controlBox);
    }

    public void addContent(String name, Pane pane){
        Label label = new Label(name);
        label.setFont(LABEL_FONT);

        pane.setPrefWidth(FIELD_WIDTH);

        content.addColumn(0, label);
        GridPane.setHalignment(label, HPos.RIGHT);
        content.addColumn(1, pane);
    }

    // ***************
    // Footer methods
    // ***************

    /**
     * Adds a cancel button to rightmost of footer to return to ControllerView
     */
    public void cancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            myModel.stateChangeRequest("Cancel", null);
            clear();
        });
        footer.getChildren().add(
                footer.getChildren().size(), //Add to rightmost side
                cancelButton);

    }

    private void clear() {
        for (Node box : content.getChildren()) {

            // If the node is an HBox or a VBox in the right column...
            if ((box instanceof HBox || box instanceof VBox)
                 && GridPane.getColumnIndex(box) == 1)
            {
                // For every node contained in that box...
                for(Node node : ((Pane) box).getChildren()) {
                    // Cast the node to Region (ComboBox, etc.)
                    Region control = (Region)node;

                    // Clear text input fields
                    if (control instanceof TextInputControl) {
                        System.out.println("\t\tclearing text input");
                        ((TextInputControl) control).clear();
                    }
                    // Set combobox to default value
                    else if (control instanceof ComboBox) {
                        System.out.println("\t\tclearing combo box");
                        ((ComboBox) control)
                                .getSelectionModel()
                                .selectFirst();
                    }
                    // Clear datepicker
                    else if (control instanceof DatePicker) {
                        System.out.println("\t\tclearing text input");
                        ((DatePicker) control).setValue(null);
                    }
                }
            }
        }
    }

    /**
     * Adds button to leftmost of footer
     */
    public void footButt(Button butt) {
        footer.getChildren().add(0, butt);
    }

    // ***************
    // Control creation
    // ***************
    public Button makeButt(String text, String state, Object prop) {
        return makeButt(text, e -> myModel.stateChangeRequest(state, prop));
    }

    public Button makeButt(String text, EventHandler<ActionEvent> event) {
        Button butt = new Button(text);
        butt.setOnAction(event);
        return butt;
    }

    public TextField makeField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    public VBox makeNotesField(String prompt, int maxLength) {
        VBox box = new VBox();
        Label count = new Label("0/" + maxLength);

        TextArea field = new TextArea();
        field.setPromptText(prompt);
        field.setPrefColumnCount(80);
        field.setPrefRowCount((int) Math.ceil(maxLength / 80.0));
        field.setWrapText(true);
        field.setFont(new Font(DEFAULT_FONT, 12));

        field.textProperty().addListener((observableValue, s, t1) -> {
            if (field.getText().length() >= maxLength)
                field.setText(field.getText().substring(0, maxLength));
            count.setText(field.getText().length() + "/" + maxLength);
        });

        box.getChildren().addAll(field, count);
        box.setAlignment(Pos.CENTER_LEFT);

        return box;
    }

    // Give any number of string options. The first will be the default.
    public ComboBox<String> makeComboBox(String ... choices){
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(choices);
        combo.getSelectionModel().selectFirst();

        return combo;
    }

    public DatePicker makeDatePicker() {
        DatePicker picker = new DatePicker();

        picker.setConverter(new StringConverter<>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }

                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }

                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

        picker.setOnAction(e -> {
            LocalDate date = picker.getValue();
            Utilities.logErr("Selected date: " + date);
        });
        return picker;
    }

    // ***************
    // Public-facing region getters
    // ***************
    public HBox footer() {
        return footer;
    }

    public VBox header() {
        return header;
    }

    public GridPane content() {
        return content;
    }

    // ***************
    // Model methods
    // ***************
    public void setRegistry(ControlRegistry registry) {
        myRegistry = registry;
    }

    // Allow models to register for state updaytes
    public void subscribe(String key, IModel subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    // Allow models to unregister for state updates
    public void unSubscribe(String key, IModel subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }
}

