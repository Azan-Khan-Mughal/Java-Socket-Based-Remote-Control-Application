package Receiving;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

//send sshots
public class ScreenSender extends Thread {

    Socket socket;
    Robot robot; // Used to capture screen
    Rectangle rectangle; //Used to represent screen dimensions
    public boolean continueLoop = true; //Used to exit the program
    public ObjectOutputStream oos = null;

    public ScreenSender(Socket socket, Robot robot, Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();    //to start the thread
    }

    // overriding run() of Thread class
    public void run() {
        //Used to write an object to the streem


        try {
            //Prepare ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            /*
             * Send screen size to the server in order to calculate correct mouse
             * location on the server's panel
             */
            oos.writeObject(rectangle);
        } catch (Exception ex) {
            ////
        }

        while (continueLoop) {
            //Capture screen
            BufferedImage image = robot.createScreenCapture(rectangle);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "JPEG", baos); // Compress to JPEG format, you can also use "PNG"

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] imageData = baos.toByteArray();

            // we acnnot send BufferedImage in stream
            // so we converted it to image

            //Send captured screen to the server
            try {
                System.out.println("before sending image");
                oos.writeObject(imageData);
                oos.reset(); //Clear ObjectOutputStream cache
                System.out.println("New screenshot sent");
            } catch (Exception ex) {
                ////
            }

            //wait for 100ms to reduce network traffic
            try {
                Thread.sleep(17);
            } catch (Exception e) {
                /////
            }
        }
    }
}
