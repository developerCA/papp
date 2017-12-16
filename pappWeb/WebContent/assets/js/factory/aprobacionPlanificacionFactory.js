app.factory("AprobacionPlanificacionFactory", ["Restangular", function (Restangular) {

    var service = Restangular.service("/planificacion");

    return {

        traerAprobacionPlanificacion: function (pagina, ejercicio) {
        	return Restangular.allUrl("planificacion/consultar/planificacion/pagina=" + pagina + "&ejerciciofiscal=" + ejercicio).getList();
        },

        traerPlanificacionAnual: function (id, ejercicio) {
        	return Restangular.allUrl("planificacion/consultar/nivelactividad/" +
    			"tipo=AC" +
    			"&nivelactividadunidadid=" + id +
    			"&nivelactividadejerfiscalid=" + ejercicio).getList();
        },

        traerAprobacionPlanificacionCustom: function (pagina, ejercicio) {
        	 var url = "planificacion/consultar/planificacion/pagina=" + pagina + "&ejerciciofiscal=" + ejercicio;
             return Restangular.allUrl(url).customGET();
        },
        
        traerAprobacionPlanificacionFiltro: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).getList();
        },

        traerAprobacionPlanificacionFiltroCustom: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).customGET();
        },

        traerItem: function (id) {
            var url = "planificacion/planificacion/" + id + "/-1";
            return Restangular.allUrl(url).customGET();
        },

        guardar: function (objeto) {
            var url = "planificacion/planificacion/";
            return Restangular.allUrl(url).customPOST(objeto);
        },
    }
}]);