package dk.frankbille.mysqldumper.sql.bin;

public class CommandLineResult {

    private final int exitCode;
    private final CharSequence commandOutput;
    private final CharSequence commandError;

    public CommandLineResult(int exitCode, CharSequence commandOutput, CharSequence commandError) {
        this.exitCode = exitCode;
        this.commandOutput = commandOutput;
        this.commandError = commandError;
    }

    public CharSequence getCommandOutput() {
        return commandOutput;
    }

    public CharSequence getCommandError() {
        return commandError;
    }

}
