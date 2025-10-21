package Sending;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientCommandsSender implements KeyListener,
        MouseMotionListener, MouseListener {

    Socket s;
    public JFrame frame;
    public PrintWriter writer;
    Rectangle r;

    ClientCommandsSender(Socket s, JFrame frame, Rectangle r) {

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
        double xScale = r.getWidth() / frame.getWidth();
        System.out.println("xScale: " + xScale);
        double yScale = r.getHeight() / frame.getHeight();
        System.out.println("yScale: " + yScale);
        System.out.println("Mouse Moved");
        writer.println(-5);
        writer.println((int) (e.getX() * xScale));
        writer.println((int) (e.getY() * yScale));
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
