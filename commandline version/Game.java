import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private ArrayList<String> dictionary = new ArrayList<>();
    private String secretWord;
    private StringBuilder guessedWord;
    private int badGuessCount;
    private Scanner sc = new Scanner(System.in);
    private static final int MAX_BAD_GUESS_COUNT = 20;

    private int hintCount;

    public Game() {
        readDictionaryFromFile("src/dictionary.txt");
        secretWord = chooseWordFromDictionary();
        guessedWord = new StringBuilder("-".repeat(secretWord.length()));
        hintCount = 2;
        badGuessCount = 0;

        sc = new Scanner(System.in);
    }

    public void gamePlaying() {
        do {
            renderGame(guessedWord.toString(), badGuessCount);
            char guess = readAGuess();
            if (contains(secretWord, guess)) {
                guessedWord = update(guessedWord, secretWord, guess);
            } else {
                badGuessCount++;
                if (badGuessCount >= 5) {
                    if (askForHint()) {
                        giveHint();
                    }
                }
            }
        } while (badGuessCount < MAX_BAD_GUESS_COUNT && !secretWord.equals(guessedWord.toString()));

        renderGame(guessedWord.toString(), badGuessCount);
        if (badGuessCount < MAX_BAD_GUESS_COUNT) {
            System.out.println("Chúc mừng nhé! Bạn đã chiến thắng lần này!");
        } else {
            System.out.println("Huhu! Thua mất tiêu rùi :<<. Đáp án phải là: " + secretWord);
        }
    }

    public void readDictionaryFromFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length >= 1) {
                    String word_target = data[0].trim();
                    dictionary.add(word_target);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi đọc file: " + e.getMessage());
        }
    }

    public String chooseWordFromDictionary() {
        Random random = new Random();
        return dictionary.get(random.nextInt(dictionary.size()));
    }

    public void giveHint() {
        if (hintCount > 0) {
            int hiddenIndex = 0;
            for (int i = 0; i < secretWord.length(); i++) {
                if (guessedWord.charAt(i) == '-') {
                    hiddenIndex = i;
                    break;
                }
            }
            char hintChar = secretWord.charAt(hiddenIndex);
            System.out.println("Gợi ý: Từ cần đoán có chứa kí tự '" + hintChar + "'");
            hintCount--;
            if (hintCount == 0) {
                System.out.println("Bạn đã hết số lần gợi ý!");
            } else {
                System.out.println("Số lần gợi ý còn lại: " + hintCount);
            }
        } else {
            System.out.println("Bạn đã hết số lần gợi ý!");
        }
    }


    public boolean askForHint() {
        if (hintCount > 0) {
            System.out.print("Bạn muốn sử dụng gợi ý không ?(Y/N) : ");
            String answer = sc.nextLine();
            return answer.equalsIgnoreCase("Y");
        } else {
            return false;
        }
    }

    public void renderGame(String guessedWord, int badGuessCount) {
        System.out.println("Guessed Word: " + guessedWord);
        System.out.println("Số lượng đoán sai: " + badGuessCount);
    }

    public char readAGuess() {
        System.out.print("Từ bạn đoán: ");
        return sc.nextLine().charAt(0);
    }

    public boolean contains(String secretWord, char guess) {
        return secretWord.indexOf(guess) != -1;
    }

    public StringBuilder update(StringBuilder guessedWord, String secretWord, char guess) {
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == guess) {
                guessedWord.setCharAt(i, guess);
            }
        }
        return guessedWord;
    }
}
