package ec.com.papp.web.administracion.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.tools.commons.to.OrderBy;
import org.hibernate.tools.commons.to.SearchResultTO;

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
import ec.com.papp.administracion.to.TipoidentificacionTO;
import ec.com.papp.administracion.to.TipoidentificaciontipoTO;
import ec.com.papp.administracion.to.TipoproductoTO;
import ec.com.papp.administracion.to.TiporegimenTO;
import ec.com.papp.administracion.to.UnidadmedidaTO;
import ec.com.papp.estructuraorganica.to.InstitucionTO;
import ec.com.papp.planificacion.to.NivelorganicoTO;
import ec.com.papp.planificacion.to.OrganismoprestamoTO;
import ec.com.papp.resource.MensajesAplicacion;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.Log;

public class ConsultasUtil {

	
	private static Log log = new Log(ConsultasUtil.class);
	
	/**
	* Metodo que consulta los ejercicios fiscales paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaEjerciciofiscalPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		EjerciciofiscalTO ejerciciofiscalTO=new EjerciciofiscalTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=200;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="anio";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			ejerciciofiscalTO.setFirstResult(primero);
//			ejerciciofiscalTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("asc"))
				ejerciciofiscalTO.setOrderByField(OrderBy.orderAsc(orderBy));
			else
				ejerciciofiscalTO.setOrderByField(OrderBy.orderDesc(orderBy));
			if(parameters.get("anio")!=null && !parameters.get("anio").equals(""))
				ejerciciofiscalTO.setAnio(Long.valueOf(parameters.get("anio")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				ejerciciofiscalTO.setEstado(parameters.get("estado"));
//			SearchResultTO<EjerciciofiscalTO> resultado=UtilSession.adminsitracionServicio.transObtenerEjerciciofiscalPaginado(ejerciciofiscalTO);
			Collection<EjerciciofiscalTO> resultado=UtilSession.adminsitracionServicio.transObtenerEjerciciofiscal(ejerciciofiscalTO);
			//long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			//HashMap<String, String>  resultado.size()=new HashMap<String, String>();
			//resultado.size().put("valor", resultado.getCountResults().toString());
			//log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,ejerciciofiscalTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las Instituciones y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaIntitucionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		InstitucionTO institucionTO=new InstitucionTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			institucionTO.setFirstResult(primero);
//			institucionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				institucionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				institucionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				institucionTO.setEstado(parameters.get("estado"));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				institucionTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("entidadidserial")!=null && !parameters.get("entidadidserial").equals(""))
				institucionTO.setEntidadidserial(Double.valueOf(parameters.get("entidadidserial")));
			if(parameters.get("institucionejerciciofiscalid")!=null && !parameters.get("institucionejerciciofiscalid").equals(""))
				institucionTO.setInstitucionejerciciofiscalid(Long.valueOf(parameters.get("institucionejerciciofiscalid")));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				institucionTO.setNombre(parameters.get("nombre"));
			Collection<InstitucionTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerInstitucion(institucionTO);	
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", Integer.valueOf(resultado.size()).toString());
//			log.println("totalresultado: " + resultado.size());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,institucionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	/**
	* Metodo que consulta las Divisiones geograficas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaDivisionesgeograficas(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		DivisiongeograficaTO divisiongeograficaTO=new DivisiongeograficaTO();
		try{
			Long id=null;
			String estado=null;
			String tipo=null;
			Long padreid=null;
			String nombre=null;
			String nombrepadre=null;
			String codigo=null;
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				id=Long.valueOf(parameters.get("id"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				estado=parameters.get("estado");
			if(parameters.get("padreid")!=null && !parameters.get("padreid").equals(""))
				padreid= Long.valueOf(parameters.get("padreid"));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				tipo=parameters.get("tipo");
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				nombre=parameters.get("nombre");
			if(parameters.get("nombrepadre")!=null && !parameters.get("nombrepadre").equals(""))
				nombrepadre=parameters.get("nombrepadre");
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				codigo=parameters.get("codigo");

			Collection<DivisiongeograficaTO> resultado=UtilSession.adminsitracionServicio.transObtenerDivisiongeografica(id, estado,tipo,padreid,nombre,nombrepadre,codigo);	
			//itero los resultados para setear los datos del padre
//			for(DivisiongeograficaTO divisiongeograficaTO2: resultado){
//				if(divisiongeograficaTO2.getDivisiongeograficapadreid()!=null){
//					Collection<DivisiongeograficaTO> collection=UtilSession.adminsitracionServicio.transObtenerDivisiongeografica(divisiongeograficaTO.getDivisiongeograficapadreid(),null,null,null,null,null,null);
//					DivisiongeograficaTO padreTO=(DivisiongeograficaTO)collection.iterator().next();
//					divisiongeograficaTO2.setNpcodigopadre(padreTO.getCodigo());
//					divisiongeograficaTO2.setNpnombrepadre(padreTO.getNombre());
//					divisiongeograficaTO2.setNptipopadre(padreTO.getTipo());
//				}
//			}
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", Integer.valueOf(resultado.size()).toString());
			log.println("totalresultado: " + resultado.size());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,divisiongeograficaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las Divisiones geograficas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaDivisionesgeograficasPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		DivisiongeograficaTO divisiongeograficaTO=new DivisiongeograficaTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=200;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			divisiongeograficaTO.setFirstResult(primero);
			divisiongeograficaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				divisiongeograficaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				divisiongeograficaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				divisiongeograficaTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				divisiongeograficaTO.setEstado(parameters.get("estado"));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				divisiongeograficaTO.setTipo(parameters.get("tipo"));
			if(parameters.get("padreid")!=null && !parameters.get("padreid").equals(""))
				divisiongeograficaTO.setDivisiongeograficapadreid(Long.valueOf(parameters.get("padreid")));
			SearchResultTO<DivisiongeograficaTO> resultado=UtilSession.adminsitracionServicio.transObtenerDivisiongeograficaPaginado(divisiongeograficaTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + resultado.getClass().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,divisiongeograficaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los unida medida paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadmedidaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadmedidaTO unidadmedidaTO=new UnidadmedidaTO();
		GrupomedidaTO grupomedidaTO=new GrupomedidaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			unidadmedidaTO.setFirstResult(primero);
//			unidadmedidaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadmedidaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadmedidaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				unidadmedidaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				unidadmedidaTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				unidadmedidaTO.setEstado(parameters.get("estado"));
			if(parameters.get("codigogrupo")!=null && !parameters.get("codigogrupo").equals(""))
				grupomedidaTO.setCodigo(parameters.get("codigogrupo"));
			if(parameters.get("nombregrupo")!=null && !parameters.get("nombregrupo").equals(""))
				grupomedidaTO.setNombre(parameters.get("nombregrupo"));

			unidadmedidaTO.setGrupomedida(grupomedidaTO);
//			SearchResultTO<UnidadmedidaTO> resultado=UtilSession.adminsitracionServicio.transObtenerUnidadmedidaPaginado(unidadmedidaTO);
			Collection<UnidadmedidaTO> resultado=UtilSession.adminsitracionServicio.transObtenerUnidadmedida(unidadmedidaTO);
			//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			//HashMap<String, String>  resultado.size()=new HashMap<String, String>();
			//resultado.size().put("valor", resultado.getCountResults().toString());
			//log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,unidadmedidaTO.getJsonConfigComun()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los parametros paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaParametrosPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ParametroTO parametroTO=new ParametroTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			parametroTO.setFirstResult(primero);
//			parametroTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				parametroTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				parametroTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				parametroTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				parametroTO.setEstado(parameters.get("estado"));
//			SearchResultTO<ParametroTO> resultado=UtilSession.adminsitracionServicio.transObtenerParametroPaginado(parametroTO);
			Collection<ParametroTO> resultado=UtilSession.adminsitracionServicio.transObtenerParametrol(parametroTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,parametroTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los consecutivo paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaConsecutivoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ConsecutivoTO consecutivoTO=new ConsecutivoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			consecutivoTO.setFirstResult(primero);
//			consecutivoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				consecutivoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				consecutivoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("prefijo")!=null && !parameters.get("prefijo").equals(""))
				consecutivoTO.setPrefijo(parameters.get("prefijo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				consecutivoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				consecutivoTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				consecutivoTO.setConsecutivoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<ConsecutivoTO> resultado=UtilSession.adminsitracionServicio.transObtenerConsecutivoPaginado(consecutivoTO);
			Collection<ConsecutivoTO> resultado=UtilSession.adminsitracionServicio.transObtenerConsecutivo(consecutivoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,consecutivoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los identificacion paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaIdentificacionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TipoidentificacionTO tipoidentificacionTO=new TipoidentificacionTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tipoidentificacionTO.setFirstResult(primero);
//			tipoidentificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tipoidentificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tipoidentificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				tipoidentificacionTO.setNombre(parameters.get("nombre"));
//			SearchResultTO<TipoidentificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificacionPaginado(tipoidentificacionTO);
			Collection<TipoidentificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificacion(tipoidentificacionTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipoidentificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los fuente financiamiento paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaFuentefinanciamientoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		FuentefinanciamientoTO fuentefinanciamientoTO=new FuentefinanciamientoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			fuentefinanciamientoTO.setFirstResult(primero);
//			fuentefinanciamientoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				fuentefinanciamientoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				fuentefinanciamientoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				fuentefinanciamientoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				fuentefinanciamientoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				fuentefinanciamientoTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				fuentefinanciamientoTO.setFuentefinanejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<FuentefinanciamientoTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuentefinanciamientoPaginado(fuentefinanciamientoTO);
			Collection<FuentefinanciamientoTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuentefinanciamiento(fuentefinanciamientoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,fuentefinanciamientoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los organismo paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrganismoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		OrganismoTO organismoTO=new OrganismoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			organismoTO.setFirstResult(primero);
//			organismoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				organismoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				organismoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				organismoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				organismoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				organismoTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				organismoTO.setOrganismoejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<OrganismoTO> resultado=UtilSession.adminsitracionServicio.transObtenerOrganismoPaginado(organismoTO);
			Collection<OrganismoTO> resultado=UtilSession.adminsitracionServicio.transObtenerOrganismo(organismoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,organismoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los organismoprestamo paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaOrganismoprestamoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		OrganismoprestamoTO organismoprestamoTO=new OrganismoprestamoTO();
		OrganismoTO organismoTO=new OrganismoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			organismoprestamoTO.setFirstResult(primero);
//			organismoprestamoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				organismoprestamoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				organismoprestamoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				organismoprestamoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				organismoprestamoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				organismoprestamoTO.setEstado(parameters.get("estado"));
			if(parameters.get("npcodigoorganismo")!=null && !parameters.get("npcodigoorganismo").equals(""))
				organismoTO.setCodigo(parameters.get("npcodigoorganismo"));
			if(parameters.get("npnombreorganismo")!=null && !parameters.get("npnombreorganismo").equals(""))
				organismoTO.setNombre(parameters.get("npnombreorganismo"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				organismoTO.setOrganismoejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			organismoprestamoTO.setOrganismo(organismoTO);
//			SearchResultTO<OrganismoprestamoTO> resultado=UtilSession.planificacionServicio.transObtenerOrganismoprestamoPaginado(organismoprestamoTO);
			Collection<OrganismoprestamoTO> resultado=UtilSession.planificacionServicio.transObtenerOrganismoprestamo(organismoprestamoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,organismoprestamoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los procedimiento paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaProcedimientoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ProcedimientoTO procedimientoTO=new ProcedimientoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			procedimientoTO.setFirstResult(primero);
//			procedimientoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				procedimientoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				procedimientoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				procedimientoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				procedimientoTO.setActivo(Integer.valueOf(parameters.get("estado")));
//			SearchResultTO<ProcedimientoTO> resultado=UtilSession.adminsitracionServicio.transObtenerProcedimientoPaginado(procedimientoTO);
			Collection<ProcedimientoTO> resultado=UtilSession.adminsitracionServicio.transObtenerProcedimiento(procedimientoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,procedimientoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los tiporegimen paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaTiporegimenPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TiporegimenTO tiporegimenTO=new TiporegimenTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tiporegimenTO.setFirstResult(primero);
//			tiporegimenTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tiporegimenTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tiporegimenTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				tiporegimenTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				tiporegimenTO.setActivo(Integer.valueOf(parameters.get("estado")));
//			SearchResultTO<TiporegimenTO> resultado=UtilSession.adminsitracionServicio.transObtenerTiporegimenPaginado(tiporegimenTO);
			Collection<TiporegimenTO> resultado=UtilSession.adminsitracionServicio.transObtenerTiporegimen(tiporegimenTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tiporegimenTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los tipoproducto paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaTipoproductoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TipoproductoTO tipoproductoTO=new TipoproductoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tipoproductoTO.setFirstResult(primero);
//			tipoproductoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tipoproductoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tipoproductoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				tipoproductoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				tipoproductoTO.setActivo(Integer.valueOf(parameters.get("estado")));
//			SearchResultTO<TipoproductoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoproductoPaginado(tipoproductoTO);
			Collection<TipoproductoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoproducto(tipoproductoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipoproductoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las obras paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaObraPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ObraTO obraTO=new ObraTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			obraTO.setFirstResult(primero);
//			obraTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				obraTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				obraTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				obraTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				obraTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				obraTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				obraTO.setObraejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<ObraTO> resultado=UtilSession.adminsitracionServicio.transObtenerObraPaginado(obraTO);
			Collection<ObraTO> resultado=UtilSession.adminsitracionServicio.transObtenerObra(obraTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,obraTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los items paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaItemPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ItemTO itemTO=new ItemTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			itemTO.setFirstResult(primero);
//			itemTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				itemTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				itemTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				itemTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				itemTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				itemTO.setEstado(parameters.get("estado"));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				itemTO.setTipo(parameters.get("tipo"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				itemTO.setItemejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<ItemTO> resultado=UtilSession.adminsitracionServicio.transObtenerItemPaginado(itemTO);
			Collection<ItemTO> resultado=UtilSession.adminsitracionServicio.transObtenerItem(itemTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,itemTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los subitem paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaSubitemPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		SubitemTO subitemTO=new SubitemTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo,"nombre"};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			subitemTO.setFirstResult(primero);
//			subitemTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				subitemTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				subitemTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				subitemTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				subitemTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				subitemTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals("")){
				ItemTO itemTO=new ItemTO();
				itemTO.setItemejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
				subitemTO.setItem(itemTO);
			}	
			if(parameters.get("itemunidadid")!=null && !parameters.get("itemunidadid").equals("")){
				log.println("se envio el itemunidad");
				//traigo el item
				log.println("va a consultar el item unidad: " + parameters.get("itemunidadid"));
				//ItemunidadTO itemunidadTO=UtilSession.planificacionServicio.transObtenerItemunidadTO(Long.valueOf(parameters.get("itemunidadid")));
				//log.println("item: " + itemunidadTO.getItemunidaditemid());
				subitemTO.setSubitemitemid(Long.valueOf(parameters.get("itemunidadid")));
			}
			subitemTO.setUnidadmedida(new UnidadmedidaTO());
			subitemTO.setItem(new ItemTO());
//			SearchResultTO<SubitemTO> resultado=UtilSession.adminsitracionServicio.transObtenerSubitemPaginado(subitemTO);
			Collection<SubitemTO> resultado=UtilSession.adminsitracionServicio.transObtenerSubitem(subitemTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,subitemTO.getJsonConfigedit()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los grupo medida paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaGrupomedidaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		GrupomedidaTO grupomedidaTO=new GrupomedidaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			grupomedidaTO.setFirstResult(primero);
//			grupomedidaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				grupomedidaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				grupomedidaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				grupomedidaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				grupomedidaTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				grupomedidaTO.setEstado(parameters.get("estado"));
//			SearchResultTO<GrupomedidaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGrupomedidaPaginado(grupomedidaTO);
			Collection<GrupomedidaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGrupomedida(grupomedidaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,grupomedidaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las clases registro paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaClaseregistroPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ClaseregistroTO claseregistroTO=new ClaseregistroTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			claseregistroTO.setFirstResult(primero);
//			claseregistroTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				claseregistroTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				claseregistroTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				claseregistroTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				claseregistroTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				claseregistroTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				claseregistroTO.setClaseregistroejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<ClaseregistroTO> resultado=UtilSession.adminsitracionServicio.transObtenerClaseregistroPaginado(claseregistroTO);
			Collection<ClaseregistroTO> resultado=UtilSession.adminsitracionServicio.transObtenerClaseregistro(claseregistroTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,claseregistroTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	
	
	/**
	* Metodo que consulta las clases registro modificacion paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaClasemodificacionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ClaseregistroclasemodificacionTO claseregistroclasemodificacionTO=new ClaseregistroclasemodificacionTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			claseregistroclasemodificacionTO.setFirstResult(primero);
//			claseregistroclasemodificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				claseregistroclasemodificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				claseregistroclasemodificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				claseregistroclasemodificacionTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				claseregistroclasemodificacionTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				claseregistroclasemodificacionTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				claseregistroclasemodificacionTO.setClaseregistrocmejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			claseregistroclasemodificacionTO.setClaseregistro(new ClaseregistroTO());
//			SearchResultTO<ClaseregistroclasemodificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerClaseregistroclasemodificacionPaginado(claseregistroclasemodificacionTO);
			Collection<ClaseregistroclasemodificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerClaseregistroclasemodificacion(claseregistroclasemodificacionTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,claseregistroclasemodificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los tipos de documentoss paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaTipodocumentoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TipodocumentoTO tipodocumentoTO=new TipodocumentoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tipodocumentoTO.setFirstResult(primero);
//			tipodocumentoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tipodocumentoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tipodocumentoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				tipodocumentoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				tipodocumentoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				tipodocumentoTO.setEstado(parameters.get("estado"));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals(""))
				tipodocumentoTO.setTipodocumentoejerfiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
//			SearchResultTO<TipodocumentoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipodocumentoPaginado(tipodocumentoTO);
			Collection<TipodocumentoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipodocumento(tipodocumentoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipodocumentoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los tipos de identificacion paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaTipoidentificaciontipoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TipoidentificacionTO tipoidentificacionTO=new TipoidentificacionTO();
		TipoidentificaciontipoTO tipoidentificaciontipoTO=new TipoidentificaciontipoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="tipoidentificacion.nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tipoidentificaciontipoTO.setFirstResult(primero);
//			tipoidentificaciontipoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tipoidentificaciontipoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tipoidentificaciontipoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				tipoidentificacionTO.setNombre(parameters.get("nombre"));
			if(parameters.get("identificacionid")!=null && !parameters.get("identificacionid").equals(""))
				tipoidentificaciontipoTO.getId().setIdentificacionid(Long.valueOf(parameters.get("nombre")));
			tipoidentificaciontipoTO.setTipoidentificacion(tipoidentificacionTO);
//			SearchResultTO<TipoidentificaciontipoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificaciontipoPaginado(tipoidentificaciontipoTO);
			Collection<TipoidentificaciontipoTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificaciontipo(tipoidentificaciontipoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipoidentificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los tipos de identificacion tipo paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaTipoidentificacionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		TipoidentificacionTO tipoidentificacionTO=new TipoidentificacionTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			tipoidentificacionTO.setFirstResult(primero);
//			tipoidentificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				tipoidentificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				tipoidentificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
//			SearchResultTO<TipoidentificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificacionPaginado(tipoidentificacionTO);
			Collection<TipoidentificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerTipoidentificacion(tipoidentificacionTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipoidentificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los parametro indicador paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaParametroindicadorPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ParametroindicadorTO parametroindicadorTO=new ParametroindicadorTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			parametroindicadorTO.setFirstResult(primero);
//			parametroindicadorTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				parametroindicadorTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				parametroindicadorTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				parametroindicadorTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				parametroindicadorTO.setNombre(parameters.get("nombre"));
//			SearchResultTO<ParametroindicadorTO> resultado=UtilSession.adminsitracionServicio.transObtenerParametroindicadorPaginado(parametroindicadorTO);
			Collection<ParametroindicadorTO> resultado=UtilSession.adminsitracionServicio.transObtenerParametroindicador(parametroindicadorTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,parametroindicadorTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las fuerzas paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaFuerzaPaginado(Map<String, String> parameters,JSONObject jsonObject,String tipo) throws MyException {
		String campo="";
		FuerzaTO fuerzaTO=new FuerzaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			fuerzaTO.setFirstResult(primero);
//			fuerzaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				fuerzaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				fuerzaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				fuerzaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				fuerzaTO.setNombre(parameters.get("nombre"));
			if(parameters.get("sigla")!=null && !parameters.get("sigla").equals(""))
				fuerzaTO.setNombre(parameters.get("sigla"));
			if(tipo.equals("busquedafuerza"))
				fuerzaTO.setEstado(MensajesAplicacion.getString("estado.activo"));
//			SearchResultTO<FuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuerzaPaginado(fuerzaTO);
			Collection<FuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuerza(fuerzaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,fuerzaTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	

	/**
	* Metodo que consulta los socio negocio paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaSocionegocioPaginado(Map<String, String> parameters,JSONObject jsonObject,String tipo) throws MyException {
		String campo="";
		SocionegocioTO socionegocioTO=new SocionegocioTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			socionegocioTO.setFirstResult(primero);
//			socionegocioTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				socionegocioTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				socionegocioTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				socionegocioTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				socionegocioTO.setNombrecomercial(parameters.get("nombre"));
//			if(parameters.get("primernombre")!=null && !parameters.get("primernombre").equals(""))
//				socionegocioTO.setPrimernombre(parameters.get("primernombre"));
//			if(parameters.get("primerapellido")!=null && !parameters.get("primerapellido").equals(""))
//				socionegocioTO.setPrimerapellido(parameters.get("primerapellido"));
//			if(parameters.get("segundonombre")!=null && !parameters.get("segundonombre").equals(""))
//				socionegocioTO.setSegundonombre(parameters.get("segundonombre"));
			if(parameters.get("nombremostrado")!=null && !parameters.get("nombremostrado").equals(""))
				socionegocioTO.setNombremostrado(parameters.get("nombremostrado"));
			if(parameters.get("emptipo")!=null && !parameters.get("emptipo").equals(""))
				socionegocioTO.setEmptipo(parameters.get("emptipo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				socionegocioTO.setEstado(parameters.get("estado"));
			if(tipo.equals("busquedasocionegocio"))
				socionegocioTO.setEstado(MensajesAplicacion.getString("estado.activo"));
			if(tipo.equals("empleado"))
				socionegocioTO.setEsempleado(1);
			if(tipo.equals("socionegocio"))
				socionegocioTO.setEsempleado(null);
//			SearchResultTO<SocionegocioTO> resultado=UtilSession.adminsitracionServicio.transObtenerSocionegocioPaginado(socionegocioTO);
			Collection<SocionegocioTO> resultado=UtilSession.adminsitracionServicio.transObtenerSocionegocio(socionegocioTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			if(tipo.equals("socionegocio"))
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,socionegocioTO.getJsonConfig()));
			else if(tipo.equals("empleado"))
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,socionegocioTO.getJsonConfigEmpleado()));
			else
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,socionegocioTO.getJsonConfigBusqueda()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	


	
	/**
	* Metodo que consulta los grado paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaGradoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		GradoTO gradoTO=new GradoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			gradoTO.setFirstResult(primero);
//			gradoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				gradoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				gradoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				gradoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				gradoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				gradoTO.setEstado(parameters.get("estado"));
			if(parameters.get("sigla")!=null && !parameters.get("sigla").equals(""))
				gradoTO.setSigla(parameters.get("sigla"));
			gradoTO.setGrupo(new GrupoTO());
//			SearchResultTO<GradoTO> resultado=UtilSession.adminsitracionServicio.transObtenerGradoPaginado(gradoTO);
			Collection<GradoTO> resultado=UtilSession.adminsitracionServicio.transObtenerGrado(gradoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,gradoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los clasificacion paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaClasificacionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ClasificacionTO clasificacionTO=new ClasificacionTO();
		try{
			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			clasificacionTO.setFirstResult(primero);
//			clasificacionTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				clasificacionTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				clasificacionTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				clasificacionTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				clasificacionTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				clasificacionTO.setEstado(parameters.get("estado"));
//			SearchResultTO<ClasificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerClasificacionPaginado(clasificacionTO);
			Collection<ClasificacionTO> resultado=UtilSession.adminsitracionServicio.transObtenerClasificacion(clasificacionTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,clasificacionTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	
	/**
	* Metodo que consulta los grupos paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaGrupoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		GrupoTO grupoTO=new GrupoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			grupoTO.setFirstResult(primero);
//			grupoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				grupoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				grupoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				grupoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				grupoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("sigla")!=null && !parameters.get("sigla").equals(""))
				grupoTO.setSigla(parameters.get("sigla"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				grupoTO.setEstado(parameters.get("estado"));
//			SearchResultTO<GrupoTO> resultado=UtilSession.adminsitracionServicio.transObtenerGrupoPaginado(grupoTO);
			Collection<GrupoTO> resultado=UtilSession.adminsitracionServicio.transObtenerGrupo(grupoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,grupoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	
	/**
	* Metodo que consulta las fuerzas paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaFuerzaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		FuerzaTO fuerzaTO=new FuerzaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			fuerzaTO.setFirstResult(primero);
//			fuerzaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				fuerzaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				fuerzaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				fuerzaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				fuerzaTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				fuerzaTO.setEstado(parameters.get("estado"));
//			SearchResultTO<FuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuerzaPaginado(fuerzaTO);
			Collection<FuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerFuerza(fuerzaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,fuerzaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los gradosfuerza paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaGradofuerzaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		GradofuerzaTO gradofuerzaTO=new GradofuerzaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			gradofuerzaTO.setFirstResult(primero);
//			gradofuerzaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				gradofuerzaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				gradofuerzaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				gradofuerzaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				gradofuerzaTO.setEstado(parameters.get("estado"));
			FuerzaTO fuerzaTO=new FuerzaTO();
			GradoTO gradoTO=new GradoTO();
			GrupoTO grupoTO=new GrupoTO();
			if(parameters.get("npnombregrado")!=null && !parameters.get("npnombregrado").equals(""))
				gradoTO.setNombre(parameters.get("npnombregrado"));
			if(parameters.get("npgrupo")!=null && !parameters.get("npgrupo").equals(""))
				grupoTO.setNombre(parameters.get("npgrupo"));
			if(parameters.get("npnombrefuerza")!=null && !parameters.get("npnombrefuerza").equals(""))
				fuerzaTO.setNombre(parameters.get("npnombrefuerza"));
			gradofuerzaTO.setFuerza(fuerzaTO);
			gradoTO.setGrupo(grupoTO);
			gradofuerzaTO.setGrado(gradoTO);
//			SearchResultTO<GradofuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGradofuerzaPaginado(gradofuerzaTO);
			Collection<GradofuerzaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGradofuerza(gradofuerzaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,gradofuerzaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta especialidades paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaEspecialidadPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		EspecialidadTO especialidadTO=new EspecialidadTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			especialidadTO.setFirstResult(primero);
//			especialidadTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				especialidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				especialidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				especialidadTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				especialidadTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				especialidadTO.setEstado(parameters.get("estado"));
			especialidadTO.setFuerza(new FuerzaTO());
//			SearchResultTO<EspecialidadTO> resultado=UtilSession.adminsitracionServicio.transObtenerEspecialidadPaginado(especialidadTO);
			Collection<EspecialidadTO> resultado=UtilSession.adminsitracionServicio.transObtenerEspecialidad(especialidadTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,especialidadTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los cargos paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaCargoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		CargoTO cargoTO=new CargoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			cargoTO.setFirstResult(primero);
//			cargoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				cargoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				cargoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				cargoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				cargoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				cargoTO.setEstado(parameters.get("estado"));
//			SearchResultTO<CargoTO> resultado=UtilSession.adminsitracionServicio.transObtenerCargoPaginado(cargoTO);
			Collection<CargoTO> resultado=UtilSession.adminsitracionServicio.transObtenerCargo(cargoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,cargoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los escalarmu paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaEscalarmuPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		EscalarmuTO escalarmuTO=new EscalarmuTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			escalarmuTO.setFirstResult(primero);
//			escalarmuTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				escalarmuTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				escalarmuTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				escalarmuTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				escalarmuTO.setEstado(parameters.get("estado"));
			if(parameters.get("grupoocupacional")!=null && !parameters.get("grupoocupacional").equals(""))
				escalarmuTO.setGrupoocupacional(parameters.get("grupoocupacional"));
			if(parameters.get("gradocodigo")!=null && !parameters.get("gradocodigo").equals(""))
				escalarmuTO.setGradocodigo(parameters.get("gradocodigo"));
//			SearchResultTO<EscalarmuTO> resultado=UtilSession.adminsitracionServicio.transObtenerEscalarmuPaginado(escalarmuTO);
			Collection<EscalarmuTO> resultado=UtilSession.adminsitracionServicio.transObtenerEscalarmu(escalarmuTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,escalarmuTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los gradoescala paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaGradoescalaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		GradoescalaTO gradoescalaTO=new GradoescalaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			gradoescalaTO.setFirstResult(primero);
//			gradoescalaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				gradoescalaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				gradoescalaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				gradoescalaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				gradoescalaTO.setEstado(parameters.get("estado"));
			if(parameters.get("gradofuerza")!=null && !parameters.get("gradofuerza").equals("")){
				gradoescalaTO.setGegradofuerzaid(Long.valueOf(parameters.get("gradofuerza")));
			}
			if(parameters.get("geescalarmuid")!=null && !parameters.get("geescalarmuid").equals("")){
				gradoescalaTO.setGeescalarmuid(Long.valueOf(parameters.get("geescalarmuid")));
			}
			FuerzaTO fuerzaTO=new FuerzaTO();
			GradoTO gradoTO=new GradoTO();
			GradofuerzaTO gradofuerzaTO=new GradofuerzaTO();
			EscalarmuTO escalarmuTO=new EscalarmuTO();
			if(parameters.get("npnombregrado")!=null && !parameters.get("npnombregrado").equals(""))
				gradoTO.setNombre(parameters.get("npnombregrado"));
			if(parameters.get("npnombrefuerza")!=null && !parameters.get("npnombrefuerza").equals(""))
				fuerzaTO.setNombre(parameters.get("npnombrefuerza"));
			if(parameters.get("npgrupoocupacional")!=null && !parameters.get("npgrupoocupacional").equals(""))
				escalarmuTO.setGrupoocupacional(parameters.get("npgrupoocupacional"));
			gradofuerzaTO.setFuerza(fuerzaTO);
			gradofuerzaTO.setGrado(gradoTO);
			gradoescalaTO.setGradofuerza(gradofuerzaTO);
			gradoescalaTO.setEscalarmu(escalarmuTO);
//			SearchResultTO<GradoescalaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGradoescalaPaginado(gradoescalaTO);
			Collection<GradoescalaTO> resultado=UtilSession.adminsitracionServicio.transObtenerGradoescala(gradoescalaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,gradoescalaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los cargoescala paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaCargoescalaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		CargoescalaTO cargoescalaTO=new CargoescalaTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			cargoescalaTO.setFirstResult(primero);
//			cargoescalaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				cargoescalaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				cargoescalaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				cargoescalaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				cargoescalaTO.setEstado(parameters.get("estado"));
			if(parameters.get("cecargoid")!=null && !parameters.get("cecargoid").equals("")){
				cargoescalaTO.setCecargoid(Long.valueOf(parameters.get("cecargoid")));
			}
			if(parameters.get("ceescalarmuid")!=null && !parameters.get("ceescalarmuid").equals("")){
				cargoescalaTO.setCeescalarmuid(Long.valueOf(parameters.get("ceescalarmuid")));
			}
			CargoTO cargoTO=new CargoTO();
			EscalarmuTO escalarmuTO=new EscalarmuTO();
			if(parameters.get("npnombrecargo")!=null && !parameters.get("npnombrecargo").equals(""))
				cargoTO.setNombre(parameters.get("npnombrecargo"));
			if(parameters.get("npgrupoocupacional")!=null && !parameters.get("npgrupoocupacional").equals(""))
				escalarmuTO.setGrupoocupacional(parameters.get("npgrupoocupacional"));
			cargoescalaTO.setCargo(cargoTO);
			cargoescalaTO.setEscalarmu(escalarmuTO);
//			SearchResultTO<CargoescalaTO> resultado=UtilSession.adminsitracionServicio.transObtenerCargoescalaPaginado(cargoescalaTO);
			Collection<CargoescalaTO> resultado=UtilSession.adminsitracionServicio.transObtenerCargoescala(cargoescalaTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,cargoescalaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta los nivelorganico paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaNivelorganicoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		NivelorganicoTO nivelorganicoTO=new NivelorganicoTO();
		try{
//			int pagina=1;
//			if(parameters.get("pagina")!=null)		
//				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
//			int filas=20;
//			if(parameters.get("filas")!=null)
//				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
//			int primero=(pagina*filas)-filas;
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
//			nivelorganicoTO.setFirstResult(primero);
//			nivelorganicoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				nivelorganicoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				nivelorganicoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				nivelorganicoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				nivelorganicoTO.setEstado(parameters.get("estado"));
//			SearchResultTO<NivelorganicoTO> resultado=UtilSession.planificacionServicio.transObtenerNivelorganidoPaginado(nivelorganicoTO);
			Collection<NivelorganicoTO> resultado=UtilSession.planificacionServicio.transObtenerNivelorganido(nivelorganicoTO);
//			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
//			HashMap<String, String>  resultado.size()=new HashMap<String, String>();
//			resultado.size().put("valor", resultado.getCountResults().toString());
//			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,nivelorganicoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(resultado.size()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}


}
