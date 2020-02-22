package de.techfak.gse.mbaig;

public class NoMusicFilesFoundException extends Exception {
    static final long serialVersionUID = 100L;
    public NoMusicFilesFoundException(String message) {
        super(message);
    }

}
