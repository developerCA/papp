app.factory("mantenerIndicadoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traer : function(pagina, ejercicio) {
			var url = "planificacion/consultar/indicador/" +
				"pagina=" + pagina +
				"&indicadorejerciciofiscalid=" + ejercicio +
				"&estado=A";
			return Restangular.allUrl(url).getList();
		},

		traerHijos : function(pagina, ejercicio, padre) {
			var url = "planificacion/consultar/indicador/" +
				"pagina=" + pagina +
				"&indicadorejerciciofiscalid=" + ejercicio +
				"&id=" + padre +
				"&estado=A";
//			var url = "planificacion/indicador/" + padre + "/0/0";
			return Restangular.allUrl(url).getList();
		},

		traerFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/indicador/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerEditar : function(id) {
			var url = "planificacion/indicador/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/indicador/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
