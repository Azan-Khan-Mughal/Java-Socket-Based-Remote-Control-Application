package Frames;

import AppConfig.ApplicationManager;
import Server.Server;

import javax.swing.*;
import java.awt.*;

public class InitialWindow extends JFrame {
    public static void main(String[] args) {
        new InitialWindow();
    }

    TextField portTextField;
    TextField passwordTextField;
    JButton changeButton;

    public InitialWindow(){

        setup();

        changeButton.addActionListener(e->{

            try{
                Server server = new Server(portTextField.getText());
                int port= Integer.parseInt(portTextField.getText());


                // Saving port and password
                ApplicationManager.server = server;
                ApplicationManager.port = Integer.parseInt(portTextField.getText());
                ApplicationManager.password = passwordTextField.getText();
                dispose();

                //Initiating main controller class and Dashboard
                new ApplicationManager(new Dashboard_Window());


            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        });


    }

    public void setup(){
        setSize(400,250);
        setLocation(300,300);
        getContentPane().setBackground(ApplicationManager.BACKGROUND_COLOUR);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        portTextField = new TextField();
        Label portLabel = new Label("Port");

        portLabel.setSize(60,20);
        portTextField.setSize(120,20);

        portLabel.setLocation(40,40);
        portTextField.setLocation(105,40);

        add(portLabel);
        add(portTextField);

        Label passwordLabel = new Label("Password");
        passwordTextField = new TextField();

        passwordLabel.setSize(60,20);
        passwordTextField.setSize(120,20);

        passwordLabel.setLocation(40,80);
        passwordTextField.setLocation(105,80);

        add(passwordLabel);
        add(passwordTextField);

        changeButton = new JButton("Change");
        changeButton.setSize(100,20);
        changeButton.setLocation(115,120);

        add(changeButton);
        setVisible(true);
    }

}
