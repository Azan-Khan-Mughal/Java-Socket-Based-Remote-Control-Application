package Sending;

import AppConfig.ApplicationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientScreenReciever extends Thread {
    public Image mainwali = null;
    ObjectInputStream ois;
    public JFrame frame;
    public boolean continueLoop = true;
    Socket s;

    public ClientScreenReciever(ObjectInputStream ois, JFrame frame) {
        this.ois = ois;
        this.frame = frame;
        //start the thread and thus call the run method
        start();
    }

    public void run() {

        try {

            //Read screenshots of the client then draw them
            while (continueLoop) {
                //Recieve client screenshot and resize it to the current panel size
                try {
                    int a;
                    byte[] imagedata = (byte[]) ois.readObject();
                    if (Arrays.equals(imagedata, ApplicationManager.terminatingReciving)) {
                        System.out.println("rtyuiiuytrtyuiuytrtyujjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
                        ApplicationManager.removeSendingDevice(this);
                    }
                    System.out.println("New image recieved");
                    ByteArrayInputStream bais = new ByteArrayInputStream(imagedata);
                    Image image = ImageIO.read(bais);
                    if (mainwali == null && image != null) {
                        mainwali = image;
                    }
                    image = image.getScaledInstance(frame.getWidth(), frame.getHeight()
                            , Image.SCALE_SMOOTH);
                    //Draw the recieved screenshot
                    Graphics graphics = frame.getGraphics();
                    graphics.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), frame);
                } catch (Exception e) {
                    int r = ois.read();
                    if (r == 696969) {
                        ApplicationManager.removeSendingDevice(this);
                    }
                }

            }
        } catch (Exception e) {
            ////
        }
    }
}
