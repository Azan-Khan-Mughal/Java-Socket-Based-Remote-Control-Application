package Sending;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Sending_Device extends Thread {


    JInternalFrame interFrame = new JInternalFrame("Client Screen",true, true, true);
    public Socket client;
    JFrame frame;
    public ClientScreenReciever clientScreenReciever;
    public ClientCommandsSender clientCommandsSender;
    public Sending_Device(Socket client) {

        this.client = client;
        frame= new JFrame();
        frame.setVisible(true);
        start();
    }

    public void run(){

        //used to represent client screen size
        Rectangle clientScreenDim = null;
        //Used to read screenshots and client screen dimension
        ObjectInputStream ois = null;
        //start drawing GUI

        try{
            //Read client screen dimension
            ois = new ObjectInputStream(client.getInputStream());
            clientScreenDim =(Rectangle) ois.readObject();
        }catch(Exception e){
            ////
        }
        //Start recieveing screenshots
        clientScreenReciever = new ClientScreenReciever(ois,frame);
        //Start sending events to the client
        clientCommandsSender = new ClientCommandsSender(client,frame,clientScreenDim);
    }

}


/*

    PRESS_MOUSE(-1),
    RELEASE_MOUSE(-2),
    PRESS_KEY(-3),
    RELEASE_KEY(-4),
    MOVE_MOUSE(-5);

   



*/