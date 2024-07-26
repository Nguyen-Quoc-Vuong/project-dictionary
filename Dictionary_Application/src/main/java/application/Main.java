package application;

import application.util.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/application/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
        DbConnection.getInstance().getConnection();

        stage.setOnCloseRequest(event -> {
            DbConnection.getInstance().closeConnection();
        });
    }
    public static void main(String[] args) {
        launch();
    }
}