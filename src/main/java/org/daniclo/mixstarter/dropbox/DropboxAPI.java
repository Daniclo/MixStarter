package org.daniclo.mixstarter.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadBuilder;
import com.dropbox.core.v2.files.WriteMode;

import java.io.*;
import java.util.Date;

public class DropboxAPI {
    private static final String KEY = "s0tv6js5yshjc4x";
    private static final String SECRET = "4dl6udwohgrrapc";
    private static final String ACCESS_TOKEN = "";

    public static void uploadFile(File file){
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        // Get current account info to check if it's working
        try {
            System.out.println(client.users().getCurrentAccount().getName().getDisplayName());
        } catch (DbxException e) {
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
    public static void downloadFile(String name){
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
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

    public static void main(String[] args) {
        uploadFile(new File("C:\\Users\\Usuario\\OneDrive\\Escritorio\\entradas_12805053.pdf"));
        downloadFile("entradas_12805053.pdf");
    }
}
