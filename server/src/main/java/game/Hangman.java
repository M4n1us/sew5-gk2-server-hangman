package game;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Hangman implements Runnable{

    private int attempts = 7;

    private final String choosenWord;

    private final ArrayList guessedCharacters;


    public Hangman(@NonNull String word) {
        this.choosenWord = word;
        this.guessedCharacters = new ArrayList();
    }


    private String buildMessage() {
        String s = "";
        for(int i = 0; i < this.choosenWord.length(); i++){
            char currentChar =  this.choosenWord.charAt(i);
            if (this.guessedCharacters.contains(currentChar)) {
                s += currentChar;
            } else {
                s += "_";
            }
        }

        return "Word: " + s + " Attempts left: " + this.attempts;
    }

    public String guess(String input) {
        if (input.length() > 1) {
            if (this.choosenWord.toLowerCase().equals(input.toLowerCase())) {
                attempts = 0;
                return "Correct! The word was: " + this.choosenWord;
            } else {
                attempts--;
                return "The word was not correct.";
            }
        } else if ( input.length() == 1) {
            if (this.guessedCharacters.contains(input.charAt(0))) {
                return "The character has been guessed already";
            } else {
                this.guessedCharacters.add(input.charAt(0));
                attempts--;
                return buildMessage();
            }
        } else {
            return "Input is empty.";
        }
    }

    public String playRound(String input) {
        if (this.attempts > 0) {
            return guess(input);
        } else {
            return null;
        }
    }

    @Override
    public void run() {

    }
}
