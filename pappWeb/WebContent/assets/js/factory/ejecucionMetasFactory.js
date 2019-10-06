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

        traerActividades : function(acitividadunidad, ejerciciofiscal) {
			var url = "ejecucion/actividadunidad/" + acitividadunidad + "/"+ ejerciciofiscal;
			return Restangular.allUrl(url).customGET();
		},

		traerRenovar : function(actividad, ejerciciofiscal, mes) {
			var url = "ejecucion/consultar/actividadesEjecucionMetas/";
			url += "institucionid=" + actividad.npInstitucionId;
			url += "&entidadid=" + actividad.npentidadid;
			url += "&unidadid=" + actividad.npunidad;
			url += "&ejeciciofiscalid=" + ejerciciofiscal;
			url += "&actividadid=" + actividad.id;
			if (mes != null && mes != "") url += "&mes=" + mes;
			return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "administrar/ejecucionMetas/";
			return Restangular.allUrl(url).customPOST(objeto);
		},

	}
} ]);