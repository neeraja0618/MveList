package project0;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Watchlist watchlist = DataManager.load();
        MainScreen mainScreen = new MainScreen(watchlist, stage);
        mainScreen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}