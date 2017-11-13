import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.input.*;
import javafx.scene.shape.Shape;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import java.util.ArrayList;
import javafx.scene.Scene;


public class Car extends Rectangle{
    
    CreateMap createMap = new CreateMap();
    AnimationTimer animation_timer;
    Scene scene;
    Group obstacles = new Group();
    Group playerGroup = new Group();
    StackPane stack = new StackPane();
    int sceneWidth = createMap.sceneWidth;
    int sceneHeight = createMap.sceneHeight;
     
    public Car(double x, double y, double w, double h, Image carImage) {
        super(x, y, w, h);
        this.setFill(new ImagePattern(carImage, 0, 0, 1, 1, true));
    }

    public Car() {
        
    }
    
    public Boolean checkCollision(Shape other) { //palauttaa tosi jos tämä ja other leikkaa
        return Shape.intersect(this, other).getBoundsInLocal().getWidth() != -1;
    }
    
    public void generateCar() {
        
        ArrayList<Car> nodes = new ArrayList();
        

        
        obstacles.setManaged(false);
        playerGroup.setManaged(false);
        
        Car player = new Car(600, 400, 100, 200, new Image("picassoRED.png"));
        
        player.setOnMouseDragged((MouseEvent e) -> { //Ohjaus hiirellä
            player.setX(e.getSceneX() - player.getWidth() / 2);
            player.setY(e.getSceneY() - player.getHeight() / 2);
        });
        
        
        obstacles.getChildren().addAll(nodes); //autot
        playerGroup.getChildren().add(player); //pelaaja
        
        
        stack.getChildren().addAll(obstacles, playerGroup);
        
        
        animation_timer = new AnimationTimer() { //Game Loop
            
            int timer = 0;
            Boolean collided;
            ArrayList<Car> despawning = new ArrayList();
            
            @Override
            public void handle(long now) {
                if(Math.random() * 1500 < ++timer) { //spawn
                    Car newCar = new Car((int) (Math.random() * sceneWidth), -200, 100, 200, new Image("picassoGREEN.png"));
                    nodes.add(newCar);
                    newCar.setRotate(180);
                    if(timer % 3 == 0) { //Esimerkki, spawnaa autoja satunnaisesti eri suuntiin
                        newCar.setRotate(Math.random() * 45 + 180);
                        if(newCar.getX() < sceneWidth / 2 - 50) newCar.setRotate(180 - newCar.getRotate() + 180);
                    }
                    obstacles.getChildren().add(nodes.get(nodes.size() -1));
                    timer = 0;
                }
                
                collided = false;
                
                for(Car car : nodes) { //liike
                    double rotation = car.getRotate() - 180;
                    
                    car.setY(car.getY() + 5 + 5 * ((rotation == 0 ? 45 : (45 - Math.abs(rotation))) / 45));
                    car.setX(car.getX() + 5 * (rotation / -45));
                    
                    if(car.getY() >= sceneHeight) { //despawn muistiin
                        obstacles.getChildren().remove(car);
                        despawning.add(car);
                        continue;
                    }
                    if(player.checkCollision(car)) {
                        collided = true;
                    }
                }
                
                if(!despawning.isEmpty()) { //despawn
                    nodes.removeAll(despawning);
                    despawning.clear();
                }
                
                if(collided) { //KOLLISIO HAVAITTU
                
                }

            }
        };
        animation_timer.start();
    }
}