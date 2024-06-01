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
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.model.Post;

import java.io.IOException;
import java.util.Objects;

public class CommentCardController {

    private Post post;

    @FXML
    private Label lbText;

    @FXML
    private Label lbUsername;

    public void setData(Comment comment){
        post = comment.getPost();
        lbUsername.setText(comment.getUser().getUsername());
        lbText.setText(comment.getText());
    }

    @FXML
    private void showUser() {
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
