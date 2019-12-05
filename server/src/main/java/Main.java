import org.apache.commons.cli.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args){

        try{
            Map<String, String> config = parseArgs(args);
            Server server = new Server(NumberUtils.toInt(config.get("port")));

        } catch (ParseException ex) {
            System.err.println("Shutting down. Arguments are invalid: " + ex.getMessage());
            System.exit(1);
        }
    }

    /**
     * Parses CLI arguments for the server Application
     * @param args CLI Args passed during launch
     * @return Map containing `port` as key with the value assigned via CLI
     * @throws ParseException Throws ParseException when there is something wrong with the arguments.
     */
    public static Map<String, String> parseArgs(String[] args) throws ParseException{
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption("p", "port", true, "TCP port");

        HashMap<String, String> ret = new HashMap<>();

        CommandLine line = parser.parse(options, args);

        ret.put("port", line.getOptionValue("p"));

        return ret;
    }
}
