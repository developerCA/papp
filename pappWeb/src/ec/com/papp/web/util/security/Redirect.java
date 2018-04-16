
package ec.com.papp.web.util.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller

public class Redirect {

	@RequestMapping("redirect")
	public ModelAndView redireccionar(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("entro a redireccionar...");
		return new ModelAndView(new RedirectView("login"));
	}
	
}
