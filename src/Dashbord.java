import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class Dashbord {
    public static void main(String[] args) {
        new Dashbord();
    }
    JButton give;
    JPanel RemotePCPanel;
    JFrame frame;
    public Dashbord(){




        //setting us dashboard frame
        frame = new JFrame();
        frame.getContentPane().setBackground(Staticwaliclass.bg);
        frame.setLayout(null);
        frame.setSize(1000,600);
        frame.setResizable(false);
        frame.setLocation(100,75);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel IP;
        try {
            IP = new JLabel("IP: " + InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        JLabel port = new JLabel("Port:"+ Staticwaliclass.port);
        JLabel password = new JLabel("Password: "+ Staticwaliclass.password);

        IP.setLocation(725,10);
        IP.setSize(100,30);
        frame.add(IP);

        port.setLocation(825,10);
        port.setSize(100,30);
        frame.add(port);

        password.setLocation(725,40);
        password.setSize(200,30);
        frame.add(password);


        //Setting Up and Adding File Transfer button
        JButton FileTransfer = new JButton("File Transfer");
        FileTransfer.setBackground(Staticwaliclass.primary);
        FileTransfer.setFocusable(false);
        FileTransfer.setForeground(Staticwaliclass.secondary);
        FileTransfer.setSize(150,35);
        FileTransfer.setLocation(15,15);
        FileTransfer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new File_Transfer();
            }
        });
        frame.add(FileTransfer);


        //Setting up panel to store remote devices
        JPanel panal = new JPanel();
        panal.setBackground(Staticwaliclass.primary);
        panal.setSize(1000,435);
        panal.setLocation(0,75);
        panal.setLayout(null);
        frame.add(panal);


        JLabel RemotePCLable = new JLabel("Remote PCs");
        RemotePCLable.setSize(200,45);
        RemotePCLable.setFont(new Font("Serif", Font.BOLD, 25));
        RemotePCLable.setLocation(45,0);
        RemotePCLable.setForeground(Staticwaliclass.secondary);
        panal.add(RemotePCLable);

        JButton Add = new JButton("Add PC");
        Add.setSize(100,30);
        Add.setBackground(Staticwaliclass.bg);
        Add.setForeground(Staticwaliclass.primary);
        Add.setFocusable(false);
        Add.setLocation(875,10);
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Add_Window();
            }
        });
        panal.add(Add);


        RemotePCPanel = new JPanel();
        RemotePCPanel.setBackground(Staticwaliclass.secondary);
        RemotePCPanel.setSize(850,385);
        RemotePCPanel.setLocation(65,50);
        RemotePCPanel.setLayout(new FlowLayout(FlowLayout.LEFT,25,10));
        panal.add(RemotePCPanel);




        JLabel GiveAccessLabel = new JLabel("Give remote access of your pc");
        GiveAccessLabel.setSize(800,50);
        GiveAccessLabel.setFont(new Font("Serif", Font.BOLD, 25));
        GiveAccessLabel.setLocation(542,508);
        GiveAccessLabel.setForeground(Staticwaliclass.primary);
        frame.add(GiveAccessLabel);

        give = new JButton("Connect");
        give.setSize(100,30);
        give.setFocusable(false);
        give.setBackground(Color.GREEN);
        give.setForeground(Color.WHITE);
        give.setLocation(870,522);
        give.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(Staticwaliclass.sending_data == false){
                    new Give_Window();
                }
                //if already connected to someone then disconnect
                else {
                    Staticwaliclass.removeRecivingDevice();
                }

            }
        });
        frame.add(give);


        frame.setVisible(true);
    }
    public void updateGiveButton(){
        if(Staticwaliclass.sending_data){
            give.setText("Disconnect");
            give.setForeground(Color.WHITE);
            give.setBackground(Color.RED);
        }
        else{
            give.setText("Connect");
            give.setForeground(Color.WHITE);
            give.setBackground(Color.GREEN);
        }
    }
    public void updateRemotePCpanel(){
        RemotePCPanel.removeAll();

        for(Sending_Device i : Staticwaliclass.sendingDevices){

            JPanel jp = new JPanel();
            jp.setPreferredSize(new Dimension(250,187));
            jp.setLayout(null);

            while(i.clientScreenReciever == null || i.clientScreenReciever.mainwali == null){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            JLabel label = new JLabel(i.client.getInetAddress().toString());
            Image image = i.clientScreenReciever.mainwali;
            image = image.getScaledInstance(250,187,Image.SCALE_AREA_AVERAGING);
            ImageIcon icon = new ImageIcon(image) ;
            label.setIcon(icon);
            label.setBounds(0,0,250,187);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Sending_Device temp = null;
                    boolean found =false;
                    for(Sending_Device i : Staticwaliclass.sendingDevices){
                        if(i.client.getInetAddress().toString().equals(label.getText())){
                            temp = i;
                            found = true;
                            break;
                        }
                    }
                    if(found){
                        temp.clientScreenReciever.frame.setAlwaysOnTop(true);
                        temp.clientScreenReciever.frame.setAlwaysOnTop(false);
                    }
                }
            });

            JButton b = new JButton(i.client.getInetAddress().toString());
            b.setSize(50,50);
            b.setLocation(200,134);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Sending_Device temp = null;
                    boolean found =false;
                    for(Sending_Device i : Staticwaliclass.sendingDevices){
                        if(i.client.getInetAddress().toString().equals(b.getText())){
                            temp = i;
                            found = true;
                            break;
                        }
                    }
                    if(found){
                        Staticwaliclass.removeSendingDevice(temp);
                    }
                }
            });
            jp.add(b);
            jp.add(label);
            this.RemotePCPanel.add(jp);

        }
        RemotePCPanel.repaint();
        frame.repaint();
        frame.setVisible(true);

    }
}
