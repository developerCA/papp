package ec.com.papp.web.resource;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @autor(s): jcalderon
 * @fecha: 26-10-2015
 * @copyright: xcelsa
 * @version: 1.0
 * @descripcion Clase que obtiene los valores de las variables del archivo de propiedades
 */

public class MensajesWeb {
	
	/**
	* Obtiene el valor de las propiedades del archivo
	*
	* @param clave 
	*
	* @return valor de la clave del archivo de propiedades
	*/

static HashMap<String,String> claves = new HashMap<String,String>(); 
	
	static {
		try {
			String propiedades = "ec.com.papp.web.resource.mensajeWeb"; 
			ResourceBundle resource = ResourceBundle.getBundle(propiedades);
			Enumeration<String> enume = resource.getKeys();
			
			while (  enume.hasMoreElements() ) {
				String tmp = enume.nextElement();
				//System.out.println(tmp + " " + resource.getString(tmp) ); 
				claves.put(tmp,resource.getString(tmp));
		     }
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}//Cambia todos y prueba
	} 
	public static String getString(String clave) {
		return claves.get(clave);
	}	
	
	
}

