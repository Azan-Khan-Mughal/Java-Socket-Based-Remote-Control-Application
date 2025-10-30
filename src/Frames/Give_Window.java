package Frames;

import AppConfig.ApplicationManager;
import Receiving.Receiving_Device;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

/**
 * The GiveWindow allows the user to connect to a remote machine
 * by entering IP address, port, and password.
 */
public class Give_Window extends JFrame {

    private TextField ipField;
    private TextField portField;
    private TextField passwordField;
    private JButton connectButton;

    // Main method (for testing)
    public static void main(String[] args) {
        new Give_Window();
    }

    // Constructor
    public Give_Window() {
        setupUI();
        setupEventHandlers();
        setVisible(true);
    }

    /**
     * Sets up the UI components and layout.
     */
    private void setupUI() {
        setTitle("Connect to Remote PC");
        setSize(400, 250);
        setLocation(300, 300);
        setLayout(null);
        getContentPane().setBackground(ApplicationManager.BACKGROUND_COLOUR);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- IP Address Field ---
        Label ipLabel = new Label("IP Address:");
        ipLabel.setBounds(40, 25, 80, 20);
        add(ipLabel);

        ipField = new TextField();
        ipField.setBounds(130, 25, 200, 20);
        add(ipField);

        // --- Port Field ---
        Label portLabel = new Label("Port:");
        portLabel.setBounds(40, 60, 80, 20);
        add(portLabel);

        portField = new TextField();
        portField.setBounds(130, 60, 200, 20);
        add(portField);

        // --- Password Field ---
        Label passwordLabel = new Label("Password:");
        passwordLabel.setBounds(40, 95, 80, 20);
        add(passwordLabel);

        passwordField = new TextField();
        passwordField.setEchoChar('*');
        passwordField.setBounds(130, 95, 200, 20);
        add(passwordField);

        // --- Connect Button ---
        connectButton = new JButton("Connect");
        connectButton.setBounds(150, 140, 100, 30);
        connectButton.setFocusable(false);
        connectButton.setBackground(Color.GREEN);
        connectButton.setForeground(Color.WHITE);
        add(connectButton);
    }

    /**
     * Sets up all button event handlers.
     */
    private void setupEventHandlers() {
        connectButton.addActionListener(this::onConnectClicked);
    }

    /**
     * Handles the connect button click event.
     */
    private void onConnectClicked(ActionEvent e) {
        try {
            // Establish connection
            String ip = ipField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            String password = passwordField.getText().trim();

            Socket socket = new Socket(ip, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send password
            writer.println(password);
            System.out.println("Password sent: " + password);

            // Wait for server response
            String response = reader.readLine();
            System.out.println("Received response: " + response);

            if ("YES".equalsIgnoreCase(response)) {
                writer.println("2");
                writer.flush();

                System.out.println("Sent command: 2");
                ApplicationManager.receivingDevice = new Receiving_Device(socket);
                ApplicationManager.sending_data = true;
                ApplicationManager.updateGUI();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Authentication failed. Please check password.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
                socket.close();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: Can't establish connection.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid port number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose();
        }
    }
}
