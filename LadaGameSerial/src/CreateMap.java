import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;

public class CreateMap {
    
    Group groupForMap = new Group();
    
    
    //Create objects for texture processing
    Image roadImage = new Image("road.jpg");
    Image grassImage = new Image("grass.jpg");
    PixelReader roadImageReader = roadImage.getPixelReader();
    PixelReader grassImageReader = grassImage.getPixelReader();
    WritableImage newRoadImage;
    WritableImage newGrassImage;
    
    //Game world properties
    private final int roadWidth = 450;
    private final int roadHeight = 5;
    public final int sceneWidth = 1280;
    public final int sceneHeight = 720;
    private final double grassWidth = roadWidth * 1.5;
    private double roadX;
    private double roadY;
    private double grassX;
    private double grassY;
    private String direction = "straight";
    
    
    //Timer, node indexing, scene creation
    private AnimationTimer timer;
    private final ArrayList<Rectangle> rect = new ArrayList<>();
    //Scene scene;
    
    public CreateMap(){
        
    }
    public String getDirection() {
        return direction;
    }
    
    public int getRoadHeight() {
        return roadHeight;
    }
    
    public int getRoadWidth() {
        return roadWidth;
    }
    
    public double getGrassWidth() {
        return grassWidth;
    }
    
    public double getRoadX() {
        return roadX;
    }
    
    public double getRoadY() {
        return roadY;
    }
    
    public double getGrassX() {
        return grassX;
    }
    
    public double getGrassY() {
        return grassY;
    }

    private String roadDirection(){
        
        //Random numbers generate directions for the road
        int i = (int) (Math.random() * 3);
        direction = null;
        switch (i) {
            case 0:
                direction = "left";
                break;
            case 1:
                direction = "straight";
                break;
            case 2:
                direction = "right";
                break;
            default:
                break;
        }
        return direction;
    }
    
    public void generateRoad(){
        
        groupForMap.setManaged(false);
        //Create starting block so the road starts off going straight
        Rectangle startRoadBlock = new Rectangle(sceneWidth / 3, 0, roadWidth, sceneHeight);
        Rectangle startGrassBlock = new Rectangle(sceneWidth / 4, 0, grassWidth, sceneHeight);
        startRoadBlock.setFill(new ImagePattern(roadImage, 0, 0, roadWidth, sceneHeight, false));
        startGrassBlock.setFill(new ImagePattern(grassImage, 0, 0, grassWidth, sceneHeight, false));
        rect.add(startGrassBlock); //rect is the ArrayList that acts as an index for the handle method
        rect.add(startRoadBlock);
        groupForMap.getChildren().add(startGrassBlock);
        groupForMap.getChildren().add(startRoadBlock);
        
        
        timer = new AnimationTimer() {
            
            int tick = 150; //"Slices" of road that will go to a same direction
            
            //Counters for dividing textures
            int pictureCounterRoad = (int) (roadImage.getHeight() / roadHeight) - roadHeight; 
            int pictureCounterGrass = (int) (grassImage.getHeight() / roadHeight) - roadHeight;
            
            private final ArrayList<Rectangle> despawn = new ArrayList<>();
            
            @Override
            public void handle(long t) {
                
                Rectangle road = new Rectangle(rect.get(rect.size()-1).getX(), 0, roadWidth, roadHeight);
                Rectangle grass = new Rectangle(road.getX()-107, 0, grassWidth, roadHeight);
                            
                newRoadImage = new WritableImage(roadImageReader, 0, pictureCounterRoad * roadHeight, roadWidth, roadHeight);
                newGrassImage = new WritableImage(grassImageReader, 0, pictureCounterGrass * roadHeight, roadWidth, roadHeight);
                   
                //If clauses prevent the counters from going negative
                if(pictureCounterRoad <= 0) {
                    pictureCounterRoad = (int) (roadImage.getHeight() / roadHeight);
                }
                
                if(pictureCounterGrass <= 0) {
                    pictureCounterGrass = (int) (grassImage.getHeight() / roadHeight);
                }
                
                pictureCounterGrass -= roadHeight;
                pictureCounterRoad -= roadHeight;
                
                road.setFill(new ImagePattern(newRoadImage, 0, 0, roadWidth, roadHeight, false));
                grass.setFill(new ImagePattern(newGrassImage, 0, 0, grassWidth, roadHeight, false));
                rect.add(grass);
                rect.add(road);
                groupForMap.getChildren().add(grass);
                groupForMap.getChildren().add(road);
                if(tick == 0){
                    direction = roadDirection();
                    tick = 150;
                }
                else{
                    tick--;
                }
                
                rect.forEach((node) -> {
                    
                    //Move all of the slices of road horizontally left or right, depending on the random number generator
                    if(rect.indexOf(node) == rect.size() - 1){
                        switch(direction) {
                        case "left":
                                //node.setX(node.getX() <= 100 ? 100 : node.getX() - 2);
                                if(node.getX() <= 100){
                                    direction = "straight";
                                }
                                else{
                                    node.setX(node.getX() - 2);
                                }
                            break;
                        case "right":
                                //node.setX(node.getX() >= sceneWidth - (road.getWidth()+100) ? sceneWidth - (road.getWidth()+100) :node.getX() +2);
                                if(node.getX() >= sceneWidth - (road.getWidth() + 100)) {
                                    direction = "straight";
                                }
                                else{
                                    node.setX(node.getX() + 2);
                                }
                            break;  
                        case "straight":
                            break;
                        }
                        
                    }
                    if(node.getY() > sceneHeight) {
                            groupForMap.getChildren().remove(node);
                            despawn.add(node);
                            
                    }
                    //Move all of the slices of road vertically
                    node.setY(node.getY() + roadHeight);
                    roadX = node.getX();
                    //System.out.println("CreateMap roadX : " +(int) (roadX));
                });
                if(!despawn.isEmpty()) {
                        rect.removeAll(despawn);
                        despawn.clear();
                }
                /*if(rect.size() > 100)
                roadX = rect.get(100).getX() + 107;  */
            }
        };
        timer.start();
    }
}