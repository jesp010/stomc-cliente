package server;

import comm.Conexion;
import dominio.Message;
import dominio.Turn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread {
    protected Socket socket;
    protected ObjectOutputStream os;
    protected ObjectInputStream is;

    TurnManager turnManager = new TurnManager();

    public Client(Socket clientSocket) {
        this.socket = clientSocket;
    }

    @Override
    public void run() {
        try {
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

            if (!socket.isClosed()) {
                try {
                    Message message = null;

                    assert is != null;
                    message = (Message) is.readObject();

                    handleReceivedMessage(message, os);
                } catch (Exception e) {
                    try {
                        close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        break;
                    }
                    e.printStackTrace();
                    break;
                }
            } else {
                break;
            }
        }
        System.out.println("Fin del cliente");
    }

    public void handleReceivedMessage(Message message, ObjectOutputStream os) {

        switch (message.getType()) {
            case CALL_NEXT_CAJA:
            case CALL_NEXT_MODULO:
            case CALL_NEXT_GENERIC:
            case NEW_TURN_CAJA:
            case NEW_TURN_MODULO:
            case NEW_TURN_GENERIC:
            case RELEASE_TURN:
            case GET_TURNS_STATUS:
            case GET_TURNS_START_DATE:
            case GET_TURNS_END_DATE:
            case GET_TURNS_STATUS_START_DATE:
            case GET_TURNS_STATUS_END_DATE:
            case GET_TURNS_START_AND_END_DATE:
            case GET_TURNS_STATUS_START_AND_END_DATE:
            case GET_MANY_TURN:
                message = turnManager.handleRequest(message);
                break;
        }

        sendMessage(message, os);
    }

    public void sendMessage(Message message, ObjectOutputStream os) {
        try {
            os.writeObject(message);
            os.flush();

            Conexion.getInstance().sendMessage(message);
        } catch (IOException e) {
            System.out.println("Could not connect to client.");
            if (message.getType() == Message.MessageType.CALL_NEXT_CAJA
                    || message.getType() == Message.MessageType.CALL_NEXT_MODULO
                    || message.getType() == Message.MessageType.CALL_NEXT_GENERIC) {
                turnManager.sendBackTurn((Turn) message.getObject());
            }
        }
    }

    public void close() throws IOException {
        is.close();
        os.close();
        socket.close();

        is = null;
        os = null;
        socket = null;
    }
}

