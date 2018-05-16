package store.main;

import store.database.models.user.User;
import store.database.models.dao.MySQL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application
{
    static Stage primaryStage;
    public static User user;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane root = new BorderPane();

        root.setTop(getTopContent());
        root.setCenter(getCenterContent());

        Scene scene = new Scene(root);
        this.primaryStage = new Stage();
        this.primaryStage.setTitle("AppStore");
        this.primaryStage.setScene(scene);
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public static Parent getTopContent() throws IOException
    {
        return FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("store/fxml/menu/menu.fxml")));
    }

    public Parent getCenterContent() throws IOException
    {
        return FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("store/fxml/home/home.fxml")));
    }

    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public static void main(String[] args)
    {
        MySQL.Connect();
        launch(args);
        MySQL.Disconnect();
    }
}
