app.factory("ejecucionMetasFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
        
        traerFiltro: function (pagina, ejercicio, codigo, nombre, estado) {
            var url = "planificacion/consultar/planificacion/pagina=" + pagina+ "&ejerciciofiscal=" + ejercicio;

            if (codigo != null && codigo != "") url += "&codigopresup=" + codigo.toUpperCase();
            if (nombre != null && nombre != "") url += "&nombre=" + nombre;
            if (estado != null && estado != "") url += "&estado=" + estado;

            return Restangular.allUrl(url).getList();
        },

        traerActividades: function(acitividadunidad, ejerciciofiscal) {
			var url = "ejecucion/actividadunidad/" + acitividadunidad + "/"+ ejerciciofiscal;
			return Restangular.allUrl(url).customGET();
		},

		traerRenovarActividades: function(institucionId, entidadId, unidadId, ejerciciofiscal, actividadId, mes) {
			var url = "ejecucion/consultar/actividadesEjecucionMetas/";
			url += "institucionid=" + institucionId;
			url += "&entidadid=" + entidadId;
			url += "&unidadid=" + unidadId;
			url += "&ejerciciofiscalid=" + ejerciciofiscal;
			if (actividadId != null && actividadId != "") url += "&actividadid=" + actividadId;
			if (mes != null && mes != "") url += "&mes=" + mes;
			return Restangular.allUrl(url).customGET();
		},

		guardarLineActividad: function(objeto, ejerciciofiscal){
			var url = "ejecucion/cronogramaactividades/" + ejerciciofiscal;
			return Restangular.allUrl(url).customPOST(objeto);
		},

//        traerSubactividades: function(id, unidad, ejerciciofiscal) {
//			var url = "planificacion/subactividadplanificacion/" + id + "/" +
//        			"unidadid=" + unidad +
//        			"&ejerciciofiscal=" + ejerciciofiscal;
//			return Restangular.allUrl(url).customGET();
//		},

		traerNiveles: function (tipo, padreid, unidad, ejercicio, actividadid) {
        	var url = "planificacion/consultar/nivelactividad/" +
    			"tipo=" + tipo +
    			"&nivelactividadejerfiscalid=" + ejercicio +
    			"&nivelactividadpadreid=" + padreid +
    			"&nivelactividadunidadid=" + unidad +
				"&estado=A" +
				"&actividadid=" + actividadid
			return Restangular.allUrl(url).getList();
        },

        traerTareas: function(id, unidad, ejerciciofiscal) {
			var url = "planificacion/tareaplanificacion/" + id + "/" +
        			"unidadid=" + unidad +
        			"&ejerciciofiscal=" + ejerciciofiscal;
			return Restangular.allUrl(url).customGET();
		},

        traerSubtareas: function(acitividadunidad, ejerciciofiscal) {
			var url = "ejecucion/actividadunidad/" + acitividadunidad + "/"+ ejerciciofiscal;
			return Restangular.allUrl(url).customGET();
		},

		traerRenovarSubtareas: function(institucionId, entidadId, unidadId, ejerciciofiscal, actividadId, mesDesde, mesHasta, subtactividadid, tareaid) {
			var url = "ejecucion/consultar/subtareasEjecucionMetas/";
			url += "institucionid=" + institucionId;
			url += "&entidadid=" + entidadId;
			url += "&unidadid=" + unidadId;
			url += "&ejerciciofiscalid=" + ejerciciofiscal;
			if (actividadId != null && actividadId != "") url += "&actividadid=" + actividadId;
			if (mesDesde != null && mesDesde != "") url += "&mesdesde=" + mesDesde;
			if (mesHasta != null && mesHasta != "") url += "&meshasta=" + mesHasta;
			if (subtactividadid != null && subtactividadid != "") url += "&subtactividadid=" + subtactividadid;
			if (tareaid != null && tareaid != "") url += "&tareaid=" + tareaid;
			return Restangular.allUrl(url).customGET();
		},

		guardarLineSubtarea: function(objeto, ejerciciofiscal){
			var url = "ejecucion/cronogramasubtareas/" + ejerciciofiscal;
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);