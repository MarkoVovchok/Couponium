package dbdao.core;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import connections.ConnectionPool;
import core.beans.Coupon;
import core.beans.CouponType;
import dao.interfaces.CouponDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class translates data from java to SQL and back. It is used to
 * retrieve,update or insert coupons data in the database
 */
public class CouponDBDAO implements CouponDAO {

	private ConnectionPool pool;

	public CouponDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This method creates a new coupon record in the database. This record
	 * should be provided with the unique id and title.
	 */
	@Override
	public void createCoupon(Coupon coupon) {
		Connection con = pool.getConnection();
		try {
			PreparedStatement prep = con.prepareStatement("INSERT INTO coupon VALUES(?,?,?,?,?,?,?,?,?)");

			prep.setLong(1, coupon.getId());
			prep.setString(2, coupon.getTitle());
			prep.setDate(3, new java.sql.Date(coupon.getStartDate().getTime()));
			prep.setDate(4, new java.sql.Date(coupon.getEndDate().getTime()));
			prep.setInt(5, coupon.getAmount());
			prep.setString(6, coupon.getType().name());
			prep.setString(7, coupon.getMessage());
			prep.setDouble(8, coupon.getPrice());
			prep.setString(9, coupon.getImage());

			prep.executeUpdate();

		} catch (SQLException e) {
			Exception c = new CouponSystemException(e);
			System.out.println(c);
			System.out.println("Coupon creation issue/coupon with the similar id: " + coupon.getId() + "or title:"
					+ coupon.getTitle() + " already exists");
		}
		System.out.println("Coupon: " + coupon.getTitle() + " added!");
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method deletes coupon records from the database.NOTE: coupons that
	 * were purchased are still available to see. Also, coupon creation history
	 * is still available through company_coupon table;
	 */
	@Override
	public void removeCoupon(Coupon coupon) {
		Connection con = pool.getConnection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM coupon WHERE id=" + coupon.getId());
			System.out.println("Coupon: " + coupon.getTitle() + " has been deleted");
		} catch (SQLException e) {
			Exception c = new CouponSystemException(e);
			System.out.println(c);
			System.out.println("Cannot erase the coupon: " + coupon.getTitle());
		}
		if (con != null) {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method is a little risky, but it can be very usable. I decided to
	 * try to update coupon by erasing the old one, and creating a new record in
	 * the database. NOTE: purchase history and creation history of this coupon
	 * will stay unchanged. It must be done by business logic methods.
	 */
	@Override
	public void updateCoupon(Coupon coupon) {
		removeCoupon(coupon);
		createCoupon(coupon);
	}

	@Override
	public Coupon getCoupon(long id) {
		Connection con = pool.getConnection();
		Coupon c = new Coupon();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM coupon WHERE id=" + id);

			if (rs.next()) {
				c.setAmount(rs.getInt("amount"));
				c.setEndDate(rs.getDate("end_date"));
				c.setStartDate(rs.getDate("start_date"));
				c.setId(rs.getLong("id"));
				c.setTitle(rs.getString("title"));
				c.setType(CouponType.valueOf(rs.getString("type").toUpperCase()));
				c.setMessage(rs.getString("message"));
				c.setPrice(rs.getDouble("price"));
				c.setImage(rs.getString("image"));
			}
		} catch (SQLException e) {
			Exception o = new CouponSystemException(e);
			System.out.println(o);
			System.out.println("Coupon: " + id + " unavailable at the moment");

		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return c;

	}

	/**
	 * This method retrieves the list of all the coupons that are stored in the
	 * coupon table of the database.
	 * 
	 */
	@Override
	public Collection<Coupon> getAllCoupon() {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Connection con = pool.getConnection();

		try {
			ResultSet rs1 = con.createStatement().executeQuery("SELECT id FROM coupon");
			while (rs1.next()) {
				coupons.add(getCoupon(rs1.getLong("id")));
				rs1.next();
			}
		} catch (SQLException e) {
			Exception c = new CouponSystemException(e);
			System.out.println(c);
			System.out.println("Coupon Collection view error");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return coupons;
	}

	/**
	 * This method will return a collection of all coupons that are of this
	 * type.
	 */
	@Override
	public Collection<Coupon> getCouponByType(CouponType coupontype) {
		Collection<Coupon> coupons = new ArrayList<Coupon>();
		Connection con = pool.getConnection();

		try {
			ResultSet rs = con.createStatement()
					.executeQuery("SELECT* FROM coupon WHERE type='" + coupontype.name() + "'");
			while (rs.next()) {
				coupons.add(getCoupon(rs.getLong("id")));
				rs.next();
			}
		} catch (SQLException e) {
			Exception c = new CouponSystemException(e);
			System.out.println(c);
			System.out.println("Coupons of type: " + coupontype.name() + " not found");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return coupons;
	}

	/**
	 * This method returns with a collection of coupons that are available at a
	 * given price or lower;
	 * 
	 * @param price
	 * @return Collection of coupons up to given price
	 */
	public Collection<Coupon> getCouponsBythePrice(double price) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon curr : getAllCoupon()) {
			if (curr.getPrice() <= price) {
				searched.add(curr);
			}
		}
		return searched;
	}

	/**
	 * This method will return with the collection of a coupons that are
	 * available for given date.
	 * 
	 * @param date
	 * 
	 * @return Available coupons up to date;
	 */
	public Collection<Coupon> getCouponsbyDate(Date date) {
		Collection<Coupon> searched = new ArrayList<Coupon>();
		for (Coupon curr : getAllCoupon()) {
			if (curr.getEndDate().before(date) || curr.getEndDate().equals(date)) {
				searched.add(curr);
			}
		}
		return searched;
	}

}
