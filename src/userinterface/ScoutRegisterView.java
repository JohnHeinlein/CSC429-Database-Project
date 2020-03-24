// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.time.*;
import java.util.Properties;

// project imports
import impresario.IModel;

public class ScoutRegisterView extends View {

    // GUI stuff
    private TextField lastName;
    private TextField firstName;
    private TextField middleName;
    private DatePicker dateOfBirth;
    private TextField phoneNumber;
    private TextField email;
    private TextField troopId;

    private Button submitButton;

    private ComboBox<String> status;

    // For showing error message
    private MessageView statusLog;

    private short clear = 0;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public ScoutRegisterView(IModel scout)
    {
        super(scout, "ScoutRegisterView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // Add a title for this panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this Container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("             "));

        getChildren().add(container);

        populateFields();

        myModel.subscribe("UpdateStatusMessage", this);
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Insert Scout Data");
        titleText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.BLACK);
        container.getChildren().add(titleText);

        return container;
    }

    private GridPane createFormContent() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // data entry fields
        Label fnameLabel = new Label("First Name:");
        grid.add(fnameLabel, 0, 0);

        firstName = new TextField();
        firstName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(firstName, 1, 0);

        Label lnameLabel = new Label("Last Name:");
        grid.add(lnameLabel, 0, 1);

        lastName = new TextField();
        lastName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(lastName, 1, 1);

        //
        Label midnameLabel = new Label("Middle Name:");
        grid.add(midnameLabel, 0, 2);

        middleName = new TextField();
        middleName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(middleName, 1, 2);

        Label dobLabel = new Label("Date Of Birth:");
        grid.add(dobLabel, 0, 3);

        dateOfBirth = new DatePicker();
        grid.add(dateOfBirth, 1, 3);

        Label pnLabel = new Label("Phone Number:");
        grid.add(pnLabel, 0, 4);

        phoneNumber = new TextField();
        phoneNumber.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(phoneNumber, 1, 4);

        Label emlabel = new Label("Email: ");
        grid.add(emlabel, 0, 5);

        email = new TextField();
        email.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(email, 1, 5);

        Label trooplabel = new Label("Troop ID: ");
        grid.add(trooplabel, 0, 6);

        troopId = new TextField();
        troopId.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
            }
        });
        grid.add(troopId, 1, 6);


        Label statusid = new Label("Status:");
        grid.add(statusid, 0, 7);
        status = new ComboBox<String>();
        grid.add(status, 1, 7);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                processAction(a);
                if ( clear == 0 ) {
                    firstName.setText("");
                    lastName.setText("");
                    middleName.setText("");
                    phoneNumber.setText("");
                    email.setText("");
                    troopId.setText("");
                    status.setValue("Active");
                }
            }
        });

        HBox btnContainer = new HBox(10);
        btnContainer.setAlignment(Pos.BOTTOM_LEFT);
        btnContainer.getChildren().add(submitButton);
        grid.add(btnContainer, 0, 8);

        Button backButton = new Button("Cancel");
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent a) {
                firstName.setText("");
                lastName.setText("");
                middleName.setText("");
                phoneNumber.setText("");
                email.setText("");
                troopId.setText("");
                status.setValue("Active");
                myModel.stateChangeRequest("Cancel", null);
            }
        });

        HBox backbtnContainer = new HBox(10);
        backbtnContainer.setAlignment(Pos.BOTTOM_RIGHT);
        backbtnContainer.getChildren().add(backButton);
        grid.add(backbtnContainer, 1, 8);

        return grid;

    }
    // This method processes events generated from our GUI components.
    // Make the ActionListeners delegate to this method
    //-------------------------------------------------------------
    public void processAction(Event evt)
    {
        // DEBUG: System.out.println("TellerView.actionPerformed()");

        clearErrorMessage();

        String fnameEntered = firstName.getText();
        String lnameEntered = lastName.getText();
        String mnameEntered = middleName.getText();
        String dobEntered = dateOfBirth.getValue().toString();
        System.out.println(dobEntered);
        String phoneEntered = phoneNumber.getText();
        String emailEntered = email.getText();
        String troopIdEntered = troopId.getText();

        try {
            if ((fnameEntered == null) || (fnameEntered.length() == 0) || (lnameEntered == null) || (lnameEntered.length() == 0) || (mnameEntered == null) || (mnameEntered.length() == 0) || (dobEntered == null) || (troopIdEntered.length() == 0) || (troopIdEntered == null)) {
                displayErrorMessage("Please do not leave fields blank!");
                firstName.requestFocus();
                clear = 1;
            }
            else {
                Properties props = new Properties();
                props.put("firstName", fnameEntered);
                props.put("lastName", lnameEntered);
                props.put("middleName", mnameEntered);
                props.put("dateOfBirth", dobEntered);
                props.put("phoneNumber", phoneEntered);
                props.put("email", emailEntered);
                props.put("troopId", troopIdEntered);
                props.put("status", status.getValue());

                LocalDate today = LocalDate.now();

                props.put("dateStatusUpdated", today.toString());

                statusLog.displayMessage("Success");
                myModel.stateChangeRequest("ScoutRegisterSubmit", props);
                clear = 0;
            }
        } catch (NumberFormatException e) {displayErrorMessage("Please enter a numeric Phone Number"); phoneNumber.requestFocus(); clear = 1;}
    }

    //-------------------------------------------------------------
    public void populateFields()
    {
        firstName.setText("");
        lastName.setText("");
        middleName.setText("");
        phoneNumber.setText("");
        email.setText("");
        troopId.setText("");
        status.setValue("Active");
        status.getItems().add("Inactive");
        status.getItems().add("Active");
    }

    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {

        statusLog = new MessageView(initialMessage);

        return statusLog;
    }
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {
        // STEP 6: Be sure to finish the end of the 'perturbation'
        // by indicating how the view state gets updated.
        if (key.equals("InsertScout") == true) {

        }

    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}
