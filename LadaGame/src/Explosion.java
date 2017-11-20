import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
    KEK
*/

public class Explosion 
{
    Group explosions = new Group();
    Rectangle animation;
    Circle particle;
    double alphaColor = 0.0;
    Color startColor = new Color(1.0, 1.0, 0.0, 1.0);
    ArrayList<Node> nodes = new ArrayList();
    
    
    public Explosion() {
        explosions.setManaged(false);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for(Node node : explosions.getChildren()) {
                    Shape animation = (Shape) node;
                    
                    if(animation.getOpacity() < 0.98) {
                        animation.setOpacity(animation.getOpacity() + 0.04);
                    }
                    else nodes.add(node);
                }
                if(!nodes.isEmpty()) {
                    explosions.getChildren().removeAll(nodes);
                    nodes.clear();
                }
            }
        };
        timer.start();
    }
    
    public void explode(Car target) {
        
        animation = new Rectangle(target.getX(), target.getY(), target.getWidth(), target.getHeight());
        
        for(int i = 0; i < 30; i++) {
            
            particle = new Circle(target.getX() + i, target.getY() + i, 5);
            particle.setFill(Color.ORANGERED);
            particle.setOpacity(alphaColor);
            explosions.getChildren().add(particle);
        }
        animation.setRotate(target.getRotate());
        animation.setFill(startColor);
        animation.setOpacity(alphaColor);
        explosions.getChildren().add(animation);
    }
}
