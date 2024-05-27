package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.model.Post;

import java.io.IOException;
import java.util.Objects;

public class PostCardController {

    private Post post;

    @FXML
    private Label lbPostText;

    @FXML
    private Label lbPostTitle;

    @FXML
    private Label lbUsername;

    public void setData(Post post){
        this.post = post;
        lbUsername.setText(post.getUser().getUsername());
        lbPostTitle.setText(post.getTitle());
        lbPostText.setText(post.getText());
    }

    @FXML
    private void showPost(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/postview.fxml"));
            BorderPane parent = fxmlLoader.load();
            Scene scene = new Scene(parent,600,400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
            stage.getIcons().add(stageIcon);
            stage.setScene(scene);
            PostViewController controller = fxmlLoader.getController();
            controller.setData(post);
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    private void showUser(){
       try{
           FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/userview.fxml"));
           BorderPane parent = fxmlLoader.load();
           Scene scene = new Scene(parent,600,400);
           Stage stage = new Stage();
           stage.initModality(Modality.APPLICATION_MODAL);
           Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
           stage.getIcons().add(stageIcon);
           stage.setScene(scene);
           UserViewController controller = fxmlLoader.getController();
           controller.setData(post.getUser());
           stage.showAndWait();
       } catch (IOException e) {
           System.err.println(e.getMessage());
       }
    }
}