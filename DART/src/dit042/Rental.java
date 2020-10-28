package dit042;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Rental model object
 *
 * <p>
 * Various attributes, getters and setters for rental object which represents single
 * rental transaction for Rental tracking, transaction, history, rating and reviews.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class Rental {
    private String rentalId;
    private String rentedItemId;
    private String rentedCustomerId;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private Double rentalIncome;
    private Double rating;
    private String review;
    private boolean isReturned;

    public Rental(String rentalId, String itemId, String customerId, LocalDate rentalDate, LocalDate returnDate, Double rentalIncome, Double rentalRating, String rentalReview, boolean isReturned) {
        this.rentalId = rentalId;
        this.rentedItemId = itemId;
        this.rentedCustomerId = customerId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.rentalIncome = rentalIncome;
        this.rating = rentalRating;
        this.review = rentalReview;
        this.isReturned = isReturned;
    }

    public Rental(String itemId, String customerId, LocalDate rentalDate) {
        this.rentalId = String.valueOf(UUID.randomUUID());
        this.rentedItemId = itemId;
        this.rentedCustomerId = customerId;
        this.rentalDate = rentalDate;
        this.returnDate = LocalDate.of(1970,01,01);
        this.rentalIncome = 0.0;
        this.rating = -1.0;
        this.review = null;
        this.isReturned = false;
    }

    public Rental(String itemId, Double rentalIncome, String rentedCustomerId) {
        this.rentedItemId = itemId;
        this.rentalIncome = rentalIncome;
        this.rentedCustomerId = rentedCustomerId;
    }

    //Setters
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }
    public void setItemId(String itemId) { this.rentedItemId = itemId; }
    public void setCustomerId(String customerId) { this.rentedCustomerId = customerId; }
    public void setRentalDate(LocalDate rentalDate) { this.rentalDate = rentalDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setRentalIncome(Double rentalIncome) { this.rentalIncome = rentalIncome; }
    public void setRating(Double rating) { this.rating = rating; }
    public void setReview(String review) { this.review = review; }
    public void setIsReturned(Boolean isReturned) { this.isReturned = isReturned; }

    //Getters
    public String getRentalId() { return rentalId; }
    public String getRentedItemId() { return rentedItemId; }
    public String getRentedCustomerId() { return rentedCustomerId; }
    public LocalDate getRentalDate() { return rentalDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public Double getRentalIncome() { return rentalIncome; }
    public String getReview() { return review; }
    public boolean getIsReturned() { return isReturned; }
    public Double getRating() { return rating; }

    public String showRating() {
        String ratingString = "N/A";
        if (this.getRating() != -1.0)
            ratingString = String.valueOf(this.getRating());
        return ratingString;
    }

    public String getReturnStatus() {
        String returnStatus;
        if(isReturned) {
            returnStatus = "Returned";
        } else {
            returnStatus = "* Out on Rental *";
        }
        return returnStatus;
    }

    @Override
    public String toString() {
        return getRentalId() + " - Rented itemId: " + getRentedItemId() + " | Rented customerId: " +getRentedCustomerId() + " | Rented date: " + getRentalDate() + " | Return date: " + getReturnDate() + " | Rent income: " + getRentalIncome() + " | State: " + getReturnStatus() + " | Rating: " + showRating();
    }
}