package de.techfak.gse.mbaig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.waiter.media.ParsedWaiter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Playlist {
    // consts
    private static final int DIVISOR_TIME = 1000;
    private static final String UNDEFINED = "<undefined>";
    // Obj-specific
    ArrayList<Song> content;
    private File[] musicFiles;
    private Song temp;

    // struct
    protected Playlist() {
        content = new ArrayList<>();
    }

    public ArrayList<Song> getContent() {
        return content;
    }

    public void setContent(ArrayList<Song> content) {
        this.content = content;
    }

    /**
     * parseFiles - Parse the given folder path & extract if possible.
     * @param givenPath The path to scan for
     * @throws NoMusicFilesFoundException If no MP3s are found, this is thrown & program exits.
     */
    void parseFiles(String givenPath) throws NoMusicFilesFoundException {
        // Prefetch all logical entries thanks to this beautiful function!
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/FilenameFilter.html
        // Change from 09.12.2019: Shift the exiting condition from GSERadio to Playlist
        if (new File(givenPath).isDirectory() && new File(givenPath).canRead()) {
            musicFiles = new File(givenPath).listFiles((dir, name) -> name.toUpperCase().endsWith(".MP3"));
        } else {
            throw new NoMusicFilesFoundException("No music files found (Directory does not exist)! Aborting...");
        }
        if (musicFiles.length != 0) {
            MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
            for (File track : musicFiles) {
                Media media = mediaPlayerFactory.media().newMedia(track.getAbsolutePath());
                ParsedWaiter parsed = new ParsedWaiter(media) {
                    @Override
                    protected boolean onBefore(Media component) {
                        return media.parsing().parse();
                    }
                };
                try {
                    parsed.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MetaData metaData = media.meta().asMetaData();
                InfoApi infoApiFromMedia = media.info();
                Song songFromMusicFolder = new Song(
                    metaData.get(Meta.TITLE),
                    metaData.get(Meta.ARTIST),
                    metaData.get(Meta.ALBUM),
                    metaData.get(Meta.GENRE),
                    ((float) infoApiFromMedia.duration() / DIVISOR_TIME),
                    track.getAbsolutePath()
                );
                getCurrentPlaylist();
                media.release();
                content.add(songFromMusicFolder);
            }
            mediaPlayerFactory.release();
            System.out.println("(Info) Shuffling Playlist now");
            Collections.shuffle(content, new Random(2));
        } else {
            throw new NoMusicFilesFoundException("No music files (MP3s) found in folder! Aborting...");
        }
    }

    /**
     * getSong - Returns the Song-Obj at a given index from the Playlist.
     * @param index The index to pick the Song from
     * @return The Song as Song-Obj
     */
    @JsonIgnore
    public Song getSong(int index) {
        return content.get(index);
    }

    /**
     * getSongInfo - Get Infos from the current Song & print them to stdout.
     * @param index The current index in the playlist
     */
    @JsonIgnore
    public void getSongInfo(int index) {
        System.out.printf("\n===> Now Live:\n"
            + "Title:     %s \n"
            + "Artist:    %s \n"
            + "Album:     %s \n"
            + "Genre:     %s \n"
            + "Length:    %.2fsec\n\n",
            // Ternary notation to scan for null values & replace with undefined
            ((content.get(index).getTitle() == null) ? UNDEFINED : content.get(index).getTitle()),
            ((content.get(index).getArtist() == null) ? UNDEFINED : content.get(index).getArtist()),
            ((content.get(index).getAlbum() == null) ? UNDEFINED : content.get(index).getAlbum()),
            ((content.get(index).getGenre() == null) ? UNDEFINED : content.get(index).getGenre()),
            content.get(index).getLength()
        );
    }

    /**
     * getCurrentPlaylist - Print the playlist from the current Song's position to stdout (incl. wrapping)
     */
    @JsonIgnore
    public void getCurrentPlaylist() {
        System.out.println("\n===> Current Playlist:");
        for (int i = 0; i < content.size(); i++) {
            System.out.printf("Title:     %s\n"
                    + "Artist:    %s\n"
                    + "Album:     %s\n"
                    + "Genre:     %s\n"
                    + "Length:    %.2fsec\n---\n",
                ((content.get(i).getTitle() == null) ? UNDEFINED : content.get(i).getTitle()),
                ((content.get(i).getArtist() == null) ? UNDEFINED : content.get(i).getArtist()),
                ((content.get(i).getAlbum() == null) ? UNDEFINED : content.get(i).getAlbum()),
                ((content.get(i).getGenre() == null) ? UNDEFINED : content.get(i).getGenre()),
                content.get(i).getLength()
            );
        }
    }

    /**
     * updatePlaylist - Strip song at the top and add to the bottom.
     */
    public void updatePlaylist() {
        //temp = observablePlaylist.get(0);
        temp = content.get(0);
        content.remove(0);
        //observablePlaylist.remove(0);
        content.add(temp);
        //observablePlaylist.add(temp);
        temp = null;
    }

    public void addVote(int indexOfSong) {
        content.get(indexOfSong).addVote();
    }
    public void resetVotes(int indexOfSong) {
        content.get(indexOfSong).resetVotes();
    }
    public void sortByVotes() {
        content.sort(new SongComparator());
    }

}
