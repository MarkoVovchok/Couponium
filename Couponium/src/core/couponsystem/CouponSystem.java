package core.couponsystem;

import connections.ConnectionPool;
import core.facade.AdminFacade;
import core.facade.ClientTypes;
import core.facade.CompanyFacade;
import core.facade.CouponClientFacade;
import core.facade.CustomerFacade;
import threads.DailyCouponExpirationTask;

public class CouponSystem {
	private static CouponSystem instance = null;
	private DailyCouponExpirationTask task;
	private ConnectionPool pool;
	private CompanyFacade company = new CompanyFacade();
	private CustomerFacade customer = new CustomerFacade();
	private AdminFacade admin = new AdminFacade();

	private CouponSystem() {
		pool = ConnectionPool.getInstance();
		task = new DailyCouponExpirationTask();
	}

	public static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
			System.out.println("CouponSystem is ready to work");
		}
		return instance;
	}

	public void shutdown() {
		task.stopTask();
		pool.closeAllConnections();
	}

	/**
	 * This method logins the user according to his client type;
	 * 
	 * @param name
	 * @param password
	 * @param clientType
	 */
	public CouponClientFacade login(String name, String password, ClientTypes clientType) {
		switch (clientType) {
		case ADMIN:
			admin = admin.login(name, password);
			return admin;
		case CUSTOMER:
			customer = customer.login(name, password, clientType);
			return customer;
		case COMPANY:
			company = company.login(name, password, clientType);
			return company;
		}
		return null;

	}
}
