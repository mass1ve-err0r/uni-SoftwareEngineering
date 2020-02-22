package de.techfak.gse.mbaig;

import java.util.Comparator;

/* DESCENDING ORDER!
* ref: https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
*/
public class SongComparator implements Comparator<Song> {
    @Override
    public int compare(Song s1, Song s2) {
        return s2.getVotes() - s1.getVotes();
    }
}
