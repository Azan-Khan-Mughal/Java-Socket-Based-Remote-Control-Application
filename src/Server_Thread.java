import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Thread extends Thread{
    public Server_Thread(){
        start();
    }
    @Override
    public void run() {
        try {
            ServerSocket sc = new ServerSocket(Staticwaliclass.port);
            //Infinite loop
            while(true){
                //Accepting connection
                Socket client = sc.accept();
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
                if(password.equals(Staticwaliclass.password)){
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
                    if(Staticwaliclass.sending_data == false){
                        //Initiating new thread to give our pc control
                        Staticwaliclass.receivingDevice = new Receiving_Device(client);
                        Staticwaliclass.sending_data = true;
                        Staticwaliclass.updateGUI();
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
                    Staticwaliclass.sendingDevices.add(new Sending_Device(client));
                    Staticwaliclass.updateGUI();
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
