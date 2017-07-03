package ec.com.papp.web.seguridad.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.tools.commons.to.OrderBy;
import org.hibernate.tools.commons.to.SearchResultTO;

import ec.com.papp.estructuraorganica.to.UnidadTO;
import ec.com.papp.estructuraorganica.to.UsuariounidadTO;
import ec.com.papp.seguridad.to.MenuTO;
import ec.com.papp.seguridad.to.PerfilTO;
import ec.com.papp.seguridad.to.PerfilpermisoTO;
import ec.com.papp.seguridad.to.PermisoTO;
import ec.com.papp.seguridad.to.PermisoobjetoTO;
import ec.com.papp.seguridad.to.UsuarioTO;
import ec.com.papp.web.comun.util.UtilSession;
import ec.com.xcelsa.utilitario.exception.MyException;
import ec.com.xcelsa.utilitario.metodos.Log;

public class ConsultasUtil {

	
	private static Log log = new Log(ConsultasUtil.class);
	
	/**
	* Metodo que consulta menu arma el json para mostrarlos en grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaMenu(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		MenuTO menuTO=new MenuTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			menuTO.setFirstResult(primero);
			menuTO.setMaxResults(filas);
			if(parameters.get("id")!=null && !parameters.get("id").equals(""))
				menuTO.setId(Long.valueOf(parameters.get("id")));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				menuTO.setNombre(parameters.get("nombre"));
			if(parameters.get("orden")!=null && !parameters.get("orden").equals(""))
				menuTO.setOrden(Double.valueOf(parameters.get("orden")));
//			if(parameters.get("nppadre")!=null && !parameters.get("nppadre").equals(""))
//				menuTO.setNpnombre2(parameters.get("nppadre"));
			menuTO.setPermiso(new PermisoTO());
			Collection<MenuTO> resultado=UtilSession.seguridadServicio.transObtenerMenu(menuTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", (Integer.valueOf(resultado.size()).toString()));
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado,menuTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta perfil arma el json para mostrarlos en arbol
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPerfil(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		PerfilTO perfilTO=new PerfilTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			perfilTO.setFirstResult(primero);
			perfilTO.setMaxResults(filas);
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				perfilTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				perfilTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				perfilTO.setNombre(parameters.get("nombre"));
			SearchResultTO<PerfilTO> resultado=UtilSession.seguridadServicio.transObtenerPerfilPaginado(perfilTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),perfilTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta perfilpermiso y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPerfilpermiso(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		PerfilpermisoTO perfilpermisoTO=new PerfilpermisoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			perfilpermisoTO.setFirstResult(primero);
			perfilpermisoTO.setMaxResults(filas);
			campo="id.perfilid";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				perfilpermisoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				perfilpermisoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("perfilid")!=null && !parameters.get("perfilid").equals(""))
				perfilpermisoTO.getId().setPerfilid(Long.valueOf(parameters.get("perfilid")));
			if(parameters.get("permisoid")!=null && !parameters.get("permisoid").equals(""))
				perfilpermisoTO.getId().setPermisoid(Long.valueOf(parameters.get("permisoid")));
			SearchResultTO<PerfilpermisoTO> resultado=UtilSession.seguridadServicio.transObtenerPerfilpermisoPaginado(perfilpermisoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),perfilpermisoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta permiso arma el json para mostrarlos en grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPermiso(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		PermisoTO permisoTO=new PermisoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			permisoTO.setFirstResult(primero);
			permisoTO.setMaxResults(filas);
			campo="nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				permisoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				permisoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("descripcion")!=null && !parameters.get("descripcion").equals(""))
				permisoTO.setDescripcion(parameters.get("descripcion"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				permisoTO.setNombre(parameters.get("nombre"));
			SearchResultTO<PermisoTO> resultado=UtilSession.seguridadServicio.transObtenerPermisoPaginado(permisoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),permisoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta permisoobjeto y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaPermisoobjeto(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		PermisoobjetoTO permisoobjetoTO=new PermisoobjetoTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			permisoobjetoTO.setFirstResult(primero);
			permisoobjetoTO.setMaxResults(filas);
			campo="id.objetoid";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				permisoobjetoTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				permisoobjetoTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("objetoid")!=null && !parameters.get("objetoid").equals(""))
				permisoobjetoTO.getId().setObjetoid(Long.valueOf(parameters.get("objetoid")));
			if(parameters.get("permisoid")!=null && !parameters.get("permisoid").equals(""))
				permisoobjetoTO.getId().setPermisoid(Long.valueOf(parameters.get("permisoid")));
			SearchResultTO<PermisoobjetoTO> resultado=UtilSession.seguridadServicio.transObtenerPermisoobjetoPaginado(permisoobjetoTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),permisoobjetoTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta usuario y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUsuario(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UsuarioTO usuarioTO=new UsuarioTO();
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			usuarioTO.setFirstResult(primero);
			usuarioTO.setMaxResults(filas);
			campo="usuario";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				usuarioTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				usuarioTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("usuario")!=null && !parameters.get("usuario").equals(""))
				usuarioTO.setUsuario(parameters.get("usuario"));
			if(parameters.get("nombremostrado")!=null && !parameters.get("nombremostrado").equals(""))
				usuarioTO.getSocionegocio().setNombremostrado(parameters.get("nombremostrado"));
			SearchResultTO<UsuarioTO> resultado=UtilSession.seguridadServicio.transObtenerusuarioPaginado(usuarioTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),usuarioTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
	
	/**
	* Metodo que consulta usuariounidad y arma el json para mostrarlos en la grilla
	*
	* @param request 
	* @return JSONObject Estructura que contiene los valores para armar la grilla
	* @throws MyException
	*/

	public static JSONObject consultaUsuariounidad(Map<String, String> parameters,JSONObject jsonObject) throws MyException {
		String campo="";
		UsuariounidadTO usuariounidadTO=new UsuariounidadTO();
		usuariounidadTO.setUnidadTO(new UnidadTO());
		try{
			int pagina=1;
			if(parameters.get("pagina")!=null)		
				pagina=(Integer.valueOf(parameters.get("pagina"))).intValue();
			int filas=20;
			if(parameters.get("filas")!=null)
				filas=(Integer.valueOf(parameters.get("filas"))).intValue();
			int primero=(pagina*filas)-filas;
			usuariounidadTO.setFirstResult(primero);
			usuariounidadTO.setMaxResults(filas);
			campo="unidadTO.nombre";
			String[] columnas={campo};
			if(parameters.get("sidx")!=null && !parameters.get("sidx").equals(""))
				campo=parameters.get("sidx");
			String[] orderBy = columnas;
			if(parameters.get("sord")!=null && parameters.get("sord").equals("desc"))
				usuariounidadTO.setOrderByField(OrderBy.orderDesc(orderBy));
			else
				usuariounidadTO.setOrderByField(OrderBy.orderAsc(orderBy));
			if(parameters.get("codigopresup")!=null && !parameters.get("codigopresup").equals(""))
				usuariounidadTO.getUnidadTO().setCodigopresup(parameters.get("codigopresup"));
			if(parameters.get("nombre")!=null && !parameters.get("nombre").equals(""))
				usuariounidadTO.getUnidadTO().setNombre(parameters.get("nombre"));
			usuariounidadTO.setUsuarioTO(new UsuarioTO());
			SearchResultTO<UsuariounidadTO> resultado=UtilSession.estructuraorganicaServicio.transObtenerUsuariounidadPaginado(usuariounidadTO);
			HashMap<String, String>  totalMap=new HashMap<String, String>();
			totalMap.put("valor", resultado.getCountResults().toString());
			jsonObject.put("result", (JSONArray)JSONSerializer.toJSON(resultado.getResults(),usuariounidadTO.getJsonConfig()));
			jsonObject.put("total", (JSONObject)JSONSerializer.toJSON(totalMap));
		}catch (Exception e) {
			e.printStackTrace();
			throw new MyException(e);
		}
		return jsonObject;
	}
}
