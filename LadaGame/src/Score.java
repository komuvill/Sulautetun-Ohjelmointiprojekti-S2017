import javafx.scene.Group;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score {
    private int score = 0;
    private int combo = 0;
    private final int MAXCOMBO = 300;
    
    private Group scoreGroup;
    private Text scoreText;
    
    
    Score() {
        scoreText = new Text(50, 140, "0");
        scoreText.setFill(Color.CYAN);
        scoreText.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        
        scoreGroup = new Group();
        scoreGroup.setManaged(false);
        scoreGroup.getChildren().add(scoreText);
    }
    
    public void score() {
        score += (int) (combo / 30);
        scoreText.setText(String.valueOf(score));
        
        if(combo < MAXCOMBO) combo++;
    }
    
    public void resetCombo() {
        combo = 0;
    }
    
    public int getScore() {
        return score;
    }
    
    public Group getGroup() {
        return scoreGroup;
    }
}
