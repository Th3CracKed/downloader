package downloader.fc;

import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;

import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import javafx.concurrent.Task;


public class Downloader extends Task<String>{
    
	public static final int CHUNK_SIZE = 1024;
        private final ReentrantLock reentrantLock = new ReentrantLock();
	private URL url;
	private int content_length;
	private BufferedInputStream in;
	
	private String filename;
	private File temp;
	private FileOutputStream out;

	private int size = 0;
	private int count = 0;
	private DownloadStatus downloadStatus;
        
	public Downloader(String uri,DownloadStatus downloadStatus) {
            this.downloadStatus = downloadStatus;
            try {
                    url = new URL(uri);
                    URLConnection connection = url.openConnection();
                    content_length = connection.getContentLength();
                    
                    in = new BufferedInputStream(connection.getInputStream());
                    String[] path = url.getFile().split("/");
                    filename = path[path.length-1];
                    temp = File.createTempFile(filename, ".part");
                    out = new FileOutputStream(temp);
            }
            catch(MalformedURLException e) { downloadStatus.onInvalidLink(); throw new RuntimeException(); }
            catch(IOException e) { throw new RuntimeException(e); }
	}

        @Override
	public String toString() {
		return url.toString();
	}
	
	public String download() throws InterruptedException {
		byte buffer[] = new byte[CHUNK_SIZE];
		
		while(count >= 0 && !isCancelled()) {
                    reentrantLock.lock();
                        try {
                            out.write(buffer, 0, count);
                        }catch(IOException e) { 
                            continue; 
			} finally {
                            reentrantLock.unlock();
                        }
			size += count;
                        
                        updateProgress(1.*size/content_length, 1);
			Thread.sleep(1000);
			
			try {
				count = in.read(buffer, 0, CHUNK_SIZE);
			}
			catch(IOException e) {}
		}
		
		if(size < content_length) {
			temp.delete();
			throw new InterruptedException();
		}
			
		temp.renameTo(new File(filename));
		return filename;
	}

        @Override
        protected String call() throws Exception {
            try {
                return download();
            }catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override 
        protected void succeeded() {
            downloadStatus.onSucceed();
        }

        @Override 
        protected void cancelled() {
            downloadStatus.onCancel();
            System.out.println("cancelled()");
        }
        
        public void pause(){
            downloadStatus.onPause();
            reentrantLock.lock();
        }
        public void resume(){        
            downloadStatus.onResume();
            reentrantLock.unlock();
        }
}
