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
import javafx.application.Platform;

public class LadaGame extends Application {
    Group god = new Group();
    Scene scene;
    Scene menuScene;
    public String lada = "lada.mp3";
    Media hit = new Media(new File(lada).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    Car createCar;
    CreateMap createMap;
    
    @Override
    public void start(Stage primaryStage) {
        
        //Shows only the main menu
        MainMenu mainMenu = new MainMenu();
        menuScene = new Scene(mainMenu.menuItems , 1280 , 720);
        menuScene.setFill(Color.GREEN);
        primaryStage.setTitle("Lada The Ultimate Challenge 2017");
        primaryStage.setScene(menuScene);
        primaryStage.show();
        
        //Main menu button functions
        mainMenu.buttonStart.setOnMouseClicked((MouseEvent e) -> {
            AnimationTimer timer;
            mediaPlayer.play();
            
            //Create objects
            createMap = new CreateMap();
            createCar = new Car();
            scene = new Scene(god,createMap.sceneWidth,createMap.sceneHeight);
            createMap.generateRoad();
            scene.setFill(Color.GREEN);
            createCar.generateCar();

            scene.setOnMouseMoved((MouseEvent a) -> {
                createCar.move(a);
            });
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    createCar.setRoadX(createMap.getRoadTopX());
                    createCar.setDirection(createMap.getDirection());
                }
            };
            timer.start();
            god.getChildren().addAll(createMap.groupForMap, createCar.stack);
            primaryStage.setScene(scene);
        });
        
        mainMenu.buttonScores.setOnMouseClicked((MouseEvent e) -> {
            //TODO
        });
        
        mainMenu.buttonQuit.setOnMouseClicked((MouseEvent e) -> {
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
