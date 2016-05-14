package dk.frankbille.mysqldumper.sql.bin;

import java.io.IOException;
import java.util.List;

public class CommandLineHelper {

    public static CommandLineResult executeCommand(List<String> command, String commandInput) throws IOException, InterruptedException {


        ProcessBuilder processBuilder = new ProcessBuilder(command);

        final Process process = processBuilder.start();

        InputStreamHandler inputHandler = new InputStreamHandler(process.getInputStream());
        InputStreamHandler errorHandler = new InputStreamHandler(process.getErrorStream());

        final int exitCode = process.waitFor();

        return new CommandLineResult(exitCode, inputHandler.getResult(), errorHandler.getResult());
    }

}
