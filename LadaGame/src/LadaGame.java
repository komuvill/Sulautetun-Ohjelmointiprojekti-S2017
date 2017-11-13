import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class LadaGame extends Application {
    Group god = new Group();
    Scene scene;
    public String lada = "lada.mp3";
    Media hit = new Media(new File(lada).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    
    @Override
    public void start(Stage primaryStage) {
        mediaPlayer.play();
        AnimationTimer timer;
        CreateMap createMap = new CreateMap();
        createMap.generateRoad();
        Car createCar = new Car();
        createCar.generateCar();
        scene = new Scene(god,createMap.sceneWidth,createMap.sceneHeight);
        scene.setFill(Color.GREEN);
        god.getChildren().addAll(createMap.groupForMap,createCar.stack);
        
        scene.setOnMouseMoved((MouseEvent e) -> {
            createCar.move(e);
        });
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                createCar.setRoadX(createMap.getRoadX());
                createCar.setDirection(createMap.getDirection());
                //System.out.println("LadaGame roadX: " + createMap.getRoadX());
            }
            
        };
        
        timer.start();
        primaryStage.setTitle("Lada The Ultimate Challenge 2017");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
