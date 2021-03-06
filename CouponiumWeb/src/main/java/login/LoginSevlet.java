package login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.couponsystem.CouponSystem;
import core.facade.AdminFacade;
import core.facade.ClientTypes;
import core.facade.CompanyFacade;
import core.facade.CouponClientFacade;
import core.facade.CustomerFacade;

/**
 * Servlet implementation class LoginSevlet
 */
public class LoginSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginSevlet() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String clientType = request.getParameter("clientType");
		String forget = request.getParameter("remember");
		System.out.println(forget);

		CouponClientFacade facade;

		facade = CouponSystem.getInstance().login(name, password, ClientTypes.valueOf(clientType));
		if (facade != null && forget != null) {
			int maxAge = 900;
			Cookie nameCookie = new Cookie("name", name);
			Cookie passCookie = new Cookie("password", password);
			Cookie clientTypeCookie = new Cookie("clientType", clientType);
			nameCookie.setMaxAge(maxAge);
			passCookie.setMaxAge(maxAge);
			clientTypeCookie.setMaxAge(maxAge);
			response.addCookie(clientTypeCookie);
			response.addCookie(passCookie);
			response.addCookie(nameCookie);
		}

		request.getSession().setAttribute("facade", facade);

		if (facade instanceof CompanyFacade) {
			response.sendRedirect("Company/CompanyBasic.html");
		} else if (facade instanceof CustomerFacade) {
			response.sendRedirect("Client/ClientBasic.html");
		} else if (facade instanceof AdminFacade) {
			response.sendRedirect("Admin/AdminBasic.html");
		} else {
			response.sendRedirect("index.html");
		}
	}

}
