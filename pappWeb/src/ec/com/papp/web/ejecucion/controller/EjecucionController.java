package ec.com.papp.web.ejecucion.controller;

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

import ec.com.papp.administracion.to.ClaseregistroTO;
import ec.com.papp.administracion.to.ClaseregistroclasemodificacionTO;
import ec.com.papp.administracion.to.ItemTO;
import ec.com.papp.administracion.to.SocionegocioTO;
import ec.com.papp.administracion.to.TipodocumentoTO;
import ec.com.papp.administracion.to.TipodocumentoclasedocumentoTO;
import ec.com.papp.administracion.to.UnidadmedidaTO;
import ec.com.papp.planificacion.id.CertificacionlineaID;
import ec.com.papp.planificacion.id.OrdendevengolineaID;
import ec.com.papp.planificacion.id.OrdengastolineaID;
import ec.com.papp.planificacion.id.OrdenreversionlineaID;
import ec.com.papp.planificacion.id.ReformalineaID;
import ec.com.papp.planificacion.id.ReformametalineaID;
import ec.com.papp.planificacion.to.CertificacionTO;
import ec.com.papp.planificacion.to.CertificacionlineaTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.ContratoTO;
import ec.com.papp.planificacion.to.ItemunidadTO;
import ec.com.papp.planificacion.to.NivelactividadTO;
import ec.com.papp.planificacion.to.OrdendevengoTO;
import ec.com.papp.planificacion.to.OrdendevengolineaTO;
import ec.com.papp.planificacion.to.OrdengastoTO;
import ec.com.papp.planificacion.to.OrdengastolineaTO;
import ec.com.papp.planificacion.to.OrdenreversionTO;
import ec.com.papp.planificacion.to.OrdenreversionlineaTO;
import ec.com.papp.planificacion.to.ReformaTO;
import ec.com.papp.planificacion.to.ReformalineaTO;
import ec.com.papp.planificacion.to.ReformametaTO;
import ec.com.papp.planificacion.to.ReformametalineaTO;
import ec.com.papp.planificacion.to.SubitemunidadacumuladorTO;
import ec.com.papp.planificacion.to.SubtareaunidadTO;
import ec.com.papp.planificacion.to.SubtareaunidadacumuladorTO;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.ejecucion.util.ConsultasUtil;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

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
				id=certificacionTO.getNpid().toString();
				certificacionTO.setId(certificacionTO.getNpid());
				jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
			}
			//certificacion linea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = gson.fromJson(new StringReader(objeto), CertificacionlineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				CertificacionlineaTO certificacionlineaTO2=new CertificacionlineaTO();
				log.println("consulta lineas: " + certificacionlineaTO.getNivelactid() + certificacionlineaTO.getId().getId());
				certificacionlineaTO2.setNivelactid(certificacionlineaTO.getNivelactid());
				certificacionlineaTO2.getId().setId(certificacionlineaTO.getId().getId());
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO2);
				log.println("certificaciones: " + certificacionlineaTOs.size());
				boolean grabar=true;
				if(certificacionlineaTOs.size()>0){
					for(CertificacionlineaTO certificacionlineaTO3:certificacionlineaTOs) {
						if((certificacionlineaTO.getId().getId()!=null && certificacionlineaTO.getId().getId().longValue()!=0) && certificacionlineaTO3.getId().getId().longValue()!=certificacionlineaTO.getId().getId().longValue()) {
							grabar=false;
							break;
						}
						else if(certificacionlineaTO.getId().getClass()==null) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					accion = (certificacionlineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarCertificacionlinea(certificacionlineaTO);
					id=certificacionlineaTO.getId().getId().toString() + certificacionlineaTO.getId().getLineaid();
					//Traiga la lista de cetificacionlinea
					ConsultasUtil.obtenercertificacion(certificacionlineaTO.getId().getId(), jsonObject);
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
				id=ordengastoTO.getNpid().toString();
				ordengastoTO.setId(ordengastoTO.getNpid());
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
					id=ordengastolineaTO.getId().getId().toString()+ ordengastolineaTO.getId().getLineaid();
					//Traigo la lista de ordengastolinea
					OrdengastolineaTO ordengastolineaTO3=new OrdengastolineaTO();
					ordengastolineaTO3.getId().setId(ordengastolineaTO.getId().getId());
					OrdengastoTO ordengastoTO=UtilSession.planificacionServicio.transObtenerOrdengastoTO(ordengastolineaTO.getId().getId());
					ordengastolineaTO3.setOrdengasto(ordengastoTO);
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
				id=ordendevengoTO.getNpid().toString();
				ordendevengoTO.setId(ordendevengoTO.getNpid());
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
					id=ordendevengolineaTO.getId().getId().toString()+ordendevengolineaTO.getId().getLineaid();
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

			//ordenreversion
			if(clase.equals("ordenreversion")){
				OrdenreversionTO ordenreversionTO = gson.fromJson(new StringReader(objeto), OrdenreversionTO.class);
				if(ordenreversionTO.getNpfechaaprobacion()!=null)
					ordenreversionTO.setFechaaprobacion(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechaaprobacion()));
				if(ordenreversionTO.getNpfechacreacion()!=null)
					ordenreversionTO.setFechacreacion(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechacreacion()));
				if(ordenreversionTO.getNpfechaeliminacion()!=null)
					ordenreversionTO.setFechaeliminacion(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechaeliminacion()));
				if(ordenreversionTO.getNpfechaanulacion()!=null)
					ordenreversionTO.setFechaanulacion(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechaanulacion()));
				if(ordenreversionTO.getNpfechanegacion()!=null)
					ordenreversionTO.setFechanegacion(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechanegacion()));
				if(ordenreversionTO.getNpfechasolicitud()!=null)
					ordenreversionTO.setFechasolicitud(UtilGeneral.parseStringToDate(ordenreversionTO.getNpfechasolicitud()));
				accion = (ordenreversionTO.getId()==null)?"crear":"actualizar";
				UtilSession.planificacionServicio.transCrearModificarOrdenreversion(ordenreversionTO, null);
				id=ordenreversionTO.getNpid().toString();
				ordenreversionTO.setId(ordenreversionTO.getNpid());
				jsonObject.put("ordenreversion", (JSONObject)JSONSerializer.toJSON(ordenreversionTO,ordenreversionTO.getJsonConfig()));
			}
			//ordenreversion linea
			else if(clase.equals("ordenreversionlinea")){
				OrdenreversionlineaTO ordenreversionlineaTO = gson.fromJson(new StringReader(objeto), OrdenreversionlineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				OrdenreversionlineaTO ordenreversionlineaTO2=new OrdenreversionlineaTO();
				ordenreversionlineaTO2.setNivelactid(ordenreversionlineaTO.getNivelactid());
				ordenreversionlineaTO2.getId().setId(ordenreversionlineaTO.getId().getId());
				Collection<OrdenreversionlineaTO> ordenreversionlineaTOs=UtilSession.planificacionServicio.transObtenerOrdenreversionlinea(ordenreversionlineaTO2);
				if(ordenreversionlineaTOs.size()==0){
					accion = (ordenreversionlineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarOrdenreversionlinea(ordenreversionlineaTO);
					id=ordenreversionlineaTO.getId().getId().toString()+ordenreversionlineaTO.getId().getLineaid();
					//Traigo la lista de ordenreversionlinea
					OrdenreversionlineaTO ordenreversionlineaTO3=new OrdenreversionlineaTO();
					ordenreversionlineaTO3.getId().setId(ordenreversionlineaTO.getId().getId());
					Collection<OrdenreversionlineaTO> ordendevengolineaTOs2=UtilSession.planificacionServicio.transObtenerOrdenreversionlinea(ordenreversionlineaTO3);
					jsonObject.put("ordenreversionlinea", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs2,ordenreversionlineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//Ver Contrato
			else if(clase.equals("vercontrato")){
				OrdengastoTO ordengastoTO = gson.fromJson(new StringReader(objeto), OrdengastoTO.class);
				//Valido que sea un tipo de documento contrato para que permita ingresar contratos
				log.println("busca la palabra contrato: " + ordengastoTO.getNpnombretipodocumento().indexOf("CONTRATO") + "-" + ordengastoTO.getNpnombretipodocumento().indexOf("contrato")+"-"+ordengastoTO.getNpnombretipodocumento().indexOf("Contrato"));
		        if(ordengastoTO.getNpnombretipodocumento().indexOf("CONTRATO")!=-1 || ordengastoTO.getNpnombretipodocumento().indexOf("contrato")!=-1 ||ordengastoTO.getNpnombretipodocumento().indexOf("Contrato")!=-1) {
					ContratoTO contratoTO=new ContratoTO();
					log.println("datos proveedor " + ordengastoTO.getNpproveedornombre());
					if(ordengastoTO.getOrdengastocontratoid()!=null && ordengastoTO.getOrdengastocontratoid().longValue()!=0) {
						contratoTO=UtilSession.planificacionServicio.transObtenerContratoTO(ordengastoTO.getOrdengastocontratoid());
						contratoTO.setSocionegocio(new SocionegocioTO());
						if(contratoTO!=null && (contratoTO.getId()!=null || contratoTO.getId().longValue()!=0)) {
							if(contratoTO.getNpfechainicio()!=null)
								contratoTO.setFechainicio(UtilGeneral.parseStringToDate(contratoTO.getNpfechainicio()));
							if(contratoTO.getContratoproveedorid()!=null) {
								contratoTO.setNpproveedor(contratoTO.getSocionegocio().getNombremostrado());
								contratoTO.setNpproveedorcodigo(contratoTO.getSocionegocio().getCodigo());
							}
						}
						else {
							contratoTO=new ContratoTO();
							if(ordengastoTO.getOrdengastoproveedorid()!=null) {
								contratoTO.setContratoproveedorid(ordengastoTO.getOrdengastoproveedorid());
								contratoTO.setNpproveedor(ordengastoTO.getNpproveedornombre());
								contratoTO.setNpproveedorcodigo(ordengastoTO.getNpproveedorcodigo());
								contratoTO.setEstado(MensajesAplicacion.getString("estado.activo"));
							}
						}
					}
					else {
						contratoTO=new ContratoTO();
						contratoTO.setContratoproveedorid(ordengastoTO.getOrdengastoproveedorid());
						contratoTO.setNpproveedor(ordengastoTO.getNpproveedornombre());
						contratoTO.setNpproveedorcodigo(ordengastoTO.getNpproveedorcodigo());
						contratoTO.setEstado(MensajesAplicacion.getString("estado.activo"));
					}
					jsonObject.put("contrato", (JSONObject)JSONSerializer.toJSON(contratoTO,contratoTO.getJsonConfigedicion()));
		        }
		        else {
					mensajes.setMsg("Para acceder a la opcion de contrato la clase de documento debe ser CONTRATO");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
		        }
			}

			//reforma
			if(clase.equals("reforma")){
				ReformaTO reformaTO = gson.fromJson(new StringReader(objeto), ReformaTO.class);
				accion = (reformaTO.getId()==null)?"crear":"actualizar";
				if(reformaTO.getNpfechaaprobacion()!=null)
					reformaTO.setFechaaprobacion(UtilGeneral.parseStringToDate(reformaTO.getNpfechaaprobacion()));
				if(reformaTO.getNpfechacreacion()!=null)
					reformaTO.setFechacreacion(UtilGeneral.parseStringToDate(reformaTO.getNpfechacreacion()));
				if(reformaTO.getNpfechaeliminacion()!=null)
					reformaTO.setFechaeliminacion(UtilGeneral.parseStringToDate(reformaTO.getNpfechaeliminacion()));
				if(reformaTO.getNpfechanegacion()!=null)
					reformaTO.setFechanegacion(UtilGeneral.parseStringToDate(reformaTO.getNpfechanegacion()));
				if(reformaTO.getNpfechasolicitud()!=null)
					reformaTO.setFechasolicitud(UtilGeneral.parseStringToDate(reformaTO.getNpfechasolicitud()));
				UtilSession.planificacionServicio.transCrearModificarReforma(reformaTO,null);
				id=reformaTO.getNpid().toString();
				reformaTO.setId(reformaTO.getNpid());
				jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfig()));
			}
			
			//reforma linea
			else if(clase.equals("reformalinea")){
				ReformalineaTO reformalineaTO = gson.fromJson(new StringReader(objeto), ReformalineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				ReformalineaTO reformalineaTO2=new ReformalineaTO();
				log.println("consulta lineas: " + reformalineaTO.getNivelactid() + reformalineaTO.getId().getId());
				reformalineaTO2.setNivelactid(reformalineaTO.getNivelactid());
				reformalineaTO2.getId().setId(reformalineaTO.getId().getId());
				Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO2);
				log.println("reformas: " + reformalineaTOs.size());
				if(reformalineaTOs.size()==0){
					accion = (reformalineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarReformalinea(reformalineaTO);
					id=reformalineaTO.getId().getId().toString() + reformalineaTO.getId().getLineaid();
					//Traigo la lista de ordengastolinea
					ReformalineaTO reformalineaTO3=new ReformalineaTO();
					reformalineaTO3.getId().setId(reformalineaTO.getId().getId());
					Collection<ReformalineaTO> reformalineaTOs2=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO3);
					jsonObject.put("reformalinea", (JSONArray)JSONSerializer.toJSON(reformalineaTOs2,reformalineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//reformameta
			if(clase.equals("reformameta")){
				ReformametaTO reformametaTO = gson.fromJson(new StringReader(objeto), ReformametaTO.class);
				accion = (reformametaTO.getId()==null)?"crear":"actualizar";
				if(reformametaTO.getNpfechaaprobacion()!=null)
					reformametaTO.setFechaaprobacion(UtilGeneral.parseStringToDate(reformametaTO.getNpfechaaprobacion()));
				if(reformametaTO.getNpfechacreacion()!=null)
					reformametaTO.setFechacreacion(UtilGeneral.parseStringToDate(reformametaTO.getNpfechacreacion()));
				if(reformametaTO.getNpfechaeliminacion()!=null)
					reformametaTO.setFechaeliminacion(UtilGeneral.parseStringToDate(reformametaTO.getNpfechaeliminacion()));
				if(reformametaTO.getNpfechanegacion()!=null)
					reformametaTO.setFechanegacion(UtilGeneral.parseStringToDate(reformametaTO.getNpfechanegacion()));
				if(reformametaTO.getNpfechasolicitud()!=null)
					reformametaTO.setFechasolicitud(UtilGeneral.parseStringToDate(reformametaTO.getNpfechasolicitud()));
				UtilSession.planificacionServicio.transCrearModificarReformameta(reformametaTO,null);
				id=reformametaTO.getNpid().toString();
				reformametaTO.setId(reformametaTO.getNpid());
				jsonObject.put("reformameta", (JSONObject)JSONSerializer.toJSON(reformametaTO,reformametaTO.getJsonConfig()));
			}
			
			//reforma meta linea
			else if(clase.equals("reformametalinea")){
				ReformametalineaTO reformametalineaTO = gson.fromJson(new StringReader(objeto), ReformametalineaTO.class);
				//pregunto si ya tiene una linea con el mismo subtarea y no le dejo
				ReformametalineaTO reformametalineaTO2=new ReformametalineaTO();
				log.println("consulta lineas: " + reformametalineaTO.getNivelacid() + reformametalineaTO.getId().getId());
				reformametalineaTO2.setNivelacid(reformametalineaTO2.getNivelacid());
				reformametalineaTO2.getId().setId(reformametalineaTO2.getId().getId());
				Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO2);
				log.println("reformas: " + reformametalineaTOs.size());
				if(reformametalineaTOs.size()==0){
					accion = (reformametalineaTO.getId()==null)?"crear":"actualizar";
					UtilSession.planificacionServicio.transCrearModificarReformametalinea(reformametalineaTO);
					id=reformametalineaTO.getId().getId().toString() + reformametalineaTO.getId().getLineaid();
					//Traigo la lista de reformametalinea
					ReformametalineaTO reformametalineaTO3=new ReformametalineaTO();
					reformametalineaTO3.getId().setId(reformametalineaTO.getId().getId());
					Collection<ReformametalineaTO> reformametalineaTOs2=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO3);
					jsonObject.put("reformametalinea", (JSONArray)JSONSerializer.toJSON(reformametalineaTOs2,reformametalineaTO.getJsonConfig()));
				}
				else{
					mensajes.setMsg(MensajesWeb.getString("advertencia.subtarealinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//Registro la auditoria
//			if(mensajes.getMsg()==null && !clase.equals("vercontrato"))
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
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = new CertificacionlineaTO();
				certificacionlineaTO.getId().setId(id);
				jsonObject.put("certificacionlinea", (JSONObject)JSONSerializer.toJSON(certificacionlineaTO,certificacionlineaTO.getJsonConfig()));
			}
			//Ordengasto
			else if(clase.equals("ordengasto")){
				OrdengastoTO  ordengastoTO=new OrdengastoTO();
				ordengastoTO.setOrdengastoejerfiscalid(id);
				ordengastoTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				ordengastoTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfignuevo()));
			}
			//Ordengastolinea
			else if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(id);
				jsonObject.put("ordengastolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
			}
			//Ordendevengo
			else if(clase.equals("ordendevengo")){
				OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
				ordendevengoTO.setOrdendevengoejerfiscalid(id);
				ordendevengoTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				ordendevengoTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("ordendevengo", (JSONObject)JSONSerializer.toJSON(ordendevengoTO,ordendevengoTO.getJsonConfignuevo()));
			}
			//Ordendevengolinea
			else if(clase.equals("ordendevengolinea")){
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(id);
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
			}
			//Ordendreversion
			else if(clase.equals("ordenreversion")){
				OrdenreversionTO ordenreversionTO=new OrdenreversionTO();
				ordenreversionTO.setOrdenreversionejerfiscalid(id);
				ordenreversionTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				ordenreversionTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("ordenreversion", (JSONObject)JSONSerializer.toJSON(ordenreversionTO,ordenreversionTO.getJsonConfignuevo()));
			}
			//Ordendevengolinea
			else if(clase.equals("ordenreversionlinea")){
				OrdenreversionlineaTO ordenreversionlineaTO=new OrdenreversionlineaTO();
				ordenreversionlineaTO.getId().setId(id);
				jsonObject.put("ordenreversionlinea", (JSONObject)JSONSerializer.toJSON(ordenreversionlineaTO,ordenreversionlineaTO.getJsonConfig()));
			}
			//Reforma
			else if(clase.equals("reforma")){
				ReformaTO reformaTO=new ReformaTO();
				reformaTO.setReformaejerfiscalid(id);
				reformaTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				reformaTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfignuevo()));
			}
			//Reformalinea
			else if(clase.equals("reformalinea")){
				ReformalineaTO reformalineaTO=new ReformalineaTO();
				reformalineaTO.getId().setId(id);
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformalineaTO,reformalineaTO.getJsonConfig()));
			}

			//Reformameta
			else if(clase.equals("reformameta")){
				ReformametaTO reformametaTO=new ReformametaTO();
				reformametaTO.setEjerfiscalid(id);
				reformametaTO.setNpfechacreacion(UtilGeneral.parseDateToString(new Date()));
				reformametaTO.setEstado(MensajesAplicacion.getString("certificacion.estado.registrado"));
				jsonObject.put("reformameta", (JSONObject)JSONSerializer.toJSON(reformametaTO,reformametaTO.getJsonConfignuevo()));
			}
			//Reformalinea
			else if(clase.equals("reformametalinea")){
				ReformalineaTO reformalineaTO=new ReformalineaTO();
				reformalineaTO.getId().setId(id);
				jsonObject.put("reformametalinea", (JSONObject)JSONSerializer.toJSON(reformalineaTO,reformalineaTO.getJsonConfig()));
			}

			log.println("json retornado: " + jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al obtener para editar");
			mensajes.setMsg("Error al intentar crear un nuevo registro");
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
				ConsultasUtil.obtenercertificacion(id, jsonObject);
			}
			//Certificacionlinea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = UtilSession.planificacionServicio.transObtenerCertificacionlineaTO(new CertificacionlineaID(id, id2));
				certificacionlineaTO.setNpvalor(certificacionlineaTO.getValor());
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(certificacionlineaTO.getNivelactid(), jsonObject, mensajes);
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(certificacionlineaTO.getNivelactid()));
				//1. traigo el total disponible del subitem
				double total=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid());
				//2. Obtengo el detalle del subitem
//				SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				double saldo=ConsultasUtil.obtenersaldodisponible(total, nivelactividadTO.getTablarelacionid(),certificacionlineaTO.getNivelactid());
				certificacionlineaTO.setNpvalorinicial(saldo);
				jsonObject.put("certificacionlinea", (JSONObject)JSONSerializer.toJSON(certificacionlineaTO,certificacionlineaTO.getJsonConfig()));
			}
			//Ordengasto
			else if(clase.equals("ordengasto")){
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
				if(ordengastoTO.getOrdengastoproveedorid()!=null) {
					ordengastoTO.setNpproveedorcodigo(ordengastoTO.getSocionegocio3().getCodigo());
					ordengastoTO.setNpproveedornombre(ordengastoTO.getSocionegocio3().getNombremostrado());
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
				ordengastolineaTO.setOrdengasto(ordengastoTO);
				ordengastolineaTO.getId().setId(ordengastoTO.getId());
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
				log.println("lineas "+ ordengastolineaTOs.size());
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
			else if(clase.equals("ordendevengo")){
				OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
				ordendevengoTO = UtilSession.planificacionServicio.transObtenerOrdendevengoTO(id);
				log.println("orden de gasto atada: " + ordendevengoTO.getOrdendevengoordengastoid());
				if(ordendevengoTO.getOrdendevengoordengastoid()!=null){
					ordendevengoTO.setNpordengastoedit(ordendevengoTO.getOrdengasto().getCodigo());
					log.println("valor del gasto " + ordendevengoTO.getOrdengasto().getValortotal());
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
				jsonObject.put("ordendevengolineas", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs,ordendevengolineaTO.getJsonConfigconsulta()));
				jsonObject.put("ordendevengo", (JSONObject)JSONSerializer.toJSON(ordendevengoTO,ordendevengoTO.getJsonConfig()));
			}
			//Ordendevengolinea
			else if(clase.equals("ordendevengolinea")){
				OrdendevengolineaTO ordendevengolineaTO = UtilSession.planificacionServicio.transObtenerOrdendevengolineaTO(new OrdendevengolineaID(id, id2));
				ordendevengolineaTO.setNpvalor(ordendevengolineaTO.getValor());
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordendevengolineaTO,ordendevengolineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordendevengolineaTO.getNivelactid(), jsonObject, mensajes);
			}
			//Ordenreversion
			else if(clase.equals("ordenreversion")){
				OrdenreversionTO ordenreversionTO=new OrdenreversionTO();
				ordenreversionTO = UtilSession.planificacionServicio.transObtenerOrdenreversionTO(id);
				if(ordenreversionTO.getOrdenrversionogastoid()!=null){
					ordenreversionTO.setNpordengastoedit(ordenreversionTO.getOrdengasto().getCodigo());
					ordenreversionTO.setNpordengastovalor(ordenreversionTO.getOrdengasto().getValortotal());
				}
				//asigno las fechas
				ordenreversionTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(ordenreversionTO.getFechaaprobacion()));
				ordenreversionTO.setNpfechacreacion(UtilGeneral.parseDateToString(ordenreversionTO.getFechacreacion()));
				ordenreversionTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(ordenreversionTO.getFechaeliminacion()));
				ordenreversionTO.setNpfechaanulacion(UtilGeneral.parseDateToString(ordenreversionTO.getFechaanulacion()));
				ordenreversionTO.setNpfechanegacion(UtilGeneral.parseDateToString(ordenreversionTO.getFechanegacion()));
				ordenreversionTO.setNpfechasolicitud(UtilGeneral.parseDateToString(ordenreversionTO.getFechasolicitud()));
				//traigo las ordeneslineas las traigo
				OrdenreversionlineaTO ordenreversionlineaTO=new OrdenreversionlineaTO();
				ordenreversionlineaTO.getId().setId(ordenreversionTO.getId());
				Collection<OrdenreversionlineaTO> ordendevengolineaTOs=UtilSession.planificacionServicio.transObtenerOrdenreversionlinea(ordenreversionlineaTO);
				jsonObject.put("ordenreversionlineas", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs,ordenreversionTO.getJsonConfig()));
				jsonObject.put("ordenreversion", (JSONObject)JSONSerializer.toJSON(ordenreversionTO,ordenreversionTO.getJsonConfig()));
			}
			//Ordenreversionlinea
			else if(clase.equals("ordenreversionlinea")){
				OrdenreversionlineaTO ordenreversionlineaTO = UtilSession.planificacionServicio.transObtenerOrdenreversionlineaTO(new OrdenreversionlineaID(id, id2));
				ordenreversionlineaTO.setNpvalor(ordenreversionlineaTO.getValor());
				jsonObject.put("ordenreversionlinea", (JSONObject)JSONSerializer.toJSON(ordenreversionlineaTO,ordenreversionlineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordenreversionlineaTO.getNivelactid(), jsonObject, mensajes);
			}
			//subitemacumulador: Recibo el id del subitem y traigo el valor disponible eso es para Certificacion de fondos
			else if(clase.equals("valordisponiblesi")) {
				//1. traigo el total disponible del subitem
				double total=ConsultasUtil.obtenertotalsubitem(id);
				//2. Obtengo el detalle del subitem
//				SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				double saldo=ConsultasUtil.obtenersaldodisponible(total, id,id2);
//				//2. traigo todas las certificaciones para saber cuanto es el saldo disponible
//				double valorcertificacion=0.0;
//				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtienecertificacionesnoeliminadas(id);
//				for(CertificacionlineaTO certificacionlineaTO:certificacionlineaTOs)
//					valorcertificacion=valorcertificacion+certificacionlineaTO.getValor().doubleValue();
//				//3. resto el total disponible menos las certificaciones ya creadas
//				double saldo=total-valorcertificacion;
				//Obtengo el valor ajustado del subitme
				//traigo los datos de actividadunidadacumulador
				Map<String, Double> saldodisponible=new HashMap<>();
				SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
				subitemunidadacumuladorTO.getId().setId(id);
				subitemunidadacumuladorTO.setTipo("A");
				subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
				Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
				if(subitemunidadacumuladorTOs.size()>0) {
					subitemunidadacumuladorTO=(SubitemunidadacumuladorTO)subitemunidadacumuladorTOs.iterator().next();
					saldodisponible.put("valorajustado", subitemunidadacumuladorTO.getTotal());
				}
				saldodisponible.put("saldo", saldo);
				jsonObject.put("valordisponiblesi", (JSONObject)JSONSerializer.toJSON(saldodisponible));
			}
			
			//datoslineaordend: Obtiene el saldo disponible de la certificacion y el valor de las ordenes no aprobadas
			else if(clase.equals("datoslineaordend")) {
				//1. traigo el total disponible del subitem
				double total=ConsultasUtil.obtenertotalsubitem(id);
				//2 Obtengo el id del nivelactividad
				NivelactividadTO nivelactividadTO=new NivelactividadTO();
				nivelactividadTO.setTablarelacionid(id);
				Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtenerNivelactividad(nivelactividadTO);
				log.println("niveles: " + nivelactividadTOs.size());
				nivelactividadTO=(NivelactividadTO) nivelactividadTOs.iterator().next();
				//2. Obtengo el detalle del subitem
				double saldo=ConsultasUtil.obtenersaldodisponible(total, id, nivelactividadTO.getId());
				//3. Consulto las ordenes de devengo no aprobadas
				Collection<OrdendevengoTO> ordendevengoTOs=UtilSession.planificacionServicio.transObtieneordenesdevengopendientes(id2);
				double ordenesnoaprob=0.0;
				for(OrdendevengoTO ordendevengoTO:ordendevengoTOs)
					ordenesnoaprob=ordenesnoaprob+ordendevengoTO.getValortotal();
				Map<String, Double> saldodisponible=new HashMap<>();
				saldodisponible.put("saldo", saldo);
				saldodisponible.put("noaprobadas", ordenesnoaprob);
				jsonObject.put("datoslineaordend", (JSONObject)JSONSerializer.toJSON(saldodisponible));
			}
			//Reforma
			else if(clase.equals("reforma")){
				ReformaTO reformaTO=new ReformaTO();
				reformaTO = UtilSession.planificacionServicio.transObtenerReformaTO(id);
				reformaTO.setNpunidadcodigo(reformaTO.getUnidad().getCodigopresup());
				reformaTO.setNpunidadnombre(reformaTO.getUnidad().getNombre());
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformaTO.getId()));
				//Consulto el item
				ItemunidadTO itemunidadTO=new ItemunidadTO();
				itemunidadTO.setItem(new ItemTO());
				itemunidadTO.setId(nivelactividadTO.getTablarelacionid());
				Collection<ItemunidadTO> itemunidadTOs=UtilSession.planificacionServicio.transObtenerItemunidad(itemunidadTO);
				if(itemunidadTOs.size()>0) {
					itemunidadTO=(ItemunidadTO)itemunidadTOs.iterator().next();
					reformaTO.setNpitemcodigo(itemunidadTO.getItem().getCodigo());
					reformaTO.setNpitemnombre(itemunidadTO.getItem().getNombre());
				}
				//asigno las fechas
				reformaTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(reformaTO.getFechaaprobacion()));
				reformaTO.setNpfechacreacion(UtilGeneral.parseDateToString(reformaTO.getFechacreacion()));
				reformaTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(reformaTO.getFechaeliminacion()));
				reformaTO.setNpfechanegacion(UtilGeneral.parseDateToString(reformaTO.getFechanegacion()));
				reformaTO.setNpfechasolicitud(UtilGeneral.parseDateToString(reformaTO.getFechasolicitud()));
				//traigo las reformaslinea las traigo
				ReformalineaTO reformalineaTO=new ReformalineaTO();
				Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO);
				jsonObject.put("reformalineas", (JSONArray)JSONSerializer.toJSON(reformalineaTOs,reformalineaTO.getJsonConfig()));
				jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfig()));
			}
			//Reformalinea
			else if(clase.equals("reformalinea")){
				ReformalineaTO reformalineaTO = UtilSession.planificacionServicio.transObtenerReformalineaTO(new ReformalineaID(id, id2));
				reformalineaTO.setNpvalordecremento(reformalineaTO.getValordecremento());
				reformalineaTO.setNpvalorincremento(reformalineaTO.getValorincremento());
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(reformalineaTO.getNivelactid(), jsonObject, mensajes);
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformalineaTO.getNivelactid()));
				//1. traigo el total disponible del subitem
				double total=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid());
				//2. Obtengo el detalle del subitem
//				SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				double valtotal=ConsultasUtil.obtenertotalsubitem(total, nivelactividadTO.getTablarelacionid(),reformalineaTO.getNivelactid(),reformalineaTO);
				reformalineaTO.setNpvalortotal(valtotal);
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformalineaTO,reformalineaTO.getJsonConfig()));
			}

			//Reformameta
			else if(clase.equals("reformameta")){
				ReformametaTO reformametaTO=new ReformametaTO();
				reformametaTO = UtilSession.planificacionServicio.transObtenerReformametaTO(id);
				reformametaTO.setNpunidadcodigo(reformametaTO.getUnidad().getCodigopresup());
				reformametaTO.setNpunidadnombre(reformametaTO.getUnidad().getNombre());
				//asigno las fechas
				reformametaTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(reformametaTO.getFechaaprobacion()));
				reformametaTO.setNpfechacreacion(UtilGeneral.parseDateToString(reformametaTO.getFechacreacion()));
				reformametaTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(reformametaTO.getFechaeliminacion()));
				reformametaTO.setNpfechanegacion(UtilGeneral.parseDateToString(reformametaTO.getFechanegacion()));
				reformametaTO.setNpfechasolicitud(UtilGeneral.parseDateToString(reformametaTO.getFechasolicitud()));
				//traigo las reformaslinea las traigo
				ReformametalineaTO reformametalineaTO=new ReformametalineaTO();
				Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO);
				jsonObject.put("reformametalineas", (JSONArray)JSONSerializer.toJSON(reformametalineaTOs,reformametalineaTO.getJsonConfig()));
				jsonObject.put("reformameta", (JSONObject)JSONSerializer.toJSON(reformametaTO,reformametaTO.getJsonConfig()));
			}
			//Reformametalinea
			else if(clase.equals("reformametalinea")){
				ReformametalineaTO reformametalineaTO = UtilSession.planificacionServicio.transObtenerReformametalineaTO(new ReformametalineaID(id, id2));
				reformametalineaTO.setNpdecremento(reformametalineaTO.getDecremento());
				reformametalineaTO.setNpincremento(reformametalineaTO.getIncremento());
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformametalineaTO.getNivelacid()));
				//1. traigo los datos de unidad medida y meta descripcion
				SubtareaunidadTO subtareaunidadTO=new SubtareaunidadTO();
				subtareaunidadTO.setId(nivelactividadTO.getTablarelacionid());
				subtareaunidadTO.setUnidadmedidaTO(new UnidadmedidaTO());
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.setSubtareaunidad(subtareaunidadTO);
				subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTO.setOrderByField(OrderBy.orderDesc("id.acumid"));
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				if(subtareaunidadacumuladorTOs.size()>0) {
					subtareaunidadacumuladorTO=(SubtareaunidadacumuladorTO)subtareaunidadacumuladorTOs.iterator().next();
					reformametalineaTO.setNpmetadescripcion(subtareaunidadacumuladorTO.getDescripcion());
				}
				reformametalineaTO.setNpunidadmedida(subtareaunidadTO.getUnidadmedidaTO().getNombre());
				jsonObject.put("reformametalinea", (JSONObject)JSONSerializer.toJSON(reformametalineaTO,reformametalineaTO.getJsonConfig()));
			}
			
			//datossubtarea: Recibo el id de la subtareaunidad y el id del nivelactividadid para buscar la unidadmedida y la descripcion y valor actual de la meta
			else if(clase.equals("datossubtarea")) {
				//1. Consulto la subtareaunidadacumulador y la subtarea para traer los datos necesarios
				SubtareaunidadTO subtareaunidadTO=new SubtareaunidadTO();
				subtareaunidadTO.setId(id);
				subtareaunidadTO.setUnidadmedidaTO(new UnidadmedidaTO());
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.setSubtareaunidad(subtareaunidadTO);
				subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTO.setOrderByField(OrderBy.orderDesc("id.acumid"));
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				if(subtareaunidadacumuladorTOs.size()>0) {
					Map<String, Double> valoractual=new HashMap<>();
					Map<String, String> descripciones=new HashMap<>();
					subtareaunidadacumuladorTO=(SubtareaunidadacumuladorTO)subtareaunidadacumuladorTOs.iterator().next();
					descripciones.put("metadescripcion", subtareaunidadacumuladorTO.getDescripcion());
					descripciones.put("unidadmedida", subtareaunidadTO.getUnidadmedidaTO().getNombre());
					valoractual.put("valor", subtareaunidadacumuladorTO.getCantidad());
					jsonObject.put("descripciones", (JSONObject)JSONSerializer.toJSON(descripciones));
					jsonObject.put("valoractual", (JSONObject)JSONSerializer.toJSON(valoractual));

				}
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
	
	@RequestMapping(value = "/flujo/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujo(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			CertificacionTO certificacionTO=UtilSession.planificacionServicio.transObtenerCertificacionTO(id);
			if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP")) {
				certificacionTO.setEstado(tipo);
				if(parameters.get("cur")!=null)
					certificacionTO.setCur(parameters.get("cur"));
				if(tipo.equals("EL")) {
					if(parameters.get("observacion")!=null)
						certificacionTO.setMotivoeliminacion(parameters.get("observacion"));
				}
				else if(tipo.equals("NE")) {
					if(parameters.get("observacion")!=null)
						certificacionTO.setMotivonegacion(parameters.get("observacion"));
				}
				UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
				//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
			//Verifico que no tenga atada una orden de gasto
			else if(tipo.equals("LT")) {
				certificacionTO.setEstado(tipo);
				OrdengastoTO ordengastoTO=new OrdengastoTO();
				ordengastoTO.setOrdengastocertificacionid(id);
				Collection<OrdengastoTO> ordengastoTOs=UtilSession.planificacionServicio.transObtenerOrdengasto(ordengastoTO);
				if(ordengastoTOs.size()>0) {
					boolean existeorden=false;
					for(OrdengastoTO ordengastoTO2:ordengastoTOs) {
						if(ordengastoTO2.getEstado().equals("AP") || ordengastoTO2.getEstado().equals("SO") || ordengastoTO2.getEstado().equals("RE")) {
							existeorden=true;
						}
					}
					if(existeorden) {
						mensajes.setMsg("No se puede liquidar totalmente a una Certificación que esté asociada a una Orden de gasto");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						respuesta.setEstado(false);
					}
					else {
						if(parameters.get("observacion")!=null)
							certificacionTO.setMotivoliquidacion(parameters.get("observacion"));
						UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
					}
				}
				else {
					if(parameters.get("observacion")!=null)
						certificacionTO.setMotivoliquidacion(parameters.get("observacion"));
					UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
				}
			}
			else if(tipo.equals("LP")) {
				certificacionTO.setEstado(tipo);
				OrdengastoTO ordengastoTO=new OrdengastoTO();
				ordengastoTO.setOrdengastocertificacionid(id);
				Collection<OrdengastoTO> ordengastoTOs=UtilSession.planificacionServicio.transObtenerOrdengasto(ordengastoTO);
				if(ordengastoTOs.size()==0) {
					mensajes.setMsg("No puede Liquidar manualmente una Certificacion que no este asociada a una Orden de Gasto");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
				else {
					double valorordenes=0.0;
					boolean aprobada=false;
					for(OrdengastoTO ordengastoTO2:ordengastoTOs) {
						if(ordengastoTO2.getEstado().equals("AP")) {
							valorordenes=valorordenes+ordengastoTO2.getValortotal();
							aprobada=true;
						}
					}
					if(!aprobada) {
						mensajes.setMsg("No puede Liquidar manualmente una Certificacion que no este asociada a una Orden de Gasto aprobada");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						respuesta.setEstado(false);
					}
					else {
						certificacionTO.setNptotalordenes(valorordenes);
						if(parameters.get("observacion")!=null)
							certificacionTO.setMotivoliquidacion(parameters.get("observacion"));
						UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
					}
				}
			}
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
	
	@RequestMapping(value = "/flujoordenes/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoordenes(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			OrdengastoTO ordengastoTO=UtilSession.planificacionServicio.transObtenerOrdengastoTO(id);
			if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP") || tipo.equals("AN")) {
				ordengastoTO.setEstado(tipo);
				if(parameters.get("cur")!=null)
					ordengastoTO.setCur(parameters.get("cur"));
				if(tipo.equals("EL")) {
					if(parameters.get("observacion")!=null)
						ordengastoTO.setMotivoeliminacion(parameters.get("observacion"));
				}
				else if(tipo.equals("NE")) {
					if(parameters.get("observacion")!=null)
						ordengastoTO.setMotivonegacion(parameters.get("observacion"));
				}
				else if(tipo.equals("AN")) {
					if(parameters.get("observacion")!=null)
						ordengastoTO.setMotivoanulacion(parameters.get("observacion"));
				}
				UtilSession.planificacionServicio.transCrearModificarOrdengasto(ordengastoTO, tipo);
				//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
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
	
	@RequestMapping(value = "/flujodevengo/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoordendevengo(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			OrdendevengoTO ordendevengoTO=UtilSession.planificacionServicio.transObtenerOrdendevengoTO(id);
			if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP") || tipo.equals("AN")) {
				ordendevengoTO.setEstado(tipo);
				if(tipo.equals("EL")) {
					if(parameters.get("observacion")!=null)
						ordendevengoTO.setMotivoeliminacion(parameters.get("observacion"));
				}
				else if(tipo.equals("NE")) {
					if(parameters.get("observacion")!=null)
						ordendevengoTO.setMotivonegacion(parameters.get("observacion"));
				}
				else if(tipo.equals("AN")) {
					if(parameters.get("observacion")!=null)
						ordendevengoTO.setMotivoanulacion(parameters.get("observacion"));
				}
				UtilSession.planificacionServicio.transCrearModificarOrdendevengo(ordendevengoTO, tipo);
				//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
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
	
	@RequestMapping(value = "/flujoreversion/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoreversion(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			OrdenreversionTO ordenreversionTO=UtilSession.planificacionServicio.transObtenerOrdenreversionTO(id);
			ordenreversionTO.setEstado(tipo);
			if(tipo.equals("EL")) {
				if(parameters.get("observacion")!=null)
					ordenreversionTO.setMotivoeliminacion(parameters.get("observacion"));
			}
			else if(tipo.equals("NE")) {
				if(parameters.get("observacion")!=null)
					ordenreversionTO.setMotivonegacion(parameters.get("observacion"));
			}
			UtilSession.planificacionServicio.transCrearModificarOrdenreversion(ordenreversionTO, tipo);
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

	@RequestMapping(value = "/flujoreforma/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoreforma(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			ReformametaTO reformametaTO=UtilSession.planificacionServicio.transObtenerReformametaTO(id);
			if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP")) {
				reformametaTO.setEstado(tipo);
				if(tipo.equals("EL")) {
					if(parameters.get("observacion")!=null)
						reformametaTO.setMotivoeliminar(parameters.get("observacion"));
					reformametaTO.setFechaeliminacion(new Date());
				}
				else if(tipo.equals("NE")) {
					if(parameters.get("observacion")!=null)
						reformametaTO.setMotivonegacion(parameters.get("observacion"));
					reformametaTO.setFechanegacion(new Date());
				}
				else if(tipo.equals("AN")) {
					if(parameters.get("observacion")!=null)
						reformametaTO.setMotivoanulacion(parameters.get("observacion"));
					reformametaTO.setFechanegacion(new Date());
				}
				UtilSession.planificacionServicio.transCrearModificarReformameta(reformametaTO, tipo);
				//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
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

	@RequestMapping(value = "/flujoreformameta/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoreformameta(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			ReformaTO reformaTO=UtilSession.planificacionServicio.transObtenerReformaTO(id);
			if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP") || tipo.equals("AN")) {
				reformaTO.setEstado(tipo);
				if(tipo.equals("EL")) {
					if(parameters.get("observacion")!=null)
						reformaTO.setMotivoeliminacion(parameters.get("observacion"));
					reformaTO.setFechaeliminacion(new Date());
				}
				else if(tipo.equals("NE")) {
					if(parameters.get("observacion")!=null)
						reformaTO.setMotivonegacion(parameters.get("observacion"));
					reformaTO.setFechanegacion(new Date());
				}
				UtilSession.planificacionServicio.transCrearModificarReforma(reformaTO, tipo);
				//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
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

	@RequestMapping(value = "/consultar/{clase}", method = RequestMethod.POST)
	public String consultarpost(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo consultar: " + objeto);
		Mensajes mensajes=new Mensajes();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			//Certificacion
			if(clase.equals("certificacion")){
				jsonObject=ConsultasUtil.consultaCertificacionPaginado(parameters, jsonObject, mensajes);
			}
			
			//Orden de gasto
			else if(clase.equals("ordengasto")){
				jsonObject=ConsultasUtil.consultaOrdengastoPaginado(parameters, jsonObject, mensajes);
			}
			
			//Orden de devengo
			else if(clase.equals("ordendevengo")){
				jsonObject=ConsultasUtil.consultaOrdendevengoPaginado(parameters, jsonObject, mensajes);
			}
			
			//Orden de reversion
			else if(clase.equals("ordenreversion")){
				jsonObject=ConsultasUtil.consultaOrdenreversionPaginado(parameters, jsonObject, mensajes);
			}

			//Reforma
			else if(clase.equals("reforma")){
				jsonObject=ConsultasUtil.consultaReformaPaginado(parameters, jsonObject, mensajes);
			}

			//Reformameta
			else if(clase.equals("reformameta")){
				jsonObject=ConsultasUtil.consultaReformametaPaginado(parameters, jsonObject, mensajes);
			}

			//Certificacion busqueda
			else if(clase.equals("certificacionbusqueda")){
				//jsonObject=ConsultasUtil.consultaCertificacionBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.certificacionbusqueda(parameters, jsonObject);
			}
			
			//Ordengasto busqueda
			else if(clase.equals("ordengastobusqueda")){
				//jsonObject=ConsultasUtil.consultaOrdengastoBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.ordenesgastobusqueda(parameters, jsonObject);
			}
	} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			//throw new MyException(e);
		}
		log.println("existe mensaje: " + mensajes.getMsg());
		if(mensajes.getMsg()!=null)
			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		log.println("json retornado: " + jsonObject.toString());
		return jsonObject.toString();	
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

			
			//Subitemunidad
			if(clase.equals("subitemunidad")){
				jsonObject=ConsultasUtil.consultaListasubitemunidad(parameters, jsonObject, mensajes);
			}

			//Sibiteminfo
			else if(clase.equals("subitemunidadinfo")){
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(Long.valueOf(parameters.get("nivelactividad")), jsonObject, mensajes);
			}
			
			//Subtareainfo
			else if(clase.equals("subitareainfo")){
				jsonObject=ConsultasUtil.consultaInformacionsubtarea(Long.valueOf(parameters.get("nivelactividad")), jsonObject, mensajes);
			}

			//subitemcertificado
			else if(clase.equals("subitemcertificado")){
				CertificacionlineaTO certificacionlineaTO=new CertificacionlineaTO();
				Collection<CertificacionlineaTO> resultado=UtilSession.planificacionServicio.transObtienesubitemporcertificacion(Long.valueOf(parameters.get("certificacionid")));
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,certificacionlineaTO.getJsonConfigbusqueda()));
				HashMap<String, Integer>  totalMap=new HashMap<String, Integer>();
				totalMap.put("valor", resultado.size());
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
			
			//subitemordengasto
			else if(clase.equals("subitemordengasto")){
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				Collection<OrdengastolineaTO> resultado=UtilSession.planificacionServicio.transObtienesubitemporordengasto(Long.valueOf(parameters.get("ordengastoid")));
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,ordengastolineaTO.getJsonConfigbusqueda()));
				HashMap<String, Integer>  totalMap=new HashMap<String, Integer>();
				totalMap.put("valor", resultado.size());
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
			
			//Certificacion busqueda
//			else if(clase.equals("certificacionbusqueda")){
//				//jsonObject=ConsultasUtil.consultaCertificacionBusquedaPaginado(parameters, jsonObject, mensajes);
//				jsonObject=ConsultasUtil.certificacionbusqueda(parameters, jsonObject);
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
	
	@RequestMapping(value = "/{clase}/{id}/{id2}", method = RequestMethod.DELETE)
	//@ResponseStatus(HttpStatus.NO_CONTENT)
	public Respuesta eliminar(@PathVariable String clase,@PathVariable Long id,@PathVariable Long id2,HttpServletRequest request){
		log.println("entra al metodo eliminar");
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		try {
			//Certificacionlinea
			if(clase.equals("certificacionlinea")){
				UtilSession.planificacionServicio.transEliminarCertificacionlinea(new CertificacionlineaTO(new CertificacionlineaID(id, id2)));
			}
			//ordengastolinea
			else if(clase.equals("ordengastolinea")){
				UtilSession.planificacionServicio.transEliminarOrdengastolinea(new OrdengastolineaTO(new OrdengastolineaID(id, id2)));
			}
			//ordendevengolinea
			else if(clase.equals("ordendevengolinea")){
				UtilSession.planificacionServicio.transEliminarOrdendevengolinea(new OrdendevengolineaTO(new OrdendevengolineaID(id, id2)));
			}
			//ordenreversionlinea
			else if(clase.equals("ordenreversionlinea")){
				UtilSession.planificacionServicio.transEliminarOrdenreversionlinea(new OrdenreversionlineaTO(new OrdenreversionlineaID(id, id2)));
			}
			//reformalinea
			else if(clase.equals("reformalinea")){
				UtilSession.planificacionServicio.transEliminarReformalinea(new ReformalineaTO(new ReformalineaID(id, id2)));
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


	
}
