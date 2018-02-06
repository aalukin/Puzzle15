package com.hse.aalukin.puzzle15;

// Student: Lukin Artur
// Group: BPI153(2)

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("puzzle15.fxml"));

        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.setTitle("Puzzle 15");
        primaryStage.setScene(new Scene(root, 690, 510));
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    /**
     * Program's entry point
     * @param args program's params
     */
    public static void main(String[] args) {
        launch(args);
    }
}