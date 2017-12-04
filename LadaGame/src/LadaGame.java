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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.stage.WindowEvent;

public class LadaGame extends Application implements SerialPortEventListener {
    Group god = new Group();
    Scene scene;
    Scene menuScene;
    AnimationTimer timer;
    AnimationTimer serialTimer;
    long schedule = System.nanoTime() + 10^9;
    public String lada = "lada.wav";
    Media hit = new Media(new File(lada).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    MainMenu mainMenu;
    Car createCar;
    CreateMap createMap;
    HighscoreClient highScore;
    private Boolean gameEnded = false;
    SerialPort serialPort;
    CommPortIdentifier portId = null;
    //Muuta vastaamaan käytettävää porttia
    static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM6", // Windows
                        };
    BufferedReader input;
    OutputStream output;
    final int TIME_OUT = 2000;
    final int DATA_RATE = 9600;
    double rotation = 0;
    
    private void clearTimer() {
        timer = null;
    }
    
    private void waitSerial() {
        //First, Find an instance of serial port as set in PORT_NAMES.
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                portId = currPortId;

                for (String portName : PORT_NAMES) {
                        if (currPortId.getName().equals(portName)) {
                                portId = currPortId;
                                break;
                        }
                }
        }

        if(portId == null){
                /*
                * Tämän pitäisi oikeastaan näkyä jotenkin pelinäkymässä,
                * jotta tiedetään, että peliohjain ei ole kytketty.
                */
                System.out.println("Could not find COM port.");
                return;
        }
        
        serialTimer.stop();
        
        mainMenu.serialReady();
        try{
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                            TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }catch (Exception e) {
                System.err.println(e.toString());
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        scene = new Scene(god, 1280, 720);
        scene.setFill(Color.GREEN);
        
        //Shows only the main menu
        mainMenu = new MainMenu();
        menuScene = new Scene(mainMenu.menuItems , 1280 , 720);
        menuScene.setFill(Color.GREEN);
        primaryStage.setTitle("Lada The Ultimate Challenge 2017");
        primaryStage.setScene(menuScene);
        primaryStage.show();
        
        //Main menu button functions
        mainMenu.buttonStart.setOnMouseClicked((MouseEvent e) -> { 
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
            
            //Create objects
            createMap = new CreateMap();
            createCar = new Car();
            createMap.generateRoad();
            createCar.generateCar();
            createCar.setRoadWidth(createMap.getRoadWidth());
            createCar.setGrassWidth(createMap.getGrassWidth());

            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(!gameEnded) {
                        createCar.setRoadTopX(createMap.getRoadTopX());
                        createCar.setRoadX(createMap.getRoadX());
                        createCar.setGrassX(createMap.getGrassX());
                        createCar.setDirection(createMap.getDirection());
                        createCar.move(rotation);

                        if(createCar.getHP() == 0) {
                            try {
                                highScore = new HighscoreClient(createCar.getScore());
                            } catch (IOException ex) {
                                Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            createCar.stopTimer();
                            createMap.stopTimer();
                            gameEnded = true;
                        }
                    } else {
                        if(highScore.getButtonClicked()) {
                            highScore.closeFrame();
                            highScore = null;
                            createCar = null;
                            createMap = null;
                            primaryStage.setScene(menuScene);
                            god.getChildren().clear();
                            
                            mediaPlayer.stop();
                            timer.stop();
                            clearTimer();
                            gameEnded = false;
                            System.gc();
                        }
                    }
                }
            };
            timer.start();
            god.getChildren().addAll(createMap.groupForMap, createCar.stack);
            primaryStage.setScene(scene);
        });
        
        mainMenu.buttonScores.setOnMouseClicked((MouseEvent e) -> {
            try {
                Desktop.getDesktop().browse(new URL("http://www.students.oamk.fi/~t6kovi01/lada/LadaLeaderboards.php").toURI());
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(LadaGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        mainMenu.buttonQuit.setOnMouseClicked((MouseEvent e) -> {
            Platform.exit();
        });
	
	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(serialPort != null) {
                    serialPort.close();
                    serialPort.removeEventListener();
                }
                System.exit(0);
            } 
        });
        
        serialTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now > schedule) {
                    schedule += 10e8;
                    waitSerial();
                }
            }
        };
        serialTimer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
                                rotation = Double.parseDouble(input.readLine()) / 100 - 10;
			} catch (Exception e) {
				System.err.println(e.toString());
                        }
        }
    }
}
