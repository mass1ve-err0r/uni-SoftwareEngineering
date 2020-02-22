package de.techfak.gse.mbaig;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MPlayer {
    // static
    private static final int ERROR_MFILES = 100;
    private static final String ERROR_DIALOG = "No Music Files found!";
    // obj
    Playlist playlist;
    PropertyChangeSupport support;
    // gloabl consts
    private String options;
    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();

    /**
     * MPlayer struct 1 - Laucnh normally for GUI operations.
     * @param path - The path to construct the playlist with
     */
    protected MPlayer(String path) {
        support = new PropertyChangeSupport(this);
        try {
            this.playlist = new Playlist();
            this.playlist.parseFiles(path);
        } catch (NoMusicFilesFoundException e) {
            System.err.println(ERROR_DIALOG);
            GSERadio.exitWithCode(ERROR_MFILES);
        }
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> {
                    support.firePropertyChange("SetNewSong", null, 1);
                    mediaPlayer.media().play(playlist.getSong(0).getFilepath());
                    support.firePropertyChange("UpdateView", null, 1);
                });
            }
        });
    }

    /**
     * MPlayer (2nd struct) - Load MPlayer in NoGUI-Mode.
     * @param path - The path to search music files for
     * @param noGUI - Indicator that we are using the 2nd struct
     */
    protected MPlayer(String path, int noGUI) {
        try {
            this.playlist = new Playlist();
            this.playlist.parseFiles(path);
        } catch (NoMusicFilesFoundException e) {
            System.err.println(ERROR_DIALOG);
            GSERadio.exitWithCode(ERROR_MFILES);
        }
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getSong(0).getFilepath()));
                playlist.updatePlaylist();
            }
        });
    }

    /**
     * MPlayer struct 3 - Laucnh in Server-Mode.
     * @param path - Path to music files
     * @param noGUI - Identifier for noGUI
     * @param port - THe port to stream over
     */
    protected MPlayer(String path, int noGUI, int port) {
        try {
            this.playlist = new Playlist();
            this.playlist.parseFiles(path);
        } catch (NoMusicFilesFoundException e) {
            System.err.println(ERROR_DIALOG);
            GSERadio.exitWithCode(ERROR_MFILES);
        }
        options = ":sout=#rtp{dst=127.0.0.1,port=" + port + ",mux=ts}";
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(final MediaPlayer mediaPlayer) {
                mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getSong(0).getFilepath(), options));
                playlist.updatePlaylist();
            }
        });
    }

    //Hinzufügen eines Observers bzw. PropertyChangeListeners
    public void addPropertyChangeListener(PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    ///////////////////////////////////////////////////////////////////////////
    // PLAYER CONTROLS
    ///////////////////////////////////////////////////////////////////////////

    /**
     * playMusic - Play Music from a specific index of the playlist.
     * @param pos - Position to play from
     */
    void playMusic(int pos) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getSong(pos).getFilepath()));
    }

    /**
     * playMusic (struct 2) - Playback music for Server-Mode.
     * @param pos - position of the song
     * @param isServer - identifier for server
     */
    void playMusic(int pos, int isServer) {
        mediaPlayer.submit(() -> mediaPlayer.media().play(playlist.getSong(pos).getFilepath(), options));
    }

    /**
     * fullstopMusic - Completely stop playback.
     */
    private void fullstopMusic() {
        mediaPlayer.controls().stop();
    }

    /**
     * pauseMusic - Pause the playback.
     */
    private void pauseMusic() {
        mediaPlayer.controls().pause();
    }

    /**
     * resumeMusic - Resume the playback.
     */
    private void resumeMusic() {
        mediaPlayer.controls().play();
    }

    /**
     * stopPlayer - Prepare the MPlayer for a shutdown.
     */
    void stopPlayer() {
        fullstopMusic();
        mediaPlayer.release();
        mediaPlayerFactory.release();
    }
}
