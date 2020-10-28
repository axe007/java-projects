package dit042.exceptions;
import dit042.UserInterface;

/**
 * Double type - Custom exception class - Extends native Exceptions class
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
 * @author Altansukh Tumenjargal
 * @version 0.1
 */
public class DoubleTypeException extends Exception {
    private static final long serialVersionUID = 1203L;

    public DoubleTypeException(String message) {
        super(message);
    }
}