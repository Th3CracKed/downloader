package downloader.fc;

import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application  implements DownloadStatus{
        @Override
	public void start(Stage stage) {
		for(String url: getParameters().getRaw()) {
			Downloader downloader;
			try {
				downloader = new Downloader(url,this);
			}
			catch(RuntimeException e) {
				System.err.format("skipping %s %s\n", url, e);
				continue;
			}
			System.out.format("Downloading %s:", downloader);
			
			downloader.progressProperty().addListener((obs, o, n) -> {
				System.out.print(".");
				System.out.flush();
			});
			
			String filename;
			try {
				filename = downloader.download();
			}
			catch(InterruptedException e) {
				System.err.println("failed!");
				continue;
			}
			System.out.format("into %s\n", filename);
                        System.out.println(filename);
		}
		System.exit(0);
	}

	public static void main(String argv[]) {
		launch(argv);
	}

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onSucceed() {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onInvalidLink() {
        
    }
}
