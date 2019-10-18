package ec.com.papp.web.ejecucion.controller;

import java.io.StringReader;
import java.security.Principal;
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
import ec.com.papp.planificacion.id.ReformametasubtareaID;
import ec.com.papp.planificacion.to.ActividadTO;
import ec.com.papp.planificacion.to.CertificacionTO;
import ec.com.papp.planificacion.to.CertificacionlineaTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.ContratoTO;
import ec.com.papp.planificacion.to.CronogramaTO;
import ec.com.papp.planificacion.to.CronogramalineaTO;
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
import ec.com.papp.planificacion.to.ReformametasubtareaTO;
import ec.com.papp.planificacion.to.SubtareaunidadTO;
import ec.com.papp.planificacion.to.SubtareaunidadacumuladorTO;
import ec.com.papp.planificacion.util.Ejecuciondetalleact;
import ec.com.papp.planificacion.util.Ejecuciondetallesubtarea;
import ec.com.papp.planificacion.util.MatrizDetalle;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.administracion.controller.ComunController;
import ec.com.papp.web.comun.util.ConstantesSesion;
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
				accion = (certificacionTO.getId()==null)?"I":"U";
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
				//id=certificacionTO.getNpid().toString();
				certificacionTO.setId(certificacionTO.getNpid());
				ConsultasUtil.obtenercertificacion(certificacionTO.getNpid(), jsonObject);
				//jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
			}
			//certificacion linea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = gson.fromJson(new StringReader(objeto), CertificacionlineaTO.class);
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				CertificacionlineaTO certificacionlineaTO2=new CertificacionlineaTO();
				log.println("consulta lineas: " + certificacionlineaTO.getNivelactid() +"-"+ certificacionlineaTO.getId().getId());
				certificacionlineaTO2.setNivelactid(certificacionlineaTO.getNivelactid());
				certificacionlineaTO2.getId().setId(certificacionlineaTO.getId().getId());
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO2);
				log.println("certificaciones: " + certificacionlineaTOs.size());
				boolean grabar=true;
				if(certificacionlineaTOs.size()>0){
					for(CertificacionlineaTO certificacionlineaTO3:certificacionlineaTOs) {
						if((certificacionlineaTO.getId().getLineaid()!=null && certificacionlineaTO.getId().getLineaid().longValue()!=0) && certificacionlineaTO3.getId().getLineaid().longValue()!=certificacionlineaTO.getId().getLineaid().longValue()) {
							grabar=false;
							break;
						}
						else if(certificacionlineaTO.getId().getLineaid()==null || certificacionlineaTO.getId().getLineaid().longValue()==0L) {
							grabar=false;
							mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
							break;
						}
					}
				}
				Collection<CertificacionlineaTO> certificacionlineaTOs2=UtilSession.planificacionServicio.transObtienecertificadoslinea(certificacionlineaTO.getId().getId());
				CertificacionTO certificacionTO=UtilSession.planificacionServicio.transObtenerCertificacionTO(certificacionlineaTO.getId().getId());
				NivelactividadTO subitemnivelact=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(certificacionlineaTO.getNivelactid()));
				NivelactividadTO nivelactividadTO=new NivelactividadTO();
				nivelactividadTO.setId(subitemnivelact.getNivelactividadpadreid());
				nivelactividadTO.setNivelactividadejerfiscalid(certificacionTO.getCertificacionejerfiscalid());
				nivelactividadTO.setTipo("IT");
				nivelactividadTO.setEstado("A");
				Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO, false);
				nivelactividadTO =(NivelactividadTO)nivelactividadTOs.iterator().next();
				System.out.println("fuente de financiamiento: " + nivelactividadTO.getNpcodigofuente());
				for(CertificacionlineaTO certificacionlineaTO3:certificacionlineaTOs2) {
					System.out.println("fuente a comparar: " + certificacionlineaTO3.getNpcodigofuente());
					if(!nivelactividadTO.getNpcodigofuente().equals(certificacionlineaTO3.getNpcodigofuente())){
						grabar=false;
						mensajes.setMsg("El subitem debe tener la misma fuente de financiamiento");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						break;
					}
				}
				if(grabar){
					accion = (certificacionlineaTO.getId()==null)?"I":"U";
					UtilSession.planificacionServicio.transCrearModificarCertificacionlinea(certificacionlineaTO);
					//id=certificacionlineaTO.getId().getId().toString() + certificacionlineaTO.getId().getLineaid();
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
				accion = (ordengastoTO.getId()==null)?"I":"U";
				UtilSession.planificacionServicio.transCrearModificarOrdengasto(ordengastoTO,null);
				//id=ordengastoTO.getNpid().toString();
				ordengastoTO.setId(ordengastoTO.getNpid());
				ConsultasUtil.obtenerordengasto(ordengastoTO.getNpid(), jsonObject);
				//jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfig()));
			}
			//ordengasto linea
			else if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO = gson.fromJson(new StringReader(objeto), OrdengastolineaTO.class);
				ordengastolineaTO.setSaldo(ordengastolineaTO.getNpsaldocertificacion());
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				OrdengastolineaTO ordengastolineaTO2=new OrdengastolineaTO();
				log.println("consulta lineas: " + ordengastolineaTO.getNivelactid() +"-"+ ordengastolineaTO.getId().getId());
				ordengastolineaTO2.setNivelactid(ordengastolineaTO.getNivelactid());
				ordengastolineaTO2.getId().setId(ordengastolineaTO.getId().getId());
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO2,true);
				log.println("ordenes: " + ordengastolineaTOs.size());
				boolean grabar=true;
				if(ordengastolineaTOs.size()>0){
					for(OrdengastolineaTO ordengastolineaTO3:ordengastolineaTOs) {
						log.println("ordengastolineaTO.getId().getLineaid(): " + ordengastolineaTO.getId().getLineaid());
						log.println("ordengastolineaTO3.getId().getLineaid(): " + ordengastolineaTO3.getId().getLineaid());
						log.println("ordengastolineaTO3.getNplineaid): " + ordengastolineaTO3.getNpordenlineaid());
						if((ordengastolineaTO.getId().getLineaid()!=null && ordengastolineaTO.getId().getLineaid().longValue()!=0) && ordengastolineaTO3.getNpordenlineaid().longValue()!=ordengastolineaTO.getId().getLineaid().longValue()) {
							grabar=false;
							break;
						}
						else if(ordengastolineaTO.getId().getLineaid()==null || ordengastolineaTO.getId().getLineaid().longValue()==0L) {
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
					accion = (ordengastolineaTO.getId()==null)?"I":"U";
					UtilSession.planificacionServicio.transCrearModificarOrdengastolinea(ordengastolineaTO);
					//id=ordengastolineaTO.getId().getId().toString() + ordengastolineaTO.getId().getLineaid();
					ConsultasUtil.obtenerordengasto(ordengastolineaTO.getId().getId(), jsonObject);
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
				accion = (ordendevengoTO.getId()==null)?"I":"U";
				UtilSession.planificacionServicio.transCrearModificarOrdendevengo(ordendevengoTO, null);
				//id=ordendevengoTO.getNpid().toString();
				ordendevengoTO.setId(ordendevengoTO.getNpid());
				ConsultasUtil.obtenerordendevengo(ordendevengoTO.getNpid(), jsonObject);
			}
			//ordendevengo linea
			else if(clase.equals("ordendevengolinea")){
				OrdendevengolineaTO ordendevengolineaTO = gson.fromJson(new StringReader(objeto), OrdendevengolineaTO.class);
				ordendevengolineaTO.setSaldo(ordendevengolineaTO.getNpsaldo());
				OrdendevengolineaTO ordendevengolineaTO2=new OrdendevengolineaTO();
				log.println("consulta lineas: " + ordendevengolineaTO.getNivelactid() +"-"+ ordendevengolineaTO.getId().getId());
				ordendevengolineaTO2.setNivelactid(ordendevengolineaTO.getNivelactid());
				ordendevengolineaTO2.getId().setId(ordendevengolineaTO.getId().getId());
				Collection<OrdendevengolineaTO> ordendevengolineaTOs=UtilSession.planificacionServicio.transObtenerOrdendevengolinea(ordendevengolineaTO2);
				log.println("ordenes: " + ordendevengolineaTOs.size());
				boolean grabar=true;
				if(ordendevengolineaTOs.size()>0){
					for(OrdendevengolineaTO ordendevengolineaTO3:ordendevengolineaTOs) {
						if((ordendevengolineaTO.getId().getLineaid()!=null && ordendevengolineaTO.getId().getLineaid().longValue()!=0) && ordendevengolineaTO3.getId().getLineaid().longValue()!=ordendevengolineaTO.getId().getLineaid().longValue()) {
							grabar=false;
							break;
						}
						else if(ordendevengolineaTO.getId().getLineaid()==null || ordendevengolineaTO.getId().getLineaid().longValue()==0L) {
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
					accion = (ordendevengolineaTO.getId()==null)?"I":"U";
					UtilSession.planificacionServicio.transCrearModificarOrdendevengolinea(ordendevengolineaTO);
					//id=ordendevengolineaTO.getId().getId().toString()+ordendevengolineaTO.getId().getLineaid();
					ConsultasUtil.obtenerordendevengo(ordendevengolineaTO.getId().getId(), jsonObject);
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
				accion = (ordenreversionTO.getId()==null)?"I":"U";
				UtilSession.planificacionServicio.transCrearModificarOrdenreversion(ordenreversionTO, null);
				//id=ordenreversionTO.getNpid().toString();
				ordenreversionTO.setId(ordenreversionTO.getNpid());
				ConsultasUtil.obtenerordenreversion(ordenreversionTO.getNpid(), jsonObject);
			}
			//ordenreversion linea
			else if(clase.equals("ordenreversionlinea")){
				OrdenreversionlineaTO ordenreversionlineaTO = gson.fromJson(new StringReader(objeto), OrdenreversionlineaTO.class);
				ordenreversionlineaTO.setSaldo(ordenreversionlineaTO.getSaldo());
				//pregunto si ya tiene una linea con el mismo subitem y no le dejo
				OrdenreversionlineaTO ordenreversionlineaTO2=new OrdenreversionlineaTO();
				System.out.println("consulta lineas: " + ordenreversionlineaTO.getNivelactid() +"-"+ ordenreversionlineaTO.getId().getId());
				ordenreversionlineaTO2.setNivelactid(ordenreversionlineaTO.getNivelactid());
				ordenreversionlineaTO2.getId().setId(ordenreversionlineaTO.getId().getId());
				Collection<OrdenreversionlineaTO> ordenreversionlineaTOs=UtilSession.planificacionServicio.transObtenerOrdenreversionlinea(ordenreversionlineaTO2);
				System.out.println("ordenes: " + ordenreversionlineaTOs.size());
				boolean grabar=true;
				if(ordenreversionlineaTOs.size()>0){
					for(OrdenreversionlineaTO ordenreversionlineaTO3:ordenreversionlineaTOs) {
						System.out.println("nivelactividad3: " + ordenreversionlineaTO3.getNivelactid());
						if((ordenreversionlineaTO.getId().getLineaid()!=null && ordenreversionlineaTO.getId().getLineaid().longValue()!=0) && ordenreversionlineaTO3.getId().getLineaid().longValue()!=ordenreversionlineaTO.getId().getLineaid().longValue()) {
							grabar=false;
							break;
						}
						else if(ordenreversionlineaTO.getId().getLineaid()==null || ordenreversionlineaTO.getId().getLineaid().longValue()==0L) {
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
					accion = (ordenreversionlineaTO.getId()==null)?"I":"U";
					UtilSession.planificacionServicio.transCrearModificarOrdenreversionlinea(ordenreversionlineaTO);
					//id=ordenreversionlineaTO.getId().getId().toString()+ordenreversionlineaTO.getId().getLineaid();
					ConsultasUtil.obtenerordenreversion(ordenreversionlineaTO.getId().getId(), jsonObject);
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
						contratoTO.setSocionegocio(new SocionegocioTO());
						contratoTO=UtilSession.planificacionServicio.transObtenerContratoTO(ordengastoTO.getOrdengastocontratoid());
						if(contratoTO!=null && (contratoTO.getId()!=null || contratoTO.getId().longValue()!=0)) {
							log.println("id del contrato: " + contratoTO.getId());
							log.println("fecha de inicio: " + contratoTO.getFechainicio());
							log.println("id del proveedor: " + contratoTO.getContratoproveedorid());
							if(contratoTO.getFechainicio()!=null)
								contratoTO.setNpfechainicio(UtilGeneral.parseDateToString(contratoTO.getFechainicio()));
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
					//si tiene porcentaje calculo el anticipo
					if(contratoTO.getAnticipoporcentaje()!=null && contratoTO.getValortotal()!=null && contratoTO.getValortotal()>0){
						System.out.println("total: " +contratoTO.getValortotal() +"anticipo%: "+ contratoTO.getAnticipoporcentaje());
						double anticipo=(contratoTO.getValortotal()*contratoTO.getAnticipoporcentaje())/100;
						System.out.println("anticipo: "+anticipo);
						contratoTO.setAnticipovalor(UtilGeneral.redondear(anticipo,2));
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
				accion = (reformaTO.getId()==null)?"I":"U";
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
				if(reformaTO.getNivelactividadid()!=null && reformaTO.getNivelactividadid()==0)
					reformaTO.setNivelactividadid(null);
				//verifico que esten hechas las distribuciones
				String resultado="";
				if(reformaTO.getId()!=null && reformaTO.getId().longValue()!=0){
					System.out.println("va a ver las distribuciones ");
					resultado=UtilSession.planificacionServicio.transListareformalineasdistribucion(reformaTO.getId());
				}
				UtilSession.planificacionServicio.transCrearModificarReforma(reformaTO,null);
				//id=reformaTO.getNpid().toString();
				reformaTO.setId(reformaTO.getNpid());
				ConsultasUtil.obtenerreforma(reformaTO.getId(), jsonObject);
				if(!resultado.equals("")){
					mensajes.setMsg("Realice la distribucion de los subitems: " + resultado);
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					System.out.println("mensaje: " + mensajes.getMsg());
				}
				else{
					resultado="";
					if(reformaTO.getId()!=null && reformaTO.getId().longValue()!=0){
						System.out.println("va a ver las distribuciones de las subtareas ");
						resultado=UtilSession.planificacionServicio.transListareformalineasdistribucionrms(reformaTO);
						System.out.println("resultad: " + resultado);
						if(!resultado.equals("")){
							mensajes.setMsg("Realice la distribucion de las subtareas: " + resultado);
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
							System.out.println("mensaje: " + mensajes.getMsg());
						}
					}
				}
			}
			
			//reforma linea
			else if(clase.equals("reformalinea")){
				ReformalineaTO reformalineaTO = gson.fromJson(new StringReader(objeto), ReformalineaTO.class);
				Collection<ReformalineaTO> reformalineaTOexistentes=UtilSession.planificacionServicio.transObtienereformasnoelne(reformalineaTO.getNivelactid());
				boolean grabar=true;
				String codigoreforma="";
				if(reformalineaTOexistentes.size()>0){
					//verifico que si es de tipo MU O EU debe tener la misma fuente de financiamiento
					for(ReformalineaTO reformalineaTO2:reformalineaTOexistentes){
						if((reformalineaTO.getId().getLineaid()!=null && reformalineaTO.getId().getLineaid().longValue()!=0) && 
								(reformalineaTO2.getReforma().getEstado().equals("RE") || reformalineaTO2.getReforma().getEstado().equals("SO")) &&
								(reformalineaTO2.getId().getLineaid().longValue()!=reformalineaTO.getId().getLineaid().longValue() && reformalineaTO.getNivelactid().longValue()==reformalineaTO2.getNivelactid().longValue())) {
							grabar=false;
							codigoreforma=reformalineaTO2.getReforma().getCodigo();
							break;
						}
						else if((reformalineaTO.getId().getLineaid()==null || reformalineaTO.getId().getLineaid().longValue()==0L) && 
								(reformalineaTO2.getReforma().getEstado().equals("RE") || reformalineaTO2.getReforma().getEstado().equals("SO")) &&
								reformalineaTO.getNivelactid().longValue()==reformalineaTO2.getNivelactid().longValue()) {
							grabar=false;
							codigoreforma=reformalineaTO2.getReforma().getCodigo();
							break;
						}
					}
				}
				if(grabar){
					//traigo la reforma para saber de que tipo es
					ReformaTO reformaTO=UtilSession.planificacionServicio.transObtenerReformaTO(reformalineaTO.getId().getId());
					//pregunto si ya tiene una linea con el mismo subitem y no le dejo
					ReformalineaTO reformalineaTO2=new ReformalineaTO();
					//System.out.println("consulta lineas: " + reformalineaTO.getNivelactid() +"-"+ reformalineaTO.getId().getId() +"-"+reformalineaTO.getId().getLineaid());
					reformalineaTO2.setNivelactid(reformalineaTO.getNivelactid());
					reformalineaTO2.getId().setId(reformalineaTO.getId().getId());
					Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO2,null);
					//System.out.println("reformas: " + reformalineaTOs.size());
					grabar=true;
					//if(reformalineaTOs.size()>0 && (reformaTO.getTipo().equals("MU") || reformaTO.getTipo().equals("EU"))){
						NivelactividadTO subitemnivelact=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformalineaTO.getNivelactid()));
						NivelactividadTO nivelactividadTO=new NivelactividadTO();
						nivelactividadTO.setId(subitemnivelact.getNivelactividadpadreid());
						nivelactividadTO.setNivelactividadejerfiscalid(reformaTO.getReformaejerfiscalid());
						nivelactividadTO.setTipo("IT");
						nivelactividadTO.setEstado("A");
						Collection<NivelactividadTO> nivelactividadTOs=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO, false);
						nivelactividadTO =(NivelactividadTO)nivelactividadTOs.iterator().next();
						for(ReformalineaTO reformalineaTO3:reformalineaTOs) {
							if(!nivelactividadTO.getNpcodigofuente().equals(reformalineaTO3.getNpcodigofuente())){
								grabar=false;
								break;
							}
						}
					//}
					if(grabar){
						if(reformalineaTOs.size()>0){
							//valido que el subitem no exista en otra reforma que no este aprobada
							
							for(ReformalineaTO reformalineaTO3:reformalineaTOs) {
								//si la reforma es de tipo entre subitem "es" debo ver que pertenezcan al mismo item
								//System.out.println("reforma linea existente: " + reformalineaTO3.getNivelactid() +"-"+ reformalineaTO3.getId().getId() + " - " +reformalineaTO3.getId().getLineaid() );
								if((reformalineaTO.getId().getLineaid()!=null && reformalineaTO.getId().getLineaid().longValue()!=0) && (reformalineaTO3.getId().getLineaid().longValue()!=reformalineaTO.getId().getLineaid().longValue() && reformalineaTO.getNivelactid().longValue()==reformalineaTO3.getNivelactid().longValue())) {
									grabar=false;
									break;
								}
								else if((reformalineaTO.getId().getLineaid()==null || reformalineaTO.getId().getLineaid().longValue()==0L) && reformalineaTO.getNivelactid().longValue()==reformalineaTO3.getNivelactid().longValue()) {
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
							accion = (reformalineaTO.getId()==null)?"I":"U";
							if(reformalineaTO.getNpvalordecremento()==null)
								reformalineaTO.setNpvalordecremento(0.0);
							if(reformalineaTO.getNpvalorincremento()==null)
								reformalineaTO.setNpvalorincremento(0.0);
							UtilSession.planificacionServicio.transCrearModificarReformalinea(reformalineaTO);
							//id=reformalineaTO.getId().getId().toString() + reformalineaTO.getId().getLineaid();
							//Traiga la lista de cetificacionlinea
							ConsultasUtil.obtenerreforma(reformalineaTO.getId().getId(), jsonObject);
		
							
		//					//Traigo la lista de ordengastolinea
		//					ReformalineaTO reformalineaTO3=new ReformalineaTO();
		//					reformalineaTO3.getId().setId(reformalineaTO.getId().getId());
		//					Collection<ReformalineaTO> reformalineaTOs2=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO3);
		//					jsonObject.put("reformalinea", (JSONArray)JSONSerializer.toJSON(reformalineaTOs2,reformalineaTO.getJsonConfig()));
						}
					}
					else{
						mensajes.setMsg("El subitem debe tener la misma fuente de financiamiento: " + codigoreforma);
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
				else{
					mensajes.setMsg("El subitem ya esta siendo utilizado en otra reforma en curso: " + codigoreforma);
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//reformameta
			if(clase.equals("reformameta")){
				ReformametaTO reformametaTO = gson.fromJson(new StringReader(objeto), ReformametaTO.class);
				accion = (reformametaTO.getId()==null)?"I":"U";
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
//				String resultado="";
//				if(reformametaTO.getId()!=null && reformametaTO.getId().longValue()!=0){
//					System.out.println("va a ver las distribuciones ");
//					resultado=UtilSession.planificacionServicio.transListareformalineasdistribucionrm(reformametaTO.getId());
//				}
				UtilSession.planificacionServicio.transCrearModificarReformameta(reformametaTO,null);
				//id=reformaTO.getNpid().toString();
				System.out.println("reforma metaid:  " + reformametaTO.getNpid());
				reformametaTO.setId(reformametaTO.getNpid());
				ConsultasUtil.obtenerreformameta(reformametaTO.getId(), jsonObject);
//				if(!resultado.equals("")){
//					mensajes.setMsg("Realice la distribucion de las subtareas: " + resultado);
//					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//					System.out.println("mensaje: " + mensajes.getMsg());
//				}
				//id=reformametaTO.getNpid().toString();
				reformametaTO.setId(reformametaTO.getNpid());
				jsonObject.put("reformameta", (JSONObject)JSONSerializer.toJSON(reformametaTO,reformametaTO.getJsonConfig()));
			}
			
			//reforma meta linea
			else if(clase.equals("reformametalinea")){
				ReformametalineaTO reformametalineaTO = gson.fromJson(new StringReader(objeto), ReformametalineaTO.class);
				//pregunto si ya tiene una linea con el mismo subtarea y no le dejo
				ReformametalineaTO reformalineaTO2=new ReformametalineaTO();
				System.out.println("consulta lineas: " + reformametalineaTO.getNivelactid() +"-"+ reformametalineaTO.getId().getId());
				reformalineaTO2.setNivelactid(reformametalineaTO.getNivelactid());
				reformalineaTO2.getId().setId(reformametalineaTO.getId().getId());
				Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtenerReformametalinea(reformalineaTO2,null);
				System.out.println("ordenes: " + reformametalineaTOs.size());
				boolean grabar=true;
				if(reformametalineaTOs.size()>0){
					for(ReformametalineaTO reformalineaTO3:reformametalineaTOs) {
						if((reformametalineaTO.getId().getLineaid()!=null && reformametalineaTO.getId().getLineaid().longValue()!=0) && reformalineaTO3.getId().getLineaid().longValue()!=reformametalineaTO.getId().getLineaid().longValue()) {
							grabar=false;
							System.out.println("entro 1");
							break;
						}
						else if(reformametalineaTO.getId().getLineaid()==null || reformametalineaTO.getId().getLineaid().longValue()==0L) {
							grabar=false;
							System.out.println("entro 2");
							break;
						}
					}
				}

				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("advertencia.certificacionlinea.repetida"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					accion = (reformametalineaTO.getId()==null)?"I":"U";
					if(reformametalineaTO.getValorincremento().doubleValue()>0 && reformametalineaTO.getValordecremento().doubleValue()>0)
						reformametalineaTO.setCambia(1);
					else
						reformametalineaTO.setCambia(0);
					UtilSession.planificacionServicio.transCrearModificarReformametalinea(reformametalineaTO);
					id=reformametalineaTO.getId().getId().toString() + reformametalineaTO.getId().getLineaid();
					//Traigo la lista de reformametalinea
					ReformametalineaTO reformametalineaTO3=new ReformametalineaTO();
					reformametalineaTO3.getId().setId(reformametalineaTO.getId().getId());
					ReformametaTO reformametaTO=UtilSession.planificacionServicio.transObtenerReformametaTO(reformametalineaTO.getId().getId());
					Collection<ReformametalineaTO> reformametalineaTOs2=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO3,reformametaTO.getFechacreacion());
					jsonObject.put("reformametalinea", (JSONArray)JSONSerializer.toJSON(reformametalineaTOs2,reformametalineaTO.getJsonConfig()));
				}
			}

			//reforma meta subtarea, se usa para consultar las metas de las reformas
			else if(clase.equals("obtenerreformametasubtarea")){
				ReformaTO reformaTO = gson.fromJson(new StringReader(objeto), ReformaTO.class);
				//debe llegar tambien las lineas
				reformaTO=UtilSession.planificacionServicio.transObtienereformametasubtarea(reformaTO);
				jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfig()));
				jsonObject.put("reformametasubtarea", (JSONArray)JSONSerializer.toJSON(reformaTO.getReformametasubtareaTOs(),(new ReformametasubtareaTO()).getJsonConfig()));
				request.getSession().setAttribute(ConstantesSesion.REFORMA, reformaTO);
				if(reformaTO.getMensajes()!=null && !reformaTO.getMensajes().equals("")) {
					mensajes.setMsg(reformaTO.getMensajes());
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//reforma meta subtarea, grabar la reforma de metas dentro de las reformas
			else if(clase.equals("reformametasubtarea")){
				ReformametasubtareaTO reformametasubtareaTO = gson.fromJson(new StringReader(objeto), ReformametasubtareaTO.class);
				accion = (reformametasubtareaTO.getId()==null)?"I":"U";
				//verifico que esten hechas las distribuciones
				String resultado="";
				//if(reformametasubtareaTO.getId()!=null && reformametasubtareaTO.getId().getId()!=0){
					System.out.println("va a ver las distribuciones ");
					resultado=UtilSession.planificacionServicio.transListareformalineasdistribucionrmm(reformametasubtareaTO);
				//}
//				if(!resultado.equals("")){
//					mensajes.setMsg("Realice la distribucion de la subtarea ");
//					mensajes.setType(MensajesWeb.getString("mensaje.exito"));
//					System.out.println("mensaje: " + mensajes.getMsg());
//				}
				UtilSession.planificacionServicio.transCrearModificarReformametasubtarea(reformametasubtareaTO);
				ReformaTO reformaTO = (ReformaTO) request.getSession().getAttribute(ConstantesSesion.REFORMA);
				//debe llegar tambien las lineas
				reformaTO=UtilSession.planificacionServicio.transObtienereformametasubtarea(reformaTO);
				//jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfig()));
				jsonObject.put("reformametasubtarea", (JSONArray)JSONSerializer.toJSON(reformaTO.getReformametasubtareaTOs(),(new ReformametasubtareaTO()).getJsonConfig()));
				//id=reformametasubtareaTO.getId().getId().toString() + reformametasubtareaTO.getId().getLineaid();
				//Traigo la lista de reformametasubtarea
//				ReformametasubtareaTO reformametasubtareaTO2=new ReformametasubtareaTO();
//				reformametasubtareaTO2.getId().setId(reformametasubtareaTO.getId().getId());
//				Collection<ReformametasubtareaTO> reformametalineaTOs2=UtilSession.planificacionServicio.transObtenerReformametasubatera(reformametasubtareaTO2);
//				System.out.println("reformametalineaTOs2: " + reformametalineaTOs2.size());
//				for(ReformametasubtareaTO reformametasubtareaTO3: reformametalineaTOs2){
//					System.out.println("reformasubtarea***: " + reformametasubtareaTO3.getNivelactid());
//					MatrizDetalle subtareaunidad=UtilSession.planificacionServicio.transObtienedetallesubtarea(reformametasubtareaTO3.getNivelactid(), 0L);
//					System.out.println("subtareaunidad.getIdtarea() " + subtareaunidad.getIdtarea());
//					SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
//					subtareaunidadacumuladorTO.getId().setId(subtareaunidad.getIdtarea());
//					subtareaunidadacumuladorTO.setTipo("A");
//					subtareaunidadacumuladorTO.getId().setAcumid(2L);
//					Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
//					subtareaunidadacumuladorTO=(SubtareaunidadacumuladorTO) subtareaunidadacumuladorTOs.iterator().next();
//					System.out.println("subtareaunidadacumuladorTO.getDescripcion(): "+ subtareaunidadacumuladorTO.getDescripcion());
//					System.out.println("subtareaunidad.getNpunidadmedida(): " + subtareaunidad.getNpunidadmedida());
//					reformametasubtareaTO3.setNpSubtarea(subtareaunidad.getSubtareanombre());
//					reformametasubtareaTO3.setNpSubtareacodigo(subtareaunidad.getSubtareacodigo());
//					reformametasubtareaTO3.setNpSubtareaid(subtareaunidad.getIdtarea());
//					reformametasubtareaTO3.setNpunidadmedida(subtareaunidad.getNpunidadmedida());
//					reformametasubtareaTO3.setNpmetadescripcion(subtareaunidadacumuladorTO.getDescripcion());
//				}
//				jsonObject.put("reformametasubtarea", (JSONArray)JSONSerializer.toJSON(reformametalineaTOs2,reformametasubtareaTO.getJsonConfig()));
			}


			System.out.println("mensajes.... " + mensajes.getMsg());
			if(mensajes.getMsg()==null){
				ComunController.crearAuditoria(request, clase, accion, objeto, id);
				mensajes.setMsg(MensajesWeb.getString("mensaje.guardar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			else if(!mensajes.getType().equals(MensajesWeb.getString("mensaje.exito")))
				respuesta.setEstado(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			if(clase.equals("obtenerreformametasubtarea")){
				mensajes.setMsg("Error al obtener las subtareas");
				mensajes.setType(MensajesWeb.getString("mensaje.error"));
				respuesta.setEstado(false);
			}
			else{
				mensajes.setMsg(MensajesWeb.getString("error.guardar"));
				mensajes.setType(MensajesWeb.getString("mensaje.error"));
				respuesta.setEstado(false);
			}
			//throw new MyException(e);
		}
		log.println("existe mensaje: " + mensajes.getMsg());
//		if(mensajes.getMsg()!=null)
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}

	@RequestMapping(value = "/{clase}/{ejerciciofiscal}", method = RequestMethod.POST)
	public Respuesta grabarejecucion(@PathVariable String clase, @RequestBody String objeto, @PathVariable Long ejerciciofiscal,HttpServletRequest request){
		log.println("entra al metodo grabar en ejecucion de metas: " + objeto);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		String id="";
		String accion="";
		try {
			//para la ejecucion de metas
			if(clase.equals("cronogramaactividades")){
				Ejecuciondetalleact ejecuciondetalleact= gson.fromJson(new StringReader(objeto), Ejecuciondetalleact.class);
				//accion = (cronogramaTO.getId()==null)?"I":"U";
				System.out.println("entra a grabar el cronogramaactividades");
				UtilSession.planificacionServicio.transCrearModificarCronogramaejeactividad(ejecuciondetalleact, ejerciciofiscal);
			}
			else if(clase.equals("cronogramasubtareas")){
				Ejecuciondetallesubtarea ejecuciondetallesubtarea= gson.fromJson(new StringReader(objeto), Ejecuciondetallesubtarea.class);
				//accion = (cronogramaTO.getId()==null)?"I":"U";
				UtilSession.planificacionServicio.transCrearModificarCronogramaejesubtarea(ejecuciondetallesubtarea, ejerciciofiscal);
			}
			System.out.println("mensajes.... " + mensajes.getMsg());
			if(mensajes.getMsg()==null){
				ComunController.crearAuditoria(request, clase, accion, objeto, id);
				mensajes.setMsg(MensajesWeb.getString("mensaje.guardar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			else if(!mensajes.getType().equals(MensajesWeb.getString("mensaje.exito")))
				respuesta.setEstado(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
		}
		log.println("existe mensaje: " + mensajes.getMsg());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}

	
	@RequestMapping(value = "/nuevo/{clase}/{id}", method = RequestMethod.GET)
	public Respuesta nuevo(@PathVariable String clase,@PathVariable Long id,HttpServletRequest request){
		log.println("entra al metodo nuevo: " + id);
		JSONObject jsonObject=new JSONObject();
		request.getSession().removeAttribute(ConstantesSesion.ORDENGASTO);
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
				OrdendevengolineaTO ordendevengolineaTO=new OrdendevengolineaTO();
				ordendevengolineaTO.getId().setId(id);
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordendevengolineaTO,ordendevengolineaTO.getJsonConfig()));
				OrdendevengoTO ordendevengoTO=UtilSession.planificacionServicio.transObtenerOrdendevengoTO(id);
				request.getSession().setAttribute(ConstantesSesion.ORDENGASTO, ordendevengoTO.getOrdengasto());
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
		log.println("entra al metodo recuperar: "  + id + " - " + id2);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Certificacion
			if(clase.equals("certificacion")){
				ConsultasUtil.obtenercertificacion(id, jsonObject);
			}
			//Certificacion
			if(clase.equals("certificacionbusqueda")){
				ConsultasUtil.obtenercertificacionbusqueda(id, jsonObject);
			}

			//Certificacionlinea
			else if(clase.equals("certificacionlinea")){
				CertificacionlineaTO certificacionlineaTO = UtilSession.planificacionServicio.transObtenerCertificacionlineaTO(new CertificacionlineaID(id, id2));
				certificacionlineaTO.setNpvalor(certificacionlineaTO.getValor());
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(certificacionlineaTO.getNivelactid(), jsonObject, mensajes);
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(certificacionlineaTO.getNivelactid()));
				//1. traigo el total disponible del subitem
				//double total=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid(),false);
				//2. Obtengo el detalle del subitem
//				SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				double saldo=ConsultasUtil.obtenersaldodisponiblelineacertificacion(nivelactividadTO.getTablarelacionid(),certificacionlineaTO.getNivelactid(),certificacionlineaTO.getCertificacion().getFechacreacion(),certificacionlineaTO);
				certificacionlineaTO.setNpvalorinicial(saldo);
				jsonObject.put("certificacionlinea", (JSONObject)JSONSerializer.toJSON(certificacionlineaTO,certificacionlineaTO.getJsonConfig()));
			}
			//Ordengasto
			else if(clase.equals("ordengasto")){
				ConsultasUtil.obtenerordengasto(id, jsonObject);
			}
			//Ordengastolinea
			else if(clase.equals("ordengastolinea")){
				OrdengastolineaTO ordengastolineaTO = UtilSession.planificacionServicio.transObtenerOrdengastolineaTO(new OrdengastolineaID(id, id2));
				ordengastolineaTO.setNpvalor(ordengastolineaTO.getValor());
				log.println("nivelactividad id " + ordengastolineaTO.getNivelactid());
//				//obtengo el saldo de la certificacion
//				Collection<CertificacionlineaTO> resultado=UtilSession.planificacionServicio.transObtienesubitemporcertificacion(ordengastolineaTO.getOrdengasto().getOrdengastocertificacionid());
//				for(CertificacionlineaTO linea:resultado) {
//					if(linea.getNivelactid().longValue()==ordengastolineaTO.getNivelactid().longValue()) {
//						ordengastolineaTO.setNpsaldocertificacion(linea.getNpdisponible()-ordengastolineaTO.getValor().doubleValue());
//						break;
//					}
//				}
				jsonObject.put("ordengastolinea", (JSONObject)JSONSerializer.toJSON(ordengastolineaTO,ordengastolineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordengastolineaTO.getNivelactid(), jsonObject, mensajes);
			}
			//Ordendevengo
			else if(clase.equals("ordendevengo")){
				ConsultasUtil.obtenerordendevengo(id, jsonObject);
			}
			//Ordendevengolinea
			else if(clase.equals("ordendevengolinea")){
				OrdendevengolineaTO ordendevengolineaTO = UtilSession.planificacionServicio.transObtenerOrdendevengolineaTO(new OrdendevengolineaID(id, id2));
				ordendevengolineaTO.setNpvalor(ordendevengolineaTO.getValor());
				//1. traigo el total disponible del subitem
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(ordendevengolineaTO.getNivelactid()));
				//double total=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid(),true);
//				//2. Obtengo el detalle del subitem
//				double saldo=ConsultasUtil.obtenersaldodisponible(total, nivelactividadTO.getTablarelacionid(), ordendevengolineaTO.getNivelactid(),ordendevengolineaTO.getOrdendevengo().getFechacreacion());
//				ordendevengolineaTO.setNpsaldo(saldo);
				jsonObject.put("ordendevengolinea", (JSONObject)JSONSerializer.toJSON(ordendevengolineaTO,ordendevengolineaTO.getJsonConfig()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(ordendevengolineaTO.getNivelactid(), jsonObject, mensajes);

			}
			//Ordenreversion
			else if(clase.equals("ordenreversion")){
				ConsultasUtil.obtenerordenreversion(id, jsonObject);
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
				double total=ConsultasUtil.obtenertotalsubitem(id,true);
				//System.out.println("total***: " + total);
				//2. Obtengo el detalle del subitem
				//double saldo=ConsultasUtil.obtenersaldodisponible(total, id,id2,new Date());
				double saldo=ConsultasUtil.obtenersaldodisponibleactual(id, id2, new Date());
				System.out.println("saldo*** " + saldo);
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
//				SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
//				subitemunidadacumuladorTO.getId().setId(id);
//				subitemunidadacumuladorTO.setTipo("A");
//				subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
//				Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
//				if(subitemunidadacumuladorTOs.size()>0) {
//					subitemunidadacumuladorTO=(SubitemunidadacumuladorTO)subitemunidadacumuladorTOs.iterator().next();
//					saldodisponible.put("valorajustado", subitemunidadacumuladorTO.getTotal());
//				}
				saldodisponible.put("valorajustado", total);
				saldodisponible.put("saldo", saldo);
				jsonObject.put("valordisponiblesi", (JSONObject)JSONSerializer.toJSON(saldodisponible));
			}
			
			//datoslineaordend: Obtiene el saldo disponible de la certificacion y el valor de las ordenes no aprobadas
			else if(clase.equals("datoslineaordend")) {
				//1. traigo el total disponible del subitem
				//..double total=ConsultasUtil.obtenertotalsubitem(id);
				//2. Obtengo el detalle del subitem
				//double saldo=ConsultasUtil.obtenersaldodisponible(total, id, id2,new Date());
				//3. Obtengos las ordenes pendientes de este nivel
				log.println("id para calculo de no aprobadas:  " + id2);
				OrdengastoTO ordengastoTO=(OrdengastoTO) request.getSession().getAttribute(ConstantesSesion.ORDENGASTO);
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(ordengastoTO.getId());
				ordengastolineaTO.setNivelactid(id2);
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
				ordengastolineaTO=ordengastolineaTOs.iterator().next();
				Collection<OrdendevengolineaTO> pendientes=UtilSession.planificacionServicio.transObtieneordenesdevengopendientes(id2,ordengastoTO.getId());
				log.println("ordenes no aprobadas " + pendientes.size());
				double ordenesnoaprob=0.0;
				for(OrdendevengolineaTO ordendevengolineaTO:pendientes)
					ordenesnoaprob=ordenesnoaprob+ordendevengolineaTO.getValor();
				log.println("ordenesnoaprob " +ordenesnoaprob);
				Collection<OrdenreversionlineaTO> pendientesrev=UtilSession.planificacionServicio.transObtieneordenesreversionpendientes(id2,ordengastoTO.getId());
				log.println("ordenes reversion no aprobadas " + pendientes.size());
				double ordenesnoaprobrev=0.0;
				for(OrdenreversionlineaTO ordenreversionlineaTO:pendientesrev)
					ordenesnoaprobrev=ordenesnoaprobrev+ordenreversionlineaTO.getValor();

				//4. Consulto las ordenes de devengo aprobada
				Collection<OrdendevengolineaTO> aprobadas=UtilSession.planificacionServicio.transObtieneordenesdevengoaprobadas(id2,ordengastoTO.getId());
				log.println("aprobadas "+ aprobadas.size());
				double ordenesaprobadas=0.0;
				for(OrdendevengolineaTO aprobada:aprobadas)
					ordenesaprobadas=ordenesaprobadas+aprobada.getValor();
				log.println("valor aprobadas " + ordenesaprobadas);
				Collection<OrdenreversionlineaTO> aprobadasrev=UtilSession.planificacionServicio.transObtieneordenesreversionaprobadas(id2, null, null, ordengastoTO.getId());
				log.println("aprobadas rev"+ aprobadasrev.size());
				double ordenesaprobadasrev=0.0;
				for(OrdenreversionlineaTO aprobadarev:aprobadasrev)
					ordenesaprobadasrev=ordenesaprobadasrev+aprobadarev.getValor();
				log.println("valor aprobadas " + ordenesaprobadasrev);

				//Traigo el valor total de la linea de la orden de gasto
//				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
//				ordengastolineaTO.setNivelactid(id2);
//				ordengastolineaTO.getId().setId(ordengastoTO.getId());
//				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO, false);
//				ordengastolineaTO=(OrdengastolineaTO)ordengastolineaTOs.iterator().next();
//				double saldo=ordengastolineaTO.getValor()-ordenesnoaprob-ordenesaprobadas;
				double saldo=ordengastolineaTO.getValor()-ordenesnoaprob-ordenesaprobadas-ordenesnoaprobrev-ordenesaprobadasrev;
				Map<String, Double> saldodisponible=new HashMap<>();
				saldodisponible.put("saldo", saldo);
				saldodisponible.put("noaprobadas", ordenesnoaprob);
				saldodisponible.put("aprobadas", ordenesaprobadas);
				saldodisponible.put("noaprobadasreversion", ordenesnoaprobrev);
				saldodisponible.put("aprobadasreversion", ordenesaprobadasrev);
				jsonObject.put("datoslineaordend", (JSONObject)JSONSerializer.toJSON(saldodisponible));
			}
			//Reforma
			else if(clase.equals("reforma")){
				ConsultasUtil.obtenerreforma(id, jsonObject);
			}
			//Reformalinea
			else if(clase.equals("reformalinea")){
				ReformalineaTO reformalineaTO = UtilSession.planificacionServicio.transObtenerReformalineaTO(new ReformalineaID(id, id2));
				reformalineaTO.setNpvalordecremento(reformalineaTO.getValordecremento());
				reformalineaTO.setNpvalorincremento(reformalineaTO.getValorincremento());
				reformalineaTO.setNpfechacreacion(UtilGeneral.parseDateToString(reformalineaTO.getReforma().getFechacreacion()));
				jsonObject=ConsultasUtil.consultaInformacionsubitemunidad(reformalineaTO.getNivelactid(), jsonObject, mensajes);
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformalineaTO.getNivelactid()));
				//1. traigo el total disponible del subitem
				//..double total=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid());
				//2. Obtengo el detalle del subitem
				//SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(id);
				//..double valtotal=ConsultasUtil.obtenertotalsubitem(total, nivelactividadTO.getTablarelacionid(),reformalineaTO.getNivelactid(),reformalineaTO);
				double valtotalsubitem=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid(),false);
				System.out.println("valtotalsubitem "+valtotalsubitem);
				double valtotal=0.0;
				//..ReformaTO reformaTO=UtilSession.planificacionServicio.transObtenerReformaTO(reformalineaTO.getId().getId());
				//Collection<ReformalineaTO> resultado=UtilSession.planificacionServicio.transObtienereformalinea(reformalineaTO.getId().getId());
				//System.out.println("fecha: " + reformaTO.getFechacreacion());
				System.out.println("reformaid: " + reformalineaTO.getId().getId());
				Collection<ReformalineaTO> reformalineaTO1s=UtilSession.planificacionServicio.transObtienereformasnoelne(reformalineaTO.getNivelactid());
				double totalreforma=0.0;
				//  System.out.println("reformalinea.getNpSubitemvalor(): " + reformalinea.getNpSubitemvalor() + " id " +reformalinea.getNpreformaid());
				for(ReformalineaTO reformalinea1TO:reformalineaTO1s){
//						if(reformalinea1TO.getReforma().getEstado().equals("AP") && reformalinea1TO.getReforma().getFechacreacion().compareTo(fechacreacion)<=0
					if((reformalineaTO.getReforma().getEstado().equals("SO") || reformalineaTO.getReforma().getEstado().equals("RE")) && reformalineaTO.getReforma().getId().longValue()!=reformalinea1TO.getId().getId().longValue()){
//								&& reformalinea.getNpreformaid().longValue()<reformalinea1TO.getId().getId().longValue()){
						System.out.println("valor:/// " + reformalinea1TO.getValorincremento() + ", " + reformalinea1TO.getValordecremento()+" id "+reformalinea1TO.getId().getId());
						totalreforma=totalreforma+reformalinea1TO.getValorincremento().doubleValue()-reformalinea1TO.getValordecremento().doubleValue();
					}
					else if(reformalineaTO.getReforma().getId().longValue()!=reformalinea1TO.getId().getId().longValue() && reformalineaTO.getReforma().getEstado().equals("AP") && reformalinea1TO.getReforma().getFechacreacion().compareTo(reformalineaTO.getReforma().getFechaaprobacion())<=0){
//								&& reformalinea.getNpreformaid().longValue()<reformalinea1TO.getId().getId().longValue()){
						System.out.println("valor:***** " + reformalinea1TO.getValorincremento() + ", " + reformalinea1TO.getValordecremento()+" id "+reformalinea1TO.getId().getId());
						totalreforma=totalreforma+reformalinea1TO.getValorincremento().doubleValue()-reformalinea1TO.getValordecremento().doubleValue();
					}
				}
//				
//				
//				for(ReformalineaTO reformalinea1TO:reformalineaTO1s){
//					System.out.println("idreforma: " + reformalinea1TO.getId().getId()+"fecha reforma comparar: " + reformalinea1TO.getNpfechacreacion());
//					if((reformalinea1TO.getReforma().getEstado().equals("AP")) && reformalinea1TO.getId().getId().longValue()<reformalineaTO.getId().getId().longValue()){
////					if(reformalinea1TO.getReforma().getEstado().equals("AP") && reformalinea1TO.getReforma().getFechacreacion().compareTo(reformaTO.getFechacreacion())<=0
////							&& reformalineaTO.getId().getId().longValue()<reformalinea1TO.getId().getId().longValue()){
//						//System.out.println("valor: " + reformalinea1TO.getValorincremento() + ", " + reformalinea1TO.getValordecremento()+" id "+reformalinea1TO.getId().getId());
//						totalreforma=totalreforma+reformalinea1TO.getValorincremento().doubleValue()-reformalinea1TO.getValordecremento().doubleValue();
//					}
//				}
				System.out.println("total**: " + totalreforma);
				valtotal=valtotalsubitem + totalreforma;

				//2. Obtengo el detalle del subitem
				//double saldo=ConsultasUtil.obtenersaldodisponible(valtotal, nivelactividadTO.getTablarelacionid(), reformalineaTO.getNivelactid(),reformalineaTO.getReforma().getFechacreacion());
				double saldo=ConsultasUtil.obtenersaldodisponiblelineareforma(nivelactividadTO.getTablarelacionid(), nivelactividadTO.getId(), reformalineaTO,true);
				//saldo=saldo+reformalineaTO.getValordecremento()-reformalineaTO.getValorincremento();
				log.println("saldo: " + saldo);
				//double saldo=ConsultasUtil.obtenersaldodisponible(total, nivelactividadTO.getTablarelacionid(),reformalineaTO.getNivelactid());
				reformalineaTO.setNpsaldo(UtilGeneral.redondear(saldo,2));

				reformalineaTO.setNpvalortotal(valtotal);
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformalineaTO,reformalineaTO.getJsonConfig()));
			}

			//Reformameta
			else if(clase.equals("reformameta")){
				ConsultasUtil.obtenerreformameta(id, jsonObject);
			}
			//Reformametalinea
			else if(clase.equals("reformametalinea")){
				ReformametalineaTO reformametalineaTO = UtilSession.planificacionServicio.transObtenerReformametalineaTO(new ReformametalineaID(id, id2));
				reformametalineaTO.setNpdecremento(reformametalineaTO.getValordecremento());
				reformametalineaTO.setNpincremento(reformametalineaTO.getValorincremento());
				jsonObject=ConsultasUtil.consultaInformacionsubtarea(reformametalineaTO.getNivelactid(), jsonObject, mensajes);
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformametalineaTO.getNivelactid()));
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
					reformametalineaTO.setNpunidadmedida(subtareaunidadacumuladorTO.getSubtareaunidad().getUnidadmedidaTO().getNombre());
				}
				reformametalineaTO.setNpunidadmedida(subtareaunidadacumuladorTO.getSubtareaunidad().getUnidadmedidaTO().getNombre());
				double saldo=ConsultasUtil.obtenersaldodisponiblesubtarea(subtareaunidadacumuladorTOs,reformametalineaTO.getNivelactid(),reformametalineaTO.getReformameta().getFechacreacion());
				reformametalineaTO.setCodificado(saldo);
				//reformametalineaTO.setNpvalorinicial(saldo);
				jsonObject.put("reformametalinea", (JSONObject)JSONSerializer.toJSON(reformametalineaTO,reformametalineaTO.getJsonConfig()));
			}
			
			//datossubtarea: Recibo el id de la subtareaunidad y el id del nivelactividadid para buscar la unidadmedida y la descripcion y valor actual de la meta
			else if(clase.equals("datossubtarea")) {
				//1. Consulto la subtareaunidadacumulador y la subtarea para traer los datos necesarios
				System.out.println("va a consultar datos subtarea: " + id);
				SubtareaunidadTO subtareaunidadTO=new SubtareaunidadTO();
				subtareaunidadTO.setId(id);
				subtareaunidadTO.setUnidadmedidaTO(new UnidadmedidaTO());
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.setSubtareaunidad(subtareaunidadTO);
				//subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTO.setOrderByField(OrderBy.orderDesc("id.acumid"));
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				System.out.println("subtareaunidadacumuladorTOs "+subtareaunidadacumuladorTOs.size());
				if(subtareaunidadacumuladorTOs.size()>0) {
					//busco las lineas agregadas para sumar incrementos y restar decrementos
					Map<String, Double> valoractual=new HashMap<>();
					Map<String, String> descripciones=new HashMap<>();
					subtareaunidadacumuladorTO=(SubtareaunidadacumuladorTO)subtareaunidadacumuladorTOs.iterator().next();
					double saldo=ConsultasUtil.obtenersaldodisponiblesubtarea(subtareaunidadacumuladorTOs,id,null);

					System.out.println("metadescripcion: " +subtareaunidadacumuladorTO.getDescripcion());
					System.out.println("unidadmedida: " +subtareaunidadacumuladorTO.getSubtareaunidad().getUnidadmedidaTO().getNombre());
					System.out.println("valor: " +saldo);
					descripciones.put("metadescripcion", subtareaunidadacumuladorTO.getDescripcion());
					descripciones.put("unidadmedida", subtareaunidadacumuladorTO.getSubtareaunidad().getUnidadmedidaTO().getNombre());
					valoractual.put("valor", saldo);
					jsonObject.put("descripciones", (JSONObject)JSONSerializer.toJSON(descripciones));
					jsonObject.put("valoractual", (JSONObject)JSONSerializer.toJSON(valoractual));
					System.out.println("json " + jsonObject.toString());
				}
			}
			
			//Reformametasubtarea
			else if(clase.equals("reformametasubtarea")){
				ReformametasubtareaTO reformametasubtareaTO = UtilSession.planificacionServicio.transObtenerReformasubtareaTO(new ReformametasubtareaID(id, id2));
				reformametasubtareaTO.setNpdecremento(reformametasubtareaTO.getValordecremento());
				reformametasubtareaTO.setNpincremento(reformametasubtareaTO.getValorincremento());
				NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformametasubtareaTO.getNivelactid()));
				MatrizDetalle matrizDetalle=UtilSession.planificacionServicio.transObtienedetallesubtarea(null, nivelactividadTO.getId());
				
				reformametasubtareaTO.setNpunidadmedida(matrizDetalle.getNpunidadmedida());
				//1. traigo los datos de unidad medida y meta descripcion
				SubtareaunidadTO subtareaunidadTO=new SubtareaunidadTO();
				subtareaunidadTO.setId(nivelactividadTO.getTablarelacionid());
				subtareaunidadTO.setUnidadmedidaTO(new UnidadmedidaTO());
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.setSubtareaunidad(subtareaunidadTO);
				subtareaunidadacumuladorTO.getId().setAcumid(2L);
				//subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTO.setOrderByField(OrderBy.orderDesc("id.acumid"));
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				if(subtareaunidadacumuladorTOs.size()>0) {
					subtareaunidadacumuladorTO=(SubtareaunidadacumuladorTO)subtareaunidadacumuladorTOs.iterator().next();
					//reformametasubtareaTO.setCodificado(subtareaunidadacumuladorTO.getCantidad());
					reformametasubtareaTO.setNpmetadescripcion(subtareaunidadacumuladorTO.getDescripcion());
				}
				double saldo=ConsultasUtil.obtenersaldodisponiblereformasubtarea(subtareaunidadacumuladorTOs,reformametasubtareaTO.getNivelactid(),reformametasubtareaTO.getReforma().getFechacreacion());
				reformametasubtareaTO.setCodificado(saldo);
				System.out.println("descripcion: " + reformametasubtareaTO.getNpmetadescripcion());
				System.out.println("unidad: " + reformametasubtareaTO.getNpunidadmedida());
				jsonObject.put("reformametasubtarea", (JSONObject)JSONSerializer.toJSON(reformametasubtareaTO,reformametasubtareaTO.getJsonConfig()));
				jsonObject.put("subtareainfo", (JSONObject)JSONSerializer.toJSON(matrizDetalle,matrizDetalle.getJsonConfigsubitemarbol()));
			}
			//trae las actividades por unidad
			else if(clase.equals("actividadunidad")){
				Collection<ActividadTO> actividadTOs=UtilSession.planificacionServicio.transObtieneActividadesporunidad(id, id2);
				System.out.println("actividades: " + actividadTOs.size());
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(actividadTOs,new ActividadTO().getJsonConfigActividadunidad()));
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", Integer.valueOf(actividadTOs.size()).toString());
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}


			System.out.println("json retornado: " + jsonObject.toString());
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
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			boolean continuar=true;
			if(tipo.equals("SO")) {
				//obtengo las lineas
				CertificacionlineaTO certificacionlineaTO=new CertificacionlineaTO();
				certificacionlineaTO.getId().setId(certificacionTO.getId());
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO);
				if(certificacionlineaTOs.size()==0) {
					continuar=false;
					mensajes.setMsg("Debe existir al menos una linea creada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			if(continuar) {
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
					else if(tipo.equals("SO")) {
						certificacionTO.setFechasolicitud(new Date());
					}
					else if(tipo.equals("AP")) {
						certificacionTO.setCertificacionemprevisaid(UtilSession.getUsuario(request).getSocionegocioid());
						certificacionTO.setCertificacionempapruebaid(UtilSession.getUsuario(request).getSocionegocioid());
					}

					UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
					//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
					ComunController.crearAuditoria(request, "CERTIFICACION", "U", objeto, id.toString());
					mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
					mensajes.setType(MensajesWeb.getString("mensaje.exito"));
		//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
				}
				//Verifico que no tenga atada una orden de gasto
				else if(tipo.equals("LT")) {
					certificacionTO.setEstado("AN");
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
							mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
							mensajes.setType(MensajesWeb.getString("mensaje.exito"));
						}
					}
					else {
						if(parameters.get("observacion")!=null)
							certificacionTO.setMotivoliquidacion(parameters.get("observacion"));
						UtilSession.planificacionServicio.transCrearModificarCertificacion(certificacionTO,tipo);
						mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
						mensajes.setType(MensajesWeb.getString("mensaje.exito"));
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
							else if(ordengastoTO2.getEstado().equals("RE") || ordengastoTO2.getEstado().equals("SO")){
								aprobada=false;
								break;
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
							mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
							mensajes.setType(MensajesWeb.getString("mensaje.exito"));
						}
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
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			ordengastoTO.setNpcodigoregistro(ordengastoTO.getClaseregistrocmcgasto().getClaseregistroclasemodificacion().getClaseregistro().getCodigo());
			boolean continuar=true;
			if(tipo.equals("SO")) {
				//obtengo las lineas
				OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
				ordengastolineaTO.getId().setId(ordengastoTO.getId());
				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO, false);
				if(ordengastolineaTOs.size()==0) {
					continuar=false;
					mensajes.setMsg("Debe existir al menos una linea creada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			if(continuar) {
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
					else if(tipo.equals("AP")) {
						ordengastoTO.setOrdengastoempapruebaid(UtilSession.getUsuario(request).getSocionegocioid());
						ordengastoTO.setOrdengastoemprevisaid(UtilSession.getUsuario(request).getSocionegocioid());
					}
					else if(tipo.equals("AN")) {
						//valido que no tenga ordenes de devengo aprobadas o en proceso para poder anular
						OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
						ordendevengoTO.setOrdendevengoordengastoid(ordengastoTO.getId());
						Collection<OrdendevengoTO> ordendevengoTOs=UtilSession.planificacionServicio.transObtenerOrdendevengo(ordendevengoTO);
						for(OrdendevengoTO ordendevengoTO2:ordendevengoTOs){
							if(ordendevengoTO2.getEstado().equals("RE") || ordendevengoTO2.getEstado().equals("SO") || ordendevengoTO2.getEstado().equals("AP")){
								mensajes.setMsg("No se puede anular la orden porque existen ordenes de devengo");
								mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
								respuesta.setEstado(false);
								break;
							}
						}
						//valido que si la certificacion esta en estado LT o LP no se pueda anular
						CertificacionTO certificacionTO=UtilSession.planificacionServicio.transObtenerCertificacionTO(ordengastoTO.getOrdengastocertificacionid());
						if(certificacionTO.getEstado().equals("LP")){
							mensajes.setMsg("No se puede anular la orden porque la certificacion esta en estado Liquidado");
							mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
							respuesta.setEstado(false);
						}
						
						if(parameters.get("observacion")!=null)
							ordengastoTO.setMotivoanulacion(parameters.get("observacion"));
					}
					if(mensajes.getMsg()==null){
						UtilSession.planificacionServicio.transCrearModificarOrdengasto(ordengastoTO, tipo);
						ComunController.crearAuditoria(request, "ORDENGASTO", "U", objeto, id.toString());
						//FormularioUtil.crearAuditoria(request, clase, "Eliminar", "", id.toString());
						mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
						mensajes.setType(MensajesWeb.getString("mensaje.exito"));
					}
		//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
				}
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
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
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			boolean continuar=true;
			if(tipo.equals("SO")) {
				//obtengo las lineas
				OrdendevengolineaTO ordendevengolineaTO=new OrdendevengolineaTO();
				ordendevengolineaTO.getId().setId(ordendevengoTO.getId());
				Collection<OrdendevengolineaTO> ordendevengolineaTOs=UtilSession.planificacionServicio.transObtenerOrdendevengolinea(ordendevengolineaTO);
				if(ordendevengolineaTOs.size()==0) {
					continuar=false;
					mensajes.setMsg("Debe existir al menos una linea creada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			if(continuar) {
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
					else if(tipo.equals("AP")) {
						ordendevengoTO.setOrdendevengoempapruebaid(UtilSession.getUsuario(request).getSocionegocioid());
						ordendevengoTO.setOrdendevengoemprevisaid(UtilSession.getUsuario(request).getSocionegocioid());
					}
					UtilSession.planificacionServicio.transCrearModificarOrdendevengo(ordendevengoTO, tipo);
					ComunController.crearAuditoria(request, "ORDENDEVENGO", "U", objeto, id.toString());
					mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
					mensajes.setType(MensajesWeb.getString("mensaje.exito"));
		//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
				}
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
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
//			respuesta.setMensajes(mensajes);
//
//		}
		log.println("devuelve**** " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
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
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			ordenreversionTO.setEstado(tipo);
			if(tipo.equals("EL")) {
				if(parameters.get("observacion")!=null)
					ordenreversionTO.setMotivoeliminacion(parameters.get("observacion"));
			}
			else if(tipo.equals("NE")) {
				if(parameters.get("observacion")!=null)
					ordenreversionTO.setMotivonegacion(parameters.get("observacion"));
			}
			else if(tipo.equals("AP")) {
				ordenreversionTO.setOrdenreversionempapruebaid(UtilSession.getUsuario(request).getSocionegocioid());
				ordenreversionTO.setOrdenreversionemprevisaid(UtilSession.getUsuario(request).getSocionegocioid());
			}
			UtilSession.planificacionServicio.transCrearModificarOrdenreversion(ordenreversionTO, tipo);
			ComunController.crearAuditoria(request, "ORDENREVERSION", "U", objeto, id.toString());
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
//			respuesta.setMensajes(mensajes);
//
//		}
		log.println("devuelve**** " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}

	@RequestMapping(value = "/flujoreforma/{id}/{tipo}", method = RequestMethod.POST)
	public Respuesta flujoreforma(@PathVariable Long id,@PathVariable String tipo,@RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo flujo: " + id);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		JSONObject jsonObject=new JSONObject();
		Gson gson = new Gson();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			ReformaTO reformaTO=UtilSession.planificacionServicio.transObtenerReformaTO(id);
			System.out.println("reforma: " + reformaTO);
			System.out.println("reforma id: " + reformaTO.getId());
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			boolean continuar=true;
			//obtengo las lineas
			ReformalineaTO reformalineaTO=new ReformalineaTO();
			reformalineaTO.getId().setId(reformaTO.getId());
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO, null);
			if(tipo.equals("SO")) {
				if(reformalineaTOs.size()==0) {
					continuar=false;
					mensajes.setMsg("Debe existir al menos una linea creada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			//si va a solicitar debo validar que de acuerdo al tipo que escogio tenga el mismo incremento y el mismo decremento
			if(tipo.equals("SO") && (reformaTO.getTipo().equals("MU") || reformaTO.getTipo().equals("ES"))){
				double totaldecremento=0.0;
				double totalincremento=0.0;
				for(ReformalineaTO reformalineaTO2:reformalineaTOs){
					totaldecremento=totaldecremento+reformalineaTO2.getValordecremento();
					totalincremento=totalincremento+reformalineaTO2.getValorincremento();
				}
				if(totalincremento-totaldecremento!=0){
					continuar=false;
					mensajes.setMsg("El valor del incremento debe ser igual a decremento");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					respuesta.setEstado(false);
				}
			}
			if(tipo.equals("SO")){
				//verifico que esten hechas las distribuciones
				String resultado="";
				if(reformaTO.getId()!=null && reformaTO.getId().longValue()!=0){
					resultado=UtilSession.planificacionServicio.transListareformalineasdistribucion(reformaTO.getId());
				}
				if(!resultado.equals("")){
					mensajes.setMsg("Realice la distribucion de los subitems: " + resultado);
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					continuar=false;
					respuesta.setEstado(false);
				}
				else{
					resultado=UtilSession.planificacionServicio.transListareformalineasdistribucionrms(reformaTO);
					if(!resultado.equals("")){
						mensajes.setMsg("Realice la distribucion de las subtareas: " + resultado);
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
						respuesta.setEstado(false);
						continuar=false;
						System.out.println("mensaje: " + mensajes.getMsg());
					}
				}
			}
			if(continuar) {
				if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP")) {
					reformaTO.setEstado(tipo);
					if(tipo.equals("AP")) {
						System.out.println(" - socionegocio: " + UtilSession.getUsuario(request).getSocionegocioid());
						reformaTO.setReformaempapruebaid(UtilSession.getUsuario(request).getSocionegocioid());
						reformaTO.setReformaemprevisaid(UtilSession.getUsuario(request).getSocionegocioid());
						reformaTO.setFechaaprobacion(new Date());
					}
					else if(tipo.equals("EL")) {
						if(parameters.get("observacion")!=null)
							reformaTO.setMotivoeliminacion(parameters.get("observacion"));
						reformaTO.setFechaeliminacion(new Date());
					}
					else if(tipo.equals("NE")) {
						if(parameters.get("observacion")!=null)
							reformaTO.setMotivonegacion(parameters.get("observacion"));
						reformaTO.setFechanegacion(new Date());
					}
					if(mensajes.getMsg()==null || mensajes.getMsg().equals("")){
						UtilSession.planificacionServicio.transCrearModificarReforma(reformaTO, tipo);
						ComunController.crearAuditoria(request, "REFORMA", "U", objeto, id.toString());
						mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
						mensajes.setType(MensajesWeb.getString("mensaje.exito"));
					}
		//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
				}
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			mensajes.setMsg(MensajesWeb.getString("error.guardar"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
//		if(mensajes.getMsg()!=null){dd
//			//System.out.println("tiene mensajes");
//			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
//			respuesta.setMensajes(mensajes);
//			log.println("existen mensajes");
//		}
		System.out.println("devuelve**** " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
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
			ReformametaTO reformametaTO=UtilSession.planificacionServicio.transObtenerReformametaTO(id);
			request.getSession().setAttribute(ConstantesSesion.VALORANTIGUO, jsonObject.toString());
			boolean continuar=true;
			if(tipo.equals("SO")) {
				//obtengo las lineas
				ReformametalineaTO reformametalineaTO=new ReformametalineaTO();
				reformametalineaTO.getId().setId(reformametaTO.getId());
				Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO,null);
				if(reformametalineaTOs.size()==0) {
					continuar=false;
					mensajes.setMsg("Debe existir al menos una linea creada");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}
			//verifico que esten hechas las distribuciones
			String resultado="";
			if(reformametaTO.getId()!=null && reformametaTO.getId().longValue()!=0){
				resultado=UtilSession.planificacionServicio.transListareformalineasdistribucionrm(reformametaTO.getId());
			}
			if(!resultado.equals("")){
				mensajes.setMsg("Realice la distribucion de las subtareas: " + resultado);
				mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				continuar=false;
			}

			if(continuar) {
				if(tipo.equals("SO") || tipo.equals("EL") || tipo.equals("NE") || tipo.equals("AP") || tipo.equals("AN")) {
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
					UtilSession.planificacionServicio.transCrearModificarReformameta(reformametaTO, tipo);
					//ComunController.crearAuditoria(request, "REFORMAMETA", "U", objeto, id.toString());
					mensajes.setMsg(MensajesWeb.getString("mensaje.flujo.exito"));
					mensajes.setType(MensajesWeb.getString("mensaje.exito"));
		//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
				}
	//			UtilSession.planificacionServicio.transCrearModificarAuditoria(auditoriaTO);
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
//			respuesta.setMensajes(mensajes);
//			log.println("existen mensajes");
//		}
		log.println("devuelve**** " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	
	}

	@RequestMapping(value = "/consultar/{clase}", method = RequestMethod.POST)
	public String consultarpost(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request, Principal principal){
		log.println("entra al metodo consultar: " + objeto);
		Mensajes mensajes=new Mensajes();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		Respuesta respuesta=new Respuesta();
		try {
			Map<String, String> parameters= gson.fromJson(new StringReader(objeto), Map.class);
			//Certificacion
			if(clase.equals("certificacion")){
				jsonObject=ConsultasUtil.consultaCertificacionPaginado(parameters, jsonObject, mensajes,principal);
			}
			
			//Orden de gasto
			else if(clase.equals("ordengasto")){
				jsonObject=ConsultasUtil.consultaOrdengastoPaginado(parameters, jsonObject, mensajes,principal);
			}
			
			//Orden de devengo
			else if(clase.equals("ordendevengo")){
				jsonObject=ConsultasUtil.consultaOrdendevengoPaginado(parameters, jsonObject, mensajes,principal);
			}
			
			//Orden de reversion
			else if(clase.equals("ordenreversion")){
				jsonObject=ConsultasUtil.consultaOrdenreversionPaginado(parameters, jsonObject, mensajes,principal);
			}

			//Reforma
			else if(clase.equals("reforma")){
				jsonObject=ConsultasUtil.consultaReformaPaginado(parameters, jsonObject, mensajes,principal);
			}

			//Reformameta
			else if(clase.equals("reformameta")){
				request.getSession().removeAttribute(ConstantesSesion.REFORMA);
				jsonObject=ConsultasUtil.consultaReformametaPaginado(parameters, jsonObject, mensajes,principal);
			}

			//Certificacion busqueda
			else if(clase.equals("certificacionbusqueda")){
				//jsonObject=ConsultasUtil.consultaCertificacionBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.certificacionbusqueda(parameters, jsonObject,principal);
			}
			
			//Ordengasto busqueda
			else if(clase.equals("ordengastobusqueda")){
				//jsonObject=ConsultasUtil.consultaOrdengastoBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.ordenesgastobusqueda(parameters, jsonObject,principal);
			}
			
			//ejecucion de metas actividadesEjecucionMetas
			else if(clase.equals("actividadesEjecucionMetas")){
				//jsonObject=ConsultasUtil.consultaOrdengastoBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.consultaActividadesEjecucionMetas(parameters, jsonObject, mensajes);
			}
			
			//ejecucion de metas actividadesEjecucionMetas
			else if(clase.equals("subtareasEjecucionMetas")){
				jsonObject=ConsultasUtil.consultaSubtareaEjecucionMetas(parameters, jsonObject, mensajes);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			mensajes.setMsg("Error al realizar la consulta");
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			respuesta.setEstado(false);
			//throw new MyException(e);
		}
		log.println("existe mensaje: " + mensajes.getMsg());
		if(mensajes.getMsg()!=null)
			//jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
			respuesta.setMensajes(mensajes);
		//System.out.println("json retornado: " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		
		//return respuesta;
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

			if(clase.equals("actividadesEjecucionMetas")){
				//jsonObject=ConsultasUtil.consultaOrdengastoBusquedaPaginado(parameters, jsonObject, mensajes);
				jsonObject=ConsultasUtil.consultaActividadesEjecucionMetas(parameters, jsonObject, mensajes);
			}

			//ejecucion de metas actividadesEjecucionMetas
			else if(clase.equals("subtareasEjecucionMetas")){
				jsonObject=ConsultasUtil.consultaSubtareaEjecucionMetas(parameters, jsonObject, mensajes);
			}
			
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
				OrdengastoTO ordengastoTO=UtilSession.planificacionServicio.transObtenerOrdengastoTO(Long.valueOf(parameters.get("ordengastoid")));
				request.getSession().setAttribute(ConstantesSesion.ORDENGASTO, ordengastoTO);
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
		log.println("entra al metodo eliminar: " + id + " - " + id2);
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
			//reformametalinea
			else if(clase.equals("reformametalinea")){
				UtilSession.planificacionServicio.transEliminarReformametalinea(new ReformametalineaTO(new ReformametalineaID(id, id2)));
			}

			//reformametasubtarea
			else if(clase.equals("reformametasubtarea")){
				UtilSession.planificacionServicio.transEliminarReformasubtarea(new ReformametasubtareaTO(new ReformametasubtareaID(id, id2)));
			}

			//ComunController.crearAuditoria(request, clase, "D", "", id.toString());
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

	/**
	 * obtienecronogramareforma retorna o graba la distribucion de metas de reformas o de reformameta
	 * @param tipo indica si es reforma o reformameta (r/rm)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/metareforma/{tipo}/{ejerciciofiscal}", method = RequestMethod.POST)
	public Respuesta obtienecronogramareforma(@PathVariable String tipo,@PathVariable String ejerciciofiscal, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo: " +tipo +" "+ " - "+ objeto);
		Mensajes mensajes=new Mensajes();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		Respuesta respuesta=new Respuesta();
		try {
			//tipo=r y accion=o
			if(tipo.equals("r")){
				ReformalineaTO reformalineaTO= gson.fromJson(new StringReader(objeto), ReformalineaTO.class);
				//NivelactividadTO nivelactividadTO=UtilSession.planificacionServicio.transObtenerNivelactividadTO(new NivelactividadTO(reformalineaTO.getNivelactid()));
				//double valtotal=ConsultasUtil.obtenertotalsubitem(nivelactividadTO.getTablarelacionid(),true);
				ReformaTO reformaTO=UtilSession.planificacionServicio.transObtenerReformaTO(reformalineaTO.getId().getId());
				reformalineaTO.setReforma(reformaTO);
				double valtotal=ConsultasUtil.obtenersaldodisponiblelineareforma(reformalineaTO.getNpSubitemid(), reformalineaTO.getNivelactid(), reformalineaTO,false);
				//double valtotal=ConsultasUtil.obtenersaldodisponibleactual(reformalineaTO.getNpSubitemid(), reformalineaTO.getNivelactid(), UtilGeneral.parseStringToDate(reformalineaTO.getNpfechacreacion()));
				System.out.println("valtotal: " + valtotal);
				System.out.println("relacion: " + reformalineaTO.getNpSubitemid());
				System.out.println("nivelacti: "+reformalineaTO.getNivelactid());
				System.out.println("fecha: "+reformalineaTO.getNpfechacreacion());
				//2. Obtengo el detalle del subitem
				//double saldo=ConsultasUtil.obtenersaldodisponible(valtotal, nivelactividadTO.getTablarelacionid(), reformalineaTO.getNivelactid(),UtilGeneral.parseStringToDate(reformalineaTO.getNpfechacreacion()));
				//..double saldo=ConsultasUtil.obtenercodificadoyreformas(valtotal, reformalineaTO.getNpSubitemid(), reformalineaTO.getNivelactid(),UtilGeneral.parseStringToDate(reformalineaTO.getNpfechacreacion()));

				//..log.println("saldo: " + saldo);
				//double saldo=ConsultasUtil.obtenersaldodisponible(total, nivelactividadTO.getTablarelacionid(),reformalineaTO.getNivelactid());
				System.out.println("saldo: " + valtotal + " inc: " + reformalineaTO.getValorincremento()+" dec: "+reformalineaTO.getValordecremento());
				reformalineaTO.setNpSubitemvalor(valtotal+reformalineaTO.getValorincremento()-reformalineaTO.getValordecremento());
				CronogramaTO cronogramaTO=UtilSession.planificacionServicio.transCronogramarforma(tipo, ejerciciofiscal, reformalineaTO, null,null,reformalineaTO.getNpSubitemvalor());
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformalineaTO,reformalineaTO.getJsonConfig()));
				jsonObject.put("cronograma", (JSONObject)JSONSerializer.toJSON(cronogramaTO,cronogramaTO.getJsonConfig()));
				jsonObject.put("cronogramalinea", (JSONArray)JSONSerializer.toJSON(cronogramaTO.getCronogramalineaTOs(),new CronogramalineaTO().getJsonConfigreforma()));
			}
			//tipo=rm
			if(tipo.equals("rm")){
				ReformametalineaTO reformametalineaTO= gson.fromJson(new StringReader(objeto), ReformametalineaTO.class);
				CronogramaTO cronogramaTO=UtilSession.planificacionServicio.transCronogramarforma(tipo, ejerciciofiscal, null, reformametalineaTO,null,null);
				SubtareaunidadTO subtareaunidadTO=new SubtareaunidadTO();
				subtareaunidadTO.setId(reformametalineaTO.getNpSubtareaid());
				subtareaunidadTO.setUnidadmedidaTO(new UnidadmedidaTO());
				SubtareaunidadacumuladorTO subtareaunidadacumuladorTO=new SubtareaunidadacumuladorTO();
				subtareaunidadacumuladorTO.setSubtareaunidad(subtareaunidadTO);
				//subtareaunidadacumuladorTO.setTipo(MensajesWeb.getString("presupuesto.ajustado"));
				subtareaunidadacumuladorTO.setOrderByField(OrderBy.orderDesc("id.acumid"));
				Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubtareaunidadacumulador(subtareaunidadacumuladorTO);
				System.out.println("subtareaunidadacumuladorTOs "+subtareaunidadacumuladorTOs.size());
				double valtotal=ConsultasUtil.obtenersaldodisponiblesubtarea(subtareaunidadacumuladorTOs, reformametalineaTO.getNivelactid(), null);
				reformametalineaTO.setNpvalorinicial(valtotal);
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformametalineaTO,reformametalineaTO.getJsonConfig()));
				jsonObject.put("cronograma", (JSONObject)JSONSerializer.toJSON(cronogramaTO,cronogramaTO.getJsonConfig()));
				jsonObject.put("cronogramalinea", (JSONArray)JSONSerializer.toJSON(cronogramaTO.getCronogramalineaTOs(),new CronogramalineaTO().getJsonConfigreforma()));
			}
			//tipo=rmm
			if(tipo.equals("rmm")){
				ReformametasubtareaTO reformametasubtareaTO= gson.fromJson(new StringReader(objeto), ReformametasubtareaTO.class);
				CronogramaTO cronogramaTO=UtilSession.planificacionServicio.transCronogramarforma(tipo, ejerciciofiscal, null, null ,reformametasubtareaTO,null);
				jsonObject.put("reformalinea", (JSONObject)JSONSerializer.toJSON(reformametasubtareaTO,reformametasubtareaTO.getJsonConfig()));
				jsonObject.put("cronograma", (JSONObject)JSONSerializer.toJSON(cronogramaTO,cronogramaTO.getJsonConfig()));
				jsonObject.put("cronogramalinea", (JSONArray)JSONSerializer.toJSON(cronogramaTO.getCronogramalineaTOs(),new CronogramalineaTO().getJsonConfigreforma()));
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
		if(mensajes.getMsg()!=null)
			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		log.println("json retornado: " + jsonObject.toString());
		respuesta.setJson(jsonObject);
		respuesta.setMensajes(mensajes);
		return respuesta;	

	}

	
}
