package ec.com.papp.web.planificacion.controller;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.tools.commons.to.OrderBy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.com.papp.administracion.to.ObraTO;
import ec.com.papp.administracion.to.OrganismoTO;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.planificacion.id.ActividadunidadID;
import ec.com.papp.planificacion.id.ProyectometaID;
import ec.com.papp.planificacion.id.SubactividadunidadID;
import ec.com.papp.planificacion.to.ActividadTO;
import ec.com.papp.planificacion.to.ActividadunidadTO;
import ec.com.papp.planificacion.to.ActividadunidadacumuladorTO;
import ec.com.papp.planificacion.to.CronogramaTO;
import ec.com.papp.planificacion.to.CronogramalineaTO;
import ec.com.papp.planificacion.to.IndicadorTO;
import ec.com.papp.planificacion.to.IndicadormetodoTO;
import ec.com.papp.planificacion.to.ItemunidadTO;
import ec.com.papp.planificacion.to.NivelactividadTO;
import ec.com.papp.planificacion.to.NivelprogramaTO;
import ec.com.papp.planificacion.to.ObjetivoTO;
import ec.com.papp.planificacion.to.OrganismoprestamoTO;
import ec.com.papp.planificacion.to.PlannacionalTO;
import ec.com.papp.planificacion.to.ProgramaTO;
import ec.com.papp.planificacion.to.ProyectoTO;
import ec.com.papp.planificacion.to.ProyectometaTO;
import ec.com.papp.planificacion.to.SubactividadTO;
import ec.com.papp.planificacion.to.SubactividadunidadTO;
import ec.com.papp.planificacion.to.SubitemunidadTO;
import ec.com.papp.planificacion.to.SubitemunidadacumuladorTO;
import ec.com.papp.planificacion.to.SubprogramaTO;
import ec.com.papp.planificacion.to.SubtareaunidadTO;
import ec.com.papp.planificacion.to.SubtareaunidadacumuladorTO;
import ec.com.papp.planificacion.to.TareaunidadTO;
import ec.com.papp.planificacion.util.MatrizDetalle;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.planificacion.util.ConsultasUtil;
import ec.com.papp.web.resource.MensajesWeb;
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
 * @descripcion Clase para realizar administraciones centralizadas de planificacion
*/

@RestController
@RequestMapping("/rest/planificacion")
public class PlanificacionController {
	private Log log = new Log(PlanificacionController.class);
	
	@RequestMapping(value = "/{clase}", method = RequestMethod.POST)
	public Respuesta grabar(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo grabar*****8: " + clase + " - " + objeto);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		String id="";
		String accion="";
		
		try {
			//Objetivo
			if(clase.equals("objetivo")){
				ObjetivoTO objetivoTO = gson.fromJson(new StringReader(objeto), ObjetivoTO.class);
				accion = (objetivoTO.getId()==null)?"crear":"actualizar";
				if(!objetivoTO.getTipo().equals("E")) {
					//pregunto si ya existe el codigo en el nivel actual
					ObjetivoTO objetivoTO2=new ObjetivoTO();
					objetivoTO2.setObjetivoejerciciofiscalid(objetivoTO.getObjetivoejerciciofiscalid());
					objetivoTO2.setCodigo(objetivoTO.getCodigo());
					objetivoTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
					objetivoTO2.setObjetivopadreid(objetivoTO.getObjetivopadreid());
					if(objetivoTO.getObjetivopadreid()!=null && objetivoTO.getObjetivopadreid().longValue()!=0)
						objetivoTO2.setObjetivopadreid(objetivoTO.getObjetivopadreid());
					Collection<ObjetivoTO> objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO2);
					log.println("objetivos encontrados: " + objetivoTOs.size());
					boolean grabar=true;
					if(objetivoTOs.size()>0){
						for(ObjetivoTO objetivoTO3:objetivoTOs) {
							if((objetivoTO.getId()!=null && objetivoTO.getId().longValue()!=0) && objetivoTO3.getId().longValue()!=objetivoTO.getId().longValue() && objetivoTO3.getCodigo().equals(objetivoTO.getCodigo())) {
								grabar=false;
								break;
							}
							else if((objetivoTO.getId()==null || (objetivoTO.getId()!=null && objetivoTO3.getId().longValue()!=objetivoTO.getId().longValue())) && objetivoTO.getCodigo()!=null && objetivoTO3.getCodigo().equals(objetivoTO.getCodigo())) {
								grabar=false;
								break;
							}
						}
	//					objetivoTO2=(ObjetivoTO)objetivoTOs.iterator().next();
	//					if(objetivoTO.getId()!=null && objetivoTO2.getId().longValue()!=objetivoTO.getId().longValue())
	//						grabar=false;
					}
					if(!grabar){
						mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
					else{
						//Si va a inactivar valido que no hayan hijos
						if(objetivoTO.getId()!=null && objetivoTO.getId().longValue()!=0 && objetivoTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
							ObjetivoTO hijo=new ObjetivoTO();
							hijo.setObjetivopadreid(objetivoTO.getId());
							hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
							Collection<ObjetivoTO> objetivoTOs2=UtilSession.planificacionServicio.transObtenerObjetivoArbol(hijo);
							if(objetivoTOs2.size()>0) {
								grabar=false;
								mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
								mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
							}
						}
						if(grabar) {
							UtilSession.planificacionServicio.transCrearModificarObjetivo(objetivoTO);
							id=objetivoTO.getNpid().toString();
							jsonObject.put("objetivo", (JSONObject)JSONSerializer.toJSON(objetivoTO,objetivoTO.getJsonConfig()));
						}
					}
				}
				else if(objetivoTO.getTipo().equals("E")) {
					//pregunto si ya existe el codigo en el nivel actual combinado con el codigo institucional
					ObjetivoTO objetivoTO2=new ObjetivoTO();
					objetivoTO2.setObjetivoejerciciofiscalid(objetivoTO.getObjetivoejerciciofiscalid());
					objetivoTO2.setCodigo(objetivoTO.getCodigo());
					objetivoTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
					objetivoTO2.setObjetivoinstitucionid(objetivoTO.getObjetivoinstitucionid());
					objetivoTO2.setObjetivopadreid(objetivoTO.getObjetivopadreid());
					if(objetivoTO.getObjetivopadreid()!=null && objetivoTO.getObjetivopadreid().longValue()!=0)
						objetivoTO2.setObjetivopadreid(objetivoTO.getObjetivopadreid());
					Collection<ObjetivoTO> objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO2);
					log.println("objetivos encontrados: " + objetivoTOs.size());
					boolean grabar=true;
					if(objetivoTOs.size()>0){
						for(ObjetivoTO objetivoTO3:objetivoTOs) {
							if((objetivoTO.getId()!=null && objetivoTO.getId().longValue()!=0) && objetivoTO3.getId().longValue()!=objetivoTO.getId().longValue() && objetivoTO3.getObjetivoinstitucionid().longValue()==objetivoTO.getObjetivoinstitucionid().longValue()) {
								grabar=false;
								break;
							}
							else if((objetivoTO.getId()==null || (objetivoTO.getId()!=null && objetivoTO3.getId().longValue()!=objetivoTO.getId().longValue())) && objetivoTO3.getObjetivoinstitucionid().longValue()==objetivoTO.getObjetivoinstitucionid().longValue()) {
								grabar=false;
								break;
							}
						}
					}
					if(!grabar){
						mensajes.setMsg("Ya existe un objetivo creado para esta institucion");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
					else{
						//Si va a inactivar valido que no hayan hijos
						if(objetivoTO.getId()!=null && objetivoTO.getId().longValue()!=0 && objetivoTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
							ObjetivoTO hijo=new ObjetivoTO();
							hijo.setObjetivopadreid(objetivoTO.getId());
							hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
							Collection<ObjetivoTO> objetivoTOs2=UtilSession.planificacionServicio.transObtenerObjetivoArbol(hijo);
							if(objetivoTOs2.size()>0) {
								grabar=false;
								mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
								mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
							}
						}
						if(grabar) {
							UtilSession.planificacionServicio.transCrearModificarObjetivo(objetivoTO);
							id=objetivoTO.getNpid().toString();
							jsonObject.put("objetivo", (JSONObject)JSONSerializer.toJSON(objetivoTO,objetivoTO.getJsonConfig()));
						}
					}
				}
			}
			//Plan nacional
			else if(clase.equals("plannacional")){
				PlannacionalTO plannacionalTO = gson.fromJson(new StringReader(objeto), PlannacionalTO.class);
				log.println("padre id: " + plannacionalTO.getPlannacionalpadreid());
				log.println("descripcion: " + plannacionalTO.getDescripcion());
				accion = (plannacionalTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				PlannacionalTO plannacionalTO2=new PlannacionalTO();
				plannacionalTO2.setPlannacionalejerfiscalid(plannacionalTO.getPlannacionalejerfiscalid());
				plannacionalTO2.setCodigo(plannacionalTO.getCodigo());
				plannacionalTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				//plannacionalTO2.setTipo(plannacionalTO.getTipo());
				if(plannacionalTO.getPlannacionalpadreid()!=null && plannacionalTO.getPlannacionalpadreid().longValue()!=0)
					plannacionalTO2.setPlannacionalpadreid(plannacionalTO.getPlannacionalpadreid());
				//Collection<PlannacionalTO> plannacionalTOs=UtilSession.planificacionServicio.transObtenerPlannacionalArbol(plannacionalTO2);
				Collection<PlannacionalTO> plannacionalTOs=UtilSession.planificacionServicio.transObtenerPlannacionalArbol(plannacionalTO2);
				log.println("plannacionalTOs: " + plannacionalTOs.size());
				boolean grabar=true;
				if(plannacionalTOs.size()>0){
					for(PlannacionalTO plannacionalTO3:plannacionalTOs) {
						log.println("id: " + plannacionalTO.getId() + " - " + plannacionalTO3.getId());
						log.println("codigo: " + plannacionalTO.getCodigo() + " - " + plannacionalTO3.getCodigo());
						if((plannacionalTO.getId()!=null && plannacionalTO.getId().longValue()!=0) && plannacionalTO3.getId().longValue()!=plannacionalTO.getId().longValue() && plannacionalTO3.getCodigo().equals(plannacionalTO.getCodigo())) {
							log.println("entro por 1: ");
							grabar=false;
							break;
						}
						else if((plannacionalTO.getId()==null || (plannacionalTO.getId()!=null && plannacionalTO3.getId().longValue()!=plannacionalTO.getId().longValue())) && plannacionalTO.getCodigo()!=null && plannacionalTO3.getCodigo().equals(plannacionalTO.getCodigo())) {
							log.println("entro por 2:");
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
//					if(plannacionalTO.getId()!=null && plannacionalTO.getId().longValue()!=0 && plannacionalTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
//						ObjetivoTO objetivoTO=new ObjetivoTO();
//						objetivoTO.setCodigo(plannacionalTO.getCodigo());
//						objetivoTO.setEstado(MensajesAplicacion.getString("estado.activo"));
//						objetivoTO.setObjetivoejerciciofiscalid(plannacionalTO.getPlannacionalejerfiscalid());
//						objetivoTO.setTipo(MensajesAplicacion.getString("planinstitucional.tipo.plannacional"));
//						log.println("datos: " + plannacionalTO.getCodigo() + "-"+MensajesAplicacion.getString("estado.activo")+"-"+plannacionalTO.getPlannacionalejerfiscalid()+"-"+MensajesAplicacion.getString("planinstitucional.tipo.plannacional"));
//						Collection<ObjetivoTO> objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO);
//						log.println("objetivos: " + objetivoTOs.size());
//						if(objetivoTOs.size()>0) {
//							grabar=false;
//							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe.plannacional"));
//							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//						}
//					}
//					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarPlannacional(plannacionalTO);
						id=plannacionalTO.getNpid().toString();
						jsonObject.put("plannacional", (JSONObject)JSONSerializer.toJSON(plannacionalTO,plannacionalTO.getJsonConfig()));
//					}
				}
			}
			//Indicador
			else if(clase.equals("indicador")){
				IndicadorTO indicadorTO = gson.fromJson(new StringReader(objeto), IndicadorTO.class);
				log.println("descripcion " + indicadorTO.getDescripcion());
				accion = (indicadorTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				IndicadorTO indicadorTO2=new IndicadorTO();
				indicadorTO2.setIndicadorejerciciofiscalid(indicadorTO.getIndicadorejerciciofiscalid());
				indicadorTO2.setCodigo(indicadorTO.getCodigo());
				indicadorTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				indicadorTO2.setIndicadorpadreid(indicadorTO.getIndicadorpadreid());
				Collection<IndicadorTO> indicadorTOs=UtilSession.planificacionServicio.transObtenerIndicadorArbol(indicadorTO2);
				boolean grabar=true;
				if(indicadorTOs.size()>0){
					for(IndicadorTO indicadorTO3:indicadorTOs) {
						if((indicadorTO.getId()!=null && indicadorTO.getId().longValue()!=0) && indicadorTO3.getId().longValue()!=indicadorTO.getId().longValue() && indicadorTO3.getCodigo().equals(indicadorTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((indicadorTO.getId()==null || (indicadorTO.getId()!=null && indicadorTO3.getId().longValue()!=indicadorTO.getId().longValue())) && indicadorTO.getCodigo()!=null && indicadorTO3.getCodigo().equals(indicadorTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
//					indicadorTO2=(IndicadorTO)plannacionalTOs.iterator().next();
//					if(indicadorTO.getId()!=null && indicadorTO2.getId().longValue()!=indicadorTO.getId().longValue())
//						grabar=false;
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(indicadorTO.getId()!=null && indicadorTO.getId().longValue()!=0 && indicadorTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						IndicadorTO hijo=new IndicadorTO();
						hijo.setId(indicadorTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<IndicadorTO> indicadorTOs2=UtilSession.planificacionServicio.transObtenerIndicadorArbol(hijo);
						if(indicadorTOs2.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarIndicador(indicadorTO);
						id=indicadorTO.getNpid().toString();
						jsonObject.put("indicador", (JSONObject)JSONSerializer.toJSON(indicadorTO,indicadorTO.getJsonConfig()));
					}
				}
			}
			//Programa
			else if(clase.equals("programa")){
				ProgramaTO programaTO = gson.fromJson(new StringReader(objeto), ProgramaTO.class);
				accion = (programaTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual y combinado con el objetivo fuerza
				ProgramaTO programaTO2=new ProgramaTO();
				programaTO2.setProgramaejerciciofiscalid(programaTO.getProgramaejerciciofiscalid());
				programaTO2.setCodigo(programaTO.getCodigo());
				programaTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				programaTO2.setProgramaobjetivofuersasid(programaTO.getProgramaobjetivofuersasid());
				Collection<ProgramaTO> plannacionalTOs=UtilSession.planificacionServicio.transObtenerPrograma(programaTO2);
				log.println("encontro el codigo: " + plannacionalTOs.size());
				boolean grabar=true;
				if(plannacionalTOs.size()>0){
					for(ProgramaTO programaTO3:plannacionalTOs) {
						if((programaTO.getId()!=null && programaTO.getId().longValue()!=0) && programaTO3.getId().longValue()!=programaTO.getId().longValue() && programaTO3.getProgramaobjetivofuersasid().longValue()==programaTO.getProgramaobjetivofuersasid().longValue()) {
							grabar=false;
							break;
						}
						else if((programaTO.getId()==null || (programaTO.getId()!=null && programaTO3.getId().longValue()!=programaTO.getId().longValue()))  && programaTO3.getProgramaobjetivofuersasid().longValue()==programaTO.getProgramaobjetivofuersasid().longValue()) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("Ya existe programa en la fuerza seleccionada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(programaTO.getId()!=null && programaTO.getId().longValue()!=0 && programaTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
						nivelprogramaTO.setTablarelacionid(programaTO.getId());
						nivelprogramaTO.setNivelprogramaejerfiscalid(programaTO.getProgramaejerciciofiscalid());
						nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.programa"));
						nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
						log.println("nivel programa: " + nivelprogramaTO.getId());
						NivelprogramaTO hijo=new NivelprogramaTO();
						hijo.setId(nivelprogramaTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<NivelprogramaTO> nivelprogramaTOs=UtilSession.planificacionServicio.transObtenerNivelprogramaArbol(hijo);
						log.println("hijos encontrados: " + nivelprogramaTOs.size());
						if(nivelprogramaTOs.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						log.println("va a grabar: " + programaTO.getId());
						UtilSession.planificacionServicio.transCrearModificarPrograma(programaTO);
						id=programaTO.getNpid().toString();
						jsonObject.put("programa", (JSONObject)JSONSerializer.toJSON(programaTO,programaTO.getJsonConfig()));
					}
				}
			}
			//Subprograma
			else if(clase.equals("subprograma")){
				SubprogramaTO subprogramaTO = gson.fromJson(new StringReader(objeto), SubprogramaTO.class);
				accion = (subprogramaTO.getId()==null)?"crear":"actualizar";
				log.println("programa: " + subprogramaTO.getPadre());
				//pregunto si ya existe el codigo en el nivel actual
				SubprogramaTO subprogramaTO2=new SubprogramaTO();
				subprogramaTO2.setSubprogramaejerciciofiscalid(subprogramaTO.getSubprogramaejerciciofiscalid());
				subprogramaTO2.setCodigo(subprogramaTO.getCodigo());
				subprogramaTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				subprogramaTO2.setPadre(subprogramaTO.getPadre());
				Collection<SubprogramaTO> subprogramaTOs=UtilSession.planificacionServicio.transObtenerSubprograma(subprogramaTO);
				//Collection<SubprogramaTO> subprogramaTOs=UtilSession.planificacionServicio.transObtenerSubprograma(subprogramaTO2);
				log.println("subprogramaTOs: " + subprogramaTOs.size());
				boolean grabar=true;
				if(subprogramaTOs.size()>0){
					for(SubprogramaTO subprogramaTO3:subprogramaTOs) {
						if((subprogramaTO.getId()!=null && subprogramaTO.getId().longValue()!=0) && subprogramaTO3.getId().longValue()!=subprogramaTO.getId().longValue() && subprogramaTO3.getCodigo().equals(subprogramaTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((subprogramaTO.getId()==null || (subprogramaTO.getId()!=null && subprogramaTO3.getId().longValue()!=subprogramaTO.getId().longValue())) && subprogramaTO.getCodigo()!=null && subprogramaTO3.getCodigo().equals(subprogramaTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(subprogramaTO.getId()!=null && subprogramaTO.getId().longValue()!=0 && subprogramaTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
						nivelprogramaTO.setTablarelacionid(subprogramaTO.getId());
						nivelprogramaTO.setNivelprogramaejerfiscalid(subprogramaTO.getSubprogramaejerciciofiscalid());
						nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.subprograma"));
						nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
						NivelprogramaTO hijo=new NivelprogramaTO();
						hijo.setId(nivelprogramaTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<NivelprogramaTO> nivelprogramaTOs=UtilSession.planificacionServicio.transObtenerNivelprogramaArbol(hijo);
						if(nivelprogramaTOs.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarSubprograma(subprogramaTO);
						id=subprogramaTO.getNpid().toString();
						jsonObject.put("subprograma", (JSONObject)JSONSerializer.toJSON(subprogramaTO,subprogramaTO.getJsonConfig()));
					}
				}
			}
			
			//Proyecto
			else if(clase.equals("proyecto")){
				ProyectoTO proyectoTO = gson.fromJson(new StringReader(objeto), ProyectoTO.class);
				log.println("metas " + proyectoTO.getProyectometaTOs().size());
				accion = (proyectoTO.getId()==null)?"crear":"actualizar";
				if(proyectoTO.getNpFechainicio()!=null)
					proyectoTO.setFechainicio(UtilGeneral.parseStringToDate(proyectoTO.getNpFechainicio()));
				if(proyectoTO.getNpFechafin()!=null){
					log.println("fecha fin np: " + proyectoTO.getNpFechafin());
					proyectoTO.setFechafin(UtilGeneral.parseStringToDate(proyectoTO.getNpFechafin()));
				}
				log.println("fecha fin: " + proyectoTO.getFechafin());
				log.println("padre: " + proyectoTO.getPadre());
				//pregunto si ya existe el codigo en el nivel actual
				ProyectoTO proyectoTO2=new ProyectoTO();
				proyectoTO2.setProyectoejerciciofiscalid(proyectoTO.getProyectoejerciciofiscalid());
				proyectoTO2.setCodigo(proyectoTO.getCodigo());
				proyectoTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				proyectoTO2.setPadre(proyectoTO.getPadre());
				Collection<ProyectoTO> proyectoTOs=UtilSession.planificacionServicio.transObtenerProyecto(proyectoTO2);
				log.println("proyectoTOs: " + proyectoTOs.size());
				boolean grabar=true;
				if(proyectoTOs.size()>0){
					for(ProyectoTO proyectoTO3:proyectoTOs) {
						if((proyectoTO.getId()!=null && proyectoTO.getId().longValue()!=0) && proyectoTO3.getId().longValue()!=proyectoTO.getId().longValue() && proyectoTO3.getCodigo().equals(proyectoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((proyectoTO.getId()==null || (proyectoTO.getId()!=null && proyectoTO3.getId().longValue()!=proyectoTO.getId().longValue())) && proyectoTO.getCodigo()!=null && proyectoTO3.getCodigo().equals(proyectoTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(proyectoTO.getId()!=null && proyectoTO.getId().longValue()!=0 && proyectoTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
						nivelprogramaTO.setTablarelacionid(proyectoTO.getId());
						nivelprogramaTO.setNivelprogramaejerfiscalid(proyectoTO.getProyectoejerciciofiscalid());
						nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.proyecto"));
						nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
						NivelprogramaTO hijo=new NivelprogramaTO();
						hijo.setId(nivelprogramaTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<NivelprogramaTO> nivelprogramaTOs=UtilSession.planificacionServicio.transObtenerNivelprogramaArbol(hijo);
						if(nivelprogramaTOs.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarProyecto(proyectoTO);
						id=proyectoTO.getNpid().toString();
						jsonObject.put("proyecto", (JSONObject)JSONSerializer.toJSON(proyectoTO,proyectoTO.getJsonConfig()));
					}
				}
			}
			
			//Actividad
			else if(clase.equals("actividad")){
				ActividadTO actividadTO = gson.fromJson(new StringReader(objeto), ActividadTO.class);
				accion = (actividadTO.getId()==null)?"crear":"actualizar";
				log.println("va a grabar actividad");
				//pregunto si ya existe el codigo en el nivel actual
				ActividadTO actividadTO2=new ActividadTO();
				actividadTO2.setActividadeejerciciofiscalid(actividadTO.getActividadeejerciciofiscalid());
				actividadTO2.setCodigo(actividadTO.getCodigo());
				actividadTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				actividadTO2.setPadre(actividadTO.getPadre());
				Collection<ActividadTO> actividadTOs=UtilSession.planificacionServicio.transObtenerActividad(actividadTO2);						
				log.println("actividadTOs: " + actividadTOs.size());
				boolean grabar=true;
				if(actividadTOs.size()>0){
					for(ActividadTO actividadTO3:actividadTOs) {
						if((actividadTO.getId()!=null && actividadTO.getId().longValue()!=0) && actividadTO3.getId().longValue()!=actividadTO.getId().longValue() && actividadTO3.getCodigo().equals(actividadTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((actividadTO.getId()==null || (actividadTO.getId()!=null && actividadTO3.getId().longValue()!=actividadTO.getId().longValue())) && actividadTO.getCodigo()!=null && actividadTO3.getCodigo().equals(actividadTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(actividadTO.getId()!=null && actividadTO.getId().longValue()!=0 && actividadTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
						nivelprogramaTO.setTablarelacionid(actividadTO.getId());
						nivelprogramaTO.setNivelprogramaejerfiscalid(actividadTO.getActividadeejerciciofiscalid());
						nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.actividad"));
						nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
						NivelprogramaTO hijo=new NivelprogramaTO();
						hijo.setId(nivelprogramaTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<NivelprogramaTO> nivelprogramaTOs=UtilSession.planificacionServicio.transObtenerNivelprogramaArbol(hijo);
						if(nivelprogramaTOs.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarActividad(actividadTO);
						id=actividadTO.getNpid().toString();
						jsonObject.put("actividad", (JSONObject)JSONSerializer.toJSON(actividadTO,actividadTO.getJsonConfig()));
					}
				}
			}
			
			//Subactividad
			else if(clase.equals("subactividad")){
				SubactividadTO subactividadTO = gson.fromJson(new StringReader(objeto), SubactividadTO.class);
				accion = (subactividadTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				SubactividadTO subactividadTO2=new SubactividadTO();
				subactividadTO2.setSubactividadejerfiscalid(subactividadTO.getSubactividadejerfiscalid());
				subactividadTO2.setCodigo(subactividadTO.getCodigo());
				subactividadTO2.setEstado(MensajesAplicacion.getString("estado.activo"));
				subactividadTO2.setPadre(subactividadTO.getPadre());
				Collection<SubactividadTO> subactividadTOs=UtilSession.planificacionServicio.transObtenerSubactividad(subactividadTO2);						
				log.println("subactividadTOs: " + subactividadTOs.size());
				boolean grabar=true;
				if(subactividadTOs.size()>0){
					for(SubactividadTO plannacionalTO3:subactividadTOs) {
						if((subactividadTO.getId()!=null && subactividadTO.getId().longValue()!=0) && plannacionalTO3.getId().longValue()!=subactividadTO.getId().longValue() && plannacionalTO3.getCodigo().equals(subactividadTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((subactividadTO.getId()==null || (subactividadTO.getId()!=null && plannacionalTO3.getId().longValue()!=subactividadTO.getId().longValue())) && subactividadTO.getCodigo()!=null && plannacionalTO3.getCodigo().equals(subactividadTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//Si va a inactivar valido que no hayan hijos
					if(subactividadTO.getId()!=null && subactividadTO.getId().longValue()!=0 && subactividadTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
						NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
						nivelprogramaTO.setTablarelacionid(subactividadTO.getId());
						nivelprogramaTO.setNivelprogramaejerfiscalid(subactividadTO.getSubactividadejerfiscalid());
						nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.subactividad"));
						nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
						NivelprogramaTO hijo=new NivelprogramaTO();
						hijo.setId(nivelprogramaTO.getId());
						hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
						Collection<NivelprogramaTO> nivelprogramaTOs=UtilSession.planificacionServicio.transObtenerNivelprogramaArbol(hijo);
						if(nivelprogramaTOs.size()>0) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						}
					}
					if(grabar) {
						UtilSession.planificacionServicio.transCrearModificarSubactividad(subactividadTO);
						id=subactividadTO.getNpid().toString();
						jsonObject.put("subactividad", (JSONObject)JSONSerializer.toJSON(subactividadTO,subactividadTO.getJsonConfig()));
					}
				}
			}
			//Actividad unidad (Planificacion anual - modificar actividad)
			else if(clase.equals("actividadunidad")){
				ActividadunidadTO actividadunidadTO = gson.fromJson(new StringReader(objeto), ActividadunidadTO.class);
				accion = (actividadunidadTO.getId()==null)?"crear":"actualizar";
				actividadunidadTO.setFechainicio(UtilGeneral.parseStringToDate(actividadunidadTO.getNpFechainicio()));
				log.println("npfechafin: " + actividadunidadTO.getNpFechafin());
				actividadunidadTO.setFechafin(UtilGeneral.parseStringToDate(actividadunidadTO.getNpFechafin()));
				log.println("fecha fin: " + actividadunidadTO.getFechafin());
				UtilSession.planificacionServicio.transCrearModificarActividadunidad(actividadunidadTO);
				id=actividadunidadTO.getId().toString();
				jsonObject.put("actividadunidad", (JSONObject)JSONSerializer.toJSON(actividadunidadTO,actividadunidadTO.getJsonConfig()));
			}

			//Subactividad unidad (Planificacion anual - modificar subactividad)
			else if(clase.equals("subactividadunidad")){
				SubactividadunidadTO subactividadunidadTO = gson.fromJson(new StringReader(objeto), SubactividadunidadTO.class);
				accion = (subactividadunidadTO.getId()==null)?"crear":"actualizar";
				//verifico que no se pase del 100% de la ponderacion
				Double ponderacion=UtilSession.planificacionServicio.transObtienePoneracionSubactividades(subactividadunidadTO.getPadre(),subactividadunidadTO.getId().getUnidadid());
				//Si la ponderacion guardada mas la ingresada suma menos o igual a 100 la graba
				if((ponderacion.doubleValue()+subactividadunidadTO.getPonderacion().doubleValue()-subactividadunidadTO.getNpponderacion())<=100){
					UtilSession.planificacionServicio.transCrearModificarSubactividadunidad(subactividadunidadTO);
					id=subactividadunidadTO.getId().toString();
					jsonObject.put("subactividadunidad", (JSONObject)JSONSerializer.toJSON(subactividadunidadTO,subactividadunidadTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.ponderacion"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}
			
			//Tarea unidad (Planificacion anual - modificar subactividad)
			else if(clase.equals("tareaunidad")){
				TareaunidadTO tareaunidadTO = gson.fromJson(new StringReader(objeto), TareaunidadTO.class);
				accion = (tareaunidadTO.getId()==null)?"crear":"actualizar";
				//pregunto si ya existe el codigo en el nivel actual
				boolean grabar=true;
				if(tareaunidadTO.getCodigo()!=null && !tareaunidadTO.getCodigo().equals("") && !tareaunidadTO.getCodigo().equals(" ")) {
					NivelactividadTO nivelactividadTO=new  NivelactividadTO();
					nivelactividadTO.setEstado(MensajesAplicacion.getString("estado.activo"));
					nivelactividadTO.setNivelactividadpadreid(tareaunidadTO.getPadre());
					nivelactividadTO.setTipo("TA");
					nivelactividadTO.setNivelactividadunidadid(tareaunidadTO.getTareaunidadmetaumid());
					nivelactividadTO.setNivelactividadejerfiscalid(tareaunidadTO.getTareaunidadejerciciofiscalid());
					Collection<NivelactividadTO> resultado=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO);
					log.println("tareaunidadTOs: " + resultado.size());
					if(resultado.size()>0){
						for(NivelactividadTO nivelactividadTO2:resultado) {
							if((tareaunidadTO.getId()!=null && tareaunidadTO.getId().longValue()!=0) && nivelactividadTO2.getId().longValue()!=tareaunidadTO.getId().longValue() && nivelactividadTO2.getNpcodigo().equals(tareaunidadTO.getCodigo())) {
								grabar=false;
								break;
							}
							else if((tareaunidadTO.getId()==null || (tareaunidadTO.getId()!=null && nivelactividadTO2.getId().longValue()!=tareaunidadTO.getId().longValue())) && tareaunidadTO.getCodigo()!=null && nivelactividadTO2.getNpcodigo().equals(tareaunidadTO.getCodigo())) {
								grabar=false;
								break;
							}
						}
	
					}
					if(!grabar){
						mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
				if(grabar) {
					//verifico que no se pase del 100% de la ponderacion
					log.println("padre: " + tareaunidadTO.getPadre());
					log.println("unidad: " + tareaunidadTO.getTareaunidadunidadid());
					Double ponderacion=UtilSession.planificacionServicio.transObtienePoneracionTareas(tareaunidadTO.getPadre(),tareaunidadTO.getTareaunidadunidadid());
					log.println("ponderacion: " + ponderacion);
					if(ponderacion==null)
						ponderacion=0.0;
					//Si la ponderacion guardada mas la ingresada suma menos o igual a 100 la graba
					if((ponderacion.doubleValue()+tareaunidadTO.getPonderacion().doubleValue()-tareaunidadTO.getNpponderacion())<=100){
						//Si va a inactivar valido que no hayan hijos
						if(tareaunidadTO.getId()!=null && tareaunidadTO.getId().longValue()!=0 && tareaunidadTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
							NivelactividadTO nivelactividadTO=new NivelactividadTO();
							nivelactividadTO.setTablarelacionid(tareaunidadTO.getId());
							nivelactividadTO.setNivelactividadejerfiscalid(tareaunidadTO.getTareaunidadejerciciofiscalid());
							nivelactividadTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.tarea"));
							nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(nivelactividadTO);
							NivelactividadTO hijo=new NivelactividadTO();
							hijo.setId(nivelactividadTO.getId());
							hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
							Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtenerNivelactividadArbol(hijo);
							if(nivelactividadTOs.size()>0) {
								mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
								mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
								grabar=false;
							}
						}
						if(grabar) {
							UtilSession.planificacionServicio.transCrearModificarTareaunidad(tareaunidadTO);
							id=tareaunidadTO.getNpid().toString();
							jsonObject.put("tareaunidad", (JSONObject)JSONSerializer.toJSON(tareaunidadTO,tareaunidadTO.getJsonConfig()));
						}

					}
					else{
						mensajes.setMsg(MensajesWeb.getString("advertencia.ponderacion"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
			}
			
			//Subtarea unidad (Planificacion anual - modificar actividad)
			else if(clase.equals("subtareaunidad")){
				SubtareaunidadTO subtareaunidadTO = gson.fromJson(new StringReader(objeto), SubtareaunidadTO.class);
				accion = (subtareaunidadTO.getId()==null)?"crear":"actualizar";
				boolean grabar=true;
				if(subtareaunidadTO.getCodigo()!=null && !subtareaunidadTO.getCodigo().equals("") && !subtareaunidadTO.getCodigo().equals(" ")) {
					//pregunto si ya existe el codigo en el nivel actual
					log.println("va a verificar codigo");
					NivelactividadTO nivelactividadTO=new  NivelactividadTO();
					nivelactividadTO.setEstado(MensajesAplicacion.getString("estado.activo"));
					nivelactividadTO.setNivelactividadpadreid(subtareaunidadTO.getPadre());
					nivelactividadTO.setTipo("SA");
					nivelactividadTO.setNivelactividadunidadid(subtareaunidadTO.getSubtareaunidadunidadid());
					nivelactividadTO.setNivelactividadejerfiscalid(subtareaunidadTO.getSubtareaunidadejerfiscalid());
					Collection<NivelactividadTO> resultado=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO);
					log.println("resultado: " + resultado.size());
					if(resultado.size()>0){
						for(NivelactividadTO nivelactividadTO2:resultado) {
							if((subtareaunidadTO.getId()!=null && subtareaunidadTO.getId().longValue()!=0) && nivelactividadTO2.getId().longValue()!=subtareaunidadTO.getId().longValue() && nivelactividadTO2.getNpcodigo().equals(subtareaunidadTO.getCodigo())) {
								grabar=false;
								break;
							}
							else if((subtareaunidadTO.getId()==null || (subtareaunidadTO.getId()!=null && nivelactividadTO2.getId().longValue()!=subtareaunidadTO.getId().longValue())) && subtareaunidadTO.getCodigo()!=null && nivelactividadTO2.getNpcodigo().equals(subtareaunidadTO.getCodigo())) {
								grabar=false;
								break;
							}
						}
	
					}
				}
				if(grabar) {
					//verifico que no se pase del 100% de la ponderacion
					Double ponderacion=UtilSession.planificacionServicio.transObtienePoneracionSubtarea(subtareaunidadTO.getPadre(),subtareaunidadTO.getSubtareaunidadunidadid());
					if(ponderacion==null)
						ponderacion=0.0;
					//Si la ponderacion guardada mas la ingresada suma menos o igual a 100 la graba
					if((ponderacion.doubleValue()+subtareaunidadTO.getPonderacion().doubleValue()-subtareaunidadTO.getNpponderacion())<=100){
						//Si va a inactivar valido que no hayan hijos
						if(subtareaunidadTO.getId()!=null && subtareaunidadTO.getId().longValue()!=0 && subtareaunidadTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
							NivelactividadTO nivelactividadTO=new NivelactividadTO();
							nivelactividadTO.setTablarelacionid(subtareaunidadTO.getId());
							nivelactividadTO.setNivelactividadejerfiscalid(subtareaunidadTO.getSubtareaunidadejerfiscalid());
							nivelactividadTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.subtarea"));
							nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(nivelactividadTO);
							log.println("nivel actividad: " + nivelactividadTO.getId());
							NivelactividadTO hijo=new NivelactividadTO();
							hijo.setId(nivelactividadTO.getId());
							hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
							Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtenerNivelactividadArbol(hijo);
							log.println("hijos existentes "+ nivelactividadTOs.size());
							if(nivelactividadTOs.size()>0) {
								mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
								mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
								grabar=false;
							}
						}
						if(grabar) {
							log.println("va a grabar");
							UtilSession.planificacionServicio.transCrearModificarSubtareaunidad(subtareaunidadTO);
							log.println("subtarea id: " + subtareaunidadTO.getId());
							subtareaunidadTO.setId(subtareaunidadTO.getNpid());
							id=subtareaunidadTO.getNpid().toString();
							SubtareaunidadTO subtareaunidadTO1 = UtilSession.planificacionServicio.transObtenerSubtareaunidadTO(subtareaunidadTO.getNpid());
							subtareaunidadTO1.setEstado(MensajesWeb.getString("estado.activo"));
							subtareaunidadTO1.setPadre(subtareaunidadTO.getPadre());
							subtareaunidadTO1.setNpponderacion(subtareaunidadTO.getPonderacion());
							jsonObject.put("subtareaunidad", (JSONObject)JSONSerializer.toJSON(subtareaunidadTO1,subtareaunidadTO1.getJsonConfigeditar()));
							//traigo los datos de subtareaunidadacumulador
							SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
							subtareaunidadacumuladorTO.getId().setId(subtareaunidadTO.getId());
							Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
							for(SubtareaunidadacumuladorTO subtareaunidadacumuladorTO2:subtareaunidadacumuladorTOs)
								subtareaunidadacumuladorTO2.setNpValor(subtareaunidadacumuladorTO2.getCantidad());
							jsonObject.put("subtareaunidadacumulador", (JSONArray)JSONSerializer.toJSON(subtareaunidadacumuladorTOs,subtareaunidadacumuladorTO.getJsonConfig()));
						}

					}
					else{
						mensajes.setMsg(MensajesWeb.getString("advertencia.ponderacion"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
			}
			
			//Item (Planificacion anual - modificar subactividad)
			else if(clase.equals("itemunidad")){
				ItemunidadTO itemunidadTO = gson.fromJson(new StringReader(objeto), ItemunidadTO.class);
				accion = (itemunidadTO.getId()==null)?"crear":"actualizar";
				//Si va a inactivar valido que no hayan hijos
				boolean grabar=true;
				if(itemunidadTO.getId()!=null && itemunidadTO.getId().longValue()!=0 && itemunidadTO.getEstado().equals(MensajesAplicacion.getString("estado.inactivo"))) {
					NivelactividadTO nivelactividadTO=new NivelactividadTO();
					nivelactividadTO.setTablarelacionid(itemunidadTO.getId());
					nivelactividadTO.setNivelactividadejerfiscalid(itemunidadTO.getItemunidadejerciciofiscalid());
					nivelactividadTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.item"));
					nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(nivelactividadTO);
					NivelactividadTO hijo=new NivelactividadTO();
					hijo.setId(nivelactividadTO.getId());
					hijo.setEstado(MensajesAplicacion.getString("estado.activo"));
					Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtenerNivelactividadArbol(hijo);
					if(nivelactividadTOs.size()>0) {
						mensajes.setMsg(MensajesWeb.getString("error.hijo.existe"));
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						grabar=false;
					}
				}
				if(grabar) {
					UtilSession.planificacionServicio.transCrearModificarItemunidad(itemunidadTO);
					id=itemunidadTO.getNpid().toString();
					jsonObject.put("itemunidad", (JSONObject)JSONSerializer.toJSON(itemunidadTO,itemunidadTO.getJsonConfig()));
				}
			}

			//Subitem (Planificacion anual - modificar subitem)
			else if(clase.equals("subitemunidad")){
				SubitemunidadTO subitemunidadTO = gson.fromJson(new StringReader(objeto), SubitemunidadTO.class);
				log.println("detalle**: " + subitemunidadTO.getSubitemunidadejerfiscalid()+"--"+ subitemunidadTO.getNpcodigosubitem()+" - " +subitemunidadTO.getNpnombresubitem());
				accion = (subitemunidadTO.getId()==null)?"crear":"actualizar";
				//Verifico que no exista ya creado otro subitem unidad del mismo subitem en este nivel
				NivelactividadTO nivelactividadTO=new NivelactividadTO();
				nivelactividadTO.setNivelactividadejerfiscalid(subitemunidadTO.getSubitemunidadejerfiscalid());
				nivelactividadTO.setNivelactividadpadreid(subitemunidadTO.getPadre());
				log.println("eje: "+ subitemunidadTO.getSubitemunidadejerfiscalid()+" padre " + subitemunidadTO.getPadre());
				Collection<NivelactividadTO> resultado=UtilSession.planificacionServicio.transObtenerNivelactividad(nivelactividadTO);
				log.println("niveles: " + resultado.size());
				boolean existesubiten=false;
				for(NivelactividadTO nivelactividadTO2:resultado){
					log.println("descripcion " + nivelactividadTO2.getDescripcionexten());
					if(nivelactividadTO2.getDescripcionexten()!=null) {
						String [] descripcion=nivelactividadTO2.getDescripcionexten().split("-");
						if((subitemunidadTO.getId()==null || subitemunidadTO.getId().longValue()==0) && (descripcion[0].equals(subitemunidadTO.getNpcodigosubitem()) && descripcion[1].equals(subitemunidadTO.getNpnombresubitem()))){
							existesubiten=true;
							break;
						}
						else if((subitemunidadTO.getId()!=null && subitemunidadTO.getId().longValue()!=0) 
								&& (subitemunidadTO.getId().longValue()!=nivelactividadTO2.getTablarelacionid().longValue())
								&& (descripcion[0].equals(subitemunidadTO.getNpcodigosubitem()) && descripcion[1].equals(subitemunidadTO.getNpnombresubitem()))){
							existesubiten=true;
							break;
						}
					}
				}
				if(!existesubiten){
					log.println("detalle: " + subitemunidadTO.getNpcodigosubitem()+" - " +subitemunidadTO.getNpnombresubitem());
					UtilSession.planificacionServicio.transCrearModificarSubitemunidad(subitemunidadTO);
					id=subitemunidadTO.getNpid().toString();
					subitemunidadTO.setId(subitemunidadTO.getNpid());
					jsonObject.put("subitemunidad", (JSONObject)JSONSerializer.toJSON(subitemunidadTO,subitemunidadTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.crearsubitem"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}
			
			//Cronograma (Planificacion anual - metas para actividad unidad, subtareaunidad y subitemunidad)
			else if(clase.equals("cronograma")){
				CronogramaTO cronogramaTO = gson.fromJson(new StringReader(objeto), CronogramaTO.class);
				accion = (cronogramaTO.getId()==null)?"crear":"actualizar";
				UtilSession.planificacionServicio.transCrearModificarCronograma(cronogramaTO);
				id=cronogramaTO.getNpid().toString();
				//jsonObject.put("cronograma", (JSONObject)JSONSerializer.toJSON(subitemunidadTO,subitemunidadTO.getJsonConfig()));
			}
			
			//observacion matriz presupuesto (Planificacion anual - matriz presupuesto)
			else if(clase.equals("matrizpresupuesto")){
				Type matriz = new TypeToken<List<MatrizDetalle>>(){}.getType();
		    	Collection<MatrizDetalle> matrizDetalles = gson.fromJson(new StringReader(objeto), matriz);
				//MatrizDetalle matrizDetalle = gson.fromJson(new StringReader(objeto), MatrizDetalle.class);
				accion = "actualizar";
				UtilSession.planificacionServicio.transModificarMatrizpresupuesto(matrizDetalles);
			}


			//observacion matriz metas (Planificacion anual - matriz metas)
			else if(clase.equals("matrizmetas")){
				Type matriz = new TypeToken<List<MatrizDetalle>>(){}.getType();
		    	Collection<MatrizDetalle> matrizDetalles = gson.fromJson(new StringReader(objeto), matriz);
		    	log.println("detalles: " + matrizDetalles.size());
				//MatrizDetalle matrizDetalle = gson.fromJson(new StringReader(objeto), MatrizDetalle.class);
				accion = "actualizar";
				UtilSession.planificacionServicio.transModificarMatrizmetas(matrizDetalles);
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
		log.println("resultado: " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}
	
	@RequestMapping(value = "/nuevo/{clase}/{id}/{ejercicio}/{tipo}", method = RequestMethod.GET)
	public Respuesta nuevo(@PathVariable String clase,@PathVariable Long id,@PathVariable Long ejercicio,@PathVariable String tipo,HttpServletRequest request){
		log.println("entra al metodo nuevo: " + clase + " - " + id +  " - "+ ejercicio +  " -  " + tipo);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Objetivo
			if(clase.equals("objetivo")){
				ObjetivoTO objetivoTO = new ObjetivoTO();
				objetivoTO.setObjetivopadreid(id);
				objetivoTO.setObjetivoejerciciofiscalid(ejercicio);
				objetivoTO.setEstado(MensajesWeb.getString("estado.activo"));
				if(tipo.equals(MensajesAplicacion.getString("planinstitucional.tipo.plannacional")))
					objetivoTO.setTipo(MensajesAplicacion.getString("planinstitucional.tipo.estrategico"));
				else if(tipo.equals(MensajesAplicacion.getString("planinstitucional.tipo.estrategico")))
					objetivoTO.setTipo(MensajesAplicacion.getString("planinstitucional.tipo.operativo")); 
				else if(tipo.equals(MensajesAplicacion.getString("planinstitucional.tipo.operativo")))
					objetivoTO.setTipo(MensajesAplicacion.getString("planinstitucional.tipo.fuerzas"));	
				else
					objetivoTO.setTipo(MensajesAplicacion.getString("planinstitucional.tipo.plannacional"));
				jsonObject.put("objetivo", (JSONObject)JSONSerializer.toJSON(objetivoTO,objetivoTO.getJsonConfig()));
			}
			//Plan nacional
			else if(clase.equals("plannacional")){
				PlannacionalTO plannacionalTO = new PlannacionalTO();
				plannacionalTO.setPlannacionalpadreid(id);
				plannacionalTO.setPlannacionalejerfiscalid(ejercicio);
				if(tipo.equals(MensajesAplicacion.getString("plannacional.tipo.objetivo")))
					plannacionalTO.setTipo(MensajesAplicacion.getString("plannacional.tipo.politica")); 
				else if(tipo.equals(MensajesAplicacion.getString("plannacional.tipo.politica")))
					plannacionalTO.setTipo(MensajesAplicacion.getString("plannacional.tipo.meta"));	
				else
					plannacionalTO.setTipo(MensajesAplicacion.getString("plannacional.tipo.objetivo"));
				plannacionalTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("plannacional", (JSONObject)JSONSerializer.toJSON(plannacionalTO,plannacionalTO.getJsonConfig()));
			}
			//Programa
			else if(clase.equals("programa")){
				ProgramaTO programaTO = new ProgramaTO();
				programaTO.setProgramaejerciciofiscalid(ejercicio);
				programaTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("programa", (JSONObject)JSONSerializer.toJSON(programaTO,programaTO.getJsonConfig()));
			}
			//Subprograma
			else if(clase.equals("subprograma")){
				SubprogramaTO subprogramaTO = new SubprogramaTO();
				subprogramaTO.setPadre(id);//npNivelid 
				subprogramaTO.setSubprogramaejerciciofiscalid(ejercicio);
				subprogramaTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subprograma", (JSONObject)JSONSerializer.toJSON(subprogramaTO,subprogramaTO.getJsonConfig()));
			}
			//Proyecto
			else if(clase.equals("proyecto")){
				ProyectoTO proyectoTO = new ProyectoTO();
				proyectoTO.setPadre(id);//npNivelid 
				proyectoTO.setProyectoejerciciofiscalid(ejercicio);
				proyectoTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("proyecto", (JSONObject)JSONSerializer.toJSON(proyectoTO,proyectoTO.getJsonConfig()));
			}
			//Actividad
			else if(clase.equals("actividad")){
				ActividadTO actividadTO = new ActividadTO();
				actividadTO.setPadre(id);//npNivelid 
				actividadTO.setActividadeejerciciofiscalid(ejercicio);
				actividadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("actividad", (JSONObject)JSONSerializer.toJSON(actividadTO,actividadTO.getJsonConfigNuevo()));
			}
			//Subactividad
			else if(clase.equals("subactividad")){
				//Me llega el id de la atividad, debo buscar el id correspondiente en la tabla nivelprograma para enviar el padre a la subactividad
				NivelprogramaTO nivelprogramaTO=new NivelprogramaTO();
				nivelprogramaTO.setTablarelacionid(id);// el id de la actividad
				nivelprogramaTO.setNivelprogramaejerfiscalid(ejercicio);
				nivelprogramaTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.actividad"));
				nivelprogramaTO=UtilSession.planificacionServicio.transObtenerNivelprogramaTO(nivelprogramaTO);
				SubactividadTO subactividadTO=new SubactividadTO();
				subactividadTO.setPadre(nivelprogramaTO.getId());
				subactividadTO.setNpActividadid(nivelprogramaTO.getTablarelacionid());
				subactividadTO.setSubactividadejerfiscalid(ejercicio);
				subactividadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subactividad", (JSONObject)JSONSerializer.toJSON(subactividadTO,subactividadTO.getJsonConfig()));
			}
			//Tarea
			else if(clase.equals("tareaunidad")){
				String[] pares = tipo.split("&");
				Map<String, String> parameters = new HashMap<String, String>();
				for(String pare : pares) {
				    String[] nameAndValue = pare.split("=");
				    parameters.put(nameAndValue[0], nameAndValue[1]);
				}
				TareaunidadTO tareaunidadTO = new TareaunidadTO();
				tareaunidadTO.setPadre(id);//id del nivelactividad
				tareaunidadTO.setTareaunidadunidadid(Long.valueOf(parameters.get("unidadid")));
				tareaunidadTO.setTareaunidadejerciciofiscalid(ejercicio);
				tareaunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("tareaunidad", (JSONObject)JSONSerializer.toJSON(tareaunidadTO,tareaunidadTO.getJsonConfig()));
			}

			//Subtarea
			else if(clase.equals("subtareaunidad")){
				//traigo el valor presupuestado y aprobado de la actividad
				String[] pares = tipo.split("&");
				Map<String, String> parameters = new HashMap<String, String>();
				for(String pare : pares) {
				    String[] nameAndValue = pare.split("=");
				    parameters.put(nameAndValue[0], nameAndValue[1]);
				}
				SubtareaunidadTO subtareaunidadTO = new SubtareaunidadTO();
				subtareaunidadTO.setPadre(id);//id del nivelactividad
				subtareaunidadTO.setSubtareaunidadunidadid(Long.valueOf(parameters.get("unidadid")));
				subtareaunidadTO.setSubtareaunidadejerfiscalid(ejercicio);
				subtareaunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subtareaunidad", (JSONObject)JSONSerializer.toJSON(subtareaunidadTO,subtareaunidadTO.getJsonConfig()));
				//obtengo la lista de subtareaunidadacumuladorTO existente para saber que acumulador toca
				SubtareaunidadacumuladorTO subtareaunidadacumuladorExiste=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorExiste.getId().setId(id);
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=new ArrayList<>();
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorExistentes=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorExiste);
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.getId().setAcumid(Long.valueOf(subtareaunidadacumuladorExistentes.size()+1));
				//subtareaunidadacumuladorTO.getId().setId(id);
				subtareaunidadacumuladorTO.setTotal(0.0);
				subtareaunidadacumuladorTO.setNpValor(0.0);
				subtareaunidadacumuladorTO.setValor(100.0);
				subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.planificado"));
				subtareaunidadacumuladorTOs.add(subtareaunidadacumuladorTO);
				
				subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				//subtareaunidadacumuladorTO.getId().setId(id);
				subtareaunidadacumuladorTO.getId().setAcumid(Long.valueOf(subtareaunidadacumuladorExistentes.size()+2));
				subtareaunidadacumuladorTO.setNpValor(0.0);
				subtareaunidadacumuladorTO.setTotal(0.0);
				subtareaunidadacumuladorTO.setValor(100.0);
				subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTOs.add(subtareaunidadacumuladorTO);
				jsonObject.put("subtareaunidadacumulador", (JSONArray)JSONSerializer.toJSON(subtareaunidadacumuladorTOs,subtareaunidadacumuladorTO.getJsonConfig()));
			}
			
			//Item
			else if(clase.equals("itemunidad")){
				//traigo el valor presupuestado y aprobado de la actividad
				String[] pares = tipo.split("&");
				Map<String, String> parameters = new HashMap<String, String>();
				for(String pare : pares) {
				    String[] nameAndValue = pare.split("=");
				    parameters.put(nameAndValue[0], nameAndValue[1]);
				}
				ItemunidadTO itemunidadTO = new ItemunidadTO();
				itemunidadTO.setPadre(id);//id del nivelactividad
				itemunidadTO.setItemunidadejerciciofiscalid(ejercicio);
				itemunidadTO.setItemunidadunidadid(Long.valueOf(parameters.get("unidadid")));
				itemunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				//Obtengo la obra de codigo 000 para setearlo por defecto
				ObraTO obraTO=new ObraTO();
				obraTO.setObraejerciciofiscalid(ejercicio);
				obraTO.setCodigo(MensajesWeb.getString("codigo.cero.tres"));
				Collection<ObraTO> obraTOs=UtilSession.adminsitracionServicio.transObtenerObra(obraTO);
				if(obraTOs.size()>0){
					obraTO=(ObraTO)obraTOs.iterator().next();
					itemunidadTO.setItemunidadobraid(obraTO.getId());
					itemunidadTO.setNpcodigoobra(obraTO.getCodigo());
					itemunidadTO.setNpnombreobra(obraTO.getNombre());
				}
				//Obtengo el organismo de codigo 0000 para seterarlo por defecto
//				OrganismoTO organismoTO=new OrganismoTO();
//				organismoTO.setOrganismoejerciciofiscalid(ejercicio);
//				organismoTO.setCodigo(MensajesWeb.getString("codigo.cero.cuatro"));
//				Collection<OrganismoTO> organismoTOs=UtilSession.adminsitracionServicio.transObtenerOrganismo(organismoTO);
//				if(organismoTOs.size()>0){
//					log.println("npcodigoorganismo: " + itemunidadTO.getNpcodigoorganismo());
//					organismoTO=(OrganismoTO)organismoTOs.iterator().next();
//					itemunidadTO.setItemunidadorganismoid(organismoTO.getId());
//					itemunidadTO.setNpcodigoorganismo(organismoTO.getCodigo());
//					itemunidadTO.setNpnombreorganismo(organismoTO.getNombre());
//				}
				//Obtengo el organismo prestamo de codigo 0000 para sete
				OrganismoprestamoTO organismoprestamoTO=new OrganismoprestamoTO();
				organismoprestamoTO.setCodigo(MensajesWeb.getString("codigo.cero.cuatro"));
				//organismoprestamoTO.getId().setId(ejercicio);
				OrganismoTO organismoTO=new OrganismoTO();
				organismoTO.setOrganismoejerciciofiscalid(ejercicio);
				organismoTO.setCodigo(MensajesWeb.getString("codigo.cero.cuatro"));
				organismoprestamoTO.setOrganismo(organismoTO);
				Collection<OrganismoprestamoTO> organismoprestamoTOs=UtilSession.planificacionServicio.transObtenerOrganismoprestamo(organismoprestamoTO);
				log.println("organismoprstamo: " + organismoprestamoTOs.size());
				if(organismoprestamoTOs.size()>0){
					
					organismoprestamoTO=(OrganismoprestamoTO)organismoprestamoTOs.iterator().next();
					itemunidadTO.setNpcodigoorgpres(organismoprestamoTO.getCodigo());
					itemunidadTO.setNpnombreorgpres(organismoprestamoTO.getNombre());
					itemunidadTO.setItemunidadorganismoid(organismoprestamoTO.getId().getId());
					itemunidadTO.setNpcodigoorganismo(organismoprestamoTO.getOrganismo().getCodigo());
					itemunidadTO.setNpnombreorganismo(organismoprestamoTO.getOrganismo().getNombre());
					itemunidadTO.setItemunidadorgprestamoid(organismoprestamoTO.getId().getPrestamoid());
				}
				log.println("npcodigoorganismo***: " + itemunidadTO.getNpcodigoorganismo());
				jsonObject.put("itemunidad", (JSONObject)JSONSerializer.toJSON(itemunidadTO,itemunidadTO.getJsonConfig()));
			}
			
			//Subitem
			else if(clase.equals("subitemunidad")){
				//traigo el valor presupuestado y aprobado de la actividad
				String[] pares = tipo.split("&");
				Map<String, String> parameters = new HashMap<String, String>();
				for(String pare : pares) {
				    String[] nameAndValue = pare.split("=");
				    parameters.put(nameAndValue[0], nameAndValue[1]);
				}
				SubitemunidadTO subitemunidadTO = new SubitemunidadTO();
				subitemunidadTO.setPadre(id);//id del nivelactividad
				subitemunidadTO.setSubitemunidadejerfiscalid(ejercicio);
				subitemunidadTO.setSubitemunidadunidadid(Long.valueOf(parameters.get("unidadid")));
				subitemunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subitemunidad", (JSONObject)JSONSerializer.toJSON(subitemunidadTO,subitemunidadTO.getJsonConfig()));
				//obtengo la lista de subitemunidadacumuladorTO existente para saber que acumulador toca
				SubitemunidadacumuladorTO subitemunidadacumuladorExiste=new SubitemunidadacumuladorTO();
				subitemunidadacumuladorExiste.getId().setId(id);
				Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorExistentes=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorExiste);
				SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
				Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=new ArrayList<>();
				subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+1));
				//subitemunidadacumuladorTO.getId().setId(id);
				subitemunidadacumuladorTO.setNpvalor(0.0);
				subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.planificado"));
				subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);
				
				subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
				//subitemunidadacumuladorTO.getId().setId(id);
				subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+2));
				subitemunidadacumuladorTO.setNpvalor(0.0);
				subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);

				subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
				//subitemunidadacumuladorTO.getId().setId(id);
				subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+3));
				subitemunidadacumuladorTO.setNpvalor(0.0);
				subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.devengo"));
				subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);
				jsonObject.put("subitemunidadacumulador", (JSONArray)JSONSerializer.toJSON(subitemunidadacumuladorTOs,subitemunidadacumuladorTO.getJsonConfig()));
				//Saldo para los valores planificados y acumulados
				//1. traigo el valor presupuestado y aprobado de la actividad
				ActividadunidadTO actividadunidadTO=UtilSession.planificacionServicio.transObtenerActividadunidadTO(new ActividadunidadID(Long.valueOf(parameters.get("actividadid")), Long.valueOf(parameters.get("unidadid"))));
				//2. traigo los valores ya reservados para restar y mostrar solo lo disponible
				log.println("actividad id: " + actividadunidadTO.getId().getId());
				log.println("unidad: " + parameters.get("unidadid"));
				log.println("ejercicio: " + ejercicio);
				
				//2. traigo los valores ya reservados para restar y mostrar solo lo disponible
				//Map<String, Double> totales=UtilSession.planificacionServicio.transObtieneAcumulados(id, null, Long.valueOf(parameters.get("unidadid")), Long.valueOf(parameters.get("ejerciciofiscal")));
				Map<String, Double> totales=UtilSession.planificacionServicio.transObtieneAcumulados(id, null, Long.valueOf(parameters.get("unidadid")), ejercicio);
				log.println("valores planificados: " + actividadunidadTO.getPresupplanif() + " - " + totales.get("tplanificado"));
				log.println("valores ajustados: " + actividadunidadTO.getPresupajust().doubleValue() + " - " +totales.get("tacumulado"));
				actividadunidadTO.setPresupplanif(UtilGeneral.redondear(actividadunidadTO.getPresupplanif().doubleValue()-totales.get("tplanificado").doubleValue(),2));
				actividadunidadTO.setPresupajust(UtilGeneral.redondear(actividadunidadTO.getPresupajust().doubleValue()-totales.get("tacumulado").doubleValue(),2));
				log.println("total planificado: " + actividadunidadTO.getPresupplanif());
				log.println("toal presupuestado: " + actividadunidadTO.getPresupajust());
				Map<String, Double> saldos= new HashMap<String,Double>();
				saldos.put("tplanificado", actividadunidadTO.getPresupplanif());
				saldos.put("tacumulado", actividadunidadTO.getPresupajust());
				jsonObject.put("totales", (JSONObject)JSONSerializer.toJSON(saldos));
				jsonObject.put("actividadunidad", (JSONObject)JSONSerializer.toJSON(actividadunidadTO,actividadunidadTO.getJsonConfigSubitem()));
			}
			//Indicador
			else if(clase.equals("indicador")){
				IndicadorTO indicadorTO=new IndicadorTO();
				indicadorTO.setIndicadorpadreid(id);// el id de la actividad
				jsonObject.put("indicador", (JSONObject)JSONSerializer.toJSON(indicadorTO,indicadorTO.getJsonConfig()));
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
	
	@RequestMapping(value = "/{clase}/{id}/{parametro}", method = RequestMethod.GET)
	public Respuesta editarniveles(@PathVariable String clase,@PathVariable Long id,@PathVariable String parametro,HttpServletRequest request){
		log.println("entra al metodo editar niveles: " + clase + " - " + id  + " - " + parametro);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			if(parametro.equals("0"))
				parametro="parametro=0";
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			//Objetivo
			if(clase.equals("objetivo")){
				ObjetivoTO objetivoTO = UtilSession.planificacionServicio.transObtenerObjetivoTO(id);
				//Si tiene meta traigo la politica
				if(objetivoTO.getObjetivometaid()!=null){
					PlannacionalTO plannacionalTO=UtilSession.planificacionServicio.transObtenerPlannacionalTO(objetivoTO.getPlannacional().getPlannacionalpadreid());
					objetivoTO.setNpCodigoPolitica(plannacionalTO.getCodigo());
					objetivoTO.setNpDescripcionPolitica(plannacionalTO.getDescripcion());
				}
				jsonObject.put("objetivo", (JSONObject)JSONSerializer.toJSON(objetivoTO,objetivoTO.getJsonConfig()));
			}
			//Plan nacional
			else if(clase.equals("plannacional")){
				log.println("va a editar el plan nacional");
				PlannacionalTO plannacionalTO = UtilSession.planificacionServicio.transObtenerPlannacionalTO(id);
				jsonObject.put("plannacional", (JSONObject)JSONSerializer.toJSON(plannacionalTO,plannacionalTO.getJsonConfig()));
			}
			//Indicador
			else if(clase.equals("indicador")){
				IndicadorTO indicadorTO = UtilSession.planificacionServicio.transObtenerIndicadorTO(new IndicadorTO(id));
				jsonObject.put("indicador", (JSONObject)JSONSerializer.toJSON(indicadorTO,indicadorTO.getJsonConfig()));
				//Traigo los indicadores metodos
				IndicadormetodoTO indicadormetodoTO=new IndicadormetodoTO();
				indicadormetodoTO.getId().setId(indicadorTO.getId());
				Collection<IndicadormetodoTO> indicadormetodoTOs=UtilSession.planificacionServicio.transObtenerIndicadormetodo(indicadormetodoTO);
				jsonObject.put("indicadormetodo", (JSONArray)JSONSerializer.toJSON(indicadormetodoTOs,indicadormetodoTO.getJsonConfig()));
			}
			//Programa
			else if(clase.equals("programa")){
				ProgramaTO programaTO = UtilSession.planificacionServicio.transObtenerProgramaTO(id);
				programaTO.setNpNivelid(Long.valueOf(parameters.get("npnivelid")));
				programaTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("programa", (JSONObject)JSONSerializer.toJSON(programaTO,programaTO.getJsonConfig()));
			}
			//Subprograma
			else if(clase.equals("subprograma")){
				SubprogramaTO subprogramaTO = UtilSession.planificacionServicio.transObtenerSubprogramaTO(id);
				subprogramaTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subprograma", (JSONObject)JSONSerializer.toJSON(subprogramaTO,subprogramaTO.getJsonConfig()));
			}
			//Proyecto
			else if(clase.equals("proyecto")){
				ProyectoTO proyectoTO = UtilSession.planificacionServicio.transObtenerProyectoTO(id);
				log.println("fecha inicio@@@@: " + proyectoTO.getFechainicio());
				log.println("fecha fin: " + proyectoTO.getFechafin());
				if(proyectoTO.getFechainicio()!=null)
					proyectoTO.setNpFechainicio(UtilGeneral.parseDateToString(proyectoTO.getFechainicio()));
				if(proyectoTO.getFechafin()!=null)
					proyectoTO.setNpFechafin(UtilGeneral.parseDateToString(proyectoTO.getFechafin()));
				proyectoTO.setEstado(MensajesWeb.getString("estado.activo"));
				log.println("fecha npinicio: " + proyectoTO.getNpFechainicio());
				log.println("fecha npfin: " + proyectoTO.getNpFechafin());
				jsonObject.put("proyecto", (JSONObject)JSONSerializer.toJSON(proyectoTO,proyectoTO.getJsonConfig()));
				//Traigo los pryectos meta
				ProyectometaTO proyectometaTO=new ProyectometaTO();
				ProyectometaID proyectometaID=new ProyectometaID();
				proyectometaID.setId(proyectoTO.getId());
				//proyectometaID.setMetaejerciciofiscalid(proyectoTO.getProyectoejerciciofiscalid());
				proyectometaTO.setId(proyectometaID);
				Collection<ProyectometaTO> proyectometaTOs=UtilSession.planificacionServicio.transObtenerProyectometa(proyectometaTO);
				jsonObject.put("proyectometa", (JSONArray)JSONSerializer.toJSON(proyectometaTOs,proyectometaTO.getJsonConfig()));
			}
			//Actividad
			else if(clase.equals("actividad")){
				ActividadTO actividadTO = UtilSession.planificacionServicio.transObtenerActividad(id);
				log.println("indicador metodo: " + actividadTO.getIndicadormetodo());
				actividadTO.setEstado(MensajesWeb.getString("estado.activo"));
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadorcodigo(actividadTO.getIndicadormetodo().getIndicador().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadornombre(actividadTO.getIndicadormetodo().getIndicador().getNombre());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null) 
					actividadTO.setNpIndicadordescripcion(actividadTO.getIndicadormetodo().getIndicador().getDescripcion());
				if(actividadTO.getIndicadormetodo()!=null)
					actividadTO.setNpIndicadormetcodigo(actividadTO.getIndicadormetodo().getCodigo());
				if(actividadTO.getIndicadormetodo()!=null)
					actividadTO.setNpIndicadormetdescripcion(actividadTO.getIndicadormetodo().getDescripcion());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadormetadescriopcion(actividadTO.getIndicadormetodo().getIndicador().getMetadescripcion());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida()!=null)
					actividadTO.setNpUnidadmedidacodigo(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida()!=null)
					actividadTO.setNpUnidadmedidanombre(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getNombre());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida()!=null)
					actividadTO.setNpGrupomedidacodigo(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida()!=null)
					actividadTO.setNpGrupomedidanombre(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida().getNombre());
				jsonObject.put("actividad", (JSONObject)JSONSerializer.toJSON(actividadTO,actividadTO.getJsonConfig()));
				//Traigo la lista de nivelactividad
				NivelactividadTO nivelactividadTO=new NivelactividadTO();
				nivelactividadTO.setTablarelacionid(actividadTO.getId());
				nivelactividadTO.setTipo(MensajesAplicacion.getString("formulacion.tipo.actividad"));
				nivelactividadTO.setUnidad(new UnidadTO());
				Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtenerNivelactividad(nivelactividadTO);
				jsonObject.put("nivelactividad", (JSONArray)JSONSerializer.toJSON(nivelactividadTOs,nivelactividadTO.getJsonConfigActividad()));
			}
			//Subactividad
			else if(clase.equals("subactividad")){
				SubactividadTO subactividadTO = UtilSession.planificacionServicio.transObtenerSubactividadTO(id);
				subactividadTO.setEstado(MensajesWeb.getString("estado.activo"));
				jsonObject.put("subactividad", (JSONObject)JSONSerializer.toJSON(subactividadTO,subactividadTO.getJsonConfigedicion()));
			}
			
			//Actividad unidad en la planificacion se carga al poner editar actividad
			else if(clase.equals("actividadunidad")){
				ActividadTO actividadTO = UtilSession.planificacionServicio.transObtenerActividad(id);
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadorcodigo(actividadTO.getIndicadormetodo().getIndicador().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadornombre(actividadTO.getIndicadormetodo().getIndicador().getNombre());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null) 
					actividadTO.setNpIndicadordescripcion(actividadTO.getIndicadormetodo().getIndicador().getDescripcion());
				if(actividadTO.getIndicadormetodo()!=null)
					actividadTO.setNpIndicadormetcodigo(actividadTO.getIndicadormetodo().getCodigo());
				if(actividadTO.getIndicadormetodo()!=null)
					actividadTO.setNpIndicadormetdescripcion(actividadTO.getIndicadormetodo().getDescripcion());
				if(actividadTO.getIndicadormetodo().getIndicador()!=null)
					actividadTO.setNpIndicadormetadescriopcion(actividadTO.getIndicadormetodo().getIndicador().getMetadescripcion());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida()!=null)
					actividadTO.setNpUnidadmedidacodigo(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida()!=null)
					actividadTO.setNpUnidadmedidanombre(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getNombre());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida()!=null)
					actividadTO.setNpGrupomedidacodigo(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida().getCodigo());
				if(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida()!=null)
					actividadTO.setNpGrupomedidanombre(actividadTO.getIndicadormetodo().getIndicador().getUnidadmedida().getGrupomedida().getNombre());
				jsonObject.put("actividad", (JSONObject)JSONSerializer.toJSON(actividadTO,actividadTO.getJsonConfig()));
				ActividadunidadTO actividadunidadTO=new ActividadunidadTO();
				actividadunidadTO.getId().setId(id);
				actividadunidadTO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
				log.println("va a conssultar actividad unidad: " + id + "---" + Long.valueOf(parameters.get("unidadid")));
				Collection<ActividadunidadTO> actividadunidadTOs = UtilSession.planificacionServicio.transObtenerActividadunidad(actividadunidadTO);
				actividadunidadTO=(ActividadunidadTO)actividadunidadTOs.iterator().next();
				log.println("ajustado: " + actividadunidadTO.getPresupajust());
				log.println("planificado: " + actividadunidadTO.getPresupplanif());
				log.println("fecha de inicio: " + actividadunidadTO.getFechainicio());
				log.println("fecha de fin: " + actividadunidadTO.getFechafin());
				actividadunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				if(actividadunidadTO.getFechainicio()!=null){
					SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");//-------------------------OJO
					actividadunidadTO.setNpFechainicio(sdfDestination.format(actividadunidadTO.getFechainicio()));
				}
				if(actividadunidadTO.getFechafin()!=null)
					actividadunidadTO.setNpFechafin(UtilGeneral.parseDateToString(actividadunidadTO.getFechafin()));
				jsonObject.put("actividadunidad", (JSONObject)JSONSerializer.toJSON(actividadunidadTO,actividadunidadTO.getJsonConfig()));
				//traigo los datos de actividadunidadacumulador
				ActividadunidadacumuladorTO actividadunidadacumuladorTO=new ActividadunidadacumuladorTO();
				actividadunidadacumuladorTO.getId().setId(id);
				actividadunidadacumuladorTO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
				Collection<ActividadunidadacumuladorTO> actividadunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerActividadunidadacumulador(actividadunidadacumuladorTO);
				//Si no existe debo crearlas
				if(actividadunidadacumuladorTOs==null || actividadunidadacumuladorTOs.size()==0){
					//obtengo la lista de actividadunidadacumuladorTO existente para saber que acumulador toca
					ActividadunidadacumuladorTO actividadunidadacumuladorExiste=new ActividadunidadacumuladorTO();
					actividadunidadacumuladorExiste.getId().setId(id);
					Collection<ActividadunidadacumuladorTO> actividadunidadacumuladorExistentes=UtilSession.planificacionServicio.transObtenerActividadunidadacumulador(actividadunidadacumuladorExiste);
					actividadunidadacumuladorTO.getId().setAcumid(Long.valueOf(actividadunidadacumuladorExistentes.size()+1));
					//actividadunidadacumuladorTO.getId().setId(id);
					actividadunidadacumuladorTO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
					actividadunidadacumuladorTO.setNpValor(0.0);
					actividadunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.planificado"));
					actividadunidadacumuladorTOs.add(actividadunidadacumuladorTO);
					
					actividadunidadacumuladorTO=new ActividadunidadacumuladorTO();
					//actividadunidadacumuladorTO.getId().setId(id);
					actividadunidadacumuladorTO.getId().setUnidadid(Long.valueOf(parameters.get("unidadid")));
					actividadunidadacumuladorTO.getId().setAcumid(Long.valueOf(actividadunidadacumuladorExistentes.size()+2));
					actividadunidadacumuladorTO.setNpValor(0.0);
					actividadunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
					actividadunidadacumuladorTOs.add(actividadunidadacumuladorTO);
				}
				//si ya existen asigno el valor total en nptotal para luego comparar si fue moficado
				else{
					for(ActividadunidadacumuladorTO actividadunidadacumuladorTO2:actividadunidadacumuladorTOs){
						actividadunidadacumuladorTO2.setNpValor(actividadunidadacumuladorTO2.getCantidad());
					}
				}
				jsonObject.put("actividadunidadacumulador", (JSONArray)JSONSerializer.toJSON(actividadunidadacumuladorTOs,actividadunidadacumuladorTO.getJsonConfig()));
			}
			
			//Subactividad unidad en la planificacion se carga al poner editar subactividad
			else if(clase.equals("subactividadunidad")){
				SubactividadunidadTO subactividadunidadTO = UtilSession.planificacionServicio.transObtenerSubactividadunidadTO(new SubactividadunidadID(id, Long.valueOf(parameters.get("unidadid"))));
				subactividadunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				subactividadunidadTO.setPadre(Long.valueOf(parameters.get("nivelactividadid")));
				subactividadunidadTO.setNpponderacion(subactividadunidadTO.getPonderacion());
				jsonObject.put("subactividadunidad", (JSONObject)JSONSerializer.toJSON(subactividadunidadTO,subactividadunidadTO.getJsonConfig()));
			}
			
			//Tarea unidad en la planificacion se carga al poner editar tarea
			else if(clase.equals("tareaunidad")){
				TareaunidadTO tareaunidadTO = UtilSession.planificacionServicio.transObtenerTareaunidadTO(id);
				tareaunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				log.println("nivelactividadid: " + parameters.get("nivelactividad"));
				tareaunidadTO.setPadre(Long.valueOf(parameters.get("nivelactividad")));
				tareaunidadTO.setNpponderacion(tareaunidadTO.getPonderacion());
				jsonObject.put("tareaunidad", (JSONObject)JSONSerializer.toJSON(tareaunidadTO,tareaunidadTO.getJsonConfig()));
			}
			
			//Subtarea unidad en la planificacion se carga al poner editar subtarea
			else if(clase.equals("subtareaunidad")){
				SubtareaunidadTO subtareaunidadTO = UtilSession.planificacionServicio.transObtenerSubtareaunidadTO(id);
				subtareaunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				subtareaunidadTO.setPadre(Long.valueOf(parameters.get("nivelactividad")));
				subtareaunidadTO.setNpponderacion(subtareaunidadTO.getPonderacion());
				jsonObject.put("subtareaunidad", (JSONObject)JSONSerializer.toJSON(subtareaunidadTO,subtareaunidadTO.getJsonConfigeditar()));
				//traigo los datos de subtareaunidadacumulador
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.getId().setId(id);
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				//Si no existe debo crearlas
				if(subtareaunidadacumuladorTOs==null || subtareaunidadacumuladorTOs.size()==0){
					//obtengo la lista de subtareaunidadacumuladorTO existente para saber que acumulador toca
					SubtareaunidadacumuladorTO subtareaunidadacumuladorExiste=new SubtareaunidadacumuladorTO();
					subtareaunidadacumuladorExiste.getId().setId(id);
					Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorExistentes=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorExiste);
					subtareaunidadacumuladorTO.getId().setAcumid(Long.valueOf(subtareaunidadacumuladorExistentes.size()+1));
					//subtareaunidadacumuladorTO.getId().setId(id);
					subtareaunidadacumuladorTO.setNpValor(0.0);
					subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.planificado"));
					subtareaunidadacumuladorTOs.add(subtareaunidadacumuladorTO);
					
					subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
					//subtareaunidadacumuladorTO.getId().setId(id);
					subtareaunidadacumuladorTO.getId().setAcumid(Long.valueOf(subtareaunidadacumuladorExistentes.size()+2));
					subtareaunidadacumuladorTO.setNpValor(0.0);
					subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
					subtareaunidadacumuladorTOs.add(subtareaunidadacumuladorTO);
				}
				//si ya existen asigno el valor total en nptotal para luego comparar si fue moficado
				else{
					for(SubtareaunidadacumuladorTO subtareaunidadacumuladorTO2:subtareaunidadacumuladorTOs){
						subtareaunidadacumuladorTO2.setNpValor(subtareaunidadacumuladorTO2.getCantidad());
					}
				}
				jsonObject.put("subtareaunidadacumulador", (JSONArray)JSONSerializer.toJSON(subtareaunidadacumuladorTOs,subtareaunidadacumuladorTO.getJsonConfig()));
			}
			
			//Item unidad en la planificacion se carga al poner editar item
			else if(clase.equals("itemunidad")){
				ItemunidadTO itemunidadTO = UtilSession.planificacionServicio.transObtenerItemunidadTO(id);
				itemunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				//Seteo los valores
				itemunidadTO.setNpcodigoitem(itemunidadTO.getItem().getCodigo());
				itemunidadTO.setNpnombreitem(itemunidadTO.getItem().getNombre());
				itemunidadTO.setNpcodigoobra(itemunidadTO.getObra().getCodigo());
				itemunidadTO.setNpnombreobra(itemunidadTO.getObra().getNombre());
				itemunidadTO.setNpcodigofuente(itemunidadTO.getFuentefinanciamiento().getCodigo());
				itemunidadTO.setNpnombrefuente(itemunidadTO.getFuentefinanciamiento().getNombe());
				itemunidadTO.setNpcodigoorganismo(itemunidadTO.getOrganismoprestamo().getCodigo());
				itemunidadTO.setNpnombreorganismo(itemunidadTO.getOrganismoprestamo().getNombre());
				itemunidadTO.setNpcodigoorgpres(itemunidadTO.getOrganismoprestamo().getCodigo());
				itemunidadTO.setNpnombreorgpres(itemunidadTO.getOrganismoprestamo().getNombre());
				itemunidadTO.setNpcodigocanton(itemunidadTO.getDivisiongeografica().getCodigo());
				itemunidadTO.setNpnombrecanton(itemunidadTO.getDivisiongeografica().getNombre());
				jsonObject.put("itemunidad", (JSONObject)JSONSerializer.toJSON(itemunidadTO,itemunidadTO.getJsonConfig()));
			}
			
			//Subitem unidad en la planificacion se carga al poner editar subitem
			else if(clase.equals("subitemunidad")){
				log.println("va a editar el subitem unidad");
				SubitemunidadTO subitemunidadTO = UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				subitemunidadTO.setEstado(MensajesWeb.getString("estado.activo"));
				subitemunidadTO.setNpcodigosubitem(subitemunidadTO.getSubitem().getCodigo());
				subitemunidadTO.setNpnombresubitem(subitemunidadTO.getSubitem().getNombre());
				jsonObject.put("subitemunidad", (JSONObject)JSONSerializer.toJSON(subitemunidadTO,subitemunidadTO.getJsonConfig()));
				//traigo los datos de actividadunidadacumulador
				SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
				subitemunidadacumuladorTO.getId().setId(id);
				subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
				Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
				//Si no existe debo crearlas
				if(subitemunidadacumuladorTOs==null || subitemunidadacumuladorTOs.size()==0){
					//obtengo la lista de subitemunidadacumuladorTO existente para saber que acumulador toca
					SubitemunidadacumuladorTO subitemunidadacumuladorExiste=new SubitemunidadacumuladorTO();
					subitemunidadacumuladorExiste.getId().setId(id);
					Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorExistentes=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorExiste);
					subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+1));
					//subitemunidadacumuladorTO.getId().setId(id);
					subitemunidadacumuladorTO.setNpvalor(0.0);
					subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.planificado"));
					subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);
					
					subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
					//subitemunidadacumuladorTO.getId().setId(id);
					subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+2));
					subitemunidadacumuladorTO.setNpvalor(0.0);
					subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
					subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);

					subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
					//subitemunidadacumuladorTO.getId().setId(id);
					subitemunidadacumuladorTO.getId().setAcumid(Long.valueOf(subitemunidadacumuladorExistentes.size()+2));
					subitemunidadacumuladorTO.setNpvalor(0.0);
					subitemunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.devengo"));
					subitemunidadacumuladorTOs.add(subitemunidadacumuladorTO);
				}
				//si ya existen asigno el valor total en nptotal para luego comparar si fue moficado
				else{
					for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs){
						subitemunidadacumuladorTO2.setNpvalor(subitemunidadacumuladorTO2.getTotal());
					}
				}
				jsonObject.put("subitemunidadacumulador", (JSONArray)JSONSerializer.toJSON(subitemunidadacumuladorTOs,subitemunidadacumuladorTO.getJsonConfig()));
				//Saldo para los valores planificados y acumulados
				//1. traigo el valor presupuestado y aprobado de la actividad
				log.println("actividadid: " + parameters.get("actividadid"));
				log.println("unidad: " + parameters.get("unidadid"));
				log.println("ejercicio: " + parameters.get("ejerciciofiscal"));
				ActividadunidadTO actividadunidadTO=UtilSession.planificacionServicio.transObtenerActividadunidadTO(new ActividadunidadID(Long.valueOf(parameters.get("actividadid")), Long.valueOf(parameters.get("unidadid"))));
				log.println("valores planificados: " + actividadunidadTO.getPresupplanif());
				log.println("valores ajustados: " + actividadunidadTO.getPresupajust().doubleValue());
				//2. traigo los valores ya reservados para restar y mostrar solo lo disponible
				//Map<String, Double> totales=UtilSession.planificacionServicio.transObtieneAcumulados(id, null, Long.valueOf(parameters.get("unidadid")), Long.valueOf(parameters.get("ejerciciofiscal")));
				Map<String, Double> totales=UtilSession.planificacionServicio.transObtieneAcumulados(id, null, Long.valueOf(parameters.get("unidadid")), Long.valueOf(parameters.get("ejerciciofiscal")));
				log.println("total planificados: " + totales.get("tplanificado"));
				log.println("total ajustados: " + totales.get("tacumulado"));
				actividadunidadTO.setPresupplanif(UtilGeneral.redondear(actividadunidadTO.getPresupplanif().doubleValue()-totales.get("tplanificado").doubleValue(),2));
				actividadunidadTO.setPresupajust(UtilGeneral.redondear(actividadunidadTO.getPresupajust().doubleValue()-totales.get("tacumulado").doubleValue(),2));
				log.println("total planificado=: " + actividadunidadTO.getPresupplanif());
				log.println("toal presupuestado=: " + actividadunidadTO.getPresupajust());
				Map<String, Double> saldos= new HashMap<String,Double>();
				saldos.put("tplanificado", actividadunidadTO.getPresupplanif());
				saldos.put("tacumulado", actividadunidadTO.getPresupajust());
				jsonObject.put("totales", (JSONObject)JSONSerializer.toJSON(saldos));
				jsonObject.put("actividadunidad", (JSONObject)JSONSerializer.toJSON(actividadunidadTO,actividadunidadTO.getJsonConfigSubitem()));
				
			}
			
			//-----------------Las cabeceras cuando se da click en la descripcion de los niveles del arbol
			//ActividadPlanificacion
			if(clase.equals("actividadplanificacion")){
				ActividadTO actividadTO=new ActividadTO();
				actividadTO.setId(id);
				log.println("id:... " + id);
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesniveles(actividadTO,Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("actividadplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,actividadTO.getJsonConfigEditarPlanificacion()));
			}
			
			//SubctividadPlanificacion
			else if(clase.equals("subactividadplanificacion")){
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesnivelesSuactiviad(id, Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("subactividadplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,new ActividadTO().getJsonConfigEditarPlanificacion()));
			}
			
			//TareaPlanificacion
			else if(clase.equals("tareaplanificacion")){
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesnivelesTarea(id, Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("tareaplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,new ActividadTO().getJsonConfigEditarPlanificacion()));
			}
			
			//SubtareaPlanificacion
			else if(clase.equals("subtareaplanificacion")){
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesnivelesSubtarea(id, Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("subtareaplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,new ActividadTO().getJsonConfigEditarPlanificacion()));
			}
			
			//ItemPlanificacion
			else if(clase.equals("itemplanificacion")){
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesnivelesItem(id, Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("itemplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,new ActividadTO().getJsonConfigEditarPlanificacion()));
			}
			
			//SubitemPlanificacion
			else if(clase.equals("subitemplanificacion")){
				ActividadTO actividadTO1 = UtilSession.planificacionServicio.transObtieneActividadesnivelesSubitem(id, Long.valueOf(parameters.get("unidadid")),Long.valueOf(parameters.get("ejerciciofiscal")));
				jsonObject.put("subitemplanificacion", (JSONObject)JSONSerializer.toJSON(actividadTO1,new ActividadTO().getJsonConfigEditarPlanificacion()));
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
	
	@RequestMapping(value = "/{clase}/{id}/{id2}", method = RequestMethod.DELETE)
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	public Respuesta eliminar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,HttpServletRequest request){
		log.println("entra al metodo eliminar: " + id);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			//Objetivo
			if(clase.equals("objetivo")){
				UtilSession.planificacionServicio.transEliminarObjetivo(new ObjetivoTO(id));
			}
			//Plan nacional
			else if(clase.equals("plannacional")){
				UtilSession.planificacionServicio.transEliminarPlannacional(new PlannacionalTO(id));
			}
			//Indicador
			else if(clase.equals("indicador")){
				UtilSession.planificacionServicio.transEliminarIndicador(new IndicadorTO(id));
			}
			//Programa
			else if(clase.equals("programa")){
				UtilSession.planificacionServicio.transEliminarPrograma(new ProgramaTO(id));
			}
			//Subprograma
			else if(clase.equals("subprograma")){
				UtilSession.planificacionServicio.transEliminarSubprograma(new SubprogramaTO(id));
			}
			//Proyecto
			else if(clase.equals("proyecto")){
				UtilSession.planificacionServicio.transEliminarProyecto(new ProyectoTO(id));
			}
			//Proyecto meta
			else if(clase.equals("proyectometa")){
				UtilSession.planificacionServicio.transEliminarProyectometa(new ProyectometaTO(new ProyectometaID(id, id2)));
			}
			//Actividad
			else if(clase.equals("actividad")){
				UtilSession.planificacionServicio.transEliminarActividad(new ActividadTO(id));
			}
			//Subactividad
			else if(clase.equals("subactividad")){
				UtilSession.planificacionServicio.transEliminarSubactividad(new SubactividadTO(id));
			}
			//Actividadunidad
			else if(clase.equals("actividadunidad")){
				UtilSession.planificacionServicio.transEliminarActividadunidad(new ActividadunidadTO(new ActividadunidadID(id, id2)));
			}
			//Nivelactividad
			else if(clase.equals("nivelactividad")){
				UtilSession.planificacionServicio.transEliminarNivelactividad(new NivelactividadTO(id));
			}
			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.eliminar") + " " + clase);
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
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
	public Respuesta consultar(HttpServletRequest request,@PathVariable String clase,@PathVariable String parametro, Principal principal) {
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
			
			//Objetivo
			if(clase.equals("objetivo")){
				jsonObject=ConsultasUtil.consultaObjetivoarbol(parameters, jsonObject);
			}
			//Plan nacional
			else if(clase.equals("plannacional")){
				log.println("Entra a consultar plan nacional");
				jsonObject=ConsultasUtil.consultaPlannacionalarbol(parameters, jsonObject);
			}
			//Metas
			else if(clase.equals("metas")){
				jsonObject=ConsultasUtil.consultaMetas(parameters, jsonObject);
			}
			//Indicador
			else if(clase.equals("indicador")){
				jsonObject=ConsultasUtil.consultaIndicadorarbol(parameters, jsonObject);
			}
			//Programa
			else if(clase.equals("programa")){
				jsonObject=ConsultasUtil.consultaPrograma(parameters, jsonObject);
			}
			//indicadormetodo
			else if(clase.equals("indicadormetodo")){
				Collection<IndicadormetodoTO> indicadormetodoTOs=UtilSession.planificacionServicio.transObtenerIndicadormetodo(new IndicadormetodoTO());
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(indicadormetodoTOs,new IndicadormetodoTO().getJsonConfig()));
			}
			
			//Objetivo grilla
			else if(clase.equals("objetivogrilla")){
				jsonObject=ConsultasUtil.consultaObjetivogrilla(parameters, jsonObject);
			}
			
			//Subprograma
			else if(clase.equals("subprograma")){
				jsonObject=ConsultasUtil.consultaSubprograma(parameters, jsonObject);
			}
			
			//Proyecto
			else if(clase.equals("proyecto")){
				jsonObject=ConsultasUtil.consultaProyecto(parameters, jsonObject);
			}

			//Proyectoreporte
			else if(clase.equals("proyectoreporte")){
				jsonObject=ConsultasUtil.consultaProyectoReporte(parameters, jsonObject);
			}

			//Busqueda Indicador
			else if(clase.equals("consultaBusquedaIndicador")){
				jsonObject=ConsultasUtil.consultaBusquedaIndicador(parameters, jsonObject);
			}

			//Actividad
			else if(clase.equals("actividad")){
				jsonObject=ConsultasUtil.consultaActividad(parameters, jsonObject);
			}

			//Subactividad
			else if(clase.equals("subactividad")){
				jsonObject=ConsultasUtil.consultaSubactividad(parameters, jsonObject);
			}
			
			//Busqueda Indicador
			else if(clase.equals("consultaBusquedaIndicadoractividad")){
				jsonObject=ConsultasUtil.consultaBusquedaIndicadordesdeActividad(parameters, jsonObject);
			}

			//Consulta Planificiacion es la consulta de la grilla del menu planificacion
			else if(clase.equals("planificacion")){
				jsonObject=ConsultasUtil.consultaPlanificacion(parameters, jsonObject,request,principal);
			}
			
			//NivelActividad
			else if(clase.equals("nivelactividad")){
				jsonObject=ConsultasUtil.consultaNivelactividad(parameters, jsonObject);
				//si el tipo es AC enviar el nombre de la unidad
				if(parameters.get("tipo").equals("AC") &&parameters.get("nivelactividadunidadid")!=null && !parameters.get("nivelactividadunidadid").equals("")) {
					UnidadTO unidadTO=UtilSession.estructuraorganicaServicio.transObtenerUnidadTO(Long.valueOf(parameters.get("nivelactividadunidadid")));
					Map<String, String> unidad=new HashMap<String, String>();
					unidad.put("nombre", unidadTO.getNombre());
					jsonObject.put("unidad", (JSONObject)JSONSerializer.toJSON(unidad));
				}
			}
			
			//Aprobar unidad
			else if(clase.equals("aprobar")){
				//debe llegar id y npacitividadunidad
				log.println("nivelactividadunidad: " + parameters.get("nivelactividadunidadid"));
				log.println("ejercicio fiscal: " + parameters.get("nivelactividadejerfiscalid"));
				log.println("tipo aprobacion: " + parameters.get("tipo"));
				log.println("id: " + parameters.get("unidad"));
				ActividadunidadTO actividadunidadTO=UtilSession.planificacionServicio.transObtenerActividadunidadTO(new ActividadunidadID(Long.valueOf(parameters.get("nivelactividadunidadid")),Long.valueOf(parameters.get("unidad"))));
				log.println("actividad: " + actividadunidadTO.getId().getId());
				Boolean mensaje=ConsultasUtil.aprobacionplanificacion(Long.valueOf(parameters.get("unidad")), Long.valueOf(parameters.get("nivelactividadejerfiscalid")), parameters.get("tipo"), Long.valueOf(parameters.get("nivelactividadunidadid")), actividadunidadTO.getId().getId(), jsonObject);
				if(mensaje){
					mensajes.setMsg("No se puede aprobar existen observaciones");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					//va aprobar la actividadunidad
					if(parameters.get("tipo").equals("P"))
						actividadunidadTO.setPresupaprobado(1.0);
					else
						actividadunidadTO.setPresupajust(1.0);
					UtilSession.planificacionServicio.transCrearModificarActividadunidad(actividadunidadTO);
					mensajes.setMsg("se aprobo con exito");
					mensajes.setType(MensajesWeb.getString("mensaje.exito"));
				}
			}

			//Matriz de presupuestos
			else if(clase.equals("matrizpresupuestos")){
				jsonObject=ConsultasUtil.matrizpresupuesto(parameters, jsonObject);
			}
			
			//Matriz de presupuestos
			else if(clase.equals("matrizmetas")){
				jsonObject=ConsultasUtil.matrizmetas(parameters, jsonObject);
			}
			
			
//			//Instituto entidad
//			else if(clase.equals("institutoentidad")){
//				jsonObject=ConsultasUtil.consultaInstitucionentidadPaginado(parameters, jsonObject);
//			}
//
//			//Estructura organica
//			else if(clase.equals("estructuraorganica")){
//				jsonObject=ConsultasUtil.consultaEstructuraorganicaPaginado(parameters, jsonObject);
//			}
//
//			//Unidad
//			else if(clase.equals("unidad")){
//				jsonObject=ConsultasUtil.consultaUnidadPaginado(parameters, jsonObject);
//			}
//
//			//Unidad arbol
//			else if(clase.equals("unidadarbol")){
//				jsonObject=ConsultasUtil.consultaUnidadaarboarbol(parameters, jsonObject);
//			}
//
//			//Unidad arbol plaza
//			else if(clase.equals("unidadarbolplaza")){
//				jsonObject=ConsultasUtil.consultaUnidadarbolplazaPaginado(parameters, jsonObject);
//			}
//
//			//Unidad arbol plaza empleado
//			else if(clase.equals("unidadarbolplazaempleado")){
//				jsonObject=ConsultasUtil.consultaUnidadarbolplazaempleadoPaginado(parameters, jsonObject);
//			}
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
	
	
	@RequestMapping(value = "/cronograma/{id}/{acumulador}/{unidad}/{tipo}/{tipometa}/{ejerciciofiscal}", method = RequestMethod.GET)
	public Respuesta cronograma(@PathVariable Long id,@PathVariable Long acumulador,@PathVariable Long unidad,@PathVariable String tipo,@PathVariable String tipometa,@PathVariable String ejerciciofiscal,HttpServletRequest request){
		log.println("entra al metodo recuperar: " + id + "- "+  acumulador + "- "+ unidad + "- "+ tipo + "- "+ tipometa + "- "+ ejerciciofiscal);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Metas (cronograma y cronograma linea) distribucion de presupuesto
			//..if(clase.equals("cronograma")){
				//Obtengo las metas del que corresponda planificado o ajustado
				CronogramaTO cronogramaTO=new CronogramaTO();
				cronogramaTO.setTablarelacionid(id);//actividad,subtarea unidad
				cronogramaTO.setAcumuladorid(acumulador);//es el mismo acumulador de la tabla actividadunidadacumulador
				cronogramaTO.setTiporelacion(tipo);
				cronogramaTO.setCronogramaunidadid(unidad);
				Collection<CronogramaTO> cronogramaTOs=UtilSession.planificacionServicio.transObtenerCronograma(cronogramaTO);
				log.println("numero de cronogramas encontrados: " + cronogramaTOs.size());
				//Si existe ya creado obtengo todo los datos sino, creo la cabecera y el detalle
				Collection<CronogramalineaTO> cronogramalineaTOs=new ArrayList<CronogramalineaTO>();
				if(cronogramaTOs.size()>0){
					cronogramaTO=(CronogramaTO)cronogramaTOs.iterator().next();
					//Obtengo los detalles
					CronogramalineaTO cronogramalineaTO=new CronogramalineaTO();
					cronogramalineaTO.getId().setId(cronogramaTO.getId());
					cronogramalineaTOs=UtilSession.planificacionServicio.transObtenerCronogramalinea(cronogramalineaTO);
					log.println("detalle cronograma: " + cronogramalineaTOs.size()); 
					for(CronogramalineaTO cronogramalineaTO2:cronogramalineaTOs)
						cronogramalineaTO2.setNpfechainicio(UtilGeneral.parseDateToString(cronogramalineaTO2.getFechainicio()));
				}
				else{
					log.println("no existe el cronograma");
					
					//obtengo el ejerciciofiscal
					cronogramaTO.setCronogramaunidadid(unidad);
					cronogramaTO.setTiporelacion(tipo);
					int contador=1;
					String fecha="";
					if(tipometa.equals("A"))
						contador=13;
					if(tipometa.equals("D"))
						contador=25;
					for(int i=1;i<13;i++){
						//Creo los detalles
						CronogramalineaTO cronogramalineaTO=new CronogramalineaTO();
						cronogramalineaTO.setMes(Long.valueOf(i));
						cronogramalineaTO.getId().setLineaid(Long.valueOf(contador+i-1));
						if(i<10)
							fecha="01"+"/0"+i+"/"+ejerciciofiscal;
						else
							fecha="01"+"/"+i+"/"+ejerciciofiscal;
						cronogramalineaTO.setFechainicio(UtilGeneral.parseStringToDate(fecha));
						cronogramalineaTO.setNpfechainicio(fecha);
						cronogramalineaTOs.add(cronogramalineaTO);
					}
				}
				jsonObject.put("cronograma", (JSONObject)JSONSerializer.toJSON(cronogramaTO,cronogramaTO.getJsonConfig()));
				jsonObject.put("cronogramalinea", (JSONArray)JSONSerializer.toJSON(cronogramalineaTOs,new CronogramalineaTO().getJsonConfig()));
			//..}
		
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
	
}
