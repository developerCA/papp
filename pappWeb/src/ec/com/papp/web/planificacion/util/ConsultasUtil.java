package ec.com.papp.web.planificacion.util;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.tools.commons.to.OrderBy;
import org.hibernate.tools.commons.to.SearchResultTO;

import ec.com.papp.administracion.to.GrupomedidaTO;
import ec.com.papp.administracion.to.UnidadmedidaTO;
import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.planificacion.to.ActividadTO;
import ec.com.papp.planificacion.to.IndicadorTO;
import ec.com.papp.planificacion.to.IndicadormetodoTO;
import ec.com.papp.planificacion.to.NivelactividadTO;
import ec.com.papp.planificacion.to.ObjetivoTO;
import ec.com.papp.planificacion.to.PlannacionalTO;
import ec.com.papp.planificacion.to.ProgramaTO;
import ec.com.papp.planificacion.to.ProyectoTO;
import ec.com.papp.planificacion.to.SubactividadTO;
import ec.com.papp.planificacion.to.SubprogramaTO;
import ec.com.papp.planificacion.util.Matriz;
import ec.com.papp.planificacion.util.MatrizDetalle;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.papp.web.resource.MensajesWeb;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.Log;

public class ConsultasUtil {

	
	private static Log log = new Log(ConsultasUtil.class);
	
	/**
	* Metodo que consulta objetivos arma el json para mostrarlos en arbol
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaObjetivoarbol(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ObjetivoTO objetivoTO=new ObjetivoTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");

			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				objetivoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				objetivoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				objetivoTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("objetivoinstitucionid")!=null && !parameters.get("objetivoinstitucionid").equals(""))
				objetivoTO.setObjetivoinstitucionid(Long.valueOf(parameters.get("objetivoinstitucionid")));
			if(parameters.get("objetivoejerciciofiscalid")!=null && !parameters.get("objetivoejerciciofiscalid").equals(""))
				objetivoTO.setObjetivoejerciciofiscalid(Long.valueOf(parameters.get("objetivoejerciciofiscalid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				objetivoTO.setEstado(parameters.get("estado"));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				objetivoTO.setTipo(parameters.get("tipo"));
			if(parameters.get("objetivometaid")!=null && !parameters.get("objetivometaid").equals(""))
				objetivoTO.setObjetivometaid(Long.valueOf(parameters.get("objetivometaid")));
			Collection<ObjetivoTO> resultado=UtilSession.planificacionServicio.transObtenerObjetivoArbol(objetivoTO);
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,objetivoTO.getJsonConfigArbol()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta plannacional arma el json para mostrarlos en arbol
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPlannacionalarbol(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		PlannacionalTO plannacionalTO=new PlannacionalTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");

			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				plannacionalTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				plannacionalTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				plannacionalTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				plannacionalTO.setDescripcion(parameters.get("descripcion").toUpperCase());
			if(parameters.get("plannacionalejerciciofiscalid")!=null && !parameters.get("plannacionalejerciciofiscalid").equals(""))
				plannacionalTO.setPlannacionalejerfiscalid(Long.valueOf(parameters.get("plannacionalejerciciofiscalid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				plannacionalTO.setEstado(parameters.get("estado"));
			Collection<PlannacionalTO> resultado=UtilSession.planificacionServicio.transObtenerPlannacionalArbol(plannacionalTO);
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,plannacionalTO.getJsonConfigArbol()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta metas arma el json para mostrarlos en grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaMetas(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		//String campo="";
		PlannacionalTO plannacionalTO=new PlannacionalTO();
		try{
//			campo="codigo";
//			String[] columnas={campo};
//			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
//				campo=parameters.get("sidx");
//
//			String[] orderBy = columnas;
//			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
//				plannacionalTO.setOrderByField(OrderBy.orderDesc(orderBy));
//			else
//				plannacionalTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombrepadre")!=null && !parameters.get("nombrepadre").equals(""))
				plannacionalTO.setNombrepadre(parameters.get("nombrepadre"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				plannacionalTO.setDescripcion(parameters.get("descripcion").toUpperCase());
			if(parameters.get("plannacionalejerfiscalid")!=null && !parameters.get("plannacionalejerfiscalid").equals(""))
				plannacionalTO.setPlannacionalejerfiscalid(Long.valueOf(parameters.get("plannacionalejerfiscalid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				plannacionalTO.setEstado(parameters.get("estado"));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				plannacionalTO.setTipo(parameters.get("tipo"));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				plannacionalTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("codigopadre")!=null && !parameters.get("codigopadre").equals(""))
				plannacionalTO.setCodigopadre(parameters.get("codigopadre"));
			Collection<PlannacionalTO> resultado=UtilSession.planificacionServicio.transObtienePlannacionalmetas(plannacionalTO);
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,plannacionalTO.getJsonConfigMetas()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta indicador arma el json para mostrarlos en arbol
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaIndicadorarbol(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		IndicadorTO indicadorTO=new IndicadorTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");

			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				indicadorTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				indicadorTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				indicadorTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				indicadorTO.setDescripcion(parameters.get("nombre").toUpperCase());
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				indicadorTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("indicadorejerciciofiscalid")!=null && !parameters.get("indicadorejerciciofiscalid").equals(""))
				indicadorTO.setIndicadorejerciciofiscalid(Long.valueOf(parameters.get("indicadorejerciciofiscalid")));
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				indicadorTO.setEstado(parameters.get("estado"));
			Collection<IndicadorTO> resultado=UtilSession.planificacionServicio.transObtenerIndicadorArbol(indicadorTO);
			log.println("indicadores: " + resultado.size());
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,indicadorTO.getJsonConfigArbol()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta programas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPrograma(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ProgramaTO programaTO=new ProgramaTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				programaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				programaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				programaTO.setNombre(parameters.get("nombre").toUpperCase());
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				programaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("programaejerciciofiscalid")!=null && !parameters.get("programaejerciciofiscalid").equals(""))
				programaTO.setProgramaejerciciofiscalid(Long.valueOf(parameters.get("programaejerciciofiscalid")));
			Collection<ProgramaTO> resultado=UtilSession.planificacionServicio.transObtenerPrograma(programaTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,programaTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta objetivos arma el json para mostrarlos en grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaObjetivogrilla(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		ObjetivoTO objetivoTO=new ObjetivoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			objetivoTO.setFirstResult(primero);
			objetivoTO.setMaxResults(filas);
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				objetivoTO.setCodigo4(parameters.get("codigo"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				objetivoTO.setDescripcion3(parameters.get("descripcion").toUpperCase());
			if(parameters.get("estado")!=null && !parameters.get("estado").equals(""))
				objetivoTO.setEstado(parameters.get("estado"));
			if(parameters.get("objetivoejerciciofiscalid")!=null && !parameters.get("objetivoejerciciofiscalid").equals(""))
				objetivoTO.setObjetivoejerciciofiscalid(Long.valueOf(parameters.get("objetivoejerciciofiscalid")));
			Collection<ObjetivoTO> resultado=UtilSession.planificacionServicio.transObtieneObjetivoGrilla(objetivoTO);
			Integer totalRegistrosPagina=resultado.size();
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", totalRegistrosPagina.toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,objetivoTO.getJsonConfigGrilla()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta subprogramas y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaSubprograma(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		SubprogramaTO subprogramaTO=new SubprogramaTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				subprogramaTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				subprogramaTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("subprogramaejerciciofiscalid")!=null && !parameters.get("subprogramaejerciciofiscalid").equals(""))
				subprogramaTO.setSubprogramaejerciciofiscalid(Long.valueOf(parameters.get("subprogramaejerciciofiscalid")));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				subprogramaTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("padre")!=null && !parameters.get("padre").equals(""))
				subprogramaTO.setPadre(Long.valueOf(parameters.get("padre")));
			log.println("padre:  " + subprogramaTO.getPadre());
			Collection<SubprogramaTO> resultado=UtilSession.planificacionServicio.transObtenerSubprograma(subprogramaTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,subprogramaTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta proyecto y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaProyecto(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ProyectoTO proyectoTO=new ProyectoTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				proyectoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				proyectoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("proyectoejerfiscalid")!=null && !parameters.get("proyectoejerfiscalid").equals(""))
				proyectoTO.setProyectoejerciciofiscalid(Long.valueOf(parameters.get("proyectoejerfiscalid")));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				proyectoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("padre")!=null && !parameters.get("padre").equals(""))
				proyectoTO.setPadre(Long.valueOf(parameters.get("padre")));
			Collection<ProyectoTO> resultado=UtilSession.planificacionServicio.transObtenerProyecto(proyectoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,proyectoTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta la lista de proyectos para reporte y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaProyectoReporte(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ProyectoTO proyectoTO=new ProyectoTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				proyectoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				proyectoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("proyectoejerfiscalid")!=null && !parameters.get("proyectoejerfiscalid").equals(""))
				proyectoTO.setProyectoejerciciofiscalid(Long.valueOf(parameters.get("proyectoejerfiscalid")));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				proyectoTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				proyectoTO.setNombre(parameters.get("nombre").toUpperCase());
			if(parameters.get("npcodigoprograma")!=null && !parameters.get("npcodigoprograma").equals(""))
				proyectoTO.setNpcodigoprograma(parameters.get("npcodigoprograma").toUpperCase());
			if(parameters.get("npnombreprograma")!=null && !parameters.get("npnombreprograma").equals(""))
				proyectoTO.setNpnombreprograma(parameters.get("npnombreprograma").toUpperCase());
			if(parameters.get("npcodigosubprograma")!=null && !parameters.get("npcodigosubprograma").equals(""))
				proyectoTO.setNpcodigosubprograma(parameters.get("npcodigosubprograma"));
			if(parameters.get("npunidad")!=null && !parameters.get("npunidad").equals(""))
				proyectoTO.setNpunidad(Long.valueOf(parameters.get("npunidad")));
			if(parameters.get("npprogramaid")!=null && !parameters.get("npprogramaid").equals(""))
				proyectoTO.setNpprogramaid(Long.valueOf(parameters.get("npprogramaid")));
			Collection<ProyectoTO> resultado=UtilSession.planificacionServicio.transObtieneProyectoreporte(proyectoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,proyectoTO.getJsonConfigReporte()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta la lista de actividades para reporte y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaActividadReporte(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ActividadTO actividadTO=new ActividadTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				actividadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				actividadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("ejerciciofiscal")!=null && !parameters.get("ejerciciofiscal").equals(""))
				actividadTO.setActividadeejerciciofiscalid(Long.valueOf(parameters.get("ejerciciofiscal")));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				actividadTO.setDescripcion(parameters.get("nombre").toUpperCase());
			if(parameters.get("npunidad")!=null && !parameters.get("npunidad").equals(""))
				actividadTO.setNpunidad(Long.valueOf(parameters.get("npunidad")));
			if(parameters.get("npprogramaid")!=null && !parameters.get("npprogramaid").equals(""))
				actividadTO.setNpprogramaid(Long.valueOf(parameters.get("npprogramaid")));
			if(parameters.get("npproyectoid")!=null && !parameters.get("npproyectoid").equals(""))
				actividadTO.setNpproyectoid(Long.valueOf(parameters.get("npproyectoid")));
			Collection<ActividadTO> resultado=UtilSession.planificacionServicio.transObtieneActividadreporte(actividadTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,actividadTO.getJsonConfigReporte()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta indicador y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaBusquedaIndicador(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		IndicadorTO indicadorTO=new IndicadorTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=100;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			indicadorTO.setFirstResult(primero);
			indicadorTO.setMaxResults(filas);
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				indicadorTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				indicadorTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				indicadorTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				indicadorTO.setNombre(parameters.get("descripcion"));
			if(parameters.get("indicadorejerciciofiscalid")!=null && !parameters.get("indicadorejerciciofiscalid").equals(""))
				indicadorTO.setIndicadorejerciciofiscalid(Long.valueOf(parameters.get("indicadorejerciciofiscalid")));
			SearchResultTO<IndicadorTO> resultado=UtilSession.planificacionServicio.transObtenerIndicadorPaginado(indicadorTO);
			
//			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,tipoidentificacionTO.getJsonConfig()));
//			HashMap<String, Integer>  totalMap=new HashMap<String, Integer>();
//			totalMap.put("valor", resultado.size());
//			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));

			
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),indicadorTO.getJsonConfigArbol()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta actividad y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaActividad(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		ActividadTO actividadTO=new ActividadTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				actividadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				actividadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("actividadejerfiscalid")!=null && !parameters.get("actividadejerfiscalid").equals(""))
				actividadTO.setActividadeejerciciofiscalid(Long.valueOf(parameters.get("actividadejerfiscalid")));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				actividadTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("padre")!=null && !parameters.get("padre").equals(""))
				actividadTO.setPadre(Long.valueOf(parameters.get("padre")));
			Collection<ActividadTO> resultado=UtilSession.planificacionServicio.transObtenerActividad(actividadTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,actividadTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta actividad y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaSubactividad(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		SubactividadTO subactividadTO=new SubactividadTO();
		try{
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				subactividadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				subactividadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("subactividadejerfiscalid")!=null && !parameters.get("subactividadejerfiscalid").equals(""))
				subactividadTO.setSubactividadejerfiscalid(Long.valueOf(parameters.get("subactividadejerfiscalid")));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				subactividadTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("padre")!=null && !parameters.get("padre").equals(""))
				subactividadTO.setPadre(Long.valueOf(parameters.get("padre")));
			Collection<SubactividadTO> resultado=UtilSession.planificacionServicio.transObtenerSubactividad(subactividadTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,subactividadTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta indicador y arma el json para mostrarlos en la grilla para la busqueda desde actividad
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaBusquedaIndicadordesdeActividad(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		IndicadormetodoTO indicadormetodoTO=new IndicadormetodoTO();
		indicadormetodoTO.setEstado("A");
		IndicadorTO indicadorTO=new IndicadorTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			indicadorTO.setFirstResult(primero);
			indicadorTO.setMaxResults(filas);
			campo="codigo";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				indicadorTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				indicadorTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigo")!=null && !parameters.get("codigo").equals(""))
				indicadorTO.setCodigo(parameters.get("codigo"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				indicadorTO.setNombre(parameters.get("nombre").toUpperCase());
			if(parameters.get("indicadorejerciciofiscalid")!=null && !parameters.get("indicadorejerciciofiscalid").equals(""))
				indicadorTO.setIndicadorejerciciofiscalid(Long.valueOf(parameters.get("indicadorejerciciofiscalid")));
			UnidadmedidaTO unidadmedidaTO=new UnidadmedidaTO();
			unidadmedidaTO.setGrupomedida(new GrupomedidaTO());
			indicadorTO.setUnidadmedida(unidadmedidaTO);
			indicadormetodoTO.setIndicador(indicadorTO);
			SearchResultTO<IndicadormetodoTO> resultado=UtilSession.planificacionServicio.transObtenerIndicadormetodoPaginado(indicadormetodoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),indicadormetodoTO.getJsonConfigConsulta()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta la lista para la seccion planificacion
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPlanificacion(Map<String, String> parameters,JSONObject jsonObject,HttpServletRequest request, Principal principal) throws MyException {
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
			unidadTO.setFirstResult(primero);
			unidadTO.setMaxResults(filas);
			campo="codigopresup";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				unidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				unidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigopresup")!=null && !parameters.get("codigopresup").equals(""))
				unidadTO.setCodigopresup(parameters.get("codigopresup"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				unidadTO.setNombre(parameters.get("nombre").toUpperCase());
			if(parameters.get("estadoaprobado")!=null && !parameters.get("estadoaprobado").equals(""))
				unidadTO.setNpajusaprobado(Integer.valueOf(parameters.get("estadoaprobado")));
			Collection<UnidadTO> resultado=UtilSession.planificacionServicio.transConsultaplanificacion(unidadTO, Long.valueOf(parameters.get("ejerciciofiscal")), principal.getName());
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", (Integer.valueOf(resultado.size())).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,unidadTO.getJsonConfigPlanificado()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta nivel actividad y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaNivelactividad(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		NivelactividadTO nivelactividadTO=new NivelactividadTO();
		try{
			campo="descripcionexten";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				nivelactividadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				nivelactividadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			log.println("unidad: " + parameters.get("nivelactividadunidadid"));
			if(parameters.get("nivelactividadunidadid")!=null && !parameters.get("nivelactividadunidadid").equals(""))
				nivelactividadTO.setNivelactividadunidadid(Long.valueOf(parameters.get("nivelactividadunidadid")));
			if(parameters.get("nivelactividadejerfiscalid")!=null && !parameters.get("nivelactividadejerfiscalid").equals(""))
				nivelactividadTO.setNivelactividadejerfiscalid(Long.valueOf(parameters.get("nivelactividadejerfiscalid")));
			if(parameters.get("tipo")!=null && !parameters.get("tipo").equals(""))
				nivelactividadTO.setTipo(parameters.get("tipo"));
			if(parameters.get("nivelactividadpadreid")!=null && !parameters.get("nivelactividadpadreid").equals(""))
				nivelactividadTO.setNivelactividadpadreid(Long.valueOf(parameters.get("nivelactividadpadreid")));
			if(parameters.get("nodo")!=null && !parameters.get("nodo").equals(""))
				nivelactividadTO.setNphoja(Integer.valueOf(parameters.get("nodo")));
			nivelactividadTO.setEstado(MensajesWeb.getString("estado.activo"));
			nivelactividadTO.setUnidad(new UnidadTO());
			log.println("nivel actividad asignada: " + nivelactividadTO.getNivelactividadunidadid());
			//Collection<NivelactividadTO> resultado=UtilSession.planificacionServicio.transObtenerNivelactividad(nivelactividadTO);
			Collection<NivelactividadTO> resultado=UtilSession.planificacionServicio.transObtieneNivelactividadarbolact(nivelactividadTO,false);
			//Siempre que me llege la variable nivelactividad yo le asigno en una variable temporal
			if(parameters.get("actividadid")!=null) {
				for(NivelactividadTO nivel:resultado) {
					nivel.setNpactividadid(Long.valueOf(parameters.get("actividadid")));
				}
			}
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,nivelactividadTO.getJsonConfigActividadPlanifiacion()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta la matriz de presupuestos de la planificacion
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject matrizpresupuesto(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		try{
			Matriz matriz=new Matriz();
			
			Long unidad= Long.valueOf(parameters.get("unidad"));
			Long ejerciciofiscal=Long.valueOf(parameters.get("ejerciciofiscal")); 
			String tipoplanificacion=parameters.get("tipoplanificacion");
			
			matriz=UtilSession.planificacionServicio.transObtienematrizpresupuesto(unidad, ejerciciofiscal, tipoplanificacion);
			
//			HashMap<String, String>  totalMap=new HashMap<String, String>();
//			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
//			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,nivelactividadTO.getJsonConfigActividadPlanifiacion()));
			jsonObject.put("unidad", (JSONObject)JSONSerializer.toJSON(matriz.getMatrizUnidad()));
			jsonObject.put("cabecera", (JSONArray)JSONSerializer.toJSON(matriz.getMatrizCabecera()));
			jsonObject.put("detalle", (JSONArray)JSONSerializer.toJSON(matriz.getMatrizDetalle(),new MatrizDetalle().getJsonConfigpresupuesto()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}

	/**
	* Metodo que consulta la matriz de presupuestos de la planificacion
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject matrizmetas(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		try{
			Matriz matriz=new Matriz();
			
			Long unidad= Long.valueOf(parameters.get("unidad"));
			Long ejerciciofiscal=Long.valueOf(parameters.get("ejerciciofiscal")); 
			String tipoplanificacion=parameters.get("tipoplanificacion");
			log.println("tipoplanificacion..: " + parameters.get("tipoplanificacion"));
			matriz=UtilSession.planificacionServicio.transObtienematrizmetas(unidad, ejerciciofiscal, tipoplanificacion);
			
//			HashMap<String, String>  totalMap=new HashMap<String, String>();
//			totalMap.put("valor", Integer.valueOf(resultado.size()).toString());
//			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,nivelactividadTO.getJsonConfigActividadPlanifiacion()));
			jsonObject.put("unidad", (JSONObject)JSONSerializer.toJSON(matriz.getMatrizUnidad()));
			jsonObject.put("cabecera", (JSONArray)JSONSerializer.toJSON(matriz.getMatrizCabecera()));
			jsonObject.put("detalle", (JSONArray)JSONSerializer.toJSON(matriz.getMatrizDetalle(),new MatrizDetalle().getJsonConfigmetas()));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	
	/**
	* Metodo que hace la aprobacion de la planificacion
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static Collection<Map<String, String>> aprobacionplanificacion(Long unidad, Long ejerciciofiscal, String tipo, Long nivelactividadunidadid, Long npactividadid,JSONObject jsonObject) throws MyException {
		Collection<Map<String, String>> resultado=new ArrayList<>();
		try{
			if(tipo.equals("P"))
				resultado=UtilSession.planificacionServicio.transValidaaprobacion(unidad, ejerciciofiscal, true, npactividadid,nivelactividadunidadid);
			else
				resultado=UtilSession.planificacionServicio.transValidaaprobacion(unidad, ejerciciofiscal,false, npactividadid,nivelactividadunidadid);
		log.println("resultados%% "+resultado.size());
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return resultado;
	}
}
