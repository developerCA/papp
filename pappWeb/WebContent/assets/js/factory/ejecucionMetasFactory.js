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

        traerSubactividades: function(id, unidad, ejerciciofiscal) {
			var url = "planificacion/subactividadplanificacion/" + id + "/" +
        			"unidadid=" + unidad +
        			"&ejerciciofiscal=" + ejerciciofiscal;
			return Restangular.allUrl(url).customGET();
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

		traerRenovarSubtareas: function(actividad, ejerciciofiscal, mesDesde, mesHasta, subtactividadid, tareaid) {
			var url = "ejecucion/consultar/subtareasEjecucionMetas/";
			url += "institucionid=" + actividad.npInstitucionId;
			url += "&entidadid=" + actividad.npentidadid;
			url += "&unidadid=" + actividad.npunidad;
			url += "&ejerciciofiscalid=" + ejerciciofiscal;
			url += "&actividadid=" + actividad.id;
			if (mesDesde != null && mesDesde != "") url += "&mesdesde=" + mesDesde;
			if (mesHasta != null && mesHasta != "") url += "&meshasta=" + mesHasta;
			if (subtactividadid != null && subtactividadid != "") url += "&subtactividadid=" + subtactividadid;
			if (tareaid != null && tareaid != "") url += "&tareaid=" + tareaid;
			return Restangular.allUrl(url).customGET();
		},

		guardarLineSubtareas: function(objeto){
			var url = "administrar/ejecucionMetas/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);