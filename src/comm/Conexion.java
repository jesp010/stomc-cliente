package comm;

import dominio.Message;
import main.IController;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class Conexion implements Runnable {

    private static Conexion instance;

    private static IController controller;

    protected static int serverPort = 0; // Server: 5511 -- Client: 5512
    protected static String serverHost = "";

    private static Socket socket;
    protected static ObjectOutputStream os;
    protected static ObjectInputStream is;

    private Conexion() {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            serverPort = Integer.parseInt(prop.getProperty("server_port"));
            serverHost = prop.getProperty("serverHost");

            connect();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Thread hiloEscucha = new Thread(this);
        hiloEscucha.start();
    }

    public static Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }

        return instance;
    }

    public static IController getController() {
        return controller;
    }

    public void setController(IController controller) {
        Conexion.controller = controller;
    }

    public void sendMessage(Message message) throws IOException {
        try {
            os.writeObject(message);
            os.reset();
        } catch (IOException e) {
            try {
                close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return;
            }
            e.printStackTrace();
            return;
        }
    }

    public static void close() throws IOException {
        is.close();
        os.close();
        socket.close();
    }

    public static void connect() throws IOException {
        socket = new Socket(serverHost, serverPort);
        os = new ObjectOutputStream(socket.getOutputStream());
        is = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            if (!socket.isClosed()) {
                try {
                    Message message = (Message) is.readObject();

                    getController().handleMessage(message);
                } catch (Exception e) {
                    try {
                        close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                }
            } else {
                try {
                    connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
