package john.bryce89.couponiumWeb;

import java.util.Collection;

import javax.ws.rs.*;

import core.beans.Coupon;
import core.beans.CouponType;
import core.couponsystem.CouponSystem;
import core.facade.*;
import john.bryce89.couponiumWeb.WebCoupon;

@Path("company")
public class CompanyService {

	public CompanyService() {

	}

	/**
	 * This is a fake login later i will change it
	 */
	public CompanyFacade getBrutalPower() {
		CompanyFacade c = (CompanyFacade) CouponSystem.getInstance().login("BrutalPower", "Huah", ClientTypes.COMPANY);
		return c;
	}

	@POST
	@Path("newCoupon")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public void createCoupon(WebCoupon webc) {
		Coupon cop = webc.convertToCoupon();
		getBrutalPower().createCoupon(cop);
	}

	/**
	 * Deletes the coupon AND it's history from DB;
	 */
	@DELETE
	@Path("removeCoupon/{id}")
	public void removeCoupon(@PathParam("id") int id) {
		getBrutalPower().removeCoupon(getBrutalPower().getCoupon(id));
	}

	@PUT
	@Path("updateCoupon")
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public void updateCoupon(WebCoupon webc) {
		getBrutalPower().updateCoupon(webc.convertToCoupon());
	}

	@GET
	@Path("getCoupon/{id}")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public WebCoupon getCoupon(@PathParam("id") int id) {
		WebCoupon wbc = new WebCoupon(getBrutalPower().getCoupon(id));
		
		return wbc;
	}
	
	/**
	 * This method will return all the coupons of the current company, that are still available
	 * 
	 */
	@GET
	@Path("getAllCoupons")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getAllCoupons() {
		Collection<WebCoupon> cowc = WebCoupon
				.convertAlltoWebCoupons(getBrutalPower().getAllCoupons(getBrutalPower().getCompID()));
		return cowc;
	}
	@GET
	@Path("getCouponByType/{ctype}")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getCouponsByType(@PathParam ("ctype") CouponType ctp){
		Collection<Coupon> cops = getBrutalPower().getCouponByType(ctp, getBrutalPower().getCompID());
		return WebCoupon.convertAlltoWebCoupons(cops);
	}
	
}
