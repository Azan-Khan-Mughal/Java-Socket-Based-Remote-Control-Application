package Server;

import AppConfig.ApplicationManager;
import FileTransfer.File_Receiving;
import Receiving.Receiving_Device;
import Sending.Sending_Device;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Thread extends Thread{
    ServerSocket serverSocket;
    public Server_Thread(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
        start();
    }
    @Override
    public void run() {
        try {
            while(true){
                //Accepting connection
                Socket client = this.serverSocket.accept();
                System.out.println("Some one requested");


                //Creating Reading Objects
                InputStream input = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                //Creating Writing object
                OutputStream output = client.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                //Waiting for password
                System.out.println("waiting");
                String password = reader.readLine();
                System.out.println("password recived " + password);

                //Checking Validity of password.
                if(password.equals(ApplicationManager.password)){
                    //Sending response yes
                    writer.println("YES");
                }
                else{
                    //Sending response NO and terminating connection
                    writer.println("NO");
                    client.close();
                    continue;
                }

                //Reading type of connection other PC wants
                System.out.println("Reading type");
                String type = reader.readLine();
                System.out.println("recived type " + type);


                //1 means he wants our pc control
                if(type.equals("1")){
                    //Checking if some other PC already have our PC control
                    if(ApplicationManager.sending_data == false){
                        //Initiating new thread to give our pc control
                        ApplicationManager.receivingDevice = new Receiving_Device(client);
                        ApplicationManager.sending_data = true;
                        ApplicationManager.updateGUI();
                    }
                    else{
                        //Closing connection with an error
                        JOptionPane.showMessageDialog(null,
                                "Error: You are already connected to some one.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        writer.println("NO");
                        client.close();
                    }
                }
                //Other PC wants to give his access to US
                else if (type.equals("2")) {
                    //Initiating new thread to take other pc control
                    ApplicationManager.sendingDevices.add(new Sending_Device(client));
                    ApplicationManager.updateGUI();
                }
                //Other pc wants to deliver a file
                else if (type.equals("3")) {
                    new File_Receiving(client);
                }
            }
        } catch (IOException e) {
            ////
        }
    }
}
