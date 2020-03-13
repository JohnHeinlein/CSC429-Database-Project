import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Controller;
import userinterface.MainStageContainer;
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
