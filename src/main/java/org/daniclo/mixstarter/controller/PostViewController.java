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
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.LoginData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostViewController {

    private Post post;

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    private final UserDAO userDAO = new UserDAOImpl(User.class);
    private final GenericDAO<UserLikesSong> userLikesSongDAO = new GenericDAOImpl<>(UserLikesSong.class);
    private final GenericDAO<UserLikesAlbum> userLikesAlbumDAO = new GenericDAOImpl<>(UserLikesAlbum.class);

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
        List<Album> likedAlbums = albumDAO.getAlbumsLikedByUser(LoginData.getCurrentUser());
        List<Song> likedSongs = songDAO.getSongsLikedByUser(LoginData.getCurrentUser());
        List<Post> likedPosts = new ArrayList<>();
        for (Song song:likedSongs)
            if (song.getPost() != null) likedPosts.add(song.getPost());
        for (Album album:likedAlbums)
            if (album.getPost() != null) likedPosts.add(album.getPost());
        for (Post likedPost:likedPosts)
            if (Objects.equals(likedPost.getId(), post.getId())){
                btLikePost.setText("Unlike post");
            }else {
                btLikePost.setText("Like post");
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
                UserLikesSong entity = new UserLikesSong();
                entity.setUser(LoginData.getCurrentUser());
                entity.setSong(post.getSong());
                userDAO.save(LoginData.getCurrentUser());
                songDAO.save(post.getSong());
                userLikesSongDAO.create(entity);
            }
            if (post.getAlbum() != null){
                UserLikesAlbum entity = new UserLikesAlbum();
                entity.setUser(LoginData.getCurrentUser());
                entity.setAlbum(post.getAlbum());
                userDAO.save(LoginData.getCurrentUser());
                albumDAO.save(post.getAlbum());
                userLikesAlbumDAO.create(entity);
            }
        }else {
            if (post.getSong() != null){
                UserLikesSong entity = new UserLikesSong();
                entity.setUser(LoginData.getCurrentUser());
                entity.setSong(post.getSong());
                userDAO.save(LoginData.getCurrentUser());
                songDAO.save(post.getSong());
                userLikesSongDAO.delete(entity);
            }
            if (post.getAlbum() != null){
                UserLikesAlbum entity = new UserLikesAlbum();
                entity.setUser(LoginData.getCurrentUser());
                entity.setAlbum(post.getAlbum());
                userDAO.save(LoginData.getCurrentUser());
                albumDAO.save(post.getAlbum());
                userLikesAlbumDAO.delete(entity);
            }
        }
        initializeLike();
    }

    private void reload() {
        post = postDAO.getPostsByTitle(post.getTitle()).getFirst();
        setData(post);
    }
}