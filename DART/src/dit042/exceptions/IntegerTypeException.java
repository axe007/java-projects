package dit042.exceptions;
import dit042.UserInterface;

/**
 * Integer type - Custom exception class - Extends native Exceptions class
 *
 * <p>
 *     Invoked when exception is thrown when user input is less than zero.
 *
 *     When caller method specifies its attributeName to helper class when
 *     getting Scanner input, it compares to zero and throws Exception.
 *     Exception error message is customized through UserInterface  and shows
 *     specific error message for the attributeName passed.
 *
 *     Contains exception within do-while statement and requests input again
 *     until any zero or positive input is given. Zero input is accepted.
 *
 *     If caller method does not pass attributeName, that a generic message says
 *     "... this field cannot be negative" since it uses similar overloaded input
 *     method in many other places.
 *
 *     When invoked in Return Rental method, exception interrupts the method
 *     assuming system date/time configuration is changed between initial rental
 *     date and return date, thus resulting in negative days. Zero day is assumed
 *     as same day return, and rounded up to 1 day of rental.
 *
 *
 * @author Altansukh Tumenjargal
 * @version 0.1
 */
public class IntegerTypeException extends Exception {
    private static final long serialVersionUID = 1203L;
    private UserInterface userInterface = new UserInterface();

    public IntegerTypeException(String message, String methodName, String attributeName) {
        if (methodName.equals("returnRental") && attributeName.equals("number of days rented")) {
            userInterface.printInterfaceLabels("exceptionRentalNegativeException", attributeName);
        } else if (!attributeName.equals(null)) {
            userInterface.printInterfaceLabels("exceptionInputTypeException", attributeName);
        }
        else {
            userInterface.printInterfaceLabels("errorExceptionGenericThisField");
        }
    }
}