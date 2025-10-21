import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;

class Sending_Device extends Thread {


    JInternalFrame interFrame = new JInternalFrame("Client Screen",true, true, true);
    Socket client;
    JFrame frame;
    ClientScreenReciever clientScreenReciever;
    ClientCommandsSender clientCommandsSender;
    Sending_Device(Socket client) {

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



class ClientScreenReciever extends Thread {
    Image mainwali = null;
    ObjectInputStream ois;
    JFrame frame;
    boolean continueLoop = true;
    Socket s;
    public ClientScreenReciever(ObjectInputStream ois, JFrame frame) {
        this.ois = ois;
        this.frame = frame;
        //start the thread and thus call the run method
        start();
    }

    public void run(){

        try {

            //Read screenshots of the client then draw them
            while(continueLoop){
                //Recieve client screenshot and resize it to the current panel size
                try{
                    int a;
                    byte[] imagedata = (byte[]) ois.readObject();
                    if(Arrays.equals(imagedata,Staticwaliclass.terminatingReciving)){
                        System.out.println("rtyuiiuytrtyuiuytrtyujjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
                        Staticwaliclass.removeSendingDevice(this);
                    }
                    System.out.println("New image recieved");
                    ByteArrayInputStream bais = new ByteArrayInputStream(imagedata);
                    Image image = ImageIO.read(bais);
                    if(mainwali == null && image != null){
                        mainwali = image;
                    }
                    image = image.getScaledInstance(frame.getWidth(),frame.getHeight()
                            ,Image.SCALE_SMOOTH);
                    //Draw the recieved screenshot
                    Graphics graphics = frame.getGraphics();
                    graphics.drawImage(image, 0, 0, frame.getWidth(),frame.getHeight(),frame);
                }catch (Exception e){
                    int r = ois.read();
                    if(r == 696969){
                        Staticwaliclass.removeSendingDevice(this);
                    }
                }

            }
        } catch (Exception e) {
            ////
        }
    }
}



class ClientCommandsSender implements KeyListener,
        MouseMotionListener, MouseListener {

    Socket s;
    JFrame frame;
    PrintWriter writer ;
    Rectangle r ;

    ClientCommandsSender( Socket s ,JFrame frame, Rectangle r) {

        this.frame = frame;
        this.r = r;
        //Associate event listners to the panel
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        try {
            //Prepare PrintWriter which will be used to send commands to
            //the client
            writer = new PrintWriter(s.getOutputStream());
        } catch (Exception e) {
            /////
        }

    }

    //Not implemeted yet
    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {

        // we find ratio between client screen n server screen size by dividing them
        double xScale = r.getWidth()/frame.getWidth();
        System.out.println("xScale: " + xScale);
        double yScale = r.getHeight()/frame.getHeight();
        System.out.println("yScale: " + yScale);
        System.out.println("Mouse Moved");
        writer.println(-5);
        writer.println((int)(e.getX() * xScale));
        writer.println((int)(e.getY() * yScale));
        writer.flush();
    }

    //this is not implemented
    public void mouseClicked(MouseEvent e) {
    }


    /*	for getButton() methiod left mouse click = 1
                                    right mouse click = 3

            for robot class left mouse click = 16
                            right mouse click = 4

                            */
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed");
        writer.println(-1);
        int button = e.getButton();
        //first we assume left button is clicked
        int xButton = 16;
        if (button == 3) // if right button is clciked
        {
            xButton = 4;
        }

        // xbutton is value used to tell robot class which mouse button is pressed
        writer.println(xButton);
        writer.flush();
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released");
        writer.println(-2);
        int button = e.getButton();
        System.out.println(button);
        int xButton = 16;
        if (button == 3) {
            xButton = 4;
        }
        writer.println(xButton);
        writer.flush();
    }

    //not implemented
    public void mouseEntered(MouseEvent e) {
    }

    //not implemented
    public void mouseExited(MouseEvent e) {

    }

    //not implemented
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("Key Pressed");
        writer.println(-3);
        writer.println(e.getKeyCode());
        writer.flush();
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("Mouse Released");
        writer.println(-4);
        writer.println(e.getKeyCode());
        writer.flush();
    }

}

/*

    PRESS_MOUSE(-1),
    RELEASE_MOUSE(-2),
    PRESS_KEY(-3),
    RELEASE_KEY(-4),
    MOVE_MOUSE(-5);

   



*/