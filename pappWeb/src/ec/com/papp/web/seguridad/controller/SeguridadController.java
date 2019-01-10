package ec.com.papp.web.seguridad.controller;

import java.io.StringReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.tools.commons.to.OrderBy;
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
import ec.com.papp.web.administracion.controller.ComunController;
import ec.com.papp.web.comun.util.CambioClave;
import ec.com.papp.web.comun.util.ConstantesSesion;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.papp.web.seguridad.util.ConsultasUtil;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

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
				accion = (menuTO.getId()==null)?"I":"U";
				//si vienen 0 en padre o permiso le asigno null
				if(menuTO.getPadreid()!=null && menuTO.getPadreid().longValue()==0)
					menuTO.setPadreid(null);
				if(menuTO.getPermisoid()!=null && menuTO.getPermisoid().longValue()==0)
					menuTO.setPermisoid(null);
				UtilSession.seguridadServicio.transCrearModificarMenu(menuTO);
				id=menuTO.getId().toString();
				menuTO=UtilSession.seguridadServicio.transObtenerMenuTO(menuTO.getId());
				jsonObject.put("menu", (JSONObject)JSONSerializer.toJSON(menuTO,menuTO.getJsonConfig()));
			}
			//Perfilpermiso
			else if(clase.equals("perfilpermiso")){
				PerfilpermisoTO perfilpermisoTO = gson.fromJson(new StringReader(objeto), PerfilpermisoTO.class);
				accion = (perfilpermisoTO.getId()==null)?"I":"U";
				UtilSession.seguridadServicio.transCrearModificarPerfilpermiso(perfilpermisoTO);
				//id=perfilpermisoTO.getId().toString();
				perfilpermisoTO=UtilSession.seguridadServicio.transObtenerPerfilpermisoTO(new PerfilpermisoID(perfilpermisoTO.getId().getPerfilid(), perfilpermisoTO.getId().getPermisoid()));
				jsonObject.put("perfilpermiso", (JSONObject)JSONSerializer.toJSON(perfilpermisoTO,perfilpermisoTO.getJsonConfig()));
			}

			//Perfil
			else if(clase.equals("perfil")){
				PerfilTO perfilTO = gson.fromJson(new StringReader(objeto), PerfilTO.class);
				accion = (perfilTO.getId()==null)?"I":"U";
				UtilSession.seguridadServicio.transCrearModificarPerfil(perfilTO);
				//id=perfilTO.getId().toString();
				jsonObject.put("perfil", (JSONObject)JSONSerializer.toJSON(perfilTO,perfilTO.getJsonConfig()));
				//traigo los permisos
				PerfilpermisoTO perfilpermisoTO=new PerfilpermisoTO();
				perfilpermisoTO.getId().setPerfilid(perfilTO.getId());
				perfilpermisoTO.setPerfil(new PerfilTO());
				perfilpermisoTO.setPermiso(new PermisoTO());
				Collection<PerfilpermisoTO> perfilpermisoTOs=UtilSession.seguridadServicio.transObtenerPerfilpermiso(perfilpermisoTO);
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(perfilpermisoTOs,perfilpermisoTO.getJsonConfig()));
			}

			//Permiso
			else if(clase.equals("permiso")){
				PermisoTO permisoTO = gson.fromJson(new StringReader(objeto), PermisoTO.class);
				accion = (permisoTO.getId()==null)?"I":"U";
				UtilSession.seguridadServicio.transCrearModificarPermiso(permisoTO);
				id=permisoTO.getId().toString();
				jsonObject.put("permiso", (JSONObject)JSONSerializer.toJSON(permisoTO,permisoTO.getJsonConfig()));
			}

			//Usuario
			else if(clase.equals("usuario")){
				UsuarioTO usuarioTO = gson.fromJson(new StringReader(objeto), UsuarioTO.class);
				accion = (usuarioTO.getId()==null)?"I":"U";
				if(usuarioTO.getNpfechaactualizacionclave()!=null)
					usuarioTO.setFechaactualizacionclave(UtilGeneral.parseStringToDate(usuarioTO.getNpfechaactualizacionclave()));
				//Verifico que no se repita el usuario
				boolean grabar=true;
				UsuarioTO usuarioTO2=new UsuarioTO();
				usuarioTO2.setUsuario(usuarioTO.getUsuario());
				usuarioTO2.setEstado1("1");
				Collection<UsuarioTO> usuarios=UtilSession.seguridadServicio.transObtenerusuario(usuarioTO2);
				if(usuarios.size()>0) {
					for(UsuarioTO usuarioTO3:usuarios) {
						if((usuarioTO.getId()!=null && usuarioTO.getId().longValue()!=0) && usuarioTO3.getId().longValue()!=usuarioTO.getId().longValue() && usuarioTO3.getUsuario().equals(usuarioTO.getUsuario())) {
							grabar=false;
							break;
						}
						else if((usuarioTO.getId()==null || (usuarioTO.getId()!=null && usuarioTO3.getId().longValue()!=usuarioTO.getId().longValue())) && usuarioTO.getUsuario()!=null && usuarioTO3.getUsuario().equals(usuarioTO.getUsuario())) {
							grabar=false;
							break;
						}
					}
					if(!grabar) {
						mensajes.setMsg("El usuario ingresado ya existe, por favor seleccione otro");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
				if(grabar) {
					//Verifico que no se repita el empleado
					UsuarioTO usuarioTO4=new UsuarioTO();
					usuarioTO4.setEstado1("1");
					usuarioTO4.setSocionegocioid(usuarioTO.getSocionegocioid());
					Collection<UsuarioTO> usuarioTOs=UtilSession.seguridadServicio.transObtenerusuario(usuarioTO4);
					if(usuarioTOs.size()>0) {
						for(UsuarioTO usuarioTO3:usuarioTOs) {
							if((usuarioTO.getId()!=null && usuarioTO.getId().longValue()!=0) && usuarioTO3.getId().longValue()!=usuarioTO.getId().longValue() && usuarioTO3.getSocionegocioid().longValue()==usuarioTO.getSocionegocioid().longValue()) {
								//grabar=false;
								break;
							}
							else if((usuarioTO.getId()==null || (usuarioTO.getId()!=null && usuarioTO3.getId().longValue()!=usuarioTO.getId().longValue()))  && usuarioTO3.getSocionegocioid().longValue()==usuarioTO.getSocionegocioid().longValue()) {
								//grabar=false;
								break;
							}
						}
						if(!grabar) {
							mensajes.setMsg("El empleado ya se encuentra asignado a un usuario, por favor seleccione otro");
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
				}
				System.out.println("grabar: " + grabar);
				if(grabar) {
					if(usuarioTO.getId()==null) {
						usuarioTO.setClave(MensajesWeb.getString("clave.inicial.usuario"));
						usuarioTO.setCambiarclave("1");
						usuarioTO.setFechaactualizacionclave(new Date());
						usuarioTO.setEstado1("1");
					}
					else {
						//Si la clave es distinta la encripto nuevamente
						UsuarioTO usuariograbado=UtilSession.seguridadServicio.transObtenerUsuarioTO(usuarioTO.getId());
						if(!usuarioTO.getClave().equals(usuariograbado.getClave())){
							//System.out.println("va a cambiar clave");
							String clave=usuarioTO.getClave();
							//usuarioTO.setClave(ConsultasUtil.encriptarClave(clave.toLowerCase()));
							usuarioTO.setClave(ConsultasUtil.encriptarClave(clave));
							usuarioTO.setCambiarclave("1");
							usuarioTO.setFechaactualizacionclave(new Date());
						}
					}
					UtilSession.seguridadServicio.transCrearModificarusuario(usuarioTO,false);
					System.out.println("guardo");
					//id=usuarioTO.getId().toString();
					jsonObject.put("usuario", (JSONObject)JSONSerializer.toJSON(usuarioTO,usuarioTO.getJsonConfig()));
				}
			}
			
			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				UsuariounidadTO usuariounidadTO = gson.fromJson(new StringReader(objeto), UsuariounidadTO.class);
				accion = (usuariounidadTO.getId()==null)?"I":"U";
				UtilSession.estructuraorganicaServicio.transCrearModificarUsuariounidad(usuariounidadTO);
				id=usuariounidadTO.getId().toString();
				jsonObject.put("usuariounidad", (JSONObject)JSONSerializer.toJSON(usuariounidadTO,usuariounidadTO.getJsonConfig()));
			}

			//Registro la auditoria
//			if(mensajes.getMsg()==null)
//				FormularioUtil.crearAuditoria(request, clase, accion, objeto, id);
			if(mensajes.getMsg()==null){
				ComunController.crearAuditoria(request, clase, accion, objeto, id);
				mensajes.setMsg(MensajesWeb.getString("mensaje.guardar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			else
				respuesta.setEstado(false);
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
		System.out.println("retorna: " + respuesta.getJson()+ "-"+ respuesta.getMensajes().getMsg());
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
				perfilpermisoTO.setPerfil(new PerfilTO());
				perfilpermisoTO.setPermiso(new PermisoTO());
				perfilpermisoTO.setOrderByField(OrderBy.orderAsc("id"));
				Collection<PerfilpermisoTO> perfilpermisoTOs=UtilSession.seguridadServicio.transObtenerPerfilpermiso(perfilpermisoTO);
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(perfilpermisoTOs,perfilpermisoTO.getJsonConfig()));
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
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(usuariounidadTOs,usuariounidadTO.getJsonConfig()));
			}

			//Usuariounidad
			else if(clase.equals("usuariounidad")){
				UsuariounidadTO usuariounidadTO = UtilSession.estructuraorganicaServicio.transObtenerUsuariounidadTO(new UsuariounidadID(id, id2));
				jsonObject.put("usuariounidad", (JSONObject)JSONSerializer.toJSON(usuariounidadTO,usuariounidadTO.getJsonConfig()));
			}
			
			log.println("json retornado: " + jsonObject.toString());
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
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
	
	@RequestMapping(value = "/{clase}/{id}/{id2}", method = RequestMethod.DELETE)
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	public Respuesta eliminar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,HttpServletRequest request){
		log.println("entra al metodo eliminar");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			//Menu
			if(clase.equals("menu")){
				MenuTO menuTO=new MenuTO();
				menuTO.setId(id);
				UtilSession.seguridadServicio.transEliminarMenu(menuTO);
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
			ComunController.crearAuditoria(request, clase, "E", null, id.toString());
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
	
	/**
	* Metodo para llamar al cambio de clave
	*/
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String cambioclave(HttpServletRequest request){
		JSONObject jsonObject=new JSONObject();
		UsuarioTO usuarioTO=(UsuarioTO)UtilSession.getUsuario(request);
		jsonObject.put("ususario", (JSONObject)JSONSerializer.toJSON(usuarioTO,usuarioTO.getJsonConfig()));
		return jsonObject.toString();
	}
	
	/**
	* Metodo para traer los perfiles de usuario
	*/
//	@RequestMapping(value = "/perfil/id", method = RequestMethod.GET)
//	public String perfil(HttpServletRequest request,@RequestBody Long id){
//		JSONObject jsonObject=new JSONObject();
//		UsuarioTO usuarioTO=(UsuarioTO)UtilSession.getUsuario(request);
//		jsonObject.put("ususario", (JSONObject)JSONSerializer.toJSON(usuarioTO,usuarioTO.getJsonConfig()));
//		return jsonObject.toString();
//	}
	
	@RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
	public Respuesta cambiarClave(@RequestBody String objeto,HttpServletRequest request){
		log.println("entra cambiar la clave: " + objeto);
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			CambioClave cambioClave = gson.fromJson(new StringReader(objeto), CambioClave.class);
			//Usuario logeado
			UsuarioTO usuario = UtilSession.getUsuario(request);//preguntar esto!!!
			//Recupero el usuario de sesion
			log.println("clave anterior: " +usuario.getClave());
			log.println("clave nueva: " +cambioClave.getClave());
			log.println("clave confirma: " +cambioClave.getConfirmacion());
			log.println("clave anterior: " +cambioClave.getClaveanterior());
			if(ConsultasUtil.encriptarClave(cambioClave.getClaveanterior()).equals(usuario.getClave())){
				log.println("paso primera");
			//if(bean.getPassword()!=null && bean.getPassword().length()>=8){
				if(cambioClave.getClave().equals(cambioClave.getConfirmacion())){
					log.println("paso segunda");
					if(!(ConsultasUtil.encriptarClave(cambioClave.getClave())).equals(usuario.getClave())){
						log.println("paso tercera");
						usuario.setClave(ConsultasUtil.encriptarClave(cambioClave.getClave()));
						//usuario.setFechaClave(new Date());
						usuario.setCambiarclave("0");
						log.println("va a guardar el usuario");
						log.println("el nombre de usuario es:"+usuario.getNombre());
						usuario.setFechaactualizacionclave(new Date());
						UtilSession.seguridadServicio.transCrearModificarusuario(usuario,true);
					}
					else{
						log.println("La clave es igual a la guardada en base");
						mensajes.setMsg(MensajesWeb.getString("error.claveIgual"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						respuesta.setEstado(false);
					}
				}
				else{
					log.println("La nueva clave es diferente a la confirmacion");
					mensajes.setMsg(MensajesWeb.getString("error.claveValidacion"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			else{
				log.println("La nueva clave es diferente a la confirmacion");
				mensajes.setMsg("La clave anterior ingresada esta incorrecta");
				mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				respuesta.setEstado(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al obtener para editar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
		}
		if(mensajes.getMsg()!=null)
			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}
}
