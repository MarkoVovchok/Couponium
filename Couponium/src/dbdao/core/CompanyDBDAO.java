package dbdao.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedHashSet;

import connections.ConnectionPool;
import core.beans.Company;
import dao.interfaces.CompanyDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class is made translate java objects to a specific tables in the
 * database and back. No business activity records here
 * 
 */
public class CompanyDBDAO implements CompanyDAO {

	private ConnectionPool pool;

	public CompanyDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This method creates a new record in the database with the given
	 * parameters;
	 */
	@Override
	public void createCompany(Company company) {

		Connection con = pool.getConnection();

		String prepStmt = "INSERT INTO company VALUES(?,?,?,?)";

		try {
			PreparedStatement pstmt = con.prepareStatement(prepStmt);

			pstmt.setLong(1, company.getId());
			pstmt.setString(2, company.getCompName());
			pstmt.setString(3, company.getPassword());
			pstmt.setString(4, company.getEmail());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("Company creation issue: id/company name have been already registered");
		}
		if (con != null) {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method deletes all the company records in the company table. NOTE:
	 * there may be other company traces that are not deleted by this method,
	 * check other tables and activities;
	 */
	@Override
	public void removeCompany(Company company) {

		Connection con = pool.getConnection();

		String sql = "DELETE FROM company WHERE id=" + company.getId();

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println(
					"Could not delete this company: " + company.getCompName() + " /or has been already deleted;");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method updates company information in the company table. NOTE: Other
	 * records created by the business logic methods should be updated
	 * separately.
	 */
	@Override
	public void updateCompany(Company company) {
		Connection con = pool.getConnection();

		try {
			Statement stmt = con.createStatement();

			String sql = "UPDATE company SET name='" + company.getCompName() + "',password='" + company.getPassword()
					+ "',email='" + company.getEmail() + "',id=" + company.getId() + " WHERE id=" + company.getId();

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Update of the company: " + company.getCompName() + "was unsecessusful");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method is the same as updateCompany(), except of this one does not
	 * allow you to change company's name.
	 * 
	 * @param Company
	 */
	public void updateCompanyExceptName(Company company) {
		Connection con = pool.getConnection();

		try {
			Statement stmt = con.createStatement();

			String sql = "UPDATE company SET password='" + company.getPassword() + "',email='" + company.getEmail()
					+ "',id=" + company.getId() + " WHERE name='" + company.getCompName() + "'";

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("Update of the company: " + company.getCompName() + "was unsecessusful");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method returns a company by searching and retrieving data from the
	 * database
	 */
	@Override
	public Company getCompany(long id) {
		Connection con = pool.getConnection();
		Company curr = new Company();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM company WHERE id=" + id);

			if (rs.next()) {
				curr.setCompName(rs.getString("name"));
				curr.setId(rs.getLong("id"));
				curr.setEmail(rs.getString("email"));
				curr.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			Exception z = new CouponSystemException(e);
			System.err.println(z);
			System.out.println("Error retrieving data for the company with id: " + id);
		}

		if (con != null) {
			pool.returnConnection(con);
		}

		return curr;
	}

	/**
	 * This method returns a collection of all companies from the company table
	 * by building objects from the metadata and adding them to a collection.
	 */
	@Override
	public Collection<Company> getAllCompanies() {
		Connection con = pool.getConnection();
		Collection<Company> companies = new LinkedHashSet<Company>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM company");
			while (rs.next()) {
				Company c = new Company();
				c.setCompName(rs.getString("name"));
				c.setId(rs.getLong("id"));
				c.setPassword(rs.getString("password"));
				c.setEmail(rs.getString("email"));
				companies.add(c);
			}

		} catch (SQLException e) {
			Exception r = new CouponSystemException(e);
			System.err.println(r);
			System.out.println("All companies data request gone wrong");
		}

		if (con != null) {
			pool.returnConnection(con);
		}
		return companies;
	}

	// Right here there was a method which signature was
	// getCoupons():Collection<Coupon>; I am not fully getting the idea of
	// business logic, but if the method includes searching in the records of a
	// business activity, in my opinion, it should be in the suitable facade. So
	// if you are Eldar and you're looking for it, please go to the
	// CompanyFacade to
	// a method getAllCoupons(companyID);

	/**
	 * This is a simple login method
	 */
	@Override
	public boolean login(String compName, String password) {
		Connection con = pool.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT password FROM company WHERE name='" + compName + "'");
			if (rs.next()) {
				String temp = rs.getString("password");
				if (temp.equals(password)) {
					return true;
				}

			} else {
				System.out.println("Wrong password");

				return false;
			}
			return false;

		} catch (SQLException e) {
			Exception t = new CouponSystemException(e);
			System.err.println(t);
			System.out.println("Invalid name or connection error");
			return false;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}
		}
	}

	/**
	 * This badboy is used to identify the company which is using a
	 * CompanyFacade at the moment, this needed to enable some of the methods in
	 * CompanyFacade class
	 * 
	 * @param It
	 *            gets a company name as parameter
	 * @return It returns an id of the current user
	 */
	public long searchForID(String compName) {
		long id;
		Connection con = pool.getConnection();
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT id FROM company WHERE name='" + compName + "'");
			if (rs.next()) {
				id = rs.getLong("id");
			} else
				id = 0;
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("ID search of the company: " + compName + " failed");
			return id = 0;
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return id;
	}
}
