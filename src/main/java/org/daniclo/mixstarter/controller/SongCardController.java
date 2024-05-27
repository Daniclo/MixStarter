package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.daniclo.mixstarter.model.Song;

public class SongCardController {
    @FXML
    Label lbTitle;
    @FXML
    Label lbAuthor;

    public void setData(Song song){
        lbTitle.setText(song.getName());
        if (song.getPost() == null)
            lbAuthor.setText(song.getAlbum().getPost().getUser().getUsername());
        else
            lbAuthor.setText(song.getPost().getUser().getUsername());
    }
}
