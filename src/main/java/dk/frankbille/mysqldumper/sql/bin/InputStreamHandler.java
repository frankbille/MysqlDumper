package dk.frankbille.mysqldumper.sql.bin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

class InputStreamHandler {

    private static class InputReader implements Runnable {
        private final InputStream inputStream;
        private final StringBuilder result;

        InputReader(InputStream inputStream, StringBuilder result) {
            this.inputStream = inputStream;
            this.result = result;
        }

        @Override
        public void run() {
            try {
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    result.append(line).append("\n");
                }
                reader.close();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final StringBuilder result = new StringBuilder();

    public InputStreamHandler(InputStream inputStream) {
        new Thread(new InputReader(inputStream, result)).start();
    }

    public CharSequence getResult() {
        return result;
    }
}
