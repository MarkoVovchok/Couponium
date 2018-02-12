package core.facade;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import connections.ConnectionPool;
import core.beans.Coupon;
import core.beans.CouponType;
import dbdao.core.CouponDBDAO;
import dbdao.core.CustomerDBDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class consists of the methods and options which are available for
 * customer.
 *
 */
public class CustomerFacade implements CouponClientFacade {

	private ConnectionPool pool;
	private CustomerDBDAO cust;
	private CouponDBDAO coup;
	private long CustID;

	public CustomerFacade() {
		super();
		pool = ConnectionPool.getInstance();
		cust = new CustomerDBDAO();
		coup = new CouponDBDAO();
		CustID = 0;
	}

	/**
	 * This method adds a new coupon record to a customer purchase history. It
	 * includes checks proposed by a business logic of this project.
	 */
	public void purchaseCoupon(Coupon coupon) {
		if (firstTimeBuyCheck(coupon.getId()) && amountCheck(coupon.getId()) && dateCheck(coupon.getEndDate())) {
			addtoCustomerCouponTable(coupon.getId(), CustID);
			
		}
	}

	/**
	 * This method is designed to provide view of all coupons that are in
	 * possession of a given customer.
	 */
	public Collection<Coupon> getCustomersCoupons(long customerID) {
		Connection con = pool.getConnection();
		Collection<Coupon> coupons = new ArrayList<Coupon>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM customer_coupon WHERE customer_id=" + customerID);

			while (rs.next()) {
				coupons.add(coup.getCoupon(rs.getLong("coupon_id")));
			}
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("No coupons found");
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
		System.out.println(coupons.toString());
		return coupons;

	}

	/**
	 * This method prints all of the coupons of a given type, which are
	 * currently at customers possession.
	 * 
	 * @param coupontype
	 * @return
	 */
	public Collection<Coupon> getAllCustomersCouponsByType(CouponType coupontype) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon coupon : getCustomersCoupons(CustID)) {
			if (coupon.getType().equals(coupontype)) {
				searched.add(coupon);
			}
		}
		System.out.println("Coupons for your request: ");
		System.out.println(searched.toString());
		return searched;
	}

	/**
	 * This method searches in the customer's coupons collection by price
	 * parameter.
	 * 
	 */
	public Collection<Coupon> getAllCustomersCouponsByPrice(double price) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon current : getCustomersCoupons(getCustID())) {
			if (current.getPrice() <= price) {
				searched.add(current);
			}
		}
		System.out.println("All of purchased coupons up to the price of: " + price);
		return searched;
	}

	public long getCustID() {
		return CustID;
	}

	/**
	 * This method is used only when a new coupon is purchased by customer.
	 * 
	 * @param couponID
	 * @param customerID
	 */
	public void addtoCustomerCouponTable(long couponID, long customerID) {
		Connection con = pool.getConnection();
		try {
			con.createStatement().executeUpdate(
					"INSERT INTO customer_coupon(customer_id,coupon_id) VALUES(" + customerID + "," + couponID + ")");
			con.createStatement().executeUpdate(
					"UPDATE coupon SET amount="+(coup.getCoupon(couponID).getAmount()-1) + "WHERE id=" + couponID);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("CustomerCouponUpdate gone wrong");
		}
		System.out.println("Amount changed");
		System.out.println("Purchased");
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method checks if a customer already has the same coupon in his
	 * possession. If not exception will occur and customer will be able to
	 * proceed with his purchase.
	 * 
	 */
	public boolean firstTimeBuyCheck(long couponId) {
		Connection con = pool.getConnection();
		
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT customer_id FROM customer_coupon WHERE coupon_id=");
			while(rs.next()){
				if(CustID == rs.getLong("customer_id")){
					System.out.println("You already have this coupon");
					return false;
				}
			}
		} catch (SQLException e) {
			return true;
		}
		return true;
	}

	/**
	 * This method checks amount of the given coupons that are still available
	 * to purchase. If there are available it changes amount accordingly.
	 */
	public boolean amountCheck(long couponID) {
		Connection con = pool.getConnection();
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT amount FROM coupon WHERE id=" + couponID);
			if (rs.next()) {
				if (rs.getInt("amount") >= 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("Coupon: " + couponID + " is not available");
			if (con != null) {
				pool.returnConnection(con);
			}
			return false;
		}
		return false;
	}

	// I am not sure about this one?
	public boolean dateCheck(Date couponEndDate) {
		Calendar cal = Calendar.getInstance();
		if (cal.getTime().equals(couponEndDate)) {
			return false;
		}
		return true;
	}
	
	public Collection<Coupon> getAllAvailable(){
		return coup.getAllCoupon();
	}

	public CustomerFacade login(String custName, String password, ClientTypes clientType) {
		if (cust.login(custName, password)) {
			CustomerFacade customer = new CustomerFacade();
			customer.CustID = cust.searchForID(custName);
			System.out.println("Welcome, " + custName + "Couponium is ready to work. Your id is=" + customer.getCustID());
			System.out.println("Use 'customer.' to activate commands!");
			return customer;
		}
		System.out.println("Something gone wrong");
		return null;
	}
}
