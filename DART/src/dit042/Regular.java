package dit042;

/**
 * Regular type subclass of Customer superclass
 *
 * <p>
 *     Discount rate and number of concurrent rentals are
 *     set as constant numbers when instantiating the customer
 *     object or importing from text file.
 *
 * @author Altansukh Tumenjargal
 * @version 0.1
 */
public class Regular extends Customer {
    private final double discountRate;
    private final int concurrentRentals;
    private int customerCredit;

    public Regular(String customerId, String customerName, String customerPassword) {
        super(customerId, customerName, customerPassword);
        this.discountRate = 0.0;
        this.concurrentRentals = 1;
        this.customerCredit = 0;
    }

    public Regular(String customerId, String customerName, String customerPassword, int customerCredit) {
        super(customerId, customerName, customerPassword);
        this.discountRate = 0.0;
        this.concurrentRentals = 1;
        this.customerCredit = customerCredit;
    }

    public String getMembershipType() {
        Class c = getClass();
        return c.getSimpleName();
    }

    public double getDiscountRate() { return this.discountRate; }
    public int getConcurrentRentals() { return this.concurrentRentals; }
    public int getCustomerCredit() { return this.customerCredit; }

    public void setCustomerCredit(int credit) { customerCredit = credit; }

    private String creditPlural() {
        String creditPlural;
        if(customerCredit > 1) {
            creditPlural = "credits";
        } else {
            creditPlural = "credit";
        }
        return creditPlural;
    }

    // Customer toString override
    @Override
    public String toString() {
        return getId() + " : " + getCustomerName() + " , Balance: " + getCustomerCredit() + " " + creditPlural() + ". Membership: " + getMembershipType();
    }
}