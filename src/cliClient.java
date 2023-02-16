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

        client_loop: while (true) {
            String[] command = scanner.nextLine().split(" ");


            switch (command[0]) {
                case "cd":
                    String fileName = command[1];
                    String res = simpleCommand("cd", fileName);

                    if (res.startsWith("Error")) {
                        System.out.println(res);
                        break;
                    }
                    currentDirectory = res;
                    break;
                case "ls":
                    System.out.println(simpleCommand("ls"));
                    break;
                case "exit":
                    res = simpleCommand("exit");
                    System.out.println(res);

                    if (res.equals("You have been disconnected.")) {
                        break client_loop;
                    }
                    break;
                case "mkdir":
                    String dirName = command[1];
                    String resp = simpleCommand("mkdir", dirName);
                    if (resp.startsWith("Error")) {
                        System.out.println(resp);
                        break;
                    }
                    System.out.println(resp);
                    break;
                case "upload":
                    fileName = command[1];
                    res = upload(fileName);
                    System.out.println(res);
                    break;
                case "download":
                    String downloadFileName = command[1];
                    res = download(downloadFileName);
                    System.out.println(res);
                    break;
                default:
                    break;
            }

            System.out.printf("%s ", currentDirectory);
        }

        socket.close();

    }

    private static String simpleCommand(String commandName) { return simpleCommand(commandName, null); }

    private static String simpleCommand(String commandName, String arg) {
        String response;

        try {
            Command command = new Command(commandName, arg);

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
    private static String upload(String fileName) {
        String response;
        try {
            Command command = new Command("upload",fileName);
            int bytes = 0;
            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(command);

            String path = System.getProperty("user.dir") + '/' + fileName;
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            dataOut.writeLong(file.length());
            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer))
                    != -1) {
                dataOut.write(buffer, 0, bytes);
                dataOut.flush();
            }
            fileInputStream.close();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            response = in.readUTF();
        }
        catch (IOException e){
            response = "Error handling" + e;
        }
        return response;
    }

    private static String download(String downloadFileName) {
        String response;
        try {
            Command command = new Command("download",downloadFileName);
            DataInputStream downloadIn = new DataInputStream(socket.getInputStream());
            DataOutputStream downloadOut = new DataOutputStream(socket.getOutputStream());

            ObjectOutputStream downloadObjOut = new ObjectOutputStream(downloadOut);
            downloadObjOut.writeObject(command);

            String downloadPath = System.getProperty("user.dir") + '/' + downloadFileName;
            File downloadFile = new File(downloadPath);
            FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);

            if (!downloadFile.exists()) {
                downloadFile.createNewFile();
            }
            long downloadSize = downloadIn.readLong();
            int downloadBytes;
            byte[] buffer = new byte[4 * 1024];

            while (downloadSize > 0 && (downloadBytes = downloadIn.read(buffer, 0, (int)Math.min(buffer.length, downloadSize)))
                    != -1) {
                fileOutputStream.write(buffer, 0, downloadBytes);
                downloadSize -= downloadBytes;
            }
            fileOutputStream.close();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            response = in.readUTF();
        }
        catch (IOException e){
            response = "Error handling" + e;
        }
        return response;
    }
}
