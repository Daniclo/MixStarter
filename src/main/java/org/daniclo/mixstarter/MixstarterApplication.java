package org.daniclo.mixstarter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.daniclo.mixstarter.util.HibernateUtil;

import java.io.IOException;
import java.util.Objects;

public class MixstarterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setResizable(false);
        Image stageIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icons/MixLogo.png")));
        stage.getIcons().add(stageIcon);
        stage.setTitle("MixStarter");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        launch();
    }
}