import java.util.*;

/**
 * Dictionary.
 */
public class Dictionary {
    /**
     * Words.
     */
    private ArrayList<Word> words = new ArrayList<>();

    public Dictionary() {
        this.words = new ArrayList<>();
    }
    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    /**
     * Vi tri thich hop de add.
     */
    public int getIndexToAdd(String word_target) {
        int l = 0, r = words.size();
        while (l < r) {
            int m = (l + r) / 2;
            Word midValue = words.get(m);
            if (midValue.getWord_target().compareToIgnoreCase(word_target) > 0) {
                r = m;
            } else if (midValue.getWord_target().compareToIgnoreCase(word_target) < 0) {
                l = m + 1;
            } else {
                return m;
            }
        }
        return l;
    }

    /**
     * Add word vao index.
     */
    public void addWordtoDictionary(Word word) {
        int index = getIndexToAdd(word.getWord_target());
        if (index >= 0) {
            words.add(index, word);
        }
    }

    /**
     * Tim kiem nhi phan.
     */
    public int indexOfBinarySearch(String s, int l, int r) {
        if (l > r) return -1;
        int m = (l + r) / 2;
        if (words.get(m).getWord_target().equalsIgnoreCase(s)) {
            return m;
        } else if (words.get(m).getWord_target().compareToIgnoreCase(s) < 0) {
            return indexOfBinarySearch(s, m + 1, r);
        } else {
            return indexOfBinarySearch(s, l, m - 1);
        }
    }

}
