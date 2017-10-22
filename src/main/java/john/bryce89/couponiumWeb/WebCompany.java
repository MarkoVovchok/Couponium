package john.bryce89.couponiumWeb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

import core.beans.Company;
import core.beans.Coupon;

@XmlRootElement
public class WebCompany implements Serializable {

	private long id;
	private String compName;
	private String password;
	private String email;
	private Collection<Coupon> coupons = new HashSet<Coupon>();

	public WebCompany() {

	}
	/**
	 * This CTOR creates a WebCompany object from a consumed Company.class object
	 */
	public WebCompany(Company company) {
		super();
		this.id = company.getId();
		this.compName = company.getCompName();
		this.password = company.getPassword();
		this.email = company.getEmail();
		this.coupons = company.getCoupons();
	}
	/**
	 * This method consumes a WebCompany object and creates a Company object;
	 * @param WebCompany
	 * @return Company
	 */
	public Company convertToCompany(WebCompany webc) {
		Company comp = new  Company();
		comp.setId(webc.getId());
		comp.setCompName(webc.getCompName());
		comp.setPassword(webc.getPassword());
		comp.setEmail(webc.getEmail());
		comp.setCoupons(webc.getCoupons());
		return comp;
	}

	@Override
	public String toString() {
		return "WebCompany [id=" + id + ", compName=" + compName + ", email=" + email + "]";
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
	

}
