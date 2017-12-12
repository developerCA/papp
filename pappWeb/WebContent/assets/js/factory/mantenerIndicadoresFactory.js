app.factory("mantenerIndicadoresFactory", [ "Restangular", function(Restangular) {

	var service = Restangular.service("/planificacion");

	return {
		traerMantenerIndicadores : function(pagina, ejercicio) {
			var url = "planificacion/consultar/indicador/pagina=" + pagina + "&indicadorejerciciofiscalid=" + ejercicio;
			return Restangular.allUrl(url).getList();
		},

		traerMantenerIndicadoresHijos : function(pagina, ejercicio, padre) {
			var url = "planificacion/consultar/indicador/" +
					"pagina=" + pagina +
					"&indicadorejerciciofiscalid=" + ejercicio +
					"&id=" + padre;
			return Restangular.allUrl(url).getList();
		},

		traerMantenerIndicadoresFiltro : function(pagina,nombre) {
			var url = "planificacion/consultar/indicador/pagina="+pagina;

			if(nombre!=null && nombre != "") url += "&nombre=" + nombre;	

			return Restangular.allUrl(url).getList();
		},

		traerMantenerIndicadoresEditar : function(id) {
			var url = "planificacion/indicador/"+id+"/0";

		    return Restangular.allUrl(url).customGET();
		},

		traerMantenerIndicadoresNuevoEstructura : function(padreid, ejerciciofiscalid, tipopadre) {
			var url = "planificacion/nuevo/indicador/"+padreid+"/"+ejerciciofiscalid+"/"+tipopadre;

		    return Restangular.allUrl(url).customGET();
		},

		guardar:function(objeto){
			var url = "planificacion/indicador/";
			return Restangular.allUrl(url).customPOST(objeto);
		},
	}
} ]);
