package FileTransfer;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class File_Sending extends Thread {
    Socket s;
    File file;
    public File_Sending(Socket s,File file){
        this.s=s;
        this.file = file;
        start();

    }
    JFrame frame;

    @Override
    public void run() {
        OutputStream out = null;
        try {

            frame = new JFrame("Sending file...");
            frame.setSize(300,100);
            frame.setLocation(300,300);
            frame.setLayout(null);

            JProgressBar jpb = new JProgressBar();
            jpb.setSize(250,20);
            jpb.setLocation(17,20);


            JLabel label = new JLabel("0%");
            label.setSize(60,30);
            label.setLocation(130,14);
            frame.add(label);
            frame.add(jpb);

            frame.setVisible(true);


            out = s.getOutputStream();
            FileInputStream in = new FileInputStream(file);

            OutputStream output = s.getOutputStream();
            PrintWriter writer = new PrintWriter(output);
            writer.println(file.getName());
            writer.flush();


            System.out.println("Name of file is "+ file.getName());
            int percentage = (int) Math.ceil(file.length()/(float)1024);
            writer.write(percentage);
            writer.flush();
            jpb.setMaximum(percentage);
            jpb.setValue(0);

            Thread.sleep(1000);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                System.out.println("Send:");
                jpb.setValue(jpb.getValue()+1);
                label.setText((percentage/(float)jpb.getValue())*100+"%");
            }
            Thread.sleep(500);
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        frame.dispose();


    }
}
