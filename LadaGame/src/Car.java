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
    private final int sceneWidth = createMap.sceneWidth;
    private final int sceneHeight = createMap.sceneHeight;
    private double roadX;
    private String direction;
    private double degree;
     
    public Car(double x, double y, double w, double h, Image carImage) {
        super(x, y, w, h);
        this.setFill(new ImagePattern(carImage, 0, 0, 1, 1, true));
    }

    public Car() {
        
    }
    
    public Boolean checkCollision(Shape other) { //palauttaa tosi jos tämä ja other leikkaa
        return Shape.intersect(this, other).getBoundsInLocal().getWidth() != -1;
    }
    
    public void setRoadX(double roadX) {
        this.roadX = roadX;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public void generateCar() {
        
        ArrayList<Car> nodes = new ArrayList();

        obstacles.setManaged(false);
        playerGroup.setManaged(false);
        
        Car player = new Car(600, 400, 75, 150, new Image("picassoRED.png"));
        
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
                if(Math.random() * 100 < ++timer) { //spawn
                    Car newCar = new Car(roadX + Math.random() * (createMap.getRoadWidth() - 100), -200, 75, 150, new Image("picassoGREEN.png"));
                    nodes.add(newCar);
                    newCar.setRotate(180);
                        
                    switch (direction) {
                        case "left":
                            degree = 160;
                            newCar.setX(newCar.getX() - 60);
                            break;
                        case "straight":
                            degree = 180;
                            break;
                        case "right":
                            degree = 200;
                            newCar.setX(newCar.getX() + 100);
                            break;
                        default:
                            break;
                    }
                    newCar.setRotate(degree);
                    //if(newCar.getX() < sceneWidth / 2 - 50) newCar.setRotate(180 - newCar.getRotate() + 180);*/
                    
                    obstacles.getChildren().add(nodes.get(nodes.size() -1));
                    timer = 0;
                }
                
                collided = false;
                
                for(Car car : nodes) { //liike
                    double rotation = car.getRotate() - 180;
                    
                    car.setY(car.getY() + 5 + 1.15 + 1.15 * ((rotation == 0 ? 45 : (45 - Math.abs(rotation))) / 45));
                    car.setX(car.getX() + 1.4 * (rotation / -45));
                    
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