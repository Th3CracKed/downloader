/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import java.util.List;
import java.util.regex.Pattern;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
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
            runOnThread(param);
        }
        //runOnThread("http://iihm.imag.fr/index.html");
        //TODO recuperer et lancer un thread pour chaque URL
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        TextField champUrl = new TextField();
        champUrl.setPromptText("Enter a URL");
        Button ajouterBtn = new Button("Add");
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
    
    
    private void runOnThread(String url){
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
    
}
