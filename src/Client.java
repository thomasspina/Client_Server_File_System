import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String serverAddress = InputValidation.getAddress(scanner);
        int serverPort = InputValidation.getPort(scanner);

        socket = new Socket(serverAddress, serverPort);
        System.out.format("Connected to server running on [%s:%d]\n", serverAddress, serverPort);

        DataInputStream in = new DataInputStream(socket.getInputStream());

        String helloMessageFromServer = in.readUTF();
        System.out.println(helloMessageFromServer);

        socket.close();
    }
}
