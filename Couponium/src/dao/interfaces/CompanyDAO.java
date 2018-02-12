package dao.interfaces;

import java.util.Collection;

import core.beans.Company;

public interface CompanyDAO {

	/**
	 * This interface lists all the methods that should be available in every
	 * company access object;
	 * 
	 */
	public void createCompany(Company company);

	public void removeCompany(Company company);

	public void updateCompany(Company company);

	public Company getCompany(long id);

	public Collection<Company> getAllCompanies();

	// This method implemented in CompanyFacade , because the the search is
	// based on the business activity of the company
	// public Collection<Coupon> getAllCoupons(long companyId);

	public boolean login(String compName, String password);
}
