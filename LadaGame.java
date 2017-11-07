package ladagame;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

public class LadaGame extends Application {
        @Override
        public void start(Stage primaryStage) {
            CreateMap createMap = new CreateMap();
            SerialRotationReader reader = new SerialRotationReader();
            AnimationTimer timer;
            timer = new AnimationTimer(){
                float rotation = 0;
                @Override
                public void handle(long now) {
                    rotation = reader.getRotation();
                    if(rotation < 0){
                        System.out.println("Auto kääntyy vasemmalle!");
                    }else if(rotation > 0){
                        System.out.println("Auto kääntyy oikealle!");
                    }
                }
            };
            
            primaryStage.setTitle("Lada The Ultimate Challenge 2017");
            primaryStage.setScene(createMap.scene);
            primaryStage.show();
            timer.start();
            
        }
    

    public static void main(String[] args) {
        launch(args);
    }
    
}
