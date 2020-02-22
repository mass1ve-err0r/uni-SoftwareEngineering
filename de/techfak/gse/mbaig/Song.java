package de.techfak.gse.mbaig;

public class Song {
    // necessary fields of the obj
    String title;
    String artist;
    String album;
    String genre;
    String filepath;
    float length;
    int votes;

    // loaded struct & empty struct
    Song(String title, String artist, String album, String genre, float length, String filepath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.length = length;
        this.filepath = filepath;
        this.votes = 0;
    }

    Song() {
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public float getLength() {
        return length;
    }

    public String getFilepath() {
        return filepath;
    }

    public int getVotes() {
        return votes;
    }

    // setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    // methods
    public void addVote() {
        votes += 1;
    }
    public void resetVotes() {
        votes = 0;
    }
}
