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
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import utilities.Alerts;
import utilities.Debug;
import utilities.Utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;

public abstract class View extends Group implements IView, IControl {
    private final String DEFAULT_FONT = "Comic Sans MS";
    private final Font LABEL_FONT = new Font(DEFAULT_FONT, 18);
    private final Font BUTTON_FONT = new Font(DEFAULT_FONT,14);
    private final double FIELD_WIDTH = 300.0;

    protected IModel myModel;
    protected ControlRegistry myRegistry;

    protected Properties props; //Collects information from input fields for submission
    protected HashMap<String, Control> controlList; //Keeps track of what content is a control

    private BorderPane container;
    private VBox header;
    private HBox footer;
    private GridPane content;

    private String viewName; //Debugging purposes

    public View(IModel model, String classname) {
        myModel = model;
        myRegistry = new ControlRegistry(classname);
        viewName = classname;

        container = new BorderPane();
        header = new VBox();
        footer = new HBox();
        content = new GridPane();

        controlList = new HashMap<>();
        props = new Properties();

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
    public void addContent(String name, Region... controls) {
        // Label
        Label label = new Label(name);
        label.setFont(LABEL_FONT);

        String propertyName = Utilities.toCamelCase(name);

        // Controls/Fields
        HBox controlBox = new HBox();
        for (Region control : controls) {
            control.setPrefWidth(FIELD_WIDTH / controls.length); //Scale width to fill space
            controlBox.getChildren().add(control);

            boolean isControl = false; //debug purposes

            switch (control.getClass().toString()) {
                case "class javafx.scene.control.ComboBox", "class javafx.scene.control.DatePicker" -> {
                    controlList.put(propertyName,
                            (Control) control);
                    props.put(propertyName, "");
                    isControl = true; //Debugging
                }
                case "class userinterface.View$TextFieldWrapper" -> {
                    TextField textfield = ((TextFieldWrapper) control).getField();
                    controlList.put(Utilities.toCamelCase(textfield.getPromptText()),
                            textfield);
                    props.put(Utilities.toCamelCase(textfield.getPromptText()), "");
                    isControl = true; //Debugging
                }
                case "class userinterface.View$NotesFieldWrapper" -> {
                    controlList.put(propertyName,
                            ((NotesFieldWrapper) control).getField());
                    props.put(propertyName, "");
                    isControl = true; //Debugging
                }
            }

            // Print content that was added
            Debug.logMsg("(" + viewName + ") Added "
                    + ((isControl)? "control #" + controlList.size(): "button")
                    + " from section \"" + name + "\"");
        }

        content.addColumn(0, label);
        content.addColumn(1, controlBox);

        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setValignment(label, VPos.TOP);
    }
    public void addContent(TableView<?> table){
        int col_count = table.getColumns().size();
        double col_width = table.getColumns().get(0).getWidth();
        double width =  col_count * col_width;

        container.setPrefWidth(width);
        container.setCenter(table);

        Debug.logMsg("Set width: %s, actual width: %s", width, container.getWidth());
    }

    // ***************
    // Footer methods
    // ***************
    /**
     * Adds a submit button to footer to return to ControllerView
     */
    public void submitButton() {
        Button submitButton = makeButt("Submit", e -> {
            submit();
        });
        submitButton.setStyle("-fx-background-color: lightgreen");
        footButt(submitButton);
    }

    /**
     * Overridden to allow submitting data
     */
    protected void submit() {
        scrapeFields();
    }

    /**
     * Adds a cancel button to footer to return to ControllerView
     */
    public void cancelButton() {
        Button cancelButton = makeButt("Cancel", e -> {
            myModel.stateChangeRequest("Cancel", null);
        });
        cancelButton.setStyle("-fx-background-color: indianred");
        footButt(cancelButton);
    }

    /**
     * Adds button to leftmost of footer
     */
    public void footButt(Button butt) {
        footer.getChildren().add(footer.getChildren().size(), butt);
    }

    // ***************
    // Buttons
    // ***************

    /**
     * Returns a button that calls a statechangerequest
     *
     * @param text  Label of button
     * @param state Name of state for statechangerequest
     * @param prop  Properties to pass
     * @return Formatted button
     */
    public Button makeButt(String text, String state, Object prop) {
        return makeButt(text, e -> myModel.stateChangeRequest(state, prop));
    }

    /**
     * Returns a button with a specified function
     *
     * @param text  Label of button
     * @param event Event Handler called on press. Recommended to be a lambda.
     * @return Formatted button
     */
    public Button makeButt(String text, EventHandler<ActionEvent> event) {
        Button butt = new Button(text);
        butt.setOnAction(event);
        butt.setFont(BUTTON_FONT);
        return butt;
    }

    // ***************
    // Text Field
    // ***************
    public TextFieldWrapper makeField(String prompt) {
        return makeField(prompt, true);
    }

    public TextFieldWrapper makeField(String prompt, boolean editable) {
        TextFieldWrapper field = new TextFieldWrapper(prompt);
        field.setEditable(editable);
        field.setDisable(!editable);
        return field;
    }

    public NotesFieldWrapper makeNotesField(String prompt, int maxLength) {
        return new NotesFieldWrapper(prompt, maxLength);
    }

    /**
     * Creates combo box with any number of choices. Defaults to first choice.
     *
     * @param choices List of choices. First will be selected by default.
     * @return
     */
    public ComboBox<String> makeComboBox(String... choices) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(choices);
        combo.getSelectionModel().selectFirst();

        return combo;
    }

    /**
     * Creates pre-formatted date picker.
     * Defaults to pattern "MM/dd/yyyy"
     *
     * @return Formatted date picker
     */
    public DatePicker makeDatePicker() {
        DatePicker picker = new DatePicker();

        picker.setConverter(new StringConverter<LocalDate>() {
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            {
                picker.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        picker.setOnAction(e -> {
            LocalDate date = picker.getValue();
            Debug.logMsg("Selected date: " + date);
        });

        picker.setEditable(false);
        return picker;
    }

    // ***************
    // Misc controls
    // ***************

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

    /**
     * Scrapes the fields into the local Properties object.
     * Shows error dialogue if a field is empty.
     * @return Whether scrape was successful or not
     */
    public Boolean scrapeFields(){
        return scrapeFields(true);
    }

    /**
     * Scrapes fields into the local Properties object
     * @return Whether scrape was successful or not (always true for unsafe)
     */
    public Boolean scrapeFieldsUnsafe(){
        return scrapeFields(false);
    }

    /**
     * Scrapes the fields into the local Properties object.
     * @param safe Whether or not all fields are required
     * @return Whether scrape succeeded or not
     */
    private Boolean scrapeFields(Boolean safe) {
        for (String field : controlList.keySet()) {
            Control control = controlList.get(field);
            String data = "";
            switch (control.getClass().toString()) {
                case "class javafx.scene.control.ComboBox" -> data = ((ComboBox<String>) control).getValue();
                case "class javafx.scene.control.DatePicker" -> data = ((DatePicker) control).getValue().toString();
                case "class javafx.scene.control.TextField" -> data = ((TextField) control).getText();
                case "class javafx.scene.control.TextArea" -> data = ((TextArea) control).getText();
                default -> {
                    Debug.logErr("Unsupported Control type " + control.getClass().toString());
                    Alerts.errorMessage("Unsupported Control type, enable debugging");
                }
            }
            if (safe && data.equals("") || data == null) {
                Alerts.errorMessage("All fields must be entered!");
                Debug.logErr("Empty fields, returning false");
                return false;
            } else {
                props.put(field, data);
            }
        }
        Debug.logMsg(
                """
                (%s)
                    Fields queried:
                        %s
                    Properties retrieved:
                        %s
                """,
                viewName,
                controlList.toString()
                        .replaceAll(",", ",\n\t\t")
                        .replaceAll("@[^},]*", ""), //Exclude memory address (trust me)
                props.toString().replaceAll(",", ",\n\t\t")
        );
        return true;
    }

    protected String getValue(Control control) {
        return switch (control.getClass().toString()) {
            case "class javafx.scene.control.ComboBox"  -> ((ComboBox<String>) control).getValue();
            case "class javafx.scene.control.DatePicker"-> ((DatePicker) control).getConverter().toString();
            case "class javafx.scene.control.TextField" -> ((TextField) control).getText();
            case "class javafx.scene.control.TextArea"  -> ((TextArea) control).getText();
            default -> {
                Debug.logErr("Unsupported control: " + control.getClass());
                yield null;
            }
        };
    }

    protected void setValue(Control control, String value) {
        Debug.logMsg(String.format("Updating %s to value %s",control,value));
        switch (control.getClass().toString()) {
            case "class javafx.scene.control.DatePicker" -> {
                DatePicker picker = ((DatePicker) control);
                StringConverter<LocalDate> converter = picker.getConverter();
                picker.setValue(converter.fromString(value));
            }
            case "class javafx.scene.control.ComboBox"  -> ((ComboBox<String>) control).getSelectionModel().select(value);
            case "class javafx.scene.control.TextField" -> ((TextField) control).setText(value);
            case "class javafx.scene.control.TextArea"  -> ((TextArea) control).setText(value);
            default -> Debug.logErr("Unsupported control: " + control.getClass());
        }
    }

    public void setRegistry(ControlRegistry registry) {
        myRegistry = registry;
    }

    // Allow models to register for state updates
    public void subscribe(String key, IModel subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    // Allow models to unregister for state updates
    public void unSubscribe(String key, IModel subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    protected class TextFieldWrapper extends VBox {
        private TextField field;
        private MessageView message;

        public TextFieldWrapper(String prompt) {
            field = new TextField();
            field.setPromptText(prompt);
            field.setOnAction(e -> submit());
            field.setStyle("-fx-font-family: " + DEFAULT_FONT);

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

        //Pass to field so we don't need to chain getField().setEditable();
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
}
