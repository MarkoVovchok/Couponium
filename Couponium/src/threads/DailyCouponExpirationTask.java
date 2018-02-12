package threads;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import core.beans.Coupon;
import core.facade.CompanyFacade;
import dbdao.core.CouponDBDAO;

/**
 * This thread starts daily and removes coupons that are not relevant anymore.
 *
 */
public class DailyCouponExpirationTask {

	private CompanyFacade compf;
	private CouponDBDAO coup;
	private ScheduledFuture<?> cleanerShedule; 

	public DailyCouponExpirationTask() {
		compf = new CompanyFacade();
		coup = new CouponDBDAO();
		ScheduledExecutorService sheduler = Executors.newScheduledThreadPool(1);
		cleanerShedule= sheduler.scheduleAtFixedRate(clearDatabase(), 1, 1, TimeUnit.DAYS);
		System.out.println("database cleaning routine is running");
	}

	/**
	 * This Runnable method makes irrelevant coupons disappear from database on
	 * daily basis. It uses removeCoupon() method of the company facade because
	 * it is used to include business logic in it.
	 * 
	 */
	public Runnable clearDatabase() {
		Calendar cal = Calendar.getInstance();
		final Runnable purge = new Runnable() {
			public void run() {
				for (Coupon current : coup.getAllCoupon()) {
					if (current.getEndDate().after(cal.getTime())) {
						compf.removeCoupon(current);
					}
				}
			}
		};
		return purge;
	}
	/**
	 * This method cancel the scheduled task and interrupts the current thread. 
	 */
	public void stopTask() {
		cleanerShedule.cancel(true);
		Thread.currentThread().interrupt();
	}

}
