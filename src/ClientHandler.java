import java.io.*;
import java.net.Socket;
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
                        pwd = Paths.get();
                        System.out.println(Paths.get("./root").toAbsolutePath().normalize());
                        out.writeUTF(pwd);
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
