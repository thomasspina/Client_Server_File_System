import java.util.Scanner;

public class InputValidation {
    public static String getAddress(Scanner scanner) {
        String address;

        outer: while (true) {
            System.out.print("Enter a valid IP address : ");
            address = scanner.nextLine();

            String[] splitAddress = address.split("\\.");
            if (splitAddress.length != 4) {
                System.out.println("INVALID : The IP address is not of the right length.\n");
                continue;
            }

            for (String ipFragment : splitAddress) {
                try {
                    int num = Integer.parseInt(ipFragment);
                    if (num > 255 || num < 0) {
                        System.out.printf("INVALID : %s is not in the 0 to 255 inclusive range.\n\n", ipFragment);
                        continue outer;
                    }
                }
                catch (NumberFormatException ex) {
                    System.out.printf("INVALID : '%s' is not an integer.\n\n", ipFragment);
                    continue outer;
                }
            }

            break;
        }

        return address;
    }

    public static int getPort(Scanner scanner) {
        int port;

        do {
            System.out.print("Enter a port number between 5000 and 5050 : ");
            port = scanner.nextInt();

        } while (port < 5000 || port > 5050);

        return port;
    }
}
