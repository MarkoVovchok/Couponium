package dbdao.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import connections.ConnectionPool;
import core.beans.Customer;
import dao.interfaces.CustomerDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class consists of the programming logic for customer objects. It is used
 * only for translation from java objects to SQL and back. For business logic,
 * see CustomerFacade class.
 */
public class CustomerDBDAO implements CustomerDAO {

	private ConnectionPool pool;

	public CustomerDBDAO() {
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This method is used to create a new customer records in the customer
	 * table.
	 */
	@Override
	public void createCustomer(Customer customer) {
		Connection con = pool.getConnection();
		try {
			PreparedStatement prep = con.prepareStatement("INSERT INTO customer VALUES(?,?,?)");

			prep.setLong(1, customer.getId());
			prep.setString(2, customer.getCustName());
			prep.setString(3, customer.getPassword());

			prep.executeUpdate();

		} catch (SQLException e) {
			Exception t = new CouponSystemException(e);
			System.err.println(t);
			System.out.println("Customer profile was not created, name: " + customer.getCustName() + " or id:"
					+ customer.getId() + " already exists");
		}
		if (con != null) {
			pool.returnConnection(con);
		}

	}

	/**
	 * Use this method to delete all the customer information from the database.
	 * NOTE: customer's purchase history is still available in the database.
	 */
	@Override
	public void removeCustomer(Customer customer) {
		Connection con = pool.getConnection();
		String sql = "DELETE FROM customer WHERE id=" + customer.getId();
		try {
			Statement stmt = con.createStatement();

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			Exception g = new CouponSystemException(e);
			System.err.println(g);
			System.out.println("No such record: " + customer.getCustName() + " found");
		}
		if (con != null) {
			pool.returnConnection(con);
		}

	}

	/**
	 * This method updates customer records in the database. Please note that
	 * customer's purchase history is updated with other method in the
	 * CustomerFacade"
	 */
	@Override
	public void updateCustomer(Customer customer) {
		Connection con = pool.getConnection();
		String sql = "UPDATE customer SET cust_name='" + customer.getCustName() + "',password='"
				+ customer.getPassword() + "' id=" + customer.getId() + " WHERE id=" + customer.getId();

		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception b = new CouponSystemException(e);
			System.out.println(b);
			System.out.println("Customer: " + customer.getCustName() + " update error, please check the id");
		}

		if (con != null) {
			pool.returnConnection(con);
		}
	}

	@Override
	public Customer getCustomer(long id) {
		Connection con = pool.getConnection();
		Customer c = new Customer();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM customer WHERE id=" + id);

			if (rs.next()) {
				c.setId(rs.getLong("id"));
				c.setCustName(rs.getString("cust_name"));
				c.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			Exception n = new CouponSystemException(e);
			e.printStackTrace();
			System.out.println(n);
			System.err.println("No customer with id: " + id + " found");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return c;
	}

	/**
	 * This methods will allow to a caller to view/or change all the customers
	 * records in the database;
	 * 
	 * @return All customers collection
	 */
	@Override
	public Collection<Customer> getAllCustomers() {
		Connection con = pool.getConnection();
		Collection<Customer> clients = new ArrayList<Customer>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM customer");

			while (rs.next()) {
				Customer c = new Customer();
				c.setCustName(rs.getString("cust_name"));
				c.setPassword(rs.getString("password"));
				c.setId(rs.getLong("id"));
				clients.add(c);

			}
		} catch (SQLException e) {
			Exception r = new CouponSystemException(e);
			System.err.println(r);
			System.out.println("Clients list unavailable");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return clients;
	}

	/**
	 * This method is a version of updateCustomer() method, except that caller
	 * can't change customer's name.
	 */
	public void updateCustomerExceptName(Customer customer) {
		Connection con = pool.getConnection();
		String sql = "UPDATE customer SET id=" + customer.getId() + ",password='" + customer.getPassword()
				+ "' WHERE cust_name='" + customer.getCustName() + "'";

		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			Exception b = new CouponSystemException(e);
			System.err.println(b);
			System.out.println("Customer: " + customer.getCustName() + " update error, please check the id");
		}

		if (con != null) {
			pool.returnConnection(con);
		}
	}
	// Method getCoupons():Collection<Coupons> is based on a buying activity of
	// the client so i moved it to a CustomerFacade under the signature of
	// getAllCustomersCoupons();

	@Override
	public boolean login(String custName, String password) {
		Connection con = pool.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT* FROM customer WHERE cust_name='" + custName + "'");
			if (rs.next()) {
				String pass = rs.getString("password");
				if (password.equals(pass)) {
					return true;
				}
			} else
				System.out.println("Customer login error");
			return false;
		} catch (SQLException e) {
			Exception n = new CouponSystemException(e);
			System.err.println(n);
			System.out.println("No customer: " + custName + " found in the database");
			return false;
		} finally {
			if (con != null) {
				pool.returnConnection(con);
			}

		}
	}

	/**
	 * This method will help to identify the current customer when he is using a
	 * facade instance.
	 */
	public long searchForID(String custName) {
		long id;
		Connection con = pool.getConnection();
		try {
			ResultSet rs = con.createStatement()
					.executeQuery("SELECT id FROM customer WHERE cust_name='" + custName + "'");
			if (rs.next()) {
				id = rs.getLong("id");
			} else
				id = 0;
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.err.println(ex);
			System.out.println("ID search of the customer: " + custName + " failed");
			return id = 0;
		}
		if (con != null) {
			pool.returnConnection(con);
		}
		return id;
	}

}
