package ec.com.papp.web.administracion.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ec.com.papp.administracion.dao.CargoDAO;
import ec.com.papp.administracion.to.CargoTO;
import ec.com.papp.administracion.to.ClaseregistroTO;
import ec.com.papp.administracion.to.ClaseregistroclasemodificacionTO;
import ec.com.papp.administracion.to.DivisiongeograficaTO;
import ec.com.papp.administracion.to.EjerciciofiscalTO;
import ec.com.papp.administracion.to.EscalarmuTO;
import ec.com.papp.administracion.to.FuerzaTO;
import ec.com.papp.administracion.to.GradoTO;
import ec.com.papp.administracion.to.GrupomedidaTO;
import ec.com.papp.administracion.to.ItemTO;
import ec.com.papp.administracion.to.ProcedimientoTO;
import ec.com.papp.administracion.to.SubitemTO;
import ec.com.papp.administracion.to.TipodocumentoTO;
import ec.com.papp.administracion.to.TipodocumentoclasedocumentoTO;
import ec.com.papp.administracion.to.TipoidentificacionTO;
import ec.com.papp.administracion.to.TipoidentificaciontipoTO;
import ec.com.papp.administracion.to.TipoproductoTO;
import ec.com.papp.administracion.to.TiporegimenTO;
import ec.com.papp.administracion.to.UnidadmedidaTO;
import ec.com.papp.estructuraorganica.to.InstitucionTO;
import ec.com.papp.estructuraorganica.to.InstitucionentidadTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.ObjetivoTO;
import ec.com.papp.planificacion.to.SubitemunidadTO;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.xcelsa.utilitario.metodos.Log;

@RestController
@RequestMapping("/rest/comun")
public class ComunController {

	private Log log = new Log(ComunController.class);
	
	//Obtiene los datos para combos de seleccion
	@RequestMapping(value = "/{parametros}", method = RequestMethod.GET)
	public String consultar(@PathVariable String parametros,HttpServletRequest request,HttpServletResponse response) throws IOException {
		log.println("ingresa a consultar: " + parametros);
		Mensajes mensajes=new Mensajes();
		JSONObject jsonObject=new JSONObject();
		try{
			//Itero los parametros para saber cuantas consultas debo devover
			String [] consultas=parametros.split(",");
			for(String consulta: consultas){
				String [] parametro = consulta.split("@");
				log.println("consulta: " + consulta);
				//Ejerciciofiscal
				if(parametro[0].equals("ejerciciofiscal")){
					EjerciciofiscalTO ejerciciofiscalTO=new EjerciciofiscalTO();
					ejerciciofiscalTO.setEstado(MensajesWeb.getString("estado.activo"));
					Collection<EjerciciofiscalTO> ejerciciofiscalTOs=UtilSession.adminsitracionServicio.transObtenerEjerciciofiscal(ejerciciofiscalTO);
					jsonObject.put("ejerciciofiscal", (JSONArray)JSONSerializer.toJSON(ejerciciofiscalTOs,ejerciciofiscalTO.getJsonConfigComun()));
				}
				//Grupo medida
				else if(parametro[0].equals("grupomedida")){
					GrupomedidaTO grupomedidaTO=new GrupomedidaTO();
					grupomedidaTO.setEstado(MensajesWeb.getString("estado.activo"));
					Collection<GrupomedidaTO> grupomedidaTOs=UtilSession.adminsitracionServicio.transObtenerGrupomedida(grupomedidaTO);
					jsonObject.put("grupomedida", (JSONArray)JSONSerializer.toJSON(grupomedidaTOs,grupomedidaTO.getJsonConfig()));
				}
//				//Divisiongeografica
//				else if(parametro[0].equals("divisiongeografica")){
//					DivisiongeograficaTO divisiongeograficaTO=new DivisiongeograficaTO();
//					Collection<DivisiongeograficaTO> divisiongeograficaTOs=new ArrayList<DivisiongeograficaTO>();
//					if(parametro.length==2)
//						divisiongeograficaTOs=UtilSession.adminsitracionServicio.transObtenerDivisiongeografica(Long.valueOf(parametro[1]),null);
//					else if(parametro.length==3)
//						divisiongeograficaTOs=UtilSession.adminsitracionServicio.transObtenerDivisiongeografica(Long.valueOf(parametro[1]),parametro[2]);
//					jsonObject.put("divisiongeografica", (JSONArray)JSONSerializer.toJSON(divisiongeograficaTOs,divisiongeograficaTO.getJsonConfigComun()));
//				}
				//Item
				else if(parametro[0].equals("item")){
					ItemTO itemTO=new ItemTO();
					Collection<ItemTO> itemTOs=new ArrayList<ItemTO>();
					if(parametro.length==3){
						itemTO.setItemejerciciofiscalid(Long.valueOf(parametro[1]));
						itemTO.setId(Long.valueOf(parametro[2]));
						itemTOs=UtilSession.adminsitracionServicio.transObtieneItemarbol(itemTO);
					}
					else if(parametro.length==4){
						itemTO.setItemejerciciofiscalid(Long.valueOf(parametro[1]));
						itemTO.setId(Long.valueOf(parametro[2]));
						itemTO.setEstado(parametro[3]);
						itemTOs=UtilSession.adminsitracionServicio.transObtieneItemarbol(itemTO);
					}
					jsonObject.put("item", (JSONArray)JSONSerializer.toJSON(itemTOs,itemTO.getJsonConfigcomun()));
				}
				//Unidadmedida
				if(parametro[0].equals("unidadmedida")){
					UnidadmedidaTO unidadmedidaTO=new UnidadmedidaTO();
					unidadmedidaTO.setGrupomedida(new GrupomedidaTO());
					unidadmedidaTO.setEstado(MensajesWeb.getString("estado.activo"));
					Collection<UnidadmedidaTO> unidadmedidaTOs=UtilSession.adminsitracionServicio.transObtenerUnidadmedida(unidadmedidaTO);
					jsonObject.put("unidadmedida", (JSONArray)JSONSerializer.toJSON(unidadmedidaTOs,unidadmedidaTO.getJsonConfigComun()));
				}
				//Institucion
				if(parametro[0].equals("institucion")){
					InstitucionTO institucionTO=new InstitucionTO();
					institucionTO.setEstado(MensajesWeb.getString("estado.activo"));
					if(parametro.length==2)
						institucionTO.setInstitucionejerciciofiscalid(Long.valueOf(parametro[1]));
					Collection<InstitucionTO> institucionTOs=UtilSession.estructuraorganicaServicio.transObtenerInstitucion(institucionTO);
					jsonObject.put("institucion", (JSONArray)JSONSerializer.toJSON(institucionTOs,institucionTO.getJsonConfigComun()));
				}
				//Institucionentidad
				else if(parametro[0].equals("institucionentidad")){
					InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
					if(parametro.length==2){
						institucionentidadTO.getId().setId(Long.valueOf(parametro[1]));
					}
					institucionentidadTO.setInstitucion(new InstitucionTO());
					Collection<InstitucionentidadTO> institucionentidadTOs=UtilSession.estructuraorganicaServicio.transObtenerInstitucionentidad(institucionentidadTO);
					jsonObject.put("institucionentidad", (JSONArray)JSONSerializer.toJSON(institucionentidadTOs,institucionentidadTO.getJsonConfigComun()));
				}
				//Objetivo
				else if(parametro[0].equals("objetivo")){
					log.println("entro a consultar objetivo");
					ObjetivoTO objetivoTO=new ObjetivoTO();
					Collection<ObjetivoTO> objetivoTOs=new ArrayList<ObjetivoTO>();
					if(parametro.length==2){
						log.println("va a consultar por ejercicio fiscal");
						objetivoTO.setObjetivoejerciciofiscalid(Long.valueOf(parametro[1]));
						objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO);
					}
					else if(parametro.length==3){
						objetivoTO.setObjetivoejerciciofiscalid(Long.valueOf(parametro[1]));
						objetivoTO.setId(Long.valueOf(parametro[2]));
						objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO);
					}
					else if(parametro.length==4){
						objetivoTO.setObjetivoejerciciofiscalid(Long.valueOf(parametro[1]));
						objetivoTO.setId(Long.valueOf(parametro[2]));
						objetivoTO.setEstado(parametro[3]);
						objetivoTOs=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO);
					}
					jsonObject.put("objetivo", (JSONArray)JSONSerializer.toJSON(objetivoTOs,objetivoTO.getJsonConfigArbol()));
				}
				//Tiporegimen
				if(parametro[0].equals("tiporegimen")){
					TiporegimenTO tiporegimenTO=new TiporegimenTO();
					Collection<TiporegimenTO> tiporegimenTOs=UtilSession.adminsitracionServicio.transObtenerTiporegimen(tiporegimenTO);
					jsonObject.put("tiporegimen", (JSONArray)JSONSerializer.toJSON(tiporegimenTOs,tiporegimenTO.getJsonConfig()));
				}
				//Tipoproducto
				if(parametro[0].equals("tipoproducto")){
					TipoproductoTO tipoproductoTO=new TipoproductoTO();
					Collection<TipoproductoTO> tipoproductoTOs=UtilSession.adminsitracionServicio.transObtenerTipoproducto(tipoproductoTO);
					jsonObject.put("tipoproducto", (JSONArray)JSONSerializer.toJSON(tipoproductoTOs,tipoproductoTO.getJsonConfig()));
				}
				//Grado
				if(parametro[0].equals("grado")){
					GradoTO gradoTO=new GradoTO();
					Collection<GradoTO> gradoTOs=UtilSession.adminsitracionServicio.transObtenerGrado(gradoTO);
					jsonObject.put("grado", (JSONArray)JSONSerializer.toJSON(gradoTOs,gradoTO.getJsonConfig()));
				}
				//Fuerza
				if(parametro[0].equals("fuerza")){
					FuerzaTO fuerzaTO=new FuerzaTO();
					Collection<FuerzaTO> fuerzaTOs=UtilSession.adminsitracionServicio.transObtenerFuerza(fuerzaTO);
					jsonObject.put("fuerza", (JSONArray)JSONSerializer.toJSON(fuerzaTOs,fuerzaTO.getJsonConfig()));
				}
				//Escalarmu
				if(parametro[0].equals("escalarmu")){
					EscalarmuTO escalarmuTO=new EscalarmuTO();
					Collection<EscalarmuTO> escalarmuTOs=UtilSession.adminsitracionServicio.transObtenerEscalarmu(escalarmuTO);
					jsonObject.put("escalarmu", (JSONArray)JSONSerializer.toJSON(escalarmuTOs,escalarmuTO.getJsonConfig()));
				}
				//Cargo
				if(parametro[0].equals("cargo")){
					CargoTO cargoTO=new CargoTO();
					Collection<CargoTO> cargoTOs=UtilSession.adminsitracionServicio.transObtenerCargo(cargoTO);
					jsonObject.put("cargo", (JSONArray)JSONSerializer.toJSON(cargoTOs,cargoTO.getJsonConfig()));
				}
				//Procedimiento
				if(parametro[0].equals("procedimiento")){
					ProcedimientoTO procedimientoTO=new ProcedimientoTO();
					Collection<ProcedimientoTO> procedimientoTOs=UtilSession.adminsitracionServicio.transObtenerProcedimiento(procedimientoTO);
					jsonObject.put("procedimiento", (JSONArray)JSONSerializer.toJSON(procedimientoTOs,procedimientoTO.getJsonConfig()));
				}
				//Subitenunidad
				else if(parametro[0].equals("subitemunidad")){
					SubitemunidadTO subitemunidadTO=new SubitemunidadTO();
					subitemunidadTO.setSubitem(new SubitemTO());
					if(parametro.length==2){
						subitemunidadTO.setSubitemunidadunidadid(Long.valueOf(parametro[1]));
					}
					Collection<SubitemunidadTO> subitemunidadTOs=UtilSession.planificacionServicio.transObtenerSubitemunidad(subitemunidadTO);
					jsonObject.put("subitemunidad", (JSONArray)JSONSerializer.toJSON(subitemunidadTOs,subitemunidadTO.getJsonConfigSubitem()));
				}
				//claseregistrocmcgasto
				else if(parametro[0].equals("claseregistrocmcgasto")){
					ClaseregistrocmcgastoTO claseregistrocmcgastoTO=new ClaseregistrocmcgastoTO();
					ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO=new ClaseregistroclasemodificacionTO();
					claseregistroclasemodificacionTO.setClaseregistro(new ClaseregistroTO());
					claseregistrocmcgastoTO.setClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
					if(parametro.length==2){
						claseregistroclasemodificacionTO.setClaseregistrocmejerfiscalid(Long.valueOf(parametro[1]));
					}
					Collection<ClaseregistrocmcgastoTO> claseregistrocmcgastoTOs=UtilSession.planificacionServicio.transObtenerClaseregistrocmcgasto(claseregistrocmcgastoTO);
					jsonObject.put("claseregistrocmcgasto", (JSONArray)JSONSerializer.toJSON(claseregistrocmcgastoTOs,claseregistrocmcgastoTO.getJsonConfigListaClaseg()));
				}
				//tipoidentificaciontipo
				else if(parametro[0].equals("tipoidentificaciontipo")){
					TipoidentificaciontipoTO tipoidentificaciontipoTO=new TipoidentificaciontipoTO();
					tipoidentificaciontipoTO.setTipoidentificacion(new TipoidentificacionTO());
					if(parametro.length==3){
						tipoidentificaciontipoTO.getId().setIdentificacionid(Long.valueOf(parametro[2]));
					}
					Collection<TipoidentificaciontipoTO> tipodocumentoclasedocumentoTOs=UtilSession.adminsitracionServicio.transObtenerTipoidentificaciontipo(tipoidentificaciontipoTO);
					jsonObject.put("tipodocumentoclasedocumento", (JSONArray)JSONSerializer.toJSON(tipodocumentoclasedocumentoTOs,tipoidentificaciontipoTO.getJsonConfig()));
				}
				//tipodocumentoclasedocumento
				else if(parametro[0].equals("tipodocumentoclasedocumento")){
					TipodocumentoclasedocumentoTO tipodocumentoclasedocumentoTO=new TipodocumentoclasedocumentoTO();
					tipodocumentoclasedocumentoTO.setTipodocumento(new TipodocumentoTO());
					if(parametro.length==2){
						tipodocumentoclasedocumentoTO.setTipodocumentoclasedocefid(Long.valueOf(parametro[1]));
					}
					if(parametro.length==3){
						tipodocumentoclasedocumentoTO.setTipodocumentoclasedocefid(Long.valueOf(parametro[1]));
						tipodocumentoclasedocumentoTO.getId().setId(Long.valueOf(parametro[2]));
					}
					Collection<TipodocumentoclasedocumentoTO> tipodocumentoclasedocumentoTOs=UtilSession.adminsitracionServicio.transObtenerTipodocumentoclasedocumento(tipodocumentoclasedocumentoTO);
					jsonObject.put("tipodocumentoclasedocumento", (JSONArray)JSONSerializer.toJSON(tipodocumentoclasedocumentoTOs,tipodocumentoclasedocumentoTO.getJsonConfigLista()));
				}
			}
			log.println("json retornado: " + jsonObject.toString());
		   // return jsonObject.toString();
		}catch (Exception e) {
			e.printStackTrace();
			mensajes.setMsg(MensajesWeb.getString("error.obtener"));
			mensajes.setType(MensajesWeb.getString("mensaje.error"));
			//throw new MyException(e);
		}
		if(mensajes.getMsg()!=null)
			jsonObject.put("mensajes", (JSONObject)JSONSerializer.toJSON(mensajes));
		//return jsonObject.toString();	
//		request.setCharacterEncoding("UTF-8");
//		response.setContentType("text/json;charset=UTF-8");
//		PrintWriter out = response.getWriter();
//		out.println(jsonObject);
//		out.flush();
//		out.close();
//		return new ModelAndView("index");
		return jsonObject.toString();
	}
}
