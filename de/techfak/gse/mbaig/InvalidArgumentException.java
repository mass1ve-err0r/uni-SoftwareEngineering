package de.techfak.gse.mbaig;

// custom exception to throw if args mismatch.
public class InvalidArgumentException extends Exception {
    static final long serialVersionUID = 200L;
    public InvalidArgumentException(String message) {
        super(message);
    }
}
