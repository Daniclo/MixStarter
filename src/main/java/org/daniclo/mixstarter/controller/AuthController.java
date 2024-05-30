package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class AuthController {

    public static String authCode;

    public String authURL;

    @FXML
    private TextField tfCode;

    @FXML
    void cancel(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void confirm(ActionEvent event) {
        if (tfCode.getText().isEmpty()){
            tfCode.setPromptText("Input your code here.");
            tfCode.requestFocus();
            return;
        }
        String code = tfCode.getText();
        if (code != null) {
            code = code.trim();
            authCode = code;
        }
        closeWindow(event);
    }

    @FXML
    private void openLink(){
        try {
            Desktop.getDesktop().browse(URI.create(authURL));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startAuth(String authURL){
        this.authURL = authURL;
        try {
            Desktop.getDesktop().browse(URI.create(this.authURL));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void closeWindow(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
