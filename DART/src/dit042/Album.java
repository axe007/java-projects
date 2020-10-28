package dit042;

import java.util.Comparator;

/**
 * Album model object - extends abstract Product superclass
 *
 * <p>
 * Album object attributes, getters and setters
 * Implements Comparator< Album > to sort with attribute Artist
 *
 * @param "albumArtist" is unique to this object
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
class Album extends Product {
    private String albumArtist;
    private int albumYear;

    // Default constructor without rating
    public Album(String productId, String productTitle, String albumArtist, int albumYear, double dailyRent, boolean isRented) {
        super(productId, productTitle, dailyRent, isRented);
        this.albumArtist = albumArtist;
        this.albumYear = albumYear;
    }

    // Setters
    public void setAlbumArtist(String artist) { albumArtist = artist; }

    //Getters
    public String getArtist() { return this.albumArtist; }
    public int getAlbumYear() { return this.albumYear; }

    // Album toString override
    @Override
    public String toString() {
        return getId() + " : " + getTitle() + " - by " + getArtist() + ". Released in " + getAlbumYear() + ". Price: " + getDailyRent() + " SEK. Rating: "+ this.displayRating() + " | Status: " + getRentalStatus();
    }
}