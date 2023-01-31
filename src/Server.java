import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private static ServerSocket listener;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String address = InputValidation.getAddress(scanner);
        int port = InputValidation.getPort(scanner);

        listener = new ServerSocket();
        listener.setReuseAddress(true);
        InetAddress serverIP = InetAddress.getByName(address);

        listener.bind(new InetSocketAddress(serverIP, port));
        System.out.format("Server running on [%s:%d]\n", address, port);

        try {
            while (true) {
                new ClientHandler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

}
