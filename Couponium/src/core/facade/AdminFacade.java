package core.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import connections.ConnectionPool;
import core.beans.Company;
import core.beans.Customer;
import dbdao.core.CompanyDBDAO;
import dbdao.core.CustomerDBDAO;
import exceptions_handling.CouponSystemException;

/**
 * This class is intended to consist of all the methods available for an
 * administrator of the coupon system. It includes business logic operations
 * that can be changed at any time.
 *
 */
public class AdminFacade implements CouponClientFacade {

	private CompanyDBDAO comp;
	public CustomerDBDAO cust;
	private CompanyFacade compF;
	private ConnectionPool pool;

	public AdminFacade() {
		super();
		comp = new CompanyDBDAO();
		cust = new CustomerDBDAO();
		compF = new CompanyFacade();
		pool = ConnectionPool.getInstance();
	}

	/**
	 * This method uses company DAO to create a new record in the database. It
	 * will throw an exception if company's name or id have been already
	 * registered.
	 * 
	 * @param company
	 */
	public void createCompany(Company company) {
		comp.createCompany(company);
		System.out.println("Company: " + company.getCompName() + " created");
	}

	/**
	 * This method will delete the company completely and absolutely, including
	 * it's coupons that are already purchased and those which are available to
	 * buy. Use it wisely.
	 */
	public void removeCompany(Company company) {
		compF.deleteCompanysCoupons(company.getId());
		comp.removeCompany(company);
		System.out.println("Company: " + company.getCompName() + " has been removed from Couponium");
	}

	/**
	 * This method allows to administrator to upgrade company's records, except
	 * of it's name.
	 */
	public void updateCompanyExceptName(Company company, long oldCompanyID) {
		updateHistory(ClientTypes.COMPANY, company, comp.searchForID(company.getCompName()));
		comp.updateCompanyExceptName(company);
		System.out.println("Company: " + company.getCompName() + " has been updated");
	}

	/**
	 * This method prints company records onto the server screen and returns
	 * with a company object if needed.
	 * 
	 * @param Companyid
	 * 
	 * @return Company object
	 */
	public Company getCompany(long id) {
		Company curr = comp.getCompany(id);
		System.out.println(curr.toString());
		return curr;
	}

	/**
	 * This method prints the list of all companies onto the server console and
	 * returns with a collection of company objects for further use.
	 * 
	 * @return A Collection of companies that are registered in database.
	 */
	public Collection<Company> getAllCompanies() {
		Collection<Company> curr = comp.getAllCompanies();
		System.out.println(curr.toString());
		return curr;
	}

	/**
	 * This method creates a new customer record in the database. The name and
	 * id parameters are checked by the SQL, and if not unique, an exception
	 * will occur.
	 * 
	 * @param customer
	 */
	public void createCustomer(Customer customer) {
		cust.createCustomer(customer);
		System.out.println("A new customer: " + customer.getCustName() + " extends the family! Welcome!");
	}

	/**
	 * This method deletes the customer and his purchase history completely. Use
	 * with caution.
	 * 
	 * @param Customer
	 */
	public void removeCustomer(Customer customer) {
		deleteCustomersHistory(customer.getId());
		cust.removeCustomer(customer);
	}

	/**
	 * This method updates all customer parameters, except of his name. It also
	 * updates purchase history so it will answer to a new customer parameters.
	 * 
	 * @param Customer
	 */
	public void updateCustomerExceptname(Customer customer, long customerOldID) {
		updateHistory(ClientTypes.CUSTOMER, customer, cust.searchForID(customer.getCustName()));
		cust.updateCustomerExceptName(customer);
		System.out.println("Customer's: " + customer.getCustName() + " info has been updated.");
	}

	/**
	 * This method will allow to administrator to look through all the customers;
	 * 
	 * @return All Customers List
	 */
	public Collection<Customer> getCustomersCollection() {
		System.out.println("=====Customers of the Couponium=====");
		System.out.println(cust.getAllCustomers().toString());
		return cust.getAllCustomers();
	}

	public Customer viewCustomer(long id) {
		System.out.println(cust.getCustomer(id).toString());
		return cust.getCustomer(id);
	}

	public AdminFacade login(String login, String password) {
		if (login.toUpperCase().equals("ADMIN") && password.equals("1234")) {
			AdminFacade admin = new AdminFacade();
			System.out
					.println("Welcome, administrator.Couponium awaits you!. You can use 'admin.' to test your power.");
			return admin;
		} else
			System.out.println("wrong password/login");
		return null;
	}

	/**
	 * This method deletes purchase history of the given customer. It is a part
	 * of a business logic and can be changed or deprived at any time.
	 */
	public void deleteCustomersHistory(long customerID) {
		Connection con = pool.getConnection();
		try {
			con.createStatement().executeUpdate("DELETE FROM customer_coupon WHERE customer_id=" + customerID);
			System.out.println("Customer's: " + customerID + " purchase history deleted");
		} catch (SQLException e) {
			Exception ex = new CouponSystemException(e);
			System.out.println(ex);
			System.out.println("Records of customer: " + customerID + "are not available");
		}
		if (con != null) {
			pool.returnConnection(con);
		}
	}

	/**
	 * This method will update purchase/or creation history of the coupon, in
	 * case if customer or coupon or company's status has changed.
	 */
	public void updateHistory(ClientTypes clientType, Object updatedUnit, long oldID) {
		Connection con = pool.getConnection();
		switch (clientType) {
		case COMPANY:
			Company comp = (Company) updatedUnit;
			try {
				con.createStatement().executeUpdate(
						"UPDATE company_coupon SET company_id=" + comp.getId() + " WHERE company_id=" + oldID);
			} catch (SQLException e) {
				Exception ex = new CouponSystemException(e);
				System.err.println(ex);
				System.out.println("Check old id, update for: " + comp.getCompName() + " was not completed");
			}
			break;

		case CUSTOMER:
			Customer cust = (Customer) updatedUnit;
			try {
				con.createStatement().executeUpdate(
						"UPDATE customer_coupon SET customer_id=" + cust.getId() + "WHERE customer_id=" + oldID);
			} catch (SQLException e) {
				Exception ex = new CouponSystemException(e);
				System.out.println(ex);
				System.out.println("Update for: " + cust.getCustName() + " was not completed, check old id");
			}
			break;
		case ADMIN:
			break;
		}
		if (con != null) {
			pool.returnConnection(con);
		}

	}
}
