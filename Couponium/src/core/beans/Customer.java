package core.beans;

import java.util.Collection;
import java.util.HashSet;

public class Customer {

	private long id;
	private String custName;
	private String password;
	private Collection<Coupon> coupons = new HashSet<Coupon>();
	
	
	public Customer() {
		super();
	}


	public Customer(long id, String custName, String password) {
		super();
		this.id = id;
		this.custName = custName;
		this.password = password;
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


	@Override
	public String toString() {
		return "Customer [id=" + id + ", Customer Name=" + custName + ", password=" + password + "]";
	}
	
	

	
	
}

