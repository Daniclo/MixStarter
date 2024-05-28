package org.daniclo.mixstarter.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.dao.PostDAO;
import org.daniclo.mixstarter.dao.PostDAOImpl;
import org.daniclo.mixstarter.model.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private final PostDAO postDAO = new PostDAOImpl(Post.class);
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
            case "Tag" -> postsToShow = postDAO.getPostsByTag(searchText);
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