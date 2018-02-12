package core.facade;

import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import connections.ConnectionPool;
import core.beans.Coupon;
import core.beans.CouponType;
import dbdao.core.CompanyDBDAO;
import dbdao.core.CouponDBDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class includes all the possible operations for company, including
 * business logic.
 */
public class CompanyFacade implements CouponClientFacade {

	private CompanyDBDAO comp;
	private CouponDBDAO coup;
	private long CompID;
	private ConnectionPool pool;

	public CompanyFacade() {
		pool = ConnectionPool.getInstance();
		comp = new CompanyDBDAO();
		coup = new CouponDBDAO();
		CompID = 0;
	}

	/**
	 * This method creates a new coupon record in the database.Throws an
	 * exception if coupon id or title are already registered. Also updates
	 * company_coupon table.
	 * 
	 */
	public void createCoupon(Coupon coupon) {
		coup.createCoupon(coupon);
		addtoCompanyCouponTable(coupon.getId(), CompID);
		System.out.println("Coupon: " + coupon.getTitle() + " created!");
	}

	/**
	 * This method deletes coupon from the database. Also according to the
	 * business logic of this project, this coupon will be deleted from purchase
	 * history of the customers.
	 * 
	 * @param coupon
	 */
	public void removeCoupon(Coupon coupon) {
		coup.removeCoupon(coupon);
		deleteCouponRecords(coupon.getId());
		System.out.println("Coupon: " + coupon.getId() + " was deleted");
	}

	public void updateCoupon(Coupon coupon) {
		coup.updateCoupon(coupon);
		System.out.println("Coupon number: " + coupon.getId() + "has been updated successfully");
	}

	// To look through specific coupon
	public Coupon getCoupon(long couponID) {
		System.out.println(coup.getCoupon(couponID));
		return coup.getCoupon(couponID);
	}

	/**
	 * This method allows a caller to look through all the coupons of given
	 * company, that are still available.
	 * 
	 * @return
	 */
	public Collection<Coupon> getAllCoupons(long companyID) {
		Connection con = pool.getConnection();
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT coupon_id FROM company_coupon WHERE company_id=" + companyID);
			while (rs.next()) {
				coupons.add(getCoupon(rs.getLong("coupon_id")));
			}
			
		} catch (SQLException e) {
			Exception r = new CouponSystemException(e);
			System.err.println(r);
			System.out.println("Can't retrieve coupon list for company: " + companyID);
		}

		if (con != null) {
			pool.returnConnection(con);
		}
		System.out.println(coupons.toString());
		return coupons;
	}

	public CompanyFacade login(String compName, String password, ClientTypes clienttype) {
		if (comp.login(compName, password)) {
			CompanyFacade com = new CompanyFacade();
			com.setCompID(comp.searchForID(compName));
			System.out.println("Welcome, " + compName + " your id=" + com.CompID + " to use your powers, use 'company.'");
			return com;
		} else {
			System.out.println("try harder");
			return null;
		}
	}

	public long getCompID() {
		return CompID;
	}

	public void setCompID(long compID) {
		CompID = compID;
	}

	/**
	 * This method is intended to remove all of the company's issued coupons
	 * records from the database.
	 * 
	 * @param long
	 *            CompanyID = id of the company that you need to perish for
	 *            ever;
	 */
	public void deleteCompanysCoupons(long companyId) {
		Connection con = pool.getConnection();
		Collection<Coupon> removecoll = getAllCoupons(companyId);
		for (Coupon curr : removecoll) {
			deleteCouponRecords(curr.getId());
		}
		System.out.println("Coupons of the company: " + companyId + "have been deleted sucesessfully");

		if (con != null) {
			pool.returnConnection(con);
		}
	}

	// Here is an overloaded method from coupon DBDAO to provide searches by
	// given parameters.
	// number 1:
	public Collection<Coupon> getCouponByType(CouponType coupontype, long companyID) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon curr : getAllCoupons(companyID)) {
			if (curr.getType().equals(coupontype)) {
				searched.add(curr);
			}
		}
		System.out.println("Available from this company: ");
		System.out.println(searched.toString());
		return searched;
	}

	// number2 :
	public Collection<Coupon> getCouponsBythePrice(double price, long companyID) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon curr : getAllCoupons(companyID)) {
			if (curr.getPrice() <= price) {
				searched.add(curr);
			}
		}
		System.out.println("Available from this company, up to the price of: " + price);
		System.out.println(searched.toString());
		return searched;
	}

	// number 3:
	public Collection<Coupon> getCouponsbyDate(Date date, long companyID) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon curr : getAllCoupons(companyID)) {
			if (curr.getEndDate().before(date) || curr.getEndDate().equals(date)) {
				searched.add(curr);
			}
		}
		return searched;
	}

	/**
	 * This method is used when a new coupon is issued by some company. It
	 * updates coupon history table.
	 * 
	 * @param couponID
	 * @param companyID
	 */
	public void addtoCompanyCouponTable(long couponID, long companyID) {
		Connection con = pool.getConnection();
		try {
			con.createStatement().executeUpdate(
					"INSERT INTO company_coupon(company_id,coupon_id) VALUES(" + companyID + "," + couponID + ")");
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("CompanyCouponUpdate gone wrong");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method allows to delete ALL records about the given coupon.
	 * 
	 * @param couponID
	 */
	public void deleteCouponRecords(long couponID) {
		Connection con = pool.getConnection();
		try {
			con.createStatement().executeUpdate("DELETE FROM coupon WHERE id=" + couponID);
			con.createStatement().executeUpdate("DELETE FROM customer_coupon WHERE coupon_id=" + couponID);
			con.createStatement().executeUpdate("DELETE FROM company_coupon WHERE coupon_id=" + couponID);
		} catch (Exception e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Coupon: " + couponID + "was not deleted");
		}

	}
}
