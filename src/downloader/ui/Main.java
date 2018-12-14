/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import java.util.regex.Pattern;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Asus
 */
public class Main  extends Application {
    private final int MIN_HEIGHT =  400;
    private final int MIN_WIDTH =  600;
    private final VBox vBox = new VBox();
    
        @Override
    public void start(Stage stage){
        
        for(String param : getParameters().getRaw()){
            runOnThread(param);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        TextField champUrl = new TextField();
        champUrl.setPromptText("Enter a URL");
        Button ajouterBtn = new Button("Add");
        ajouterBtn.setOnAction((ActionEvent event) -> {
            String url = champUrl.getText();
            runOnThread(url);
            champUrl.clear();
        });
        BorderPane bottomBar = new BorderPane();
        bottomBar.setCenter(champUrl);
        bottomBar.setRight(ajouterBtn);
        BorderPane rootPane = new BorderPane();
        rootPane.setCenter(scrollPane);
        rootPane.setBottom(bottomBar);
        stage.setScene(new Scene(rootPane));
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setTitle("Downloader");
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            launch(args);
    }
    
    /**
     * permet de lancer un telechargement et l'ajouter à l'interface 
     * @param url le lien a telechargé
     */
    private void runOnThread(String url){
        url = url.trim();//supprimer les espaces du lien 
        if(isValidUrl(url)) {
            DownloaderView downloaderView = new DownloaderView(vBox, url);
               new Thread(){
                    @Override
                    public void run(){
                       downloaderView.download();
                       System.out.println("it works!!");
                    }
               }.start();
               Platform.runLater(() -> {
                    downloaderView.getDownloader().progressProperty().addListener((obs, o, n) -> {
                        downloaderView.setProgress(n);
                    });
               });
        }else{
            showAlert(url);
        }
    }
    
    /**
     * verifier si la chaine passer en paramètre est un lien valide
     * @param url le lien a verifier
     * @return Valide ou non
     */
    private Boolean isValidUrl(String url){
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Boolean isUrl = Pattern.matches(regex, url);
        if(isUrl){
            System.out.println(url +" est un lien valide");
            return true;
        }else{
            System.out.println(url +" n'est pas un lien valide");
            return false;
        }
    }
    /**
     * Afficher une alerte si le lien n'est pas valide
     * @param url le ligne qui n'est pas valide
     */
    private void showAlert(String url) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Url Incorrect");
        alert.setContentText("Verifiez si ce lien est correcte : "+url);
        alert.showAndWait();
    }
    
}
