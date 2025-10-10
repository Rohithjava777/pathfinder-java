package source.cspathfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    //Overriding
    @Override
    public void start(Stage stage) throws IOException {
        //set up the scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Scene.fxml"));
        //set the size of the application
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        //set the title of the application
        stage.setTitle("PathFinding visualiser");
        //dont let user resize it
        stage.setResizable(false);
        //set scene
        stage.setScene(scene);
        //show scene
        stage.show();
    }

    //run the application
    public static void main(String[] args) {
        launch();
    }
}

