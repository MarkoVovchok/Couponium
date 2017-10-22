package login;

import java.io.IOException;
import javax.servlet.ServletException;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String clientType = request.getParameter("clientType");
		
		CouponClientFacade facade;

		facade = CouponSystem.getInstance().login(name, password, ClientTypes.valueOf(clientType));
		
		request.getSession().setAttribute("facade", facade);
		
		if(facade instanceof CompanyFacade){
			response.sendRedirect("Company/CompanyBasic.html");
		}else if(facade instanceof CustomerFacade){
			response.sendRedirect("Client/ClientBasic");
		}else if(facade instanceof AdminFacade){
			response.sendRedirect("Admin/AdminBasic.html");
		}else {
			response.sendRedirect("index.html");
		}
	}

}
