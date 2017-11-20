import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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
import java.util.Enumeration;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class LadaGame extends Application implements SerialPortEventListener {
    Group god = new Group();
    Scene scene;
    public String lada = "lada.mp3";
    Media hit = new Media(new File(lada).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    SerialPort serialPort;
    //Muuta vastaamaan käytettävää porttia
    static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM37", // Windows
                        };
    BufferedReader input;
    OutputStream output;
    final int TIME_OUT = 2000;
    final int DATA_RATE = 9600;
    double rotation = 0;
    
    @Override
    public void start(Stage primaryStage) {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
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
        
        mediaPlayer.play();
        AnimationTimer timer;
        CreateMap createMap = new CreateMap();
        createMap.generateRoad();
        Car createCar = new Car();
        createCar.generateCar();
        scene = new Scene(god,createMap.sceneWidth,createMap.sceneHeight);
        scene.setFill(Color.GREEN);
        god.getChildren().addAll(createMap.groupForMap, createCar.stack);
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                createCar.setRoadX(createMap.getRoadTopX());
                createCar.setDirection(createMap.getDirection());
                createCar.move(rotation);
            }
            
        };
        
        timer.start();
        primaryStage.setTitle("Lada The Ultimate Challenge 2017");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
                                rotation = Double.parseDouble(input.readLine());
			} catch (Exception e) {
				System.err.println(e.toString());
                        }
        }
    }
}
