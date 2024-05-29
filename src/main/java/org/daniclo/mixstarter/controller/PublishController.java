package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Post;
import org.daniclo.mixstarter.model.Song;
import org.daniclo.mixstarter.model.Tag;
import org.daniclo.mixstarter.util.LoginData;

import java.io.File;

public class PublishController {

    Album album;
    Song song;

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final TagDAO tagDAO = new TagDAOImpl(Tag.class);

    @FXML
    private CheckBox cbxIsAlbum;

    @FXML
    private Label lbFiles;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfTag;

    @FXML
    void chooseFiles() {
        if (cbxIsAlbum.isSelected()){
            song = null;
            album = null;
            //album creation functionality. For now, just songs.
        }else{
            song = null;
            album = null;
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            lbFiles.setText(file.getName());
            song = new Song();
            song.setName(file.getName());
            song.setLength(100);
            Tag tag = tagDAO.getTagByName(tfTag.getText());
            if (tag != null) song.setTag(tag);
            else {
                tag = new Tag();
                tag.setName(tfTag.getText());
                song.setTag(tag);
            }
            if (tag.getName().isEmpty() && tagDAO.findById(1L).isPresent()){
                tag = tagDAO.findById(1L).get();
                song.setTag(tag);
            }
            songDAO.create(song);
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        if (tfTitle.getText().isEmpty()){
            tfTitle.setPromptText("Field must not be empty.");
            tfTitle.requestFocus();
            return;
        }
        if (textArea.getText().isEmpty()){
            textArea.setPromptText("Field must not be empty.");
            textArea.requestFocus();
            return;
        }
        if (song != null){
            Post post = new Post();
            post.setTitle(tfTitle.getText());
            post.setText(textArea.getText());
            post.setSong(song);
            post.setUser(LoginData.getCurrentUser());
            postDAO.create(post);
            closeWindow(event);
        } else if (album != null){
            //Album functionality.
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Before pressing the OK button, you must " +
                    "choose an album or song in your file system.");
            alert.setTitle("You must select an album or song.");
            alert.showAndWait();
        }
    }

    @FXML
    void discard(ActionEvent event) {
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
