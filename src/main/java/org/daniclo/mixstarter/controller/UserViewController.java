package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.data.*;
import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.LoginData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserViewController {

    private User user;

    private final UserDAO userDAO = new UserDAOImpl(User.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final GenericDAO<Followers> followersDAO = new GenericDAOImpl<>(Followers.class);

    @FXML
    private Button btFollow;

    @FXML
    private Label lbLikedPosts;

    @FXML
    private Label lbUsername;

    @FXML
    private VBox likedPostParent;

    @FXML
    private VBox postParent;

    public void setData(User user) {
        this.user = user;
        lbUsername.setText(user.getUsername());
        if (!(user.getPosts().isEmpty())){
            List<Post> publishedPosts = postDAO.getPostsByUser(user.getUsername());
            initializePost(publishedPosts,postParent);
        }
        if (user.isPublicLikes()){
            lbLikedPosts.setText("Liked posts");
            List<Song> likedSongs = songDAO.getSongsLikedByUser(user);
            List<Album> likedAlbums = albumDAO.getAlbumsLikedByUser(user);
            List<Post> likedPosts = new ArrayList<>();
            for (Song song:likedSongs)
                if (song.getPost() != null) likedPosts.add(song.getPost());
            for (Album album:likedAlbums)
                if (album.getPost() != null) likedPosts.add(album.getPost());
            initializePost(likedPosts, likedPostParent);
        }
        else lbLikedPosts.setText("This user has set their likes to private");
        if (Objects.equals(LoginData.getCurrentUser().getId(), user.getId())){
            btFollow.setText("Yourself");
            return;
        }
        List<User> following = userDAO.getFollowing(LoginData.getCurrentUser().getId());
        for (User follow:following){
            if (!follow.getUsername().equals(user.getUsername())) continue;
            btFollow.setText("Unfollow");
        }
    }

    private void initializePost(List<Post> posts, VBox parent) {
        parent.getChildren().clear();
        try{
            for (Post post:posts){
                FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/postcard.fxml"));
                HBox cardBox = fxmlLoader.load();
                PostCardController cardController = fxmlLoader.getController();
                cardController.setData(post);
                parent.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void onFollow(){
        switch (btFollow.getText()){
            case "Follow" ->{
                Followers entity = new Followers();
                FollowersPK pk = new FollowersPK();
                pk.setUserFollowed(user.getId());
                pk.setUserFollows(LoginData.getCurrentUser().getId());
                userDAO.save(user);
                userDAO.save(LoginData.getCurrentUser());
                entity.setFollowed(user);
                entity.setFollows(LoginData.getCurrentUser());
                entity.setFollowersPK(pk);
                followersDAO.save(entity);
                setData(user);
            }
            case "Unfollow" ->{

            }
        }
    }
}
