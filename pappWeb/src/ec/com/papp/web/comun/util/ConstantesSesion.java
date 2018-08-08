package ec.com.papp.web.comun.util;


/**
 * @autor(s): jacalderon
 * @fecha: 29-10-2015
 * @version: 1.0
 * @descripcion Clase que contiene las variables de las constantes para sesion
 */


public interface ConstantesSesion {

    /*PAQUETES*/
    public final static String BASE_SESSION = "ec_com_utilitario";
    public final static String SESSION_MANAGER = "sm_utilitario";

    /*VARIABLES DE SESION*/    
    public final static String USUARIO_LOGIN					= SESSION_MANAGER  + "_usuarioLogin";

    public final static String USUARIO_PERMISOS					= SESSION_MANAGER  + "_permisosusuario";
    /*VALOR ANTIGUO PARA AUDITORIA*/    
    public final static String VALORANTIGUO						= BASE_SESSION  + "_valorantiguo";
    public final static String USUARIO_CAMBIARCLAVE				= SESSION_MANAGER  + "_cambiarclave";
    
}