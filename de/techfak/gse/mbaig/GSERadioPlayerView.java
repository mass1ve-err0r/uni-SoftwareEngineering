package de.techfak.gse.mbaig;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class GSERadioPlayerView {
    ObservableList<Song> viewablePlaylist = FXCollections.observableArrayList();
    // FXMLs
    @FXML private Label currentSongLabel;
    @FXML private Label currentAlbumLabel;
    @FXML private Label currentArtistLabel;
    @FXML private TableView<Song> playlistTableView;
    @FXML private TableColumn<Song, String> titleColumn;
    @FXML private TableColumn<Song, String> artistColumn;
    @FXML private TableColumn<Song, String> albumColumn;
    @FXML private TableColumn<Song, String> voteButtonColumn;
    @FXML private TableColumn<Song, String> voteCounterColumn;

    // empty req. structs
    public GSERadioPlayerView() { }

    /*
     * SUPER helpful Oracle tutorial on TableView to jumpstart development.
     * ref: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
     */

    /**
     * initGUI - An alternate GUI initializer.
     */
    public void initGUI() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        voteButtonColumn.setCellFactory(param -> new VoteCell());
        voteCounterColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));
        for (TableColumn<Song, ?> tb : playlistTableView.getColumns()) {
            tb.setResizable(false);
            tb.setReorderable(false);
        }
    }

    public void setTableContent() {
        playlistTableView.setItems(viewablePlaylist);
    }

    ///////////////////////////////////////////////////////////////////////////
    // GUI Modifiers
    ///////////////////////////////////////////////////////////////////////////

    /**
     * updateNowPlaying - Update the currently played Songs's information.
     */
    public void updateNowPlayingView() {
        currentSongLabel.setText(viewablePlaylist.get(0).getTitle());
        currentArtistLabel.setText(viewablePlaylist.get(0).getArtist());
        currentAlbumLabel.setText(viewablePlaylist.get(0).getAlbum());
        playlistTableView.refresh();
    }

    public void refreshView() {
        playlistTableView.refresh();
    }

}
