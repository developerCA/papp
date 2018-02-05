package ec.com.papp.web.ejecucion.util;

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
import ec.com.papp.planificacion.to.OrdendevengoTO;
import ec.com.papp.planificacion.to.OrdengastoTO;
import ec.com.papp.planificacion.to.OrdenreversionTO;
import ec.com.papp.planificacion.to.ReformaTO;
import ec.com.papp.planificacion.to.ReformalineaTO;
import ec.com.papp.planificacion.to.SubitemunidadTO;
import ec.com.papp.planificacion.to.SubitemunidadacumuladorTO;
import ec.com.papp.planificacion.util.MatrizDetalle;
import ec.com.papp.web.comun.util.Mensajes;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.resource.MensajesWeb;
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

	public static JSONObject consultaCertificacionPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				certificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				certificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
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
					certificacionTO.setNumprecompromiso(parameters.get("numprecompromiso"));
				if(parameters.get("certificacionejerfiscalid")!=null && !parameters.get("certificacionejerfiscalid").equals(""))
					certificacionTO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("certificacionejerfiscalid")));
				log.println("certificacion: " + certificacionTO.getCertificacionejerfiscalid());
				SearchResultTO<CertificacionTO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionPaginado(certificacionTO);
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

	public static JSONObject consultaOrdengastoPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				ordengastoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				ordengastoTO.setOrderByField(OrderBy.orderAsc(orderBy));
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
					ordengastoTO.setNumerocompromiso(parameters.get("compromiso"));
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
				SearchResultTO<OrdengastoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastoPaginado(ordengastoTO);
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

	public static JSONObject consultaOrdendevengoPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				ordendevengoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				ordendevengoTO.setOrderByField(OrderBy.orderAsc(orderBy));
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
				if(parameters.get("ordengasto")!=null && !parameters.get("ordengasto").equals("")){
					ordengastoTO.setCodigo(parameters.get("ordengasto"));
				}
				ordendevengoTO.setOrdengasto(ordengastoTO);
				SearchResultTO<OrdendevengoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdendevengoPaginado(ordendevengoTO);
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

	public static JSONObject consultaOrdenreversionPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				ordenreversionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				ordenreversionTO.setOrderByField(OrderBy.orderAsc(orderBy));
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
					ordenreversionTO.setCodigo(parameters.get("ordengasto"));
				}
				ordenreversionTO.setOrdengasto(ordengastoTO);
				SearchResultTO<OrdenreversionTO> resultado=UtilSession.planificacionServicio.transObtenerOrdenreversionPaginado(ordenreversionTO);
				long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
				HashMap<String, String>  totalMap=new HashMap<String, String>();
				totalMap.put("valor", resultado.getCountResults().toString());
				log.println("totalresultado: " + totalRegistrosPagina);
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

	public static JSONObject consultaCertificacionBusquedaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				certificacionTO.setDescripcion(parameters.get("descripcion"));
			if(parameters.get("unidadid")!=null && !parameters.get("unidadid").equals(""))
				certificacionTO.setCertificacionunidadid(Long.valueOf(parameters.get("unidadid")));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				certificacionTO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			SearchResultTO<CertificacionTO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionPaginado(certificacionTO);
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

	public static JSONObject consultaOrdengastoBusquedaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				ordengastoTO.setDescripcion(parameters.get("descripcion"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				ordengastoTO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			if(parameters.get("unidad")!=null && !parameters.get("unidad").equals("")){
				unidadTO.setNombre(parameters.get("unidad"));
			}
			ordengastoTO.setUnidad(unidadTO);
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				ordengastoTO.setOrdengastoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			SearchResultTO<OrdengastoTO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastoPaginado(ordengastoTO);
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

	public static JSONObject certificacionbusqueda(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		CertificacionOrdenVO certificacionOrdenVO=new CertificacionOrdenVO();
		try{
			if(parameters.get("ejerciciofiscal")!=null && !parameters.get("ejerciciofiscal").equals(""))
				certificacionOrdenVO.setCertificacionejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("unidad")!=null && !parameters.get("unidad").equals(""))
				certificacionOrdenVO.setCertificacionunidadid(Long.valueOf(parameters.get("unidad")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				certificacionOrdenVO.setEstado(parameters.get("estado"));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				certificacionOrdenVO.setCodigo(parameters.get("codigo"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				certificacionOrdenVO.setDescripcion(parameters.get("descripcion"));
			Collection<CertificacionOrdenVO> resultado=UtilSession.planificacionServicio.transObtenerCertificacionOrden(certificacionOrdenVO);
			log.println("certificaciones: " + resultado.size());
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
	
	public static JSONObject ordenesgastobusqueda(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
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
				gastoDevengoVO.setDescripcion(parameters.get("descripcion"));

			Collection<GastoDevengoVO> resultado=UtilSession.planificacionServicio.transObtenerOrdengastodevengo(gastoDevengoVO);
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

	public static Double obtenertotalsubitem(Long idsubitem) throws MyException {
		double total=0.0;
		try{
			//traigo los datos de actividadunidadacumulador
			SubitemunidadacumuladorTO subitemunidadacumuladorTO=new SubitemunidadacumuladorTO();
			subitemunidadacumuladorTO.getId().setId(idsubitem);
			//subitemunidadacumuladorTO.setTipo("A");
			subitemunidadacumuladorTO.setOrderByField(OrderBy.orderAsc("id.acumid"));
			Collection<SubitemunidadacumuladorTO> subitemunidadacumuladorTOs=UtilSession.planificacionServicio.transObtenerSubitemunidadacumuladro(subitemunidadacumuladorTO);
			boolean ajustado=false;//uso una bandera para solo tomar un valor ajustado de la coleccion
			for(SubitemunidadacumuladorTO subitemunidadacumuladorTO2:subitemunidadacumuladorTOs) {
				if(subitemunidadacumuladorTO2.getTipo().equals("A") && !ajustado) {
					ajustado=true;
					total=total+subitemunidadacumuladorTO2.getValor().doubleValue();
				}
				else if(subitemunidadacumuladorTO2.getTipo().equals("R"))
					total=total+subitemunidadacumuladorTO2.getTotal().doubleValue();
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

	public static Double obtenersaldodisponible(Double total,Long idsubitem,Long nivelactividadid) throws MyException {
		try{
			SubitemunidadTO subitemunidadTO=UtilSession.planificacionServicio.transObtenerSubitemunidadTO(idsubitem);
			//traigo las reformas asignadas al subitem
			Collection<ReformaTO> reformaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			double totalreforma=0.0;
			for(ReformaTO reformaTO:reformaTOs)
				totalreforma=totalreforma+reformaTO.getValorincremento().doubleValue()-reformaTO.getValordecremento();
			double saldo=total+totalreforma-subitemunidadTO.getValprecompromiso().doubleValue()-subitemunidadTO.getValxcomprometer().doubleValue()-subitemunidadTO.getValcompromiso().doubleValue();
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
			Collection<ReformaTO> reformaTOs=UtilSession.planificacionServicio.transObtienereformasnoelne(nivelactividadid);
			double totalreforma=0.0;
			for(ReformaTO reformaTO:reformaTOs)
				totalreforma=totalreforma+reformaTO.getValorincremento().doubleValue()-reformaTO.getValordecremento();
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
	* Metodo que consulta las reformas paginadas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaReformaPaginado(Map<String, String> parameters,JSONObject jsonObject,Mensajes mensajes) throws MyException {
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
				reformaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				reformaTO.setOrderByField(OrderBy.orderAsc(orderBy));
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
					reformaTO.setRangoFechacreacion(new RangeValueTO<Date>(fechaInicial,fechaFinal));
				if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
					reformaTO.setCodigo(parameters.get("codigo"));
				if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
					reformaTO.setEstado(parameters.get("estado"));
				if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
					reformaTO.setReformaejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				if(parameters.get("tipo")!=null && !parameters.get("tipo").equals("")){
					reformaTO.setTipo(parameters.get("tipo"));
				}
				SearchResultTO<ReformaTO> resultado=UtilSession.planificacionServicio.transObtenerReformaPaginado(reformaTO);
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

}
