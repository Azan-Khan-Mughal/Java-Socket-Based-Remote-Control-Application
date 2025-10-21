import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Add_Window extends JFrame {
    public static void main(String[] args) {
        new Add_Window();
    }
    TextField iptf;
    TextField ptf;
    TextField porttf;
    public Add_Window(){
        setSize(400,250);
        setLocation(300,300);
        getContentPane().setBackground(Staticwaliclass.bg);
        setLayout(null);

        iptf = new TextField();
        Label ip = new Label("IP Address");

        ip.setSize(60,20);
        iptf.setSize(120,20);

        ip.setLocation(40,25);
        iptf.setLocation(105,25);

        add(ip);
        add(iptf);

        porttf = new TextField();
        Label port = new Label("Port");

        port.setSize(60,20);
        porttf.setSize(120,20);

        port.setLocation(40,60);
        porttf.setLocation(105,60);

        add(port);
        add(porttf);



        Label p = new Label("Password");
        ptf = new TextField();

        p.setSize(60,20);
        ptf.setSize(120,20);

        p.setLocation(40,95);
        ptf.setLocation(105,95);

        add(p);
        add(ptf);
        JButton connect = new JButton("Connect");
        connect.setSize(100,20);
        connect.setLocation(115,140);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Socket sc = new Socket(iptf.getText(),Integer.parseInt(porttf.getText()));
                    OutputStream output = sc.getOutputStream();
                    PrintWriter writer = new PrintWriter(output);

                    InputStream input = sc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    writer.println(ptf.getText());
                    writer.flush();

                    System.out.println("password writed"+ ptf.getText());

                    String response = reader.readLine();
                    System.out.println("recived responce " + response);
                    if(response.equals("YES")){
                        writer.println("1");
                        writer.flush();

                        System.out.println("Writed 1");
                        Staticwaliclass.sendingDevices.add(new Sending_Device(sc));
                        Staticwaliclass.updateGUI();
                    }
                    else{
                        System.out.println("ja kam kr apnna");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: Can't establish connection.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
        add(connect);


        setVisible(true);

    }
}
