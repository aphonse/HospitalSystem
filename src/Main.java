import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static void main(String[] args) {
        Main.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/views/basic/LoginScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//        Media hit = new Media(getClass().getClassLoader().getResource("resourcefiles/sounds/notification.wav").toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(hit);
//        mediaPlayer.play();
        stage.initStyle(StageStyle.DECORATED);
        stage.getIcons().add(new Image("resources/images/22-Cardi-B-Money.png"));
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(123);
        });
        stage.setMaxWidth(1024.0);
        stage.setMaxHeight(600.0);
        stage.setTitle("E-Doc hospital system");
        stage.setMaxWidth(1200.0);
        stage.setMaxHeight(700.0);
        stage.setMaximized(false);
        stage.setFullScreen(false);
        stage.show();
    }
}