package tests;

import java.util.Date;
import java.util.GregorianCalendar;

import core.beans.Coupon;
import core.beans.CouponType;
import core.couponsystem.CouponSystem;
import core.facade.ClientTypes;
import core.facade.CompanyFacade;

public class CompanyTest {
//Das ist ein Companie testen
	public static void main(String[] args) {
		
		CouponSystem cs = CouponSystem.getInstance();
		
		CompanyFacade company = (CompanyFacade) cs.login("BrutalPower", "Huah", ClientTypes.COMPANY);
		Date date = new Date();
		Coupon co = new Coupon();
		co.setAmount(4);
		co.setStartDate(date);
		co.setEndDate(new GregorianCalendar(2017, 8, 10).getTime());
		co.setId(333425);
		co.setPrice(24.99);
		co.setImage("some img ref");
		co.setTitle("make food not war");
		co.setType(CouponType.HEALTH);
		co.setMessage("True story");
		company.createCoupon(co);
//		company.getCoupon(co.getId());
//		co.setAmount(15);
//		co.setTitle("It is a new coupon title");
//		company.updateCoupon(co);

		Coupon c2 = new Coupon(3344, "Chunky Monky stock", date, new GregorianCalendar(2017, 10, 23).getTime(), 22,
				CouponType.FOOD, "eat the monkey!", 123.40, "banana");
		company.createCoupon(c2);
		Coupon c3 = new Coupon(66647, "Berta Bons magic beans", date, new GregorianCalendar(2017, 9, 13).getTime(), 5,
				CouponType.EXTREME, "Beans of magic flavours", 234.90, "harry and hermione");
		Coupon c4 = new Coupon(54647, "Big Bad Wolf", date, new GregorianCalendar(2017, 12, 13).getTime(), 5,
				CouponType.EXTREME, "Nuff Nuff,Sniff Sniff", 34.90, "PSSS!Grandma is the wolf!");
		company.createCoupon(c3);
		company.createCoupon(c4);
		company.getAllCoupons(company.getCompID());
		company.getCouponsbyDate(new GregorianCalendar(2017, 10, 25).getTime(), company.getCompID());
		System.out.println();
		System.out.println();
		company.getCouponByType(CouponType.EXTREME, company.getCompID());
//		removeCoupon works, i just need some coupons for other tests;
//		company.removeCoupon(co);
//		company.removeCoupon(c2);
//		company.removeCoupon(c3);
		cs.shutdown();
	}

}
