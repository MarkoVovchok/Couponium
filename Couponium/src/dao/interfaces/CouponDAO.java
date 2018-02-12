package dao.interfaces;

import java.util.Collection;

import core.beans.Coupon;
import core.beans.CouponType;

public interface CouponDAO {

	/**
	 * When implemented this interface grants that all of the methods in its list will be available at
	 * receiving object;
	 */
	public void createCoupon(Coupon coupon);
	public void removeCoupon(Coupon coupon);
	public void updateCoupon (Coupon coupon);
	
	public Coupon getCoupon(long id);
	public Collection<Coupon> getAllCoupon();
	public Collection<Coupon> getCouponByType (CouponType coupontype);
	
}
