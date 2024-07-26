import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * dictionary
 */
public class DictionaryManagement {

    private Dictionary dictionary = new Dictionary();


    public void insertFromCommandline(){
        int n = Validation.getInt("Nhập số lượng từ: ", 1, Integer.MAX_VALUE);

        while (n > 0) {
            String word_target = Validation.getString("Nhập từ tiếng Anh: ");
            String word_explain = Validation.getString("Nhập nghĩa tiếng Việt: ");
            dictionary.addWordtoDictionary(new Word(word_target,word_explain));
            n--;
        }

    }
    // read file dic.txt and store it in a list
    public void insertFromFile(String path) {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length == 2) {
                    String word_target = data[0].trim();
                    String word_explain = data[1].trim();

                    //set vào words
                    dictionary.addWordtoDictionary(new Word(word_target,word_explain));
                }
            }
            System.out.println("Đã truy cập vào file :" + path);
        }
        catch (IOException e) {
            System.err.println("< Không tìm thấy file! >");
        }
        System.out.println("----------------------------------------------------------------");
    }

    // Thêm cụm từ Anh- Việt vào từ điển
    public void addWord() {
        String word_target = Validation.getString("Nhập từ bạn muốn thêm: ");
        // boolean had_Word = false;
        // for (Word word : dictionary.getWords()) {
        //     if (word.getWord_target().equalsIgnoreCase(word_target)) {
        //         had_Word = true;
        //         // Khi gặp từ muốn thêm đã có trong từ điển
        //         if (!Validation.getYN(word_target + " đã có trong danh sách, bạn có muốn cập nhật không? Y/N : ")) {
        //             return;
        //         }
        //         else {
        //             editWord();
        //         }
        //     }
        // }

        int index = dictionary.indexOfBinarySearch(word_target, 0, dictionary.getWords().size() - 1);
        if (index >= 0){
            if (!Validation.getYN(word_target + " đã có trong danh sách, bạn có muốn cập nhật không? Y/N : ")) {
                return;
            }
            else {
                editWord();
            }
        }
        if (index == -1) {
            String word_explain = Validation.getString("Nhập nghĩa tiếng Việt: ");

            dictionary.addWordtoDictionary(new Word(word_target,word_explain));
            System.out.println("Thêm dữ liệu thành công !!");
            System.out.println("----------------------------------------------------------------");
        }
    }

    // Xóa cụm từ Anh- Việt khỏi từ điển
    public void deleteWord() {
        String word_target = Validation.getString("Nhập từ bạn muốn xóa: ");
        // for (int i = 0; i < dictionary.getWords().size(); i++) {
        //     if (dictionary.getWords().get(i).getWord_target().equalsIgnoreCase(word_target)) {

        //         dictionary.getWords().remove(i);
        //         System.out.println("Xóa thành công !!");
        //         check = true;
        //     }
        // }
        int index = dictionary.indexOfBinarySearch(word_target, 0, dictionary.getWords().size() - 1);
        if (index != -1) {
            dictionary.getWords().remove(index);
            System.out.println("Xóa thành công !!");
        }
        else System.err.println("Không tìm thấy từ muốn xóa trong từ điển :((");
        System.out.println("----------------------------------------------------------------");
    }

    // Sửa từ tiếng Anh
    public void editWord() {
        String word_target = Validation.getString("Nhập từ bạn muốn sửa: ");
//        boolean check = false;
//        for (Word word : dictionary.getWords()) {
//            if (word.getWord_target().equalsIgnoreCase(word_target)) {
//                String word_new = Validation.getString("Nhập từ mới: ");
//                String explain_new = Validation.getString("Nhập nghĩa tiếng Việt mới: ");
//                word.setWord_target(word_new);
//                word.setWord_explain(explain_new);
//                check = true;
//            }
//        }

        int index = dictionary.indexOfBinarySearch(word_target, 0, dictionary.getWords().size() - 1);
        if (index != -1) {
            String word_new = Validation.getString("Nhập từ mới: ");
            int indexOfWordNew = dictionary.indexOfBinarySearch(word_new, 0, dictionary.getWords().size() - 1);
            if (indexOfWordNew != -1) {
                if (!Validation.getYN(word_new + " đã có trong danh sách, bạn có muốn cập nhật không? Y/N : ")) {
                    return;
                }
            }
            String explain_new = Validation.getString("Nhập nghĩa tiếng Việt mới: ");
            dictionary.getWords().remove(index);
            dictionary.addWordtoDictionary(new Word(word_new, explain_new));
            System.out.println("Sửa thành công !");
        }
        else System.err.println("Không tìm thấy từ muốn sửa trong từ điển :((");
        System.out.println("----------------------------------------------------------------");
    }

    // Tìm kiếm từ các kí tự đầu
    public void dictionarySearch() {
        boolean check = false;
        String s = Validation.getString("Nhập kí tự đầu: ");
        int n = s.length();
        System.out.println(String.format("%-5s | %-15s | %-15s", "No", "English", "Vietnamese"));
        for (Word word : dictionary.getWords()) {
            if (word.getWord_target().length() >= n) {
                if (word.getWord_target().substring(0, n).equalsIgnoreCase(s)) {
                    int index = dictionary.getIndexToAdd(word.getWord_target());
                    System.out.println(String.format("%-5d | %-15s | %-15s", index + 1, word.getWord_target(), word.getWord_explain()));
                    check = true;
                }
            }
        }

        if (!check) System.err.println("Không tìm thấy từ có kí tự \"" + s + "\" trong từ điển. Nhập hành động mới: ");
        System.out.println("----------------------------------------------------------------");
    }

    //tìm kiếm từ
    public void lookUp() {
        //boolean check = false;
        String word_target = Validation.getString("Nhập từ tìm kiếm: ");
//        for (Word word : dictionary.getWords()) {
//            if (word.getWord_target().equalsIgnoreCase(word_target)) {
//                System.out.println(String.format("%-5s | %-15s | %-15s", "No", "English", "Vietnamese"));
//                check = true;
//                int index = dictionary.getIndex(word.getWord_target());
//                System.out.println(String.format("%-5d | %-15s | %-15s", index + 1, word.getWord_target(), word.getWord_explain()));
//            }
//        }
        int index = dictionary.indexOfBinarySearch(word_target, 0, dictionary.getWords().size() - 1);
        if (index != -1) {
            System.out.println(String.format("%-5s | %-15s | %-15s", "No", "English", "Vietnamese"));
            System.out.println(String.format("%-5d | %-15s | %-15s", index + 1, word_target, dictionary.getWords().get(index).getWord_explain()));
        }
        else System.err.println("Không tìm thấy " + word_target + " trong từ điển");
        System.out.println("----------------------------------------------------------------");
    }

    // ghi vào File txt
    public void dictionaryExportToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Word word : dictionary.getWords()) {
                String line = word.getWord_target() + "\t" + word.getWord_explain();
                writer.write(line);
                writer.newLine(); // Thêm dấu xuống dòng sau mỗi cặp từ và nghĩa
            }
            System.out.println("Xuất dữ liệu vào file " + path + " thành công!" );
        } catch (IOException e) {
            System.err.println("Không thể xuất dữ liệu vào file " + path + " :((");
        }
    }

    public void playGame() {
        Game game = new Game();
        game.gamePlaying();
    }

    public void display() {
        for (int i = 0; i < dictionary.getWords().size(); i++) {
            Word word = dictionary.getWords().get(i);
            System.out.println(String.format("%-5d | %-15s | %-15s", (i + 1), word.getWord_target(), word.getWord_explain()));
        }
        System.out.println("----------------------------------------------------------------");
    }
}