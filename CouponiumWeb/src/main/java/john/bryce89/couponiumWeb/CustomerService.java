package john.bryce89.couponiumWeb;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import core.beans.Coupon;
import core.beans.CouponType;
import core.couponsystem.CouponSystem;
import core.facade.ClientTypes;
import core.facade.CustomerFacade;

@Path("customer")
public class CustomerService {
	@Context private HttpServletRequest request;
	public CustomerService() {
	}

/**
 * This a login, it provides Facade from the current session attribute.
 * @return
 */
	private CustomerFacade getHarambe() {
		CustomerFacade c = (CustomerFacade) request.getSession().getAttribute("facade");
		return c;
	}

	@POST
	@Path("buy")
	@Consumes(MediaType.APPLICATION_JSON)
	public void purchaseCoupon(WebCoupon webc) {
		Coupon coup = new Coupon();
		coup = webc.convertToCoupon();
		getHarambe().purchaseCoupon(coup);
	}

	@GET
	@Path("getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getAllPurchased() {
		long custId = getHarambe().getCustID();
		Collection<Coupon> coups = getHarambe().getCustomersCoupons(custId);
		Collection<WebCoupon> webc = WebCoupon.convertAlltoWebCoupons(coups);
		return webc;
	}

	@GET
	@Path("getAll/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getAllByType(@PathParam("type") String type ) {
		CouponType dis = CouponType.valueOf(type);
		Collection<WebCoupon> webc = WebCoupon.convertAlltoWebCoupons(getHarambe().getAllCustomersCouponsByType(dis));
		return webc;
	}

	@GET
	@Path("getAllby/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getAllByPrice(@PathParam ("price") double price)
	{
		Collection<WebCoupon> webc = WebCoupon.convertAlltoWebCoupons(getHarambe().getAllCustomersCouponsByPrice(price));
		return webc;
	}
	
	@GET
	@Path("showAllAvailable")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<WebCoupon> getAvailable(){
		Collection<WebCoupon> webc = WebCoupon.convertAlltoWebCoupons(getHarambe().getAllAvailable());
		return webc;
	}
	
	
}
