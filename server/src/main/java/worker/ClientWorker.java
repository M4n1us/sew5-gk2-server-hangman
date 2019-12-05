package worker;

import game.Hangman;
import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;

public class ClientWorker implements Runnable {

    @NonNull
    private String wordlistFilename;
    @NonNull
    private String highscoreFilename;

    private static File wordlistFile, highscoreFile;
    private List<String> words;

    private boolean listening= true;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Hangman game;

    public ClientWorker(@NonNull String wordlistFilename, @NonNull String highscoreFilename, Socket clientSocket) throws IOException {
        this.wordlistFilename = wordlistFilename;
        this.highscoreFilename = highscoreFilename;
        this.clientSocket = clientSocket;
        this.setupData();
        this.game = new Hangman(pickRandomWord());
    }

    private void setupData() throws IOException {
        if (wordlistFile == null){
            wordlistFile = FileUtils.getFile(this.wordlistFilename);
        }
        if (highscoreFile == null){
            highscoreFile = FileUtils.getFile(this.highscoreFilename);
        }
        readWordlist();
    }

    synchronized private void readWordlist() throws IOException {
        this.words = FileUtils.readLines(wordlistFile, "UTF-8");
    }

    private String pickRandomWord(){
        return this.words.get(getRandomWordListIndex());
    }

    private int getRandomWordListIndex(){
        Random rand = new Random();
        return rand.nextInt(this.words.size());
    }

    @Override
    public void run() {
        try {
            String s = "";
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            while (listening && (s = in.readLine()) != null) {
                String message = game.playRound(s);
                if (message == null) {
                    this.out.println("The game has ended.");
                    this.out.println("!EXIT");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.out.close();
            try {
                this.in.close();
                this.clientSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
