package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.daniclo.mixstarter.dao.*;
import org.daniclo.mixstarter.dropbox.DropboxAPI;
import org.daniclo.mixstarter.model.*;
import org.daniclo.mixstarter.util.LoginData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class PublishController {

    private Album album;
    private Song song;

    private File currentFile;

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
    private final SongDAO songDAO = new SongDAOImpl(Song.class);
    private final AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
    private final UserDAO userDAO = new UserDAOImpl(User.class);
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
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File folder = directoryChooser.showDialog(new Stage());
            if (folder != null) {
                AtomicBoolean invalidFolder = new AtomicBoolean(false);
                album = new Album();
                try (Stream<Path> paths = Files.list(folder.toPath());){
                    paths.forEach(path -> {
                        if (!invalidFolder.get() && !path.endsWith(".wav")) invalidFolder.set(true);
                        else{
                            String songname = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));
                            Song song = new Song();
                            song.setName(songname);
                            song.setAlbum(album);
                            song.setLength(100);
                            Tag tag = tagDAO.getTagByName(tfTag.getText());
                            if (tag != null) song.setTag(tag);
                            else {
                                tag = new Tag();
                                tag.setName(tfTag.getText());
                                tagDAO.create(tag);
                                song.setTag(tag);
                            }
                            if (tag.getName().isEmpty() && tagDAO.findById(1L).isPresent()){
                                tag = tagDAO.findById(1L).get();
                                song.setTag(tag);
                            }
                            tagDAO.save(tag);
                        }
                    });
                    if (invalidFolder.get()) return;
                    currentFile = folder;
                    lbFiles.setText(folder.getName());
                    album.setName(folder.getName().substring(0, folder.getName().lastIndexOf(".")));
                    Tag tag = tagDAO.getTagByName(tfTag.getText());
                    if (tag != null) album.setTag(tag);
                    else {
                        tag = new Tag();
                        tag.setName(tfTag.getText());
                        tagDAO.create(tag);
                        album.setTag(tag);
                    }
                    if (tag.getName().isEmpty() && tagDAO.findById(1L).isPresent()){
                        tag = tagDAO.findById(1L).get();
                        album.setTag(tag);
                    }
                    tagDAO.save(tag);
                    albumDAO.create(album);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }else{
            song = null;
            album = null;
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Audio Files", "*.wav");
            fileChooser.setSelectedExtensionFilter(filter);
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null){
                currentFile = file;
                String songName = file.getName().substring(0, file.getName().lastIndexOf("."));
                lbFiles.setText(songName);
                song = new Song();
                song.setName(songName);
                song.setLength(100);
                Tag tag = tagDAO.getTagByName(tfTag.getText());
                if (tag != null) song.setTag(tag);
                else {
                    tag = new Tag();
                    tag.setName(tfTag.getText());
                    tagDAO.create(tag);
                    song.setTag(tag);
                }
                if (tag.getName().isEmpty() && tagDAO.findById(1L).isPresent()){
                    tag = tagDAO.findById(1L).get();
                    song.setTag(tag);
                }
                tagDAO.save(tag);
                songDAO.create(song);
            }
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
            songDAO.save(song);
            post.setUser(LoginData.getCurrentUser());
            userDAO.save(LoginData.getCurrentUser());
            postDAO.create(post);
            DropboxAPI.uploadFile(currentFile,DropboxAPI.getAuth());
            closeWindow(event);
        } else if (album != null){
            Post post = new Post();
            post.setTitle(tfTitle.getText());
            post.setText(textArea.getText());
            post.setAlbum(album);
            albumDAO.save(album);
            post.setUser(LoginData.getCurrentUser());
            userDAO.save(LoginData.getCurrentUser());
            postDAO.create(post);
            DropboxAPI.uploadFile(currentFile,DropboxAPI.getAuth());
            closeWindow(event);
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
