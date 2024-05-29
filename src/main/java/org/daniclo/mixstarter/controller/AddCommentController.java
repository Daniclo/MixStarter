package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.daniclo.mixstarter.dao.CommentDAO;
import org.daniclo.mixstarter.dao.CommentDAOImpl;
import org.daniclo.mixstarter.dao.PostDAO;
import org.daniclo.mixstarter.dao.PostDAOImpl;
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.util.LoginData;

public class AddCommentController {

    private Post post;
    private final CommentDAO commentDAO = new CommentDAOImpl(Comment.class);

    @FXML
    private TextArea commentText;

    @FXML
    private void confirm(ActionEvent event) {
        if (commentText.getText().isEmpty()){
            commentText.setPromptText("Comment must not be empty.");
            commentText.requestFocus();
            return;
        }
        Comment comment = new Comment();
        comment.setText(commentText.getText());
        comment.setUser(LoginData.getCurrentUser());
        comment.setPost(post);
        commentDAO.create(comment);
        closeWindow(event);
    }
    @FXML
    private void discard(ActionEvent event) {
        closeWindow(event);
    }
    public void closeWindow(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void setData(Post post){
        this.post = post;
    }
}
