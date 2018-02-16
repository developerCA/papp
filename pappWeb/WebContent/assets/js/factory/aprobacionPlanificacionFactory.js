app.factory("AprobacionPlanificacionFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/planificacion");

    return {
        traer: function (pagina, ejercicio) {
        	return Restangular.allUrl(
    			"planificacion/consultar/planificacion/" +
    			"pagina=" + pagina +
    			"&ejerciciofiscal=" + ejercicio +
				"&estado=A"
			).getList();
        },
        
        traerFiltro: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).getList();
        },

        cargarMatrizPresupuesto: function(unidad, ejerciciofiscal, tipoplanificacion) {
            var url = "planificacion/consultar/matrizpresupuestos/" +
        		"unidad=" + unidad +
        		"&ejerciciofiscal=" + ejerciciofiscal+
				"&tipoplanificacion=" + tipoplanificacion;
            return Restangular.allUrl(url).customGET();
        },

        cargarMatrizMetas: function(unidad, ejerciciofiscal, tipoplanificacion) {
            var url = "planificacion/consultar/matrizmetas/" +
        		"unidad=" + unidad +
        		"&ejerciciofiscal=" + ejerciciofiscal+
				"&tipoplanificacion=" + tipoplanificacion;
            return Restangular.allUrl(url).customGET();
        },

        editarAprobarPlanificacion: function(
    		unidad,
    		ejerfiscalid,
    		nivelactividadunidadid,
    		tipo,
    		accion
		) {
			var url = "planificacion/consultar/aprobar/" +
				"unidad=" + unidad +
				"&nivelactividadejerfiscalid=" + ejerfiscalid +
				"&nivelactividadunidadid=" + nivelactividadunidadid + 
				"&tipo=" + tipo + 
				"&accion=" + accion;

		    return Restangular.allUrl(url).customGET();
		},

        guardar: function(
    		tipo,
    		objeto
		) {
            var url = "planificacion/";
            if (tipo == "P") {
            	url += "matrizpresupuesto/";
            } else {
            	url += "matrizmetas/";
            }
            return Restangular.allUrl(url).customPOST(objeto);
        },
    }
}]);