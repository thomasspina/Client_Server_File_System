import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private static ServerSocket listener;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String serverAddress = getServerAddress(scanner);
        System.out.println(serverAddress);
    }

    private static String getServerAddress(Scanner scanner) {
        String address;
        while (true) {
            System.out.print("Enter a valid IP address on which to run the server : ");
            address = scanner.nextLine();

            String[] str = address.split("\\.");
            if (str.length != 4) {
                System.out.println("INVALID : The IP address is too long.\n");
                continue;
            }

            break;
        }

        return address;
    }
}
