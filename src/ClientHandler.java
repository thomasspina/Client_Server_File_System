import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ClientHandler extends Thread {
    private Socket socket;
    private int clientNumber;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
    public ClientHandler(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println(getFormattedMessage("client # " + clientNumber + "connected."));
    }

    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("Hello from server - you are client#" + clientNumber + "\n");
        } catch (IOException e) {
            System.out.println("Error handling client# " + clientNumber + ": " + e + "\n");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close a socket, what's going on?\n");
            }
            System.out.println("Connection with client# " + clientNumber + " closed\n");
        }
    }

    private String getFormattedMessage(String message) {
        LocalDateTime now = LocalDateTime.now();

        return String.format("[%s:%d - %s]: %s\n",
                socket.getInetAddress().toString().substring(1),
                socket.getPort(),
                dateTimeFormatter.format(now),
                message);
    }
}
