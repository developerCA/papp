package ec.com.papp.web.seguridad.controller;

import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import ec.com.papp.estructuraorganica.id.UsuariounidadID;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.estructuraorganica.to.UsuariounidadTO;
import ec.com.papp.seguridad.id.PerfilpermisoID;
import ec.com.papp.seguridad.id.PermisoobjetoID;
import ec.com.papp.seguridad.to.MenuTO;
import ec.com.papp.seguridad.to.PerfilTO;
import ec.com.papp.seguridad.to.PerfilpermisoTO;
import ec.com.papp.seguridad.to.PermisoTO;
import ec.com.papp.seguridad.to.PermisoobjetoTO;
import ec.com.papp.seguridad.to.UsuarioTO;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.papp.web.seguridad.util.ConsultasUtil;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;

/**
 * @autor: jcalderon
 * @fecha: 27-10-2015
 * @copyright: Xcelsa
 * @version: 1.0
 * @descripcion Clase para realizar administraciones centralizadas de seguridad
*/

@RestController
@RequestMapping("/rest/seguridad")
public class SeguridadController {
	private Log log = new Log(SeguridadController.class);
	
	@RequestMapping(value = "/{clase}", method = RequestMethod.POST)
	public Respuesta grabar(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo grabar: " + objeto);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		String id="";
		String accion="";
		try {
			//Menu
			if(clase.equals("menu")){
				MenuTO menuTO = gson.fromJson(new StringReader(objeto), MenuTO.class);
				accion = (menuTO.getId()==null)?"crear":"actualizar";
				UtilSession.seguridadServicio.transCrearModificarMenu(menuTO);
				id=menuTO.getId().toString();
				menuTO=UtilSession.seguridadServicio.transObtenerMenuTO(menuTO.getId());
				jsonObject.put("menu", (JSONObject)JSONSerializer.toJSON(menuTO,menuTO.getJsonConfig()));
			}
			//Perfilpermiso
			else if(clase.equals("perfilpermiso")){
				PerfilpermisoTO perfilpermisoTO = gson.fromJson(new StringReader(objeto), PerfilpermisoTO.class);
				accion = (perfilpermisoTO.getId()==null)?"crear":"actualizar";
				UtilSession.seguridadServicio.transCrearModificarPerfilpermiso(perfilpermisoTO);
				id=perfilpermisoTO.getId().toString();
				perfilpermisoTO=UtilSession.seguridadServicio.transObtenerPerfilpermisoTO(new PerfilpermisoID(perfilpermisoTO.getId().getPerfilid(), perfilpermisoTO.getId().getPermisoid()));
				jsonObject.put("perfilpermiso", (JSONObject)JSONSerializer.toJSON(perfilpermisoTO,perfilpermisoTO.getJsonConfig()));
			}

			//Perfil
			else if(clase.equals("perfil")){
				PerfilTO perfilTO = gson.fromJson(new StringReader(objeto), PerfilTO.class);
				accion = (perfilTO.getId()==null)?"crear":"actualizar";
				UtilSession.seguridadServicio.transCrearModificarPerfil(perfilTO);
				id=perfilTO.getId().toString();
				jsonObject.put("perfil", (JSONObject)JSONSerializer.toJSON(perfilTO,perfilTO.getJsonConfig()));
			}

			//Permiso
			else if(clase.equals("permiso")){
				PermisoTO permisoTO = gson.fromJson(new StringReader(objeto), PermisoTO.class);
				accion = (permisoTO.getId()==null)?"crear":"actualizar";
				UtilSession.seguridadServicio.transCrearModificarPermiso(permisoTO);
				id=permisoTO.getId().toString();
				jsonObject.put("permiso", (JSONObject)JSONSerializer.toJSON(permisoTO,permisoTO.getJsonConfig()));
			}

			//Usuario
			else if(clase.equals("usuario")){
				UsuarioTO usuarioTO = gson.fromJson(new StringReader(objeto), UsuarioTO.class);
				accion = (usuarioTO.getId()==null)?"crear":"actualizar";
				if(usuarioTO.getNpfechaactualizacionclave()!=null)
					usuarioTO.setFechaactualizacionclave(UtilGeneral.parseStringToDate(usuarioTO.getNpfechaactualizacionclave()));
				UtilSession.seguridadServicio.transCrearModificarusuario(usuarioTO);
				id=usuarioTO.getId().toString();
				jsonObject.put("usuario", (JSONObject)JSONSerializer.toJSON(usuarioTO,usuarioTO.getJsonConfig()));
			}
			
			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				UsuariounidadTO usuariounidadTO = gson.fromJson(new StringReader(objeto), UsuariounidadTO.class);
				accion = (usuariounidadTO.getId()==null)?"crear":"actualizar";
				UtilSession.estructuraorganicaServicio.transCrearModificarUsuariounidad(usuariounidadTO);
				id=usuariounidadTO.getId().toString();
				jsonObject.put("usuariounidad", (JSONObject)JSONSerializer.toJSON(usuariounidadTO,usuariounidadTO.getJsonConfig()));
			}

			//Registro la auditoria
//			if(mensajes.getMsg()==null)
//				FormularioUtil.crearAuditoria(request, clase, accion, objeto, id);
			if(mensajes.getMsg()==null){
				mensajes.setMsg(MensajesWeb.getString("mensaje.guardar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
		log.println("existe mensaje: " + mensajes.getMsg());
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;		
	}
	
	@RequestMapping(value = "/{clase}/{id}/{id2}", method = RequestMethod.GET)
	public Respuesta editar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,HttpServletRequest request){
		log.println("entra al metodo recuperar: " + id);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Menu
			if(clase.equals("menu")){
				MenuTO menuTO = UtilSession.seguridadServicio.transObtenerMenuTO(id);
				jsonObject.put("menu", (JSONObject)JSONSerializer.toJSON(menuTO,menuTO.getJsonConfig()));
			}
			//Perfilpermiso
			else if(clase.equals("perfilpermiso")){
				PerfilpermisoTO perfilpermisoTO=new PerfilpermisoTO(new PerfilpermisoID(id, id2));
				jsonObject.put("perfilpermiso", (JSONObject)JSONSerializer.toJSON(perfilpermisoTO,perfilpermisoTO.getJsonConfigEditar()));
			}

			//Perfil
			else if(clase.equals("perfil")){
				PerfilTO perfilTO = UtilSession.seguridadServicio.transObtenerPerfilTO(id);
				jsonObject.put("perfil", (JSONObject)JSONSerializer.toJSON(perfilTO,perfilTO.getJsonConfig()));
				//traigo los permisos
				PerfilpermisoTO perfilpermisoTO=new PerfilpermisoTO();
				perfilpermisoTO.getId().setPerfilid(perfilTO.getId());
				Collection<PerfilpermisoTO> perfilpermisoTOs=UtilSession.seguridadServicio.transObtenerPerfilpermiso(perfilpermisoTO);
				jsonObject.put("perfilpermisos", (JSONArray)JSONSerializer.toJSON(perfilpermisoTOs,perfilpermisoTO.getJsonConfig()));
			}

			//Permisoobjeto
			else if(clase.equals("permisoobjeto")){
				PerfilpermisoTO perfilpermisoTO = UtilSession.seguridadServicio.transObtenerPerfilpermisoTO(new PerfilpermisoID(id, id2));
				jsonObject.put("permisoobjeto", (JSONObject)JSONSerializer.toJSON(perfilpermisoTO,perfilpermisoTO.getJsonConfigEditar()));
			}

			//Permiso
			else if(clase.equals("permiso")){
				PermisoTO permisoTO = UtilSession.seguridadServicio.transObtenerPermisoTO(id);
				jsonObject.put("permiso", (JSONObject)JSONSerializer.toJSON(permisoTO,permisoTO.getJsonConfig()));
			}

			//Usuario
			else if(clase.equals("usuario")){
				UsuarioTO usuarioTO = UtilSession.seguridadServicio.transObtenerUsuarioTO(id);
				if(usuarioTO.getFechaactualizacionclave()!=null)
					usuarioTO.setNpfechaactualizacionclave(UtilGeneral.parseDateToString(usuarioTO.getFechaactualizacionclave()));
				jsonObject.put("usuario", (JSONObject)JSONSerializer.toJSON(usuarioTO,usuarioTO.getJsonConfigEditar()));
				//obtengo las unidades del usuario
				UsuariounidadTO usuariounidadTO=new UsuariounidadTO();
				usuariounidadTO.getId().setId(id);
				usuariounidadTO.setUnidadTO(new UnidadTO());
				usuariounidadTO.setUsuarioTO(new UsuarioTO());
				Collection<UsuariounidadTO> usuariounidadTOs=UtilSession.estructuraorganicaServicio.transObtenerUsuariounidad(usuariounidadTO);
				jsonObject.put("usuariounidades", (JSONArray)JSONSerializer.toJSON(usuariounidadTOs,usuariounidadTO.getJsonConfig()));
			}

			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				UsuariounidadTO usuariounidadTO = UtilSession.estructuraorganicaServicio.transObtenerUsuariounidadTO(new UsuariounidadID(id, id2));
				jsonObject.put("usuariounidad", (JSONObject)JSONSerializer.toJSON(usuariounidadTO,usuariounidadTO.getJsonConfig()));
			}
			
			log.println("json retornado: " + jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al obtener para editar");
			mensajes.setMsg(MensajesWeb.getString("error.obtener"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}
	
	@RequestMapping(value = "/{clase}/{id}/{id2}/{di3}", method = RequestMethod.DELETE)
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	public Respuesta eliminar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,@PathVariable Long id3,HttpServletRequest request){
		log.println("entra al metodo eliminar");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			//Menu
			if(clase.equals("menu")){
				UtilSession.seguridadServicio.transEliminarMenu(new MenuTO(id));
			}
			//Perfilpermiso
			else if(clase.equals("perfilpermiso")){
				UtilSession.seguridadServicio.transEliminarPerfilpermiso(new PerfilpermisoTO(new PerfilpermisoID(id, id2)));
			}

			//Perfil
			else if(clase.equals("perfil")){
				UtilSession.seguridadServicio.transEliminarPerfil(new PerfilTO(id));
			}

			//Permisoobjeto
			else if(clase.equals("permisoobjeto")){
				UtilSession.seguridadServicio.transEliminarPermisoobjeto(new PermisoobjetoTO(new PermisoobjetoID(id, id2)));
			}

			//Permiso
			else if(clase.equals("permiso")){
				UtilSession.seguridadServicio.transEliminarPermiso(new PermisoTO(id));
			}

			//Usuario
			else if(clase.equals("usuario")){
				UtilSession.seguridadServicio.transEliminarusuario(new UsuarioTO(id));
			}
			
			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				UtilSession.estructuraorganicaServicio.transEliminarUsuariounidad(new UsuariounidadTO(new UsuariounidadID(id, id2)));
			}

			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.eliminar") + " " + clase);
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.seguridadServicio.transCrearModificarAuditoria(auditoriaTO);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			mensajes.setMsg(MensajesWeb.getString("error.eliminar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
//		if(mensajes.getMsg()!=null){
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
//			log.println("existen mensajes");
//		}
		log.println("devuelve**** " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}
	
	
	@RequestMapping(value = "/consultar/{clase}/{parametro}", method = RequestMethod.GET)
	public Respuesta consultar(HttpServletRequest request,@PathVariable String clase,@PathVariable String parametro) {
		log.println("ingresa a consultar%%: " + clase + " - "  + parametro + " - " + request.getParameter("pagina"));
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try{
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			log.println("pagina** " + parameters.get("pagina"));
			log.println("filas: " + parameters.get("filas"));
			
			//Menu
			if(clase.equals("menu")){
				jsonObject=ConsultasUtil.consultaMenu(parameters, jsonObject);
			}
			//Perfilpermiso
			else if(clase.equals("perfilpermiso")){
				jsonObject=ConsultasUtil.consultaPerfilpermiso(parameters, jsonObject);
			}

			//Perfil
			else if(clase.equals("perfil")){
				jsonObject=ConsultasUtil.consultaPerfil(parameters, jsonObject);
			}

			//Permisoobjeto
			else if(clase.equals("permisoobjeto")){
				jsonObject=ConsultasUtil.consultaPermisoobjeto(parameters, jsonObject);
			}
			
			//Permiso
			else if(clase.equals("permiso")){
				jsonObject=ConsultasUtil.consultaPermiso(parameters, jsonObject);
			}


			//Usuario
			else if(clase.equals("usuario")){
				jsonObject=ConsultasUtil.consultaUsuario(parameters, jsonObject);
			}
			
			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				jsonObject=ConsultasUtil.consultaUsuariounidad(parameters, jsonObject);
			}
			mensajes.setMsg("Exito al obtener");
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			log.println("json retornado: " + jsonObject.toString()); 
		}catch (Exception e) {
			e.printStackTrace();
			mensajes.setMsg(MensajesWeb.getString("error.obtener"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			mensajes.setDescripcion(e.getMessage());
			respuesta.setEstado(false);
		}
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}	
}
