package rotationdemo;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 
import java.util.Enumeration;

public class SerialRotationDemo extends JFrame implements SerialPortEventListener {

    JLabel serialLabel;
    SerialPort serialPort;
    //Muuta näitä vastaamaan porttia, jossa Arduino on.
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
    
    float rotation = 0;
  
    public SerialRotationDemo(){
        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Rotation Demo");
        
        JPanel thePanel = new JPanel();
        
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
        if (portId == null) {
                System.out.println("Could not find COM port.");
                return;
        }

        try {
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
        } catch (Exception e) {
                System.err.println(e.toString());
        }
        
        serialLabel = new JLabel("This is a label");
        thePanel.add(serialLabel);

        this.add(thePanel);
        this.setVisible(true);
    }

    @Override
    public void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
                                rotation = Float.parseFloat(input.readLine());
                                System.out.println("Rotation: " +rotation);
                                if(rotation < 0){
                                    serialLabel.setText("Vasemmalle!");
                                }else if(rotation > 0){
                                    serialLabel.setText("Oikealle!");
                                }
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
        }
}
