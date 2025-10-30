package Frames;

import AppConfig.ApplicationManager;
import Sending.Sending_Device;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Add_Window extends JFrame {

    // --- UI Components ---
    private TextField ipField;
    private TextField portField;
    private TextField passwordField;
    private JButton connectButton;


    // --- Main for testing ---
    public static void main(String[] args) {
        new Add_Window();
    }


    // --- Constructor ---

    public Add_Window() {
        setUpUI();
        setupEventHandlers();
        setVisible(true);
    }

    void setupEventHandlers(){
        connectButton.addActionListener(e -> connectToRemotePC());
    }

    // --- Core Connection Logic ---
    private void connectToRemotePC() {
        try {
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            String password = passwordField.getText().trim();

            Socket socket = new Socket(ip, port);

            // Setup input/output streams
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send password to verify connection
            writer.println(password);
            System.out.println("Password sent: " + password);

            String response = reader.readLine();
            System.out.println("Received response: " + response);

            // If accepted, send handshake type "1"
            if ("YES".equals(response)) {
                writer.println("1");
                writer.flush();

                System.out.println("Sent code 1 - starting Sending_Device");
                ApplicationManager.sendingDevices.add(new Sending_Device(socket));
                ApplicationManager.updateGUI();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect password or connection rejected.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE
                );
                socket.close();
            }

        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Unable to establish connection.",
                    "Connection Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            dispose();
        }
    }




    // --- Setup UI layout and event handling ---
    private void setUpUI() {
        setTitle("Add Remote PC");
        setSize(400, 250);
        setLocation(300, 300);
        getContentPane().setBackground(ApplicationManager.BACKGROUND_COLOUR);
        setLayout(null);
        setResizable(false);

        // --- IP Address ---
        Label ipLabel = new Label("IP Address:");
        ipField = new TextField();

        ipLabel.setBounds(40, 25, 80, 20);
        ipField.setBounds(130, 25, 200, 20);

        add(ipLabel);
        add(ipField);

        // --- Port ---
        Label portLabel = new Label("Port:");
        portField = new TextField();

        portLabel.setBounds(40, 60, 80, 20);
        portField.setBounds(130, 60, 200, 20);

        add(portLabel);
        add(portField);

        // --- Password ---
        Label passwordLabel = new Label("Password:");
        passwordField = new TextField();

        passwordLabel.setBounds(40, 95, 80, 20);
        passwordField.setBounds(130, 95, 200, 20);

        add(passwordLabel);
        add(passwordField);

        // --- Connect Button ---
        connectButton = new JButton("Connect");
        connectButton.setBounds(140, 140, 100, 30);
        connectButton.setBackground(ApplicationManager.PRIMARY_COLOUR);
        connectButton.setForeground(ApplicationManager.SECONDARY_COLOUR);
        connectButton.setFocusable(false);


        add(connectButton);
    }


}
