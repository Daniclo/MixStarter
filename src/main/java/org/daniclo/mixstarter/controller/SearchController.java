package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    @FXML
    private Label lbNumberResults;
    @FXML
    private Label lbSearchData;
    @FXML
    private VBox postsParent;

    public void setData(String searchText, String searchType){
        lbSearchData.setText(searchText + " in " + searchType + "s");
        List<Post> postsToShow = new ArrayList<>();
        switch (searchType){
            case "Title" -> postsToShow = postDAO.getPostsByTitle(searchText);
            case "User" -> postsToShow = postDAO.getPostsByUser(searchText);
            case "Tag" -> {
                List<Song> songs = songDAO.getSongsByTag(searchText);
                List<Album> albums = albumDAO.getAlbumsByTag(searchText);
                for (Song song:songs)
                    if (song.getPost() != null)
                        postsToShow.add(song.getPost());
                for (Album album:albums)
                    if (album.getPost() != null)
                        postsToShow.add(album.getPost());
            }
        }
        if (postsToShow == null || postsToShow.isEmpty()){
            lbNumberResults.setText("No results where found for this search.");
            return;
        }
        lbNumberResults.setText(postsToShow.size() + " results found.");
        initializePosts(postsToShow);
    }

    private void initializePosts(List<Post> posts) {
        for (Post post:posts){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/postcard.fxml"));
                HBox cardBox = fxmlLoader.load();
                PostCardController controller = fxmlLoader.getController();
                controller.setData(post);
                postsParent.getChildren().add(cardBox);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}