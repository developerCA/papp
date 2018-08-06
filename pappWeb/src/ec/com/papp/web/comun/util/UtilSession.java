package ec.com.papp.web.comun.util;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ec.com.papp.administracion.servicio.AdminsitracionServicio;
import ec.com.papp.estructuraorganica.servicio.EstructuraorganicaServicio;
import ec.com.papp.planificacion.servicio.PlanificacionServicio;
import ec.com.papp.seguridad.servicio.SeguridadServicio;
import ec.com.papp.seguridad.to.PerfilTO;
import ec.com.papp.seguridad.to.PerfilpermisoTO;
import ec.com.papp.seguridad.to.PermisoTO;
import ec.com.papp.seguridad.to.UsuarioTO;
import ec.com.papp.spring.Factory;
import ec.com.xcelsa.utilitario.metodos.Log;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;


public class UtilSession {


	private static Log log = new Log(UtilSession.class);
	public static AdminsitracionServicio adminsitracionServicio=(AdminsitracionServicio) Factory.getFactory().getBean("administracionTrans");
	public static EstructuraorganicaServicio estructuraorganicaServicio=(EstructuraorganicaServicio) Factory.getFactory().getBean("estructuraorganicaTrans");
	public static PlanificacionServicio planificacionServicio=(PlanificacionServicio) Factory.getFactory().getBean("planificacionTrans");
	public static SeguridadServicio seguridadServicio=(SeguridadServicio) Factory.getFactory().getBean("seguridadTrans");
		
	/**
	 * Método LogoutUser 
	 * 
	 * @param request
	 * @throws Exception
	 */
	public static void logoutUser(HttpServletRequest request) throws Exception {
		try {
			HttpSession vosession = request.getSession(false);
			vosession.invalidate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Guarda en la sesión el usuario logueado
	 * 
	 * @param request - La petición que se está procesando
	 */
	public static void setUsuario(HttpServletRequest request,UsuarioTO usuario){
		log.println("Entra a cargar el usuario");

		try{
			request.getSession(true).setAttribute(ConstantesSesion.USUARIO_LOGIN,usuario);
			JSONObject jsonObject=new JSONObject();
			//Obtengo los roles del usuario
			PerfilpermisoTO perfilpermisoTO=new PerfilpermisoTO();
			perfilpermisoTO.getId().setPerfilid(usuario.getPerfilid());
			perfilpermisoTO.setPermiso(new PermisoTO());
			perfilpermisoTO.setPerfil(new PerfilTO());
			Collection<PerfilpermisoTO> perfilpermisoTOs=UtilSession.seguridadServicio.transObtenerPerfilpermiso(perfilpermisoTO);
			jsonObject.put("permisos", (JSONArray)JSONSerializer.toJSON(perfilpermisoTOs,perfilpermisoTO.getJsonConfig()));
			request.getSession(true).setAttribute(ConstantesSesion.USUARIO_PERMISOS,jsonObject.toString());
			System.out.println("permisos: " + jsonObject);
//				log.println("usuariologin****: " + request.getSession().getAttribute(ConstantesSesion.USUARIO_LOGIN));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Obtiene el Usuario logeado
	 * 
	 * @param request -	La petición que se está procesando
	 */
	public static UsuarioTO getUsuario(HttpServletRequest request){
		HttpSession session = request.getSession();
		UsuarioTO usuarioDTO =(UsuarioTO)session.getAttribute(ConstantesSesion.USUARIO_LOGIN);	
		return usuarioDTO;
	}

}
