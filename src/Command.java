import java.io.File;
import java.io.Serializable;

public class Command implements Serializable {
    private final String command;
    private final String argument;

    public Command(String command) {
        this(command, (String) null);
    }

    public Command(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public String getArgument() { return argument; }
    public String getCommand() { return command; }

}
