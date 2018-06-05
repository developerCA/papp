package ec.com.papp.web.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ec.com.xcelsa.utilitario.metodos.Log;

/**
 * @author jcalderon
 * @telefono 0998317243
 * @mail johanna.calderon@xcelsa.com.ec
 * @descripcion Controlador para desloguear del sistema
*/
@Controller
public class LogoutController {
	
	private static Log log = new Log(LoginController.class);
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		log.println("logout legal: " + request.getParameter("j_username"));
				
		HttpSession session = request.getSession(true);
		
		session.invalidate();
		
		log.println("Close Session");
		
		StringBuffer url = new StringBuffer();
		url.append("/");
		url.append("papp-Desarrollo");
		url.append("/");
		url.append("login.jsp?loggedout=true");
					
		response.sendRedirect(url.toString());
		
		return null;
		
	}	

}
