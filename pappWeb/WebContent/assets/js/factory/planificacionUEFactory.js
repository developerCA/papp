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

        traerPAhijos: function (tipo, id, unidad, ejercicio, actividadid) {
        	var url = "planificacion/consultar/nivelactividad/" +
    			"tipo=" + tipo +
    			"&nivelactividadpadreid=" + id +
    			"&nivelactividadejerfiscalid=" + ejercicio +
    			"&nivelactividadunidadid=" + unidad +
				"&estado=A" +
				"&actividadid=" + actividadid
			return Restangular.allUrl(url).getList();
        },

        traerPAverActividad: function (id, unidad, ejercicio) {
        	return Restangular.allUrl("planificacion/actividadplanificacion/" +
    			id + "/" +
    			"unidadid=" + unidad +
    			"&ejerciciofiscal=" + ejercicio
			).customGET();
        },

        traerPAverSubActividad: function (tablarelacionid, nivelactividadunidadid) {
        	return Restangular.allUrl("planificacion/subactividadplanificacion/" +
    			tablarelacionid + "/" +
    			nivelactividadunidadid
			).customGET();
        },

        traerPAverTarea: function (tablarelacionid, nivelactividadunidadid) {
        	return Restangular.allUrl("planificacion/tareaplanificacion/" +
    			tablarelacionid + "/" +
    			nivelactividadunidadid
			).customGET();
        },

        traerPAverSubTarea: function (tablarelacionid, nivelactividadunidadid) {
        	return Restangular.allUrl("planificacion/subtareaplanificacion/" +
    			tablarelacionid + "/" +
    			nivelactividadunidadid
			).customGET();
        },

        traerPAverItem: function (tablarelacionid, nivelactividadunidadid) {
        	return Restangular.allUrl("planificacion/itemplanificacion/" +
    			tablarelacionid + "/" +
    			nivelactividadunidadid
			).customGET();
        },

        traerPAverSubItem: function (tablarelacionid, nivelactividadunidadid) {
        	return Restangular.allUrl("planificacion/subitemplanificacion/" +
    			tablarelacionid + "/" +
    			nivelactividadunidadid
			).customGET();
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

        nuevo: function(tipo, id, ejerciciofiscal) {
            var url = "planificacion/nuevo/" +
        		this.toTipo(tipo) +"/" +
        		id + "/" +
        		ejerciciofiscal;
            return Restangular.allUrl(url).customGET();
        },

        editar: function(tipo, id, unidad) {
            var url = "planificacion/" +
        		this.toTipo(tipo) +"/" +
        		id + "/" +
				unidad;
            return Restangular.allUrl(url).customGET();
        },

        editarMDP: function(
    		actividad,
    		actividadunidadacumulador,
    		unidad,
    		tipo,
    		tipometa,
    		afiscal
		) {
            var url = "planificacion/cronograma/" +
        		actividad +"/" +
        		actividadunidadacumulador + "/" +
				unidad + "/" +
				tipo + "/" +
				tipometa + "/" +
				afiscal;
            return Restangular.allUrl(url).customGET();
        },

        guardarActividades: function(tipo, objeto) {
            var url = "planificacion/" + this.toTipo(tipo) + "/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        guardarMetaDistribucionPlanificada: function(objeto) {
            var url = "planificacion/cronograma/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        guardarMetaDistribucionAjustada: function(objeto) {
            var url = "planificacion/planificacion/";
            return Restangular.allUrl(url).customPOST(objeto);
        },

        toTipo: function(tipo) {
        	switch (tipo) {
        		case "AC":
        			return "actividadunidad";
        		case "SA":
        			return "subactividadunidad";
        		case "TA":
        			return "tareaunidad";
        		case "ST":
        			return "subtareaunidad";
        		case "IT":
        			return "itemunidad";
        		case "SI":
        			return "subitemunidad";
        		default:
        			return "";
			}
        }
    }
}]);