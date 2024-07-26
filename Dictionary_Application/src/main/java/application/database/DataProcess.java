package application.database;
import application.util.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DataProcess {

    public void addWordToDatabase(String word, String meaning) throws SQLException {
        String sql = "INSERT INTO av (word, description) VALUES (?, ?)";
//        DbConnection connect = new DbConnection();
        try (Connection connectDB = DbConnection.getInstance().getConnection()){
            PreparedStatement statement = connectDB.prepareStatement(sql);

            statement.setString(1, word);
            statement.setString(2, meaning);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWordFromDatabase(String word) throws SQLException {
        String sql = "DELETE FROM av WHERE word = ?";
        try (Connection connectDB = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connectDB.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.executeUpdate();
        }
    }

    public boolean wordExistsInDatabase(String word) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM av WHERE word = ?)";
        try (Connection connectDB = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connectDB.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public void updateWordInDatabase(String word, String newMeaning) throws SQLException {
        String sql = "UPDATE av SET description = ? WHERE word = ?";
        try (Connection connectDB = DbConnection.getInstance().getConnection();
             PreparedStatement statement = connectDB.prepareStatement(sql)) {
            statement.setString(1, newMeaning);
            statement.setString(2, word);
            statement.executeUpdate();
        }
    }
}
