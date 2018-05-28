package store.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import store.database.models.dao.MySQL;
import store.database.models.user.User;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application
{
    static Stage primaryStage;
    public static User user;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane root = new BorderPane();

        root.setTop(getTopContent());
        root.setCenter(getCenterContent());

        Scene scene = new Scene(root);
        scene.getStylesheets().add("store/resources/css/JMetroLightTheme.css");

        this.primaryStage = new Stage();
        this.primaryStage.setTitle("AppStore");
        this.primaryStage.setScene(scene);
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    /**
     * Returns the top content
     *
     * @return Parent Content of the top
     * @throws IOException The file does not exist
     */
    public static Parent getTopContent() throws IOException
    {
        return FXMLLoader.load(Objects.requireNonNull(Main.class.getClassLoader().getResource("store/fxml/menu/menu.fxml")));
    }

    /**
     * Returns the center content
     *
     * @return Parent Center content
     * @throws IOException The file does not exist
     */
    private Parent getCenterContent() throws IOException
    {
        return FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("store/fxml/home/home.fxml")));
    }

    /**
     * Returns the stage
     *
     * @return Stage stage of the window
     */
    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }

    /**
     * Starts the java application
     *
     * @param args Not used
     */
    public static void main(String[] args)
    {
        MySQL.Connect();
        launch(args);
        MySQL.Disconnect();
    }
}
