/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.ui;

import downloader.fc.DownloadStatus;
import downloader.fc.Downloader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Asus
 */
public class DownloaderView implements DownloadStatus {
    
    private final ProgressBar progressBar = new ProgressBar(0);
    private final Downloader downloader;
    private ToggleButton pause;
    private Button delete;
    private Label urlLabel;
    private final String url;
    private HBox hBox;
    private final VBox vBoxRoot;
    private VBox vboxBody;
    
    public DownloaderView(String url,VBox vBox){
        this.url = url;
        downloader = new Downloader(url,this);
        this.vBoxRoot = vBox; 
    }
    
    public void startDownload() {
        new Thread(downloader).start();
    }
    
    public void setupView(){
        setupProgressBar(vBoxRoot);
        setupPauseButton();
        setupDeleteButton();
        hBox = new HBox(progressBar,pause,delete);
        hBox.setAlignment(Pos.CENTER);
        urlLabel = new Label(url);
        vboxBody = new VBox(urlLabel,hBox);
        vBoxRoot.getChildren().add(vboxBody);
    }
    

    private void setupProgressBar(VBox vBox) {
        progressBar.prefWidthProperty().bind(vBox.widthProperty().subtract(2));
        progressBar.progressProperty().bind(downloader.progressProperty());//mieux que changelistener
    }
    
    private void setupPauseButton(){
        pause = new ToggleButton("||");
        pause.setOnAction((ActionEvent actionEvent) -> {
            if(pause.isSelected()){
                downloader.pause();
            } else {
                downloader.resume();
            }
        });
        pause.setMinSize(35, 35);
    }
    
    private void setupDeleteButton() {
        delete = new Button("X");
        delete.setMinSize(35, 35);
        delete.setOnAction((ActionEvent event) -> {
            if(progressBar.progressProperty().getValue()==1.0){
                vBoxRoot.getChildren().remove(vboxBody);
            }else{
                    downloader.cancel();
                    hBox.getChildren().removeAll(pause,delete);
                    progressBar.getParent().lookup(".bar").setStyle("-fx-background-color: #F31431");//changer la couleur du progressBar si le telechargement est terminé
                TimerTask task = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        Platform.runLater(() -> {
                            vBoxRoot.getChildren().remove(vboxBody);
                        });
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task,TimeUnit.SECONDS.toMillis(3));
            }
        });
    }
    
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Url Incorrect");
        alert.setContentText("Verifiez si ce lien est correcte : "+url);
        alert.showAndWait();
    }
    
    @Override
    public void onPause() {
        urlLabel.setText(url+" Paused");
        pause.setText(">");
    }

    @Override
    public void onResume() {
        pause.setText("||");
        urlLabel.setText(url+" Resumed");
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                Platform.runLater(() -> {
                    urlLabel.setText(url);     
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,TimeUnit.SECONDS.toMillis(1));
        
    }

    @Override
    public void onSucceed() {
        urlLabel.setText(url+" Success "+downloader.valueProperty().getValue());
        progressBar.getParent().lookup(".bar").setStyle("-fx-background-color: #00ff00");//changer la couleur du progressBar si le telechargement est terminé
        hBox.getChildren().remove(pause);
    }

    @Override
    public void onCancel() {
        urlLabel.setText(url+" Cancelled");
    }

    @Override
    public void onInvalidLink() {
        showAlert();
    }
}