package dit042;

/**
 * Game model object - extends abstract Product superclass
 *
 * <p>
 * Game object attributes, getters and setters
 *
 * @param gameGenre is unique to this object
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
class Game extends Product  {
    // Game specific attributes
    private String gameGenre;

    // Game constructor
    public Game(String productId, String productTitle, String gameGenre, double dailyRent, boolean isRented) {
        super(productId,productTitle,dailyRent,isRented);
        this.gameGenre = gameGenre;
    }

    // Game setters
    public void setGenre(String genre) { gameGenre = genre;}

    // Game getters
    public String getGenre() { return this.gameGenre; }

    // Game toString override
    @Override
    public String toString() {
        return getId() + " : " + getTitle() + " | Genre: " + getGenre() + ". " + getDailyRent() + " SEK. Rating: "+ this.displayRating() + " | Status: " + this.getRentalStatus();
    }
}