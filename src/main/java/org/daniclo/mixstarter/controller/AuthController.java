package org.daniclo.mixstarter.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Scanner;

public class AuthController {

    public static String authCode;

    @FXML
    private Hyperlink link;

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

    public void startAuth(String authURL){

    }

    private void closeWindow(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
