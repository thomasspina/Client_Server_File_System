import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class cliClient {
    private static Socket socket;
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String serverAddress = InputValidation.getAddress(scanner);
        int serverPort = InputValidation.getPort(scanner);

        socket = new Socket(serverAddress, serverPort);
        System.out.format("Connected to server running on [%s:%d]\n", serverAddress, serverPort);

        DataInputStream in = new DataInputStream(socket.getInputStream());

        // get current default directory from server
        String currentDirectory = in.readUTF();

        while (true) {
            System.out.printf("%s ", currentDirectory);
            String[] command = scanner.nextLine().split(" ");

            switch (command[0]) {
                case "cd":
                    String fileName = command[1];
                    String response = cd(fileName);

                    if (response.startsWith("Error")) {
                        System.out.println(response);
                        break;
                    }
                    currentDirectory = response;
                    break;
                case "ls":
                    System.out.println(ls());
                    break;
                case "mkdir":
                    String dirName = command[1];
                    String resp = mkdir(dirName);
                    if (resp.startsWith("Error")) {
                        System.out.println(resp);
                        break;
                    }
                    currentDirectory = resp;
                default:
                    System.out.println("nope");
                    break;
            }
        }

        //socket.close();
    }

    private static String cd(String fileName) {
        String response;
        try {
            Command command = new Command("cd", fileName);

            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);

            objOut.writeObject(command);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            response = in.readUTF();
        }
        catch (IOException e) {
            response = "Error handling: " + e;
        }

        return response;
    }

    private static String ls() {
        String response;
        try {
            Command command = new Command("ls");
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);

            objOut.writeObject(command);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            response = in.readUTF();

        } catch (IOException e) {
            response = "Error handling: " + e;
        }
        return response;
    }

    private static String mkdir(String dirName) {
        String response;
        try {
            Command command = new Command("mkdir", dirName);

            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(out);

            objOut.writeObject(command);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            response = in.readUTF();
        }
        catch (IOException e) {
            response = "Error handling: " + e;
        }

        return response;
    }

    private static boolean upload(String name) {
        return false;
    }

    private static File download(String name) {
        return new File("");
    }

    private static void exit() {

    }


}
