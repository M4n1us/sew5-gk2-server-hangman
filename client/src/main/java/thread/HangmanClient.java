package thread;

import helper.client.Networking;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Multithreadable HangmanClient containing all the message and networking logic.
 */
@Getter
@Setter
public class HangmanClient extends Thread {

    @NonNull
    private String host;
    private int port;

    private Socket connection;
    private PrintWriter out;
    private BufferedReader in;

    private boolean running;

    /**
     * Creates HangmanClient Object with necessary parameters.
     * @param host NonNull Hostname that the client attempts to connect to
     * @param port TCP Port the client attempts to connect to
     */
    public HangmanClient(@NonNull String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * Method executed when HangmanClient.start() is invoked.
     */
    public void run(){
        try {
            this.connection = Networking.getConnection(this.host, this.port);
            this.running = true;
            this.in = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
            this.out = new PrintWriter(this.connection.getOutputStream(), true);
        }
        catch (Exception ex){
            System.err.println("Shutting down. Error during client creation: " + ex.getMessage());
            this.shutdown();
        }

        while (running){
            this.parseServerMessage();
        }

    }

    /**
     * Sends message to the server via the internal TCP Socket.
     * @param message Message to be pushed over the Socket to the server
     */
    public void sendMessage(String message){
        try{
            this.out.println(message);
        }
        catch (Exception ex) {
            System.err.println("Unable to send message: " + ex.getMessage());
        }
    }

    /**
     * Blocking. Handles messages from the server. Initiates shutdown when server sends `!SHUTDOWN` message.
     */
    private void parseServerMessage(){
        String message = "";
        try{
            message = this.in.readLine();
            if (message.equals("!SHUTDOWN")) {
                this.shutdown();
            }
        } catch (IOException ex) {
            System.err.println("Error durring message parsing: " + ex.getMessage());
        }
        System.out.println(message);
    }

    /**
     * Gracefully shutsdown socket by sending server notification that the client will disconnect. Setting running
     * argument to false nad closes socket connection to server.
     */
    private void shutdown(){
        if (this.running) {
            this.out.println("!DISCONNECT");
            this.running = false;
        }
        try {
            this.connection.close();
        }
        catch (IOException ex){
            System.err.println("Failed to close Network Socket: " + ex.getMessage());
        }
    }

}


