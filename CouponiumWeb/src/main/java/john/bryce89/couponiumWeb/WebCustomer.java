package john.bryce89.couponiumWeb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

import core.beans.Coupon;
import core.beans.Customer;

@XmlRootElement
public class WebCustomer implements Serializable {

	private long id;
	private String custName;
	private String password;
	private Collection<Coupon> coupons = new HashSet<Coupon>();

	public WebCustomer() {
	}

	/**
	 * This CTOR designed to create a WebCustomer object using Customer.class
	 * object from the database
	 * 
	 * @param Customer.class object
	 */
	public WebCustomer(Customer customer) {
		super();
		this.id = customer.getId();
		this.custName = customer.getCustName();
		this.password = customer.getPassword();
		this.coupons = customer.getCoupons();
	}

	@Override
	public String toString() {
		return "WebCustomer [id=" + id + ", custName=" + custName + ", password=" + password + "]";
	}

	/**
	 * Conversion method. From WebCustomer to Customer
	 * 
	 * @param WebCustomer
	 * @return Customer
	 */
	public Customer convertToCustomer(WebCustomer webcust) {
		Customer cust = new Customer();
		cust.setCoupons(webcust.getCoupons());
		cust.setCustName(webcust.custName);
		cust.setId(webcust.getId());
		cust.setPassword(webcust.getPassword());
		return cust;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

}
