import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class File_Transfer extends JFrame {
    public static void main(String[] args) {
        new File_Transfer();
    }
    File file = null;
    TextField iptf;
    TextField ptf;
    TextField porttf;
    JFileChooser f;
    JLabel b;
    public File_Transfer(){
        setSize(400,250);
        setLocation(300,300);
        getContentPane().setBackground(Staticwaliclass.bg);
        setLayout(null);


        f = new JFileChooser();
        f.setDialogTitle("niggas");

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

        JButton connect = new JButton("Send");
        connect.setSize(100,20);
        connect.setLocation(115,130);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(file == null){
                    JOptionPane.showMessageDialog(null,
                            "Error: No File Selected.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Socket sc = new Socket(iptf.getText(), Integer.parseInt(porttf.getText()));
                    OutputStream output = sc.getOutputStream();
                    PrintWriter writer = new PrintWriter(output);

                    InputStream input = sc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    writer.println(ptf.getText());
                    writer.flush();

                    System.out.println("password writed" + ptf.getText());

                    String response = reader.readLine();
                    System.out.println("recived responce " + response);
                    if (response.equals("YES")) {
                        writer.println("3");
                        writer.flush();

                        new File_Sending(sc, file);
                    } else {
                        System.out.println("ja kam kr apnna");
                    }
                }catch (IOException ex){
                    JOptionPane.showMessageDialog(null,
                            "Error: Can't establish connection.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                dispose();
            }
        });
        add(connect);


        b = new JLabel("No file selected yet");
        b.setSize(120,20);
        b.setLocation(245,70);
        add(b);
        JButton browse = new JButton("Browse");
        browse.setSize(100,20);
        browse.setLocation(250,100);
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(f.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    file = f.getSelectedFile();
                    System.out.println(file.length());
                    b.setText("File: "+file.getName());

                }
            }
        });
        add(browse);





        setVisible(true);

    }
}
