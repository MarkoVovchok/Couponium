package tests;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import core.beans.Coupon;
import core.beans.CouponType;
import core.couponsystem.CouponSystem;
import core.facade.ClientTypes;
import core.facade.CustomerFacade;

public class CustomerTest {
	// This is customer test
	public static void main(String[] args) {

		// this part is for Customer demonstration

		CouponSystem cs = CouponSystem.getInstance();
//		Coupon c2 = new Coupon(3344, "Chunky Monky stock", new Date(), new GregorianCalendar(2017, 10, 23).getTime(), 22,
//				CouponType.FOOD, "eat the monkey!", 123.40, "http://lorempixel.com/400/200");
//		Coupon c3 = new Coupon(66647, "Berta Bons magic beans",new  Date(), new GregorianCalendar(2017, 9, 13).getTime(), 5,
//				CouponType.EXTREME, "Beans of magic flavours", 234.90, "http://lorempixel.com/400/200");

		CustomerFacade customer = (CustomerFacade) cs.login("BlackPanther", "Pink12", ClientTypes.CUSTOMER);
//		customer.purchaseCoupon(c3);
//		customer.purchaseCoupon(c2);
		
//		customer.getAllCustomersCouponsByPrice(100.50);
		customer.getCustomersCoupons(customer.getCustID());
//		Collection<Coupon> coupons = customer.getAllAvailable();
//		System.out.println(coupons.toString());
		cs.shutdown();
	}

}
