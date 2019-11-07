package game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Getter
@Setter
public class Hangman {

    @NonNull
    private String wordlistFilename;
    @NonNull
    private String highscoreFilename;

    private static File wordlistFile, highscoreFile;
    private List<String> words;

    private static final int MAX_ATTEMPTS = 7;
    private int currentAttempt = 1;

    private final String choosenWord;
    private String maskedWord;

    private final char[] guessedCharacters;


    public Hangman(@NonNull String wordlistFilename, @NonNull String highscoreFilename) throws IOException {
        this.wordlistFilename = wordlistFilename;
        this.wordlistFilename = highscoreFilename;
        this.words = new LinkedList<>();
        this.setupData();
        this.choosenWord = pickRandomWord();
        this.guessedCharacters = new char[MAX_ATTEMPTS];
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

    private void buildMaskWord(){
        this.maskedWord = "";
        this.choosenWord.chars().forEach(this::matchCharacter);
    }

    private void matchCharacter(int characterValue){
        for(char guessedCharacter : this.guessedCharacters){
            if (guessedCharacter == characterValue){
                this.maskedWord += Integer.toString(characterValue);
                break;
            }
            else {
                this.maskedWord += "_";
                break;
            }
        }
    }
}
