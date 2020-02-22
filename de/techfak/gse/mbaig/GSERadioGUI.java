package de.techfak.gse.mbaig;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GSERadioGUI extends Application {
    static String path;
    // globally accessible FXMLLoader & GSERadioPlayerView, init upon javafx.Application.launch
    static FXMLLoader fxmlLoader;
    static GSERadioPlayerView gsev;
    static GSERadioController controller;

    // standard init + FXML handling | DROP ANYTHING TO INIT HERE
    @Override
    public void start(Stage primaryStage) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GSERadioPlayer.fxml"));
        Pane root = fxmlLoader.load();
        gsev = fxmlLoader.getController();
        controller = new GSERadioController(path, gsev);
        Scene scene = new Scene(root);
        primaryStage.setTitle("GSERadio v1.0");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /* HOT-TIP: Override stop to properly exit the Player & release resources; customize stop!
    * Originally read here:
    * https://stackoverflow.com/a/26620713
    * Documentation:
    * https://docs.oracle.com/javase/8/javafx/api/javafx/application/Application.html
    */
    @Override
    public void stop() {
        controller.stopPlayback();
        Platform.exit();
    }
    // Entry-point
    public static void main(String[] args) {
        path = args[1];
        launch(args);
    }

    public static void main(String[] args, String givenPath) {
        path = givenPath;
        launch(args);
    }

    public static void addVote(int idx) {
        controller.addVote(idx);
    }
    // Additional bs

}
