package proiect.java.exceptions;

public class DateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message = "";

	public DateException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
