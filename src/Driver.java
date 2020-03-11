import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Controller;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;
import Utilities.Utilities;

public class Driver{
    public static void main(String[] args) {
        Application.launch(App.class);
    }

    public static class App extends Application {
        public void start(Stage primaryStage) {
            System.out.println("CSC429 Project 1.00");

            MainStageContainer.setStage(primaryStage, "Christmas Tree Sales System");

            primaryStage.setOnCloseRequest(e -> System.exit(0));

            try {
                Controller controller = new Controller();
            } catch (Exception exc) {
                Utilities.logErr("Could not create controller");
                exc.printStackTrace();
            }
        }
    }
}
