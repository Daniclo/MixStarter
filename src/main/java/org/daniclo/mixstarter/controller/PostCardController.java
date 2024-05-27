package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.daniclo.mixstarter.model.Post;

public class PostCardController {
    @FXML
    private Label lbPostText;

    @FXML
    private Label lbPostTitle;

    @FXML
    private Label lbUsername;

    public void setData(Post post){
        lbUsername.setText(post.getUser().getUsername());
        lbPostTitle.setText(post.getTitle());
        lbPostText.setText(post.getText());
    }

}
