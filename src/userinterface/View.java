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
import utilities.Debug;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

public abstract class View extends Group implements IView, IControl {

    protected IModel myModel;
    protected ControlRegistry myRegistry;

    private VBox header;
    private HBox footer;
    private GridPane content;

    private final String DEFAULT_FONT = "Comic Sans";
    private final Font LABEL_FONT = new Font(DEFAULT_FONT, 18);
    private final double FIELD_WIDTH = 300.0;

    private Properties props; //Collects information from input fields for submission
    private HashMap<String, Control> controlList; //Keeps track of what content is a control
    private String viewName; //Debugging purposes

    public View(IModel model, String classname) {
        myModel = model;
        myRegistry = new ControlRegistry(classname);

        BorderPane container = new BorderPane();
        header = new VBox();
        footer = new HBox();
        content = new GridPane();

        controlList = new HashMap<>();
        props = new Properties();
        viewName = classname; //Debugging purposes

        //Header
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(5, 5, 5, 5));

        //Footer
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(0, 5, 5, 5));
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
    // ***************

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

            switch (control.getClass().toString()) {
                case "class javafx.scene.control.ComboBox":
                case "class javafx.scene.control.DatePicker":
                    controlList.put(name, (Control) control);
                    break;
                case "class userinterface.View$TextFieldWrapper":
                    controlList.put(name, ((TextFieldWrapper) control).getField());
                    break;
                case "class userinterface.View$NotesFieldWrapper":
                    controlList.put(name, ((NotesFieldWrapper) control).getField());
                    break;
            }
            props.put(name, "");
        }

        content.addColumn(0, label);
        content.addColumn(1, controlBox);

        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setValignment(label, VPos.TOP);

        Debug.logMsg("(" + viewName + ") Added \"" + name + "\". Controls: " + controlList.size());
    }

    // ***************
    // Footer methods
    // ***************

    /**
     * Adds a misc button to footer to return to ControllerView
     */
    public void miscButton(String name, String state, Object prop) {
        Button miscButton = new Button(name);
        miscButton.setOnAction(e -> {
            myModel.stateChangeRequest(state, prop);
            clear();
        });
        footButt(miscButton);
    }

    /**
     * Adds a submit button to footer to return to ControllerView
     */
    public void submitButton(String state) {
        Button submitButton = makeButt("Submit", e -> {
            for (String field : controlList.keySet()) {
                Control control = controlList.get(field);
                Debug.logMsg("Extracting input from " + control.getClass().toString());
                switch (control.getClass().toString()) {
                    case "class javafx.scene.control.ComboBox":
                        props.put(field, ((ComboBox<String>) control).getValue());
                        break;
                    case "class javafx.scene.control.DatePicker":
                        props.put(field, ((DatePicker) control).getConverter().toString());
                        break;
                    case "class javafx.scene.control.TextField":
                        props.put(field, ((TextField) control).getText());
                        break;
                    case "class javafx.scene.control.TextArea":
                        props.put(field, ((TextArea) control).getText());
                        break;
                }
            }
            Debug.logMsg("\nFields queried:\n\t"
                    + controlList.toString()
                    .replaceAll(",", ",\n\t")
                    .replaceAll("@[^},]*", "") //Exclude memory address (trust me)
                    + "\nProperties retrieved:\n\t"
                    + props.toString().replaceAll(",", ",\n\t"));
            myModel.stateChangeRequest(state, props);
            clear();
        });
        submitButton.setStyle("-fx-background-color: lightgreen");
        footButt(submitButton);
    }

    /**
     * Adds a cancel button to footer to return to ControllerView
     */
    public void cancelButton() {
        Button cancelButton = makeButt("Cancel", e -> {
            myModel.stateChangeRequest("Cancel", null);
            clear();
        });
        cancelButton.setStyle("-fx-background-color: indianred");
        footButt(cancelButton);
    }

    private void clear() {
        for (Node box : content.getChildren()) {

            // If the node is an HBox or a VBox in the right column...
            if ((box instanceof HBox || box instanceof VBox) && GridPane.getColumnIndex(box) == 1) {

                // For every node contained in that box...
                for (Node node : ((Pane) box).getChildren()) {
                    // Cast the node to Region (ComboBox, etc.)
                    if (node instanceof MessageView) {
                        ((MessageView) node).setText("");
                        ((MessageView) node).clearErrorMessage();
                        continue;
                    }

                    Region control = (Region) node;
                    // Clear text input fields
                    if (control instanceof TextInputControl)
                        ((TextInputControl) control).clear();

                        // Set combobox to default value
                    else if (control instanceof ComboBox)
                        ((ComboBox) control).getSelectionModel().selectFirst();

                        // Clear datepicker
                    else if (control instanceof DatePicker)
                        ((DatePicker) control).setValue(null);
                }
            }
        }
    }

    /**
     * Adds button to leftmost of footer
     */
    public void footButt(Button butt) {
        footer.getChildren().add(footer.getChildren().size(), butt);
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

    public TextFieldWrapper makeField(String prompt) {
        return makeField(prompt, true);
    }

    public TextFieldWrapper makeField(String prompt, boolean editable) {
        TextFieldWrapper field = new TextFieldWrapper(prompt);
        field.setEditable(editable);
        field.setDisable(!editable);
        return field;
    }

    protected static class TextFieldWrapper extends VBox {
        private TextField field;
        private MessageView message;

        public TextFieldWrapper(String prompt) {
            field = new TextField();
            field.setPromptText(prompt);

            message = new MessageView("");

            getChildren().addAll(field, message);
            setAlignment(Pos.CENTER_LEFT);
        }

        public void setListener(ChangeListener<? super String> listener) {
            field.textProperty().addListener(listener);
        }

        public TextField getField() {
            return field;
        }

        public void message(String text) {
            message.displayMessage(text);
        }

        public void error(String err) {
            message.displayErrorMessage(err);
        }

        //Pass to field for convenience
        public void setEditable(Boolean flag) {
            field.setEditable(flag);
        }

        public String getText() {
            return field.getText();
        }

        public void clear() {
            message.clearErrorMessage();
            message.setText("");

            field.clear();
        }
    }

    public NotesFieldWrapper makeNotesField(String prompt, int maxLength) {
        return new NotesFieldWrapper(prompt, maxLength);
    }

    protected class NotesFieldWrapper extends VBox {
        private TextArea field;
        private Label label;

        public NotesFieldWrapper(String prompt, int maxLength) {
            label = new Label();

            field = new TextArea();
            field.setPromptText(prompt);
            field.setPrefColumnCount(80);
            field.setPrefRowCount((int) Math.ceil(maxLength / 80.0));
            field.setWrapText(true);
            field.setFont(new Font(DEFAULT_FONT, 12));
            field.textProperty().addListener((observableValue, s, t1) -> {
                if (field.getText().length() >= maxLength)
                    field.setText(field.getText().substring(0, maxLength));
                label.setText(field.getText().length() + "/" + maxLength);
            });


            getChildren().addAll(field, label);
            setAlignment(Pos.CENTER_LEFT);
        }

        public TextArea getField() {
            return field;
        }
    }

    // Give any number of string options. The first will be the default.
    public ComboBox<String> makeComboBox(String... choices) {
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
                return localDate == null ? "" : dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                return (dateString == null || dateString.trim().isEmpty()) ? null : LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

        picker.setOnAction(e -> {
            LocalDate date = picker.getValue();
            Debug.logErr("Selected date: " + date);
        });
        return picker;
    }

    // TODO: Fill the ScrollPane with something.
    public ScrollPane makeScrollPane(String modelState, String collectionState) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(200);
        scrollPane.setFitToWidth(true);

        return scrollPane;
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
