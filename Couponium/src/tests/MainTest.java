package tests;

import core.beans.Company;
import core.beans.Customer;
import core.couponsystem.CouponSystem;
import core.facade.AdminFacade;
import core.facade.ClientTypes;
import db.core.TableCreator;

public class MainTest {

	public static void main(String[] args) {
		// This part is an Admin powers demonstration.
		CouponSystem cs = CouponSystem.getInstance();
		AdminFacade admin = (AdminFacade) cs.login("ADMIN", "1234", ClientTypes.ADMIN);
		// for the first time only:
//		TableCreator.main(args);

		Customer f = new Customer(8237373, "Vendette", "Anonimio");
		Customer g = new Customer(3321114, "John", "Snow");
		admin.viewCustomer(123456);
		admin.createCustomer(f);
		admin.createCustomer(g);
		f.setPassword("Venice");
		admin.updateCustomerExceptname(f, f.getId());
		admin.getCustomersCollection();

		admin.removeCustomer(g);
		admin.removeCustomer(f);

		Company a = new Company(1234, "FlipMode", "squad", "jrecords@50cent.com", null);
		Company b = new Company(354622, "Nickelback", "hero", "oldbutgood@nowhere.com", null);

		admin.createCompany(a);
		admin.createCompany(b);
		admin.getAllCompanies();
		System.out.println("===========================================");
		a.setPassword("MyBad");
		long OldIdA = a.getId();
		a.setId(12121212);
		admin.updateCompanyExceptName(a, OldIdA);
		admin.getCompany(a.getId());
//		removeCompany works , but i needed some companies for other tests;
//		admin.removeCompany(a);
//		admin.removeCompany(b);

		cs.shutdown();
	}

}
