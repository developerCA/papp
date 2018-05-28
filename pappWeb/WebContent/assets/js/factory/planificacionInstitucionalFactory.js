app.factory("planificacionInstitucionalFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traer : function(pagina, ejercicio) {
			var url = "planificacion/consultar/objetivo/" +
				"pagina=" + pagina +
				"&objetivoejerciciofiscalid=" + ejercicio +
				"&estado=A" +
				"&tipo=P";
			return Restangular.allUrl(url).getList();
		},

		traerHijos : function(pagina, ejercicio, padre, tipo) {
			var url = "planificacion/consultar/objetivo/" +
				"pagina=" + pagina +
				"&objetivoejerciciofiscalid=" + ejercicio +
				"&id=" + padre +
				"&estado=A" +
				"&tipo=" + tipo;
			return Restangular.allUrl(url).getList();
		},

		traerFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/objetivo/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerEditar : function(id) {
			var url = "planificacion/objetivo/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerNuevoEstructura : function(padreid, ejerciciofiscalid, tipopadre) {
			var url = "planificacion/nuevo/objetivo/"+padreid+"/"+ejerciciofiscalid+"/"+tipopadre;

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/objetivo/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
