package ec.com.papp.web.administracion.controller;

import java.io.StringReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import ec.com.papp.administracion.to.CargoTO;
import ec.com.papp.administracion.to.CargoescalaTO;
import ec.com.papp.administracion.to.ClaseregistroTO;
import ec.com.papp.administracion.to.ClaseregistroclasemodificacionTO;
import ec.com.papp.administracion.to.ClasificacionTO;
import ec.com.papp.administracion.to.ConsecutivoTO;
import ec.com.papp.administracion.to.DivisiongeograficaTO;
import ec.com.papp.administracion.to.EjerciciofiscalTO;
import ec.com.papp.administracion.to.EscalarmuTO;
import ec.com.papp.administracion.to.EspecialidadTO;
import ec.com.papp.administracion.to.FuentefinanciamientoTO;
import ec.com.papp.administracion.to.FuerzaTO;
import ec.com.papp.administracion.to.FuerzaclasificacionTO;
import ec.com.papp.administracion.to.GradoTO;
import ec.com.papp.administracion.to.GradoescalaTO;
import ec.com.papp.administracion.to.GradofuerzaTO;
import ec.com.papp.administracion.to.GrupoTO;
import ec.com.papp.administracion.to.GrupomedidaTO;
import ec.com.papp.administracion.to.ItemTO;
import ec.com.papp.administracion.to.ObraTO;
import ec.com.papp.administracion.to.OrganismoTO;
import ec.com.papp.administracion.to.ParametroTO;
import ec.com.papp.administracion.to.ParametroindicadorTO;
import ec.com.papp.administracion.to.ProcedimientoTO;
import ec.com.papp.administracion.to.SocionegocioTO;
import ec.com.papp.administracion.to.SubitemTO;
import ec.com.papp.administracion.to.TipodocumentoTO;
import ec.com.papp.administracion.to.TipodocumentoclasedocumentoTO;
import ec.com.papp.administracion.to.TipoidentificacionTO;
import ec.com.papp.administracion.to.TipoidentificaciontipoTO;
import ec.com.papp.administracion.to.TipoproductoTO;
import ec.com.papp.administracion.to.TiporegimenTO;
import ec.com.papp.administracion.to.UnidadmedidaTO;
import ec.com.papp.administracion.to.id.ClaseregistroclasemodificacionID;
import ec.com.papp.administracion.to.id.TipodocumentoclasedocumentoID;
import ec.com.papp.administracion.to.id.TipoidentificaciontipoID;
import ec.com.papp.estructuraorganica.to.InstitucionTO;
import ec.com.papp.estructuraorganica.to.InstitucionentidadTO;
import ec.com.papp.planificacion.to.CertificacionTO;
import ec.com.papp.planificacion.to.CertificacionlineaTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.ContratoTO;
import ec.com.papp.planificacion.to.ItemunidadTO;
import ec.com.papp.planificacion.to.NivelorganicoTO;
import ec.com.papp.planificacion.to.OrdendevengoTO;
import ec.com.papp.planificacion.to.OrdengastoTO;
import ec.com.papp.planificacion.to.OrdengastolineaTO;
import ec.com.papp.planificacion.to.OrdenreversionTO;
import ec.com.papp.planificacion.to.OrdenreversionlineaTO;
import ec.com.papp.planificacion.to.OrganismoprestamoTO;
import ec.com.papp.planificacion.to.SubitemunidadTO;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.administracion.util.ConsultasUtil;
import ec.com.papp.web.comun.util.ConstantesSesion;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.Respuesta;
import ec.com.papp.web.comun.util.UtilSession;
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
 * @descripcion Clase para realizar administraciones centralizadas
 */

@RestController
@RequestMapping("/rest/administrar")
public class AdministracionController {
	private Log log = new Log(AdministracionController.class);
	
	@RequestMapping(value = "/{clase}", method = RequestMethod.POST)
	public Respuesta grabar(@PathVariable String clase, @RequestBody String objeto,HttpServletRequest request){
		log.println("entra al metodo grabar: " + clase + " - " + objeto);
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		Gson gson = new Gson();
		JSONObject jsonObject=new JSONObject();
		String id="";
		String accion="";
		try {
			//copiar de un ejercicio fiscal a otro
			if(clase.equals("copiardatos")){
				Map<String, String> copiar= gson.fromJson(new StringReader(objeto), Map.class);
				//accion = (ejerciciofiscalTO.getId()==null)?"I":"U";
				//pregunto si es fuente de financiamiento o items
				UtilSession.adminsitracionServicio.transCopiardatos(copiar);
				mensajes.setMsg("Exito al copiar los datos de un ejercicio fiscal a otro");
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			//Ejerciciofiscal
			else if(clase.equals("ejerciciofiscal")){
				EjerciciofiscalTO ejerciciofiscalTO = gson.fromJson(new StringReader(objeto), EjerciciofiscalTO.class);
				accion = (ejerciciofiscalTO.getId()==null)?"I":"U";
				
				//pregunto si ya existe el nombre en el nivel actual
				EjerciciofiscalTO ejerciciofiscalTO3=new EjerciciofiscalTO();
				ejerciciofiscalTO3.setAnio(ejerciciofiscalTO.getAnio());
				Collection<EjerciciofiscalTO> ejerciciofiscalTOs=UtilSession.adminsitracionServicio.transObtenerEjerciciofiscal(ejerciciofiscalTO3);
				log.println("ejercicios fiscales: " + ejerciciofiscalTOs.size());
				boolean grabar=true;
				if(ejerciciofiscalTOs.size()>0){
					for(EjerciciofiscalTO ejerciciofiscalTO2:ejerciciofiscalTOs) {
						//ejerciciofiscalTO2=(EjerciciofiscalTO)ejerciciofiscalTOs.iterator().next();
						if((ejerciciofiscalTO.getId()!=null && ejerciciofiscalTO.getId().longValue()!=0) && ejerciciofiscalTO2.getId().longValue()!=ejerciciofiscalTO.getId().longValue() && ejerciciofiscalTO.getAnio()!=null && ejerciciofiscalTO2.getAnio().equals(ejerciciofiscalTO.getAnio())) {
							grabar=false;
							break;
						}
						else if((ejerciciofiscalTO.getId()==null || (ejerciciofiscalTO.getId()!=null && ejerciciofiscalTO2.getId().longValue()!=ejerciciofiscalTO.getId().longValue())) && ejerciciofiscalTO.getAnio()!=null && ejerciciofiscalTO2.getAnio().equals(ejerciciofiscalTO.getAnio())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.anio.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarEjerciciofiscal(ejerciciofiscalTO);
					id=ejerciciofiscalTO.getNpid().toString();
					jsonObject.put("ejerciciofiscal", (JSONObject)JSONSerializer.toJSON(ejerciciofiscalTO,ejerciciofiscalTO.getJsonConfig()));
				}
			}
			//Divisiones geograficas
			else if(clase.equals("divisiongeografica")){
				DivisiongeograficaTO divisiongeograficaTO = gson.fromJson(new StringReader(objeto), DivisiongeograficaTO.class);
				accion = (divisiongeograficaTO.getId()==null)?"I":"U";
				//Si el codigo empieza por el codigo del padre
				if(divisiongeograficaTO.getCodigo().startsWith(divisiongeograficaTO.getNpcodigopadre())) {
					//pregunto si ya existe el nombre en el nivel actual
					DivisiongeograficaTO divisiongeograficaTO2=new DivisiongeograficaTO();
					divisiongeograficaTO2.setCodigo(divisiongeograficaTO.getCodigo());
					log.println("divisiongeografica codigo: " + divisiongeograficaTO2.getCodigo());
					Collection<DivisiongeograficaTO> divisiongeograficaTOs=UtilSession.adminsitracionServicio.transObtenerDivisiongeografica(divisiongeograficaTO2);
					log.println("divisiones... " + divisiongeograficaTOs.size());
					boolean grabar=true;
					if(divisiongeograficaTOs.size()>0){
						//divisiongeograficaTO2=(DivisiongeograficaTO)divisiongeograficaTOs.iterator().next();
						for(DivisiongeograficaTO divisiongeograficaTO3:divisiongeograficaTOs) {
							if((divisiongeograficaTO.getId()!=null && divisiongeograficaTO.getId().longValue()!=0) && divisiongeograficaTO3.getId().longValue()!=divisiongeograficaTO.getId().longValue() && divisiongeograficaTO3.getCodigo().equals(divisiongeograficaTO.getCodigo())) {
								log.println("entra por 1");
								grabar=false;
								break;
							}
							else if((divisiongeograficaTO.getId()==null || (divisiongeograficaTO.getId()!=null && divisiongeograficaTO3.getId().longValue()!=divisiongeograficaTO.getId().longValue())) && divisiongeograficaTO.getCodigo()!=null && divisiongeograficaTO3.getCodigo().equals(divisiongeograficaTO.getCodigo())) {
								log.println("entra por 2");
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
						UtilSession.adminsitracionServicio.transCrearModificarDivisiongeografica(divisiongeograficaTO);
						id=divisiongeograficaTO.getNpid().toString();
						//		jsonObject.put("divisiongeografica", (JSONObject)JSONSerializer.toJSON(divisiongeograficaTO,divisiongeograficaTO.getJsonConfig()));
					}
				}
				else {
					mensajes.setMsg(MensajesWeb.getString("advertencia.codigo.divisiong"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//ValidaEmpleado
			else if(clase.equals("validaempleado")){
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				SocionegocioTO socionegocioTO2=new SocionegocioTO();
				socionegocioTO2.setCodigo(socionegocioTO.getCodigo());
				Collection<SocionegocioTO> socionegocioTOs=UtilSession.adminsitracionServicio.transObtenerSocionegocio(socionegocioTO2);
				boolean grabar=true;
				if(socionegocioTOs.size()>0){
					for(SocionegocioTO socionegocioTO3:socionegocioTOs) {
						//socionegocioTO2=(SocionegocioTO)socionegocioTOs.iterator().next();
						if((socionegocioTO.getId()!=null && socionegocioTO.getId().longValue()!=0) && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue() && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((socionegocioTO.getId()==null || (socionegocioTO.getId()!=null && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue())) && socionegocioTO.getCodigo()!=null && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado") + ": Verifique en la seccion Socio negocio que se lo indique como empleado");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//Empleado
			else if(clase.equals("empleado")){
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				SocionegocioTO socionegocioTO2=new SocionegocioTO();
				socionegocioTO2.setCodigo(socionegocioTO.getCodigo());
				Collection<SocionegocioTO> socionegocioTOs=UtilSession.adminsitracionServicio.transObtenerSocionegocio(socionegocioTO2);
				boolean grabar=true;
				if(socionegocioTOs.size()>0){
					for(SocionegocioTO socionegocioTO3:socionegocioTOs) {
						//socionegocioTO2=(SocionegocioTO)socionegocioTOs.iterator().next();
						if((socionegocioTO.getId()!=null && socionegocioTO.getId().longValue()!=0) && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue() && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((socionegocioTO.getId()==null || (socionegocioTO.getId()!=null && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue())) && socionegocioTO.getCodigo()!=null && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado") + ": Verifique en la seccion Socio negocio que se lo indique como empleado");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarSocionegocio(socionegocioTO);
					//id=socionegocioTO.getNpid().toString();
					jsonObject.put("socionegocio", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfig()));
				}
			}
			
			//Empleadoproveedor
			else if(clase.equals("empleadoproveedor")){
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				SocionegocioTO socionegocioTO2=new SocionegocioTO();
				socionegocioTO2.setCodigo(socionegocioTO.getCodigo());
				Collection<SocionegocioTO> socionegocioTOs=UtilSession.adminsitracionServicio.transObtenerSocionegocio(socionegocioTO2);
				boolean grabar=true;
				if(socionegocioTOs.size()>0){
					for(SocionegocioTO socionegocioTO3:socionegocioTOs) {
						//socionegocioTO2=(SocionegocioTO)socionegocioTOs.iterator().next();
						if((socionegocioTO.getId()!=null && socionegocioTO.getId().longValue()!=0) && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue() && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((socionegocioTO.getId()==null || (socionegocioTO.getId()!=null && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue())) && socionegocioTO.getCodigo()!=null && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
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
					//Si es nuevo
					if(socionegocioTO.getId()==null) {
						socionegocioTO.setEstado("A");
						socionegocioTO.setEsempleado(0);
						socionegocioTO.setEmptipo(" ");
						socionegocioTO.setEmpsituacion(" ");
						socionegocioTO.setEmpfinanciamiento(" ");
						socionegocioTO.setRazonsocial(" ");
						socionegocioTO.setEsproveedor(1);
						socionegocioTO.setRepresentantelegal(" ");
						socionegocioTO.setNombrecomercial(" ");
						socionegocioTO.setPersonaid(0.0);
					}
					UtilSession.adminsitracionServicio.transCrearModificarSocionegocio(socionegocioTO);
					id=socionegocioTO.getNpid().toString();
					jsonObject.put("socionegocio", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfig()));
				}
			}

			//Proveedorruc
			else if(clase.equals("proveedorruc")){
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				SocionegocioTO socionegocioTO2=new SocionegocioTO();
				socionegocioTO2.setCodigo(socionegocioTO.getCodigo());
				Collection<SocionegocioTO> socionegocioTOs=UtilSession.adminsitracionServicio.transObtenerSocionegocio(socionegocioTO2);
				boolean grabar=true;
				if(socionegocioTOs.size()>0){
					for(SocionegocioTO socionegocioTO3:socionegocioTOs) {
						//socionegocioTO2=(SocionegocioTO)socionegocioTOs.iterator().next();
						if((socionegocioTO.getId()!=null && socionegocioTO.getId().longValue()!=0) && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue() && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((socionegocioTO.getId()==null || (socionegocioTO.getId()!=null && socionegocioTO3.getId().longValue()!=socionegocioTO.getId().longValue())) && socionegocioTO.getCodigo()!=null && socionegocioTO3.getCodigo().equals(socionegocioTO.getCodigo())) {
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
					//Si es nuevo
					if(socionegocioTO.getId()==null) {
						socionegocioTO.setEstado("A");
						socionegocioTO.setPrimernombre(" ");
						socionegocioTO.setPrimerapellido(" ");
						socionegocioTO.setSegundonombre(" ");
						socionegocioTO.setSegundoapellido(" ");
						socionegocioTO.setEsempleado(0);
						socionegocioTO.setEmptipo(" ");
						socionegocioTO.setEmpsituacion(" ");
						socionegocioTO.setEmpfinanciamiento(" ");
						socionegocioTO.setEsproveedor(1);
						socionegocioTO.setPersonaid(0.0);
					}
					UtilSession.adminsitracionServicio.transCrearModificarSocionegocio(socionegocioTO);
					id=socionegocioTO.getNpid().toString();
					jsonObject.put("socionegocio", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfig()));
				}
			}
			//Empleado
			//			else if(clase.equals("empleado")){
			//				EmpleadoTO empleadoTO = gson.fromJson(new StringReader(objeto), EmpleadoTO.class);
			//				accion = (empleadoTO.getId()==null)?"I":"U";
			//				//pregunto si ya existe el codigo en el nivel actual
			//				EmpleadoTO empleadoTO2=new EmpleadoTO();
			//				empleadoTO2.setCodigo(empleadoTO.getCodigo());
			//				Collection<EmpleadoTO> empleadoTOs=UtilSession.adminsitracionServicio.transObtenerEmpleado(empleadoTO);
			//				boolean grabar=true;
			//				if(empleadoTOs.size()>0){
			//					empleadoTO2=(EmpleadoTO)empleadoTOs.iterator().next();
			//					if(empleadoTO.getId()!=null && empleadoTO2.getId().longValue()!=empleadoTO.getId().longValue())
			//						grabar=false;
			//				}
			//				if(!grabar){
			//					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
			//					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
			//				}
			//				else{
			//					UtilSession.adminsitracionServicio.transCrearModificarEmpleado(empleadoTO);
			//					id=empleadoTO.getId().toString();
			//					jsonObject.put("item", (JSONObject)JSONSerializer.toJSON(empleadoTO,empleadoTO.getJsonConfig()));
			//				}
			//			}

			//Unidadmedida
			else if(clase.equals("unidadmedida")){
				UnidadmedidaTO unidadmedidaTO = gson.fromJson(new StringReader(objeto), UnidadmedidaTO.class);
				accion = (unidadmedidaTO.getId()==null)?"I":"U";
				//pregunto si ya existe el nombre en el nivel actual
				UnidadmedidaTO unidadmedidaTO3=new UnidadmedidaTO();
				unidadmedidaTO3.setCodigo(unidadmedidaTO.getCodigo());
				Collection<UnidadmedidaTO> unidadmedidaTOs=UtilSession.adminsitracionServicio.transObtenerUnidadmedida(unidadmedidaTO3);
				boolean grabar=true;
				if(unidadmedidaTOs.size()>0){
					for(UnidadmedidaTO unidadmedidaTO2:unidadmedidaTOs) {
						//unidadmedidaTO2=(UnidadmedidaTO)unidadmedidaTOs.iterator().next();
						if((unidadmedidaTO.getId()!=null && unidadmedidaTO.getId().longValue()!=0) && unidadmedidaTO2.getId().longValue()!=unidadmedidaTO.getId().longValue() && unidadmedidaTO2.getCodigo().equals(unidadmedidaTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((unidadmedidaTO.getId()==null || (unidadmedidaTO.getId()!=null && unidadmedidaTO2.getId().longValue()!=unidadmedidaTO.getId().longValue())) && unidadmedidaTO.getCodigo()!=null && unidadmedidaTO2.getCodigo().equals(unidadmedidaTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarUnidadmedida(unidadmedidaTO);
					id=unidadmedidaTO.getNpid().toString();
					jsonObject.put("unidadmedida", (JSONObject)JSONSerializer.toJSON(unidadmedidaTO,unidadmedidaTO.getJsonConfig()));
				}
			}

			//Parametro
			else if(clase.equals("parametro")){
				ParametroTO parametroTO = gson.fromJson(new StringReader(objeto), ParametroTO.class);
				accion = (parametroTO.getId()==null)?"I":"U";
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//pregunto si ya existe el nombre en el nivel actual
				ParametroTO parametroTO3=new ParametroTO();
				parametroTO3.setNombre(parametroTO.getNombre());
				Collection<ParametroTO> parametroTOs=UtilSession.adminsitracionServicio.transObtenerParametrol(parametroTO3);
				boolean grabar=true;
				if(parametroTOs.size()>0){
					for(ParametroTO parametroTO2:parametroTOs) {
						//parametroTO2=(ParametroTO)parametroTOs.iterator().next();
						if((parametroTO.getId()!=null && parametroTO.getId().longValue()!=0) && parametroTO2.getId().longValue()!=parametroTO.getId().longValue() && parametroTO2.getNombre().equals(parametroTO.getNombre())) {
							grabar=false;
							break;
						}
						else if((parametroTO.getId()==null || (parametroTO.getId()!=null && parametroTO2.getId().longValue()!=parametroTO.getId().longValue())) && parametroTO.getNombre()!=null && parametroTO2.getNombre().equals(parametroTO.getNombre())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarParametro(parametroTO);
					id=parametroTO.getNpid().toString();
					jsonObject.put("parametro", (JSONObject)JSONSerializer.toJSON(parametroTO,parametroTO.getJsonConfig()));
				}
			}

			//Consecutivo
			else if(clase.equals("consecutivo")){
				ConsecutivoTO consecutivoTO = gson.fromJson(new StringReader(objeto), ConsecutivoTO.class);
				accion = (consecutivoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				ConsecutivoTO consecutivoTO3=new ConsecutivoTO();
				consecutivoTO3.setNombre(consecutivoTO.getNombre());
				consecutivoTO3.setConsecutivoejerfiscalid(consecutivoTO.getConsecutivoejerfiscalid());
				if(consecutivoTO.getId()==null)
					consecutivoTO.setUltimousado(0);
				Collection<ConsecutivoTO> consecutivoTOs=UtilSession.adminsitracionServicio.transObtenerConsecutivo(consecutivoTO3);
				boolean grabar=true;
				if(consecutivoTOs.size()>0){
					for(ConsecutivoTO consecutivoTO2:consecutivoTOs) {
						//consecutivoTO2=(ConsecutivoTO)fuentefinanciamientoTOs.iterator().next();
						if((consecutivoTO.getId()!=null && consecutivoTO.getId().longValue()!=0) && consecutivoTO2.getId().longValue()!=consecutivoTO.getId().longValue() && consecutivoTO2.getNombre().equals(consecutivoTO.getNombre())) {
							grabar=false;
							break;
						}
						else if((consecutivoTO.getId()==null || (consecutivoTO.getId()!=null && consecutivoTO2.getId().longValue()!=consecutivoTO.getId().longValue())) && consecutivoTO.getNombre()!=null && consecutivoTO2.getNombre().equals(consecutivoTO.getNombre())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarConsecutivo(consecutivoTO);
					id=consecutivoTO.getNpid().toString();
					jsonObject.put("consecutivo", (JSONObject)JSONSerializer.toJSON(consecutivoTO,consecutivoTO.getJsonConfig()));
				}
			}

			//Tipoidentificacion
			else if(clase.equals("tipoidentificacion")){
				TipoidentificacionTO tipoidentificacionTO = gson.fromJson(new StringReader(objeto), TipoidentificacionTO.class);
				accion = (tipoidentificacionTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				TipoidentificacionTO tipoidentificacionTO3=new TipoidentificacionTO();
				tipoidentificacionTO3.setNombre(tipoidentificacionTO.getNombre());
				Collection<TipoidentificacionTO> tipoidentificacionTOs=UtilSession.adminsitracionServicio.transObtenerTipoidentificacion(tipoidentificacionTO3);
				boolean grabar=true;
				if(tipoidentificacionTOs.size()>0){
					for(TipoidentificacionTO tipoidentificacionTO2:tipoidentificacionTOs) {
						//tipoidentificacionTO2=(TipoidentificacionTO)fuentefinanciamientoTOs.iterator().next();
						if((tipoidentificacionTO.getId()!=null && tipoidentificacionTO.getId().longValue()!=0) && tipoidentificacionTO2.getId().longValue()!=tipoidentificacionTO.getId().longValue() && tipoidentificacionTO2.getNombre().equals(tipoidentificacionTO.getNombre())) {
							grabar=false;
							break;
						}
						else if((tipoidentificacionTO.getId()==null || (tipoidentificacionTO.getId()!=null && tipoidentificacionTO2.getId().longValue()!=tipoidentificacionTO.getId().longValue())) && tipoidentificacionTO.getNombre()!=null && tipoidentificacionTO2.getNombre().equals(tipoidentificacionTO.getNombre())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarTipoidentificacion(tipoidentificacionTO);
					id=tipoidentificacionTO.getNpid().toString();
					jsonObject.put("tipoidentificacion", (JSONObject)JSONSerializer.toJSON(tipoidentificacionTO,tipoidentificacionTO.getJsonConfig()));
				}
			}

			//Fuentefinanciamiento
			else if(clase.equals("fuentefinanciamiento")){
				FuentefinanciamientoTO fuentefinanciamientoTO = gson.fromJson(new StringReader(objeto), FuentefinanciamientoTO.class);
				accion = (fuentefinanciamientoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				FuentefinanciamientoTO fuentefinanciamientoTO3=new FuentefinanciamientoTO();
				fuentefinanciamientoTO3.setCodigo(fuentefinanciamientoTO.getCodigo());
				fuentefinanciamientoTO3.setFuentefinanejerciciofiscalid(fuentefinanciamientoTO.getFuentefinanejerciciofiscalid());
				Collection<FuentefinanciamientoTO> fuentefinanciamientoTOs=UtilSession.adminsitracionServicio.transObtenerFuentefinanciamiento(fuentefinanciamientoTO3);
				boolean grabar=true;
				if(fuentefinanciamientoTOs.size()>0){
					for(FuentefinanciamientoTO fuentefinanciamientoTO2:fuentefinanciamientoTOs) {
						//fuentefinanciamientoTO2=(FuentefinanciamientoTO)fuentefinanciamientoTOs.iterator().next();
						if((fuentefinanciamientoTO.getId()!=null && fuentefinanciamientoTO.getId().longValue()!=0) && fuentefinanciamientoTO2.getId().longValue()!=fuentefinanciamientoTO.getId().longValue() && fuentefinanciamientoTO2.getCodigo().equals(fuentefinanciamientoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((fuentefinanciamientoTO.getId()==null || (fuentefinanciamientoTO.getId()!=null && fuentefinanciamientoTO2.getId().longValue()!=fuentefinanciamientoTO.getId().longValue())) && fuentefinanciamientoTO.getCodigo()!=null && fuentefinanciamientoTO2.getCodigo().equals(fuentefinanciamientoTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarFuentefinanciamiento(fuentefinanciamientoTO);
					id=fuentefinanciamientoTO.getNpid().toString();
					jsonObject.put("fuentefinanciamiento", (JSONObject)JSONSerializer.toJSON(fuentefinanciamientoTO,fuentefinanciamientoTO.getJsonConfig()));
				}
			}

			//Organismo
			else if(clase.equals("organismo")){
				OrganismoTO organismoTO = gson.fromJson(new StringReader(objeto), OrganismoTO.class);
				accion = (organismoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				OrganismoTO organismoTO3=new OrganismoTO();
				organismoTO3.setCodigo(organismoTO.getCodigo());
				organismoTO3.setOrganismoejerciciofiscalid(organismoTO.getOrganismoejerciciofiscalid());
				Collection<OrganismoTO> organismoTOs=UtilSession.adminsitracionServicio.transObtenerOrganismo(organismoTO3);
				log.println("organismos: " + organismoTOs.size());
				boolean grabar=true;
				if(organismoTOs.size()>0){
					for(OrganismoTO organismoTO2:organismoTOs) {
						//organismoTO2=(OrganismoTO)organismoTOs.iterator().next();
						if((organismoTO.getId()!=null && organismoTO.getId().longValue()!=0) && organismoTO2.getId().longValue()!=organismoTO.getId().longValue() && organismoTO2.getCodigo().equals(organismoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((organismoTO.getId()==null || (organismoTO.getId()!=null && organismoTO2.getId().longValue()!=organismoTO.getId().longValue())) && organismoTO.getCodigo()!=null && organismoTO2.getCodigo().equals(organismoTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarOrganismo(organismoTO);
					id=organismoTO.getNpid().toString();
					jsonObject.put("organismo", (JSONObject)JSONSerializer.toJSON(organismoTO,organismoTO.getJsonConfig()));
				}
			}

			//Obra
			else if(clase.equals("obra")){
				ObraTO obraTO = gson.fromJson(new StringReader(objeto), ObraTO.class);
				log.println("obra: " + obraTO.getObraejerciciofiscalid());
				accion = (obraTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				ObraTO obraTO3=new ObraTO();
				obraTO3.setCodigo(obraTO.getCodigo());
				obraTO3.setObraejerciciofiscalid(obraTO.getObraejerciciofiscalid());
				Collection<ObraTO> obraTOs=UtilSession.adminsitracionServicio.transObtenerObra(obraTO3);
				boolean grabar=true;
				if(obraTOs.size()>0){
					for(ObraTO obraTO2:obraTOs) {
						//obraTO2=(ObraTO)obraTOs.iterator().next();
						if((obraTO.getId()!=null && obraTO.getId().longValue()!=0) && obraTO2.getId().longValue()!=obraTO.getId().longValue() && obraTO2.getCodigo().equals(obraTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((obraTO.getId()==null || (obraTO.getId()!=null && obraTO2.getId().longValue()!=obraTO.getId().longValue())) && obraTO.getCodigo()!=null && obraTO2.getCodigo().equals(obraTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarObra(obraTO);
					id=obraTO.getNpid().toString();
					jsonObject.put("obra", (JSONObject)JSONSerializer.toJSON(obraTO,obraTO.getJsonConfig()));
				}
			}

			//Item
			else if(clase.equals("item")){
				ItemTO itemTO = gson.fromJson(new StringReader(objeto), ItemTO.class);
				log.println("id item: "+ itemTO.getId());
				log.println("padre: " + itemTO.getItempadreid());
				if(itemTO.getItempadreid()!=null && itemTO.getItempadreid().longValue()==0)
					itemTO.setItempadreid(null);
				accion = (itemTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				ItemTO itemTO3=new ItemTO();
				itemTO3.setCodigo(itemTO.getCodigo());
				itemTO3.setItemejerciciofiscalid(itemTO.getItemejerciciofiscalid());
				Collection<ItemTO> itemTOs=UtilSession.adminsitracionServicio.transObtenerItem(itemTO3);
				boolean grabar=true;
				if(itemTOs.size()>0){
					for(ItemTO itemTO2:itemTOs) {
						//itemTO2=(ItemTO)itemTOs.iterator().next();
						if((itemTO.getId()!=null && itemTO.getId().longValue()!=0) && itemTO2.getId().longValue()!=itemTO.getId().longValue() && itemTO2.getCodigo().equals(itemTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((itemTO.getId()==null || (itemTO.getId()!=null && itemTO2.getId().longValue()!=itemTO.getId().longValue())) && itemTO.getCodigo()!=null && itemTO2.getCodigo().equals(itemTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				//Si no esta repetido verifico si no esta asignado a la planificacion ue
				if(grabar && itemTO.getEstado().equals("I")){
					ItemunidadTO itemunidadTO=new ItemunidadTO();
					itemunidadTO.setItemunidadejerciciofiscalid(itemTO.getItemejerciciofiscalid());
					itemunidadTO.setEstado("A");
					itemunidadTO.setItemunidaditemid(itemTO.getId());
					Collection<ItemunidadTO> itemunidadTOs=UtilSession.planificacionServicio.transObtenerItemunidad(itemunidadTO);
					//System.out.println("items unidad: " + itemunidadTOs.size());
					if(itemunidadTOs.size()>0){
						grabar=false;
						mensajes.setMsg("No se puede inactivar el Item puesto que esta siendo utilizado en la planificacion");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
				if(mensajes.getMsg()==null && grabar){
					UtilSession.adminsitracionServicio.transCrearModificarItem(itemTO);
					id=itemTO.getNpid().toString();
					log.println("id del item: " + id);
					jsonObject.put("item", (JSONObject)JSONSerializer.toJSON(itemTO,itemTO.getJsonConfig()));
				}
				else if(mensajes.getMsg()==null){
					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			//Subitem
			else if(clase.equals("subitem")){
				SubitemTO subitemTO = gson.fromJson(new StringReader(objeto), SubitemTO.class);
				accion = (subitemTO.getId()==null)?"I":"U";
				if(subitemTO.getSubitemunidadmedidaid()!=null && subitemTO.getSubitemunidadmedidaid().longValue()==0)
					subitemTO.setSubitemunidadmedidaid(null);
				//Si no esta repetido verifico si no esta asignado a la planificacion ue
				if(subitemTO.getEstado().equals("I")){
					SubitemunidadTO subitemunidadTO=new SubitemunidadTO();
					subitemunidadTO.setSubitemunidadejerfiscalid(subitemTO.getNpejerciciofiscal());
					subitemunidadTO.setSubitemunidadsubitemid(subitemTO.getId());
					subitemunidadTO.setEstado("A");
					Collection<SubitemunidadTO> subitemunidadTOs=UtilSession.planificacionServicio.transObtenerSubitemunidad(subitemunidadTO);
					//System.out.println("subitems unidad: " + subitemunidadTOs.size());
					if(subitemunidadTOs.size()>0){
						mensajes.setMsg("No se puede inactivar el Subitem puesto que esta siendo utilizado en la planificacion");
						mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
					}
				}
				if(mensajes.getMsg()==null){
					UtilSession.adminsitracionServicio.transCrearModificarSubitem(subitemTO);
				}
				//id=subitemTO.getNpid().toString();
				jsonObject.put("subitem", (JSONObject)JSONSerializer.toJSON(subitemTO,subitemTO.getJsonConfig()));
			}

			//Grupomedida
			else if(clase.equals("grupomedida")){
				GrupomedidaTO grupomedidaTO = gson.fromJson(new StringReader(objeto), GrupomedidaTO.class);
				accion = (grupomedidaTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				GrupomedidaTO grupomedidaTO3=new GrupomedidaTO();
				grupomedidaTO3.setCodigo(grupomedidaTO.getCodigo());
				Collection<GrupomedidaTO> grupomedidaTOs=UtilSession.adminsitracionServicio.transObtenerGrupomedida(grupomedidaTO3);
				log.println("grupos: " + grupomedidaTOs.size());
				boolean grabar=true;
				if(grupomedidaTOs.size()>0){
					for(GrupomedidaTO grupomedidaTO2:grupomedidaTOs) {
						//grupomedidaTO2=(GrupomedidaTO)grupomedidaTOs.iterator().next();
						if((grupomedidaTO.getId()!=null && grupomedidaTO.getId().longValue()!=0) && grupomedidaTO2.getId().longValue()!=grupomedidaTO.getId().longValue()  && grupomedidaTO.getCodigo()!=null && grupomedidaTO2.getCodigo().equals(grupomedidaTO.getCodigo())) {
							log.println("entro por 1 " + grupomedidaTO.getId() + " - "+grupomedidaTO2.getId());
							grabar=false;
							break;
						}
						else if((grupomedidaTO.getId()==null || (grupomedidaTO.getId()!=null && grupomedidaTO2.getId().longValue()!=grupomedidaTO.getId().longValue())) && grupomedidaTO.getCodigo()!=null && grupomedidaTO2.getCodigo().equals(grupomedidaTO.getCodigo())) {
							log.println("entro por 2");
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
					UtilSession.adminsitracionServicio.transCrearModificarGrupomedida(grupomedidaTO);
					id=grupomedidaTO.getNpid().toString();
					jsonObject.put("grupomedida", (JSONObject)JSONSerializer.toJSON(grupomedidaTO,grupomedidaTO.getJsonConfig()));
				}
			}

			//Claseregistro
			else if(clase.equals("claseregistro")){
				ClaseregistroTO claseregistroTO = gson.fromJson(new StringReader(objeto), ClaseregistroTO.class);
				accion = (claseregistroTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				ClaseregistroTO claseregistroTO3=new ClaseregistroTO();
				claseregistroTO3.setCodigo(claseregistroTO.getCodigo());
				claseregistroTO3.setClaseregistroejerfiscalid(claseregistroTO.getClaseregistroejerfiscalid());
				Collection<ClaseregistroTO> claseregistroTOs=UtilSession.adminsitracionServicio.transObtenerClaseregistro(claseregistroTO3);
				log.println("clases encontradas: " + claseregistroTOs.size());
				boolean grabar=true;
				if(claseregistroTOs.size()>0){
					for(ClaseregistroTO claseregistroTO2:claseregistroTOs) {
						//claseregistroTO2=(ClaseregistroTO)claseregistroTOs.iterator().next();
						log.println("id.... " + claseregistroTO.getId() + " - " + claseregistroTO2.getId());
						if((claseregistroTO.getId()!=null && claseregistroTO.getId().longValue()!=0) && claseregistroTO2.getId().longValue()!=claseregistroTO.getId().longValue() && claseregistroTO2.getCodigo().equals(claseregistroTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((claseregistroTO.getId()==null || (claseregistroTO.getId()!=null && claseregistroTO2.getId().longValue()!=claseregistroTO.getId().longValue())) && claseregistroTO.getCodigo()!=null && claseregistroTO2.getCodigo().equals(claseregistroTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarClaseregistro(claseregistroTO);
					id=claseregistroTO.getNpid().toString();
					jsonObject.put("claseregistro", (JSONObject)JSONSerializer.toJSON(claseregistroTO,claseregistroTO.getJsonConfig()));
				}
			}

			//Clasemodificacion
			else if(clase.equals("clasemodificacion")){
				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO = gson.fromJson(new StringReader(objeto), ClaseregistroclasemodificacionTO.class);
				if(claseregistroclasemodificacionTO.getId()==null)
					claseregistroclasemodificacionTO.setId(new ClaseregistroclasemodificacionID());
				log.println("ejercicio fiscal::# " + claseregistroclasemodificacionTO.getClaseregistrocmejerfiscalid());
				accion = (claseregistroclasemodificacionTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
//				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO3=new ClaseregistroclasemodificacionTO();
//				claseregistroclasemodificacionTO3.setCodigo(claseregistroclasemodificacionTO.getCodigo());
//				claseregistroclasemodificacionTO3.setClaseregistrocmejerfiscalid(claseregistroclasemodificacionTO.getClaseregistrocmejerfiscalid());
//				Collection<ClaseregistroclasemodificacionTO> claseregistroclasemodificacionTOs=UtilSession.adminsitracionServicio.transObtenerClaseregistroclasemodificacion(claseregistroclasemodificacionTO3);
//				log.println("clases encontradas: " + claseregistroclasemodificacionTOs.size());
//				boolean grabar=true;
//				if(claseregistroclasemodificacionTOs.size()>0){
//					for(ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO2:claseregistroclasemodificacionTOs) {
//						//claseregistroclasemodificacionTO2=(ClaseregistroclasemodificacionTO)claseregistroclasemodificacionTOs.iterator().next();
//						log.println("id.... " + claseregistroclasemodificacionTO.getId().getRegistroid() + " - " + claseregistroclasemodificacionTO2.getId().getRegistroid());
//						log.println("id1.... " + claseregistroclasemodificacionTO.getId().getRegistrocmid()+ " - " + claseregistroclasemodificacionTO2.getId().getRegistrocmid());
//						if((claseregistroclasemodificacionTO.getId()!=null && claseregistroclasemodificacionTO.getId().getRegistrocmid()!=null && claseregistroclasemodificacionTO.getId().getRegistroid()!=null) && claseregistroclasemodificacionTO2.getId().getRegistrocmid().longValue()!=claseregistroclasemodificacionTO.getId().getRegistrocmid().longValue() && claseregistroclasemodificacionTO2.getId().getRegistroid().longValue()!=claseregistroclasemodificacionTO.getId().getRegistroid().longValue() && claseregistroclasemodificacionTO.getCodigo()!=null && claseregistroclasemodificacionTO2.getCodigo().equals(claseregistroclasemodificacionTO.getCodigo())) {
//							grabar=false;
//							break;
//						}
//						else if((claseregistroclasemodificacionTO.getId()==null || (claseregistroclasemodificacionTO.getId()!=null && claseregistroclasemodificacionTO2.getId().getRegistrocmid().longValue()!=claseregistroclasemodificacionTO.getId().getRegistrocmid().longValue() && claseregistroclasemodificacionTO2.getId().getRegistroid().longValue()!=claseregistroclasemodificacionTO.getId().getRegistroid().longValue() && claseregistroclasemodificacionTO2.getId().getRegistroid().longValue()!=claseregistroclasemodificacionTO.getId().getRegistroid().longValue())) && claseregistroclasemodificacionTO.getCodigo()!=null && claseregistroclasemodificacionTO2.getCodigo().equals(claseregistroclasemodificacionTO.getCodigo())) {
//							grabar=false;
//							break;
//						}
//					}
//				}
//				if(!grabar){
//					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
//					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//				}
//				else{
					UtilSession.adminsitracionServicio.transCrearModificarClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
					id=claseregistroclasemodificacionTO.getId().getRegistrocmid().toString();
					jsonObject.put("clasemodificacion", (JSONObject)JSONSerializer.toJSON(claseregistroclasemodificacionTO,claseregistroclasemodificacionTO.getJsonConfig()));
//				}
			}

			//Tipodocumento
			else if(clase.equals("tipodocumento")){
				TipodocumentoTO tipodocumentoTO = gson.fromJson(new StringReader(objeto), TipodocumentoTO.class);
				accion = (tipodocumentoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				TipodocumentoTO tipodocumentoTO3=new TipodocumentoTO();
				tipodocumentoTO3.setCodigo(tipodocumentoTO.getCodigo());
				tipodocumentoTO3.setTipodocumentoejerfiscalid(tipodocumentoTO.getTipodocumentoejerfiscalid());
				Collection<TipodocumentoTO> tipodocumentoTOs=UtilSession.adminsitracionServicio.transObtenerTipodocumento(tipodocumentoTO3);
				boolean grabar=true;
				if(tipodocumentoTOs.size()>0){
					for(TipodocumentoTO tipodocumentoTO2:tipodocumentoTOs) {
						//tipodocumentoTO2=(TipodocumentoTO)tipodocumentoTOs.iterator().next();
						if((tipodocumentoTO.getId()!=null && tipodocumentoTO.getId().longValue()!=0) && tipodocumentoTO2.getId().longValue()!=tipodocumentoTO.getId().longValue() && tipodocumentoTO2.getCodigo().equals(tipodocumentoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((tipodocumentoTO.getId()==null || (tipodocumentoTO.getId()!=null && tipodocumentoTO2.getId().longValue()!=tipodocumentoTO.getId().longValue())) && tipodocumentoTO.getCodigo()!=null && tipodocumentoTO2.getCodigo().equals(tipodocumentoTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarTipodocumento(tipodocumentoTO);
					id=tipodocumentoTO.getNpid().toString();
					jsonObject.put("tipodocumento", (JSONObject)JSONSerializer.toJSON(tipodocumentoTO,tipodocumentoTO.getJsonConfig()));
				}
			}

			//Socionegocio
			//En este metodo solo se modifica si es empleado o proveedor
			else if(clase.equals("socionegocio")){
				SocionegocioTO socionegocioTO = gson.fromJson(new StringReader(objeto), SocionegocioTO.class);
				accion = (socionegocioTO.getId()==null)?"I":"U";
				//Obtengo el registro de socio negocio
				SocionegocioTO socionegocioTO2=UtilSession.adminsitracionServicio.transObtenerSocionegocioTO(socionegocioTO.getId());
				//Le asigno lo que este seleccionado en esempleado y esproveedor
				socionegocioTO2.setEsempleado(socionegocioTO.getEsempleado());
				socionegocioTO2.setEsproveedor(socionegocioTO.getEsproveedor());
				UtilSession.adminsitracionServicio.transCrearModificarSocionegocio(socionegocioTO2);
			}

			//Parametroindicador
			else if(clase.equals("parametroindicador")){
				ParametroindicadorTO parametroindicadorTO  = gson.fromJson(new StringReader(objeto), ParametroindicadorTO.class);
				accion = (parametroindicadorTO.getId()==null)?"I":"U";
				UtilSession.adminsitracionServicio.transCrearModificarParametroindicador(new ParametroindicadorTO());
				id=parametroindicadorTO.getNpid().toString();
				jsonObject.put("parametroindicador", (JSONObject)JSONSerializer.toJSON(parametroindicadorTO,parametroindicadorTO.getJsonConfig()));
			}

			//Tipoproducto
			else if(clase.equals("tipoproducto")){
				TipoproductoTO tipoproductoTO = gson.fromJson(new StringReader(objeto), TipoproductoTO.class);
				accion = (tipoproductoTO.getId()==null)?"I":"U";
				if(tipoproductoTO.getActivo()==null)
					tipoproductoTO.setActivo(0);
				//pregunto si ya existe el codigo en el nivel actual
				TipoproductoTO tipoproductoTO3=new TipoproductoTO();
				tipoproductoTO3.setNombre(tipoproductoTO.getNombre());
				Collection<TipoproductoTO> tipoproductoTOs=UtilSession.adminsitracionServicio.transObtenerTipoproducto(tipoproductoTO3);
				boolean grabar=true;
				if(tipoproductoTOs.size()>0){
					for(TipoproductoTO tipoproductoTO2:tipoproductoTOs) {
						//tipoproductoTO2=(TipoproductoTO)tipoproductoTOs.iterator().next();
						if((tipoproductoTO.getId()!=null && tipoproductoTO.getId().longValue()!=0) && tipoproductoTO2.getId().longValue()!=tipoproductoTO.getId().longValue() && tipoproductoTO2.getNombre().equals(tipoproductoTO.getNombre())) {
							grabar=false;
							break;
						}
						else if((tipoproductoTO.getId()==null || (tipoproductoTO.getId()!=null && tipoproductoTO2.getId().longValue()!=tipoproductoTO.getId().longValue())) && tipoproductoTO.getNombre()!=null && tipoproductoTO2.getNombre().equals(tipoproductoTO.getNombre())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarTipoproducto(tipoproductoTO);
					id=tipoproductoTO.getNpid().toString();
					jsonObject.put("tipoproducto", (JSONObject)JSONSerializer.toJSON(tipoproductoTO,tipoproductoTO.getJsonConfig()));
				}
			}

			//Tiporegimen
			else if(clase.equals("tiporegimen")){
				TiporegimenTO tiporegimenTO = gson.fromJson(new StringReader(objeto), TiporegimenTO.class);
				accion = (tiporegimenTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				TiporegimenTO tiporegimenT3=new TiporegimenTO();
				tiporegimenT3.setNombre(tiporegimenTO.getNombre());
				Collection<TiporegimenTO> tiporegimenTOs=UtilSession.adminsitracionServicio.transObtenerTiporegimen(tiporegimenT3);
				boolean grabar=true;
				if(tiporegimenTOs.size()>0){
					//tiporegimenTO2=(TiporegimenTO)tiporegimenTOs.iterator().next();
					for(TiporegimenTO tiporegimenTO2:tiporegimenTOs) {
						if((tiporegimenTO.getId()!=null && tiporegimenTO.getId().longValue()!=0) && tiporegimenTO2.getId().longValue()!=tiporegimenTO.getId().longValue() && tiporegimenTO2.getNombre().equals(tiporegimenTO.getNombre())) {
							grabar=false;
							break;
						}
						else if((tiporegimenTO.getId()==null || (tiporegimenTO.getId()!=null && tiporegimenTO2.getId().longValue()!=tiporegimenTO.getId().longValue())) && tiporegimenTO.getNombre()!=null && tiporegimenTO2.getNombre().equals(tiporegimenTO.getNombre())) {
							grabar=false;
							break;
						}
					}
				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarTiporegimen(tiporegimenTO);
					id=tiporegimenTO.getNpid().toString();
					jsonObject.put("tiporegimen", (JSONObject)JSONSerializer.toJSON(tiporegimenTO,tiporegimenTO.getJsonConfig()));
				}
			}

			//Procedimiento
			else if(clase.equals("procedimiento")){
				ProcedimientoTO procedimientoTO = gson.fromJson(new StringReader(objeto), ProcedimientoTO.class);
				accion = (procedimientoTO.getId()==null)?"I":"U";
				if(procedimientoTO.getActivo()==null)
					procedimientoTO.setActivo(0);
				//pregunto si ya existe el codigo en el nivel actual
				ProcedimientoTO procedimientoTO3=new ProcedimientoTO();
				procedimientoTO3.setNombre(procedimientoTO.getNombre());
				Collection<ProcedimientoTO> procedimientoTOs=UtilSession.adminsitracionServicio.transObtenerProcedimiento(procedimientoTO3);
				boolean grabar=true;
				if(procedimientoTOs.size()>0){
					for(ProcedimientoTO procedimientoTO2:procedimientoTOs) {
							//procedimientoTO2=(ProcedimientoTO)procedimientoTOs.iterator().next();
							if((procedimientoTO.getId()!=null && procedimientoTO.getId().longValue()!=0) && procedimientoTO2.getId().longValue()!=procedimientoTO.getId().longValue() && procedimientoTO2.getNombre().equals(procedimientoTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((procedimientoTO.getId()==null || (procedimientoTO.getId()!=null && procedimientoTO2.getId().longValue()!=procedimientoTO.getId().longValue())) && procedimientoTO.getNombre()!=null && procedimientoTO2.getNombre().equals(procedimientoTO.getNombre())) {
								grabar=false;
								break;
							}
					}

				}
				if(!grabar){
					mensajes.setMsg(MensajesWeb.getString("error.nombre.duplicado"));
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarProcedimiento(procedimientoTO);
					id=procedimientoTO.getNpid().toString();
					jsonObject.put("tipoidentificacion", (JSONObject)JSONSerializer.toJSON(procedimientoTO,procedimientoTO.getJsonConfig()));
				}
			}

			//Grupo
			else if(clase.equals("grupo")){
				GrupoTO grupoTO = gson.fromJson(new StringReader(objeto), GrupoTO.class);
				accion = (grupoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				GrupoTO grupoTO4=new GrupoTO();
				grupoTO4.setCodigo(grupoTO.getCodigo());
				Collection<GrupoTO> grupoTOs=UtilSession.adminsitracionServicio.transObtenerGrupo(grupoTO4);
				boolean grabar=true;
				if(grupoTOs.size()>0){
					for(GrupoTO grupoTO2:grupoTOs) {
						//grupoTO2=(GrupoTO)grupoTOs.iterator().next();
						if((grupoTO.getId()!=null && grupoTO.getId().longValue()!=0) && grupoTO2.getId().longValue()!=grupoTO.getId().longValue() && grupoTO2.getCodigo().equals(grupoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((grupoTO.getId()==null || (grupoTO.getId()!=null && grupoTO2.getId().longValue()!=grupoTO.getId().longValue())) && grupoTO.getCodigo()!=null && grupoTO2.getCodigo().equals(grupoTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				if(grabar) {
					GrupoTO grupoTO5=new GrupoTO();
					grupoTO5.setNombre(grupoTO.getNombre());
					Collection<GrupoTO> grupoTO1s=UtilSession.adminsitracionServicio.transObtenerGrupo(grupoTO5);
					if(grupoTO1s.size()>0){
						for(GrupoTO grupoTO3:grupoTO1s) {
							//grupoTO3=(GrupoTO)grupoTO1s.iterator().next();
							if((grupoTO.getId()!=null && grupoTO.getId().longValue()!=0) && grupoTO3.getId().longValue()!=grupoTO.getId().longValue() && grupoTO3.getNombre().equals(grupoTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((grupoTO.getId()==null || (grupoTO.getId()!=null && grupoTO3.getId().longValue()!=grupoTO.getId().longValue())) && grupoTO.getNombre()!=null && grupoTO3.getNombre().equals(grupoTO.getNombre())) {
								grabar=false;
								break;
							}
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("El código o el nombre se encuentran repetidos");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarGrupo(grupoTO);
					id=grupoTO.getNpid().toString();
					jsonObject.put("grupo", (JSONObject)JSONSerializer.toJSON(grupoTO,grupoTO.getJsonConfig()));
				}
			}

			//Grado
			else if(clase.equals("grado")){
				GradoTO gradoTO = gson.fromJson(new StringReader(objeto), GradoTO.class);
				accion = (gradoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				GradoTO gradoTO4=new GradoTO();
				gradoTO4.setCodigo(gradoTO.getCodigo());
				Collection<GradoTO> gradoTOs=UtilSession.adminsitracionServicio.transObtenerGrado(gradoTO4);
				boolean grabar=true;
				if(gradoTOs.size()>0){
					for(GradoTO gradoTO2:gradoTOs) {
						//gradoTO2=(GradoTO)gradoTOs.iterator().next();
						if((gradoTO.getId()!=null && gradoTO.getId().longValue()!=0) && gradoTO2.getId().longValue()!=gradoTO.getId().longValue() && gradoTO2.getCodigo().equals(gradoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((gradoTO.getId()==null || (gradoTO.getId()!=null && gradoTO2.getId().longValue()!=gradoTO.getId().longValue())) && gradoTO.getCodigo()!=null && gradoTO2.getCodigo().equals(gradoTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				if(grabar) {
					GradoTO gradoTO5=new GradoTO();
					gradoTO5.setNombre(gradoTO.getNombre());
					Collection<GradoTO> gradoTOs2=UtilSession.adminsitracionServicio.transObtenerGrado(gradoTO5);
					log.println("gradoTOs2: "+ gradoTOs2.size());
					if(gradoTOs2.size()>0){
						for(GradoTO gradoTO3:gradoTOs2) {
							//gradoTO3=(GradoTO)gradoTOs2.iterator().next();
							if((gradoTO.getId()!=null && gradoTO.getId().longValue()!=0) && gradoTO3.getId().longValue()!=gradoTO.getId().longValue() && gradoTO3.getNombre().equals(gradoTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((gradoTO.getId()==null || (gradoTO.getId()!=null && gradoTO3.getId().longValue()!=gradoTO.getId().longValue())) && gradoTO.getNombre()!=null && gradoTO3.getNombre().equals(gradoTO.getNombre())) {
								grabar=false;
								break;
							}
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("El código o el nombre se encuentran repetidos");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarGrado(gradoTO);
					id=gradoTO.getNpid().toString();
					jsonObject.put("grado", (JSONObject)JSONSerializer.toJSON(gradoTO,gradoTO.getJsonConfig()));
				}
			}

			//Clasificacion
			else if(clase.equals("clasificacion")){
				ClasificacionTO clasificacionTO = gson.fromJson(new StringReader(objeto), ClasificacionTO.class);
				accion = (clasificacionTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				ClasificacionTO clasificacionTO3=new ClasificacionTO();
				clasificacionTO3.setCodigo(clasificacionTO.getCodigo());
				Collection<ClasificacionTO> clasificacionTOs=UtilSession.adminsitracionServicio.transObtenerClasificacion(clasificacionTO3);
				boolean grabar=true;
				if(clasificacionTOs.size()>0){
					for(ClasificacionTO clasificacionTO2:clasificacionTOs) {
						//clasificacionTO2=(ClasificacionTO)clasificacionTOs.iterator().next();
						if((clasificacionTO.getId()!=null && clasificacionTO.getId().longValue()!=0) && clasificacionTO2.getId().longValue()!=clasificacionTO.getId().longValue() && clasificacionTO2.getCodigo().equals(clasificacionTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((clasificacionTO.getId()==null || (clasificacionTO.getId()!=null && clasificacionTO2.getId().longValue()!=clasificacionTO.getId().longValue())) && clasificacionTO.getCodigo()!=null && clasificacionTO2.getCodigo().equals(clasificacionTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarClasificacion(clasificacionTO);
					id=clasificacionTO.getNpid().toString();
					jsonObject.put("clasificacion", (JSONObject)JSONSerializer.toJSON(clasificacionTO,clasificacionTO.getJsonConfig()));
				}
			}

			//Fuerza
			else if(clase.equals("fuerza")){
				FuerzaTO fuerzaTO = gson.fromJson(new StringReader(objeto), FuerzaTO.class);
				accion = (fuerzaTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				FuerzaTO fuerzaTO4=new FuerzaTO();
				fuerzaTO4.setCodigo(fuerzaTO.getCodigo());
				Collection<FuerzaTO> fuerzaTOs=UtilSession.adminsitracionServicio.transObtenerFuerza(fuerzaTO4);
				boolean grabar=true;
				if(fuerzaTOs.size()>0){
					for(FuerzaTO fuerzaTO2:fuerzaTOs) {
						//fuerzaTO2=(FuerzaTO)fuerzaTOs.iterator().next();
						if((fuerzaTO.getId()!=null && fuerzaTO.getId().longValue()!=0) && fuerzaTO2.getId().longValue()!=fuerzaTO.getId().longValue() && fuerzaTO2.getCodigo().equals(fuerzaTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((fuerzaTO.getId()==null || (fuerzaTO.getId()!=null && fuerzaTO2.getId().longValue()!=fuerzaTO.getId().longValue())) && fuerzaTO.getCodigo()!=null && fuerzaTO2.getCodigo().equals(fuerzaTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				if(grabar) {
					FuerzaTO fuerzaTO5=new FuerzaTO();
					fuerzaTO5.setNombre(fuerzaTO.getNombre());
					Collection<FuerzaTO> fuerzaTOs2=UtilSession.adminsitracionServicio.transObtenerFuerza(fuerzaTO5);
					log.println("fuerzaTOs2: "+ fuerzaTOs2.size());
					if(fuerzaTOs2.size()>0){
						for(FuerzaTO fuerzaTO3:fuerzaTOs2) {
							//fuerzaTO3=(FuerzaTO)fuerzaTOs2.iterator().next();
							if((fuerzaTO.getId()!=null && fuerzaTO.getId().longValue()!=0) && fuerzaTO3.getId().longValue()!=fuerzaTO.getId().longValue() && fuerzaTO3.getNombre().equals(fuerzaTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((fuerzaTO.getId()==null || (fuerzaTO.getId()!=null && fuerzaTO3.getId().longValue()!=fuerzaTO.getId().longValue())) && fuerzaTO.getNombre()!=null && fuerzaTO3.getNombre().equals(fuerzaTO.getNombre())) {
								grabar=false;
								break;
							}
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("El código o el nombre se encuentran repetidos");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarFuerza(fuerzaTO);
					id=fuerzaTO.getNpid().toString();
					jsonObject.put("fuerza", (JSONObject)JSONSerializer.toJSON(fuerzaTO,fuerzaTO.getJsonConfig()));
				}
			}

			//Gradofuerza
			else if(clase.equals("gradofuerza")){
				GradofuerzaTO gradofuerzaTO = gson.fromJson(new StringReader(objeto), GradofuerzaTO.class);
				log.println("grado superior*** " + gradofuerzaTO.getGradofuerzapadreid());
				accion = (gradofuerzaTO.getId()==null)?"I":"U";
				//valido que no exista creado un registro para ese grado
				GradofuerzaTO gradoesfuerza=new GradofuerzaTO();
				gradoesfuerza.setGradofuerzagradoid(gradofuerzaTO.getGradofuerzagradoid());
				gradoesfuerza.setGradofuerzafuerzaid(gradofuerzaTO.getGradofuerzafuerzaid());
				Collection<GradofuerzaTO> grados=UtilSession.adminsitracionServicio.transObtenerGradofuerza(gradoesfuerza);
				if(grados.size()>0)
					gradoesfuerza=(GradofuerzaTO)grados.iterator().next();
				log.println("escalarmu encontrados: " + grados.size());
				log.println("grado: " + gradoesfuerza.getGradofuerzafuerzaid() + "-" + gradoesfuerza.getGradofuerzafuerzaid());
				log.println("fuerza: " + gradoesfuerza.getGradofuerzagradoid() + "-" + gradofuerzaTO.getGradofuerzagradoid());
				if(grados.size()>0 && (gradofuerzaTO.getId()==null && (gradoesfuerza.getGradofuerzafuerzaid().longValue()==gradofuerzaTO.getGradofuerzafuerzaid().longValue() || gradoesfuerza.getGradofuerzagradoid().longValue()==gradofuerzaTO.getGradofuerzagradoid().longValue()))){
					mensajes.setMsg("Ya existe un registro creado para el grado seleccionado");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else {
					log.println("entro a grabar");
					//pregunto si ya existe el codigo en el nivel actual
					GradoescalaTO gradoescalaTO3=new GradoescalaTO();
					gradoescalaTO3.setCodigo(gradofuerzaTO.getCodigo());
					Collection<GradoescalaTO> gradoescalaTOs=UtilSession.adminsitracionServicio.transObtenerGradoescala(gradoescalaTO3);
					boolean grabar=true;
					if(gradoescalaTOs.size()>0){
						for(GradoescalaTO gradoescalaTO2:gradoescalaTOs) {
							//gradoescalaTO2=(GradoescalaTO)escalarmuTOs.iterator().next();
							if((gradofuerzaTO.getId()!=null && gradofuerzaTO.getId().longValue()!=0) && gradoescalaTO2.getId().longValue()!=gradofuerzaTO.getId().longValue() && gradoescalaTO2.getCodigo().equals(gradofuerzaTO.getCodigo())) {
								grabar=false;
								break;
							}
							else if((gradofuerzaTO.getId()==null || (gradofuerzaTO.getId()!=null && gradoescalaTO2.getId().longValue()!=gradofuerzaTO.getId().longValue())) && gradofuerzaTO.getCodigo()!=null && gradoescalaTO2.getCodigo().equals(gradofuerzaTO.getCodigo())) {
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
						UtilSession.adminsitracionServicio.transCrearModificarGradofuerza(gradofuerzaTO);
						id=gradofuerzaTO.getNpid().toString();
//						jsonObject.put("gradoescala", (JSONObject)JSONSerializer.toJSON(gradofuerzaTO,gradofuerzaTO.getJsonConfig()));
					}
				}
				
				
				
				//pregunto si ya existe el codigo en el nivel actual
//				GradofuerzaTO gradofuerzaTO2=new GradofuerzaTO();
//				gradofuerzaTO2.setCodigo(gradofuerzaTO.getCodigo());
//				Collection<GradofuerzaTO> gradofuerzaTOs=UtilSession.adminsitracionServicio.transObtenerGradofuerza(gradofuerzaTO2);
//				boolean grabar=true;
//				if(gradofuerzaTOs.size()>0){
//					gradofuerzaTO2=(GradofuerzaTO)gradofuerzaTOs.iterator().next();
//					if((gradofuerzaTO.getId()!=null && gradofuerzaTO.getId().longValue()!=0) && gradofuerzaTO2.getId().longValue()!=gradofuerzaTO.getId().longValue())
//						grabar=false;
//					else if((gradofuerzaTO.getId()==null || (gradofuerzaTO.getId()!=null && gradofuerzaTO2.getId().longValue()!=gradofuerzaTO.getId().longValue())) && gradofuerzaTO.getCodigo()!=null && gradofuerzaTO2.getCodigo().equals(gradofuerzaTO.getCodigo()))
//						grabar=false;
//
//				}dfa
//				if(!grabar){
//					mensajes.setMsg(MensajesWeb.getString("error.codigo.duplicado"));
//					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
//				}
//				else{
//					UtilSession.adminsitracionServicio.transCrearModificarGradofuerza(gradofuerzaTO);
//					id=gradofuerzaTO.getId().toString();
//					//jsonObject.put("gradofuerza", (JSONObject)JSONSerializer.toJSON(gradofuerzaTO,gradofuerzaTO.getJsonConfig()));
//				}
			}

			//especialidades
			else if(clase.equals("especialidades")){
				EspecialidadTO especialidadTO = gson.fromJson(new StringReader(objeto), EspecialidadTO.class);
				accion = (especialidadTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				EspecialidadTO especialidadTO3=new EspecialidadTO();
				especialidadTO3.setCodigo(especialidadTO.getCodigo());
				Collection<EspecialidadTO> especialidadTOs=UtilSession.adminsitracionServicio.transObtenerEspecialidad(especialidadTO3);
				boolean grabar=true;
				if(especialidadTOs.size()>0){
					for(EspecialidadTO especialidadTO2:especialidadTOs) {
						//especialidadTO2=(EspecialidadTO)especialidadTOs.iterator().next();
						if((especialidadTO.getId()!=null && especialidadTO.getId().longValue()!=0) && especialidadTO2.getId().longValue()!=especialidadTO.getId().longValue() && especialidadTO2.getCodigo().equals(especialidadTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((especialidadTO.getId()==null || (especialidadTO.getId()!=null && especialidadTO2.getId().longValue()!=especialidadTO.getId().longValue())) && especialidadTO.getCodigo()!=null && especialidadTO2.getCodigo().equals(especialidadTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarEspecialidad(especialidadTO);
					id=especialidadTO.getNpid().toString();
					jsonObject.put("especialidades", (JSONObject)JSONSerializer.toJSON(especialidadTO,especialidadTO.getJsonConfig()));
				}
			}

			//Cargo
			else if(clase.equals("cargo")){
				CargoTO cargoTO = gson.fromJson(new StringReader(objeto), CargoTO.class);
				accion = (cargoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				CargoTO cargoTO4=new CargoTO();
				cargoTO4.setCodigo(cargoTO.getCodigo());
				Collection<CargoTO> cargoTOs=UtilSession.adminsitracionServicio.transObtenerCargo(cargoTO4);
				boolean grabar=true;
				if(cargoTOs.size()>0){
					for(CargoTO cargoTO2:cargoTOs) {
						//cargoTO2=(CargoTO)cargoTOs.iterator().next();
						if((cargoTO.getId()!=null && cargoTO.getId().longValue()!=0) && cargoTO2.getId().longValue()!=cargoTO.getId().longValue() && cargoTO2.getCodigo().equals(cargoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((cargoTO.getId()==null || (cargoTO.getId()!=null && cargoTO2.getId().longValue()!=cargoTO.getId().longValue())) && cargoTO.getCodigo()!=null && cargoTO2.getCodigo().equals(cargoTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				if(grabar) {
					CargoTO cargoTO5=new CargoTO();
					cargoTO5.setNombre(cargoTO.getNombre());
					Collection<CargoTO> cargoTOs2=UtilSession.adminsitracionServicio.transObtenerCargo(cargoTO5);
					if(cargoTOs2.size()>0){
						for(CargoTO cargoTO3:cargoTOs2) {
							//cargoTO3=(CargoTO)cargoTOs2.iterator().next();
							if((cargoTO.getId()!=null && cargoTO.getId().longValue()!=0) && cargoTO3.getId().longValue()!=cargoTO.getId().longValue() && cargoTO3.getNombre().equals(cargoTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((cargoTO.getId()==null || (cargoTO.getId()!=null && cargoTO3.getId().longValue()!=cargoTO.getId().longValue())) && cargoTO.getNombre()!=null && cargoTO3.getNombre().equals(cargoTO.getNombre())) {
								grabar=false;
								break;
							}
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("El código o el nombre se encuentran repetidos");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.adminsitracionServicio.transCrearModificarCargo(cargoTO);
					//id=cargoTO.getNpid().toString();
					jsonObject.put("cargo", (JSONObject)JSONSerializer.toJSON(cargoTO,cargoTO.getJsonConfig()));
				}
			}

			//Escalarmu
			else if(clase.equals("escalarmu")){
				EscalarmuTO escalarmuTO = gson.fromJson(new StringReader(objeto), EscalarmuTO.class);
				accion = (escalarmuTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				EscalarmuTO escalarmuTO3=new EscalarmuTO();
				escalarmuTO3.setCodigo(escalarmuTO.getCodigo());
				Collection<EscalarmuTO> escalarmuTOs=UtilSession.adminsitracionServicio.transObtenerEscalarmu(escalarmuTO3);
				log.println("escalarmu encontrados: " + escalarmuTOs.size());
				boolean grabar=true;
				if(escalarmuTOs.size()>0){
					for(EscalarmuTO escalarmuTO2:escalarmuTOs) {
						//escalarmuTO2=(EscalarmuTO)escalarmuTOs.iterator().next();
						if((escalarmuTO.getId()!=null && escalarmuTO.getId().longValue()!=0) && escalarmuTO2.getId().longValue()!=escalarmuTO.getId().longValue() && escalarmuTO2.getCodigo().equals(escalarmuTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((escalarmuTO.getId()==null || (escalarmuTO.getId()!=null && escalarmuTO2.getId().longValue()!=escalarmuTO.getId().longValue())) && escalarmuTO.getCodigo()!=null && escalarmuTO2.getCodigo().equals(escalarmuTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarEscalarmu(escalarmuTO);
					id=escalarmuTO.getNpid().toString();
					jsonObject.put("escalarmu", (JSONObject)JSONSerializer.toJSON(escalarmuTO,escalarmuTO.getJsonConfig()));
				}
			}

			else if(clase.equals("gradoescala")){
				GradoescalaTO gradoescalaTO = gson.fromJson(new StringReader(objeto), GradoescalaTO.class);
				accion = (gradoescalaTO.getId()==null)?"I":"U";
				//valido que no exista creado un registro para ese grado
				GradoescalaTO gradoescala=new GradoescalaTO();
				gradoescala.setGegradofuerzaid(gradoescalaTO.getGegradofuerzaid());
				Collection<GradoescalaTO> grados=UtilSession.adminsitracionServicio.transObtenerGradoescala(gradoescala);
				if(grados.size()>0)
					gradoescala=(GradoescalaTO)grados.iterator().next();
				log.println("escalarmu encontrados: " + grados.size());
				if(grados.size()==0 || (grados.size()>0 && gradoescala.getId().longValue()==gradoescalaTO.getId().longValue())){
					//pregunto si ya existe el codigo en el nivel actual
					GradoescalaTO gradoescalaTO3=new GradoescalaTO();
					gradoescalaTO3.setCodigo(gradoescalaTO.getCodigo());
					Collection<GradoescalaTO> escalarmuTOs=UtilSession.adminsitracionServicio.transObtenerGradoescala(gradoescalaTO3);
					boolean grabar=true;
					if(escalarmuTOs.size()>0){
						for(GradoescalaTO gradoescalaTO2:escalarmuTOs) {
							//gradoescalaTO2=(GradoescalaTO)escalarmuTOs.iterator().next();
							if((gradoescalaTO.getId()!=null && gradoescalaTO.getId().longValue()!=0) && gradoescalaTO2.getId().longValue()!=gradoescalaTO.getId().longValue() && gradoescalaTO2.getCodigo().equals(gradoescalaTO.getCodigo())) {
								grabar=false;
								break;
							}
							else if((gradoescalaTO.getId()==null || (gradoescalaTO.getId()!=null && gradoescalaTO2.getId().longValue()!=gradoescalaTO.getId().longValue())) && gradoescalaTO.getCodigo()!=null && gradoescalaTO2.getCodigo().equals(gradoescalaTO.getCodigo())) {
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
						UtilSession.adminsitracionServicio.transCrearModificarGradoescala(gradoescalaTO);
						id=gradoescalaTO.getNpid().toString();
						jsonObject.put("gradoescala", (JSONObject)JSONSerializer.toJSON(gradoescalaTO,gradoescalaTO.getJsonConfig()));
					}
				}
				else{
					mensajes.setMsg("Ya existe un registro creado para el grado seleccionado");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
			}

			else if(clase.equals("cargoescala")){
				CargoescalaTO cargoescalaTO = gson.fromJson(new StringReader(objeto), CargoescalaTO.class);
				accion = (cargoescalaTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				CargoescalaTO cargoescalaTO3=new CargoescalaTO();
				cargoescalaTO3.setCodigo(cargoescalaTO.getCodigo());
				Collection<CargoescalaTO> cargoescalaTOs=UtilSession.adminsitracionServicio.transObtenerCargoescala(cargoescalaTO3);
				boolean grabar=true;
				if(cargoescalaTOs.size()>0){
					for(CargoescalaTO cargoescalaTO2:cargoescalaTOs) {
						//cargoescalaTO2=(CargoescalaTO)cargoescalaTOs.iterator().next();
						if((cargoescalaTO.getId()!=null && cargoescalaTO.getId().longValue()!=0) && cargoescalaTO2.getId().longValue()!=cargoescalaTO.getId().longValue() && cargoescalaTO2.getCodigo().equals(cargoescalaTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((cargoescalaTO.getId()==null || (cargoescalaTO.getId()!=null && cargoescalaTO2.getId().longValue()!=cargoescalaTO.getId().longValue())) && cargoescalaTO.getCodigo()!=null && cargoescalaTO2.getCodigo().equals(cargoescalaTO.getCodigo())) {
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
					UtilSession.adminsitracionServicio.transCrearModificarCargoescala(cargoescalaTO);
					id=cargoescalaTO.getNpid().toString();
					jsonObject.put("cargoescala", (JSONObject)JSONSerializer.toJSON(cargoescalaTO,cargoescalaTO.getJsonConfig()));
				}
			}

			else if(clase.equals("nivelorganico")){
				NivelorganicoTO nivelorganicoTO = gson.fromJson(new StringReader(objeto), NivelorganicoTO.class);
				accion = (nivelorganicoTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				NivelorganicoTO nivelorganicoTO4=new NivelorganicoTO();
				nivelorganicoTO4.setCodigo(nivelorganicoTO.getCodigo());
				Collection<NivelorganicoTO> nivelorganicoTOs=UtilSession.planificacionServicio.transObtenerNivelorganido(nivelorganicoTO4);
				boolean grabar=true;
				if(nivelorganicoTOs.size()>0){
					for(NivelorganicoTO nivelorganicoTO2:nivelorganicoTOs) {
						//nivelorganicoTO2=(NivelorganicoTO)nivelorganicoTOs.iterator().next();
						if((nivelorganicoTO.getId()!=null && nivelorganicoTO.getId().longValue()!=0) && nivelorganicoTO2.getId().longValue()!=nivelorganicoTO.getId().longValue() && nivelorganicoTO2.getCodigo().equals(nivelorganicoTO.getCodigo())) {
							grabar=false;
							break;
						}
						else if((nivelorganicoTO.getId()==null || (nivelorganicoTO.getId()!=null && nivelorganicoTO2.getId().longValue()!=nivelorganicoTO.getId().longValue())) && nivelorganicoTO.getCodigo()!=null && nivelorganicoTO2.getCodigo().equals(nivelorganicoTO.getCodigo())) {
							grabar=false;
							break;
						}
					}
				}
				if(grabar) {
					NivelorganicoTO nivelorganicoTO5=new NivelorganicoTO();
					nivelorganicoTO5.setNombre(nivelorganicoTO.getNombre());
					Collection<NivelorganicoTO> nivelorganicoTO1s=UtilSession.planificacionServicio.transObtenerNivelorganido(nivelorganicoTO5);
					if(nivelorganicoTO1s.size()>0){
						for(NivelorganicoTO nivelorganicoTO3:nivelorganicoTO1s) {
							//nivelorganicoTO3=(NivelorganicoTO)nivelorganicoTO1s.iterator().next();
							if((nivelorganicoTO.getId()!=null && nivelorganicoTO.getId().longValue()!=0) && nivelorganicoTO3.getId().longValue()!=nivelorganicoTO.getId().longValue() && nivelorganicoTO3.getNombre().equals(nivelorganicoTO.getNombre())) {
								grabar=false;
								break;
							}
							else if((nivelorganicoTO.getId()==null || (nivelorganicoTO.getId()!=null && nivelorganicoTO3.getId().longValue()!=nivelorganicoTO.getId().longValue())) && nivelorganicoTO.getNombre()!=null && nivelorganicoTO3.getNombre().equals(nivelorganicoTO.getNombre())){
								grabar=false;
								break;
							}
						}
					}
				}
				if(!grabar){
					mensajes.setMsg("El código o el nombre se encuentran repetidos");
					mensajes.setType(MensajesWeb.getString("mensaje.alerta"));
				}
				else{
					UtilSession.planificacionServicio.transCrearModificarNivelorganido(nivelorganicoTO);
					//.....id=nivelorganicoTO.getNpid().toString();
					jsonObject.put("cargoescala", (JSONObject)JSONSerializer.toJSON(nivelorganicoTO,nivelorganicoTO.getJsonConfig()));
				}
			}
			//Institucion
			else if(clase.equals("institucion")){
				InstitucionTO institucionTO = gson.fromJson(new StringReader(objeto), InstitucionTO.class);
				accion = (institucionTO.getId()==null)?"I":"U";
				//pregunto si ya existe el codigo en el nivel actual
				InstitucionTO institucionTO3=new InstitucionTO();
				institucionTO3.setCodigo(institucionTO.getCodigo());
				institucionTO3.setInstitucionejerciciofiscalid(institucionTO.getInstitucionejerciciofiscalid());
				Collection<InstitucionTO> institucionTOs=UtilSession.estructuraorganicaServicio.transObtenerInstitucion(institucionTO3);
				boolean grabar=true;
				if(institucionTOs.size()>0){
					for(InstitucionTO institucionTO2:institucionTOs) {
						//institucionTO2=(InstitucionTO)itemTOs.iterator().next();
						if(institucionTO.getId()!=null && institucionTO2.getId().longValue()!=institucionTO.getId().longValue()) {
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
					UtilSession.estructuraorganicaServicio.transCrearModificarInstitucion(institucionTO);
					//id=institucionTO.getNpid().toString();
					jsonObject.put("institucion", (JSONObject)JSONSerializer.toJSON(institucionTO,institucionTO.getJsonConfig()));
				}
			}

			//Contrato
			else if(clase.equals("contrato")){
				ContratoTO contratoTO = gson.fromJson(new StringReader(objeto), ContratoTO.class);
				accion = (contratoTO.getId()==null)?"I":"U";
				if(contratoTO.getNpfechainicio()!=null)
					contratoTO.setFechainicio(UtilGeneral.parseStringToDate(contratoTO.getNpfechainicio()));
				contratoTO.setSocionegocio(new SocionegocioTO());
				UtilSession.planificacionServicio.transCrearModificarContrato(contratoTO);
			//id=contratoTO.getNpid().toString();
				contratoTO.setId(contratoTO.getNpid());
				jsonObject.put("contrato", (JSONObject)JSONSerializer.toJSON(contratoTO,contratoTO.getJsonConfigedicion()));
			}

			//Registro la auditoria
			if(mensajes.getMsg()==null) {
				ComunController.crearAuditoria(request, clase, accion, objeto, id);
				mensajes.setMsg(MensajesWeb.getString("mensaje.guardar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			else if(!clase.equals("copiardatos"))
				respuesta.setEstado(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error grabar");
			if(clase.equals("copiardatos"))
				mensajes.setMsg("Los datos ya fueron copiados, no se van a volver a copiar");
			else
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
		log.println("entra al metodo recuperar: " + clase + " - " + id);
		JSONObject jsonObject=new JSONObject();
		Mensajes mensajes=new Mensajes();
		Respuesta respuesta=new Respuesta();
		try {
			//Ejerciciofiscal
			if(clase.equals("ejerciciofiscal")){
				EjerciciofiscalTO ejerciciofiscalTO = UtilSession.adminsitracionServicio.transObtenerEjerciciofiscalTO(id);
				jsonObject.put("ejerciciofiscal", (JSONObject)JSONSerializer.toJSON(ejerciciofiscalTO,ejerciciofiscalTO.getJsonConfig()));
			}

			//Divisiones geograficas
			else if(clase.equals("divisiongeografica")){
				DivisiongeograficaTO divisiongeograficaTO = UtilSession.adminsitracionServicio.transObtenerDivisiongeograficaTO(id);
				jsonObject.put("divisiongeografica", (JSONObject)JSONSerializer.toJSON(divisiongeograficaTO,divisiongeograficaTO.getJsonConfigEdit()));
			}

			//Unidadmedida
			else if(clase.equals("unidadmedida")){
				UnidadmedidaTO unidadmedidaTO = UtilSession.adminsitracionServicio.transObtenerUnidadmedidaTO(id);
				jsonObject.put("unidadmedida", (JSONObject)JSONSerializer.toJSON(unidadmedidaTO,unidadmedidaTO.getJsonConfig()));
			}

			//Parametro
			else if(clase.equals("parametro")){
				ParametroTO parametroTO = UtilSession.adminsitracionServicio.transObtenerParametroTO(id);
				jsonObject.put("parametro", (JSONObject)JSONSerializer.toJSON(parametroTO,parametroTO.getJsonConfig()));
			}

			//Consecutivo
			else if(clase.equals("consecutivo")){
				ConsecutivoTO consecutivoTO = UtilSession.adminsitracionServicio.transObtenerConsecutivoTO(id);
				jsonObject.put("consecutivo", (JSONObject)JSONSerializer.toJSON(consecutivoTO,consecutivoTO.getJsonConfig()));
			}

			//Tipoidentificacion
			else if(clase.equals("tipoidentificacion")){
				TipoidentificacionTO tipoidentificacionTO = UtilSession.adminsitracionServicio.transObtenerTipoidentificacionTO(id);
				//Traigo la coleccion de tipo identificacion tipo
				TipoidentificaciontipoTO tipoidentificaciontipoTO=new TipoidentificaciontipoTO();
				tipoidentificaciontipoTO.getId().setIdentificacionid(tipoidentificacionTO.getId());
				tipoidentificaciontipoTO.setTipoidentificacion(new TipoidentificacionTO());
				Collection<TipoidentificaciontipoTO> tipoidentificaciontipoTOs=UtilSession.adminsitracionServicio.transObtenerTipoidentificaciontipo(tipoidentificaciontipoTO);
				jsonObject.put("tipoidentificacion", (JSONObject)JSONSerializer.toJSON(tipoidentificacionTO,tipoidentificacionTO.getJsonConfig()));
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(tipoidentificaciontipoTOs,tipoidentificaciontipoTO.getJsonConfig()));
			}

			//Fuentefinanciamiento
			else if(clase.equals("fuentefinanciamiento")){
				FuentefinanciamientoTO fuentefinanciamientoTO = UtilSession.adminsitracionServicio.transObtenerFuentefinanciamientoTO(id);
				jsonObject.put("fuentefinanciamiento", (JSONObject)JSONSerializer.toJSON(fuentefinanciamientoTO,fuentefinanciamientoTO.getJsonConfig()));
			}

			//Organismo
			else if(clase.equals("organismo")){
				OrganismoTO organismoTO = UtilSession.adminsitracionServicio.transObtenerOrganismoTO(id);
				jsonObject.put("organismo", (JSONObject)JSONSerializer.toJSON(organismoTO,organismoTO.getJsonConfig()));
				//Traigo la coleccion
				OrganismoprestamoTO organismoprestamoTO=new OrganismoprestamoTO();
				organismoprestamoTO.getId().setId(organismoTO.getId());
				organismoprestamoTO.setOrganismo(new OrganismoTO());
				Collection<OrganismoprestamoTO> organismoprestamoTOs=UtilSession.planificacionServicio.transObtenerOrganismoprestamo(organismoprestamoTO);
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(organismoprestamoTOs,organismoprestamoTO.getJsonConfig()));
			}

			//Obra
			else if(clase.equals("obra")){
				ObraTO obraTO = UtilSession.adminsitracionServicio.transObtenerObraTO(id);
				jsonObject.put("obra", (JSONObject)JSONSerializer.toJSON(obraTO,obraTO.getJsonConfig()));
			}

			//Item
			else if(clase.equals("item")){
				ItemTO itemTO = UtilSession.adminsitracionServicio.transObtenerItemTO(id);
				jsonObject.put("item", (JSONObject)JSONSerializer.toJSON(itemTO,itemTO.getJsonConfigedit()));
			}

			//Subitem
			else if(clase.equals("subitem")){
				SubitemTO subitemTO = UtilSession.adminsitracionServicio.transObtenerSubitemTO(id);
				log.println("unidadmedida: " + subitemTO.getSubitemunidadmedidaid());
				jsonObject.put("subitem", (JSONObject)JSONSerializer.toJSON(subitemTO,subitemTO.getJsonConfigedit()));
			}

			//Grupomedida
			else if(clase.equals("grupomedida")){
				GrupomedidaTO grupomedidaTO = UtilSession.adminsitracionServicio.transObtenerGrupomedidaTO(id);
				jsonObject.put("grupomedida", (JSONObject)JSONSerializer.toJSON(grupomedidaTO,grupomedidaTO.getJsonConfig()));
			}

			//Claseregistro
			else if(clase.equals("claseregistro")){
				ClaseregistroTO claseregistroTO = UtilSession.adminsitracionServicio.transObtenerClaseregistroTO(id);
				//Traigo la coleccion de ClaseregistroclasemodificacionTO
				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO=new ClaseregistroclasemodificacionTO();
				claseregistroclasemodificacionTO.getId().setRegistroid(claseregistroTO.getId());
				claseregistroclasemodificacionTO.setClaseregistro(new ClaseregistroTO());
				Collection<ClaseregistroclasemodificacionTO> claseregistroclasemodificacionTOs=UtilSession.adminsitracionServicio.transObtenerClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
				jsonObject.put("claseregistro", (JSONObject)JSONSerializer.toJSON(claseregistroTO,claseregistroTO.getJsonConfig()));
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(claseregistroclasemodificacionTOs,claseregistroclasemodificacionTO.getJsonConfig()));
			}

			//Clasemodificacion
			else if(clase.equals("clasemodificacion")){
				ClaseregistroclasemodificacionID claseregistroclasemodificacionID=new ClaseregistroclasemodificacionID();
				claseregistroclasemodificacionID.setRegistrocmid(id);
				claseregistroclasemodificacionID.setRegistroid(id2);
				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO = UtilSession.adminsitracionServicio.transObtenerClaseregistroclasemodificacionTO(claseregistroclasemodificacionID);
				//Traigo la coleccion de Clasegasto
				ClaseregistrocmcgastoTO claseregistrocmcgastoTO=new ClaseregistrocmcgastoTO();
				claseregistrocmcgastoTO.getId().setId(claseregistroclasemodificacionTO.getId().getRegistroid());
				claseregistrocmcgastoTO.getId().setCmid(claseregistroclasemodificacionTO.getId().getRegistrocmid());
				Collection<ClaseregistrocmcgastoTO> claseregistrocmcgastoTOs=UtilSession.planificacionServicio.transObtenerClaseregistrocmcgasto(claseregistrocmcgastoTO);
				jsonObject.put("clasemodificacion", (JSONObject)JSONSerializer.toJSON(claseregistroclasemodificacionTO,claseregistroclasemodificacionTO.getJsonConfig()));
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(claseregistrocmcgastoTOs,claseregistrocmcgastoTO.getJsonConfig()));
			}

			//Tipodocumento
			else if(clase.equals("tipodocumento")){
				TipodocumentoTO tipodocumentoTO = UtilSession.adminsitracionServicio.transObtenerTipodocumentoTO(id);
				//Traigo la coleccion de tipo documento tipo
				TipodocumentoclasedocumentoTO tipodocumentoclasedocumentoTO=new TipodocumentoclasedocumentoTO();
				tipodocumentoclasedocumentoTO.getId().setId(tipodocumentoTO.getId());
				Collection<TipodocumentoclasedocumentoTO> tipodocumentoclasedocumentoTOs=UtilSession.adminsitracionServicio.transObtenerTipodocumentoclasedocumento(tipodocumentoclasedocumentoTO);
				jsonObject.put("tipodocumento", (JSONObject)JSONSerializer.toJSON(tipodocumentoTO,tipodocumentoTO.getJsonConfig()));
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(tipodocumentoclasedocumentoTOs,tipodocumentoclasedocumentoTO.getJsonConfig()));
			}

			//Socionegocio
			else if(clase.equals("socionegocio")){
				SocionegocioTO socionegocioTO = UtilSession.adminsitracionServicio.transObtenerSocionegocioTO(id);
				jsonObject.put("socionegocio", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfig()));
			}

			//Empleado
			else if(clase.equals("empleado")){
				SocionegocioTO socionegocioTO = UtilSession.adminsitracionServicio.transObtenerSocionegocioTO(id);
				jsonObject.put("empleado", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfigEmpleadoedit()));
			}

			//Empleadoproveedor
			else if(clase.equals("empleadoproveedor")){
				SocionegocioTO socionegocioTO = UtilSession.adminsitracionServicio.transObtenerSocionegocioTO(id);
				jsonObject.put("empleadoproveedor", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfigEmpleadoedit()));
			}

			//Proveedorruc
			else if(clase.equals("proveedorruc")){
				SocionegocioTO socionegocioTO = UtilSession.adminsitracionServicio.transObtenerSocionegocioTO(id);
				jsonObject.put("empleadoproveedor", (JSONObject)JSONSerializer.toJSON(socionegocioTO,socionegocioTO.getJsonConfigEmpleadoedit()));
			}

			//Parametroindicador
			else if(clase.equals("parametroindicador")){
				ParametroindicadorTO parametroindicadorTO = UtilSession.adminsitracionServicio.transObtenerParametroindicadorTO(id);
				jsonObject.put("parametroindicador", (JSONObject)JSONSerializer.toJSON(parametroindicadorTO,parametroindicadorTO.getJsonConfig()));
			}

			//Tipoproducto
			else if(clase.equals("tipoproducto")){
				TipoproductoTO tipoproductoTO = UtilSession.adminsitracionServicio.transObtenerTipoproductoTO(id);
				jsonObject.put("tipoproducto", (JSONObject)JSONSerializer.toJSON(tipoproductoTO,tipoproductoTO.getJsonConfig()));
			}

			//Tiporegimen
			else if(clase.equals("tiporegimen")){
				TiporegimenTO tiporegimenTO = UtilSession.adminsitracionServicio.transObtenerTiporegimenTO(id);
				jsonObject.put("tiporegimen", (JSONObject)JSONSerializer.toJSON(tiporegimenTO,tiporegimenTO.getJsonConfig()));
			}

			//Procedimiento
			else if(clase.equals("procedimiento")){
				ProcedimientoTO procedimientoTO = UtilSession.adminsitracionServicio.transObtenerProcedimientoTO(id);
				jsonObject.put("procedimiento", (JSONObject)JSONSerializer.toJSON(procedimientoTO,procedimientoTO.getJsonConfig()));
			}

			//Grupo
			else if(clase.equals("grupo")){
				GrupoTO grupoTO = UtilSession.adminsitracionServicio.transObtenerGrupoTO(id);
				jsonObject.put("grupo", (JSONObject)JSONSerializer.toJSON(grupoTO,grupoTO.getJsonConfig()));
			}

			//Grado
			else if(clase.equals("grado")){
				GradoTO gradoTO = UtilSession.adminsitracionServicio.transObtenerGradoTO(id);
				jsonObject.put("grado", (JSONObject)JSONSerializer.toJSON(gradoTO,gradoTO.getJsonConfig()));
			}

			//Clasificacion
			else if(clase.equals("clasificacion")){
				ClasificacionTO clasificacionTO = UtilSession.adminsitracionServicio.transObtenerClasificacionTO(id);
				jsonObject.put("clasificacion", (JSONObject)JSONSerializer.toJSON(clasificacionTO,clasificacionTO.getJsonConfig()));
			}

			//Fuerza
			else if(clase.equals("fuerza")){
				FuerzaTO fuerzaTO = UtilSession.adminsitracionServicio.transObtenerFuerzaTO(id);
				log.println("fuerza: "+ fuerzaTO.getId());
				jsonObject.put("fuerza", (JSONObject)JSONSerializer.toJSON(fuerzaTO,fuerzaTO.getJsonConfig()));
				FuerzaclasificacionTO fuerzaclasificacionTO=new FuerzaclasificacionTO();
				fuerzaclasificacionTO.getId().setFuerzaid(fuerzaTO.getId());
				fuerzaclasificacionTO.setFuerzaTO(new FuerzaTO());
				fuerzaclasificacionTO.setClasificacionTO(new ClasificacionTO());
				Collection<FuerzaclasificacionTO> fuerzaclasificacionTOs=UtilSession.adminsitracionServicio.transObtenerFuerzaclasificacion(fuerzaclasificacionTO);
				log.println("fuerza clasificacion: " + fuerzaclasificacionTOs.size());
//				//Asigno la variable que necesitan para la administracion
//				for(FuerzaclasificacionTO fuerzaclasificacionTO2:fuerzaclasificacionTOs)
//					fuerzaclasificacionTO2.setFuerzaclasificacionid(fuerzaclasificacionTO2.getId().getFuerzaclasificacionid());
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(fuerzaclasificacionTOs,fuerzaclasificacionTO.getJsonConfig()));

			}

			//Gradofuerza
			else if(clase.equals("gradofuerza")){
				GradofuerzaTO gradofuerzaTO = UtilSession.adminsitracionServicio.transObtenerGradofuerzaTO(id);
				jsonObject.put("gradofuerza", (JSONObject)JSONSerializer.toJSON(gradofuerzaTO,gradofuerzaTO.getJsonConfigedit()));
			}

			//especialidades
			else if(clase.equals("especialidades")){
				EspecialidadTO especialidadTO = UtilSession.adminsitracionServicio.transObtenerEspecialidadTO(id);
				jsonObject.put("especialidades", (JSONObject)JSONSerializer.toJSON(especialidadTO,especialidadTO.getJsonConfig()));
			}

			//Cargo
			else if(clase.equals("cargo")){
				CargoTO cargoTO = UtilSession.adminsitracionServicio.transObtenerCargoTO(id);
				jsonObject.put("cargo", (JSONObject)JSONSerializer.toJSON(cargoTO,cargoTO.getJsonConfig()));
			}

			//Escalarmu
			else if(clase.equals("escalarmu")){
				EscalarmuTO escalarmuTO = UtilSession.adminsitracionServicio.transObtenerEscalarmuTO(id);
				jsonObject.put("escalarmu", (JSONObject)JSONSerializer.toJSON(escalarmuTO,escalarmuTO.getJsonConfig()));
			}

			//Gradoescala
			else if(clase.equals("gradoescala")){
				GradoescalaTO gradoescalaTO = UtilSession.adminsitracionServicio.transObtenerGradoescalaTO(id);
				jsonObject.put("gradoescala", (JSONObject)JSONSerializer.toJSON(gradoescalaTO,gradoescalaTO.getJsonConfig()));
			}

			//Cargoescala
			else if(clase.equals("cargoescala")){
				CargoescalaTO cargoescalaTO = UtilSession.adminsitracionServicio.transObtenerCargoescalaTO(id);
				jsonObject.put("cargoescala", (JSONObject)JSONSerializer.toJSON(cargoescalaTO,cargoescalaTO.getJsonConfig()));
			}

			//Nivelorganico
			else if(clase.equals("nivelorganico")){
				NivelorganicoTO nivelorganicoTO = UtilSession.planificacionServicio.transObtenerNivelorganidoTO(id);
				jsonObject.put("nivelorganico", (JSONObject)JSONSerializer.toJSON(nivelorganicoTO,nivelorganicoTO.getJsonConfig()));
			}
			//Institucion
			else if(clase.equals("institucion")){
				InstitucionTO institucionTO = UtilSession.estructuraorganicaServicio.transObtenerInstitucionTO(id);
				jsonObject.put("institucion", (JSONObject)JSONSerializer.toJSON(institucionTO,institucionTO.getJsonConfig()));
				//Consulto la coleccion de institucion entidad
				InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
				institucionentidadTO.getId().setId(institucionTO.getId());
				institucionentidadTO.setDivisiongeografica1(new DivisiongeograficaTO());
				institucionentidadTO.setDivisiongeografica2(new DivisiongeograficaTO());
				institucionentidadTO.setDivisiongeografica3(new DivisiongeograficaTO());
				InstitucionTO institucionTO2=new InstitucionTO();
				institucionTO2.setEjerciciofiscal(new EjerciciofiscalTO());
				institucionentidadTO.setInstitucion(institucionTO2);
				Collection<InstitucionentidadTO> institucionentidadTOs=UtilSession.estructuraorganicaServicio.transObtenerInstitucionentidad(institucionentidadTO);
				jsonObject.put("details", (JSONArray)JSONSerializer.toJSON(institucionentidadTOs,institucionentidadTO.getJsonConfig()));
			}
			//Contrato
			else if(clase.equals("contrato")){
				ContratoTO contratoTO = UtilSession.planificacionServicio.transObtenerContratoTO(id);
				//contratoTO.setSocionegocio(new SocionegocioTO());
				System.out.println("socio negocio: " + contratoTO.getNpproveedorcodigov());
				if(contratoTO.getFechainicio()!=null)
					contratoTO.setNpfechainicio(UtilGeneral.parseDateToString(contratoTO.getFechainicio()));
				jsonObject.put("contrato", (JSONObject)JSONSerializer.toJSON(contratoTO,contratoTO.getJsonConfigedicion()));
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
			//Ejerciciofiscal
			if(clase.equals("ejerciciofiscal")){
				UtilSession.adminsitracionServicio.transEliminarEjerciciofiscal(new EjerciciofiscalTO(id));
			}
			//Divisiones geograficas
			else if(clase.equals("divisiongeografica")){
				UtilSession.adminsitracionServicio.transEliminarDivisiongeografica(new DivisiongeograficaTO(id));
			}

			//Unidadmedida
			else if(clase.equals("unidadmedida")){
				UtilSession.adminsitracionServicio.transEliminarUnidadmedida(new UnidadmedidaTO(id));
			}

			//Parametro
			else if(clase.equals("parametro")){
				UtilSession.adminsitracionServicio.transEliminarParametro(new ParametroTO(id));
			}

			//Consecutivo
			else if(clase.equals("consecutivo")){
				UtilSession.adminsitracionServicio.transEliminarConsecutivo(new ConsecutivoTO(id));
			}

			//Tipoidentificacion
			else if(clase.equals("tipoidentificacion")){
				UtilSession.adminsitracionServicio.transEliminarTipoidentificacion(new TipoidentificacionTO(id));
			}

			//Fuentefinanciamiento
			else if(clase.equals("fuentefinanciamiento")){
				UtilSession.adminsitracionServicio.transEliminarFuentefinanciamiento(new FuentefinanciamientoTO(id));
			}

			//Organismo
			else if(clase.equals("organismo")){
				UtilSession.adminsitracionServicio.transEliminarOrganismo(new OrganismoTO(id));
			}


			//Obra
			else if(clase.equals("obra")){
				UtilSession.adminsitracionServicio.transEliminarObra(new ObraTO());
			}

			//Item
			else if(clase.equals("item")){
				UtilSession.adminsitracionServicio.transEliminarItem(new ItemTO(id));
			}

			//Subitem
			else if(clase.equals("subitem")){
				UtilSession.adminsitracionServicio.transEliminarSubitem(new SubitemTO(id));
			}

			//Grupomedida
			else if(clase.equals("grupomedida")){
				UtilSession.adminsitracionServicio.transEliminarGrupomedida(new GrupomedidaTO(id));
			}

			//Claseregistro
			else if(clase.equals("claseregistro")){
				UtilSession.adminsitracionServicio.transEliminarClaseregistro(new ClaseregistroTO(id));
			}

			//Procedimiento
			else if(clase.equals("procedimiento")){
				UtilSession.adminsitracionServicio.transEliminarProcedimiento(new ProcedimientoTO(id));
			}

			//Tipoproducto
			else if(clase.equals("tipoproducto")){
				UtilSession.adminsitracionServicio.transEliminarTipoproducto(new TipoproductoTO(id));
			}

			//Tiporegimen
			else if(clase.equals("tiporegimen")){
				UtilSession.adminsitracionServicio.transEliminarTiporegimen(new TiporegimenTO(id));
			}

			//Clasemodificacion
			else if(clase.equals("clasemodificacion")){
				ClaseregistroclasemodificacionID claseregistroclasemodificacionID=new ClaseregistroclasemodificacionID();
				claseregistroclasemodificacionID.setRegistrocmid(id);
				claseregistroclasemodificacionID.setRegistroid(id2);
				UtilSession.adminsitracionServicio.transEliminarClaseregistroclasemodificacion(new ClaseregistroclasemodificacionTO(claseregistroclasemodificacionID));
			}

			//Tipodocumento
			else if(clase.equals("tipodocumento")){
				UtilSession.adminsitracionServicio.transEliminarTipodocumento(new TipodocumentoTO(id));			
			}

			//Socionegocio
			else if(clase.equals("socionegocio")){
				UtilSession.adminsitracionServicio.transEliminarSocionegocio(new SocionegocioTO(id));
			}

			//Parametroindicador
			else if(clase.equals("parametroindicador")){
				UtilSession.adminsitracionServicio.transEliminarParametroindicador(new ParametroindicadorTO(id));
			}

			//Grupo
			else if(clase.equals("grupo")){
				UtilSession.adminsitracionServicio.transEliminarGrupo(new GrupoTO(id));
			}

			//Grado
			else if(clase.equals("grado")){
				UtilSession.adminsitracionServicio.transEliminarGrado(new GradoTO(id));
			}

			//Clasificacion
			else if(clase.equals("clasificacion")){
				UtilSession.adminsitracionServicio.transEliminarClasificacion(new ClasificacionTO(id));
			}

			//Fuerza
			else if(clase.equals("fuerza")){
				UtilSession.adminsitracionServicio.transEliminarFuerza(new FuerzaTO(id));
			}

			//Gradofuerza
			else if(clase.equals("gradofuerza")){
				UtilSession.adminsitracionServicio.transEliminarGradofuerza(new GradofuerzaTO(id));
			}

			//especialidades
			else if(clase.equals("especialidades")){
				UtilSession.adminsitracionServicio.transEliminarEspecialidad(new EspecialidadTO(id));
			}

			//Cargo
			else if(clase.equals("cargo")){
				UtilSession.adminsitracionServicio.transEliminarCargo(new CargoTO(id));
			}

			//Escalarmu
			else if(clase.equals("escalarmu")){
				UtilSession.adminsitracionServicio.transEliminarEscalarmu(new EscalarmuTO(id));
			}

			//Gradoescala
			else if(clase.equals("gradoescala")){
				UtilSession.adminsitracionServicio.transEliminarGradoescala(new GradoescalaTO(id));
			}

			//Cargoescala
			else if(clase.equals("cargoescala")){
				UtilSession.adminsitracionServicio.transEliminarCargoescala(new CargoescalaTO(id));
			}

			//Nivelorganico
			else if(clase.equals("nivelorganico")){
				UtilSession.planificacionServicio.transEliminarNivelorganido(new NivelorganicoTO(id));
			}
			//tipodocumentoclasedocumento
			else if(clase.equals("tipodocumentoclasedocumento")){
				UtilSession.adminsitracionServicio.transEliminarTipodocumentoclasedocumento(new TipodocumentoclasedocumentoTO(new TipodocumentoclasedocumentoID(id, id2)));
			}
			//tipoidentificaciontipo
			else if(clase.equals("tipoidentificaciontipo")){
				UtilSession.adminsitracionServicio.transEliminarTipoidentificaciontipo(new TipoidentificaciontipoTO(new TipoidentificaciontipoID(id, id2)));
			}
			//Institucion
			else if(clase.equals("institucion")){
				UtilSession.estructuraorganicaServicio.transEliminarInstitucion(new InstitucionTO(id));
			}
			//Contrato
			else if(clase.equals("contrato")){
				UtilSession.planificacionServicio.transEliminarContrato(new ContratoTO(id));
			}

			//Registro la auditoria
			if(mensajes.getMsg()==null) {
				ComunController.crearAuditoria(request, clase, "D", null, id.toString());
				mensajes.setMsg(MensajesWeb.getString("mensaje.eliminar") + " " + clase);
				mensajes.setType(MensajesWeb.getString("mensaje.exito"));
			}
			//			UtilSession.adminsitracionServicio.transCrearModificarAuditoria(auditoriaTO);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al eliminar");
			if(clase.equals("procedimiento")){
				mensajes.setMsg("Eliminación no válida, hay información en Subitem Unidad");
			}
			else if(clase.equals("tipoproducto")){
				mensajes.setMsg("Eliminación no válida, hay información en Subitem Unidad");
			}
			else if(clase.equals("tiporegimen")){
				mensajes.setMsg("Eliminación no válida, hay información en Subitem Unidad");
			}
			else{
				mensajes.setMsg(MensajesWeb.getString("error.eliminar"));
			}
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
		log.println("ingresa a consultar**: " + clase + " - "  + parametro + " - " + request.getParameter("pagina"));
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
			//Ejerciciofiscal
			if(clase.equals("ejerciciofiscal")){
				jsonObject=ConsultasUtil.consultaEjerciciofiscalPaginado(parameters, jsonObject); 
			}

			//Divisiones geograficas
			else if(clase.equals("divisiongeografica")){
				jsonObject=ConsultasUtil.consultaDivisionesgeograficas(parameters, jsonObject); 
			}

			//Divisiones geograficas paginadas
			else if(clase.equals("divisiongeograficapaginada")){
				jsonObject=ConsultasUtil.consultaDivisionesgeograficasPaginado(parameters, jsonObject); 
			}

			//Unidadmedida
			else if(clase.equals("unidadmedida")){
				jsonObject=ConsultasUtil.consultaUnidadmedidaPaginado(parameters, jsonObject); 
			}

			//Parametro
			else if(clase.equals("parametros")){
				jsonObject=ConsultasUtil.consultaParametrosPaginado(parameters, jsonObject); 
			}

			//Consecutivo
			else if(clase.equals("consecutivo")){
				jsonObject=ConsultasUtil.consultaConsecutivoPaginado(parameters, jsonObject); 
			}

			//Tipoidentificacion
			else if(clase.equals("tipoidentificacion")){
				jsonObject=ConsultasUtil.consultaTipoidentificacionPaginado(parameters, jsonObject); 
			}

			//Tipoidentificaciontipo
			else if(clase.equals("tipoidentificaciontipo")){
				jsonObject=ConsultasUtil.consultaTipoidentificaciontipoPaginado(parameters, jsonObject);
			}


			//Tipodocumento
			else if(clase.equals("tipodocumento")){
				jsonObject=ConsultasUtil.consultaTipodocumentoPaginado(parameters, jsonObject); 
			}

			//Fuentefinanciamiento
			else if(clase.equals("fuentefinanciamiento")){
				jsonObject=ConsultasUtil.consultaFuentefinanciamientoPaginado(parameters, jsonObject); 
			}

			//Organismo
			else if(clase.equals("organismo")){
				jsonObject=ConsultasUtil.consultaOrganismoPaginado(parameters, jsonObject); 
			}

			//Organismoprestamo
			else if(clase.equals("organismoprestamo")){
				jsonObject=ConsultasUtil.consultaOrganismoprestamoPaginado(parameters, jsonObject); 
			}

			//Obra
			else if(clase.equals("obra")){
				jsonObject=ConsultasUtil.consultaObraPaginado(parameters, jsonObject); 
			}

			//Item
			else if(clase.equals("item")){
				jsonObject=ConsultasUtil.consultaItemPaginado(parameters, jsonObject); 
			}

			//Subitem
			else if(clase.equals("subitem")){
				jsonObject=ConsultasUtil.consultaSubitemPaginado(parameters, jsonObject); 
			}

			//Grupomedida
			else if(clase.equals("grupomedida")){
				jsonObject=ConsultasUtil.consultaGrupomedidaPaginado(parameters, jsonObject); 
			}

			//Claseregistro
			else if(clase.equals("claseregistro")){
				jsonObject=ConsultasUtil.consultaClaseregistroPaginado(parameters, jsonObject); 
			}

			//Clasemodificacion
			else if(clase.equals("clasemodificacion")){
				jsonObject=ConsultasUtil.consultaClasemodificacionPaginado(parameters, jsonObject);
			}

			//Socionegocio
			else if(clase.equals("socionegocio")){
				jsonObject=ConsultasUtil.consultaSocionegocioPaginado(parameters, jsonObject,"socionegocio");
			}

			//Empleado
			else if(clase.equals("empleado")){
				jsonObject=ConsultasUtil.consultaSocionegocioPaginado(parameters, jsonObject,"empleado");
			}
			//Empleadoproveedor
			else if(clase.equals("empleadoproveedor")){
				jsonObject=ConsultasUtil.consultaSocionegocioPaginado(parameters, jsonObject,"empleadoproveedor");
			}

			//Proveedorruc
			else if(clase.equals("proveedorruc")){
				jsonObject=ConsultasUtil.consultaSocionegocioPaginado(parameters, jsonObject,"proveedorruc");
			}

			//Busqueda Socionegocio
			else if(clase.equals("busquedasocionegocio")){
				jsonObject=ConsultasUtil.consultaSocionegocioPaginado(parameters, jsonObject,"busquedasocionegocio");
			}

			//Parametroindicador
			else if(clase.equals("parametroindicador")){
				jsonObject=ConsultasUtil.consultaParametroindicadorPaginado(parameters, jsonObject); 
			}

			//Busqueda de fuerzas
			else if(clase.equals("busquedafuerza")){
				jsonObject=ConsultasUtil.consultaFuerzaPaginado(parameters, jsonObject,"busquedafuerza"); 
			}

			//organismoprestamo
			else if(clase.equals("organismoprestamo")){
				jsonObject=ConsultasUtil.consultaOrganismoprestamoPaginado(parameters, jsonObject);
			}

			//Tipoproducto
			else if(clase.equals("tipoproducto")){
				jsonObject=ConsultasUtil.consultaTipoproductoPaginado(parameters, jsonObject);
			}

			//Tiporegimen
			else if(clase.equals("tiporegimen")){
				jsonObject=ConsultasUtil.consultaTiporegimenPaginado(parameters, jsonObject);
			}

			//Procedimiento
			else if(clase.equals("procedimiento")){
				jsonObject=ConsultasUtil.consultaProcedimientoPaginado(parameters, jsonObject);
			}

			//Grupo
			else if(clase.equals("grupo")){ 
				jsonObject=ConsultasUtil.consultaGrupoPaginado(parameters, jsonObject);
			}

			//Grado
			else if(clase.equals("grado")){
				jsonObject=ConsultasUtil.consultaGradoPaginado(parameters, jsonObject);
			}

			//Clasificacion
			else if(clase.equals("clasificacion")){
				jsonObject=ConsultasUtil.consultaClasificacionPaginado(parameters, jsonObject);
			}

			//Fuerza
			else if(clase.equals("fuerza")){
				jsonObject=ConsultasUtil.consultaFuerzaPaginado(parameters, jsonObject);
			}

			//Gradofuerza
			else if(clase.equals("gradofuerza")){
				jsonObject=ConsultasUtil.consultaGradofuerzaPaginado(parameters, jsonObject);
			}

			//especialidades
			else if(clase.equals("especialidades")){
				jsonObject=ConsultasUtil.consultaEspecialidadPaginado(parameters, jsonObject);
			}

			//Cargo
			else if(clase.equals("cargo")){
				jsonObject=ConsultasUtil.consultaCargoPaginado(parameters, jsonObject);
			}

			//Escalarmu
			else if(clase.equals("escalarmu")){
				jsonObject=ConsultasUtil.consultaEscalarmuPaginado(parameters, jsonObject);
			}
			
			//Fuerzaclasificacion
			else if(clase.equals("fuerzaclasificacion")){
				jsonObject=ConsultasUtil.consultaFuerzaclasificacionPaginado(parameters, jsonObject);
			}

			//Gradoescala
			else if(clase.equals("gradoescala")){
				jsonObject=ConsultasUtil.consultaGradoescalaPaginado(parameters, jsonObject);
			}

			//Cargoescala
			else if(clase.equals("cargoescala")){
				jsonObject=ConsultasUtil.consultaCargoescalaPaginado(parameters, jsonObject);
			}	

			//Nivelorganico
			else if(clase.equals("nivelorganico")){
				jsonObject=ConsultasUtil.consultaNivelorganicoPaginado(parameters, jsonObject);
			}	

			//Institucion
			else if(clase.equals("institucion")){
				log.println("va a consultar institucion");
				jsonObject=ConsultasUtil.consultaIntitucionPaginado(parameters, jsonObject);
			}
			
			//Clase de Gasto
			else if(clase.equals("claseregistrocmcgasto")){
				jsonObject=ConsultasUtil.consultaClasegasto(parameters, jsonObject);
			}
			
			//Tipo clase documento
			else if(clase.equals("tipodocumentoclasedocumento")){
				jsonObject=ConsultasUtil.consultaTipoclasedocumento(parameters, jsonObject);
			}
			
			//Tipo clase documento
			else if(clase.equals("contrato")){
				jsonObject=ConsultasUtil.consultaContrato(parameters, jsonObject);
			}

			log.println("json retornado de consulta: " + jsonObject.toString()); 
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
	//						UtilSession.adminsitracionServicio.transCrearModificarUsuario(usuario);
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
