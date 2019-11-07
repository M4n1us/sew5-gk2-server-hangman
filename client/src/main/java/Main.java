import org.apache.commons.cli.*;
import org.apache.commons.lang3.math.NumberUtils;
import thread.HangmanClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Entrypoint for the HangmanClient
 */
public class Main {
    /**
     * Launches the client application.
     * @param args CLI Arguments
     */
    public static void main(String[] args){
        try{
            Map<String, String> config = parseArgs(args);
            HangmanClient client = new HangmanClient(config.get("host"), NumberUtils.toInt(config.get("port")));
            client.start();
            BufferedReader cli = new BufferedReader(new InputStreamReader(System.in));

            while (client.isRunning()){
                String input = cli.readLine();
                client.sendMessage(input);
            }

        } catch (ParseException ex) {
            System.err.println("Shutting down. Arguments are invalid: " + ex.getMessage());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error while reading CLI message: " + ex.getMessage());
        }

    }

    /**
     * Parses CLI arguments for the client Application
     * @param args CLI Args passed during launch
     * @return Map containing `host` and `port` as keys with the values assigned via CLI
     * @throws ParseException Throws ParseException when there is something wrong with the arguments.
     */
    public static Map<String, String> parseArgs(String[] args) throws ParseException{
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("h", "host", true, "Server hostname");
        options.addOption("p", "port", true, "TCP port");

        HashMap<String, String> ret = new HashMap<>();

        CommandLine line = parser.parse(options, args);

        ret.put("host", line.getOptionValue("h"));
        ret.put("port", line.getOptionValue("p"));

        return ret;
    }
}
