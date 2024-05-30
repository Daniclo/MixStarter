package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.daniclo.mixstarter.data.*;
import org.daniclo.mixstarter.model.Comment;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.util.LoginData;

public class AddCommentController {

    private Post post;
    private final CommentDAO commentDAO = new CommentDAOImpl(Comment.class);
    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final UserDAO userDAO = new UserDAOImpl(User.class);

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
        userDAO.save(LoginData.getCurrentUser());
        postDAO.save(post);
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
