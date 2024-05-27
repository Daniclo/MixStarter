package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.model.Post;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class PostViewController {

    @FXML
    private Label lbAuthor;

    @FXML
    private Label lbText;

    @FXML
    private Label lbTitle;

    @FXML
    private VBox commentParent;

    public void setData(Post post){
        lbTitle.setText(post.getTitle());
        lbAuthor.setText("Made by " + post.getUser().getUsername());
        lbText.setText(post.getText());
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
}
