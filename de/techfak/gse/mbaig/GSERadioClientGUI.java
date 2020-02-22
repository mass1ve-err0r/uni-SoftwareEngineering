package de.techfak.gse.mbaig;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GSERadioClientGUI extends Application {

    static FXMLLoader fxmlLoader;
    static GSERadioClientView gsec;
    static GSERadioClientController controller;

    static final String REGEX_CHAR = ":";

    @Override
    public void start(Stage primaryStage) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GSERadioClient.fxml"));
        Pane root = fxmlLoader.load();
        gsec = fxmlLoader.getController();
        controller = new GSERadioClientController(gsec);
        Scene scene = new Scene(root);
        primaryStage.setTitle("GSERadio v1.0 :::Client-Mode:::");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        controller.timer.cancel();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void getResponse(String fullURL) {
        controller.checkConnection(fullURL);
    }
}
