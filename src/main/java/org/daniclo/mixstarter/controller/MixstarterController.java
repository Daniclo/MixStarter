package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MixstarterController implements Initializable {

    private final UserDAO userDAO = new UserDAOImpl(User.class);
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

    @FXML
    private CheckBox cbxPublicLikes;

    @FXML
    private TextField tfUpdatePassword;

    @FXML
    private TextField tfUpdateUsername;

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
        initializeScheduledUpdate();
        initializeSearchChoiceBox();
        initializePublicLikes();
    }

    private void initializeScheduledUpdate() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this::updateSocialFeed,
                5,
                5,
                TimeUnit.SECONDS);
        //Stage stage = (Stage)albumParent.getScene().getWindow();
        //stage.setOnCloseRequest(windowEvent -> service.shutdown());
    }

    private void initializePublicLikes() {
        cbxPublicLikes.setSelected(LoginData.getCurrentUser().isPublicLikes());
    }

    private void updateSocialFeed() {
        //Change this for the posts decided by the algorithm for this user in particular later on.
        List<Post> postsToShow = postDAO.getPosts();
        if (!postsToShow.isEmpty()) initializePost(postsToShow);
        System.out.println("Social feed updated");
    }

    private void initializePost(List<Post> posts) {
        postParent.getChildren().removeAll();
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
        title.setTextFill(Paint.valueOf("#07b0f2"));
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
    @FXML
    private void logout(ActionEvent event){
        LoginData.setCurrentUser(null);
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/login.fxml"));
            BorderPane parent = fxmlLoader.load();
            Scene scene = new Scene(parent,600,400);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setResizable(false);
            stage.setTitle("MixStarter");
            Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
            stage.getIcons().add(stageIcon);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
            controller.setData(LoginData.getCurrentUser());
            stage.showAndWait();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    private void updateUsername(){
        if (tfUpdateUsername.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username field must not be empty.");
            alert.setTitle("Username was not changed.");
            alert.showAndWait();
            return;
        }
        List<User> allUsers = userDAO.getUsers();
        for (User u:allUsers)
            if (u.getUsername().equals(tfUpdateUsername.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username must be unique and cannot be the same" +
                        " as any other existing username.");
                alert.setTitle("Username was not changed.");
                alert.showAndWait();
                return;
            }
        LoginData.getCurrentUser().setUsername(tfUpdateUsername.getText());
        userDAO.save(LoginData.getCurrentUser());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Username was changed successfully. " +
                "Your new username is " + LoginData.getCurrentUser().getUsername() + ".");
        alert.setTitle("Username was changed.");
        alert.showAndWait();
    }
    @FXML
    private void updatePassword(){
        if (tfUpdatePassword.getText().isEmpty()) return;
        if (passwordCriteriaMet(tfUpdatePassword.getText())){
            LoginData.getCurrentUser().setPassword(tfUpdatePassword.getText());
            userDAO.save(LoginData.getCurrentUser());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password was changed successfully.");
            alert.setTitle("Password was changed.");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Password must be at least 8 characters long," +
                    " contain one capital letter and number and be different from the username.");
            alert.setTitle("Password requirements not met.");
            alert.showAndWait();
        }
    }
    @FXML
    private void updateLikes(){
        LoginData.getCurrentUser().setPublicLikes(cbxPublicLikes.isSelected());
        Alert alert;
        if (LoginData.getCurrentUser().isPublicLikes()){
            alert = new Alert(Alert.AlertType.INFORMATION, "Your likes are now public");
        }else{
            alert = new Alert(Alert.AlertType.INFORMATION, "Your likes are now private");
        }
        alert.setTitle("Like privacy changed.");
        alert.showAndWait();
    }
    private boolean passwordCriteriaMet(String password) {
        //Longer than 8
        if (password.length() < 8) return false;
        //Can't be same as username
        if (LoginData.getCurrentUser().getUsername().equals(password)) return false;
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
}