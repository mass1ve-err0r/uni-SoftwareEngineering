package de.techfak.gse.mbaig;

import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class GSERadioClientController {

    private static final int RESPONSE_OK = 200;
    private static final int DELAY_MS = 1000;
    private static final int PERIOD_MS = 10000;
    String cachedURL = "";
    GSERadioClientView clientView;
    WebClient webClient;
    Timer timer = new Timer();
    TimerTask pollStatus = new TimerTask() {
        @Override
        public void run() {
            if (isAlive()) {
                Platform.runLater(() -> clientView.updateStatus(true));
                Platform.runLater(() -> clientView.refreshNowPlaying(webClient.retrieveSong(cachedURL)));
                Platform.runLater(() -> clientView.refreshPlaylist(webClient.retrievePlaylist(cachedURL)));
            } else {
                Platform.runLater(() -> clientView.showAutoError());
            }
        }
    };

    /**
     * GSERadioController - The "Man-in-the-middle" for the view/ controller.
     * @param givenView - The view to manage & address
     */
    public GSERadioClientController(GSERadioClientView givenView) {
        this.clientView = givenView;
        this.clientView.initGUI();
        this.webClient = new WebClient();
    }


    /**
     * checkConnection - Check the connection (basically a GET).
     * @param url - the URL to make a GET for
     */
    public void checkConnection(String url) {
        int code = webClient.getResponse(url);
        if (code == RESPONSE_OK) {
            Platform.runLater(() -> clientView.updateStatus(true));
            cachedURL = url;
            timer.schedule(pollStatus, DELAY_MS, PERIOD_MS);
            Platform.runLater(() -> clientView.refreshNowPlaying(webClient.retrieveSong(cachedURL)));
            Platform.runLater(() -> clientView.refreshPlaylist(webClient.retrievePlaylist(cachedURL)));
        } else {
            Platform.runLater(() -> clientView.updateStatus(false));
            cachedURL = "";
            //timer.cancel();
        }
    }

    public boolean isAlive() {
        return webClient.getResponse(cachedURL) == RESPONSE_OK;
    }



}
