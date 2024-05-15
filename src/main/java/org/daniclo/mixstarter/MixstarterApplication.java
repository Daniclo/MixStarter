package org.daniclo.mixstarter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MixstarterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/mixstarter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setMaximized(true);
        Image image = new Image(getClass().getResourceAsStream("icons/MixLogo.png"));
        stage.getIcons().add(image);
        stage.setTitle("MixStarter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}