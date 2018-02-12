package dao.interfaces;

import java.util.Collection;

import core.beans.Customer;

public interface CustomerDAO {
	
	/**
	 * This interface lists all the methods that are obligatory for Customer data access objects;
	 * 
	 */
	public void createCustomer(Customer customer);
	public void removeCustomer (Customer customer);
	public void updateCustomer (Customer customer);
	
	public Customer getCustomer(long id);
	public Collection<Customer> getAllCustomers();
//	this one here is for the business logic, so no use for it in DBDAO;
//	public Collection<Coupon> getCustomersCoupons(long id);
	public boolean login (String custName, String password);
	
}
