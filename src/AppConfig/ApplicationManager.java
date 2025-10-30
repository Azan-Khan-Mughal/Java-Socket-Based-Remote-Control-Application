package AppConfig;

import Receiving.*;
import Sending.*;
import Frames.*;
import Server.Server;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public final class ApplicationManager {
    public final static Color BACKGROUND_COLOUR = new Color(69,69,69);
    public final static Color PRIMARY_COLOUR = new Color(25,25,25);
    public final static Color SECONDARY_COLOUR = new Color(100,100,100);
    public static Server server;

    public static int port;
    public static String password;


    public static ArrayList<Sending_Device> sendingDevices = new ArrayList<>();
    public static Dashboard_Window d;
    public static Receiving_Device receivingDevice;
    public static  boolean sending_data = false;
    public static int terminatingSending = 696969;
    public static byte[] terminatingReciving = {1,2,3};
    public ApplicationManager(Dashboard_Window d){
        this.d = d;
    }
    public static void updateGUI(){
        d.updateGiveButton();
        d.updateRemotePCpanel();
    }
    public static void removeRecivingDevice(){
        for(int i=0;i<20;i++) {
            System.out.println("Reciver termination executed*******************************************************");
        }
        try {
            receivingDevice.screenSender.oos.writeObject(terminatingReciving);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        receivingDevice.serverCmdExecution.continueLoop = false;
        receivingDevice.screenSender.continueLoop = false;
        receivingDevice.serverCmdExecution = null;
        receivingDevice.screenSender = null;
        receivingDevice = null;
        sending_data = false;
        updateGUI();
    }
    public static void removeSendingDevice(Sending_Device sendingDevice){
        for(int i=0;i<20;i++) {
            System.out.println("Sender termination executed*******************************************************");
        }
        sendingDevice.clientCommandsSender.writer.println(terminatingSending);
        sendingDevice.clientCommandsSender.writer.flush();

        sendingDevice.clientCommandsSender.frame.removeAll();
        sendingDevice.clientCommandsSender.frame.dispose();
        sendingDevice.clientCommandsSender = null;

        sendingDevice.clientScreenReciever.continueLoop = false;
        sendingDevice.clientScreenReciever = null;
        sendingDevices.remove(sendingDevice);
        sendingDevice = null;
        updateGUI();
    }
    public static void removeSendingDevice(ClientScreenReciever clientScreenReciever){
        boolean found = false;
        Sending_Device sendingDevice = null;
        for(Sending_Device i : sendingDevices){
            if(i.clientScreenReciever == clientScreenReciever){
                found = true;
                sendingDevice = i;
            }

        }
        if(found){
            removeSendingDevice(sendingDevice);
        }
    }
}
