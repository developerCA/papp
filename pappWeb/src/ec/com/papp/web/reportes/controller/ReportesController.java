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
	public ModelAndView sueldofechas(HttpServletRequest request,HttpServletResponse response,@PathVariable String parametro) throws Throwable {
		try {
			System.out.println("ingresa a generar el reporte s01 ");
			String[] pares = parametro.split("&");
			Map<String, String> parameters = new HashMap<String, String>();
			for(String pare : pares) {
			    String[] nameAndValue = pare.split("=");
			    parameters.put(nameAndValue[0], nameAndValue[1]);
			}
			ReportesConsultas.generarXcelSueldosparticipes(request, response, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			log.println("error al cargaro no adheridos actuario");
		}
		return null;
	}

}
