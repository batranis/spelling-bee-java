import edu.willamette.cs1.spellingbee.SpellingBeeGraphics;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class SpellingBee {
    private static final String ENGLISH_DICTIONARY = "EnglishWords.txt";

    private SpellingBeeGraphics sbg;
    private String puzzleLetters = ""; //The 7 puzzle letters
    private String[] dictionary = new String[0]; //All of the dictionary words
    private int dictionarySize = 0; //Number of words in the dictionary
    private String[] found = new String[0]; //Words which have been found
    private int wordCount = 0; //Number of words which have been found
    private int totalScore = 0; //Score from the words that have been found
    private int totalWords = 0; //Total valid words that have been found

    //Puts text boxes and buttons on screen
    public void run() {
        sbg = new SpellingBeeGraphics();
        sbg.addField("Puzzle", (s) -> puzzleAction(s));
        sbg.addField("Word", (s) -> wordAction(s));
        sbg.addButton("Solve", (s) -> solveAction());
    }

    //Makes sure input is valid, gets the dictionary, and resets counters
    private void puzzleAction(String s) {
        if (contains7Letters(s) && areAllCharsLetters(s) && noDuplicateLetters(s)) {
            try {
                dictionary = readFile(ENGLISH_DICTIONARY);
                dictionarySize = dictionary.length;
                found = new String[dictionarySize];
                puzzleLetters = s.toUpperCase();
                sbg.setBeehiveLetters(puzzleLetters);
                wordCount = 0;
                totalScore = 0;
                totalWords = 0;
                sbg.clearWordList();
                sbg.showMessage("");
            } catch (IOException e) {
                //If dictionary file is missing it just resets everything
                sbg.showMessage("Dictionary file not found", Color.red);
                dictionary = new String[0];
                dictionarySize = 0;
                found = new String[0];
                puzzleLetters = "";
                sbg.clearWordList();
                sbg.setBeehiveLetters("");
            }
        } else {
            sbg.showMessage("Invalid puzzle", Color.red);
        }
    }

    //Makes sure word is valid, scores it, colors pangrams
    private void wordAction(String s) {
        sbg.showMessage("");
        String upper = s.toUpperCase();

        if (puzzleLetters.equals("")) {
            sbg.showMessage("Enter a puzzle por favor", Color.red);
        } else if (!isValidWord(s)) {
            if (s.length() < 4) {
                sbg.showMessage("Too short", Color.red);
            } else if (!upper.contains(puzzleLetters.substring(0, 1))) {
                sbg.showMessage("Needs center letter", Color.red);
            } else {
                sbg.showMessage("Needs letters only in beehive", Color.red);
            }
        } else if (!inDictionary(s)) {
            sbg.showMessage("Not in Dictionary", Color.red);
        } else if (found(upper)) {
            sbg.showMessage("Already found word", Color.red);
        } else {
            int score = getScore(s);
            Color color = Color.black;
            if (pangram(s)) {
                color = Color.blue;
            }
            sbg.addWord(s + " (" + score + ")", color);
            found[wordCount] = upper;
            wordCount++;
            totalWords++;
            totalScore += score;
            sbg.showMessage(totalWords + " words; " + totalScore + " points", Color.BLACK);
            sbg.setField("Word", "");
        }
    }

    //checks if word is in the dictionary
    private boolean inDictionary(String word) {
        String upper = word.toUpperCase();
        for (int i = 0; i < dictionarySize; i++) {
            if (dictionary[i].equals(upper)) {
                return true;
            }
        }
        return false;
    }

    //checks if word has already been found
    private boolean found(String upper) {
        for (int i = 0; i < wordCount; i++) {
            if (found[i].equals(upper)) {
                return true;
            }
        }
        return false;
    }

    //finds all the valid words in the dictionary using the button
    private void solveAction() {
        searchDictionary(dictionary);
    }

    //validation of length
    private boolean contains7Letters(String word) {
        if (word.length() == 7) {
            return true;
        } else {
            return false;
        }
    }

    //validation of letters only
    private boolean areAllCharsLetters(String word) {
        int letters = 0;
        for (int i = 0; i < word.length(); i++) {
            if ((word.charAt(i) >= 'A' && word.charAt(i) <= 'Z') || (word.charAt(i) >=
                    'a' && word.charAt(i) <= 'z')) {
                letters++;
            }
        }
        if (letters == word.length()) {
            return true;
        } else {
            return false;
        }
    }

    //validation of no duplicates
    private boolean noDuplicateLetters(String word) {
        int duplicates = 0;
        for (int i = 0; i < word.length(); i++) {
            for (int j = i + 1; j < word.length(); j++) {
                if (word.charAt(i) == word.charAt(j)) {
                    duplicates++;
                }
            }
        }
        if (duplicates == 0) {
            return true;
        } else {
            return false;
        }
    }

    //makes sure guessed word has length above four, center letter, and letters in puzzle
    private boolean isValidWord(String word) {
        if (puzzleLetters.equals("")) {
            return false;
        }
        if (word.length() < 4) {
            return false;
        }
        String upperWord = word.toUpperCase();
        String center = puzzleLetters.substring(0, 1);
        if (!upperWord.contains(center)) {
            return false;
        }
        for (int i = 0; i < upperWord.length(); i++) {
            String letter = upperWord.substring(i, i + 1);
            if (!puzzleLetters.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    //finds valid words and scores them and displays them
    private void searchDictionary(String[] dictionary) {
        sbg.clearWordList();
        totalScore = 0;
        totalWords = 0;
        for (int i = 0; i < dictionarySize; i++) {
            if (isValidWord(dictionary[i])) {
                int score = getScore(dictionary[i]);
                Color color = Color.black;
                if (pangram(dictionary[i])) {
                    color = Color.blue;
                }
                sbg.addWord(dictionary[i] + " (" + score + ")", color);
                totalWords++;
                totalScore += score;
            }
        }
        sbg.showMessage(totalWords + " words; " + totalScore + " points", Color.black);
    }

    //scores the words based on length/pangram
    private int getScore(String word) {
        int score = 0;
        if (word.length() == 4) {
            score = 1;
        } else {
            score = word.length();
        }
        if (pangram(word)) {
            score += 7;
        }
        return score;
    }

    //checks if word uses all 7 puzzle letters
    private boolean pangram(String word) {
        String upperWord = word.toUpperCase();
        for (int i = 0; i < puzzleLetters.length(); i++) {
            String letter = puzzleLetters.substring(i, i + 1);
            if (!upperWord.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    //reads dictionary to count lines and then get words
    public static String[] readFile(String address) throws IOException {
        File myFile = new File(address);
        Scanner scan = new Scanner(myFile);
        int count = 0;
        while (scan.hasNextLine()) {
            scan.nextLine();
            count++;
        }
        scan.close();
        String[] words = new String[count];
        scan = new Scanner(myFile);
        int i = 0;
        while (scan.hasNextLine()) {
            words[i] = scan.nextLine().toUpperCase();
            i++;
        }
        scan.close();
        return words;
    }

    public static void main(String[] args) {
        new SpellingBee().run();
    }
}