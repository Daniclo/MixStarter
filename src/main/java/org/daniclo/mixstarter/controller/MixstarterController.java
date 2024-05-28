package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.model.User;
import org.daniclo.mixstarter.util.LoginData;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MixstarterController implements Initializable {

    private User user;
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    private final PostDAO postDAO = new PostDAOImpl(Post.class);

    @FXML
    private VBox albumParent;

    @FXML
    private VBox postParent;

    @FXML
    private ChoiceBox<String> chbSearch;

    @FXML
    private TextField tfSearch;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param url  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = LoginData.getCurrentUser();
        System.out.println(user);
        updateAlbumFeed();
        updateSocialFeed();
        initializeSearchChoiceBox();
    }

    private void updateSocialFeed() {
        //Change this for the posts decided by the algorithm for this user in particular later on.
        List<Post> postsToShow = postDAO.getPosts();
        if (!postsToShow.isEmpty()) initializePost(postsToShow);
    }

    private void initializePost(List<Post> posts) {
        try{
            for (Post post:posts){
                FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/postcard.fxml"));
                HBox cardBox = fxmlLoader.load();
                PostCardController cardController = fxmlLoader.getController();
                cardController.setData(post);
                postParent.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void initializeSearchChoiceBox() {
        chbSearch.getItems().add("Title");
        chbSearch.getItems().add("User");
        chbSearch.getItems().add("Tag");
    }

    private void updateAlbumFeed() {
        //Create/update general album for all liked songs
        List<Song> likedSongs = songDAO.getSongsLikedByUser(user);
        if (!likedSongs.isEmpty()) initializeAlbum(likedSongs,"Liked songs");
        //Create/update user specific albums
        List<Album> likedAlbums = albumDAO.getAlbumsLikedByUser(user);
        if (!likedAlbums.isEmpty())
            for (Album album:likedAlbums) initializeAlbum(album.getSongs(),album.getName());
    }

    private void initializeAlbum(List<Song> songs, String albumTitle) {
        Label title = new Label("Album: " + albumTitle);
        albumParent.getChildren().add(title);
        HBox hBox = new HBox();
        hBox.setSpacing(15);
        albumParent.getChildren().add(hBox);
        try {
            for (Song song: songs){
                FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/songcard.fxml"));
                VBox cardBox = fxmlLoader.load();
                SongCardController cardController = fxmlLoader.getController();
                cardController.setData(song);
                hBox.getChildren().add(cardBox);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void search(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/search.fxml"));
            BorderPane parent = fxmlLoader.load();
            Scene scene = new Scene(parent,600,400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
            stage.getIcons().add(stageIcon);
            stage.setScene(scene);
            SearchController controller = fxmlLoader.getController();
            controller.setData(tfSearch.getText(), chbSearch.getValue());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
