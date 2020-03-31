package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {
    public static void makeAlert(String msg, Alert.AlertType type){
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
        alert.showAndWait();
    }

    public static void errorMessage(String msg){
        makeAlert(msg, Alert.AlertType.ERROR);
    }
    public static void infoMessage(String msg){
        makeAlert(msg,Alert.AlertType.INFORMATION);
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
