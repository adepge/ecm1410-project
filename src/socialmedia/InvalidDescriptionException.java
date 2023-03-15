package socialmedia;

/**
 * Thrown when attempting to assign an account description empty or having more than
 * the system limit of characters.
 *
 * @author Ben Ellison
 * @version 1.0
 *
 */
public class InvalidDescriptionException extends Exception {

	/**
	 * Constructs an instance of the exception with no message
	 */
	public InvalidDescriptionException() {
		// do nothing
	}

	/**
	 * Constructs an instance of the exception containing the message argument
	 * 
	 * @param message message containing details regarding the exception cause
	 */
	public InvalidDescriptionException(String message) {
		super(message);
	}

}
