import worker.ClientWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket socketServer;
    private int port;
    private  boolean listening= true;
    private ConcurrentHashMap <String, ClientWorker> clients = new ConcurrentHashMap<>();
    private String highscore = "high.txt";
    private String words = "words.txt";

    public Server(int port){
        this.port = port;
        this.start();
    }

    private void start() {
        try {
            this.socketServer = new ServerSocket(this.port,
                    8);
            while(listening){
                Socket clientSocket = this.socketServer.accept();
                System.out.println("Client connected");
                String name= "Client "+ this.clients.size();
                ClientWorker game = new ClientWorker(words, highscore, clientSocket);
                Thread t= new Thread(game);
                t.start();
                this.clients.put("Client " + this.clients.size(), game);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return listening;
    }
}
