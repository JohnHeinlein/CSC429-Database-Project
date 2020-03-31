package utilities;

import impresario.IModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {
    public static Alert makeAlert(String msg, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        switch (type) {
            case ERROR -> {
                alert.setTitle("Error");
                alert.setContentText("Error: " + msg);
            }
            case WARNING -> {
                alert.setTitle("Warning");
                alert.setContentText(msg);
            }
            case INFORMATION -> {
                alert.setTitle("Information");
                alert.setContentText(msg);
            }
        }
        return alert;
    }

    public static void errorMessage(String msg){
        Alert alert = makeAlert(msg, Alert.AlertType.ERROR);
        alert.showAndWait();
    }

    /**
     * Gives the user a confirmation that their action has succeeded
     * @param msg Message to display in window
     * @param model Model that has its main menu requested
     */
    public static void infoMessage(String msg, IModel model){
        Alert alert = makeAlert(msg,Alert.AlertType.INFORMATION);
        if(alert.showAndWait().get() == ButtonType.OK){
            model.stateChangeRequest("cancel",null);
        }
    }

    public static Optional<ButtonType> confirmMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("Confirm Action");
        alert.setContentText(msg);

        return alert.showAndWait();
    }
}
