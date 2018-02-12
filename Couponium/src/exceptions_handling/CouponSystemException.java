package exceptions_handling;

public class CouponSystemException extends Exception {

	/**
	 * This class is for exceptions handling. In Couponium every exception is
	 * changed by this class, and sends a simple, easy readable message for a method caller; 
	 */

	private static final long serialVersionUID = 1L;

	public CouponSystemException(Throwable throwable) {
		super();
		System.out.println(throwable.getMessage());
	}
	
	

}
