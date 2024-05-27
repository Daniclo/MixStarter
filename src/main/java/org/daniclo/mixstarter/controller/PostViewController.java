package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.model.Post;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PostViewController {

    private Post post;

    @FXML
    private Label lbAuthor;

    @FXML
    private Label lbText;

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbTag;

    @FXML
    private VBox commentParent;


    public void setData(Post post){
        this.post = post;
        lbTitle.setText(post.getTitle());
        lbAuthor.setText("Made by " + post.getUser().getUsername());
        lbText.setText(post.getText());
        if (post.getAlbum() != null) lbTag.setText(post.getAlbum().getTag().getName().toUpperCase());
        else lbTag.setText(post.getSong().getTag().getName().toUpperCase());
        List<Comment> comments = post.getComments();
        initializeComments(comments);
    }

    private void initializeComments(List<Comment> comments) {
        for (Comment comment:comments){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/commentcard.fxml"));
                HBox cardBox = fxmlLoader.load();
                CommentCardController controller = fxmlLoader.getController();
                controller.setData(comment);
                commentParent.getChildren().add(cardBox);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
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
