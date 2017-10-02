package ec.com.papp.web.login.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import ec.com.papp.seguridad.to.UsuarioTO;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;


/**
 * @author jcalderon
 * @telefono 0998317243
 * @mail johanna.calderon@xcelsa.com.ec
 * @descripcion Controlador para login al sistema
*/
@Controller
public class LoginController {


	private static Log log = new Log(LoginController.class);

	@RequestMapping("login")
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) throws Throwable{
		log.println("entra al login&&&************");
		UtilGeneral.removeVarSession(request,response,false);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		log.println("username: " + username);
		ModelAndView mav =null;
		mav= new ModelAndView("index");
		//Se ha logueado con exito
		if(username!=null){
			UsuarioTO usuarioTO = new UsuarioTO();
			usuarioTO.setUsuario(username);
			Collection<UsuarioTO> usuarioTOs=UtilSession.seguridadServicio.transObtenerusuario(usuarioTO);
			usuarioTO=usuarioTOs.iterator().next();
			UtilSession.setUsuario(request, usuarioTO);
			mav= new ModelAndView("index");
		}
		//Usuario sin autenticar
		else{

			mav = new ModelAndView(new RedirectView("login.jsp"));
		}
		log.println("va a salir por##: " + mav);
		return mav;
	}
}

