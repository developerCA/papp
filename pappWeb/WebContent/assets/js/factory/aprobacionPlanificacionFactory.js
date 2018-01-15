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

        guardar: function (objeto) {
            var url = "planificacion/planificacion/";
            return Restangular.allUrl(url).customPOST(objeto);
        },
    }
}]);