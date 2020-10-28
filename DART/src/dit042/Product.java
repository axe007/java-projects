package dit042;

import java.util.Comparator;

/**
 * Product model object - abstract superclass
 *
 * <p>
 * Game and Album object common attributes, getters and setters
 * Implements Comparable< Product > for both product's sorting by Rating, Year and Title.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public abstract class Product implements Comparable< Product > {
    private String productId;
    private String productTitle;
    private double dailyRent;
    private Double rating;
    private boolean isRented;

    public Product(String productId, String productTitle, double dailyRent, boolean isRented) {
        this.productId = productId;
        this.productTitle = productTitle;
        this.dailyRent = dailyRent;
        this.rating = -1.0;
        this.isRented = isRented;
    }

    // Getters
    public String getId() { return this.productId; }
    public String getTitle() { return this.productTitle; }
    public double getDailyRent() { return this.dailyRent; }
    public Double getRating() { return this.rating; }
    public boolean getIsRented() { return this.isRented; }

    // Setters
    public void setTitle(String title) { this.productTitle = title;}
    public void setDailyRent(double dailyRent) { this.dailyRent = dailyRent;}
    public void setRating(Double rating) { this.rating = rating; }
    public void setRentalStatus(boolean rentalStatus) { isRented = rentalStatus;}

    public String displayRating() {
        String ratingDisplay = "";
        if (getRating().equals(-1.0)) {
            ratingDisplay = "N/A";
        } else {
            ratingDisplay = String.valueOf(getRating());
        }
        return ratingDisplay;
    }

    public String getRentalStatus() {
        String rentalStatus;
        if(isRented) {
            rentalStatus = " * Rented * ";
        } else {
            rentalStatus = "Available";
        }
        return rentalStatus;
    }

    @Override
    public String toString() {
        return getId() + " : " + getTitle() + " | Rent: " + getDailyRent() + " SEK. Rating: "+ this.displayRating() + " | Status: " + getRentalStatus();
    }

    public int compareTo(Product product) {
        return Comparator.comparing(Product::getRating).thenComparing(Product::getTitle).thenComparing(Product::getId).compare(this, product);
    }
}