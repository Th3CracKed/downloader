/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloader.fc;

/**
 *
 * @author Asus
 */
public interface DownloadStatus {
    public void onPause();
    public void onResume();
    public void onSucceed();
    public void onCancel();
    public void onInvalidLink();
}
