package de.techfak.gse.mbaig;

import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GSERadioController implements PropertyChangeListener {
    GSERadioPlayerView playerView;
    MPlayer player;

    /**
     * GSERadioController - The "Man-in-the-middle" for the view/ controller.
     * @param path - The path to find music files to construct our Player
     * @param givenView - The view to manage & address
     */
    public GSERadioController(String path, GSERadioPlayerView givenView) {
        this.player = new MPlayer(path);
        this.playerView = givenView;
        this.playerView.initGUI();
        this.playerView.viewablePlaylist = FXCollections.observableList(player.playlist.content);
        this.playerView.setTableContent();
        player.addPropertyChangeListener(this);
        player.playMusic(0);
        Platform.runLater(() -> this.playerView.updateNowPlayingView());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("SetNewSong")) {
            Platform.runLater(() -> playerView.updateNowPlayingView());
        } else if (evt.getPropertyName().equals("UpdateView")) {
            this.player.playlist.resetVotes(0);
            this.player.playlist.updatePlaylist();
            this.player.playlist.sortByVotes();
            this.playerView.refreshView();
            //
        }
    }

    /**
     * addVote - Vote hinzufügen & playlist refreshen.
     * @param idx - Index des Songs
     */
    public void addVote(int idx) {
        this.player.playlist.addVote(idx);
        this.player.playlist.sortByVotes();
        this.playerView.refreshView();
    }

    public void stopPlayback() {
        player.stopPlayer();
    }
}
