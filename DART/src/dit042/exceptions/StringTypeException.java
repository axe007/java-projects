package dit042.exceptions;
import dit042.UserInterface;

/**
 * String type - Custom exception class - Extends native Exceptions class
 *
 * <p>
 *     Invoked when exception is thrown when user input is empty.
 *
 *     When caller method specifies its attributeName to helper class when
 *     getting Scanner input, it check for empty string and throws Exception.
 *     Exception error message is customized through UserInterface  and shows
 *     specific error message for the attributeName passed.
 *
 *     Contains exception within do-while statement and requests input again
 *     until any non-empty input is given.
 *
 *     If caller method does not pass attributeName, that a generic message says
 *     "... this field cannot be empty" since it uses similar overloaded input
 *     method in many other places.
 *
 * @author Altansukh Tumenjargal
 * @version 0.1
 */
public class StringTypeException extends Exception {
    private static final long serialVersionUID = 1203L;

    public StringTypeException(String message) {
            super(message);
    }
}