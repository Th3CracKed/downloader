/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import downloader.fc.Downloader;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

/**
 *
 * @author Asus
 */
public class DownloaderView {
    
    private final ProgressBar progressBar;
    private final Downloader downloader;
    
    public DownloaderView(VBox vBox,String url){
        progressBar = new ProgressBar(0);
        progressBar.prefWidthProperty().bind(vBox.widthProperty().subtract(2));
        vBox.getChildren().add(progressBar);
        downloader = new Downloader(url);
    }
    
    public void download(){
        System.out.format("Downloading %s:", downloader);

        String filename = "";
        try {
                filename = downloader.download();
        }catch(Exception e) {
                System.err.println("failed!");
        }
        System.out.format("into %s\n", filename);
        System.out.println("path : "+filename);
    }
    
    public ProgressBar getProgressBar(){
        return progressBar;
    }
    
    public Downloader getDownloader(){
        return downloader;
    }

    void setProgress(Number n) {
        progressBar.setProgress(n.doubleValue());
    }
}
