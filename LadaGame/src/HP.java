import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.*;


public class HP {
    private Rectangle HpBar;
    private final Rectangle HpBarFrame;
    private final Rectangle HpBarBackground;
    private final Group HpGroup;
    private final AnimationTimer timer;
    
    private double health;
    private double healthProportional;
    private final double MAXHEALTH = 300;
    private final int CARDAMAGE = 200;
    private final int GRASSDAMAGE = 3;
    
    HP() {
        health = MAXHEALTH;
        
        HpBarFrame = new Rectangle(50, 50, 250, 50);
        HpBarFrame.setFill(Color.TRANSPARENT);
        HpBarFrame.setStrokeWidth(2);
        HpBarFrame.setStroke(Color.BLACK);
        HpBarBackground = new Rectangle(50, 50, 250, 50);
        HpBarBackground.setStrokeWidth(0);
        HpBarBackground.setFill(Color.DARKSLATEGRAY);
        HpBar = new Rectangle(50, 50, 250, 50);
        HpBar.setFill(Color.LIGHTGREEN);
        HpBar.setStrokeWidth(0);
        
        HpGroup = new Group();
        HpGroup.setManaged(false);
        HpGroup.getChildren().addAll(HpBarBackground, HpBar, HpBarFrame);
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                health = (health > 0.0 && health < MAXHEALTH) ? ++health : health;
                healthProportional = health / MAXHEALTH;
                HpBar.setWidth(250.0 * healthProportional);
                HpBar.setFill(Color.color(1 - healthProportional, healthProportional, 0.0));
            }
        };
        timer.start();
    }
    
    public Group getGroup() {
        return HpGroup;
    }
    
    public void hitCar() {
        health = Math.max(0, health - CARDAMAGE);
    }
    
    public void hitGrass() {
        health = Math.max(0, health - GRASSDAMAGE);
    }
    
    public void hitOut() {
        health = 0;
    }
    public double getHP() {
        return health;
    }
}
