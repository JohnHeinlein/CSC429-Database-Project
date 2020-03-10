import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Controller;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

public class Driver extends Application{
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        System.out.println("CSC429 Project 1.00");

        MainStageContainer.setStage(primaryStage, "Placeholder");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        try{
            Controller controller = new Controller();
        }catch(Exception exc){
            System.err.println("Driver: Could not create controller");
            exc.printStackTrace();
        }
// Create controller
        WindowPosition.placeCenter(primaryStage);
        primaryStage.show();
    }
}
