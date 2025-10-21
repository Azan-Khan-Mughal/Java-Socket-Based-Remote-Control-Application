package Receiving;

import java.awt.*;
import java.net.Socket;

public class Receiving_Device{
    Socket s;
    Robot robot ; //Used to capture the screen
    Rectangle rectangle;
    public ScreenSender screenSender;
    public ServerCmdExecution serverCmdExecution;

    public Receiving_Device(Socket s){
        this.s = s;
        try {
            //Get screen dimensions
            // dimension n rectangel class is present in awt package
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            rectangle = new Rectangle(dim);

            //Prepare Robot object
            robot = new Robot();


            //Receiving.ScreenSender sends screenshots of the client screen
            screenSender = new ScreenSender(s,robot,rectangle);


            //Receiving.ServerCmdExecution recieves server commands and execute them
            serverCmdExecution = new ServerCmdExecution(s,robot);
        }
        catch (Exception ex) {
            ///
        }
    }

}


