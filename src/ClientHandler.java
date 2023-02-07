import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ClientHandler extends Thread {
    private Socket socket;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH:mm:ss");
    private Path pwd = Paths.get("root");
    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.print(getFormattedMessage("client connected"));
    }

    public void run() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            // send current default directory
            out.writeUTF(pwd.toString());
            InputStream in = socket.getInputStream();

            while (true) {
                ObjectInputStream objIn = new ObjectInputStream(in);

                Command command;
                try {
                    command = (Command) objIn.readObject();
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                    continue;
                }

                switch (command.getCommand()) {
                    case "cd":
                        String arg = command.getArgument();
                        System.out.print(getFormattedMessage(String.format("cd: %s/%s", pwd, arg)));

                        if (arg.equals("..")) {
                            if (pwd.equals(Paths.get("root"))) {
                                out.writeUTF("Error: already in root");
                                break;
                            }

                            pwd = pwd.getParent();
                            out.writeUTF(pwd.toString());
                            break;
                        }

                        Path path = Paths.get(pwd.toString() + "/" + arg);
                        if (Files.exists(path) && Files.isDirectory(path)) {
                            pwd = path;
                            out.writeUTF(pwd.toString());
                        } else if (Files.isRegularFile(path)) {
                            out.writeUTF(String.format("Error: not a directory: %s", path));
                        } else {
                            out.writeUTF(String.format("Error: no such file or directory: %s", path));
                        }
                        break;
                    case "ls":
                        System.out.print(getFormattedMessage("ls"));
                        File directory = new File(pwd.toString());
                        String[] fileArray = directory.list();
                        String outputString = "";
                        for(String fileName : fileArray) {
                            File file = new File(pwd + "/" + fileName);
                            if(file.isDirectory()){
                                outputString += "[Folder] " + fileName + "\n";
                            }
                            else {
                                outputString += "[File] " + fileName + "\n";
                            }

                        }
                        out.writeUTF(outputString);
                        break;
                    default:
                        System.out.print(getFormattedMessage("no command"));
                }
            }

        }
        catch (IOException e) {
            System.out.println(getFormattedMessage("Error handling: " + e));
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
