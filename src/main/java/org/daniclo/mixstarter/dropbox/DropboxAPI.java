package org.daniclo.mixstarter.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.WriteMode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.daniclo.mixstarter.MixstarterApplication;
import org.daniclo.mixstarter.controller.AuthController;

import java.io.*;
import java.util.Date;
import java.util.Objects;

public class DropboxAPI {
    private static DbxClientV2 getAuth(){
        try{
            DbxRequestConfig requestConfig = new DbxRequestConfig("MixStarter/1.0");
            DbxAppInfo appInfo = DbxAppInfo.Reader.readFromFile("api.app");
            DbxWebAuth auth = new DbxWebAuth(requestConfig, appInfo);

            DbxWebAuth.Request authRequest = DbxWebAuth.newRequestBuilder()
                    .withNoRedirect()
                    .build();
            String authorizeUrl = auth.authorize(authRequest);

            FXMLLoader fxmlLoader = new FXMLLoader(MixstarterApplication.class.getResource("fxml/auth.fxml"));
            VBox parent = fxmlLoader.load();
            Scene scene = new Scene(parent,600,400);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Image stageIcon = new Image(Objects.requireNonNull(MixstarterApplication.class.getResourceAsStream("icons/MixLogo.png")));
            stage.getIcons().add(stageIcon);
            stage.setScene(scene);
            AuthController controller = fxmlLoader.getController();
            controller.startAuth(authorizeUrl);
            stage.showAndWait();
            if (AuthController.authCode != null && !AuthController.authCode.isEmpty()) {
                DbxAuthFinish authFinish = auth.finishFromCode(AuthController.authCode);
                return new DbxClientV2(requestConfig, authFinish.getAccessToken());
            }else return null;
        } catch (JsonReader.FileLoadException | DbxException | IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void uploadFile(File file, DbxClientV2 client){
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        // Get current account info to check if it's working
        try {
            System.out.println(client.users().getCurrentAccount().getName().getDisplayName());
        } catch (DbxException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
        //Upload file
        try (InputStream inputStream = new FileInputStream(file)){
            UploadBuilder uploadBuilder = client.files().uploadBuilder("/"+file.getName())
                    .withClientModified(new Date(file.lastModified()))
                    .withMode(WriteMode.ADD)
                    .withAutorename(true);
            uploadBuilder.uploadAndFinish(inputStream);
        } catch (DbxException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public static void downloadFile(String name,DbxClientV2 client){
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        // Get current account info to check if it's working
        try {
            System.out.println(client.users().getCurrentAccount().getName().getDisplayName());
        } catch (DbxException e) {
            System.err.println(e.getMessage());
        }
        //Download files
        try(DbxDownloader<FileMetadata> dbxDownloader = client.files().download("/" + name);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();)
        {
            dbxDownloader.download(outputStream);
            File file = new File("downloaded/" + name);
            byte[] bytes = outputStream.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dropboxTest() {
        DbxClientV2 client = getAuth();
        uploadFile(new File("C:\\Users\\Usuario\\OneDrive\\Escritorio\\entradas_12805053.pdf"),client);
        downloadFile("entradas_12805053.pdf",client);
    }
}
