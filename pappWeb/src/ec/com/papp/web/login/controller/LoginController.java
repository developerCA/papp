package ec.com.papp.web.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
//		if(username!=null){
//			UsuarioTO usuarioTO = new UsuarioTO();
//			usuarioTO.setUsuario(username);
//			UtilSession.setUsuario(request, usuarioTO);
//			log.println("usuario logueado***:  " + UtilSession.getUsuario(request).getNombre());
//			//Verifico si la contrasenia debe ser cambiada
//			log.println("cambiar clave: " + UtilSession.getUsuario(request).getCambiarclave());
//			if(UtilSession.getUsuario(request).getCambiarclave().equals("1")){
//				request.getSession().setAttribute("param_nuevo", "true");
//				log.println("sesion: " + (String)request.getSession().getAttribute("param.nuevo"));
//				mav = new ModelAndView("cambiarClaveCaducada");
//			}
//			else if(UtilSession.getUsuario(request).getFechaClave()!=null){
//				ParametroTO diasclave=UtilSession.adminsitracionServicio.transObtenerParametroTO(Integer.valueOf(MensajesWeb.getString("parametro.tiempo.cambioclave")));
//				Date fecha=UtilGeneral.modificarFecha(UtilSession.getUsuario(request).getFechaClave(), (Integer.valueOf(diasclave.getValor())).intValue(), 0, 0);
//				//Comparo si la fecha es menor a la fecha actual no pido cambio de clave si es mayor o igual pido cambio de clave
//				log.println("fecha modificada: " + fecha);
//				log.println("fecha actual: " + new Date());
//				long resultado=fecha.compareTo(new Date());
//				log.println("resultado comparacion fecha: " + resultado);
//				if(resultado<0){
//					request.getSession().setAttribute("param_caducada", "true");
//					mav = new ModelAndView("cambiarClaveCaducada");
//				}
//				else{
//					log.println("no pide cambio de clave");
//					mav= new ModelAndView("index");
//				}
//			}
//			//Usuario sin autenticar
//			else{
//				mav= new ModelAndView("index");
//			}
//			//Subo a sesion los anios y el mes que debe salir seleccionado por defecto
//			Collection<Anio> anios=new ArrayList<>();
//			for(int i=UtilGeneral.obtenerValorFecha(new Date(), "yyyy"); i>=2005;i--){
//				Anio anio=new Anio();
//				anio.setAnio(i);
//				anios.add(anio);
//			}
//			request.getSession().setAttribute("param_anios", anios);
//			Collection<Anio> anioactual=new ArrayList<>();
//			Anio anio=new Anio();
//			anio.setAnio(UtilGeneral.obtenerValorFecha(new Date(), "yyyy"));
//			anioactual.add(anio);
//			request.getSession().setAttribute("param_anioactual", anioactual);
//
//		}
//		//Usuario sin autenticar
//		else{
//
//			mav = new ModelAndView(new RedirectView("login.jsp"));
//		}
		log.println("va a salir por##: " + mav);
		return mav;
	}
}

