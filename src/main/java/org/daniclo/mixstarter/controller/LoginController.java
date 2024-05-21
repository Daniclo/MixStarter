package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.*;

public class LoginController {
    //region TESTING
    @FXML
    private void onClickTest(){
        TestDataAccess();
    }

    private static void TestDataAccess() {
        AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
        System.out.println("Albumes:");
        System.out.println(albumDAO.getAlbums());
        SongDAO songDAO = new SongDAOImpl(Song.class);
        System.out.println("Canciones:");
        System.out.println(songDAO.getSongs());
        CommentDAO commentDAO = new CommentDAOImpl(Comment.class);
        System.out.println("Comentarios:");
        System.out.println(commentDAO.getComments());
        PostDAO postDAO = new PostDAOImpl(Post.class);
        System.out.println("Posts:");
        System.out.println(postDAO.getPosts());
        TagDAO tagDAO = new TagDAOImpl(Tag.class);
        System.out.println("Tags:");
        System.out.println(tagDAO.getTags());
        UserDAO userDAO = new UserDAOImpl(User.class);
        System.out.println("Usuarios:");
        System.out.println(userDAO.getUsers());
        System.out.println("Seguidores y seguidos de Daniel Coll");
        User daniclo = userDAO.getByName("daniclo");
        var danicloFollowers = userDAO.getFollowers(daniclo.getId());
        var danicloFollowing = userDAO.getFollowing(daniclo.getId());
        System.out.println("Seguidores:");
        System.out.println(danicloFollowers);
        System.out.println("Siguiendo:");
        System.out.println(danicloFollowing);
    }
    //endregion

    @FXML
    TextField tfUsername;
    @FXML
    PasswordField tfPassword;

    @FXML
    private void onLogin(){
        if (!tfUsername.getText().isEmpty()){
          UserDAO userDAO = new UserDAOImpl(User.class);
          var user = userDAO.getByName(tfUsername.getText());
          if (user != null){
              if (tfPassword.getText().equals(user.getPassword()))
                  System.out.println("LOGIN COMPLETED");
              else System.err.println("Incorrect password.");
          }else {
              System.err.println("The username " + tfUsername.getText() + " does not exists.");
          }
        } else System.err.println("Username field must not be empty.");
    }
    @FXML
    private void onRegister(){
        if (!tfUsername.getText().isEmpty()) {
            UserDAO userDAO = new UserDAOImpl(User.class);
            var user = userDAO.getByName(tfUsername.getText());
            if (user == null){
                //IMPLEMENT PASSWORD REQUIREMENTS
                if (!tfPassword.getText().isEmpty()) {
                    var newUser = new User();
                    newUser.setUsername(tfUsername.getText());
                    newUser.setPassword(tfPassword.getText());
                    userDAO.save(newUser);
                    System.out.println("User " + newUser.getUsername() + " has been registered correctly.");
                }else System.err.println("Password requirements not met.");
            }else System.err.println("User already exists.");
        }
    }
}