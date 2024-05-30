package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.daniclo.mixstarter.model.Album;
import org.daniclo.mixstarter.model.Song;

public class SongCardController {

    public Song song;
    public Album album;

    @FXML
    private Label lbTitle;
    @FXML
    private Label lbAuthor;

    public void setData(Song song){
        lbTitle.setText(song.getName());
        if (song.getPost() == null)
            lbAuthor.setText(song.getAlbum().getPost().getUser().getUsername());
        else
            lbAuthor.setText(song.getPost().getUser().getUsername());
    }
}