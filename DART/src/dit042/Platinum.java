package dit042;

/**
 * Platinum type subclass of Customer superclass
 *
 * <p>
 *     Discount rate and number of concurrent rentals are
 *     set as constant numbers when instantiating the customer
 *     object or importing from text file.
 *
 * @author Altansukh Tumenjargal
 * @version 0.1
 */
public class Platinum extends Customer {
    private final double discountRate;
    private final int concurrentRentals;
    private int customerCredit;

    public Platinum(String customerId, String customerName, String customerPassword) {
        super(customerId, customerName, customerPassword);
        this.discountRate = 0.25;
        this.concurrentRentals = 7;
        this.customerCredit = 0;
    }

    public Platinum(String customerId, String customerName, String customerPassword, int customerCredit) {
        super(customerId, customerName, customerPassword, customerCredit);
        this.discountRate = 0.25;
        this.concurrentRentals = 7;
        this.customerCredit = customerCredit;
    }

    public String getMembershipType() {
        Class c = getClass();
        return c.getSimpleName();
    }

    public double getDiscountRate() { return this.discountRate; }
    public int getConcurrentRentals() { return this.concurrentRentals; }
    public int getCustomerCredit() { return this.customerCredit; }

    private String creditPlural() {
        String creditPlural;
        if(customerCredit > 1) {
            creditPlural = "credits";
        } else {
            creditPlural = "credit";
        }
        return creditPlural;
    }

    public void setCustomerCredit(int credit) { this.customerCredit = credit; }

    // Customer toString override
    @Override
    public String toString() {
        return getId() + " : " + getCustomerName() + " , Balance: " + getCustomerCredit() + " " + creditPlural() + ". Membership: " + getMembershipType();
    }
}