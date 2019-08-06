package ca.jhosek.main.shared;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {
	/**
	 * email validation regex string courtesy of see
	 * http://www.regular-expressions.info/email.html
	 */
	static final String VALID_EMAIL_REGEX = "\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b";

	/**
	 * Verifies that the specified emailAddress is valid for our service.
	 * 
	 * @param emailAddress the emailAddress to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidEmail(String emailAddress) {
		if (emailAddress == null || emailAddress.isEmpty() ) {
			return false;
		}
		return emailAddress.matches(VALID_EMAIL_REGEX);
	}

	/**
	 * password validation regex string
	 * 
	 * the password length doesn’t matter, but the password must contain 
	 * at least 1 number, 
	 * at least 1 lower case letter, 
	 * and at least 1 upper case letter.
	 * 
	 * courtesy of http://nilangshah.wordpress.com/2007/06/26/password-validation-via-regular-expression/
	 */
	static final String VALID_PASSWORD_REGEX = "^\\w*(?=\\w*\\d)(?=\\w*[a-z])(?=\\w*[A-Z])\\w*$";

	/**
	 * Verifies that the specified emailAddress is valid for our service.
	 * 
	 * @param emailAddress the emailAddress to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidPassword(String pswd) {
		if (pswd == null || pswd.isEmpty() ) {
			return false;
		}
		return pswd.matches(VALID_PASSWORD_REGEX);
	}
	
	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 3;
	}
}
