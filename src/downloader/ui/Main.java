/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
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
            startDownload(param);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        TextField champUrl = new TextField();
        champUrl.setPromptText("Enter a URL");
        Button ajouterBtn = new Button("Add");
        ajouterBtn.setOnAction((ActionEvent event) -> {
            String url = champUrl.getText();
            startDownload(url);
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
    private void startDownload(String url){
        url = url.trim();//supprimer les espaces du lien 
        DownloaderView downloaderView = new DownloaderView(url,vBox);
        downloaderView.setupView();
        downloaderView.startDownload();
    }
}
