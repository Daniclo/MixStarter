package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.data.*;
import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.LoginData;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostViewController {

    private Post post;

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    private final UserLikesSongDAO userLikesSongDAO = new UserLikesSongDAOImpl(UserLikesSong.class);
    private final UserLikesAlbumDAO userLikesAlbumDAO = new UserLikesAlbumDAOImpl(UserLikesAlbum.class);

    @FXML
    private Button btLikePost;

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
        initializeLike();
    }

    private void initializeLike() {
        if (post.getAlbum() == null){
            List<Song> songs = songDAO.getSongsLikedByUser(LoginData.getCurrentUser());
            AtomicBoolean found = new AtomicBoolean(false);
            songs.forEach(song -> {
                if (song.getId() == post.getSong().getId()) found.set(true);
            });
            if (found.get()) btLikePost.setText("Unlike post");
            else btLikePost.setText("Like post");
        }else{
            List<Album> albums = albumDAO.getAlbumsLikedByUser(LoginData.getCurrentUser());
            AtomicBoolean found = new AtomicBoolean(false);
            albums.forEach(album -> {
                if (album.getId() == post.getAlbum().getId()) found.set(true);
            });
            if (found.get()) btLikePost.setText("Unlike post");
            else btLikePost.setText("Like post");
        }
    }

    private void initializeComments(List<Comment> comments) {
        commentParent.getChildren().removeAll();
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
    @FXML
    private void addComment(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/addcomment.fxml"));
            HBox parent = fxmlLoader.load();
            Scene scene = new Scene(parent,600,150);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
            stage.getIcons().add(stageIcon);
            AddCommentController controller = fxmlLoader.getController();
            controller.setData(post);
            stage.showAndWait();
            reload();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void likePost(){
        if (btLikePost.getText().equals("Like post")){
            if (post.getSong() != null){
                UserLikesSong us = new UserLikesSong(LoginData.getCurrentUser(), post.getSong());
                userLikesSongDAO.save(us);
            }
            if (post.getAlbum() != null){
                UserLikesAlbum us = new UserLikesAlbum(LoginData.getCurrentUser(), post.getAlbum());
                userLikesAlbumDAO.save(us);
            }
        }else {
            if (post.getSong() != null){
                UserLikesSong us = userLikesSongDAO.findByUserAndSong(LoginData.getCurrentUser(), post.getSong());
                userLikesSongDAO.delete(us);
            }
            if (post.getAlbum() != null){
                UserLikesAlbum us = userLikesAlbumDAO.findByUserAndAlbum(LoginData.getCurrentUser(), post.getAlbum());
                userLikesAlbumDAO.delete(us);
            }
        }
        initializeLike();
    }

    private void reload() {
        commentParent.getChildren().clear();
        post = postDAO.getPostsByTitle(post.getTitle()).getFirst();
        setData(post);
    }
}