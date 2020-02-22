package de.techfak.gse.mbaig;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class GSERadioClientView {
    ObservableList<Song> viewablePlaylist = FXCollections.observableArrayList(new ArrayList<>());
    // FXMLs
    @FXML private Label currentSongLabel;
    @FXML private Label currentAlbumLabel;
    @FXML private Label currentArtistLabel;
    @FXML private Label labelStatus;
    @FXML private Label serverAddr;
    @FXML private TextField tfURLField;
    @FXML private Button btnContactServer;
    @FXML private TableView<Song> playlistTableView;
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> albumColumn;
    @FXML private TableColumn<Song, String> voteButtonColumn;
    @FXML private TableColumn<Song, String> voteCounterColumn;

    // required empty struct
    public GSERadioClientView() {  }

    /**
     * initGUI - custom initializer Method.
     */
    public void initGUI() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        playlistTableView.setItems(viewablePlaylist);
        for (TableColumn<Song, ?> tb : playlistTableView.getColumns()) {
            tb.setResizable(false);
            tb.setReorderable(false);
        }
    }

    /**
     * updateStatus - get the connected Status.
     * @param connected - The bool from the Controller to determine what to show
     */
    public void updateStatus(boolean connected) {
        if (connected) {
            labelStatus.setText("Connected!");
        } else {
            labelStatus.setText("Failed to connect!");
        }
    }

    public void showAutoError() {
        labelStatus.setText("Failed to connect, retrying...");
    }

    public void getServerResponse() {
        GSERadioClientGUI.getResponse(tfURLField.getText());
    }

    /**
     * refreshNowPlaying - A Method for the controller to update the GUI.
     * @param song - The retrieved Song to display
     */
    public void refreshNowPlaying(Song song) {
        currentSongLabel.setText(song.getTitle());
        currentArtistLabel.setText(song.getArtist());
        currentAlbumLabel.setText(song.getAlbum());
    }

    /**
     * refreshPlaylist - A Method for the controller to update the GUI.
     * @param playlist - The retrieved Playlist to show
     */
    public void refreshPlaylist(Playlist playlist) {
        viewablePlaylist = FXCollections.observableArrayList(playlist.content);
        playlistTableView.setItems(viewablePlaylist);
        playlistTableView.refresh();
    }



}
