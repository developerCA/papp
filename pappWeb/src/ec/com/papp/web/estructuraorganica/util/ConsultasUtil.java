package ec.com.papp.web.estructuraorganica.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.tools.commons.to.OrderBy;
import org.hibernate.tools.commons.to.SearchResultTO;

import ec.com.papp.administracion.to.EjerciciofiscalTO;
import ec.com.papp.administracion.to.EmpleadoTO;
import ec.com.papp.administracion.to.SocionegocioTO;
import ec.com.papp.estructuraorganica.to.EstructuraorganicaTO;
import ec.com.papp.estructuraorganica.to.InstitucionTO;
import ec.com.papp.estructuraorganica.to.InstitucionentidadTO;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolplazaTO;
import ec.com.papp.estructuraorganica.to.UnidadarbolplazaempleadoTO;
import ec.com.papp.estructuraorganica.to.UnidadinstTO;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.Log;

public class ConsultasUtil {

	
	private static Log log = new Log(ConsultasUtil.class);
	
	/**
	* Metodo que consulta las  estructuras organicas paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaEstructuraorganicaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		EstructuraorganicaTO estructuraorganicaTO=new EstructuraorganicaTO();
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
			estructuraorganicaTO.setFirstResult(primero);
			estructuraorganicaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				estructuraorganicaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				estructuraorganicaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				estructuraorganicaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				estructuraorganicaTO.setEstado(parameters.get("estado"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				estructuraorganicaTO.setNombre(parameters.get("nombre"));
			if(parameters.get("version")!=null && !parameters.get("version").equals(""))
				estructuraorganicaTO.setVersion(Double.valueOf(parameters.get("version")));
			InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
			institucionentidadTO.setInstitucion(new InstitucionTO());
			estructuraorganicaTO.setInstitucionentidad(institucionentidadTO);
			SearchResultTO<EstructuraorganicaTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerEstructuraorganicaPaginado(estructuraorganicaTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),estructuraorganicaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	

	
	/**
	* Metodo que consulta las instituciones entidad paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaInstitucionentidadPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
		InstitucionTO institucionTO=new InstitucionTO();
		institucionTO.setEjerciciofiscal(new EjerciciofiscalTO());
		
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
			institucionentidadTO.setFirstResult(primero);
			institucionentidadTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				institucionentidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				institucionentidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				institucionentidadTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				institucionentidadTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				institucionentidadTO.setEstado(parameters.get("estado"));
			if(parameters.get("institucionentcantonid")!=null && !parameters.get("institucionentcantonid").equals(""))
				institucionentidadTO.setInstitucionentcantonid(Long.valueOf(parameters.get("institucionentcantonid")));
			if(parameters.get("institucionentpaisid")!=null && !parameters.get("institucionentpaisid").equals(""))
				institucionentidadTO.setInstitucionentpaisid(Long.valueOf(parameters.get("institucionentpaisid")));
			if(parameters.get("institucionentprovinciaid")!=null && !parameters.get("institucionentprovinciaid").equals(""))
				institucionentidadTO.setInstitucionentprovinciaid(Long.valueOf(parameters.get("institucionentprovinciaid")));
			if(parameters.get("institucionid")!=null && !parameters.get("institucionid").equals(""))
				institucionentidadTO.getId().setId(Long.valueOf(parameters.get("institucionid")));
			if(parameters.get("ejerciciofiscalid")!=null && !parameters.get("ejerciciofiscalid").equals("")){
				institucionTO.setInstitucionejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscalid")));
			}
			institucionentidadTO.setInstitucion(institucionTO);
			SearchResultTO<InstitucionentidadTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerInstitucionentidadPaginado(institucionentidadTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),institucionentidadTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta las instituciones entidad paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaInstitucionentidadReporte(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="institucion.codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			institucionentidadTO.setFirstResult(primero);
			institucionentidadTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				institucionentidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				institucionentidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				institucionentidadTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				institucionentidadTO.setNombre(parameters.get("nombre"));
			institucionentidadTO.setEstado("A");
			InstitucionTO institucionTO=new InstitucionTO();
			if(parameters.get("institucionejerciciofiscalid")!=null && !parameters.get("institucionejerciciofiscalid").equals(""))
				institucionTO.setInstitucionejerciciofiscalid(Long.valueOf(parameters.get("institucionejerciciofiscalid")));
			if(parameters.get("nombreinstitucion")!=null && !parameters.get("nombreinstitucion").equals(""))
				institucionTO.setNombre(parameters.get("nombreinstitucion"));
			if(parameters.get("codigoinstitucion")!=null && !parameters.get("codigoinstitucion").equals(""))
				institucionTO.setCodigo(parameters.get("codigoinstitucion"));
			institucionTO.setEstado("A");
			institucionentidadTO.setInstitucion(institucionTO);
			SearchResultTO<InstitucionentidadTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerInstitucionentidadPaginado(institucionentidadTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),institucionentidadTO.getJsonConfigComun()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidadarbolplaza paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadarbolplazaPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadarbolplazaTO unidadarbolplazaTO=new UnidadarbolplazaTO();
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
			unidadarbolplazaTO.setFirstResult(primero);
			unidadarbolplazaTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadarbolplazaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadarbolplazaTO.setOrderByField(OrderBy.orderAsc(orderBy));
//			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
//				unidadarbolplazaTO.setNombre(parameters.get("nombre"));
//			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
//				unidadarbolplazaTO.setEstado(parameters.get("estado"));
			SearchResultTO<UnidadarbolplazaTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplazaPaginado(unidadarbolplazaTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadarbolplazaTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidadarbolplazaempleado paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadarbolplazaempleadoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadarbolplazaempleadoTO unidadarbolplazaempleadoTO=new UnidadarbolplazaempleadoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="fechainicio";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			unidadarbolplazaempleadoTO.setFirstResult(primero);
			unidadarbolplazaempleadoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadarbolplazaempleadoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadarbolplazaempleadoTO.setOrderByField(OrderBy.orderAsc(orderBy));
//			if(parameters.get("prefijo")!=null && !parameters.get("prefijo").equals(""))
//				unidadarbolplazaempleadoTO.setPrefijo(parameters.get("prefijo"));
//			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
//				unidadarbolplazaempleadoTO.setNombre(parameters.get("nombre"));
			if(parameters.get("unidadarbolerganicaid")!=null && !parameters.get("unidadarbolerganicaid").equals("")){
				UnidadarbolTO unidadarbolTO=new UnidadarbolTO();
				unidadarbolTO.setUnidadarbolerganicaid(Long.valueOf(parameters.get("unidadarbolerganicaid")));
				UnidadarbolplazaTO unidadarbolplazaTO=new UnidadarbolplazaTO();
				unidadarbolplazaTO.setUnidadarbol(unidadarbolTO);
				unidadarbolplazaempleadoTO.setUnidadarbolplaza(unidadarbolplazaTO);
			}
			unidadarbolplazaempleadoTO.setSocionegocio(new SocionegocioTO());
			unidadarbolplazaempleadoTO.setUnidadarbolplaza(new UnidadarbolplazaTO());
			SearchResultTO<UnidadarbolplazaempleadoTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbolplazaempleadoPaginado(unidadarbolplazaempleadoTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadarbolplazaempleadoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
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

	public static JSONObject consultaSocionegocioPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		SocionegocioTO socionegocioTO=new SocionegocioTO();
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
			socionegocioTO.setFirstResult(primero);
			socionegocioTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				socionegocioTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				socionegocioTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				socionegocioTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				socionegocioTO.setNombrecomercial(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				socionegocioTO.setEstado(parameters.get("estado"));
//			if(parameters.get("primernombre")!=null && !parameters.get("primernombre").equals(""))
//				socionegocioTO.setPrimernombre(parameters.get("primernombre"));
//			if(parameters.get("primerapellido")!=null && !parameters.get("primerapellido").equals(""))
//				socionegocioTO.setPrimerapellido(parameters.get("primerapellido"));
//			if(parameters.get("segundonombre")!=null && !parameters.get("segundonombre").equals(""))
//				socionegocioTO.setSegundonombre(parameters.get("segundonombre"));
			if(parameters.get("nombremostrado")!=null && !parameters.get("nombremostrado").equals(""))
				socionegocioTO.setNombremostrado(parameters.get("nombremostrado"));
			SearchResultTO<SocionegocioTO> resultado=UtilSession.adminsitracionServicio.transObtenerSocionegocioPaginado(socionegocioTO);	
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),socionegocioTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
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

	public static JSONObject consultaEmpleadoPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		EmpleadoTO empleadoTO=new EmpleadoTO();
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
			empleadoTO.setFirstResult(primero);
			empleadoTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				empleadoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				empleadoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				empleadoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				empleadoTO.setNombremostrado(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				empleadoTO.setEstado(parameters.get("estado"));
//			if(parameters.get("primernombre")!=null && !parameters.get("primernombre").equals(""))
//				socionegocioTO.setPrimernombre(parameters.get("primernombre"));
//			if(parameters.get("primerapellido")!=null && !parameters.get("primerapellido").equals(""))
//				socionegocioTO.setPrimerapellido(parameters.get("primerapellido"));
//			if(parameters.get("segundonombre")!=null && !parameters.get("segundonombre").equals(""))
//				socionegocioTO.setSegundonombre(parameters.get("segundonombre"));
			SearchResultTO<EmpleadoTO> resultado=UtilSession.adminsitracionServicio.transObtenerEmpleadoPaginado(empleadoTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),empleadoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidadinstitucion paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadinstitucionPaginado(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadinstTO unidadinstTO=new UnidadinstTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="institucionentidad.id.entid";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			unidadinstTO.setFirstResult(primero);
			unidadinstTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadinstTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadinstTO.setOrderByField(OrderBy.orderAsc(orderBy));
//			if(parameters.get("prefijo")!=null && !parameters.get("prefijo").equals(""))
//				unidadarbolplazaempleadoTO.setPrefijo(parameters.get("prefijo"));
//			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
//				unidadarbolplazaempleadoTO.setNombre(parameters.get("nombre"));
//			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
//				unidadarbolplazaempleadoTO.setEstado(parameters.get("estado"));
			unidadinstTO.getId().setUnidad(Long.valueOf(parameters.get("unidadcodigo")));
			unidadinstTO.setUnidadTO(new UnidadTO());
			InstitucionentidadTO institucionentidadTO=new InstitucionentidadTO();
			institucionentidadTO.setInstitucion(new InstitucionTO());
			unidadinstTO.setInstitucionentidad(institucionentidadTO);
			SearchResultTO<UnidadinstTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadinstPaginado(unidadinstTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadinstTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidad paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadPaginado(Map<String, String> parameters,JSONObject jsonObject,String tipo) throws MyException {
		String campo="";
		UnidadTO unidadTO=new UnidadTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="codigopresup";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			unidadTO.setFirstResult(primero);
			unidadTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				unidadTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				unidadTO.setEstado(parameters.get("estado"));
			if(parameters.get("codigopresup")!=null && !parameters.get("codigopresup").equals(""))
				unidadTO.setCodigopresup(parameters.get("codigopresup"));
			unidadTO.setInstitucion(new InstitucionTO());
			unidadTO.setInstitucionentidad(new InstitucionentidadTO());
			SearchResultTO<UnidadTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadPaginado(unidadTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			if(tipo.equals("unidad")){
				log.println("entra por config");
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadTO.getJsonConfig()));
			}
			else{
				log.println("entra por configbusqueda");
				jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadTO.getJsonConfigBusqueda()));
			}
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidad paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadReporte(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadTO unidadTO=new UnidadTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			campo="codigopresup";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			unidadTO.setFirstResult(primero);
			unidadTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				unidadTO.setNombre(parameters.get("nombre"));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				unidadTO.setEstado(parameters.get("estado"));
			if(parameters.get("codigopresup")!=null && !parameters.get("codigopresup").equals(""))
				unidadTO.setCodigopresup(parameters.get("codigopresup"));
			SearchResultTO<UnidadTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadPaginado(unidadTO);
			long totalRegistrosPagina=(resultado.getCountResults()/filas)+1;
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			log.println("totalresultado: " + totalRegistrosPagina);
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),unidadTO.getJsonConfigReporte()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta unidadarbol paginados y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUnidadaarboarbol(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UnidadarbolTO unidadarbolTO=new UnidadarbolTO();
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
			unidadarbolTO.setFirstResult(primero);
			unidadarbolTO.setMaxResults(filas);
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadarbolTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadarbolTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				unidadarbolTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("unidadarbolnorganid")!=null && !parameters.get("unidadarbolnorganid").equals(""))
				unidadarbolTO.setUnidadarbolnorganid(Long.valueOf(parameters.get("unidadarbolnorganid")));
			if(parameters.get("unidadarbolunidadid")!=null && !parameters.get("unidadarbolunidadid").equals(""))
				unidadarbolTO.setUnidadarbolunidadid(Long.valueOf(parameters.get("unidadarbolunidadid")));
			if(parameters.get("unidadarbolerganicaid")!=null && !parameters.get("unidadarbolerganicaid").equals(""))
				unidadarbolTO.setUnidadarbolerganicaid(Long.valueOf(parameters.get("unidadarbolerganicaid")));
			if(parameters.get("unidadarbolpadreid")!=null && !parameters.get("unidadarbolerganicaid").equals(""))
				unidadarbolTO.setUnidadarbolpadreid(Long.valueOf(parameters.get("unidadarbolpadreid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				unidadarbolTO.setEstado(parameters.get("estado"));
			Collection<UnidadarbolTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUnidadarbol(unidadarbolTO);
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,unidadarbolTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	
}
