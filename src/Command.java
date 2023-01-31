import java.io.File;
import java.io.Serializable;

public class Command implements Serializable {
    private final String command;
    private final String argument;
    private final File file;

    public Command(String command) {
        this(command, (String) null);
    }

    public Command(String command, String argument) {
        this.command = command;
        this.argument = argument;
        this.file = null;
    }

    public Command(String command, File file) {
        this.command = command;
        this.argument = null;
        this.file = file;
    }


    public String getArgument() { return argument; }
    public String getCommand() { return command; }
    public File getFile() { return file; }
}
