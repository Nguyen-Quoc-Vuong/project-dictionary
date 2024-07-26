
public class DictionaryCommandLine {
    private DictionaryManagement dictionaryManagement = new DictionaryManagement();
    private String path ; // path : src/dictionary.txt

    public void showAllWords() {
        System.out.println(String.format("%-5s | %-15s | %-15s", "No", "English", "Vietnamese"));
        dictionaryManagement.display();
    }

    public void dictionaryBasic() {
        dictionaryManagement.insertFromCommandline();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        while (true) {
            System.out.println("Welcome to my application!");
            System.out.println("[0] Exit\n[1] Add\n[2] Remove\n[3] Update\n[4] Display\n[5] Lookup\n[6] Search\n[7] Game\n[8] Import from file\n[9] Export to file");
            int action = Validation.getInt("Your Action: ", 0, 9);

            switch (action) {
                case 0:
                    System.out.print("Goodbye!\n");
                    return;
                case 1:
                    dictionaryManagement.addWord();
                    break;
                case 2:
                    dictionaryManagement.deleteWord();
                    break;
                case 3:
                    dictionaryManagement.editWord();
                    break;
                case 4:
                    showAllWords();
                    break;
                case 5:
                    dictionaryManagement.lookUp();
                    break;
                case 6:
                    dictionaryManagement.dictionarySearch();
                    break;
                case 7:
                    dictionaryManagement.playGame();
                    break;
                case 8:
                    path = "src/dictionary.txt";
                    this.dictionaryManagement.insertFromFile(path);
                    break;
                case 9:
                    path = Validation.getString("Nhập file : ");
                    dictionaryManagement.dictionaryExportToFile(path);
                    break;
                default:
                    break;
            }
        }
    }
}
