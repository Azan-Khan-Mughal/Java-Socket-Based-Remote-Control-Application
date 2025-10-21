import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

class Receiving_Device{
    Socket s;
    Robot robot ; //Used to capture the screen
    Rectangle rectangle;
    ScreenSender screenSender;
    ServerCmdExecution serverCmdExecution;

    public Receiving_Device(Socket s){
        this.s = s;
        try {
            //Get screen dimensions
            // dimension n rectangel class is present in awt package
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            rectangle = new Rectangle(dim);

            //Prepare Robot object
            robot = new Robot();


            //ScreenSender sends screenshots of the client screen
            screenSender = new ScreenSender(s,robot,rectangle);


            //ServerCmdExecution recieves server commands and execute them
            serverCmdExecution = new ServerCmdExecution(s,robot);
        }
        catch (Exception ex) {
            ///
        }
    }

}







//send sshots
class ScreenSender extends Thread {

    Socket socket ;
    Robot robot; // Used to capture screen
    Rectangle rectangle; //Used to represent screen dimensions
    boolean continueLoop = true; //Used to exit the program
    ObjectOutputStream oos = null ;

    public ScreenSender(Socket socket, Robot robot,Rectangle rect) {
        this.socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();	//to start the thread
    }

    // overriding run() of Thread class
    public void run()
    {
         //Used to write an object to the streem


        try{
            //Prepare ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            /*
             * Send screen size to the server in order to calculate correct mouse
             * location on the server's panel
             */
            oos.writeObject(rectangle);
        }catch(Exception ex){
            ////
        }

        while(continueLoop){
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
            }
            catch (Exception ex) {
                ////
            }

            //wait for 100ms to reduce network traffic
            try{
                Thread.sleep(17);
            }
            catch(Exception e){
                /////
            }
        }
    }
}



class ServerCmdExecution extends Thread {

    Socket socket = null;
    Robot robot = null;
    boolean continueLoop = true;

    public ServerCmdExecution(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
        start(); //Start the thread and hence calling run method
    }

    public void run(){
        Scanner scanner = null;
        try {
            //prepare Scanner object
            System.out.println("Preparing InputStream");
            scanner = new Scanner(socket.getInputStream());

            while(continueLoop){
                //recieve commands and respond accordingly
                System.out.println("Waiting for command");
                int command = scanner.nextInt();
                if(command == Staticwaliclass.terminatingSending){
                    System.out.println("mil gaya 6969696969669696969669696969696969696969696996969696");
                    Staticwaliclass.removeRecivingDevice();
                }
                System.out.println("New command: " + command);
                switch(command){
                    case -1:
                        robot.mousePress(scanner.nextInt());
                        break;
                    case -2:
                        robot.mouseRelease(scanner.nextInt());
                        break;
                    case -3:
                        robot.keyPress(scanner.nextInt());
                        break;
                    case -4:
                        robot.keyRelease(scanner.nextInt());
                        break;
                    case -5:
                        robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                        break;
                }
            }
        } catch (Exception e) {
            ///
        }
    }

}