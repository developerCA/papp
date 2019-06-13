package ec.com.papp.web.reportes.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ec.com.papp.web.reportes.util.ReportesConsultas;
import ec.com.xcelsa.utilitario.metodos.Log;

/**
 * @autor: jcalderon
 * @fecha: 27-10-2015
 * @copyright: Xcelsa
 * @version: 1.0
 * @descripcion Clase para realizar administraciones centralizadas de estructuraorganica
*/

@Controller
public class ReportesController {
	private Log log = new Log(ReportesController.class);
	
	@RequestMapping(value = "/rest/reportes/consultar/s01/{parametro}", method = RequestMethod.GET)
	public ModelAndView s01(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte s01 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarS01plano(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

	@RequestMapping(value = "/rest/reportes/consultar/s01bloque/{parametro}", method = RequestMethod.GET)
	public ModelAndView s01bloque(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte s01bloque ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarS01bloque(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

	@RequestMapping(value = "/rest/reportes/consultar/p01/{parametro}", method = RequestMethod.GET)
	public ModelAndView p01(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p01 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP01(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

	@RequestMapping(value = "/rest/reportes/consultar/p02/{parametro}", method = RequestMethod.GET)
	public ModelAndView p02(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p02 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP02(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

	@RequestMapping(value = "/rest/reportes/consultar/p03/{parametro}", method = RequestMethod.GET)
	public ModelAndView p03(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p03 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP03(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

	@RequestMapping(value = "/rest/reportes/consultar/p04/{parametro}", method = RequestMethod.GET)
	public ModelAndView p04(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p04 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP04(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}
	
	@RequestMapping(value = "/rest/reportes/consultar/p04pac/{parametro}", method = RequestMethod.GET)
	public ModelAndView p04pac(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p04pac ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP04pac(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}


	@RequestMapping(value = "/rest/reportes/consultar/p05/{parametro}", method = RequestMethod.GET)
	public ModelAndView p05(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte p05 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarP05(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

}
