package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.daniclo.mixstarter.dao.AlbumDAO;
import org.daniclo.mixstarter.dao.AlbumDAOImpl;
import org.daniclo.mixstarter.model.Album;

public class LoginController {
    @FXML
    private void onClickTest(){
        TestDataAccess();
    }

    private static void TestDataAccess() {
        AlbumDAO albumDAO = new AlbumDAOImpl(Album.class);
        System.out.println(albumDAO.getAlbums());
    }
}