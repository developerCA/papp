package ec.com.papp.web.ejecucion.controller;

import java.io.StringReader;
import java.util.Collection;
import java.util.Date;
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

import ec.com.papp.administracion.to.ClaseregistroTO;
import ec.com.papp.administracion.to.ClaseregistroclasemodificacionTO;
import ec.com.papp.administracion.to.TipodocumentoTO;
import ec.com.papp.administracion.to.TipodocumentoclasedocumentoTO;
import ec.com.papp.planificacion.id.CertificacionlineaID;
import ec.com.papp.planificacion.id.OrdendevengolineaID;
import ec.com.papp.planificacion.id.OrdengastolineaID;
import ec.com.papp.planificacion.to.CertificacionTO;
import ec.com.papp.planificacion.to.CertificacionlineaTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.OrdendevengoTO;
import ec.com.papp.planificacion.to.OrdendevengolineaTO;
import ec.com.papp.planificacion.to.OrdengastoTO;
import ec.com.papp.planificacion.to.OrdengastolineaTO;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.ejecucion.util.ConsultasUtil;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;

/**
 * @autor: jcalderon
 * @fecha: 27-02-2016
 * @copyright: Xcelsa
 * @version: 1.0
 * @descripcion Clase para realizar administraciones centralizadas de ejecucion
*/

@RestController
@RequestMapping("/rest/ejecucion")
public class EjecucionController {
	private Log log = new Log(EjecucionController.class);
	
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
			//certificacion
			if(clase.equals("certificacion")){
				CertificacionTO certificacionTO = gson.fromJson(new StringReader(objeto), CertificacionTO.class);
				accion = (certificacionTO.getId()==null)?"crear":"actualizar";
				if(certificacionTO.getNpfechaaprobacion()!=null)
					certificacionTO.setFechaaprobacion(UtilGeneral.parseStringToDate(certificacionTO.getNpfechaaprobacion()));
				if(certificacionTO.getNpfechacreacion()!=null)
					certificacionTO.setFechacreacion(UtilGeneral.parseStringToDate(certificacionTO.getNpfechacreacion()));
				if(certificacionTO.getNpfechaeliminacion()!=null)
					certificacionTO.setFechaeliminacion(UtilGeneral.parseStringToDate(certificacionTO.getNpfechaeliminacion()));
				if(certificacionTO.getNpfechaliquidacion()!=null)
					certificacionTO.setFechaliquidacion(UtilGeneral.parseStringToDate(certificacionTO.getNpfechaliquidacion()));
				if(certificacionTO.getNpfechanegacion()!=null)
					certificacionTO.setFechanegacion(UtilGeneral.parseStringToDate(certificacionTO.getNpfechanegacion()));
				if(certificacionTO.getNpfechasolicitud()!=null)
					certificacionTO.setFechasolicitud(UtilGeneral.parseStringToDate(certificacionTO.getNpfechasolicitud()));
				UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,null);
				id=certificacionTO.getId().toString();
				jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
			}
			//certificacion linea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = gson.fromJson(new StringReader(objeto), CertificacionlineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				CertificacionlineaTO certificacionlineaTO2=new CertificacionlineaTO();
				certificacionlineaTO2.setNivelactid(certificacionlineaTO.getNivelactid());
				certificacionlineaTO2.getId().setId(certificacionlineaTO.getId().getId());
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO2);
				if(certificacionlineaTOs.size()==0){
					accion = (certificacionlineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarCertificacionlinea(certificacionlineaTO);
					id=certificacionlineaTO.getId().toString();
					//Traiga la lista de cetificacionlinea
					CertificacionlineaTO certificacionlineaTO3=new CertificacionlineaTO();
					certificacionlineaTO3.getId().setId(certificacionlineaTO.getId().getId());
					Collection<CertificacionlineaTO> certificacionlineaTOs2=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO3);
					jsonObject.put("certificacionlineas", (JSONArray)JSONSerializer.toJSON(certificacionlineaTOs2,certificacionlineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//ordengasto
			if(clase.equals("ordengasto")){
				OrdengastoTO ordengastoTO = gson.fromJson(new StringReader(objeto), OrdengastoTO.class);
				if(ordengastoTO.getNpfechaaprobacion()!=null)
					ordengastoTO.setFechaaprobacion(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechaaprobacion()));
				if(ordengastoTO.getNpfechacreacion()!=null)
					ordengastoTO.setFechacreacion(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechacreacion()));
				if(ordengastoTO.getNpfechaeliminacion()!=null)
					ordengastoTO.setFechaeliminacion(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechaeliminacion()));
				if(ordengastoTO.getNpfechaanulacion()!=null)
					ordengastoTO.setFechaanulacion(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechaanulacion()));
				if(ordengastoTO.getNpfechanegacion()!=null)
					ordengastoTO.setFechanegacion(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechanegacion()));
				if(ordengastoTO.getNpfechasolicitud()!=null)
					ordengastoTO.setFechasolicitud(UtilGeneral.parseStringToDate(ordengastoTO.getNpfechasolicitud()));
				accion = (ordengastoTO.getId()==null)?"crear":"actualizar";
				UtilSession.planificacionServicio.transCrearModificarOrdengasto(ordengastoTO,null);
				id=ordengastoTO.getId().toString();
				jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfig()));
			}
			//ordengasto linea
			else if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO = gson.fromJson(new StringReader(objeto), OrdengastolineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				OrdengastolineaTO ordengastolineaTO2=new OrdengastolineaTO();
				ordengastolineaTO2.setNivelactid(ordengastolineaTO.getNivelactid());
				ordengastolineaTO2.getId().setId(ordengastolineaTO.getId().getId());
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO2);
				if(ordengastolineaTOs.size()==0){
					accion = (ordengastolineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarOrdengastolinea(ordengastolineaTO);
					id=ordengastolineaTO.getId().toString();
					//Traigo la lista de ordengastolinea
					OrdengastolineaTO ordengastolineaTO3=new OrdengastolineaTO();
					ordengastolineaTO3.getId().setId(ordengastolineaTO.getId().getId());
					Collection<OrdengastolineaTO> ordengastolineaTOs2=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO3);
					jsonObject.put("ordengastolinea", (JSONArray)JSONSerializer.toJSON(ordengastolineaTOs2,ordengastolineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}
			
			//ordendevengo
			if(clase.equals("ordendevengo")){
				OrdendevengoTO ordendevengoTO = gson.fromJson(new StringReader(objeto), OrdendevengoTO.class);
				if(ordendevengoTO.getNpfechaaprobacion()!=null)
					ordendevengoTO.setFechaaprobacion(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechaaprobacion()));
				if(ordendevengoTO.getNpfechacreacion()!=null)
					ordendevengoTO.setFechacreacion(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechacreacion()));
				if(ordendevengoTO.getNpfechaeliminacion()!=null)
					ordendevengoTO.setFechaeliminacion(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechaeliminacion()));
				if(ordendevengoTO.getNpfechaanulacion()!=null)
					ordendevengoTO.setFechaanulacion(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechaanulacion()));
				if(ordendevengoTO.getNpfechanegacion()!=null)
					ordendevengoTO.setFechanegacion(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechanegacion()));
				if(ordendevengoTO.getNpfechasolicitud()!=null)
					ordendevengoTO.setFechasolicitud(UtilGeneral.parseStringToDate(ordendevengoTO.getNpfechasolicitud()));
				accion = (ordendevengoTO.getId()==null)?"crear":"actualizar";
				UtilSession.planificacionServicio.transCrearModificarOrdendevengo(ordendevengoTO, null);
				id=ordendevengoTO.getId().toString();
				jsonObject.put("ordendevengo", (JSONObject)JSONSerializer.toJSON(ordendevengoTO,ordendevengoTO.getJsonConfig()));
			}
			//ordendevengo linea
			else if(clase.equals("ordendevengolinea")){
				OrdendevengolineaTO ordendevengolineaTO = gson.fromJson(new StringReader(objeto), OrdendevengolineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				OrdendevengolineaTO ordendevengolineaTO2=new OrdendevengolineaTO();
				ordendevengolineaTO2.setNivelactid(ordendevengolineaTO.getNivelactid());
				ordendevengolineaTO2.getId().setId(ordendevengolineaTO.getId().getId());
				Collection<OrdendevengolineaTO> ordendevengolineaTOs=UtilSession.planificacionServicio.transObtenerOrdendevengolinea(ordendevengolineaTO2);
				if(ordendevengolineaTOs.size()==0){
					accion = (ordendevengolineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarOrdendevengolinea(ordendevengolineaTO);
					id=ordendevengolineaTO.getId().toString();
					//Traigo la lista de ordendevenolinea
					OrdendevengolineaTO ordendevengolineaTO3=new OrdendevengolineaTO();
					ordendevengolineaTO3.getId().setId(ordendevengolineaTO.getId().getId());
					Collection<OrdendevengolineaTO> ordendevengolineaTOs2=UtilSession.planificacionServicio.transObtenerOrdendevengolinea(ordendevengolineaTO3);
					jsonObject.put("ordendevengolinea", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs2,ordendevengolineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
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
	
	@RequestMapping(value = "/nuevo/{clase}/{id}", method = RequestMethod.GET)
	public Respuesta nuevo(@PathVariable String clase,@PathVariable Long id,HttpServletRequest request){
		log.println("entra al metodo nuevo: " + id);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Certificacion
			if(clase.equals("certificacion")){
				CertificacionTO certificacion=new CertificacionTO();
				certificacion.setCertificacionejerfiscalid(id);
				certificacion.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				//valores por defecto
				//Claseregistro
				ClaseregistrocmcgastoTO claseregistrocmcgastoTO=new ClaseregistrocmcgastoTO();
				claseregistrocmcgastoTO.setCodigo("OGA");
				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO=new ClaseregistroclasemodificacionTO();
				claseregistroclasemodificacionTO.setCodigo("NOR");
				claseregistroclasemodificacionTO.setClaseregistrocmejerfiscalid(id);
				ClaseregistroTO claseregistroTO=new ClaseregistroTO();
				claseregistroTO.setCodigo("COM");
				claseregistroTO.setClaseregistroejerfiscalid(id);
				claseregistroclasemodificacionTO.setClaseregistro(claseregistroTO);
				claseregistrocmcgastoTO.setClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
				claseregistrocmcgastoTO.setClaseregistrocmcgastoefid(id);
				Collection<ClaseregistrocmcgastoTO> claseregistrocmcgastoTOs=UtilSession.planificacionServicio.transObtenerClaseregistrocmcgasto(claseregistrocmcgastoTO);
				if(claseregistrocmcgastoTOs.size()>0){
					claseregistrocmcgastoTO=(ClaseregistrocmcgastoTO)claseregistrocmcgastoTOs.iterator().next();
					certificacion.setNpcodigoregcmcgasto(claseregistrocmcgastoTO.getCodigo());
					certificacion.setNpcodigoregistro(claseregistrocmcgastoTO.getClaseregistroclasemodificacion().getClaseregistro().getCodigo());
					certificacion.setNpcodigomodificacion(claseregistrocmcgastoTO.getClaseregistroclasemodificacion().getCodigo());
					certificacion.setNpnombreregcmcgasto(claseregistrocmcgastoTO.getNombre());
					certificacion.setNpnombreregistro(claseregistrocmcgastoTO.getClaseregistroclasemodificacion().getClaseregistro().getNombre());
					certificacion.setNpnombremodificacion(claseregistrocmcgastoTO.getClaseregistroclasemodificacion().getNombre());
					certificacion.setCertificacionclasemoid(claseregistrocmcgastoTO.getId().getCmid());
					certificacion.setCertificacionclaseregid(claseregistrocmcgastoTO.getId().getId());
					certificacion.setCertificaciongastoid(claseregistrocmcgastoTO.getId().getCmcgastoid());
				}
				//Tipodocumento
				TipodocumentoclasedocumentoTO tipodocumentoclasedocumentoTO=new TipodocumentoclasedocumentoTO();
				tipodocumentoclasedocumentoTO.setCodigo("01");
				tipodocumentoclasedocumentoTO.setTipodocumentoclasedocefid(id);
				TipodocumentoTO tipodocumentoTO=new TipodocumentoTO();
				tipodocumentoTO.setTipodocumentoejerfiscalid(id);
				tipodocumentoTO.setCodigo("06");
				tipodocumentoclasedocumentoTO.setTipodocumento(tipodocumentoTO);
				Collection<TipodocumentoclasedocumentoTO> tipodocumentoclasedocumentoTOs=UtilSession.adminsitracionServicio.transObtenerTipodocumentoclasedocumento(tipodocumentoclasedocumentoTO);
				if(tipodocumentoclasedocumentoTOs.size()>0){
					tipodocumentoclasedocumentoTO=(TipodocumentoclasedocumentoTO)tipodocumentoclasedocumentoTOs.iterator().next();
					certificacion.setNpcodigodocumento(tipodocumentoclasedocumentoTO.getTipodocumento().getCodigo());
					certificacion.setNpcodigotipodocumento(tipodocumentoclasedocumentoTO.getCodigo());
					certificacion.setNpnombredocumento(tipodocumentoclasedocumentoTO.getTipodocumento().getNombre());
					certificacion.setNpnombretipodocumento(tipodocumentoclasedocumentoTO.getNombre());
					certificacion.setCertificaciontipodocid(tipodocumentoclasedocumentoTO.getId().getId());
					certificacion.setCertificaciontpclasedocid(tipodocumentoclasedocumentoTO.getId().getClasedocid());
				}
				certificacion.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacion,certificacion.getJsonConfignuevo()));
			}
			//Certificacionlinea
			if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = new CertificacionlineaTO();
				certificacionlineaTO.getId().setId(id);
				jsonObject.put("certificacionlinea", (JSONObject)JSONSerializer.toJSON(certificacionlineaTO,certificacionlineaTO.getJsonConfig()));
			}
			//Ordengasto
			if(clase.equals("ordengasto")){
				OrdengastoTO  ordengastoTO=new OrdengastoTO();
				ordengastoTO.setOrdengastoejerfiscalid(id);
				ordengastoTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				ordengastoTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfignuevo()));
			}
			//Ordengastolinea
			if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(id);
				jsonObject.put("ordengastolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
			}
			//Ordendevengo
			if(clase.equals("ordendevengo")){
				OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
				ordendevengoTO.setOrdendevengoejerfiscalid(id);
				ordendevengoTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				ordendevengoTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("ordendevengo", (JSONObject)JSONSerializer.toJSON(ordendevengoTO,ordendevengoTO.getJsonConfignuevo()));
			}
			//Ordendevengolinea
			if(clase.equals("ordendevengolinea")){
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(id);
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
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
	
	@RequestMapping(value = "/{clase}/{id}/{id2}", method = RequestMethod.GET)
	public Respuesta editar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,HttpServletRequest request){
		log.println("entra al metodo recuperar: " + id);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Certificacion
			if(clase.equals("certificacion")){
				CertificacionTO certificacionTO=new CertificacionTO();
				certificacionTO = UtilSession.planificacionServicio.transObtenerCertificacionTO(id);
				if(certificacionTO.getCertificacionclasemoid()!=null){
					certificacionTO.setNpcodigoregcmcgasto(certificacionTO.getClaseregistrocmcgasto().getCodigo());
					certificacionTO.setNpcodigoregistro(certificacionTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getClaseregistro().getCodigo());
					certificacionTO.setNpcodigomodificacion(certificacionTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getCodigo());
					certificacionTO.setNpnombreregcmcgasto(certificacionTO.getClaseregistrocmcgasto().getNombre());
					certificacionTO.setNpnombreregistro(certificacionTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getClaseregistro().getNombre());
					certificacionTO.setNpnombremodificacion(certificacionTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getNombre());
				}
				if(certificacionTO.getCertificaciontipodocid()!=null){
					certificacionTO.setNpcodigodocumento(certificacionTO.getTipodocumentoclasedocumento().getTipodocumento().getCodigo());
					certificacionTO.setNpcodigotipodocumento(certificacionTO.getTipodocumentoclasedocumento().getCodigo());
					certificacionTO.setNpnombredocumento(certificacionTO.getTipodocumentoclasedocumento().getTipodocumento().getNombre());
					certificacionTO.setNpnombretipodocumento(certificacionTO.getTipodocumentoclasedocumento().getNombre());
					certificacionTO.setCertificaciontipodocid(certificacionTO.getTipodocumentoclasedocumento().getId().getId());
					certificacionTO.setCertificaciontpclasedocid(certificacionTO.getTipodocumentoclasedocumento().getId().getClasedocid());
				}
				//asigno las fechas
				certificacionTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(certificacionTO.getFechaaprobacion()));
				certificacionTO.setNpfechacreacion(UtilGeneral.parseDateToString(certificacionTO.getFechacreacion()));
				certificacionTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(certificacionTO.getFechaeliminacion()));
				certificacionTO.setNpfechaliquidacion(UtilGeneral.parseDateToString(certificacionTO.getFechaliquidacion()));
				certificacionTO.setNpfechanegacion(UtilGeneral.parseDateToString(certificacionTO.getFechanegacion()));
				certificacionTO.setNpfechasolicitud(UtilGeneral.parseDateToString(certificacionTO.getFechasolicitud()));
				//traigo las certificacionlinea las traigo
				CertificacionlineaTO certificacionlineaTO=new CertificacionlineaTO();
				certificacionlineaTO.getId().setId(certificacionTO.getId());
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO);
				jsonObject.put("certificacionlineas", (JSONArray)JSONSerializer.toJSON(certificacionlineaTOs,certificacionlineaTO.getJsonConfig()));
				jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
			}
			//Certificacionlinea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = UtilSession.planificacionServicio.transObtenerCertificacionlineaTO(new CertificacionlineaID(id, id2));
				certificacionlineaTO.setNpvalor(certificacionlineaTO.getValor());
				jsonObject.put("certificacionlinea", (JSONObject)JSONSerializer.toJSON(certificacionlineaTO,certificacionlineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(certificacionlineaTO.getNivelactid(), jsonObject, mensajes);
			}
			//Ordengasto
			if(clase.equals("ordengasto")){
				OrdengastoTO ordengastoTO=new OrdengastoTO();
				ordengastoTO = UtilSession.planificacionServicio.transObtenerOrdengastoTO(id);
				if(ordengastoTO.getOrdengastoclasemodid()!=null){
					ordengastoTO.setNpcodigoregcmcgasto(ordengastoTO.getClaseregistrocmcgasto().getCodigo());
					ordengastoTO.setNpcodigoregistro(ordengastoTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getClaseregistro().getCodigo());
					ordengastoTO.setNpcodigomodificacion(ordengastoTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getCodigo());
					ordengastoTO.setNpnombreregcmcgasto(ordengastoTO.getClaseregistrocmcgasto().getNombre());
					ordengastoTO.setNpnombreregistro(ordengastoTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getClaseregistro().getNombre());
					ordengastoTO.setNpnombremodificacion(ordengastoTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getNombre());
				}
				if(ordengastoTO.getOrdengastotipodocid()!=null){
					ordengastoTO.setNpcodigodocumento(ordengastoTO.getTipodocumentoclasedocumento().getTipodocumento().getCodigo());
					ordengastoTO.setNpcodigotipodocumento(ordengastoTO.getTipodocumentoclasedocumento().getCodigo());
					ordengastoTO.setNpnombredocumento(ordengastoTO.getTipodocumentoclasedocumento().getTipodocumento().getNombre());
					ordengastoTO.setNpnombretipodocumento(ordengastoTO.getTipodocumentoclasedocumento().getNombre());
					ordengastoTO.setOrdengastotipodocid(ordengastoTO.getTipodocumentoclasedocumento().getId().getId());
					ordengastoTO.setOrdengastoclasemodid(ordengastoTO.getTipodocumentoclasedocumento().getId().getClasedocid());
				}
				//asigno las fechas
				ordengastoTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(ordengastoTO.getFechaaprobacion()));
				ordengastoTO.setNpfechacreacion(UtilGeneral.parseDateToString(ordengastoTO.getFechacreacion()));
				ordengastoTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(ordengastoTO.getFechaeliminacion()));
				ordengastoTO.setNpfechaanulacion(UtilGeneral.parseDateToString(ordengastoTO.getFechaanulacion()));
				ordengastoTO.setNpfechanegacion(UtilGeneral.parseDateToString(ordengastoTO.getFechanegacion()));
				ordengastoTO.setNpfechasolicitud(UtilGeneral.parseDateToString(ordengastoTO.getFechasolicitud()));
				//traigo las ordeneslineas las traigo
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(ordengastoTO.getId());
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
				jsonObject.put("ordengastolineas", (JSONArray)JSONSerializer.toJSON(ordengastolineaTOs,ordengastolineaTO.getJsonConfig()));
				jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfig()));
			}
			//Ordengastolinea
			else if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO = UtilSession.planificacionServicio.transObtenerOrdengastolineaTO(new OrdengastolineaID(id, id2));
				ordengastolineaTO.setNpvalor(ordengastolineaTO.getValor());
				jsonObject.put("ordengastolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordengastolineaTO.getNivelactid(), jsonObject, mensajes);
			}
			//Ordendevengo
			if(clase.equals("ordendevengo")){
				OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
				ordendevengoTO = UtilSession.planificacionServicio.transObtenerOrdendevengoTO(id);
				if(ordendevengoTO.getOrdendevengoordengastoid()!=null){
					ordendevengoTO.setNpordengastoedit(ordendevengoTO.getOrdengasto().getCodigo());
					ordendevengoTO.setNpordengastovalor(ordendevengoTO.getOrdengasto().getValortotal());
				}
				//asigno las fechas
				ordendevengoTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(ordendevengoTO.getFechaaprobacion()));
				ordendevengoTO.setNpfechacreacion(UtilGeneral.parseDateToString(ordendevengoTO.getFechacreacion()));
				ordendevengoTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(ordendevengoTO.getFechaeliminacion()));
				ordendevengoTO.setNpfechaanulacion(UtilGeneral.parseDateToString(ordendevengoTO.getFechaanulacion()));
				ordendevengoTO.setNpfechanegacion(UtilGeneral.parseDateToString(ordendevengoTO.getFechanegacion()));
				ordendevengoTO.setNpfechasolicitud(UtilGeneral.parseDateToString(ordendevengoTO.getFechasolicitud()));
				//traigo las ordeneslineas las traigo
				OrdendevengolineaTO ordendevengolineaTO=new OrdendevengolineaTO();
				ordendevengolineaTO.getId().setId(ordendevengoTO.getId());
				Collection<OrdendevengolineaTO> ordendevengolineaTOs=UtilSession.planificacionServicio.transObtenerOrdendevengolinea(ordendevengolineaTO);
				jsonObject.put("ordendevengolineas", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs,ordendevengoTO.getJsonConfig()));
				jsonObject.put("ordendevengo", (JSONObject)JSONSerializer.toJSON(ordendevengoTO,ordendevengoTO.getJsonConfig()));
			}
			//Ordendevengolinea
			else if(clase.equals("ordendevengolinea")){
				OrdendevengolineaTO ordendevengolineaTO = UtilSession.planificacionServicio.transObtenerOrdendevengolineaTO(new OrdendevengolineaID(id, id2));
				ordendevengolineaTO.setNpvalor(ordendevengolineaTO.getValor());
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordendevengolineaTO,ordendevengolineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordendevengolineaTO.getNivelactid(), jsonObject, mensajes);
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
	
	@RequestMapping(value = "/flujo/{id}/{tipo}", method = RequestMethod.GET)
	public Respuesta flujo(@PathVariable Long id,@PathVariable String tipo,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			CertificacionTO certificacionTO=UtilSession.planificacionServicio.transObtenerCertificacionTO(id);
			certificacionTO.setEstado(tipo);
			UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
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
	
	@RequestMapping(value = "/flujoordenes/{id}/{tipo}/{cur}", method = RequestMethod.GET)
	public Respuesta flujoordenes(@PathVariable Long id,@PathVariable String tipo,@PathVariable String cur,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			OrdengastoTO ordengastoTO=UtilSession.planificacionServicio.transObtenerOrdengastoTO(id);
			ordengastoTO.setEstado(tipo);
			if(!cur.equals("0"))
				ordengastoTO.setCur(cur);
			UtilSession.planificacionServicio.transCrearModificarOrdengasto(ordengastoTO,tipo);
			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
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
	
	@RequestMapping(value = "/flujodevengo/{id}/{tipo}", method = RequestMethod.GET)
	public Respuesta flujoordenes(@PathVariable Long id,@PathVariable String tipo,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			OrdendevengoTO ordendevengoTO=UtilSession.planificacionServicio.transObtenerOrdendevengoTO(id);
			ordendevengoTO.setEstado(tipo);
			UtilSession.planificacionServicio.transCrearModificarOrdendevengo(ordendevengoTO, tipo);
			//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
			mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
			mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
		if(mensajes.getMsg()!=null){
			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
			log.println("existen mensajes");
		}
		log.println("devuelve**** " + jsonObject.toString());
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
			
			//Certificacion
			if(clase.equals("certificacion")){
				jsonObject=ConsultasUtil.consultaCertificacionPaginado(parameters, jsonObject, mensajes);
			}
			
			//Subitemunidad
			if(clase.equals("subitemunidad")){
				jsonObject=ConsultasUtil.consultaListasubitemunidad(parameters, jsonObject, mensajes);
			}

			//Sibiteminfo
			if(clase.equals("subitemunidadinfo")){
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(Long.valueOf(parameters.get("nivelactividad")), jsonObject, mensajes);
			}
			
			//Subtareainfo
			if(clase.equals("subitareainfo")){
				jsonObject=ConsultasUtil.consultaInformacionsubtarea(Long.valueOf(parameters.get("nivelactividad")), jsonObject, mensajes);
			}

			//Orden de gasto
			if(clase.equals("ordengasto")){
				jsonObject=ConsultasUtil.consultaOrdengastoPaginado(parameters, jsonObject, mensajes);
			}
			
			//Orden de devengo
			if(clase.equals("ordendevengo")){
				jsonObject=ConsultasUtil.consultaOrdendevengoPaginado(parameters, jsonObject, mensajes);
			}
			
			//Certificacion busqueda
			if(clase.equals("certificacionbusqueda")){
				jsonObject=ConsultasUtil.consultaCertificacionBusquedaPaginado(parameters, jsonObject, mensajes);
			}
			
			//Ordengasto busqueda
			if(clase.equals("ordengastobusqueda")){
				jsonObject=ConsultasUtil.consultaOrdengastoBusquedaPaginado(parameters, jsonObject, mensajes);
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
	
	

	
}
