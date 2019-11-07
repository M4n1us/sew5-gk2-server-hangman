package helper.client;

import java.io.IOException;
import java.net.Socket;

public class Networking {
    public static Socket getConnection(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        socket.setKeepAlive(true);
        return socket;
    }
}
