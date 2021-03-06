package ec.com.papp.web.ejecucion.util;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.tools.commons.to.OrderBy;
import org.hibernate.tools.commons.to.RangeValueTO;
import org.hibernate.tools.commons.to.SearchResultTO;

import ec.com.papp.administracion.to.ClaseregistroTO;
import ec.com.papp.administracion.to.ClaseregistroclasemodificacionTO;
import ec.com.papp.administracion.to.TipodocumentoTO;
import ec.com.papp.administracion.to.TipodocumentoclasedocumentoTO;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.planificacion.to.CertificacionOrdenVO;
import ec.com.papp.planificacion.to.CertificacionTO;
import ec.com.papp.planificacion.to.CertificacionlineaTO;
import ec.com.papp.planificacion.to.ClaseregistrocmcgastoTO;
import ec.com.papp.planificacion.to.GastoDevengoVO;
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
import ec.com.papp.planificacion.to.SubitemunidadTO;
import ec.com.papp.planificacion.to.SubitemunidadacumuladorTO;
import ec.com.papp.planificacion.to.SubtareaunidadacumuladorTO;
import ec.com.papp.planificacion.util.Ejecucioncabeceraact;
import ec.com.papp.planificacion.util.Ejecucioncabecerasubtarea;
import ec.com.papp.planificacion.util.MatrizDetalle;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.Log;
import ec.com.xcelsa.utilitario.metodos.UtilGeneral;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ConsultasUtil {

	
	private static Log log = new Log(ConsultasUtil.class);
	
	/**
	* Metodo que consulta las certificaciones paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaCertificacionPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		CertificacionTO certificacionTO=new CertificacionTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			certificacionTO.setFirstResult(primero);
			certificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				certificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				certificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial").replaceAll("%2F", "/"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			
			Double valorInicial=null;
			Double valorFinal=null;
			if(parameters.get("valorinicial")!=null)
				valorInicial=Double.valueOf(parameters.get("valorinicial"));
			if(parameters.get("valorfinal")!=null)
				valorFinal=Double.valueOf(parameters.get("valorfinal"));
			if(parameters.get("valorinicial")!=null && parameters.get("valorfinal")==null)
				valorFinal=(1000000.0);
			if(parameters.get("valorfinal")!=null && parameters.get("valorinicial")==null){
				valorInicial=(0.0);
			}
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null)
					certificacionTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(valorInicial!=null && valorFinal!=null && (valorInicial>0 || valorFinal>0))
					certificacionTO.setRangoValortotal(new RangeValueTO<Double>(valorInicial,valorFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					certificacionTO.setCodigo(parameters.get("codigo").replaceAll("$", "%"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					certificacionTO.setEstado(parameters.get("estado"));
				if(parameters.get("numprecompromiso")!=null && !parameters.get("numprecompromiso").equals(""))
					certificacionTO.setNumprecompromiso(parameters.get("numprecompromiso").toUpperCase());
				if(parameters.get("certificacionejerfiscalid")!=null && !parameters.get("certificacionejerfiscalid").equals(""))
					certificacionTO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("certificacionejerfiscalid")));
				log.println("certificacion: " + certificacionTO.getCertificacionejerfiscalid());
				SearchResultTO<CertificacionTO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionPaginado(certificacionTO, principal.getName());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + totalRegistrosPagina);
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),certificacionTO.getJsonConfigconsulta()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las la lista de subitems para cargarlas en combo
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar el combo
	* @throws MyException
	*/

	public static JSONObject consultaListasubitemunidad(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
		MatrizDetalle matrizDetalle=new MatrizDetalle();
		try{
			Long unidad=Long.valueOf(parameters.get("unidad"));
			Long ejerciciofiscal=Long.valueOf(parameters.get("ejerciciofiscal"));
			String tiponivel=parameters.get("tiponivel");
			Collection<MatrizDetalle> resultado=UtilSession.planificacionServicio.transObtienesubitemunidadporunidad(unidad, ejerciciofiscal, tiponivel);
			jsonObject.put("subitem", (JSONArray)JSONSerializer.toJSON(resultado,matrizDetalle.getJsonConfigsubitem()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta el arbol de informacion del subitem
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar el arbol
	* @throws MyException
	*/

	public static JSONObject consultaInformacionsubitemunidad(Long nivelactividad,JSONObject jsonObject,Mensajes mensajes) throws MyException {
		MatrizDetalle matrizDetalle=new MatrizDetalle();
		try{
			MatrizDetalle resultado=UtilSession.planificacionServicio.transObtienedetallesubitem(nivelactividad);
			jsonObject.put("subiteminfo", (JSONObject)JSONSerializer.toJSON(resultado,matrizDetalle.getJsonConfigsubitemarbol()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta el arbol de informacion del subtarea
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar el arbol
	* @throws MyException
	*/

	public static JSONObject consultaInformacionsubtarea(Long nivelactividad,JSONObject jsonObject,Mensajes mensajes) throws MyException {
		MatrizDetalle matrizDetalle=new MatrizDetalle();
		try{
			MatrizDetalle resultado=UtilSession.planificacionServicio.transObtienedetallesubtarea(nivelactividad);
			jsonObject.put("subtareainfo", (JSONObject)JSONSerializer.toJSON(resultado,matrizDetalle.getJsonConfigsubitemarbol()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta las orden de gasto paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrdengastoPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		OrdengastoTO ordengastoTO=new OrdengastoTO();
		//CertificacionTO certificacionTO=new CertificacionTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			ordengastoTO.setFirstResult(primero);
			ordengastoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				ordengastoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				ordengastoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial").replaceAll("%2F", "/"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			Double valorInicial=null;
			Double valorFinal=null;
			if(parameters.get("valorinicial")!=null)
				valorInicial=Double.valueOf(parameters.get("valorinicial"));
			if(parameters.get("valorfinal")!=null)
				valorFinal=Double.valueOf(parameters.get("valorfinal"));
			if(parameters.get("valorinicial")!=null && parameters.get("valorfinal")==null)
				valorFinal=(1000000.0);
			if(parameters.get("valorfinal")!=null && parameters.get("valorinicial")==null){
				valorInicial=(0.0);
			}
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null)
					ordengastoTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(valorInicial!=null && valorFinal!=null && (valorInicial>0 || valorFinal>0))
					ordengastoTO.setRangoValortotal(new RangeValueTO<Double>(valorInicial,valorFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					ordengastoTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					ordengastoTO.setEstado(parameters.get("estado"));
				if(parameters.get("compromiso")!=null && !parameters.get("compromiso").equals(""))
					ordengastoTO.setNumerocompromiso(parameters.get("compromiso").toUpperCase());
				if(parameters.get("ordengastoejerfiscalid")!=null && !parameters.get("ordengastoejerfiscalid").equals(""))
					ordengastoTO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ordengastoejerfiscalid")));
				if(parameters.get("certificacion")!=null && !parameters.get("certificacion").equals("")){
					ordengastoTO.setNpcertificacion(parameters.get("certificacion"));
				}
				//ordengastoTO.setCertificacion(certificacionTO);
				ClaseregistrocmcgastoTO claseregistrocmcgastoTO=new ClaseregistrocmcgastoTO();
				ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO=new ClaseregistroclasemodificacionTO();
				ClaseregistroTO claseregistroTO=new ClaseregistroTO();
				claseregistroclasemodificacionTO.setClaseregistro(claseregistroTO);
				claseregistrocmcgastoTO.setClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
				ordengastoTO.setClaseregistrocmcgasto(claseregistrocmcgastoTO);
				
				TipodocumentoclasedocumentoTO tipodocumentoclasedocumentoTO=new TipodocumentoclasedocumentoTO();
				TipodocumentoTO tipodocumentoTO=new TipodocumentoTO();
				tipodocumentoclasedocumentoTO.setTipodocumento(tipodocumentoTO);
				ordengastoTO.setTipodocumentoclasedocumento(tipodocumentoclasedocumentoTO);
				SearchResultTO<OrdengastoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastoPaginado(ordengastoTO, principal.getName());
				log.println("resultado** " + resultado.getCountResults());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + totalRegistrosPagina);
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),ordengastoTO.getJsonConfigconsulta()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las orden de devengo paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrdendevengoPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		OrdendevengoTO ordendevengoTO=new OrdendevengoTO();
		OrdengastoTO ordengastoTO=new OrdengastoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			ordendevengoTO.setFirstResult(primero);
			ordendevengoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				ordendevengoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				ordendevengoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial").replaceAll("%2F", "/"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			Double valorInicial=null;
			Double valorFinal=null;
			if(parameters.get("valorinicial")!=null)
				valorInicial=Double.valueOf(parameters.get("valorinicial"));
			if(parameters.get("valorfinal")!=null)
				valorFinal=Double.valueOf(parameters.get("valorfinal"));
			if(parameters.get("valorinicial")!=null && parameters.get("valorfinal")==null)
				valorFinal=(1000000.0);
			if(parameters.get("valorfinal")!=null && parameters.get("valorinicial")==null){
				valorInicial=(0.0);
			}
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null)
					ordendevengoTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(valorInicial!=null && valorFinal!=null && (valorInicial>0 || valorFinal>0))
					ordendevengoTO.setRangoValortotal(new RangeValueTO<Double>(valorInicial,valorFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					ordendevengoTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					ordendevengoTO.setEstado(parameters.get("estado"));
				if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
					ordendevengoTO.setOrdendevengoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				if(parameters.get("numprecompromiso")!=null && !parameters.get("numprecompromiso").equals("")){
					ordengastoTO.setCodigo(parameters.get("numprecompromiso").toUpperCase());
				}
				ordendevengoTO.setOrdengasto(ordengastoTO);
				SearchResultTO<OrdendevengoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdendevengoPaginado(ordendevengoTO, principal.getName());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + totalRegistrosPagina);
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),ordendevengoTO.getJsonConfigconsulta()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las orden de reversion paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrdenreversionPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		OrdenreversionTO ordenreversionTO=new OrdenreversionTO();
		OrdengastoTO ordengastoTO=new OrdengastoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			ordenreversionTO.setFirstResult(primero);
			ordenreversionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				ordenreversionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				ordenreversionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial").replaceAll("%2F", "/"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			Double valorInicial=null;
			Double valorFinal=null;
			if(parameters.get("valorinicial")!=null)
				valorInicial=Double.valueOf(parameters.get("valorinicial"));
			if(parameters.get("valorfinal")!=null)
				valorFinal=Double.valueOf(parameters.get("valorfinal"));
			if(parameters.get("valorinicial")!=null && parameters.get("valorfinal")==null)
				valorFinal=(1000000.0);
			if(parameters.get("valorfinal")!=null && parameters.get("valorinicial")==null){
				valorInicial=(0.0);
			}
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null)
					ordenreversionTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(valorInicial!=null && valorFinal!=null && (valorInicial>0 || valorFinal>0))
					ordenreversionTO.setRangoValortotal(new RangeValueTO<Double>(valorInicial,valorFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					ordenreversionTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					ordenreversionTO.setEstado(parameters.get("estado"));
				if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
					ordenreversionTO.setOrdenreversionejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				if(parameters.get("ordengasto")!=null && !parameters.get("ordengasto").equals("")){
					ordengastoTO.setCodigo(parameters.get("ordengasto").toUpperCase());
				}
				ordenreversionTO.setOrdengasto(ordengastoTO);
				SearchResultTO<OrdenreversionTO> resultado=UtilSession.planificacionServicio.transObtenerOrdenreversionPaginado(ordenreversionTO, principal.getName());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + resultado.getCountResults());
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),ordenreversionTO.getJsonConfigconsulta()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta las certificaciones busqueda paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaCertificacionBusquedaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes,Principal principal) throws MyException {
		String campo="";
		CertificacionTO certificacionTO=new CertificacionTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			certificacionTO.setFirstResult(primero);
			certificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				certificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				certificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				certificacionTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				certificacionTO.setEstado(parameters.get("estado"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				certificacionTO.setDescripcion(parameters.get("descripcion").toUpperCase());
			if(parameters.get("unidadid")!=null && !parameters.get("unidadid").equals(""))
				certificacionTO.setCertificacionunidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				certificacionTO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			SearchResultTO<CertificacionTO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionPaginado(certificacionTO,principal.getName());
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),certificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las Ordenesgasto busqueda paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrdengastoBusquedaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes,Principal principal) throws MyException {
		String campo="";
		OrdengastoTO ordengastoTO=new OrdengastoTO();
		UnidadTO unidadTO=new UnidadTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			ordengastoTO.setFirstResult(primero);
			ordengastoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				ordengastoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				ordengastoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				ordengastoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				ordengastoTO.setEstado(parameters.get("estado"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				ordengastoTO.setDescripcion(parameters.get("descripcion").toUpperCase());
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				ordengastoTO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			if(parameters.get("unidad")!=null && !parameters.get("unidad").equals("")){
				unidadTO.setNombre(parameters.get("unidad"));
			}
			ordengastoTO.setUnidad(unidadTO);
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				ordengastoTO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			SearchResultTO<OrdengastoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastoPaginado(ordengastoTO,principal.getName());
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),ordengastoTO.getJsonConfigconsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta certificacionbusqueda y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject certificacionbusqueda(Map<String, String> parameters,JSONObject jsonObject, Principal principal) throws MyException {
		CertificacionOrdenVO certificacionOrdenVO=new CertificacionOrdenVO();
		try{
			if(parameters.get("ejerciciofiscal")!=null && !parameters.get("ejerciciofiscal").equals(""))
				certificacionOrdenVO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("unidadid")!=null && !parameters.get("unidadid").equals(""))
				certificacionOrdenVO.setCertificacionunidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				certificacionOrdenVO.setEstado(parameters.get("estado"));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				certificacionOrdenVO.setCodigo(parameters.get("codigo"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				certificacionOrdenVO.setDescripcion(parameters.get("descripcion").toUpperCase());
			Collection<CertificacionOrdenVO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionOrden(certificacionOrdenVO, principal.getName());
			
			System.out.println("certificaciones: " + resultado.size());
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,certificacionOrdenVO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	public static JSONObject ordenesgastobusqueda(Map<String, String> parameters,JSONObject jsonObject, Principal principal) throws MyException {
		GastoDevengoVO gastoDevengoVO=new GastoDevengoVO();
		try{
			if(parameters.get("ejerciciofiscal")!=null && !parameters.get("ejerciciofiscal").equals(""))
				gastoDevengoVO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("unidad")!=null && !parameters.get("unidad").equals(""))
				gastoDevengoVO.setOrdengastounidadid(Long.valueOf(parameters.get("unidad")));
			if(parameters.get("unidadnombre")!=null && !parameters.get("unidadnombre").equals(""))
				gastoDevengoVO.setUnidadnombre(parameters.get("unidadnombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				gastoDevengoVO.setEstado(parameters.get("estado"));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				gastoDevengoVO.setCodigo(parameters.get("codigo"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				gastoDevengoVO.setDescripcion(parameters.get("descripcion").toUpperCase());

			Collection<GastoDevengoVO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastodevengo(gastoDevengoVO, principal.getName());
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,gastoDevengoVO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	
	//	/**
//	* Metodo que consulta las registro paginadas y arma el json para mostrarlos en la grilla
//	*
//	* @param request 
//	* @return JSONObject Estructura que contiene los valores para armar la grilla
//	* @throws MyException
//	*/
//
//	public static JSONObject consultaRegistroPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
//		String campo="";
//		Registo
//		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
//			campo="anio";
//			String[] columnas={campo};
//			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
//				campo=parameters.get("sidx");
//			certificacionTO.setFirstResult(primero);
//			certificacionTO.setMaxResults(filas);
//			String[] orderBy = columnas;
//			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
//				certificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
//			else
//				certificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
//			Date fechaInicial=null;
//			Date fechaFinal=null;
//			if(parameters.get("fechainicial")!=null)
//				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial"));
//			if(parameters.get("fechafinal")!=null)
//				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal"));
//			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
//				fechaFinal=(new Date());
//			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
//				mensajes.setMsg(MensajesWeb.getString("error.fechaDesde"));
//				mensajes.setType(MensajesWeb.getString("mensaje.advertencia"));
//			}
//			if(mensajes.getMsg()==null){
//				if(fechaInicial!=null)
//					certificacionTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
//				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
//					certificacionTO.setCodigo(parameters.get("codigo"));
//				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
//					certificacionTO.setEstado(parameters.get("estado"));
//				SearchResultTO<CertificacionTO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionPaginado(certificacionTO);
//				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//				HashMap<String, String>  totalMap=new HashMap<String, String>();
//				totalMap.put("valor", resultado.getCountResults().toString());
//				log.println("totalresultado: " + totalRegistrosPagina);
//				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),certificacionTO.getJsonConfigconsulta()));
//				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new MyException(e);
//		}
//		return jsonObject;
//	}
//
	
	/**
	* Metodo que obtiene el total disponible de un subitem
	*
	* @param request 
	* @return total
	* @throws MyException
	*/

	public static Double obtenertotalsubitem(Long idsubitem, boolean reforma) throws MyException {
		double total=0.0;
		try{
			//traigo los datos de subitemunidadacumulador
			SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
			subitemunidadacumuladorTO.getId().setId(idsubitem);
			//subitemunidadacumuladorTO.setTipo("A");
			subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
			Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
			boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
			for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs) {
				if(subitemunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
					ajustado=true;
					total=total+subitemunidadacumuladorTO2.getTotal().doubleValue();
				}
				if(reforma){
					//si existe reformas tomo la ultima porque ahi esta el valor codificado
					if(subitemunidadacumuladorTO2.getTipo().equals("R"))
						total=subitemunidadacumuladorTO2.getTotal().doubleValue();
				}
			}
			return total;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	/**
	* Metodo que obtiene el saldo disponible de un subitem
	*
	* @param request 
	* @return total
	* @throws MyException
	*/

	public static Double obtenersaldodisponibleactual(Long idsubitem,Long nivelactividadid) throws MyException {
		try{
			SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
			subitemunidadacumuladorTO.getId().setId(idsubitem);
			//subitemunidadacumuladorTO.setTipo("A");
			subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
			Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
			boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
			double totalajustado=0.0;
			for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs) {
				if(subitemunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
					ajustado=true;
					totalajustado=totalajustado+subitemunidadacumuladorTO2.getTotal().doubleValue();
				}
			}
			SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(idsubitem);
			//traigo las reformas asignadas al subitem
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			System.out.println("reformalineatos: " + reformalineaTOs.size());
			double totalreforma=0.0;
			for(ReformalineaTO reformalineaTO:reformalineaTOs){
				System.out.println("fecha a comparar: " + reformalineaTO.getReforma().getFechacreacion());
				//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
				if(reformalineaTO.getReforma().getEstado().equals("AP") ||reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")){
					totalreforma=totalreforma-reformalineaTO.getValordecremento().doubleValue();
				}
				if(reformalineaTO.getReforma().getEstado().equals("AP")){
					totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue();
				}

			}
			double codificado=totalajustado+totalreforma;
			System.out.println("totalajustad: " + totalajustado + "totalreforma: " + totalreforma);
			double saldo=codificado-subitemunidadTO.getValxcomprometer().doubleValue()-subitemunidadTO.getValprecompromiso()-subitemunidadTO.getValcompromiso().doubleValue();
			System.out.println("valores: " +codificado+"-"+subitemunidadTO.getValprecompromiso()+"-"+subitemunidadTO.getValxcomprometer().doubleValue()+"-"+subitemunidadTO.getValcompromiso());
			//double saldo=total+totalreforma-subitemunidadTO.getValprecompromiso().doubleValue()-subitemunidadTO.getValxcomprometer().doubleValue()-subitemunidadTO.getValcompromiso().doubleValue();
			return saldo;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static Double obtenersaldodisponiblelineacertificacion(Long idsubitem,Long nivelactividadid,Date fecha,CertificacionlineaTO certificacionlineaTO) throws MyException {
		try{
			SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
			subitemunidadacumuladorTO.getId().setId(idsubitem);
			//subitemunidadacumuladorTO.setTipo("A");
			subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
			Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
			boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
			double totalajustado=0.0;
			for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs) {
				if(subitemunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
					ajustado=true;
					totalajustado=totalajustado+subitemunidadacumuladorTO2.getTotal().doubleValue();
				}
			}
			//traigo las reformas asignadas al subitem
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			System.out.println("reformalineatos: " + reformalineaTOs.size());
			double totalreforma=0.0;
			System.out.println("fecha creacion: " + fecha);
			for(ReformalineaTO reformalineaTO:reformalineaTOs){
				System.out.println("fecha a comparar: " + reformalineaTO.getReforma().getFechacreacion());
				//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
				if((reformalineaTO.getReforma().getEstado().equals("AP")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0 &&
						((reformalineaTO.getReformalineaultimacertificacion()!=null && reformalineaTO.getReformalineaultimacertificacion().longValue()<certificacionlineaTO.getId().getId()) || reformalineaTO.getReformalineaultimacertificacion()==null)){
					totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue()-reformalineaTO.getValordecremento().doubleValue();
				}
			}
			double codificado=totalajustado+totalreforma;
			Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtienecertificacionesnoeliminadas(nivelactividadid);
			//Itero para obtener el valor inicial
			double saldo=codificado;
			for(CertificacionlineaTO certificacionlineaTO2: certificacionlineaTOs) {
				System.out.println("certificacion propia: "+ certificacionlineaTO.getId().getId() + " - "+certificacionlineaTO.getId().getLineaid());
				System.out.println("certificacion a comparar: "+ certificacionlineaTO2.getNpcertificacionid());
				if(certificacionlineaTO2.getNpcertificacionid().longValue()<certificacionlineaTO.getId().getId().longValue()) {
					System.out.println("va a restar el valor: "+ certificacionlineaTO2.getValor());
					//si la liquidacion es parcial debo ver cuanto se utilizo realmente
					if(certificacionlineaTO2.getNpcertificacionestado().equals("LP")){
						//Obtengo las ordenes de gasto aprobadas
						OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
						ordengastolineaTO.setNivelactid(certificacionlineaTO.getNivelactid());
						OrdengastoTO ordengastoTO=new OrdengastoTO();
						ordengastoTO.setOrdengastocertificacionid(certificacionlineaTO2.getNpcertificacionid());
						ordengastoTO.setEstado("AP");
						ordengastolineaTO.setOrdengasto(ordengastoTO);
						Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
						double totalordenes=0.0;
						for(OrdengastolineaTO ordengastolineaTO2:ordengastolineaTOs)
							totalordenes=totalordenes+ordengastolineaTO2.getValor();
						saldo=saldo-totalordenes;
					}
					else{
						saldo=saldo-certificacionlineaTO2.getValor().doubleValue();
					}
				}
				else
					break;
			}
			return saldo;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static Double obtenersaldodisponiblelineareforma(Long idsubitem,Long nivelactividadid,ReformalineaTO reformalinea,boolean certificacion) throws MyException {
		try{
			System.out.println("subitem: " + idsubitem);
			SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
			subitemunidadacumuladorTO.getId().setId(idsubitem);
			subitemunidadacumuladorTO.setTipo("A");
			subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
			Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
			boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
			double totalajustado=0.0;
			for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs) {
				if(subitemunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
					ajustado=true;
					totalajustado=totalajustado+subitemunidadacumuladorTO2.getTotal().doubleValue();
				}
			}
			System.out.println("total ajustado: " + totalajustado);
			//traigo las reformas asignadas al subitem
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			System.out.println("reformalineatos: " + reformalineaTOs.size());
			double totalreforma=0.0;
			System.out.println("fecha creacion: " + reformalinea.getNpfechacreacion());
			System.out.println("reformalinea.getId().getId(): " + reformalinea.getId().getId());
			System.out.println("reformalineaTO.getReforma().getEstado(): " + reformalinea.getReforma().getEstado());
			System.out.println("reformalinea.getReforma().getFechaaprobacion(): "+ reformalinea.getReforma().getFechaaprobacion());
			for(ReformalineaTO reformalineaTO:reformalineaTOs){
				System.out.println("fecha a comparar: " + reformalineaTO.getReforma().getFechacreacion());
				//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
				if((reformalinea.getReforma().getEstado().equals("SO") || reformalinea.getReforma().getEstado().equals("RE")) && reformalinea.getId().getId().longValue()!=reformalineaTO.getId().getId().longValue()){
//								&& reformalinea.getNpreformaid().longValue()<reformalinea1TO.getId().getId().longValue()){
					System.out.println("valor:/// " + reformalineaTO.getValorincremento() + ", " + reformalineaTO.getValordecremento()+" id "+reformalineaTO.getId().getId());
					totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue()-reformalineaTO.getValordecremento().doubleValue();
				}
				else if(reformalinea.getId().getId().longValue()!=reformalineaTO.getId().getId().longValue() && reformalinea.getReforma().getEstado().equals("AP") && reformalineaTO.getReforma().getFechacreacion().compareTo(reformalinea.getReforma().getFechaaprobacion())<=0){
//								&& reformalinea.getNpreformaid().longValue()<reformalinea1TO.getId().getId().longValue()){
					System.out.println("valor:***** " + reformalineaTO.getValorincremento() + ", " + reformalineaTO.getValordecremento()+" id "+reformalineaTO.getId().getId());
					totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue()-reformalineaTO.getValordecremento().doubleValue();
				}

				
			}
			System.out.println("total reformas: " + totalreforma);
			double codificado=totalajustado+totalreforma;
			double saldo=codificado;
			double certificaciones=0.0;
			double ordenesgasto=0.0;
			if(certificacion){
				Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtienecertificacionesnoeliminadas(nivelactividadid);
				System.out.println("certificaciones:  "+ certificacionlineaTOs.size());
				//Itero para obtener el valor inicial
				for(CertificacionlineaTO certificacionlineaTO2: certificacionlineaTOs) {
					System.out.println("certificacion: " + certificacionlineaTO2.getNpfechacreacion() + " - "+certificacionlineaTO2.getNpcertificacionestado());
					if(reformalinea.getReforma().getEstado().equals("AP") || reformalinea.getReforma().getEstado().equals("NE") || reformalinea.getReforma().getEstado().equals("EL")){
						if(certificacionlineaTO2.getNpfechacreacion().compareTo(UtilGeneral.parseStringToDate(reformalinea.getNpfechacreacion()))<=0 &&
								((reformalinea.getReformalineaultimacertificacion()==null ||reformalinea.getReformalineaultimacertificacion().longValue()==0) || (reformalinea.getReformalineaultimacertificacion()!=null && reformalinea.getReformalineaultimacertificacion().longValue()!=0 && certificacionlineaTO2.getNpcertificacionid()<=reformalinea.getReformalineaultimacertificacion().longValue()))){
							System.out.println("va a restar el valor: "+ certificacionlineaTO2.getValor());
							//si la liquidacion es parcial debo ver cuanto se utilizo realmente
							if(certificacionlineaTO2.getNpcertificacionestado().equals("LP")){
								//Obtengo las ordenes de gasto aprobadas
								OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
								ordengastolineaTO.setNivelactid(reformalinea.getNivelactid());
								OrdengastoTO ordengastoTO=new OrdengastoTO();
								ordengastoTO.setOrdengastocertificacionid(certificacionlineaTO2.getNpcertificacionid());
								ordengastoTO.setEstado("AP");
								ordengastolineaTO.setOrdengasto(ordengastoTO);
								Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
								double totalordenes=0.0;
								for(OrdengastolineaTO ordengastolineaTO2:ordengastolineaTOs)
									totalordenes=totalordenes+ordengastolineaTO2.getValor();
								System.out.println("ordenes: " + totalordenes);
								ordenesgasto=ordenesgasto+totalordenes;
							}
							//else if(certificacionlineaTO2.getNpcertificacionestado().equals("AP")){
							else{
								certificaciones=certificaciones+certificacionlineaTO2.getValor();
							}
						}
					}
					else if(reformalinea.getReforma().getEstado().equals("SO") || reformalinea.getReforma().getEstado().equals("RE")){
						//si la liquidacion es parcial debo ver cuanto se utilizo realmente
						if(certificacionlineaTO2.getNpcertificacionestado().equals("LP")){
							//Obtengo las ordenes de gasto aprobadas
							OrdengastolineaTO ordengastolineaTO=new OrdengastolineaTO();
							ordengastolineaTO.setNivelactid(reformalinea.getNivelactid());
							OrdengastoTO ordengastoTO=new OrdengastoTO();
							ordengastoTO.setOrdengastocertificacionid(certificacionlineaTO2.getNpcertificacionid());
							ordengastoTO.setEstado("AP");
							ordengastolineaTO.setOrdengasto(ordengastoTO);
							Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO);
							double totalordenes=0.0;
							for(OrdengastolineaTO ordengastolineaTO2:ordengastolineaTOs)
								totalordenes=totalordenes+ordengastolineaTO2.getValor();
							System.out.println("ordenes: " + totalordenes);
							ordenesgasto=ordenesgasto+totalordenes;
						}
						else{
							certificaciones=certificaciones+certificacionlineaTO2.getValor();
						}
					}

					//					else
//						break;
				}
			}
			System.out.println("codificado: "+ codificado);
			System.out.println("certificaciones: "+ certificaciones);
			System.out.println("ordenesgasto: "+ ordenesgasto);
			saldo=codificado-ordenesgasto-certificaciones;
			if(saldo<0)
				saldo=0;
			System.out.println("saldo: "+ saldo);
			return saldo;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static Double obtenersaldodisponiblesubtarea(Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs,Long nivelactividadid,Date fecha) throws MyException {
		try{
			double saldo=0.0;
			System.out.println("subtareaunidadacumuladorTOs "+subtareaunidadacumuladorTOs.size());
			if(subtareaunidadacumuladorTOs.size()>0) {
				boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
				double totalajustado=0.0;
				for(SubtareaunidadacumuladorTO subtareaunidadacumuladorTO2:subtareaunidadacumuladorTOs) {
					if(subtareaunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
						ajustado=true;
						totalajustado=totalajustado+subtareaunidadacumuladorTO2.getCantidad().doubleValue();
					}
				}	
				
				//traigo las metassubtarea asignadas al subitem
				Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtienereformassinoelne(nivelactividadid);
				System.out.println("reformametalineatos: " + reformametalineaTOs.size());
				double totalreforma=0.0;
				System.out.println("fecha creacion: " + fecha);
				for(ReformametalineaTO reformametalineaTO:reformametalineaTOs){
					System.out.println("fecha a comparar: " + reformametalineaTO.getReformameta().getFechacreacion());
					//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
					if(reformametalineaTO.getReformameta().getEstado().equals("AP") ||reformametalineaTO.getReformameta().getEstado().equals("RE") || reformametalineaTO.getReformameta().getEstado().equals("SO")){
						totalreforma=totalreforma+reformametalineaTO.getValorincremento().doubleValue()-reformametalineaTO.getValordecremento().doubleValue();
					}
				}
				saldo=totalajustado+totalreforma;
				System.out.println("totalajustad: " + totalajustado + "saldo: " + saldo);
			}
				return saldo;
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	public static Double obtenersaldodisponiblereformasubtarea(Collection<SubtareaunidadacumuladorTO> subtareaunidadacumuladorTOs,Long nivelactividadid,Date fecha) throws MyException {
		try{
			double saldo=0.0;
			System.out.println("subtareaunidadacumuladorTOs "+subtareaunidadacumuladorTOs.size());
			if(subtareaunidadacumuladorTOs.size()>0) {
				boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
				double totalajustado=0.0;
				for(SubtareaunidadacumuladorTO subtareaunidadacumuladorTO2:subtareaunidadacumuladorTOs) {
					if(subtareaunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
						ajustado=true;
						totalajustado=totalajustado+subtareaunidadacumuladorTO2.getCantidad().doubleValue();
					}
				}	
				
				//traigo las metassubtarea asignadas al subitem
				Collection<ReformametasubtareaTO> reformametasubtareaTOs=UtilSession.planificacionServicio.transObtienereformametasubtareanoelne(nivelactividadid);
				System.out.println("reformametalineatos: " + reformametasubtareaTOs.size());
				double totalreforma=0.0;
				System.out.println("fecha creacion: " + fecha);
				for(ReformametasubtareaTO reformametasubtareaTO:reformametasubtareaTOs){
					System.out.println("fecha a comparar: " + reformametasubtareaTO.getReforma().getFechacreacion());
					//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
					if(reformametasubtareaTO.getReforma().getEstado().equals("AP") ||reformametasubtareaTO.getReforma().getEstado().equals("RE") || reformametasubtareaTO.getReforma().getEstado().equals("SO")){
						totalreforma=totalreforma+reformametasubtareaTO.getValorincremento().doubleValue()-reformametasubtareaTO.getValordecremento().doubleValue();
					}
				}
				saldo=totalajustado+totalreforma;
				System.out.println("totalajustad: " + totalajustado + "saldo: " + saldo);
			}
				return saldo;
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	//	public static Double obtenersaldodisponible(Double total,Long idsubitem,Long nivelactividadid,Date fecha) throws MyException {
//		try{
//			SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(idsubitem);
//			//traigo las reformas asignadas al subitem
//			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
//			//Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(69118L);
//			System.out.println("reformalineatos: " + reformalineaTOs.size());
//			double totalreforma=0.0;
//			System.out.println("fecha creacion: " + fecha);
//			for(ReformalineaTO reformalineaTO:reformalineaTOs){
//				System.out.println("fecha a comparar: " + reformalineaTO.getReforma().getFechacreacion());
//				//if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
//				if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
//					System.out.println("incremento: " + reformalineaTO.getValorincremento() + " - " + reformalineaTO.getValordecremento());
//					//totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue()-reformalineaTO.getValordecremento().doubleValue();
//					totalreforma=totalreforma-reformalineaTO.getValordecremento().doubleValue();
//					System.out.println("total: " + totalreforma);
//				}
//			}
//			System.out.println("totalreforma: " + totalreforma);
//			System.out.println("valores: " +total+"-"+totalreforma+"-"+subitemunidadTO.getValprecompromiso()+"-"+subitemunidadTO.getValxcomprometer().doubleValue()+"-"+subitemunidadTO.getValcompromiso());
//			double saldo=total+totalreforma-subitemunidadTO.getValprecompromiso().doubleValue()-subitemunidadTO.getValxcomprometer().doubleValue()-subitemunidadTO.getValcompromiso().doubleValue();
//			return saldo;
//
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new MyException(e);
//		}
//	}
	
	/**
	* Metodo que obtiene el saldo disponible de un subitem
	*
	* @param request 
	* @return total
	* @throws MyException
	*/

	public static Double obtenercodificadoyreformas(Double total,Long idsubitem,Long nivelactividadid,Date fecha) throws MyException {
		try{
			//SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(idsubitem);
			//traigo las reformas asignadas al subitem
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			//Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(69118L);
			System.out.println("reformalineatos: " + reformalineaTOs.size());
			double totalreforma=0.0;
			System.out.println("fecha creacion: " + fecha);
			for(ReformalineaTO reformalineaTO:reformalineaTOs){
				System.out.println("fecha a comparar: " + reformalineaTO.getReforma().getFechacreacion());
				if((reformalineaTO.getReforma().getEstado().equals("RE") || reformalineaTO.getReforma().getEstado().equals("SO")) && reformalineaTO.getReforma().getFechacreacion().compareTo(fecha)<=0){
					System.out.println("incremento: " + reformalineaTO.getValorincremento() + " - " + reformalineaTO.getValordecremento());
					//totalreforma=totalreforma+reformalineaTO.getValorincremento().doubleValue()-reformalineaTO.getValordecremento().doubleValue();
					totalreforma=totalreforma-reformalineaTO.getValordecremento().doubleValue();
					System.out.println("total: " + totalreforma);
				}
			}
			System.out.println("totalreforma: " + totalreforma);
			double saldo=total+totalreforma;
			return saldo;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}


	/**
	* Metodo que obtiene el valor total del subitem
	*
	* @param request 
	* @return total
	* @throws MyException
	*/

	public static Double obtenertotalsubitem(Double total,Long idsubitem,Long nivelactividadid,ReformalineaTO reformalineaTO) throws MyException {
		try{
			//traigo las reformas asignadas al subitem
//			Collection<ReformaTO> reformaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			double totalreforma=0.0;
//			for(ReformaTO reformaTO:reformaTOs)
//				totalreforma=totalreforma+reformaTO.getValorincremento().doubleValue()-reformaTO.getValordecremento();
			double valtotal=total+totalreforma+reformalineaTO.getValordecremento().doubleValue()-reformalineaTO.getValorincremento().doubleValue();
			return valtotal;

		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
	}

	/**
	* Metodo que consulta el detalle de una certificacion y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenercertificacion(Long id,JSONObject jsonObject) throws MyException {
		CertificacionTO certificacionTO=new CertificacionTO();
		try{
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
			certificacionTO.setNpunidadcodigo(certificacionTO.getUnidad().getCodigopresup());
			//traigo las certificacionlinea las traigo
			CertificacionlineaTO certificacionlineaTO=new CertificacionlineaTO();
			certificacionlineaTO.getId().setId(certificacionTO.getId());
			//Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtenerCertificacionlinea(certificacionlineaTO);
			Collection<CertificacionlineaTO> certificacionlineaTOs=UtilSession.planificacionServicio.transObtienecertificadoslinea(certificacionTO.getId());
			jsonObject.put("certificacionlineas", (JSONArray)JSONSerializer.toJSON(certificacionlineaTOs,certificacionlineaTO.getJsonConfig()));
			jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta el detalle de una certificacion y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenercertificacionbusqueda(Long id,JSONObject jsonObject) throws MyException {
		CertificacionTO certificacionTO=new CertificacionTO();
		try{
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
			jsonObject.put("certificacion", (JSONObject)JSONSerializer.toJSON(certificacionTO,certificacionTO.getJsonConfig()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta el detalle de una orden de gasto y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenerordengasto(Long id,JSONObject jsonObject) throws MyException {
		OrdengastoTO ordengastoTO=new OrdengastoTO();
		try{
			ordengastoTO = UtilSession.planificacionServicio.transObtenerOrdengastoTO(id);
			ordengastoTO.setNpunidadcodigo(ordengastoTO.getUnidad().getCodigopresup());
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
				ordengastoTO.setOrdengastotpclasedocid(ordengastoTO.getTipodocumentoclasedocumento().getId().getClasedocid());
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
			//obtengo el nivelactividad
			Collection<OrdengastolineaTO> ordengastolineaTOs1=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordengastolineaTO,true);
			log.println("ordenes de gasto... "+ ordengastolineaTOs1.size());
//			if(ordengastolineaTOs1.size()>0) {
//				ordengastolineaTO=(OrdengastolineaTO)ordengastolineaTOs1.iterator().next();
//				OrdengastolineaTO ordenconsulta=new OrdengastolineaTO();
//				log.println("nivel actividad " + ordengastolineaTO.getNivelactid());
//				ordenconsulta.setNivelactid(ordengastolineaTO.getNivelactid());
//				//ordenconsulta.setOrdengasto(ordengastoTO);
//				log.println("id del nivel actividad " + ordengastolineaTO.getNivelactid());
//				Collection<OrdengastolineaTO> ordengastolineaTOs=UtilSession.planificacionServicio.transObtenerOrdengastolinea(ordenconsulta,true);
//				log.println("lineas "+ ordengastolineaTOs.size());
				jsonObject.put("ordengastolineas", (JSONArray)JSONSerializer.toJSON(ordengastolineaTOs1,ordengastolineaTO.getJsonConfig()));
//			}
//			else
//				jsonObject.put("ordengastolineas", (JSONArray)JSONSerializer.toJSON(new ArrayList<>(),ordengastolineaTO.getJsonConfig()));
			jsonObject.put("ordengasto", (JSONObject)JSONSerializer.toJSON(ordengastoTO,ordengastoTO.getJsonConfig()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta el detalle de una orden de devengo y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenerordendevengo(Long id,JSONObject jsonObject) throws MyException {
		try{
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
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta el detalle de una orden de reversion y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenerordenreversion(Long id,JSONObject jsonObject) throws MyException {
		try{
			OrdenreversionTO ordenreversionTO=new OrdenreversionTO();
			ordenreversionTO = UtilSession.planificacionServicio.transObtenerOrdenreversionTO(id);
			if(ordenreversionTO.getOrdenreversionogastoid()!=null){
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
			jsonObject.put("ordenreversionlineas", (JSONArray)JSONSerializer.toJSON(ordendevengolineaTOs,ordenreversionlineaTO.getJsonConfig()));
			jsonObject.put("ordenreversion", (JSONObject)JSONSerializer.toJSON(ordenreversionTO,ordenreversionTO.getJsonConfig()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta el detalle de una reforma y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenerreforma(Long id,JSONObject jsonObject) throws MyException {
		try{
			ReformaTO reformaTO=new ReformaTO();
			reformaTO = UtilSession.planificacionServicio.transObtenerReformaTO(id);
			reformaTO.setNpunidadcodigo(reformaTO.getUnidad().getCodigopresup());
			reformaTO.setNpunidadnombre(reformaTO.getUnidad().getNombre());
			if(reformaTO.getNivelactividadid()!=null){
				NivelactividadTO nivelactividadTO=new NivelactividadTO();
				System.out.println("Ejericio fical " + reformaTO.getEjerciciofiscal());
				//nivelactividadTO.setId(reformaTO.getId());
				nivelactividadTO.setTipo("IT");
				nivelactividadTO.setNivelactividadejerfiscalid(reformaTO.getReformaejerfiscalid());
				nivelactividadTO.setId(reformaTO.getNivelactividadid());
				nivelactividadTO.setEstado("A");
				Collection<NivelactividadTO> nivelactividadTO2s=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO, false);
				nivelactividadTO=(NivelactividadTO)nivelactividadTO2s.iterator().next();
				//Consulto el item
	//			ItemunidadTO itemunidadTO=new ItemunidadTO();
	//			itemunidadTO.setItem(new ItemTO());
	//			itemunidadTO.setId(nivelactividadTO.getTablarelacionid());
	//			Collection<ItemunidadTO> itemunidadTOs=UtilSession.planificacionServicio.transObtenerItemunidad(itemunidadTO);
	//			if(itemunidadTOs.size()>0) {
	//				itemunidadTO=(ItemunidadTO)itemunidadTOs.iterator().next();
					reformaTO.setNpitemcodigo(nivelactividadTO.getNpcodigo()+" - "+ nivelactividadTO.getNpcodigocanton().substring(2) + " - " + nivelactividadTO.getNpcodigofuente());
					reformaTO.setNpitemnombre(nivelactividadTO.getNpdescripcion());
	//			}
			}
			//asigno las fechas
			reformaTO.setNpfechaaprobacion(UtilGeneral.parseDateToString(reformaTO.getFechaaprobacion()));
			reformaTO.setNpfechacreacion(UtilGeneral.parseDateToString(reformaTO.getFechacreacion()));
			reformaTO.setNpfechaeliminacion(UtilGeneral.parseDateToString(reformaTO.getFechaeliminacion()));
			reformaTO.setNpfechanegacion(UtilGeneral.parseDateToString(reformaTO.getFechanegacion()));
			reformaTO.setNpfechasolicitud(UtilGeneral.parseDateToString(reformaTO.getFechasolicitud()));
			//traigo las reformaslinea las traigo
			ReformalineaTO reformalineaTO=new ReformalineaTO();
			reformalineaTO.getId().setId(reformaTO.getId());
			Collection<ReformalineaTO> reformalineaTOs=UtilSession.planificacionServicio.transObtenerReformalinea(reformalineaTO, reformaTO.getFechacreacion());
			//System.out.println("lineas... " + reformalineaTOs.size());
			jsonObject.put("reformalineas", (JSONArray)JSONSerializer.toJSON(reformalineaTOs,reformalineaTO.getJsonConfig()));
			jsonObject.put("reforma", (JSONObject)JSONSerializer.toJSON(reformaTO,reformaTO.getJsonConfig()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta el detalle de una reforma meta y sus lineas
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject obtenerreformameta(Long id,JSONObject jsonObject) throws MyException {
		try{
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
			reformametalineaTO.getId().setId(reformametaTO.getId());
			Collection<ReformametalineaTO> reformametalineaTOs=UtilSession.planificacionServicio.transObtenerReformametalinea(reformametalineaTO,reformametaTO.getFechacreacion());
			System.out.println("lineas: "+ reformametalineaTOs.size());
			jsonObject.put("reformametalineas", (JSONArray)JSONSerializer.toJSON(reformametalineaTOs,reformametalineaTO.getJsonConfigconsulta()));
			jsonObject.put("reformameta", (JSONObject)JSONSerializer.toJSON(reformametaTO,reformametaTO.getJsonConfig()));
			System.out.println("json: " + jsonObject.toString());
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}



	/**
	* Metodo que consulta las reformas paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaReformaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		ReformaTO reformaTO=new ReformaTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			reformaTO.setFirstResult(primero);
			reformaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				reformaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				reformaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial").replaceAll("%2F", "/"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			System.out.println("fechas: " + fechaInicial + " - " + fechaFinal);
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null){
					System.out.println("va  a setear fechas");
					reformaTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				}
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					reformaTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					reformaTO.setEstado(parameters.get("estado"));
				if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
					reformaTO.setReformaejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				if(parameters.get("tipo")!=null && !parameters.get("tipo").equals("")){
					reformaTO.setTipo(parameters.get("tipo"));
				}
				System.out.println("fechas: " + reformaTO.getRangoFechacreacion());
				SearchResultTO<ReformaTO> resultado=UtilSession.planificacionServicio.transObtenerReformaPaginado(reformaTO, principal.getName());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + totalRegistrosPagina);
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),reformaTO.getJsonConfigconsulta()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta las reformasmetas paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaReformametaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes, Principal principal) throws MyException {
		String campo="";
		ReformametaTO reformametaTO=new ReformametaTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechacreacion";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			reformametaTO.setFirstResult(primero);
			reformametaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				reformametaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				reformametaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			Date fechaInicial=null;
			Date fechaFinal=null;
			if(parameters.get("fechainicial")!=null)
				fechaInicial=UtilGeneral.parseStringToDate(parameters.get("fechainicial"));
			if(parameters.get("fechafinal")!=null)
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal"));
			if(parameters.get("fechainicial")!=null && parameters.get("fechafinal")==null)
				fechaFinal=(new Date());
			if(parameters.get("fechafinal")!=null && parameters.get("fechainicial")==null){
				fechaFinal=UtilGeneral.parseStringToDate(parameters.get("fechafinal").replaceAll("%2F", "/"));
				Calendar fechaactual = new GregorianCalendar();
				fechaactual.setTime(fechaFinal);
				int anio=fechaactual.get(Calendar.YEAR);
				Calendar fechag=new GregorianCalendar(anio, 0, 1);
				fechaInicial=fechag.getTime();
			}
			if(mensajes.getMsg()==null){
				if(fechaInicial!=null)
					reformametaTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					reformametaTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					reformametaTO.setEstado(parameters.get("estado"));
				if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
					reformametaTO.setEjerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				SearchResultTO<ReformametaTO> resultado=UtilSession.planificacionServicio.transObtenerReformametaPaginado(reformametaTO, principal.getName());
				System.out.println("resultado-------: " + resultado.getCountResults());
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				System.out.println("totalresultado****: " + totalRegistrosPagina);
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),reformametaTO.getJsonConfig()));
				jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta las actividades para la ejecucion de meta paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaActividadesEjecucionMetas(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
		Collection<Ejecucioncabeceraact> ejecucioncabeceraacts=new ArrayList<Ejecucioncabeceraact>();
		try{
			Long actividadid=null;
			Long mes=null;
			if(parameters.get("actividadid")!=null){
				actividadid=Long.valueOf(parameters.get("actividadid"));
			}
			if(parameters.get("mes")!=null){
				mes=Long.valueOf(parameters.get("mes"));
			}
			ejecucioncabeceraacts=UtilSession.planificacionServicio.transObtieneActividadesejecucionagrupado(Long.valueOf(parameters.get("institucionid")), Long.valueOf(parameters.get("entidadid")), Long.valueOf(parameters.get("unidadid")),actividadid, mes,Long.valueOf(parameters.get("ejerciciofiscalid")));
			Integer totalRegistrosPagina=ejecucioncabeceraacts.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(ejecucioncabeceraacts));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}


	/**
	* Metodo que consulta las actividades para la ejecucion de meta paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaSubtareaEjecucionMetas(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
		Collection<Ejecucioncabecerasubtarea> ejecucioncabecerasubtareas=new ArrayList<Ejecucioncabecerasubtarea>();
		try{
			Long actividadid=null;
			Long mesdesde=null;
			Long meshasta=null;
			Long tarea=null;
			Long subactividad=null;
			if(parameters.get("actividadid")!=null){
				actividadid=Long.valueOf(parameters.get("actividadid"));
			}
			if(parameters.get("subtactividadid")!=null){
				subactividad=Long.valueOf(parameters.get("subtactividadid"));
			}
			if(parameters.get("tareaid")!=null){
				tarea=Long.valueOf(parameters.get("tareaid"));
			}

			if(parameters.get("mesdesde")!=null){
				mesdesde=Long.valueOf(parameters.get("mesdesde"));
			}
			if(parameters.get("meshasta")!=null){
				meshasta=Long.valueOf(parameters.get("meshasta"));
			}

			System.out.println("actividad: " + actividadid + " subactividad: " + subactividad + " tarea: " + tarea);
			ejecucioncabecerasubtareas=UtilSession.planificacionServicio.transObtieneSubtareasejecucionagrupado(Long.valueOf(parameters.get("institucionid")), Long.valueOf(parameters.get("entidadid")), Long.valueOf(parameters.get("unidadid")),actividadid, mesdesde,meshasta,Long.valueOf(parameters.get("ejerciciofiscalid")),subactividad,tarea);
			Integer totalRegistrosPagina=ejecucioncabecerasubtareas.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(ejecucioncabecerasubtareas));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

}
