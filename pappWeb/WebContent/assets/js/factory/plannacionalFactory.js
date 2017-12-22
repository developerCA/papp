app.factory("plannacionalFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traer : function(pagina, ejercicio) {
			var url = "planificacion/consultar/plannacional/" +
				"pagina=" + pagina +
				"&plannacionalejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerHijos : function(pagina, ejercicio, padre) {
			var url = "planificacion/consultar/plannacional/" +
				"pagina=" + pagina +
				"&plannacionalejerciciofiscalid=" + ejercicio +
				"&id=" + padre +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/plannacional/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerEditar : function(id) {
			var url = "planificacion/plannacional/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/plannacional/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
