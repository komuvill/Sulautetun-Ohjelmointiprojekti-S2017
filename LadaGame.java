import javafx.application.Application;
import javafx.stage.Stage;

public class LadaGame extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        CreateMap createMap = new CreateMap();
        primaryStage.setTitle("Lada The Ultimate Challenge 2017");
        primaryStage.setScene(createMap.scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
