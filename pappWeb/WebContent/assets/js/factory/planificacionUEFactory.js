app.factory("PlanificacionUEFactory", ["Restangular", function (Restangular) {

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

        traerPAactividad: function (id, ejercicio) {
        	return Restangular.allUrl("planificacion/consultar/nivelactividad/" +
    			"tipo=AC" +
    			"&nivelactividadunidadid=" + id +
    			"&nivelactividadejerfiscalid=" + ejercicio +
				"&estado=A"
			).getList();
        },

        traerPAhijos: function (tipo, id, unidad, ejercicio) {
        	var url = "planificacion/consultar/nivelactividad/" +
    			"tipo=" + tipo +
    			"&nivelactividadpadreid=" + id +
    			"&nivelactividadejerfiscalid=" + ejercicio +
    			"&nivelactividadunidadid=" + unidad +
				"&estado=A"
			return Restangular.allUrl(url).getList();
        },

        traerPAverActividad: function (id, unidad, ejercicio) {
        	return Restangular.allUrl("planificacion/actividadplanificacion/" +
    			id + "/" +
    			"unidadid=" + unidad +
    			"&ejerciciofiscal=" + ejercicio
			).getList();
        },

        traerCustom: function (pagina, ejercicio) {
        	 var url = "planificacion/consultar/planificacion/pagina=" + pagina + "&ejerciciofiscal=" + ejercicio;
             return Restangular.allUrl(url).customGET();
        },
        
        traerFiltro: function (pagina, ejercicio, codigo, nombre) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;

            return Restangular.allUrl(url).getList();
        },

        traerFiltroCustom: function (pagina, ejercicio, codigo, nombre) {
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