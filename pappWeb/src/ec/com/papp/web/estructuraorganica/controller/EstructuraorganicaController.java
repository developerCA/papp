package ec.com.papp.web.estructuraorganica.controller;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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

import ec.com.papp.administracion.to.CargoTO;
import ec.com.papp.administracion.to.ClasificacionTO;
import ec.com.papp.administracion.to.EmpleadoTO;
import ec.com.papp.administracion.to.EspecialidadTO;
import ec.com.papp.administracion.to.FuerzaTO;
import ec.com.papp.administracion.to.GradoTO;
import ec.com.papp.administracion.to.GradofuerzaTO;
import ec.com.papp.administracion.to.SocionegocioTO;
import ec.com.papp.estructuraorganica.id.InstitucionentidadID;
import ec.com.papp.estructuraorganica.id.UnidadarbolplazaID;
import ec.com.papp.estructuraorganica.id.UnidadarbolplazaempleadoID;
import ec.com.papp.estructuraorganica.id.UnidadinstID;
import ec.com.papp.estructuraorganica.to.EstructuraorganicaTO;
import ec.com.papp.estructuraorganica.to.InstitucionTO;
import ec.com.papp.estructuraorganica.to.InstitucionentidadTO;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolplazaTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolplazaempleadoTO;
import ec.com.papp.estructuraorganica.to.UnidadinstTO;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.estructuraorganica.util.ConsultasUtil;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;

/**
 * @autor: jcalderon
 * @fecha: 27-10-2015
 * @copyright: Xcelsa
 * @version: 1.0
 * @descripcion Clase para realizar administraciones centralizadas de estructuraorganica
*/

@RestController
@RequestMapping("/rest/estructuraorganica")
public class EstructuraorganicaController {
	private Log log = new Log(EstructuraorganicaController.class);
	
	@RequestMapping(value = "/{clase}", method = RequestMethod.POST)
	public Respuesta grabar(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo grabar: " + clase + " - "+ objeto);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		String id="";
		String accion="";
		try {

			//Instituto entidad
			if(clase.equals("institutoentidad")){
				InstitucionentidadTO institucionentidadTO = gson.fromJson(new StringReader(objeto), InstitucionentidadTO.class);
				accion = (institucionentidadTO.getId()==null)?"crear":"actualizar";
				UtilSession.estructuraorganicaServicio.transCrearModificarInstitucionentidad(institucionentidadTO);
				id=institucionentidadTO.getId().toString();
				jsonObject.put("institutoentidad", (JSONObject)JSONSerializer.toJSON(institucionentidadTO,institucionentidadTO.getJsonConfig()));
			}


			
			//Estructura organica
			else if(clase.equals("estructuraorganica")){
				EstructuraorganicaTO estructuraorganicaTO = gson.fromJson(new StringReader(objeto), EstructuraorganicaTO.class);
				estructuraorganicaTO.setFecvigfin(UtilGeneral.parseStringToDate(estructuraorganicaTO.getNpfecvigfin()));
				estructuraorganicaTO.setFecviginicio(UtilGeneral.parseStringToDate(estructuraorganicaTO.getNpfecviginicio()));
				//Debo validar que no existe un perido vigente en el mismo rango de fechas
				//Traigo todas las estructuras vigeentes 
				EstructuraorganicaTO estructuraorganicaTO2=new EstructuraorganicaTO();
				estructuraorganicaTO2.setEstado("V");
				Collection<EstructuraorganicaTO> estructuraorganicaTOs=UtilSession.estructuraorganicaServicio.transObtenerEstructuraorganica(estructuraorganicaTO2);
				log.println("estructuras encontradas " + estructuraorganicaTOs.size());
				Calendar cal1 = new GregorianCalendar();
				Calendar cal2 = new GregorianCalendar();
				Calendar cal3 = new GregorianCalendar();
				cal3.setTime(estructuraorganicaTO.getFecviginicio());
				Calendar cal4 = new GregorianCalendar();
				cal4.setTime(estructuraorganicaTO.getFecvigfin());
				boolean estaenrango=false;
				for(EstructuraorganicaTO estructuraorganicaTO3:estructuraorganicaTOs){
					cal1.setTime(estructuraorganicaTO3.getFecviginicio());
					cal2.setTime(estructuraorganicaTO3.getFecvigfin());
					if (cal3.after(cal1) && cal3.before(cal2)){
						estaenrango=true;
						break;
					}
					if (cal4.after(cal1) && cal4.before(cal2)){
						estaenrango=true;
						break;
					}
				}
				if(estaenrango){
					mensajes.setMsg(MensajesWeb.getString("advertencia.rangofechas"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					accion = (estructuraorganicaTO.getId()==null)?"crear":"actualizar";
					UtilSession.estructuraorganicaServicio.transCrearModificarEstructuraorganica(estructuraorganicaTO);
					id=estructuraorganicaTO.getId().toString();
					jsonObject.put("estructuraorganica", (JSONObject)JSONSerializer.toJSON(estructuraorganicaTO,estructuraorganicaTO.getJsonConfig()));
				}
			}

			//Unidad
			else if(clase.equals("unidad")){
				UnidadTO unidadTO = gson.fromJson(new StringReader(objeto), UnidadTO.class);
				accion = (unidadTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				UnidadTO unidadTO2=new UnidadTO();
				unidadTO2.setCodigopresup(unidadTO.getCodigopresup());
				Collection<UnidadTO> itemTOs=UtilSession.estructuraorganicaServicio.transObtenerUnidad(unidadTO2);
				boolean grabar=true;
				if(itemTOs.size()>0){
					unidadTO2=(UnidadTO)itemTOs.iterator().next();
					if(unidadTO.getId()!=null && unidadTO2.getId().longValue()!=unidadTO.getId().longValue())
						grabar=false;
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.estructuraorganicaServicio.transCrearModificarUnidad(unidadTO);
					id=unidadTO.getId().toString();
					jsonObject.put("unidad", (JSONObject)JSONSerializer.toJSON(unidadTO,unidadTO.getJsonConfig()));
				}
			}

			//Unidad arbol
			else if(clase.equals("unidadarbol")){
				UnidadarbolTO unidadarbolTO = gson.fromJson(new StringReader(objeto), UnidadarbolTO.class);
				unidadarbolTO.setProceso("-");
				accion = (unidadarbolTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				UnidadarbolTO unidadarbolTO2=new UnidadarbolTO();
				unidadarbolTO2.setUnidadarbolpadreid(unidadarbolTO.getUnidadarbolpadreid());
				unidadarbolTO2.setCodigoorganico(unidadarbolTO.getCodigoorganico());
				unidadarbolTO2.setEstado(unidadarbolTO.getEstado());
				Collection<UnidadarbolTO> unidadarbolTOs=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbol(unidadarbolTO2);
				boolean grabar=true;
				if(unidadarbolTOs.size()>0){
					unidadarbolTO2=(UnidadarbolTO)unidadarbolTOs.iterator().next();
					if(unidadarbolTO.getId()!=null && unidadarbolTO2.getId().longValue()!=unidadarbolTO.getId().longValue())
						grabar=false;
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.estructuraorganicaServicio.transCrearModificarUnidadarbol(unidadarbolTO);
					id=unidadarbolTO.getId().toString();
					jsonObject.put("unidadarbol", (JSONObject)JSONSerializer.toJSON(unidadarbolTO,unidadarbolTO.getJsonConfig()));
				}
			}

			//Unidad arbol plaza
			else if(clase.equals("unidadarbolplaza")){
				UnidadarbolTO unidadarbolTO = gson.fromJson(new StringReader(objeto), UnidadarbolTO.class);
				//accion = (unidadarbolTO.getId()==null)?"crear":"actualizar";
				UtilSession.estructuraorganicaServicio.transCrearModificarUnidadarbolplaza(unidadarbolTO.getDetails(), unidadarbolTO.getId());
				//id=unidadarbolTO.getId().toString();
				//jsonObject.put("unidadarbolplaza", (JSONObject)JSONSerializer.toJSON(unidadarbolTO,unidadarbolTO.getJsonConfig()));
			}

			//Unidad arbol plaza empleado
			else if(clase.equals("unidadarbolplazaempleado")){
				UnidadarbolplazaTO unidadarbolplazaTO = gson.fromJson(new StringReader(objeto), UnidadarbolplazaTO.class);
				log.println("id** " + unidadarbolplazaTO.getId().getId());
				log.println("plaza id: " + unidadarbolplazaTO.getId().getPlazaid());
				log.println("codigo" + unidadarbolplazaTO.getCodigo());
				for(UnidadarbolplazaempleadoTO unidadarbolplazaempleadoTO:unidadarbolplazaTO.getDetails()) {
					log.println("fechafinc: " + unidadarbolplazaempleadoTO.getNpfechafinc());
					log.println("fechainicio: " + unidadarbolplazaempleadoTO.getNpfechainicioc());
					log.println("npsocionegocioid " + unidadarbolplazaempleadoTO.getNpsocionegocioid());
					if(unidadarbolplazaempleadoTO.getNpfechafinc()!=null)
						unidadarbolplazaempleadoTO.setFechafin(UtilGeneral.parseStringToDate(unidadarbolplazaempleadoTO.getNpfechafinc()));
					if(unidadarbolplazaempleadoTO.getNpfechainicioc()!=null)
					unidadarbolplazaempleadoTO.setFechainicio(UtilGeneral.parseStringToDate(unidadarbolplazaempleadoTO.getNpfechainicioc()));
				}
				//accion = (unidadarbolplazaempleadoTO.getId()==null)?"crear":"actualizar";
				UtilSession.estructuraorganicaServicio.transCrearModificarUnidadarbolplazaempleado(unidadarbolplazaTO.getDetails(), unidadarbolplazaTO.getId().getId(), unidadarbolplazaTO.getId().getPlazaid());
				//id=unidadarbolplazaempleadoTO.getId().toString();
				//jsonObject.put("unidadarbolplazaempleado", (JSONObject)JSONSerializer.toJSON(unidadarbolplazaempleadoTO,unidadarbolplazaempleadoTO.getJsonConfig()));
			}

			//Unidad institucion
			else if(clase.equals("unidadainstitucion")){
				UnidadinstTO unidadinstTO = gson.fromJson(new StringReader(objeto), UnidadinstTO.class);
				accion = (unidadinstTO.getId()==null)?"crear":"actualizar";
				UtilSession.estructuraorganicaServicio.transCrearModificarUnidadinst(unidadinstTO);
				id=unidadinstTO.getId().toString();
				jsonObject.put("unidadarbolplaza", (JSONObject)JSONSerializer.toJSON(unidadinstTO,unidadinstTO.getJsonConfig()));
			}
			
			//Registro la auditoria
//			if(mensajes.getMsg()==null)
//				FormularioUtil.crearAuditoria(request, clase, accion, objeto, id);
			if(mensajes.getMsg()==null){
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
		return respuesta;	
	}
	
	@RequestMapping(value = "/{clase}/{id}/{id2}/{id3}", method = RequestMethod.GET)
	public Respuesta editar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,@PathVariable Long id3,HttpServletRequest request){
		log.println("entra al metodo recuperar: " + id);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {

			//Instituto entidad
			if(clase.equals("institutoentidad")){
				InstitucionentidadID institucionentidadID=new InstitucionentidadID(id, id2);
				InstitucionentidadTO institucionentidadTO = UtilSession.estructuraorganicaServicio.transObtenerInstitucionentidadTO(institucionentidadID);
				jsonObject.put("institutoentidad", (JSONObject)JSONSerializer.toJSON(institucionentidadTO,institucionentidadTO.getJsonConfig()));
			}
			
			//Estructura organica
			else if(clase.equals("estructuraorganica")){
				EstructuraorganicaTO estructuraorganicaTO = UtilSession.estructuraorganicaServicio.transObtenerEstructuraorganicaTO(id);
				estructuraorganicaTO.setNpfecvigfin(UtilGeneral.parseDateToString(estructuraorganicaTO.getFecvigfin()));
				estructuraorganicaTO.setNpfecviginicio(UtilGeneral.parseDateToString(estructuraorganicaTO.getFecviginicio()));
				jsonObject.put("estructuraorganica", (JSONObject)JSONSerializer.toJSON(estructuraorganicaTO,estructuraorganicaTO.getJsonConfigEdicion()));
			}

			//Unidad
			else if(clase.equals("unidad")){
				UnidadTO unidadTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadTO(id);
				jsonObject.put("unidad", (JSONObject)JSONSerializer.toJSON(unidadTO,unidadTO.getJsonConfig()));
				UnidadinstTO unidadinstTO = new UnidadinstTO();
				InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
				unidadinstTO.getId().setUnidad(unidadTO.getId());
				institucionentidadTO.setInstitucion(new InstitucionTO());
				unidadinstTO.setInstitucionentidad(institucionentidadTO);
				Collection<UnidadinstTO> unidadinstTOs = UtilSession.estructuraorganicaServicio.transObtenerUnidadinst(unidadinstTO);
				//Asigno la variable que necesitan para la administracion
				for(UnidadinstTO unidadinstTO2:unidadinstTOs){
					unidadinstTO2.setUnidadinstitucionid(unidadinstTO2.getId().getId());
				}
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(unidadinstTOs,unidadinstTO.getJsonConfig()));
			}

			//Unidad arbol
			else if(clase.equals("unidadarbol")){
				UnidadarbolTO unidadarbolTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolTO(id);
				jsonObject.put("unidadarbol", (JSONObject)JSONSerializer.toJSON(unidadarbolTO,unidadarbolTO.getJsonConfigedit()));
			}
			
			//Unidad arbol
			else if(clase.equals("unidadarboldetail")){
				UnidadarbolTO unidadarbolTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolTO(id);
				log.println("nombre" + unidadarbolTO.getNombre());
				//Obtengo los unidadarbolplazaid
				UnidadarbolplazaTO unidadarbolplazaTO=new UnidadarbolplazaTO();
				unidadarbolplazaTO.getId().setId(id);
				unidadarbolplazaTO.setClasificacion(new ClasificacionTO());
				unidadarbolplazaTO.setEspecialidad(new EspecialidadTO());
				GradofuerzaTO gradofuerzaTO=new GradofuerzaTO();
				gradofuerzaTO.setGrado(new GradoTO());
				gradofuerzaTO.setFuerza(new FuerzaTO());
				unidadarbolplazaTO.setGradofuerza(gradofuerzaTO);
				unidadarbolplazaTO.setCargo(new CargoTO());
				Collection<UnidadarbolplazaTO> unidadarbolplazaTOs=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplaza(unidadarbolplazaTO);
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(unidadarbolplazaTOs,unidadarbolplazaTO.getJsonConfig()));
				jsonObject.put("unidadarbol", (JSONObject)JSONSerializer.toJSON(unidadarbolTO,unidadarbolTO.getJsonConfigedit()));
			}

			//Unidad arbol plaza
			else if(clase.equals("unidadarbolplaza")){
				UnidadarbolplazaID unidadarbolplazaID=new UnidadarbolplazaID(id, id2);
				UnidadarbolplazaTO unidadarbolplazaTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplazaTO(unidadarbolplazaID);
				jsonObject.put("unidadarbolplaza", (JSONObject)JSONSerializer.toJSON(unidadarbolplazaTO,unidadarbolplazaTO.getJsonConfig()));
				//traigo las unidadarbolplazaempleado
				UnidadarbolplazaempleadoTO unidadarbolplazaempleadoTO=new UnidadarbolplazaempleadoTO();
				unidadarbolplazaempleadoTO.getId().setId(id);
				unidadarbolplazaempleadoTO.getId().setPlazaid(id2);
				unidadarbolplazaempleadoTO.setSocionegocio(new SocionegocioTO());
				Collection<UnidadarbolplazaempleadoTO> unidadarbolplazaempleadoTOs=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplazaempleado(unidadarbolplazaempleadoTO);
				for(UnidadarbolplazaempleadoTO unidadarbolplazaempleadoTO2:unidadarbolplazaempleadoTOs) {
					if(unidadarbolplazaempleadoTO2.getFechainicio()!=null)
						unidadarbolplazaempleadoTO2.setNpfechainicioc(UtilGeneral.parseDateToString(unidadarbolplazaempleadoTO2.getFechainicio()));
					if(unidadarbolplazaempleadoTO2.getFechafin()!=null)
						unidadarbolplazaempleadoTO2.setNpfechafinc(UtilGeneral.parseDateToString(unidadarbolplazaempleadoTO2.getFechafin()));
				}
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(unidadarbolplazaempleadoTOs,unidadarbolplazaempleadoTO.getJsonConfig()));
			}

			//Unidad arbol plaza empleado
			else if(clase.equals("unidadarbolplazaempleado")){
				UnidadarbolplazaempleadoID unidadarbolplazaempleadoID=new UnidadarbolplazaempleadoID(id, id2, id3);
				UnidadarbolplazaempleadoTO unidadarbolplazaempleadoTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplazaempleadoTO(unidadarbolplazaempleadoID);
				jsonObject.put("unidadarbolplazaempleado", (JSONObject)JSONSerializer.toJSON(unidadarbolplazaempleadoTO,unidadarbolplazaempleadoTO.getJsonConfigEdicion()));
			}
			
			//Unidad institucion
			else if(clase.equals("unidadainstitucion")){
				UnidadinstID unidadinstID=new UnidadinstID(id,id2);
				UnidadinstTO unidadinstTO = UtilSession.estructuraorganicaServicio.transObtenerUnidadinstTO(unidadinstID);
				jsonObject.put("unidadainstitucion", (JSONObject)JSONSerializer.toJSON(unidadinstTO,unidadinstTO.getJsonConfig()));
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
			
			//Instituto entidad
			if(clase.equals("institutoentidad")){
				UtilSession.estructuraorganicaServicio.transEliminarInstitucionentidad(new InstitucionentidadTO(new InstitucionentidadID(id, id2)));
			}

			//Estructura organica
			else if(clase.equals("estructuraorganica")){
				UtilSession.estructuraorganicaServicio.transEliminarEstructuraorganica(new EstructuraorganicaTO(id));
			}

			//Unidad
			else if(clase.equals("unidad")){
				UtilSession.estructuraorganicaServicio.transEliminarUnidad(new UnidadTO(id));
			}
			
			//Empleado
			else if(clase.equals("empleado")){
				UtilSession.adminsitracionServicio.transEliminarEmpleado(new EmpleadoTO(id));
			}

			//Unidad arbol
			else if(clase.equals("unidadarbol")){
				UtilSession.estructuraorganicaServicio.transEliminarUnidadarbol(new UnidadarbolTO(id));
			}

			//Unidad arbol plaza
			else if(clase.equals("unidadarbolplaza")){
				UtilSession.estructuraorganicaServicio.transEliminarUnidadarbolplaza(new UnidadarbolplazaTO(new UnidadarbolplazaID(id, id2)));
			}

			//Unidad arbol plaza empleado
			else if(clase.equals("unidadarbolplazaempleado")){
				UtilSession.estructuraorganicaServicio.transEliminarUnidadarbolplazaempleado(new UnidadarbolplazaempleadoTO(new UnidadarbolplazaempleadoID(id, id2, id3)));
			}
			
			//Unidad institucion
			else if(clase.equals("unidadainstitucion")){
				UtilSession.estructuraorganicaServicio.transEliminarUnidadinst(new UnidadinstTO(new UnidadinstID(id, id2)));
			}

			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.eliminar") + " " + clase);
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.estructuraorganicaServicio.transCrearModificarAuditoria(auditoriaTO);
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
		log.println("ingresa a consultar: " + clase + " - "  + parametro + " - " + request.getParameter("pagina"));
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
			
			
			//Instituto entidad
			if(clase.equals("institutoentidad")){
				jsonObject=ConsultasUtil.consultaInstitucionentidadPaginado(parameters, jsonObject);
			}
			//Instituto entidad
			else if(clase.equals("institucionentidad")){
				jsonObject=ConsultasUtil.consultaInstitucionentidadReporte(parameters, jsonObject);
			}

			//Empleado
			else if(clase.equals("empleado")){
				jsonObject=ConsultasUtil.consultaEmpleadoPaginado(parameters, jsonObject);
			}
			
			//Estructura organica
			else if(clase.equals("estructuraorganica")){
				jsonObject=ConsultasUtil.consultaEstructuraorganicaPaginado(parameters, jsonObject);
			}

			//Unidad
			else if(clase.equals("unidad")){
				jsonObject=ConsultasUtil.consultaUnidadPaginado(parameters, jsonObject,"unidad");
				log.println("json retornado unidad: " + jsonObject.toString()); 
			}
			
			//Unidadbusqueda
			else if(clase.equals("unidadbusqueda")){
				jsonObject=ConsultasUtil.consultaUnidadPaginado(parameters, jsonObject,"unidadbusqueda");
			}

			//consultaUnidadReporte
			else if(clase.equals("consultaUnidadReporte")){
				jsonObject=ConsultasUtil.consultaUnidadReporte(parameters, jsonObject);
			}
			
			
			//Unidad arbol
			else if(clase.equals("unidadarbol")){
				jsonObject=ConsultasUtil.consultaUnidadaarboarbol(parameters, jsonObject);
			}

			//Unidad arbol plaza
			else if(clase.equals("unidadarbolplaza")){
				jsonObject=ConsultasUtil.consultaUnidadarbolplazaPaginado(parameters, jsonObject);
			}

			//Unidad arbol plaza empleado
			else if(clase.equals("unidadarbolplazaempleado")){
				jsonObject=ConsultasUtil.consultaUnidadarbolplazaempleadoPaginado(parameters, jsonObject);
			}
			//Unidad institucion
			else if(clase.equals("unidadainstitucion")){
				jsonObject=ConsultasUtil.consultaUnidadinstitucionPaginado(parameters, jsonObject);
			}
			log.println("json retornado: " + jsonObject.toString()); 
		}catch (Exception e) {
			e.printStackTrace();
			mensajes.setMsg(MensajesWeb.getString("error.obtener"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
		}
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}

//	@RequestMapping(value = "/cambiarClave", method = RequestMethod.POST)
//	public String cambiarClave(@RequestBody String objeto,HttpServletRequest request){
//		log.println("entra cambiar la clave: " + objeto);
//		Gson gson = new Gson();
//		JSONObject jsonObject=new JSONObject();
//		Mensajes mensajes=new Mensajes();
//		try {
//			CambioClave cambioClave = gson.fromJson(new StringReader(objeto), CambioClave.class);
//			//Usuario logeado
//			UsuarioTO usuario = UtilSession.getUsuario(request);//preguntar esto!!!
//			//Recupero el usuario de sesion
//			log.println("clave anterior: " +usuario.getClave());
//			//if(bean.getPassword()!=null && bean.getPassword().length()>=8){
//				if(cambioClave.getClave().equals(cambioClave.getConfirmacion())){
//					if(!(FormularioUtil.encriptarClave(cambioClave.getClave())).equals(usuario.getClave())){
//						usuario.setClave(FormularioUtil.encriptarClave(cambioClave.getClave()));
//						usuario.setFechaClave(new Date());
//						usuario.setCambiarclave("0");
//						log.println("va a guardar el usuario");
//						log.println("el nombre de usuario es:"+usuario.getNombre());
//						UtilSession.estructuraorganicaServicio.transCrearModificarUsuario(usuario);
//					}
//					else{
//						log.println("La clave es igual a la guardada en base");
//						mensajes.setMsg(MensajesWeb.getString("error.claveIgual"));
//						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//					}
//				}
//				else{
//					log.println("La nueva clave es diferente a la confirmacion");
//					mensajes.setMsg(MensajesWeb.getString("error.claveValidacion"));
//					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//				}
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.println("error al obtener para editar");
//			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
//			mensajes.setType(MensajesWeb.getString("mensaje.error"));
//		}
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
//		return jsonObject.toString();	
//	}
	
}
