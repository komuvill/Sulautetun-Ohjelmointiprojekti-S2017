package highscoretester;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.*;
import java.net.*;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HighscoreClient extends JFrame{

    String nimi;
    int pisteet;
    String dataString;
    Socket clientSocket;
    JButton submitButton;
    JLabel scoreLabel, nameLabel;
    JTextField nameField;
    JPanel panel;
    DataOutputStream toServer;
    
    public HighscoreClient(int pelaajanPisteet) throws IOException{

        this.setSize(250,250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("High Score Submitter");
        
        pisteet = pelaajanPisteet;
        scoreLabel = new JLabel("Your score was: " + pisteet);
        nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(25);
        submitButton = new JButton("Submit score!");
        panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        
        panel.add(scoreLabel, gbc);
        gbc.gridy++;
        panel.add(nameLabel, gbc);
        gbc.gridy++;
        panel.add(submitButton, gbc);
        gbc.gridy = 1;
        gbc.gridx = 1;
        panel.add(nameField,gbc);

        ListenForButton submitClicked = new ListenForButton();
        submitButton.addActionListener(submitClicked);
        
        clientSocket = new Socket("10.4.3.6", 20000); //Tähän Raspberryn IP
        toServer = new DataOutputStream(clientSocket.getOutputStream());
        
        this.add(panel);
        this.pack();
        this.setVisible(true);
        nameField.requestFocus();
        
    }
    
    private class ListenForButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == submitButton){
                nimi = nameField.getText();
                dataString = nimi + "," + Integer.toString(pisteet);
                try {
                    toServer.writeUTF(dataString);
                } catch (IOException ex) {
                    Logger.getLogger(HighscoreClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}


