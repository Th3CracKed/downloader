/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import downloader.fc.Downloader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Asus
 */
public class DownloaderView {
    
    private final ProgressBar progressBar;
    private final Downloader downloader;
    private Button play;
    private Button pause;
    
    public DownloaderView(VBox vBox,String url){
        progressBar = new ProgressBar(0);
        setupView(vBox,url);
        downloader = new Downloader(url);
    }
    
    public void download(){
        System.out.format("Downloading %s:", downloader);

        String filename = "";
        try {
                filename = downloader.download();
        }catch(InterruptedException e) {
                System.err.println("failed!");
        }
        System.out.format("into %s\n", filename);
        System.out.println("path : "+filename);
    }
    
    private void setupView(VBox vBox,String url){
        progressBar.prefWidthProperty().bind(vBox.widthProperty().subtract(2));
        play = new Button(">");
        play.setMinSize(35, 35);
        pause = new Button("X");
        pause.setMinSize(35, 35);
        HBox hbox = new HBox(progressBar,play,pause);
        hbox.setAlignment(Pos.CENTER);
        Label urlLabel = new Label(url);
        VBox vboxBody = new VBox(urlLabel,hbox);
        vBox.getChildren().add(vboxBody);
    }
    
    public ProgressBar getProgressBar(){
        return progressBar;
    }
    
    public Downloader getDownloader(){
        return downloader;
    }

    public Button getPlay() {
        return play;
    }

    public Button getPause() {
        return pause;
    }

    public void setProgress(Number n) {
        progressBar.setProgress(n.doubleValue());
    }
}
