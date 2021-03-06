package core.beans;

import java.util.Collection;
import java.util.HashSet;

public class Company {
	
	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons = new HashSet<Coupon>();

	public Company() {
		super();
	}

	public Company(long id){
		super();
		this.id=id;
	}
	
	public Company(long id, String compName, String password, String email, Collection<Coupon> coupons) {
		super();
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
		this.coupons = coupons;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", Company Name=" + compName + ", password=" + password + ", email=" + email + "]";
	}
	
	
}
