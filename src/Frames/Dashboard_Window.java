package Frames;

import AppConfig.ApplicationManager;
import FileTransfer.File_Transfer;
import Sending.Sending_Device;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Dashboard_Window {
    public static void main(String[] args) {
        new Dashboard_Window();
    }
    JButton giveButton;
    JButton fileTransferButton;
    JButton addPcButton;
    JPanel RemotePcsPanel;
    JFrame frame;


    public Dashboard_Window(){

        setupUI();
        setupEventHandlers();
        frame.setVisible(true);

    }

    void setupEventHandlers(){
        fileTransferButton.addActionListener(e -> new File_Transfer());

        giveButton.addActionListener(e-> {

            if(ApplicationManager.sending_data){
                ApplicationManager.removeRecivingDevice();
            }
            //if already connected to someone then disconnect
            else {
                new Give_Window();
            }

        });

        addPcButton.addActionListener(e-> new Add_Window());
    }

    public void updateGiveButton(){
        if(ApplicationManager.sending_data){
            giveButton.setText("Disconnect");
            giveButton.setForeground(Color.WHITE);
            giveButton.setBackground(Color.RED);
        }
        else{
            giveButton.setText("Connect");
            giveButton.setForeground(Color.WHITE);
            giveButton.setBackground(Color.GREEN);
        }
    }

    public void updateRemotePCpanel() {
        // Clear existing content
        RemotePcsPanel.removeAll();

        // Iterate through all connected sending devices
        for (Sending_Device device : ApplicationManager.sendingDevices) {

            // --- Create a container panel for each remote device ---
            JPanel devicePanel = new JPanel();
            devicePanel.setPreferredSize(new Dimension(250, 187));
            devicePanel.setLayout(null);

            // Wait until the screen receiver and image are initialized
            while (device.clientScreenReciever == null || device.clientScreenReciever.mainwali == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // --- Display the remote PC’s IP address and screenshot ---
            String clientAddress = device.client.getInetAddress().toString();

            // Scale the image preview
            Image scaledImage = device.clientScreenReciever.mainwali.getScaledInstance(
                    250, 187, Image.SCALE_AREA_AVERAGING
            );



            JLabel screenLabel = new JLabel(clientAddress);
            screenLabel.setIcon(new ImageIcon(scaledImage));
            screenLabel.setBounds(0, 0, 250, 187);

            // --- Handle click on preview image (bring its frame to front) ---
            screenLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (Sending_Device d : ApplicationManager.sendingDevices) {
                        if (d.client.getInetAddress().toString().equals(screenLabel.getText())) {
                            d.clientScreenReciever.frame.setAlwaysOnTop(true);
                            d.clientScreenReciever.frame.setAlwaysOnTop(false);
                            break;
                        }
                    }
                }
            });

            // --- Create a remove button for this remote device ---
            JButton removeButton = new JButton(clientAddress);
            removeButton.setBackground(Color.RED);
            removeButton.setBounds(100, 134, 150, 50);

            removeButton.addActionListener(e -> {
                for (Sending_Device d : ApplicationManager.sendingDevices) {
                    if (d.client.getInetAddress().toString().equals(removeButton.getText())) {
                        ApplicationManager.removeSendingDevice(d);
                        break;
                    }
                }
            });

            // --- Add components to panel ---
            devicePanel.add(removeButton);
            devicePanel.add(screenLabel);

            // --- Add this device panel to the main container ---
            RemotePcsPanel.add(devicePanel);
        }

        // --- Refresh the UI ---
        RemotePcsPanel.repaint();
        frame.repaint();
        frame.setVisible(true);
    }



    void setupUI(){

        //setting us dashboard frame
        frame = new JFrame();
        frame.getContentPane().setBackground(ApplicationManager.BACKGROUND_COLOUR);
        frame.setLayout(null);
        frame.setSize(1000,600);
        frame.setResizable(false);
        frame.setLocation(100,75);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel ipLabel;
        try {
            ipLabel = new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        JLabel portLabel = new JLabel("Port:"+ ApplicationManager.port);
        JLabel passwordLabel = new JLabel("Password: "+ ApplicationManager.password);

        ipLabel.setLocation(675,10);
        ipLabel.setSize(150,30);
        frame.add(ipLabel);

        portLabel.setLocation(825,10);
        portLabel.setSize(100,30);
        frame.add(portLabel);

        passwordLabel.setLocation(725,40);
        passwordLabel.setSize(200,30);
        frame.add(passwordLabel);


        //Setting Up and Adding File Transfer button
        fileTransferButton = new JButton("File Transfer");
        fileTransferButton.setBackground(ApplicationManager.PRIMARY_COLOUR);
        fileTransferButton.setFocusable(false);
        fileTransferButton.setForeground(ApplicationManager.SECONDARY_COLOUR);
        fileTransferButton.setSize(150,35);
        fileTransferButton.setLocation(15,15);
        frame.add(fileTransferButton);


        //Setting up panel to store remote devices
        JPanel panal = new JPanel();
        panal.setBackground(ApplicationManager.PRIMARY_COLOUR);
        panal.setSize(1000,435);
        panal.setLocation(0,75);
        panal.setLayout(null);
        frame.add(panal);


        JLabel RemotePCLable = new JLabel("Remote PCs");
        RemotePCLable.setSize(200,45);
        RemotePCLable.setFont(new Font("Serif", Font.BOLD, 25));
        RemotePCLable.setLocation(45,0);
        RemotePCLable.setForeground(ApplicationManager.SECONDARY_COLOUR);
        panal.add(RemotePCLable);

        addPcButton = new JButton("Add PC");
        addPcButton.setSize(100,30);
        addPcButton.setBackground(ApplicationManager.BACKGROUND_COLOUR);
        addPcButton.setForeground(ApplicationManager.PRIMARY_COLOUR);
        addPcButton.setFocusable(false);
        addPcButton.setLocation(875,10);
        panal.add(addPcButton);


        RemotePcsPanel = new JPanel();
        RemotePcsPanel.setBackground(ApplicationManager.SECONDARY_COLOUR);
        RemotePcsPanel.setSize(850,385);
        RemotePcsPanel.setLocation(65,50);
        RemotePcsPanel.setLayout(new FlowLayout(FlowLayout.LEFT,25,10));
        panal.add(RemotePcsPanel);




        JLabel giveAccessLabel = new JLabel("Give remote access of your pc");
        giveAccessLabel.setSize(800,50);
        giveAccessLabel.setFont(new Font("Serif", Font.BOLD, 25));
        giveAccessLabel.setLocation(542,508);
        giveAccessLabel.setForeground(ApplicationManager.PRIMARY_COLOUR);
        frame.add(giveAccessLabel);

        giveButton = new JButton("Connect");
        giveButton.setSize(100,30);
        giveButton.setFocusable(false);
        giveButton.setBackground(Color.GREEN);
        giveButton.setForeground(Color.WHITE);
        giveButton.setLocation(870,522);
        frame.add(giveButton);
    }
}
