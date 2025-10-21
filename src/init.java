import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class init extends JFrame {
    public static void main(String[] args) {
        new init();
    }

    TextField portTextField;
    TextField passwordTextField;

    public init(){
        setSize(400,250);
        setLocation(300,300);
        getContentPane().setBackground(Staticwaliclass.bg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        portTextField = new TextField();
        Label portLabel = new Label("Port");

        portLabel.setSize(60,20);
        portTextField.setSize(120,20);

        portLabel.setLocation(40,40);
        portTextField.setLocation(105,40);

        add(portLabel);
        add(portTextField);

        Label p = new Label("Password");
        passwordTextField = new TextField();

        p.setSize(60,20);
        passwordTextField.setSize(120,20);

        p.setLocation(40,80);
        passwordTextField.setLocation(105,80);

        add(p);
        add(passwordTextField);

        JButton change = new JButton("Change");
        change.setSize(100,20);
        change.setLocation(115,120);
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                try{
                     int a= Integer.parseInt(portTextField.getText());
                    ServerSocket ss = new ServerSocket(a);
                    Socket s = new Socket("localhost",a);
                    ss.close();
                    s.close();
                    ss=null;
                    s=null;


                    Staticwaliclass.port = Integer.parseInt(portTextField.getText());
                    Staticwaliclass.password = passwordTextField.getText();
                    dispose();
                    //Initiating main controller class and Dashboard
                    new Staticwaliclass(new Dashbord());
                    //Initiating Server thread
                    new Server_Thread();
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: Wrong Inputs.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: port not listening.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: port number not valid.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(change);
        setVisible(true);

    }
}
