package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server implements Runnable {

    //protected static int serverPort;
    protected static int clientPort;
    private ServerSocket ss = null;
    //private final Map<String, InetAddress> netMessageMap = new HashMap<String, InetAddress>();

    @Override
    public void run() {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            //serverPort = Integer.parseInt(prop.getProperty("server_port"));
            clientPort = Integer.parseInt(prop.getProperty("client_port"));

            ss = new ServerSocket(clientPort);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        while (true) {

            Socket socket = null;

            try {
                socket = ss.accept();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    new Client(socket).start();
                }
            }
        }
    }
}
