package john.bryce89.couponiumWeb;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import core.beans.Company;
import core.beans.Customer;
import core.couponsystem.CouponSystem;
import core.facade.AdminFacade;
import core.facade.ClientTypes;

@Path("admin")
public class AdminService {

	public AdminService() {
	}

	/**
	 * This method is a fake login check, that always returns AdminFacade
	 * 
	 * @return AdminFacade
	 */
	private AdminFacade getAdminPower() {
		AdminFacade f = (AdminFacade) CouponSystem.getInstance().login("ADMIN", "1234", ClientTypes.ADMIN);
		return f;
	}

	@POST
	@Path("newCompany")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public void createCompany(WebCompany company) {
		Company comp = company.convertToCompany(company);
		getAdminPower().createCompany(comp);
	}

	/**
	 * This method will delete the company from database completely, including
	 * business history. Use with caution.
	 * 
	 * @param id
	 */
	@DELETE
	@Path("deleteCompany/{id}")
	public void removeCompany(@PathParam("id") int id) {
		getAdminPower().removeCompany(getAdminPower().getCompany(id));
	}

	/**
	 * This method will be strange, because first part of the project does not
	 * allow for the administrator to change Company's name. So it is a little
	 * nonsense.
	 * 
	 * @param WebCompany
	 */
	@PUT
	@Path("updateCompany")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCompany(WebCompany webc) {
		Company comp = webc.convertToCompany(webc);
		getAdminPower().updateCompanyExceptName(comp, webc.getId());
	}

	@GET
	@Path("getCompany/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public WebCompany getCompany(@PathParam("id") int id) {
		Company c = getAdminPower().getCompany(id);
		WebCompany webc = new WebCompany(c);
		return webc;
	}

	/**
	 * This method return a collection of all companies currently in the
	 * database. It also converses Company.class objects to a WebCompany.class
	 * objects
	 * 
	 * @return Collection<WebCompany>
	 */
	@GET
	@Path("viewAllCompanies")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WebCompany> getAllCompanies() {
		Collection<WebCompany> webs = new HashSet<WebCompany>();
		for (Company comp : getAdminPower().getAllCompanies()) {
			WebCompany webo = new WebCompany(comp);
			webs.add(webo);
		}
		return webs;
	}

	@POST
	@Path("newCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createCustomer(WebCustomer webcust) {
		Customer custo = webcust.convertToCustomer(webcust);
		getAdminPower().createCustomer(custo);
	}

	/**
	 * This method will delete the customer and his info COMPLETELY from the
	 * database(includes purchase history).
	 * 
	 * @param WebCustomer object
	 */
	@POST
	@Path("removeCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	public void removeCustomer(WebCustomer webcust) {
		Customer custo = webcust.convertToCustomer(webcust);
		getAdminPower().removeCustomer(custo);
	}
	
	@PUT
	@Path("updateCustomer")
	@Consumes (MediaType.APPLICATION_JSON)
	public void updateCustomer(WebCustomer webcust){
		Customer custo = webcust.convertToCustomer(webcust);
		getAdminPower().updateCustomerExceptname(custo, custo.getId());
	}
	
	@GET
	@Path("getCustomer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public WebCustomer getCustomer(@PathParam("id")int id){
		WebCustomer webcust = new WebCustomer(getAdminPower().viewCustomer(id));
		return webcust;
	}
	@GET
	@Path("viewAllCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WebCustomer> getAllCustomers(){
		Collection<WebCustomer> webc = new HashSet<WebCustomer>();
		for (Customer custo : getAdminPower().getCustomersCollection()) {
			WebCustomer weboc = new WebCustomer(custo);
			webc.add(weboc);
		}
		return webc;
	}
}
