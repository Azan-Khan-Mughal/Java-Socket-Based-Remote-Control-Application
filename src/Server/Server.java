package Server;

import AppConfig.ApplicationManager;

import java.net.ServerSocket;

public class Server {
    int port;
    ServerSocket serverSocket;
    Server_Thread serverThread;

    public Server(String port) throws Exception {
        this(parsePort(port));
    }
    public Server(int port) throws Exception{
        this.serverSocket = createServerSocket(port);
        this.port = port;

        serverThread = new Server_Thread(serverSocket);

    }

    static int parsePort(String port) throws Exception{
        if (port == null || port.trim().isEmpty()) {
            throw new Exception("The Input is Empty.");
        }
        if (!port.matches("\\d+")) {
            throw new Exception("The Input Must Be a numerical Value");
        }
        return Integer.parseInt(port);
    }


    ServerSocket createServerSocket(int port) throws Exception{
        if (port < 1024 || port > 65535) {
            throw new Exception("Port must be between 1024 and 65535.");
        }
        try(ServerSocket serverSocket = new ServerSocket(port) ){
            return serverSocket;

        }catch (Exception e){
            throw new Exception("Port not available.");
        }
    }
}
