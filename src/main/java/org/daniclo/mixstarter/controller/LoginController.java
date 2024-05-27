package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.LoginData;

import java.io.IOException;

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
        System.out.println("Likes:");
        System.out.println(songDAO.getSongsLikedByUser(userDAO.getByName("daniclo")));
        System.out.println(albumDAO.getAlbumsLikedByUser(userDAO.getByName("daniclo")));
    }
    //endregion

    @FXML
    TextField tfUsername;
    @FXML
    PasswordField tfPassword;
    @FXML
    Label lbLoginInfo;

    @FXML
    private void onLogin(ActionEvent event){
        if (!tfUsername.getText().isEmpty()){
          UserDAO userDAO = new UserDAOImpl(User.class);
          var user = userDAO.getByName(tfUsername.getText());
          if (user != null){
              if (tfPassword.getText().equals(user.getPassword())){
                  String message = "LOGIN COMPLETED";
                  startApplication(event, message, user);
              }
              else {
                  System.err.println("Incorrect password.");
                  lbLoginInfo.setText("Incorrect password.");
              }
          }else {
              System.err.println("The username " + tfUsername.getText() + " does not exists.");
              lbLoginInfo.setText("The username " + tfUsername.getText() + " does not exists.");
          }
        } else{
            System.err.println("Username field must not be empty.");
            lbLoginInfo.setText("Username field must not be empty.");
        }
    }
    @FXML
    private void onRegister(ActionEvent event){
        if (!tfUsername.getText().isEmpty()) {
            UserDAO userDAO = new UserDAOImpl(User.class);
            var user = userDAO.getByName(tfUsername.getText());
            if (user == null){
                if (!tfPassword.getText().isEmpty() && passwordCriteriaMet(tfPassword.getText())) {
                    var newUser = new User();
                    newUser.setUsername(tfUsername.getText());
                    newUser.setPassword(tfPassword.getText());
                    userDAO.save(newUser);
                    String message = "User " + newUser.getUsername() + " has been registered correctly.";
                    startApplication(event, message, newUser);
                } else {
                    System.err.println("Password must be at least 8 characters long, contain one capital letter and number and be different from the username.");
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be at least 8 characters long," +
                            " contain one capital letter and number and be different from the username.");
                    alert.setTitle("Password requirements not met.");
                    alert.showAndWait();
                }
            } else {
                System.err.println("User already exists.");
                lbLoginInfo.setText("User already exists.");
            }
        }else{
            System.err.println("Username field must not be empty.");
            lbLoginInfo.setText("Username field must not be empty.");
        }
    }

    private boolean passwordCriteriaMet(String password) {
        //Longer than 8
        if (password.length() < 8) return false;
        //Can't be same as username
        if (tfUsername.getText().equals(password)) return false;
        //Must have at least 1 capital letter and 1 number
        int capitalCounter=0;
        int numberCounter=0;
        for(String c:password.split("")){
            if (c.matches("[0-9]")) numberCounter++;
            if (c.matches("[A-Z]")) capitalCounter++;
        }
        if (capitalCounter == 0) return false;
        if (numberCounter == 0) return false;
        return true;
    }

    private void startApplication(ActionEvent event, String message, User user){
        LoginData.setCurrentUser(user);
        System.out.println(message);
        lbLoginInfo.setText(message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/mixstarter.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root,900,600);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}