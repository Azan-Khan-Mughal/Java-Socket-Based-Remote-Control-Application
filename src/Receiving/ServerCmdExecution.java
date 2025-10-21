package Receiving;

import AppConfig.ApplicationManager;

import java.awt.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerCmdExecution extends Thread {

    Socket socket = null;
    Robot robot = null;
    public boolean continueLoop = true;

    public ServerCmdExecution(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
        start(); //Start the thread and hence calling run method
    }

    public void run() {
        Scanner scanner = null;
        try {
            //prepare Scanner object
            System.out.println("Preparing InputStream");
            scanner = new Scanner(socket.getInputStream());

            while (continueLoop) {
                //recieve commands and respond accordingly
                System.out.println("Waiting for command");
                int command = scanner.nextInt();
                if (command == ApplicationManager.terminatingSending) {
                    System.out.println("mil gaya 6969696969669696969669696969696969696969696996969696");
                    ApplicationManager.removeRecivingDevice();
                }
                System.out.println("New command: " + command);
                switch (command) {
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
