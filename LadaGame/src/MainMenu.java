import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.text.*;
import javafx.scene.control.Button;

public class MainMenu {
    
    Group menuItems = new Group();
    CreateMap createMap = new CreateMap();
    VBox vBtn;
    Button buttonStart;
    Button buttonScores;
    Button buttonQuit;
    Text serialText;
    
    public void serialReady() {
        vBtn.getChildren().clear();
        vBtn.getChildren().addAll(buttonStart,buttonScores,buttonQuit);
    }
    
    public MainMenu() {
        
        BorderPane border = new BorderPane();
        vBtn = new VBox();
        menuItems.setManaged(false);
        
        border.setCenter(vBtn);
        border.setPrefSize(createMap.sceneWidth, createMap.sceneHeight);
        vBtn.setSpacing(10);
        vBtn.setAlignment(Pos.CENTER);
        
        buttonStart = new Button();
        buttonScores = new Button();
        buttonQuit = new Button();
        serialText = new Text("Searching for serial...");
        serialText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        
        buttonStart.setPrefSize(132, 44);
        buttonStart.setStyle("-fx-background-image: url('startBtn.jpg')");
        buttonScores.setStyle("-fx-background-image: url('scoreBtn.jpg')");
        buttonScores.setPrefSize(166, 44);
        buttonQuit.setStyle("-fx-background-image: url('quitBtn.jpg')");
        buttonQuit.setPrefSize(105, 44);
        
        
        vBtn.getChildren().addAll(serialText,buttonScores,buttonQuit);
        menuItems.getChildren().addAll(border);
    }
}
