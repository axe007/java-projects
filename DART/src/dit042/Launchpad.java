package dit042;

/**
 * Launchpad class - the Main
 *
 * <p>
 * - main() function to start the execution and calls to User interface
 *
 * Default manager password is "admin1234"
 * Default employee and customer's "password123"
 * Employee and customer ID is listed once loaded from DartData file.
 *
 * @author Altansukh Tumenjargal
 * @version 0.3
 */
public class Launchpad {

    public static void main(String[] args) {
        UserInterface start = new UserInterface();
        start.startApp();
    }
}