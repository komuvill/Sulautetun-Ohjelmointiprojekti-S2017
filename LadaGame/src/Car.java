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
    private HP health;
    private final int sceneWidth = createMap.sceneWidth;
    private final int sceneHeight = createMap.sceneHeight;
    private double roadTopX;
    private double roadX;
    private double grassX;
    private int roadWidth;
    private double grassWidth;
    private String direction;
    private double degree;
    private final double mouseCenter = sceneWidth / 2;
    private final double deadzone = 20;
    private double serialRotation = 0;
    private double velocity = 0;
    Car newCar;
    
    //Explosive stuff
    Explosion explosion = new Explosion();
    static final int NORMAL = 0;
    static final int REQUEST_EXPLOSION = 1;
    static final int TARGET_EXPLODING = 2;
    static final int TARGET_DESTROYED = 3;
    int state = NORMAL;
    
    
    public void setState(int state) {
        this.state = state;
    }
    
    public int getState() {
        return state;
    }
     
    public Car(double x, double y, double w, double h, Image carImage) {
        super(x, y, w, h);
        this.setFill(new ImagePattern(carImage, 0, 0, 1, 1, true));
    }

    public Car() {
        
    }
    
    public void move(double rotation) {
        serialRotation = rotation;
    }
    
    public Boolean checkCollision(Shape other) { //palauttaa tosi jos tämä ja other leikkaa
        return Shape.intersect(this, other).getBoundsInLocal().getWidth() != -1;
    }
    
    public void setRoadTopX(double roadTopX) {
        this.roadTopX = roadTopX;
    }
    
    public void setRoadX(double roadX) {
        this.roadX = roadX;
        System.out.println(roadX);
    }
    
    public void setGrassX(double grassX) {
        this.grassX = grassX;
    }
    
    public void setRoadWidth(int roadWidth) {
        this.roadWidth = roadWidth;
    }
    
    public void setGrassWidth(double grassWidth) {
        this.grassWidth = grassWidth;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public void generateCar() {
        health = new HP();
        
        ArrayList<Car> nodes = new ArrayList();

        obstacles.setManaged(false);
        playerGroup.setManaged(false);
        
        Car player = new Car(600, 500, 75, 150, new Image("player.png"));
        
        /*player.setOnMouseDragged((MouseEvent e) -> { //Ohjaus hiirellä
            player.setX(e.getSceneX() - player.getWidth() / 2);
            player.setY(e.getSceneY() - player.getHeight() / 2);
        });*/
        
        
        obstacles.getChildren().addAll(nodes); //autot
        playerGroup.getChildren().add(player); //pelaaja
        
        
        stack.getChildren().addAll(obstacles, playerGroup, explosion.explosions, health.getGroup());
        
        
        animation_timer = new AnimationTimer() { //Game Loop
            
            int timer = 0;
            Boolean collided;
            Boolean roadData = false; //roadX on alussa nolla
            ArrayList<Car> despawning = new ArrayList();
            
            @Override
            public void handle(long now) {
                double playerX = player.getX() + 75 / 2;
                
                if(Math.abs(serialRotation) > deadzone) { //liike
                    velocity += serialRotation / 20;
                    if(Math.abs(velocity) > 30)
                        velocity = 30 * serialRotation / Math.abs(serialRotation);
                } else velocity = velocity * 0.9;
                
                player.setX(player.getX() + velocity / 6);
                
                // Varmistetaan, että auto pysyy pelialueen sisällä
                if(player.getX() < 0) {
                    player.setX(0);
                    velocity = 0;
                } else if(player.getX() > sceneWidth - 75) {
                    player.setX(sceneWidth - 75);
                    velocity = 0;
                }
                
                player.setRotate(velocity / 2);
                
               

                if(Math.random() * 1500 < ++timer && timer > 50) { //spawn
                    Car newCar = new Car(roadTopX + Math.random() * (createMap.getRoadWidth() - 75), -200, 75, 150, new Image("enemy.png"));

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
                    
                    obstacles.getChildren().add(nodes.get(nodes.size() -1));
                    timer = 0;
                }
                
                collided = false;
                
                for(Car car : nodes) { //liike
                    double rotation = car.getRotate() - 180;
                    car.setY(car.getY() + 5 + 1.15 + 1.15 * ((rotation == 0 ? 45 : (45 - Math.abs(rotation))) / 45));
                    car.setX(car.getX() + 1.4 * (rotation / -45));
 
                    if(car.getY() >= sceneHeight) { //despawn muistiin
                        
                        despawning.add(car);
                        continue;
                    }
                    if(player.checkCollision(car)) {
                        car.setState(REQUEST_EXPLOSION);
                        explosion.explode(car);
                        if(car.getOpacity() > 0) health.hitCar();
                        car.setOpacity(0);
                    }
                }
                
                if(!despawning.isEmpty()) { //despawn
                    obstacles.getChildren().removeAll(despawning);
                    nodes.removeAll(despawning);
                    despawning.clear();
                }
                
                //Tiellä pysymisen valvonta
                if(!roadData) roadData = (roadX > 0) ? true : false;
                else {
                    if(playerX < grassX || playerX > grassX + grassWidth) health.hitOut();
                    else if(playerX < roadX || playerX > roadX + roadWidth) health.hitGrass();
                }
            }
        };
        animation_timer.start();
    }
}