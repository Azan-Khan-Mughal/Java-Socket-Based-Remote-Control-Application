import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class File_Receiving extends Thread{
    Socket s;
    public File_Receiving(Socket s){
        this.s = s;
        start();
    }

    public static void main(String[] args) {
         new File_Receiving(null);
    }
    @Override
    public void run() {
        InputStream in = null;
        JFrame frame;
        try {

            frame = new JFrame("Receiving file...");
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


            in = s.getInputStream();

            InputStream input = s.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String name = reader.readLine();
            System.out.println("Name of file is"+name);

            File outputFile = new File("Downloads/"+name);

            int max = reader.read();
            jpb.setMaximum(max);
            jpb.setValue(0);

            // Check if we can write to the file
            if (!outputFile.exists()) {
                outputFile.createNewFile(); // Create the file if it doesn't exist
            }

            FileOutputStream out = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                System.out.println("Recived:");
                jpb.setValue(jpb.getValue()+1);
                label.setText((max/(float)jpb.getValue())*100+"%");


            }
            System.out.println("Done reciving");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        frame.dispose();

    }
}
