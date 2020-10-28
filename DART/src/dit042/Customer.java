package dit042;

/**
 * Customer model object
 *
 * <p> Customer abstract object attributes, getters and setters
 *     Implements Customer membership through subclasses
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public abstract class Customer {
    private String customerId;
    private String customerName;
    private String customerPassword;

    // Customer constructor when create new
    public Customer(String customerId, String customerName, String customerPassword) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPassword = customerPassword;
    }

    // Customer constructor when importing
    public Customer(String customerId, String customerName, String customerPassword, int customerCredit) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPassword = customerPassword;
    }

    public abstract String getMembershipType();
    public abstract double getDiscountRate();
    public abstract void setCustomerCredit(int credit);
    public abstract int getCustomerCredit();
    public abstract int getConcurrentRentals();

    // Setters
    public void setCustomerId(String id) { customerId = id; }
    public void setCustomerPassword(String password) { customerPassword = password; }
    public void setCustomerName(String name) { customerName = name; }

    //Getters
    public String getId() { return this.customerId; }
    public String getCustomerPassword() { return this.customerPassword; }
    public String getCustomerName() { return this.customerName; }

    // Customer toString override
    @Override
    public String toString() {
        return getId() + " : " + getCustomerName();
    }
}